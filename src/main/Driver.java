import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Driver {

    public boolean isSame (String protein, HashMap altAminos, String[] oligos)
    {
        int len = protein.length();
        for(int i = 0; i < len; i++) {
            //make a hashmap of protein[i] and altAminos[i];
            //make a hashmap of all oligos[x].charAt[i];
            //check if the two hashmaps are the same
            //if (sam false) {
                //return false;
            //}
        }
        return true;

    }

    public static void main(String[] args) throws FileNotFoundException {


        /*final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));

        }*/

        System.out.println("_____________________________________________________________");


        TempInputImplementation tempInput = new TempInputImplementation();
        Integer minTemp = tempInput.getMinTemp();
        Integer maxTemp = tempInput.getMaxTemp();
        Integer minLen = tempInput.getMinLen();
        Integer maxLen = tempInput.getMaxLen();
        Float costOfCodon = tempInput.getCostPerBase();
        Float costOfDegenerateCodon = tempInput.getCostPerDegenerateBase();
        ArrayList<Amino> altAminoList = SystemUtil.loadAlternateAminos("data/test/TestAlternateAminoForCuttingAlgorithmData");


        String proteinSequence = null;
        try {
            proteinSequence = SystemUtil.loadFasta(tempInput.getFastaFilePath())[1];
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Integer> codonFrequencies = SystemUtil
                .loadCodonFrequencies("data/real/HumanCodonFrequenciesData");

        HashMap<String, String[][]> decodonList = SystemUtil.loadDegenerateCodons("data/real/AA_set_decodons.human.txt");
        HashMap<Integer, String[]> convertedDecodonList = ConversionUtil.altAminoToDecodons(proteinSequence, altAminoList, decodonList);



        String[] dnaSequence = ConversionUtil.proteinSequenceToDna(proteinSequence, codonFrequencies);



        HashMap<Integer, String> degenerateCodons = new HashMap<>();
        String minTempSequence = ConversionUtil.proteinSequenceToBinary(proteinSequence, degenerateCodons, "Min");
        Integer[][] minLengthCalculatorOutput = CuttingAlgorithmUtil.overlapCalculator(minTempSequence, minTemp,
                maxTemp, maxLen, "Min");
        // CuttingAlgorithmUtil.printLengthCalculator(minLengthCalculatorOutput,
        // minTemp, maxTemp);

        String maxTempSequence = ConversionUtil.proteinSequenceToBinary(proteinSequence, degenerateCodons, "Max");
        Integer[][] maxLengthCalculatorOutput = CuttingAlgorithmUtil.overlapCalculator(maxTempSequence, minTemp,
                maxTemp, maxLen, "Max");

        // CuttingAlgorithmUtil.printLengthCalculator(minLengthCalculatorOutput,
        // minTemp, maxTemp);
        // System.out.println(minTempSequence);

        // CuttingAlgorithmUtil.printLengthCalculator(maxLengthCalculatorOutput,
        // minTemp, maxTemp);
        // System.out.println(maxTempSequence + "");



        DNASegment[] output = CuttingAlgorithmUtil.costCalculator(dnaSequence, altAminoList, convertedDecodonList, minLengthCalculatorOutput,
                maxLengthCalculatorOutput, minLen, maxLen, minTemp, maxTemp, costOfCodon, costOfDegenerateCodon);
        System.out.println(output);

        int numSegments = output.length;
        for(int currSegmentIndex = 0; currSegmentIndex < numSegments; currSegmentIndex++) {
            DNASegment currSegmentData = output[currSegmentIndex];
            //the number of versions at the current Segment
            int numVersions = currSegmentData.getNumVersions();
            for(int currVersion = 0; currVersion < numVersions; currVersion++) {
                for(int curSpace = 0; curSpace < currSegmentData.getStartIndex(); curSpace++) {
                    System.out.print(" ");
                }
                System.out.println(currSegmentData.getSequence(currVersion));
            }
        }

        //going through each segment, checking to see if they generate the correct protein segment
        for(int currSegmentIndex = 0; currSegmentIndex < numSegments; currSegmentIndex++) {
            DNASegment currSegment = output[currSegmentIndex];

            //go through every third position
            //  if (n, n + 3) are all A, T, C or G:     // do all decodons have degenerate bases?
            //      go to the lookup table to get the cooresponding amino acid
            //      if cooresponding amino acid != fastaAminoArray[n/3]
            //          print(false at dna segment # x at position y)

        }

    }
}
