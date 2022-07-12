import java.io.FileNotFoundException;
import java.util.HashMap;

public class Driver {
    
    public static void main(String[] args) {
        
        TempInputImplementation tempInput = new TempInputImplementation();
        Integer minTemp = tempInput.getMinTemp();
        Integer maxTemp = tempInput.getMaxTemp();
        Integer maxLen = tempInput.getMaxLen();


        String proteinSequence = null;
        try {
            proteinSequence = SystemUtil.loadFasta(tempInput.getFastaFilePath())[1];
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        
        HashMap<Integer, String> degenerateCodons = new HashMap<>();
        String tempSequence = ConversionUtil.proteinSequenceToBinary(proteinSequence, degenerateCodons, "Max");
        
        CuttingAlgorithmUtil.LengthCalculator(tempSequence, minTemp, maxTemp, maxLen, "Max");
    }
}
