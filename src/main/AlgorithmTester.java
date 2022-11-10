import java.io.FileNotFoundException;

public class AlgorithmTester {

    public static void main(String[] args) throws FileNotFoundException {
        String protein = SystemUtil.loadFasta("data/test/TestFastaData")[1];
        System.out.println(protein);
    }
}