public class TempInputImplementation implements GuiValues{
    public int getMinTemp() {return 1;}
    public int getMaxTemp() {return 100;}
    public int getMinLen() {return 15;}
    public int getMaxLen() {return 200;}
    public int getCostPerBase() {return 1;}
    public int getCostPerDegenerateBase() {return 2;}
    public String getFastaFilePath() {return "data/test/TestFastaData";}
    public String getAlternateAminoFilePath() {return "data/test/TestCodonFrequencyData";}
    public String getOrganism() {return "Human";}
}
