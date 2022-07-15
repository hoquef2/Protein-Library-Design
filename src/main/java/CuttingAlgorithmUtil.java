import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

public class CuttingAlgorithmUtil {

    //calculates temperature given CGpercentage, length, and NAconcentration
    //TODO determine if flooring and ceiling are associated with the correct mode
    public static Integer tempCalculator(Float CGpercentage, Integer length, Float NaConcentration, String Mode) {

        int temp;

        if(Mode.equals("Min")) {
            //temp equation for lengths less than 18
            if (length < 18) {
                temp = (int) Math.ceil((2 * length * (CGpercentage + 1)) + (16.6 * Math.log10(NaConcentration / 0.05)));
            }
            //temp equation for lengths greater than or equal to 18
            else {
                temp = (int) Math.ceil(((-820) / length) + 100.5 + (41 * CGpercentage) + (16.6 * Math.log10(NaConcentration)));
            }
        }
        else if (Mode.equals("Max")) {
            //temp equation for lengths less than 18
            if (length < 18) {
                temp = (int) Math.floor((2 * length * (CGpercentage + 1)) + (16.6 * Math.log10(NaConcentration / 0.05)));
            }
            //temp equation for lengths greater than or equal to 18
            else {
                temp = (int) Math.floor(((-820) / length) + 100.5 + (41 * CGpercentage) + (16.6 * Math.log10(NaConcentration)));
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
    public static Integer[][] LengthCalculator(String TempSequence, Integer minTemp, Integer maxTemp, Integer maxLen, String Mode) {

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
        Float[][] costArray = new Float[NUM_NUKES][tempRange];
        
        for (Float[] tempIndex : costArray) {
            //all entries in costArray are initialized to positive infinity
            Arrays.fill(tempIndex, Float.POSITIVE_INFINITY);
            
        }




        //for every temperature
        for (Integer tempIndex = 0; tempIndex < tempRange; tempIndex++) {
            
            //computing cost associated with first oligonucleotide

            //for every starting position
            //dnaIndex= starting point of currentOligo
            for(int dnaIndex = 0; dnaIndex < DNAsequence.length; dnaIndex++) {
                if(dnaIndex == 0) {
                    costArray[0][tempIndex] = 0F;
                }  
                //for every length
                //lengthIndex = ending point of currentOligo
                if(costArray[dnaIndex][tempIndex] != Float.POSITIVE_INFINITY) {
                    for(int lengthIndex = 0; lengthIndex < maxLen - minLen; lengthIndex++) {

                        //the ending position of the current oligo being examined
                        Integer endingPos = dnaIndex + lengthIndex;
                        //cost of oligo is the cost at the start of the oligo + the cost of the oligo
                        Float currCost = oligoCost(costOfBase, costOfDegenerateBase, altAminoList, dnaIndex, endingPos) + costArray[dnaIndex][tempIndex];

                        if(costArray[dnaIndex + lengthIndex][tempIndex] < currCost) {
                            costArray[dnaIndex + lengthIndex][tempIndex] = currCost;
                        }
                        
                        
                        //for every overlap length that is possible, populate the starting point of the overlap, if it at a minimum.
                        Integer minOverlapLen = minLenData[lengthIndex][tempIndex];
                        Integer maxOverlapLen = maxLenData[lengthIndex][tempIndex];
                        for(int currOverlap = minOverlapLen; currOverlap < maxOverlapLen; currOverlap++) {

                            if(currCost < costArray[lengthIndex - currOverlap][tempIndex]) {
                                costArray[endingPos - currOverlap][tempIndex] = currCost;
                            }
                        }
                        
                    }
                }
               
            }
            
            
            
            Integer currTemp = minLen + tempIndex;
            
            //Initializing all positions with a cost of infinity
            
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


