import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

    public static void writeToOutput(DNASegment[] output, int numSegments, String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        for(int currSegmentIndex = 0; currSegmentIndex < numSegments; currSegmentIndex++) {
            DNASegment currSegmentData = output[currSegmentIndex];
            int numVersions = currSegmentData.getNumVersions();
            for(int currVersion = 0; currVersion < numVersions; currVersion++) {
                for(int curSpace = 0; curSpace < currSegmentData.getStartIndex(); curSpace++) {
                    writer.print(" ");
                }
                writer.println(currSegmentData.getSequence(currVersion));
            }
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException {


        /*final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));

        }*/

        System.out.println("_____________________________________________________________");



        String pathOfOriginalInputFile = "C:/Users/Yehuda/Documents/Github_Projects/OligoNukes/data/test_FaASTA_input/GFP.fasta";
        String outputFileName = "C:/Users/Yehuda/Documents/Github_Projects/OligoNukes/data/test_FASTA_output/GFP-Output.fasta";

        Object[] parsedData = {};
        try {
            parsedData = SystemUtil.convertToNewFormat(pathOfOriginalInputFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String proteinSequence = (String) parsedData[0];
        ArrayList<Amino> altAminoList = (ArrayList<Amino>) parsedData[1];



        TempInputImplementation tempInput = new TempInputImplementation();
        Integer minTemp = tempInput.getMinTemp();
        Integer maxTemp = tempInput.getMaxTemp();
        Integer minLen = 50;
        Integer maxLen = 80;
        Float costOfCodon = tempInput.getCostPerBase();
        Float costOfDegenerateCodon = tempInput.getCostPerDegenerateBase();


        /*
        altAminoList = SystemUtil.loadAlternateAminos("data/test/TestAlternateAminoForCuttingAlgorithmData");

        try {
            proteinSequence = SystemUtil.loadFasta(tempInput.getFastaFilePath())[1];
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }*/

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
        writeToOutput(output, numSegments, outputFileName);
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
            //          print(false at
            //          dna segment # x at position y)

        }

    }
}
