import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Driver {

    public static void main(String[] args) {

        System.out.println(ConversionUtil.altAminoToBinary("ACDEFG"));

        TempInputImplementation tempInput = new TempInputImplementation();
        Integer minTemp = tempInput.getMinTemp();
        Integer maxTemp = tempInput.getMaxTemp();
        Integer minLen = tempInput.getMinLen();
        Integer maxLen = tempInput.getMaxLen();
        Float costOfCodon = tempInput.getCostPerBase();
        Float costOfDegenerateCodon = tempInput.getCostPerDegenerateBase();
        ArrayList<Amino> altAminoList = SystemUtil.loadAlternateAminos("data/test/TestAlternateAminosData");

        HashMap<String, Integer> codonFrequencies = SystemUtil
                .loadCodonFrequencies("data/real/HumanCodonFrequenciesData");

        String proteinSequence = null;
        try {
            proteinSequence = SystemUtil.loadFasta(tempInput.getFastaFilePath())[1];
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

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

        String output = CuttingAlgorithmUtil.costCalculator(dnaSequence, altAminoList, minLengthCalculatorOutput,
                maxLengthCalculatorOutput, minLen, maxLen, minTemp, maxTemp, costOfCodon, costOfDegenerateCodon);
        System.out.println(output);
    }
}
