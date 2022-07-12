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

        if (minTemp ) {
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


}
