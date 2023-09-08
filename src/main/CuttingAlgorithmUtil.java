import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;

public class CuttingAlgorithmUtil {

    // calculates temperature given CGpercentage, length, and NAconcentration
    // TODO get rid of min and max modes, they do the same thing
    public static Integer tempCalculator(Float CGpercentage, Integer length, Float NaConcentration, String Mode) {

        int temp;

        // temp equation for lengths less than 18
        if (length < 18) {
            temp = (int) Math.round((2 * length * CGpercentage + 4*length*(1-CGpercentage)+ (16.6 * Math.log10(NaConcentration / 0.05))));
        }
        // temp equation for lengths greater than or equal to 18
        else {
            temp = (int) Math.round(((-820) / length) + 100.5 + (41 * CGpercentage)+ (16.6 * Math.log10(NaConcentration / 0.05)));
        }

        return temp;
    }

    /*
     * 2D integer array that stores the length an overlap would have to be if given
     * all desired melting temperatures and starting location.
     */
    // TODO make sure that min/max mode compatibility is correctly implemented
    public static Integer[][] overlapCalculator(String TempSequence, Integer minTemp, Integer maxTemp, Integer maxLen,
            String Mode) {

        // Mode must be either 'Min' or 'Max'
        if (!(Mode.equals("Min") || Mode.equals("Max"))) {
            throw new IllegalArgumentException("Mode must either be \"Min\" or \"Max\"");
        }

        // 2D integer array that stores the lengths of oligos given temperature and
        // starting location
        Integer[][] lengthOverlapArray = new Integer[TempSequence.length()][maxTemp - minTemp];

        int numTimesCalcUsed = 0;

        // goes through the entire lengthOverlapArray, populating it
        for (int tempIndex = 0; tempIndex < maxTemp - minTemp; tempIndex++) {
            // System.out.println("Finding overlaps for temp: " + (minTemp + tempIndex));

            // minimum and maximum allowable overlap lengths
            final int MIN_OVERLAP_LEN = 12;
            final Integer MAX_OVERLAP_LEN = maxLen;

            // the target temperature
            Integer targetTemp = tempIndex + minTemp;
            // the calculated temperature
            Integer calculatedTemp = null;
            for (int DNAindex = 0; DNAindex < TempSequence.length(); DNAindex++) {
                boolean Continue = true;
                int currOverlapLen = 0;
                int numCG = 0;
                Float CGpercentage;

                // calculates the length the hard way when there is no previous location length
                // data to use as an estimate
                // TODO make algorithm more efficient by using previously calculated length best estimate for next pos

                while (Continue) {

                    // the current overlap length can never exceed the maximum overlap length
                    if (currOverlapLen == MAX_OVERLAP_LEN) {
                        Continue = false;
                        lengthOverlapArray[DNAindex][tempIndex] = -1;
                        // System.out.println("Cant continue, overlap length exceeds maximum overlap
                        // lenght.");
                    }
                    // the DNAindex - currOverlapLen cannot go beyond the front end of the DNA
                    // sequence
                    else if ((DNAindex - currOverlapLen) <= 0) {
                        Continue = false;
                        lengthOverlapArray[DNAindex][tempIndex] = -2;
                        // System.out.println("Cant continue, overlap length + position cannot exceed
                        // length of DNA sequence.");
                        // System.out.println("Position: " + DNAindex + " overlapLen: " +
                        // currOverlapLen);

                    }
                    // the current overlap length cannot be smaller than the minimum overlap length
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
                            // System.out.println("Index: " + DNAindex + "calculated temp is: " +
                            // calculatedTemp + " Desired temp is: " + (minTemp + tempIndex));
                            // TODO figure out if this is the correct statement
                            // checks if calculated temperature is the correct temperature
                            if (calculatedTemp >= targetTemp) {
                                lengthOverlapArray[DNAindex][tempIndex] = currOverlapLen;
                                // System.out.println("Stopping, length for temperature " + (minTemp +
                                // tempIndex) + " at postion " + (DNAindex) + ": " + currOverlapLen);
                                Continue = false;
                            }
                        } // if mode is max
                        else {
                            // stores temp of previous overlap length for later

                            Integer prevCalculatedTemp = calculatedTemp;

                            // calculates the temperature of the current overlap length
                            calculatedTemp = tempCalculator(CGpercentage, currOverlapLen, 0.05F, "Max");

                            // determines the number of C/G for the previous overlap length
                            Integer prevNumCG = numCG;
                            if (TempSequence.charAt(DNAindex - currOverlapLen) == '1') {
                                prevNumCG--;
                            }

                            float prevCGpercentage = (float) prevNumCG / (currOverlapLen - 1);

                            // if the temp at the current overlap length is greater than the target temp,
                            // Then if the temp at previous overlap length is less than or equal to the
                            // target temp,
                            // (or null in the case that there is no previous length) store the previous
                            // overlap length in the lengthOverlapArray
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

        // System.out.println("The temperature calculator was used " + numTimesCalcUsed
        // + " times.");
        // Integer numElements = lengthOverlapArray.length +
        // lengthOverlapArray[0].length;
        // System.out.println("There are " + numElements + " elements in the length
        // array.");
        // System.out.println("Thats an average of " + ((float) numTimesCalcUsed /
        // numElements) + " calculator usages per element");
        return lengthOverlapArray;

    }


    //Note: This function is never used. It prints out the data for the overlap calculator for debugging purposes
    public static void printLengthCalculator(Integer[][] lengthCalculatorValue, Integer minTemp, Integer maxTemp) {
        System.out.print("Curr DNA index:  ");
        for (int currDnaIndex = 0; currDnaIndex < lengthCalculatorValue.length; currDnaIndex++) {
            Formatter formatter = new Formatter();
            formatter.format("%-6.0f", (float) currDnaIndex);
            System.out.print(formatter + " ");
        }

        System.out.print("\n_________________");
        for (int currTempIndex = minTemp; currTempIndex < maxTemp; currTempIndex++) {
            System.out.print("_______");
        }
        System.out.println();

        for (int tempIndex = 0; tempIndex < maxTemp - minTemp; tempIndex++) {
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

    // function that performs the dynamically calculated cost optimisation
    // hozimiwatsit.

    //Turns The DNA and Hash map into into a a String Array Data Type.
    //Sample: ATCGTCATCAGT
    //        ATG   ATC
    //              AAC

    // 0 = A, 1 = T, 2 = C, 3 = G
    public static String[] DNAcleaner(String DNA, HashMap<Integer,String[]> decodonHash){

        //6 is at least 1 above the maximum number of decodons.
        char[][] data = new char[6][DNA.length()];

        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < DNA.length(); j++) {
                data[i][j] = ' ';
            }
        }
        for(int j = 0; j < DNA.length(); j++) {
            data[5][j] = '1';
        }

        for(int i = 0; i < DNA.length(); i++) {
            data[0][i] = DNA.charAt(i);
        }

        for(int posInDna = 0; posInDna < DNA.length(); posInDna+=3) {
            String[] codonArray = decodonHash.get(posInDna);
            if(codonArray != null){
                char numCodons = Character.forDigit(codonArray.length, 6);
                for(int currCodon = 0; currCodon < codonArray.length; currCodon++) {
                    for(int posInCodon = 0; posInCodon < 3; posInCodon++) {
                        data[currCodon][posInDna + posInCodon] = codonArray[currCodon].charAt(posInCodon);
                        data[5][posInDna + posInCodon] = numCodons;
                    }
                }
            }
        }

        String[] output = new String[6];
        for(int i = 0; i < 6; i++) {
            output[i] = String.valueOf(data[i]);
        }

        return output;
    }



    public static DNASegment[] costCalculator(String[] DNAsequence, ArrayList<Amino> altAminoList, HashMap<Integer,String[]> decodonHash, Integer[][] minLenData,
            Integer[][] maxLenData, Integer minLen, Integer maxLen, Integer minTemp, Integer maxTemp, Float costOfBase,
            Float costOfDegenerateBase) {

        //Output for the array: an arrayList of DNASegments
        //Each DNASegment contains all the oligonucleotides at that position
        //as well as the starting index
        ArrayList<DNASegment> DNASegmentsArrayListData = new ArrayList<DNASegment>();

        final Integer lenRange = maxLen - minLen;
        final Integer TEMP_RANGE = maxTemp - minTemp;

        //NUM_NUKES = the number of DNA nucleotides, as the DNA sequence is given in terms of codons, not nucleotides
        final Integer NUM_NUKES = DNAsequence.length * 3;
        // keaps track of minimum cost for each position

        // costArray[a][b] keeps track of the cost at position a, temperature b
        Float[][] costArray = new Float[NUM_NUKES][TEMP_RANGE];
        // lenArray[a][b] keeps track of the length of the most recent oligo at position
        // a, temperature b
        Integer[][] lenArray = new Integer[NUM_NUKES][TEMP_RANGE];
        // overlapArray[a][b] keeps track of the overlap of the most recent oligo at
        // position a, temperature b
        Integer[][] overlapArray = new Integer[NUM_NUKES][TEMP_RANGE];

        for (Float[] tempIndex : costArray) {
            // all entries in costArray are initialized to positive infinity
            Arrays.fill(tempIndex, Float.POSITIVE_INFINITY);

        }

        for (int tempIndex = 0; tempIndex < TEMP_RANGE; tempIndex++) {
            // the first element of each temperature row are re-initialized to 0
            costArray[0][tempIndex] = 0F;

            // the costs of all the lengths able to be reached by an oligo with position 0 for a given temperature are
            // calculated
            for (int currLen = minLen; currLen < maxLen; currLen++) {

                //the oligoCost function is called for a given oligo
                Float cost = oligoCost(costOfBase, decodonHash, 0, currLen);
                costArray[currLen][tempIndex] = cost;

                // since costArray value is changed, the corresponding lenArray and overlapArray
                // values must change as well
                lenArray[currLen][tempIndex] = currLen;
                // the first oligo has no overlap
                overlapArray[currLen][tempIndex] = 0;
            }

            // goes through each reachable index (index that is not at positive infinity) and
            // calculates the next oligo cost
            // if the current oligo ended at the given position
            for (int currDnaIndex = 1; currDnaIndex < NUM_NUKES; currDnaIndex++) {
                float prevOligoCost = costArray[currDnaIndex][tempIndex];
                if (prevOligoCost != Float.POSITIVE_INFINITY) {
                    Integer minOverlapSize = minLenData[currDnaIndex][tempIndex];
                    Integer maxOverlapSize = maxLenData[currDnaIndex][tempIndex];

                    // --------------------------->Oligo(A)
                    // <-----------Overlap(A.1)
                    // --------------------------------------->
                    // ---------------------------------------->
                    // ----------------------------------------->
                    // ...
                    // ------------------------------------------------------>
                    // ------------------------------------------------------->
                    // <----------Overlap(A.2)
                    // --------------------------------------->
                    // ---------------------------------------->
                    // ----------------------------------------->
                    // ...
                    // ------------------------------------------------------>
                    // ------------------------------------------------------->
                    // <---------Overlap(A.3)
                    // --------------------------------------->
                    // ---------------------------------------->
                    // ----------------------------------------->
                    // ...
                    // ------------------------------------------------------>
                    // ------------------------------------------------------->
                    // .
                    // .
                    // .
                    // <--Overlap(A.7)
                    // --------------------------------------->
                    // ---------------------------------------->
                    // ----------------------------------------->
                    // ...
                    // ------------------------------------------------------>
                    // ------------------------------------------------------->

                    // goes through every possible overlap.
                    for (int currOverlap = minOverlapSize; currOverlap < maxOverlapSize; currOverlap++) {
                        // goes through every possible length at given overlap, calculating cost
                        for (int currLen = minLen; currLen < maxLen; currLen++) {
                            Integer startIndex = currDnaIndex - currOverlap;
                            Integer endIndex = currDnaIndex - currOverlap + currLen;

                            // makes sure to keep all indexes in bounds
                            if (endIndex < NUM_NUKES) {
                                float currOligoCost = prevOligoCost + oligoCost(costOfBase,
                                        decodonHash, startIndex, endIndex);

                                if (currOligoCost < costArray[endIndex][tempIndex]) {
                                    costArray[endIndex][tempIndex] = currOligoCost;
                                    lenArray[endIndex][tempIndex] = currLen;
                                    overlapArray[endIndex][tempIndex] = currOverlap;
                                }
                            }

                        }
                    }
                }
            }
        }
        // printResultsArray(costArray, lenArray, overlapArray, minTemp, maxTemp);

        // the cheapest total cost
        float cheapestCost = Float.POSITIVE_INFINITY;
        int cheapestTemp = 0;
        // the temperatureIndex with the cheapest total cost
        int cheapestTempIndex = -1;

        // loop to find the temperatureIndex with the cheapest total cost
        for (int currTempIndex = 0; currTempIndex < TEMP_RANGE; currTempIndex++) {
            float currCost = costArray[NUM_NUKES - 1][currTempIndex];

            // in the case that this is the first temperature index or in the case that the
            // currCost is less than the cheapest Cost found yet
            if (cheapestTempIndex == -1 || currCost < cheapestCost) {
                cheapestTempIndex = currTempIndex;
                cheapestCost = currCost;
                cheapestTemp = minTemp + currTempIndex;
            }
        }
        for(int i = 0; i < 27; i++) {
            System.out.println("CurrentCost: " + cheapestCost);
            System.out.println("CurrentTemp " + cheapestTemp);
        }
        // System.out.println("The cheapest cost is: " + cheapestCost + " at temperature
        // " + (cheapestTempIndex + minTemp));

        // because of a lack of foresight, the DNA sequence is stored in a String array
        // instead of as a single string.
        // this makes the sequence more unwieldy than if it was stored as a single string.
        // here, it is converted into a string.
        // This is quite hacky, but it is
        // simply easier, for the time being, than refactoring all the code.
        StringBuffer dnaSequenceStringBuffer = new StringBuffer(NUM_NUKES);
        for (int currDnaCodon = 0; currDnaCodon < DNAsequence.length; currDnaCodon++) {
            dnaSequenceStringBuffer.append(DNAsequence[currDnaCodon]);
        }
        String dnaString = dnaSequenceStringBuffer.toString();

        String[] cleanDna = DNAcleaner(dnaString, decodonHash);
        for(int i = 0; i < cleanDna.length; i++) {
            System.out.println(cleanDna[i]);
        }

        // the ending position of the current oligo
        int currOligoEnd = NUM_NUKES - 1;

        // the starting position of the last oligo of the cheapest temperature
        // currOlgioStart = endingPosition - length
        int currOligoStart = currOligoEnd - lenArray[currOligoEnd][cheapestTempIndex];


        boolean cont = true;
        while (cont) {
            System.out.println("Start: " + currOligoStart + " End: " + currOligoEnd);

            int numVariants = 1;
            boolean alreadyCountedCodon = false; //keeps track if the current codon has already been taken into account in the total variant list
            for (int currBaseIndex = currOligoStart; currBaseIndex < currOligoEnd; currBaseIndex++) {
                //if a new codon has been reached
                if (currBaseIndex % 3 == 0) {
                    alreadyCountedCodon = false;
                }
                //if the current codon has not been taken into account
                if (alreadyCountedCodon == false) {
                    int numDecodons = Character.getNumericValue(cleanDna[5].charAt(currBaseIndex));
                    numVariants = numVariants * numDecodons;
                    alreadyCountedCodon = true;
                }
            }
            System.out.println("Num Variants: " + numVariants);

            char[][] oligoVariants = new char[numVariants][currOligoEnd - currOligoStart];
            for (int currBaseIndex = currOligoStart; currBaseIndex < currOligoEnd; currBaseIndex++) {
                int currOligoVariantIndex = currBaseIndex - currOligoStart;
                int numDecodons = Character.getNumericValue(cleanDna[5].charAt(currBaseIndex));
                if (numDecodons == 1) {
                    char currBase = cleanDna[0].charAt(currBaseIndex);
                    for (int currVariant = 0; currVariant < numVariants; currVariant++) {
                        oligoVariants[currVariant][currOligoVariantIndex] = currBase;
                    }
                } else {
                    int currDecodon = 0;
                    for (int currVariant = 0; currVariant < numVariants; currVariant++) {
                        char currBase = cleanDna[currDecodon].charAt(currBaseIndex);
                        oligoVariants[currVariant][currOligoVariantIndex] = currBase;
                        if(currDecodon < numDecodons - 1) {
                            currDecodon++;
                        }
                        else {
                            currDecodon = 0;
                        }

                    }
                }
            }
            for (int currVariant = 0; currVariant < numVariants; currVariant++) {
                System.out.println(String.valueOf(oligoVariants[currVariant]));
            }

            //if reached the last oligo, break
            if (currOligoStart == 0) {
                cont = false;
            }
            //otherwise, iterate to the next oligo
            else {
                int currOligoOverlap = overlapArray[currOligoEnd][cheapestTempIndex];
                currOligoEnd = currOligoStart + currOligoOverlap;
                currOligoStart = currOligoEnd - lenArray[currOligoEnd][cheapestTempIndex];

            }


            /*
            ArrayList<StringBuilder> oligoStringBuilderArray = new ArrayList<StringBuilder>();

            StringBuilder firstVariant = new StringBuilder();
            oligoStringBuilderArray.add(firstVariant);

            int currBase = currOligoStart;
            while(currBase  < currOligoEnd){

                int currOffset = currBase % 3; // the current offset from the start of a codon
                //System.out.println("CurrBase, CurrOligoEnd" + currBase +", " + currOligoEnd);

                //If there is no multidecodon at this position or in adjacent positions that are related to the same codon, add the nucleotide to the chain normally
                if(decodonHash.get(currBase - currOffset) == null) {


                    for(StringBuilder oligoVariant : oligoStringBuilderArray) {
                        String currCodon = dnaString.substring(currBase + currOffset, currBase + 3);
                        //if the codon needs to be cut off at the back
                        if(currOligoEnd < currBase + 3) {
                            currCodon = currCodon.substring(0, currOligoEnd - currBase);
                        }
                        oligoVariant.append(currCodon);
                    }
                }
                else {
                    //otherwise calculate the number of variants needed
                    int duplicationFactor = 0;
                    int numVariants = oligoStringBuilderArray.size();
                    for(String codon : decodonHash.get(currBase - currOffset)) {
                        duplicationFactor++;
                    }

                    //make a temporary copy of the oligo variant list
                    ArrayList<StringBuilder> duplicateVariantList = new ArrayList<StringBuilder>();
                    for(StringBuilder varient: oligoStringBuilderArray) {
                        StringBuilder tempVarient = new StringBuilder(varient);
                        duplicateVariantList.add(tempVarient);
                    }

                    //duplicate the variant list by the number of variants needed
                    for(int currDuplicate = 0; currDuplicate < duplicationFactor; currDuplicate++) {
                        //adding the current codon onto the current
                        for(int currVariant = 0; currVariant < numVariants; currVariant++) {

                            //current codon
                            String currCodon = decodonHash.get(currBase - currOffset)[currDuplicate];

                            // in the case that the codon is partially outside the bounds of the current segment, then we need to
                            // truncate the codon to only append the part within bounds
                            if(currOligoEnd < currBase + 3) {
                                currCodon = currCodon.substring(0, currOligoEnd - currBase);
                            } //otherwise, if the offset is greater than 0, then that means that the codon falls partially outside of bounds in the front
                            else if(currOffset > 0) {
                                currCodon = currCodon.substring(currOffset, 2);
                            }

                            StringBuilder currentVariant = new StringBuilder(oligoStringBuilderArray.get(currDuplicate * numVariants + currVariant)  + currCodon);
                            //StringBuilder currentVariant = new StringBuilder(oligoStringBuilderArray.get(currDuplicate * numVariants + currVariant) + currCodon);

                            //adding the varient to the oligoStringBuilderArray
                            oligoStringBuilderArray.set(currDuplicate * numVariants + currVariant, currentVariant);
                        }


                        //duplication occuring
                        if(currDuplicate != duplicationFactor - 1) {
                            for(StringBuilder variant : duplicateVariantList) {
                                oligoStringBuilderArray.add(variant);
                            }
                        }
                    }
                }
                currBase += 3 - currOffset;

            }

            //I am now implementing a new version of output. One that involves objects for easier handling.

            //variable is self-explanatory. Perhaps, it can be defined higher up in the code
            int numOligoVariants = oligoStringBuilderArray.size();
            //constructing a string array with the same number of elements as oligoStringBuilderArray for output purposes
            String[] oligoStringArray = new String[numOligoVariants];

            //for loop that converts the StringBuilder Array into a String Array
            for(int currVariant = 0; currVariant < numOligoVariants; currVariant++) {
                String currVariantString = oligoStringBuilderArray.get(currVariant).toString();
                oligoStringArray[currVariant] = currVariantString;
            }

            //adding the oligoStringArray to the DNASegmentsArrayListData ArrayList
            DNASegment currSegment = new DNASegment(currOligoStart, numOligoVariants, oligoStringArray);
            DNASegmentsArrayListData.add(currSegment);

            //if this is the last oligo, go to the start of the while loop and exit
            if(currOligoStart == 0) {
                cont = false;
            }
            //otherwise, iterate to the next oligo
            else{
                int currOligoOverlap = overlapArray[currOligoEnd][cheapestTempIndex];
                currOligoEnd = currOligoStart + currOligoOverlap;
                currOligoStart = currOligoEnd - lenArray[currOligoEnd][cheapestTempIndex];
            }
        }


        int numSegments = DNASegmentsArrayListData.size();
        DNASegment[] DNASegmentOutput = new DNASegment[numSegments];

        //The Segments in DNASegmentsArrayListData have been stored in reverse order, because it was
        //simply easier to do so at the time. The output, however, should be in standard order.
        for(int currSegment = 0; currSegment < numSegments; currSegment++) {
            DNASegmentOutput[numSegments - 1 - currSegment] = DNASegmentsArrayListData.get(currSegment);
        }

        //checkResult(DNASegmentOutput);

        return DNASegmentOutput;
        */

        }
        return null;
    }

    //Goes through all the DNA oligos of a calcualted solution, and checks to see if they code for the proper protein
    private static boolean checkResult(DNASegment[] dnaSegmentArray) {
        for(int segmentIndex = 0; segmentIndex < dnaSegmentArray.length; segmentIndex++) {
            DNASegment currSegment = dnaSegmentArray[segmentIndex];
            int numVersions = currSegment.getNumVersions();
            //Cuts the DNA segments into codons, and then converts the codon array into a protein
            for(int versionIndex = 0; versionIndex < numVersions; versionIndex++) {
                String currVersion = currSegment.getSequence(versionIndex);
                int startingIndex = currSegment.getStartIndex();
                int firstCodonStartingIndex = (startingIndex + (3 - (startingIndex % 3)));

                System.out.println(currSegment.getStartIndex());
                System.out.println(firstCodonStartingIndex);
                System.out.println(currVersion);


                int currIndex = firstCodonStartingIndex;
                for(int i = firstCodonStartingIndex; i < firstCodonStartingIndex + currSegment.getLength() - 6; i += 3) {
                    System.out.print(currVersion.substring(i, i + 3) + " ");

                }


            }
        }
        return false;
    }

    private static void printResultsArray(Float[][] costArray, Integer[][] lenArray, Integer[][] overlapArray,
            Integer minTemp, Integer maxTemp) {
        System.out.print("Curr DNA index:  ");
        for (int currDnaIndex = 0; currDnaIndex < costArray.length; currDnaIndex++) {
            Formatter formatter = new Formatter();
            formatter.format("%-6.0f", (float) currDnaIndex);
            System.out.print(formatter + " ");
        }
        System.out.println();
        for (int tempIndex = 0; tempIndex < maxTemp - minTemp; tempIndex++) {
            float currTemp = ((float) minTemp + tempIndex);
            Formatter formatter = new Formatter();
            formatter.format("%3.0f", currTemp);
            System.out.print("    " + formatter + "  C  |    ");

            for (int currDnaIndex = 0; currDnaIndex < costArray.length; currDnaIndex++) {

                if (costArray[currDnaIndex][tempIndex] == Float.POSITIVE_INFINITY) {
                    char infinity = '\u221E';
                    System.out.print("   Cost: " + infinity + "      ");

                } else {
                    Formatter costFormatter = new Formatter();
                    float currCost = costArray[currDnaIndex][tempIndex];
                    costFormatter.format("%-6.0f", currCost);
                    System.out.print("   Cost: " + costFormatter + " ");
                }
                if (lenArray[currDnaIndex][tempIndex] == null) {
                    System.out.print("Length: Null ");
                } else {
                    Formatter lenFormatter = new Formatter();
                    float currLen = lenArray[currDnaIndex][tempIndex];
                    lenFormatter.format("%-4.0f", currLen);
                    System.out.print("Length: " + lenFormatter + " ");

                }
                if (overlapArray[currDnaIndex][tempIndex] == null) {
                    System.out.print("Overlap: Null|");
                } else {
                    Formatter overlapFormatter = new Formatter();
                    float currOverlap = overlapArray[currDnaIndex][tempIndex];
                    overlapFormatter.format("%-4.0f", currOverlap);
                    System.out.print("Overlap: " + overlapFormatter + "|");
                }
            }
            System.out.println();

        }
    }

    // calculates the cost of a given oligo
    public static Float oligoCost(Float costOfBase, HashMap<Integer,String[]> decodonHash,
            Integer startPos, Integer endPos) {
        // TODO make cost algorithm more efficient
        Integer numBases = (endPos - startPos);
        Integer codonCostMultiplier = 1;

        // if a position has n degenerate codons: multiply the cost by (n + 1)
        for(int currPos = startPos; currPos < endPos; currPos++) {
            if(decodonHash.get(currPos) != null) {
                codonCostMultiplier *= decodonHash.get(currPos).length + 1;
            }
        }

        Float cost = (numBases * costOfBase) * codonCostMultiplier;

        return cost;
    }
}