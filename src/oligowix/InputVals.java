
package oligowix;

public class InputVals extends javax.swing.JFrame implements GuiValues {
    int minTemp = 50;
    int maxTemp = 100;
    int minLen = 20;
    int maxLen = 50;
    float costPerBase;
    float costPerDegenerateBase;
    String alternateAminoFilePath;
    String organism;
    String fastaFilePath;

public InputVals(int minTemp,int maxTemp,int minLen,int maxLen,float costPerBase,float costPerDegenerateBase,
        String organism, String alternateAminoFilePath, String fastaFilePath) {

    this.minTemp = minTemp;
    this.maxTemp = maxTemp;
    this.minLen = minLen;
    this.maxLen = maxLen;
    this.costPerBase = costPerBase;
    this.costPerDegenerateBase = costPerDegenerateBase;
    this.alternateAminoFilePath = alternateAminoFilePath;
    this.organism = organism;
    this.fastaFilePath = fastaFilePath;
    
           
    
    
    
    
}

    

    @Override
    public int getMinTemp() {return minTemp;    }

    @Override
    public int getMaxTemp() {return maxTemp;    }

    @Override
    public int getMinLen() {return minLen;    }

    @Override
    public int getMaxLen() {return maxLen;    }

    @Override
    public float getCostPerBase() {return costPerBase;    }

    @Override
    public float getCostPerDegenerateBase() {return costPerDegenerateBase;    }

    @Override
    public String getFastaFilePath() {return fastaFilePath;    }

    @Override
    public String getAlternateAminoFilePath() {return alternateAminoFilePath;    }

    @Override
    public String getOrganism() {return organism;    }
    

    
}
