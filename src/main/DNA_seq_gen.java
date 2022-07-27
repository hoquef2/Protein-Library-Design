import java.util.Random;

public class DNA_seq_gen {

    public static void DnaSeqGen(Integer numCodon) {
        System.out.print("{");
        for (int i = 0; i < numCodon; i++) {
            Character currDNA;
            Random randomNum = new Random();

            System.out.print("\"");
            for (int j = 0; j < 3; j++) {
                Integer randomInt = randomNum.nextInt(4);

                switch (randomInt) {
                    case 0:
                        currDNA = 'A';
                        break;
                    case 1:
                        currDNA = 'T';
                        break;
                    case 2:
                        currDNA = 'C';
                        break;
                    case 3:
                        currDNA = 'G';
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + randomInt);

                }
                System.out.print(currDNA);
            }
            System.out.print("\"");
            if (i != (numCodon - 1)) {
                System.out.print(", ");
            }

        }
        System.out.println("}");

    }

    public static void main(String[] args) {
        DnaSeqGen(50);

    }
}
