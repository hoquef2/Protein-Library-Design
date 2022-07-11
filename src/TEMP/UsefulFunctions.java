public class UsefulFunctions {

    //TODO decide where All functions in this class belong
    //TODO alter binaryTempSequence to account for decodons
    /*takes in a protein String as input and outputs the corresponding
     com.tcnj.oligonukes.DNA sequence, in binary representation, that minimizes or maximizes 
     (depending on the mode) CG content. C/G's are represented as a 1 and A/T's
     are represented as a 0. Mode is either "Min" or `Max`. Min and Max are
     a bit of a misnomer. They refer to minimizing or maximizing length for
     a given melting temperature. So an oligo that melts  at a temperature X 
     from the Min output will be as short as possible while an oligo from the 
     output Max will be be as long as possible
     */
    public static String binaryTempSequence(String proteinSequence, String Mode) {

        //the length of the protein sequence
        int proteinSequenceLength = proteinSequence.length();

        StringBuilder tempSequenceBuilder = new StringBuilder(proteinSequenceLength * 3);

        //If the mode is Min, then CG content is maximized to minimize length
        if (Mode.equals("Min")) {
            for (int aminoNum = 0; aminoNum < proteinSequenceLength; aminoNum++) {

                //current com.tcnj.oligonukes.Amino
                Character currentAmino = proteinSequence.charAt(aminoNum);

                //Binary Representation of corresponding codon containing FEWEST C/G
                String maxCGcodonBinary = Amino.aminoByLetter.get(currentAmino).getMaxCGcodonBinary();

                tempSequenceBuilder.insert(aminoNum * 3, maxCGcodonBinary);
            }
        }
        //If the mode is Max, then CG content is minimized to maximize length
        else if (Mode.equals("Max")) {
            for (int aminoNum = 0; aminoNum < proteinSequenceLength; aminoNum++) {

                //current com.tcnj.oligonukes.Amino
                Character currentAmino = proteinSequence.charAt(aminoNum);

                //Binary Representation of corresponding codon containing GREATEST C/G
                String minCGcodonBinary = Amino.aminoByLetter.get(currentAmino).getMinCGcodonBinary();

                tempSequenceBuilder.insert(aminoNum * 3, minCGcodonBinary);
            }
        }
        //If the mode is Invalid, throw an exception
        else {
            throw new IllegalArgumentException("Mode must either be \"Min\" or \"Max\"");
        }

        String minTempSequence = tempSequenceBuilder.toString();
        return minTempSequence;
    }

    //TODO decide where this function belongs
    /*
    Takes in a binary string representing GC content of com.tcnj.oligonukes.DNA and outputs the maximum
    length an oligo can be given all starting positions and all possible melting 
    temperatures.
    At each (position, meltTemp) coordinate, the length of an oligo which starts at the
    given position and has the given melting temperature is stored. If no length
    would result in the given melting temperature, then store a value of null.
     */

    //takes in a length of com.tcnj.oligonukes.DNA and returns the CG percentage
    public static double CGpercentageCalculator(String binaryDNAsequence) {
        int numCG = 0;
        double CGpercentage;
        for (int DNAindex = 0; DNAindex < binaryDNAsequence.length(); DNAindex++) {
            if (binaryDNAsequence.charAt(DNAindex) == '1') {
                numCG++;
            }
        }
        CGpercentage = (double) numCG / binaryDNAsequence.length();
        return CGpercentage;
    }

    /*
    2D integer array that stores the length an overlap would have to be given
    a desired melting temperature and C/G percentage content. CG percentage content
    goes up in 5% intervals from 0% to 100%, and so has 20 intervals. The temperature
    interval goes up 1 degree Celsius.
    */
    public static Integer[][] LengthCalculatorStorage(Integer minTemp, Integer maxTemp, float Naconcentration) {
        
        
        //array used to store output data
        Integer[][] CGamountTemperature = new Integer[21][maxTemp - minTemp];


        //loop that cycles through the entire CGamountTemperature array, populating it with lengths
        for (int currCG_PercentageIndex = 0; currCG_PercentageIndex < 21; currCG_PercentageIndex++) {
            //the length of the com.tcnj.oligonukes.DNA segment being analyzed
            Integer currLength = null;
            float currCGpercentage = ((float) currCG_PercentageIndex * 5) / 100;
            for (int currTempIndex = 0; currTempIndex < maxTemp - minTemp; currTempIndex++) {
                int currTemp = minTemp + currTempIndex;

                //there are two equations. One for com.tcnj.oligonukes.DNA lengths 18 and above, and one for com.tcnj.oligonukes.DNA lengths less than 18.
                //first equation
                currLength = (int) Math.floor(((float) currTemp - (16.6 * Math.log10(Naconcentration / 0.05))) / ((2 * currCGpercentage) + 2));
                if (currLength >= 18) {
                    //second equation
                    currLength = (int) Math.floor(820 / ((100.5 + (41 * currCGpercentage) + (16.6 * Math.log10(Naconcentration))) - currTemp));
                }
                //with really high CGpercentages and melting temperatures,
                //the second equation stops working and returns negative lengths.
                if (currLength <= 0) {
                    currLength = null;
                }
                CGamountTemperature[currCG_PercentageIndex][currTempIndex] = currLength;


            }
        }

        //for testing purposes only. Prints out all of CGamountTemperature[][]
        TestingFunctions.printCGamountTemperature(minTemp, maxTemp, CGamountTemperature);

        return CGamountTemperature;
    }
    
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


            //minimum and maximum allowable overlap lengths
            final Integer MIN_OVERLAP_LEN = 12;
            final Integer MAX_OVERLAP_LEN = maxLen;

            //the target temperature
            Integer targetTemp = tempIndex + minTemp;
            //the calculated temperature
            Integer calculatedTemp = null;
            for (int DNAindex = 0; DNAindex < TempSequence.length(); DNAindex++) {
                Boolean Continue;
                Integer currOverlapLen;
                Integer numCG;
                Float CGpercentage;

                //calculates the length the hard way when there is no previous location length data to use as an estimate
                //TODO make algorithm more efficient by using previously calculated length as best estimate for next pos
                Continue = true;
                currOverlapLen = 0;
                numCG = 0;


                while (Continue) {

                    //the current overlap length can never exceed the maximum overlap length
                    if (currOverlapLen == MAX_OVERLAP_LEN) {
                        Continue = false;
                        lengthOverlapArray[DNAindex][tempIndex] = -1;
                    }
                    //the DNAindex + currOverlapLen cannot exceed the length of the entire com.tcnj.oligonukes.DNA sequence
                    else if ((DNAindex + currOverlapLen) >= TempSequence.length() - 1) {
                        Continue = false;
                        lengthOverlapArray[DNAindex][tempIndex] = -2;

                    }
                    //the current overlap length cannot be smaller than the minimum overlap length
                    else if (currOverlapLen < MIN_OVERLAP_LEN) {
                        currOverlapLen++;
                        if (TempSequence.charAt(DNAindex + currOverlapLen) == '1') {
                            numCG++;
                        }
                    } else {
                        currOverlapLen++;

                        if (TempSequence.charAt(DNAindex + currOverlapLen) == '1') {
                            numCG++;
                        }
                        CGpercentage = (float) numCG / currOverlapLen;
                        numTimesCalcUsed++;

                        if (Mode == "Min") {
                            calculatedTemp = tempCalculator(CGpercentage, currOverlapLen, 0.05F, "Min");
                            //TODO figure out if this is the correct statement
                            //checks if calculated temperature is the correct temperature
                            if (calculatedTemp >= targetTemp) {
                                lengthOverlapArray[DNAindex][tempIndex] = currOverlapLen;
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
                            if (TempSequence.charAt(DNAindex + currOverlapLen) == '1') {
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
    
    
    //function that performs the dynamically calculated cost optimisation hozimiwatsit.
    public static  Float[] costCalculator (String[] DNAsequence, Integer[][] minLengthData, Integer[][] maxLengthData, Input input) {
            final Integer MIN_LEN = input.getMinLen();
            final Integer MAX_LEN = input.getMaxLen();
            final Integer LEN_RANGE = MAX_LEN - MIN_LEN;
            final Integer MIN_TEMP = input.getMinTemp();
            final Integer MAX_TEMP = input.getMaxTemp();
            final Integer TEMP_RANGE = MAX_TEMP - MIN_TEMP;
            final Integer NUM_NUKES = DNAsequence.length * 3;
            Float[][] costArray = new Float[NUM_NUKES][TEMP_RANGE];
            
            
            //for every temperature
            for(Integer tempIndex = 0; tempIndex < TEMP_RANGE; tempIndex++) {
                Integer currTemp = MIN_TEMP + tempIndex;
                //for every com.tcnj.oligonukes.DNA nucleotide
                
                for(Integer dnaIndex = 0; dnaIndex < NUM_NUKES; dnaIndex++) {
                    costArray[tempIndex][dnaIndex] = Float.POSITIVE_INFINITY;
                    //initializing every element in the cost array with positive infinity
                    Integer minOverlapSize = minLengthData[dnaIndex][tempIndex];
                    Integer maxOverlapSize = maxLengthData[dnaIndex][tempIndex];
                    //for every 
                    for(Integer lenIndex = 0; lenIndex < LEN_RANGE; lenIndex++){
                        Integer currLen = MIN_LEN + lenIndex;
                        
                        
                    }
                    
                    
                }
            }
            return null;
    }
}
