import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

public class CuttingAlgorithmUtil {

    //calculates temperature given CGpercentage, length, and NAconcentration
    //TODO get rid of min and max modes, they do the same thing
    public static Integer tempCalculator(Float CGpercentage, Integer length, Float NaConcentration, String Mode) {

        int temp;

        if(Mode.equals("Min")) {
            //temp equation for lengths less than 18
            if (length < 18) {
                temp = (int) Math.round((2 * length * (CGpercentage + 1)) + (16.6 * Math.log10(NaConcentration / 0.05)));
            }
            //temp equation for lengths greater than or equal to 18
            else {
                temp = (int) Math.round(((-820) / length) + 100.5 + (41 * CGpercentage) + (16.6 * Math.log10(NaConcentration)));
            }
        }
        else if (Mode.equals("Max")) {
            //temp equation for lengths less than 18
            if (length < 18) {
                temp = (int) Math.round((2 * length * (CGpercentage + 1)) + (16.6 * Math.log10(NaConcentration / 0.05)));
            }
            //temp equation for lengths greater than or equal to 18
            else {
                temp = (int) Math.round(((-820) / length) + 100.5 + (41 * CGpercentage) + (16.6 * Math.log10(NaConcentration)));
            }
        }
        else {
            throw new IllegalArgumentException("Mode must either be \"Min\" or \"Max\"");
        }
        return temp;
    }
    
    
    /*
         2D integer array that stores the length an overlap would have to be given
         all desired melting temperatures and starting location.
         */
    //TODO make sure that min/max mode compatibility is correctly implemented
    public static Integer[][] overlapCalculator(String TempSequence, Integer minTemp, Integer maxTemp, Integer maxLen, String Mode) {

        //Mode must be either 'Min' or 'Max'
        if (!(Mode.equals("Min") || Mode.equals("Max"))) {
            throw new IllegalArgumentException("Mode must either be \"Min\" or \"Max\"");
        }
        

        //2D integer array that stores the lengths of oligos given temperature and starting location
        Integer[][] lengthOverlapArray = new Integer[TempSequence.length()][maxTemp - minTemp];

        Integer numTimesCalcUsed = 0;

        //goes through the entire lengthOverlapArray, populating it
        for (int tempIndex = 0; tempIndex < maxTemp - minTemp; tempIndex++) {
            //System.out.println("Finding overlaps for temp: " + (minTemp + tempIndex));

            //minimum and maximum allowable overlap lengths
            final Integer MIN_OVERLAP_LEN = 12;
            final Integer MAX_OVERLAP_LEN = maxLen;

            //the target temperature
            Integer targetTemp = tempIndex + minTemp;
            //the calculated temperature
            Integer calculatedTemp = null;
            for (int DNAindex = 0; DNAindex < TempSequence.length(); DNAindex++) {
                Boolean Continue = true;
                Integer currOverlapLen = 0;
                Integer numCG = 0;
                Float CGpercentage;

                //calculates the length the hard way when there is no previous location length data to use as an estimate
                //TODO make algorithm more efficient by using previously calculated length as best estimate for next pos



                while (Continue) {

                    //the current overlap length can never exceed the maximum overlap length
                    if (currOverlapLen == MAX_OVERLAP_LEN) {
                        Continue = false;
                        lengthOverlapArray[DNAindex][tempIndex] = -1;
                        //System.out.println("Cant continue, overlap length exceeds maximum overlap lenght.");
                    }
                    //the DNAindex - currOverlapLen cannot go beyond the front end of the DNA sequence
                    else if ((DNAindex - currOverlapLen) <= 0) {
                        Continue = false;
                        lengthOverlapArray[DNAindex][tempIndex] = -2;
                        //System.out.println("Cant continue, overlap length + position cannot exceed length of DNA sequence.");
                        //System.out.println("Position: "  + DNAindex + " overlapLen: " + currOverlapLen);


                    }
                    //the current overlap length cannot be smaller than the minimum overlap length
                    else if (currOverlapLen < MIN_OVERLAP_LEN) {
                        currOverlapLen++;
                        if (TempSequence.charAt(DNAindex - currOverlapLen) == '1') {
                            numCG++;
                        }
                    } else {
                        currOverlapLen++;

                        if (TempSequence.charAt(DNAindex - currOverlapLen) == '1') {
                            numCG++;
                        }
                        CGpercentage = (float) numCG / currOverlapLen;
                        numTimesCalcUsed++;

                        if (Mode == "Min") {
                            calculatedTemp = tempCalculator(CGpercentage, currOverlapLen, 0.05F, "Min");
                            //System.out.println("Index: " + DNAindex + "calculated temp is: " + calculatedTemp + " Desired temp is: " + (minTemp + tempIndex));
                            //TODO figure out if this is the correct statement
                            //checks if calculated temperature is the correct temperature
                            if (calculatedTemp >= targetTemp) {
                                lengthOverlapArray[DNAindex][tempIndex] = currOverlapLen;
                                //System.out.println("Stopping, length for temperature " + (minTemp + tempIndex) + " at postion " + (DNAindex) + ": " + currOverlapLen);
                                Continue = false;
                            }
                        } //if mode is max
                        else {
                            //stores temp of previous overlap length for later

                            Integer prevCalculatedTemp = calculatedTemp;

                            //calculates the temperature of the current overlap length
                            calculatedTemp = tempCalculator(CGpercentage, currOverlapLen, 0.05F, "Max");



                            //determines the number of C/G for the previous overlap length
                            Integer prevNumCG = numCG;
                            if (TempSequence.charAt(DNAindex - currOverlapLen) == '1') {
                                prevNumCG--;
                            }

                            float prevCGpercentage = (float) prevNumCG / (currOverlapLen - 1);

                            //if the temp at the current overlap length is greater than the target temp,
                            //Then if the temp at previous overlap length is less than or equal to the target temp,
                            //(or null in the case that there is no previous length) store the previous
                            //overlap length in the lengthOverlapArray
                            if (calculatedTemp > targetTemp) {
                                if (prevCalculatedTemp <= targetTemp || prevCalculatedTemp == null) {
                                    lengthOverlapArray[DNAindex][tempIndex] = currOverlapLen;
                                    Continue = false;
                                }
                            }

                        }
                    }
                }
            }
        }


        System.out.println("The temperature calculator was used " + numTimesCalcUsed + " times.");
        Integer numElements = lengthOverlapArray.length + lengthOverlapArray[0].length;
        System.out.println("There are " + numElements + " elements in the length array.");
        System.out.println("Thats an average of " + ((float) numTimesCalcUsed / numElements) + " calculator usages per element");
        return lengthOverlapArray;

    }

