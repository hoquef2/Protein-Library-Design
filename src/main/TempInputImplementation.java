public class TempInputImplementation implements GuiValues {
    public int getMinTemp() {
        return 60;
    }

    public int getMaxTemp() {
        return 80;
    }

    public int getMinLen() {
        return 40;
    }

    public int getMaxLen() {
        return 90;
    }

    public Float getCostPerBase() {
        return 0.38F;
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
