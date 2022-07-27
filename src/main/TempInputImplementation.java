public class TempInputImplementation implements GuiValues {
    public int getMinTemp() {
        return 40;
    }

    public int getMaxTemp() {
        return 100;
    }

    public int getMinLen() {
        return 15;
    }

    public int getMaxLen() {
        return 35;
    }

    public Float getCostPerBase() {
        return 0.1F;
    }

    public Float getCostPerDegenerateBase() {
        return 0.5F;
    }

    public String getFastaFilePath() {
        return "data/test/TestFastaData";
    }

    public String getAlternateAminoFilePath() {
        return "data/test/TestCodonFrequencyData";
    }

    public String getOrganism() {
        return "Human";
    }
}