    public static void printLengthCalculator (Integer[][] lengthCalculatorValue, Integer minTemp, Integer maxTemp) {
        System.out.print("Curr DNA index:  ");
        for(int currDnaIndex = 0; currDnaIndex < lengthCalculatorValue.length; currDnaIndex++){
            Formatter formatter = new Formatter();
            formatter.format("%-6.0f", (float) currDnaIndex);
            System.out.print(formatter + " ");
        }

        System.out.print("\n_________________");
        for(int currTempIndex = minTemp; currTempIndex < maxTemp; currTempIndex++){
            System.out.print("_______");
        }
        System.out.println();

        for(int tempIndex = 0; tempIndex < maxTemp - minTemp; tempIndex ++) {
            float currTemp = ((float) minTemp + tempIndex);
            Formatter formatter = new Formatter();
            formatter.format("%3.0f", currTemp);
            System.out.print("    " + formatter + "  C  |    ");

            for (int currDnaIndex = 0; currDnaIndex < lengthCalculatorValue.length; currDnaIndex++) {
                Formatter formatter1 = new Formatter();
                Integer currLen = lengthCalculatorValue[currDnaIndex][tempIndex];
                if (currLen == null) {
                    System.out.print("null   ");
                } else {
                    formatter1.format("%-6.0f", (float) currLen);
                    System.out.print(formatter1 + " ");
                }
            }
            System.out.println();
        }
    }
    
