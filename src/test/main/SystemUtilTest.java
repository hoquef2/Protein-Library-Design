import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class SystemUtilTest {

    @Test
    @DisplayName("Loading TestFastaData file")
    void loadFasta() {
        // Given.
        String expectedTitle = "gi|186681228|ref|YP_001864424.1| phycoerythrobilin:ferredoxin oxidoreductase";
        String expectedContent = "MQLEHCLSPSIMLSKKFLNVSSSYPHSGGSELVLHDHPIISTTDNLERSSPLKKITRGMT" +
                "NQSDTDNFPDSKDSPGDVQRSKLSPVLDGVSELRHSFDGSAADRYLLSQSSQPQSAATAP" +
                "SAMFPYPGQHGPAHPAFSIGSPSRYMAHHPVITNGAYNSLLSNSSPQGYPTAGYPYPQQY" +
                "GHSYQGAPFYQFSSTQPGLVPGKAQVYLCNRPLWLKFHRHQTEMIITKQGRRMFPFLSFN" +
                "ISGLDPTAHYNIFVDVILADPNHWRFQGGKWVPCGKADTNVQGNRVYMHPDSPNTGAHWM" +
                "RQEISFGKLKLTNNKGASNNNGQMVVLQSLHKYQPRLHVVEVNEDGTEDTSQPGRVQTFT" +
                "FPETQFIAVTAYQNTDITQLKIDHNPFAKGFRDNYDTIYTGCDMDRLTPSPNDSPRSQIV" +
                "PGARYAMAGSFLQDQFVSNYAKARFHPGAGAGPGPGTDRSVPHTNGLLSPQQAEDPGAPS" +
                "PQRWFVTPANNRLDFAASAYDTATDFAGNAATLLSYAAAGVKALPLQAAGCTGRPLGYYA" +
                "DPSGWGARSPPQYCGTKSGSVLPCWPNSAAAAARMAGANPYLGEEAEGLAAERSPLPPGA" +
                "AEDAKPKDLSDSSWIETPSSIKSIDSSDSGIYEQAKRRRISPADTPVSESSSPLKSEVLA" +
                "QRDCEKNCAKDISGYYGFYSHS";
        String path = "data/test/TestFastaData";

        // When.
        try {
            String[] fastaData = SystemUtil.loadFasta(path);
            String fastaTitle = fastaData[0];
            String fastaContent = fastaData[1];

            // Then.
            assertEquals(expectedTitle, fastaTitle);
            assertEquals(expectedContent, fastaContent);
        } catch (Exception e) {
            fail("There should not be any exception.");
        }
    }

    @Test
    @DisplayName("Loading TestFastaData file with incorrect path")
    void loadFasta_wrongPath_throwError() {
        // Given.
        String path = "data/FASTA_does_not_exists.txt";

        // When.

        // Then.
        try {
            String[] fastaData = SystemUtil.loadFasta(path);
            fail("This code should never be executed.");
        } catch (FileNotFoundException fnfe) {
            assertEquals("java.io.FileNotFoundException: File did not load correctly.", fnfe.toString());
        } catch (Exception e) {
            fail("Any other exceptions are not expected.");
        }
    }

    @Test
    @DisplayName("Loading Alternate Amino File")
    void loadAlternateAmino() {
        // Given.
        String path = "data/test/TestAlternateAminosData";

        // When.
        try {
            ArrayList<Amino> aminoList = SystemUtil.loadAlternateAminos(path);

            // Then.
            assertEquals(aminoList.get(0).getLocation(), 674);
            assertEquals(aminoList.get(0).getData(), "ADF");
            assertEquals(aminoList.get(1).getLocation(), 23);
            assertEquals(aminoList.get(1).getData(), "PLK");
            assertEquals(aminoList.get(2).getLocation(), 2012);
            assertEquals(aminoList.get(2).getData(), "CFP");

        } catch (Exception e) {
            fail("There should not be any exception.");
        }
    }

    @Test
    @DisplayName("Loading Codon Frequencies File")
    void loadCodonFrequencies() {
        // Given.

        String path = "data/test/TestCodonFrequencyData";

        // When.
        try {
            HashMap<String, Integer> codonFrequencies = SystemUtil.loadCodonFrequencies(path);

            // Then.
            assertEquals(100000, codonFrequencies.get("ABC"));
            assertEquals(200000, codonFrequencies.get("XYZ"));

        } catch (Exception e) {
            fail("There should not be any exception.");
        }
    }

    @Test
    @DisplayName("Loading Degenerate Codon File")
    void loadDegenerateCodon() {
        // Given.

        String path = "data/test/TestDegenerateCodonData";

        // When.
        try {
            HashMap<String, String[][]> degenerateCodons = SystemUtil.loadDegenerateCodons(path);

            // Then.
            assertEquals("ABC", degenerateCodons.get("00000000000000000000")[0][0]);
            assertEquals("DEF", degenerateCodons.get("00000000000000000000")[0][1]);
            assertEquals("0.000", degenerateCodons.get("00000000000000000000")[1][0]);

            assertEquals("GHI", degenerateCodons.get("11111111111111111111")[0][0]);
            assertEquals("KLM", degenerateCodons.get("11111111111111111111")[0][1]);
            assertEquals("NPQ", degenerateCodons.get("11111111111111111111")[0][2]);
            assertEquals("RST", degenerateCodons.get("11111111111111111111")[0][3]);
            assertEquals("WXY", degenerateCodons.get("11111111111111111111")[0][4]);
            assertEquals("AAA", degenerateCodons.get("11111111111111111111")[0][5]);
            assertEquals("1.000", degenerateCodons.get("11111111111111111111")[1][0]);

        } catch (Exception e) {
            fail("There should not be any exception.");
        }
    }
}