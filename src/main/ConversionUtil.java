import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ConversionUtil {
    public static String dnaToProteinSequence(String[] dnaSequence) {

        // if the dnaSequence is empty
        if (dnaSequence[0] == "") {
            return "";
        }

        StringBuffer convertedProtein = new StringBuffer(dnaSequence.length);
        for (int codonIndex = 0; codonIndex < dnaSequence.length; codonIndex++) {
            String currCodon = dnaSequence[codonIndex];
            String proteinChar = String.valueOf(AminoData.aminoByCodon.get(currCodon).getOneLetterAbbreviation());
            convertedProtein.append(proteinChar);
        }

        return convertedProtein.toString();
    }

    public static String[] proteinSequenceToDna(String proteinSequence, HashMap<String, Integer> codonFrequencies) {

        // if the protein sequence is empty
        if (proteinSequence.length() == 0) {
            return new String[] { "" };
        }

        String[] DNAsequence = new String[proteinSequence.length()];
        // converting protein sequence into com.tcnj.oligonukes.DNA sequence
        // for each amino acid

        for (int currAminoIndex = 0; currAminoIndex < AminoData.aminoList.length(); currAminoIndex++) {
            // number of aminoAcid occurrences
            Integer numAminos = 0;
            // location of each com.tcnj.oligonukes.Amino Acid occurrence
            ArrayList<Integer> aminoLocations = new ArrayList<Integer>();
            // computing aminoAmount and aminoLocations
            for (int currOligoIndex = 0; currOligoIndex < proteinSequence.length(); currOligoIndex++) {
                if (proteinSequence.charAt(currOligoIndex) == AminoData.aminoList.charAt(currAminoIndex)) {
                    numAminos++;
                    // System.out.println("Adding 1 to num Aminos: " + numAminos);
                    aminoLocations.add(currOligoIndex);
                }
            }

            Character currentAmino = AminoData.aminoList.charAt(currAminoIndex);
            // System.out.println("Current Amino is now: " + currentAmino);
            // generate an int array with size = the frequency of the given amino acid in
            // the protein sequence
            Integer[] codonRandomizer = randomPermutation(numAminos);
            // String array containing the corresponding codons
            String[] codons = AminoData.aminoByLetter.get(currentAmino).getCodons();
            Integer[] codonAmounts = new Integer[codons.length];

            // populating codonAmounts
            for (int currCodonIndex = 0; currCodonIndex < codons.length; currCodonIndex++) {
                String currCodon = codons[currCodonIndex];
                codonAmounts[currCodonIndex] = codonFrequencies.get(currCodon);
            }
            Integer[] allocationList = apportionment(codonAmounts, numAminos);

            // for every codon
            for (int codonElement = 0; codonElement < codons.length; codonElement++) {
                // for every aminoacid location

                for (int permutationElement = 0; permutationElement < numAminos; permutationElement++) {
                    // permutationList[element] <= allocationList[codonNumber]
                    if (codonRandomizer[permutationElement] <= allocationList[codonElement]) {
                        // DNAsequence[aminoLocations[permutationElement]] = currentCodon
                        DNAsequence[aminoLocations.get(permutationElement)] = codons[codonElement];
                        // break
                    }
                }
            }
        }
        return DNAsequence;
    }

    // proteinSequenceToDna helper function: generates a random permutation from 1
    // through N
    private static Integer[] randomPermutation(int size) {
        Integer[] array = new Integer[size];

        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        for (int i = size - 1; i >= 0; i--) {
            int indexToBeSwapped = (int) (Math.random() * i);

            int temp = array[indexToBeSwapped];
            array[indexToBeSwapped] = array[i];
            array[i] = temp;

        }
        return array;
    }

    // proteinSequenceToDna helper function: uses the Huntington - Hill method of
    // apportionment to most fairly distribute amino acids among codons
    private static Integer[] apportionment(Integer[] codonAmounts, Integer numberOfAminos) {

        // if there are no occurrences of a particular amino acid
        if (numberOfAminos == 0) {
            Integer[] apportionmentValues = new Integer[codonAmounts.length];
            Arrays.fill(apportionmentValues, 0);
            return apportionmentValues;
        }

        // if the number of codons is more than the number of aminos to be allocated,
        // the huntington hill
        // method breaks down, and an alternate method is needed.
        // In this case, aminos will be handed out to the codon with the highest
        // frequency, until there are
        // no more codons left
        if (numberOfAminos < codonAmounts.length) {
            Integer[] apportionmentValues = new Integer[codonAmounts.length];

            // initializing all values to 0
            for (int i = 0; i < apportionmentValues.length; i++) {
                apportionmentValues[i] = 0;
            }

            for (int apportionmentIndex = 0; apportionmentIndex < apportionmentValues.length; apportionmentIndex++) {

                int maxValue = 0;
                int maxIndex = -1;
                for (int codonAmountsIndex = 0; codonAmountsIndex < codonAmounts.length; codonAmountsIndex++) {
                    if (maxValue < codonAmounts[codonAmountsIndex]) {
                        maxIndex = codonAmountsIndex;
                        maxValue = codonAmounts[codonAmountsIndex];
                    }
                }
                apportionmentValues[maxIndex] = 1;
            }
            return apportionmentValues;
        }

        for (int codonIndex = 0; codonIndex < codonAmounts.length; codonIndex++) {
            // System.out.println(codonAmounts[codonIndex]);
        }
        Integer numberOfCodons = codonAmounts.length;

        double[] quota = new double[numberOfCodons];
        Integer[] lowerQuota = new Integer[numberOfCodons];

        Float[] geoMeanLowerQuota = new Float[numberOfCodons];
        Integer[] allocationList = new Integer[numberOfCodons];
        Integer totalCodonAmount = 0;
        Integer allocation = 0;

        // computing the standard divisor
        for (Integer codonAmount : codonAmounts) {
            totalCodonAmount += codonAmount;
        }
        Double divisorHigh = totalCodonAmount.doubleValue() / numberOfAminos;
        Double divisorLow = divisorHigh;
        Double divisorAvg;

        while (numberOfAminos != allocation) {

            // resetting allocation value
            allocation = 0;

            divisorAvg = (divisorHigh + divisorLow) / 2;
            for (int i = 0; i < numberOfCodons; i++) {
                // computing the quota for codon
                Integer codonAmount = codonAmounts[i];
                quota[i] = codonAmount / divisorAvg;

                // computing the lower quota for codon
                lowerQuota[i] = (int) Math.floor(quota[i]);

                // computing the geometric mean of the lower quota and one value higher for
                // codon
                geoMeanLowerQuota[i] = (float) Math.sqrt(lowerQuota[i] * (lowerQuota[i] + 1));

                // computing the allocation
                if (quota[i] < geoMeanLowerQuota[i]) {
                    allocationList[i] = (int) Math.floor(quota[i]);
                } else {
                    allocationList[i] = (int) Math.ceil(quota[i]);
                }
                allocation += allocationList[i];
            }

            // If the allocation is to small or to large, recompute the divisor anr
            // recalculate allocationList
            if (allocation < numberOfAminos) {
                // System.out.println("number of aminos assigned, " + allocation + ", is too
                // low");
                // Checks if this is the initial run-through
                if (divisorHigh == divisorLow) {
                    divisorLow = divisorHigh * 0.9;
                } else {
                    divisorHigh = divisorAvg;
                }
            } else if (allocation > numberOfAminos) {
                // System.out.println("number of aminos assigned, " + allocation + ", is too
                // high");
                // Checks if this is the initial run-through
                if (divisorHigh == divisorLow) {
                    divisorHigh = divisorLow * 1.2;
                } else {
                    divisorLow = divisorAvg;
                }
            }
        }

        // formats the allocation list for easy use in allocation aminos to codons
        Integer[] adjustedAllocationList = new Integer[numberOfCodons];

        adjustedAllocationList[0] = allocationList[0] - 1;
        Integer total = adjustedAllocationList[0];
        for (int i = 1; i < numberOfCodons; i++) {
            adjustedAllocationList[i] = allocationList[i] + total;
            total += adjustedAllocationList[i];
        }

        return adjustedAllocationList;
    }

    public static String proteinSequenceToBinary(String proteinSequence, HashMap<Integer, String> degenerateCodons,
            String Mode) {

        // the length of the protein sequence
        int proteinSequenceLength = proteinSequence.length();

        StringBuilder tempSequenceBuilder = new StringBuilder(proteinSequenceLength * 3);

        // If the mode is Min, then CG content is maximized to minimize length
        if (Mode.equals("Min")) {
            for (int aminoNum = 0; aminoNum < proteinSequenceLength; aminoNum++) {

                // current com.tcnj.oligonukes.Amino
                Character currentAmino = proteinSequence.charAt(aminoNum);

                // Binary Representation of corresponding codon containing FEWEST C/G
                String maxCGcodonBinary = AminoData.aminoByLetter.get(currentAmino).getMaxCGcodonBinary();

                tempSequenceBuilder.insert(aminoNum * 3, maxCGcodonBinary);
            }
        }
        // If the mode is Max, then CG content is minimized to maximize length
        else if (Mode.equals("Max")) {
            for (int aminoNum = 0; aminoNum < proteinSequenceLength; aminoNum++) {

                // current com.tcnj.oligonukes.Amino
                Character currentAmino = proteinSequence.charAt(aminoNum);

                // Binary Representation of corresponding codon containing GREATEST C/G
                String minCGcodonBinary = AminoData.aminoByLetter.get(currentAmino).getMinCGcodonBinary();

                tempSequenceBuilder.insert(aminoNum * 3, minCGcodonBinary);
            }
        }
        // If the mode is Invalid, throw an exception
        else {
            throw new IllegalArgumentException("Mode must either be \"Min\" or \"Max\"");
        }

        String tempSequence = tempSequenceBuilder.toString();
        return tempSequence;
    }

    //converts a list of alternate aminos to a binary number.
    public static HashMap<Integer, String[]> altAminoToDecodons(String proteinSequence, ArrayList<Amino> alternateAminoList, HashMap<String, String[][]> degenCodonHash) {

        HashMap<Integer, String[]> decodonsHash = new HashMap<>();

        Integer numAminos = alternateAminoList.size();


        for (int currAmino = 0; currAmino < numAminos; currAmino++) {
            int currAminoLocation = alternateAminoList.get(currAmino).getLocation();
            String currAltAminos = alternateAminoList.get(currAmino).getData();
            //altAminos and originalAminoAtPosition
            String currAllAminos = currAltAminos.concat(String.valueOf(proteinSequence.charAt(currAminoLocation)));
            String aminosBinaryNumber = altAminoToBinary(currAltAminos);
            String[] currDecodons = degenCodonHash.get(aminosBinaryNumber)[0];

            decodonsHash.put(currAminoLocation * 3, currDecodons);

        }

        return decodonsHash;
    }

    //converts an alternateAmino to binary reprisentation.
    public static String altAminoToBinary(String aminoList) {
        //binary representation stored as an Integer[]
        Integer binaryAminoNum = 0;

        //goes through all 20 aminos
        for (int currAminoIndex = 0; currAminoIndex < AminoData.NUM_AMINOS; currAminoIndex++) {
            //goes through every amino in the alternate amino list
            for (int currAltAminoIndex = 0; currAltAminoIndex < aminoList.length(); currAltAminoIndex++) {

                //checks if the .Amino in the alternate amino list is equivalent to the current amino in the amino list
                if (aminoList.charAt(currAltAminoIndex) == AminoData.aminoList.charAt(currAminoIndex)) {
                    binaryAminoNum += (int) Math.pow(2, currAminoIndex);
                }
            }
        }
        String binaryAminoString = "";

        Integer numFiller = 20 - Integer.toBinaryString(binaryAminoNum).length();
        for (int j = 0; j < numFiller; j++) {
            binaryAminoString += "0";
        }
        binaryAminoString += Integer.toBinaryString(binaryAminoNum);

        return binaryAminoString;
    }
}