    //function that performs the dynamically calculated cost optimisation hozimiwatsit.
    public static  Float[] costCalculator (String[] DNAsequence, ArrayList<Amino> altAminoList, Integer[][] minLenData, Integer[][] maxLenData, Integer minLen, Integer maxLen, Integer minTemp, Integer maxTemp, Float costOfBase, Float costOfDegenerateBase) {

        final Integer lenRange = maxLen - minLen;
        final Integer tempRange = maxTemp - minTemp;
        final Integer NUM_NUKES = DNAsequence.length * 3;
        //keaps track of minimum cost for each position
        Float[][] costArray = new Float[NUM_NUKES][tempRange];
        

        for (Float[] tempIndex : costArray) {
            //all entries in costArray are initialized to positive infinity
            Arrays.fill(tempIndex, Float.POSITIVE_INFINITY);
            
        }
        
        

        for (int tempIndex = 0; tempIndex < tempRange; tempIndex++) {
            //the first element of each temperature row are re-initialized to 0
            costArray[0][tempIndex] = 0F;

            //the costs of all the lengths able to be reached by an oligo starting at 0 are calculated
            for(int currLen = minLen; currLen < maxLen; currLen++) {
                
                Float cost = oligoCost(costOfBase,costOfDegenerateBase, altAminoList, 0, currLen);
                costArray[currLen][tempIndex] = cost;

            }
            
            //goes through each reachable indx (index that is not at +infinity) and calculates the next oligo cost
            //if the current oligo ended at the given position
            for(int currDnaIndex = 1; currDnaIndex < NUM_NUKES; currDnaIndex++) {
                float prevOligoCost = costArray[currDnaIndex][tempIndex];
                if(prevOligoCost != Float.POSITIVE_INFINITY) {
                    Integer minOverlapSize = minLenData[currDnaIndex][tempIndex];
                    Integer maxOverlapSize = maxLenData[currDnaIndex][tempIndex];
                    
                    
                    //--------------------------->Oligo(A)
                    //                <-----------Overlap(A.1)
                    //                --------------------------------------->
                    //                ---------------------------------------->
                    //                ----------------------------------------->
                    //                ...
                    //                ------------------------------------------------------>
                    //                ------------------------------------------------------->
                    //                 <----------Overlap(A.2)
                    //                 --------------------------------------->
                    //                 ---------------------------------------->
                    //                 ----------------------------------------->
                    //                 ...
                    //                 ------------------------------------------------------>
                    //                 ------------------------------------------------------->
                    //                  <---------Overlap(A.3)  
                    //                  --------------------------------------->
                    //                  ---------------------------------------->
                    //                  ----------------------------------------->
                    //                  ...
                    //                  ------------------------------------------------------>
                    //                  ------------------------------------------------------->
                    //                                .
                    //                                .
                    //                                .
                    //                         <--Overlap(A.7)
                    //                         --------------------------------------->
                    //                         ---------------------------------------->
                    //                         ----------------------------------------->
                    //                         ...
                    //                         ------------------------------------------------------>
                    //                         ------------------------------------------------------->
                    
                    //goes through every possible overlap.
                    for(int currOverlap = minOverlapSize; currOverlap < maxOverlapSize; currOverlap++) {
                        //goes through every possible length at given overlap, calculating cost
                        for(int currLen = minLen; currLen < maxLen; currLen++) {
                            Integer startIndex = currDnaIndex - currOverlap;
                            Integer endIndex = currDnaIndex - currOverlap + currLen;
                            
                            //makes sure to keep al indexes in bounds
                            if(endIndex < NUM_NUKES) {
                                float currOligoCost = prevOligoCost + oligoCost(costOfBase,costOfDegenerateBase, altAminoList, startIndex, endIndex);

                                if(currOligoCost < costArray[endIndex][tempIndex]) {
                                    costArray[endIndex][tempIndex] = currOligoCost;
                                }
                            }
                            
                        }
                    }
                }
            }
        }
        printCostArray(costArray, minTemp, maxTemp);
        return null;
    }

    private static void printCostArray(Float[][] costArray, Integer minTemp, Integer maxTemp) {
        System.out.print("Curr DNA index:  ");
        for(int currDnaIndex = 0; currDnaIndex < costArray.length; currDnaIndex++){
            Formatter formatter = new Formatter();
            formatter.format("%-6.0f", (float) currDnaIndex);
            System.out.print(formatter + " ");
        }
        System.out.println();
        for(int tempIndex = 0; tempIndex < maxTemp - minTemp; tempIndex ++) {
            float currTemp = ((float) minTemp + tempIndex);
            Formatter formatter = new Formatter();
            formatter.format("%3.0f", currTemp);
            System.out.print("    " + formatter + "  C  |    ");

            for (int currDnaIndex = 0; currDnaIndex < costArray.length; currDnaIndex++) {

                if (costArray[currDnaIndex][tempIndex] == Float.POSITIVE_INFINITY) {
                    char infinity = '\u221E';
                    System.out.print(infinity + "      ");

                } else {
                    Formatter formatter1 = new Formatter();
                    float currCost = costArray[currDnaIndex][tempIndex];
                    formatter1.format("%-6.0f", currCost);
                    System.out.print(formatter1 + " ");
                }
            }
            System.out.println();
        }
    }

    public static Float oligoCost(Float costOfBase, Float costOfDegenerateBase, ArrayList<Amino> altAminoList 
            , Integer startPos, Integer endPos) {
        //TODO make cost algorithm more efficient
        Integer numBases = (endPos - startPos);
        Integer numDegenerateBases = 0;
        
        //calculating the number of degenerate codons
        for (int currCodon = 0; currCodon < altAminoList.size(); currCodon++) {
            if (startPos <= altAminoList.get(currCodon).getLocation() && altAminoList.get(currCodon).getLocation() <= endPos) {
                //TODO confirm if true
                //The minus 1 is there since one of the degenerate codons are accounted for, I thik
                numDegenerateBases += altAminoList.get(currCodon).getData().length() - 1;
            }
        }
        Float cost = (numBases * costOfBase) + (numDegenerateBases * costOfDegenerateBase);
            
        return cost;
        }
    }


