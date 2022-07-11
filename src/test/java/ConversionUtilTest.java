import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConversionUtilTest {
    
    @Test
    @DisplayName("Converting DNA into protein sequence")
    void convertDnaSequenceToProtein() {
        //Given.
        String[] dnaSequence = {"ATT", "ATC", "ATA", 
                "CTT", "CTC", "CTA", "CTG", "TTA", "TTG", 
                "GTT", "GTC", "GTA", "GTG", 
                "TTT", "TTC", 
                "ATG", 
                "TGT", "TGC", 
                "GCT", "GCC", "GCA", "GCG", 
                "GGT", "GGC", "GGA", "GGG", 
                "CCT", "CCC", "CCA", "CCG", 
                "ACT", "ACC", "ACA", "ACG", 
                "TCT", "TCC", "TCA", "TCG", "AGT", "AGC", 
                "TAT", "TAC", 
                "TGG", 
                "CAA", "CAG", 
                "AAT", "AAC", 
                "CAT", "CAC", 
                "GAA", "GAG", 
                "GAT", "GAC", 
                "AAA", "AAG", 
                "CGT", "CGC", "CGA", "CGG", "AGA", "AGG"};

        //When.
        String actualConversion = ConversionUtil.dnaToProteinSequence(dnaSequence);
        String expectedConversion = "IIILLLLLLVVVVFFMCCAAAAGGGGPPPPTTTTSSSSSSYYWQQNNHHEEDDKKRRRRRR";
        
        //Then.
        assertEquals(expectedConversion, actualConversion);
    }
    
    @Test
    @DisplayName("Converting protein sequence into oligo sequence")
    void convertProteinSequenceToDna() {
        //Given.
        String path = "data/real/HumanCodonFrequenciesData";
        HashMap<String, Integer> codonFrequencies = SystemUtil.loadCodonFrequencies(path);
        String proteinSequence1 = "";
        String proteinSequence2 = "AAAAAAAA";
        String proteinSequence3 = "ACDEFGHIKLMNPQRSTVWY";
        
        //When.
        String[] dnaSequence1 = ConversionUtil.proteinSequenceToDna(proteinSequence1, codonFrequencies);
        String backConversion1 = ConversionUtil.dnaToProteinSequence(dnaSequence1);
        String[] dnaSequence2 = ConversionUtil.proteinSequenceToDna(proteinSequence2, codonFrequencies);
        String backConversion2 = ConversionUtil.dnaToProteinSequence(dnaSequence2);
        String[] dnaSequence3 = ConversionUtil.proteinSequenceToDna(proteinSequence3, codonFrequencies);
        String backConversion3 = ConversionUtil.dnaToProteinSequence(dnaSequence3);
        
        //Then.
        assertEquals(proteinSequence1, backConversion1);
        assertEquals(proteinSequence2, backConversion2);
        assertEquals(proteinSequence3, backConversion3);

    }
    
    @Test
    @DisplayName("Converting protein sequence into Max and Min binary temperature oligo sequence w/o degenerate codons")
    void convertProteinSequenceToBinaryTemperatureDna() {
        //Given.
        String proteinSequence = "ACDEFGHIKLMNPQRSTVWY";
        HashMap<Integer, String> degenerateCodons= new HashMap<Integer, String>;
        String modeMax = "Max";
        String modeMin = "Min";
        
        //When.
        String minBinarySequence = ConversionUtil.proteinSequenceToBinary(proteinSequence, degenerateCodons, modeMin);
        String expectedMinSequence = "111011101101001111101001001101001001111101111011011101011001";
        String maxBinarySequence = ConversionUtil.proteinSequenceToBinary(proteinSequence, degenerateCodons, modeMax);
        String expectedMaxSequence = "110010100100000110100000000000001000110100010010010100011000";
        
        //Then.
        assertEquals(expectedMinSequence, minBinarySequence);
        assertEquals(expectedMaxSequence, maxBinarySequence);
    }
}


