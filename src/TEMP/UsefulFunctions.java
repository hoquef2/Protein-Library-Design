public class UsefulFunctions {

    //TODO alter binaryTempSequence to account for decodons
    
    

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
