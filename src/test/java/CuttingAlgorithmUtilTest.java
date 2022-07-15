import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CuttingAlgorithmUtilTest {

    @Test
    @DisplayName("Calculating cost")
    void convertProteinSequenceToDna() {
        //Given.
        
        Float costOfCodon = 1F;
        Float costOfDegenerateCodon = 2F;
        String[] DNAsequence = {"GTA", "CTC", "AGC", "GAC", "CCC", "CCC", "TCG",
                "ATT", "GCA", "CAG", "CAC", "TCA", "ACT", "CGA", "AAG", "GTA", 
                "ATC", "ACT", "TCC", "GGT", "TAT", "CTG", "TGG", "TAA", "TAC",
                "CCC", "GTC", "TTG", "CCG", "ATG", "AAG", "CCA", "TAT", "CGG",
                "GAG", "CGT", "TTC", "TCG", "GAT", "TTA", "GTA", "CTA", "ACG",
                "AGT", "CTA", "GCT", "CCC", "TGA", "TAG", "CGT"};
        ArrayList<Amino> alternateAminos = SystemUtil.loadAlternateAminos("data/test/TestAlternateAminoForCuttingAlgorithmData");
        Integer startingLoc = 20;
        Integer endingLoc = 40;
        

        //When.
        Float cost = CuttingAlgorithmUtil.oligoCost(costOfCodon, costOfDegenerateCodon, alternateAminos,startingLoc, endingLoc);
        //Then.
        System.out.println(cost);
        
        //assertEquals(proteinSequence1, backConversion1);
        //assertEquals(proteinSequence2, backConversion2);
        //assertEquals(proteinSequence3, backConversion3);

    }
}
