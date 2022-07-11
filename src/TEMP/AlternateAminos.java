package com.tcnj.oligonukes;

public class AlternateAminos {
    private String alternateAminos;
    private Integer location;
    
    public AlternateAminos(String alternateAminos, Integer location) {
        this.alternateAminos = alternateAminos;
        this.location = location;
    }

    public AlternateAminos(String alternateAminos) {
        this.alternateAminos = alternateAminos;
        this.location = null;
    }
    
    public String getAminos() {return alternateAminos;}
    public Integer getLocation() {return location;}
    
    //returns the alternate amino acid stored at the given location
    public char aminoAt(Integer location) {return alternateAminos.charAt(location);}
    
    //returns the number of alternate amino acids stored
    public int getSize() {return alternateAminos.length();}

    //converts a binary number into an alternate amino object
    public static AlternateAminos toAlternateAminos(String binaryNumber) {

        StringBuffer aminoStringBuffer = new StringBuffer();
        for (int currentAmino = 0; currentAmino < Amino3.NUM_AMINOS; currentAmino++) {
            if (binaryNumber.charAt(currentAmino) == '1') {
                //19 is the length of the com.tcnj.oligonukes.Amino.aminoList string, and its there because we need to cycle through
                // the list backwards since the binary numbers being processed store the aminos in reverse
                //alphabetical order
                aminoStringBuffer.append(Amino3.aminoList.charAt(19 - currentAmino));
            }
        }
        return new AlternateAminos(aminoStringBuffer.toString());

    }

    //converts the amino acid sequence of the alternate amino object into a binary number
    public String toBinary() {

        //binary representation stored as an Integer[]
        Integer binaryAminoNum = 0;

        //goes through all 20 aminos
        for (int currAminoIndex = 0; currAminoIndex < Amino3.NUM_AMINOS; currAminoIndex++) {
            //goes through every amino in the alternate amino list
            for (int currAltAminoIndex = 0; currAltAminoIndex < alternateAminos.length(); currAltAminoIndex++) {

                //checks if the current com.tcnj.oligonukes.Amino in the alternate amino list is equivalent to the current amino in the amino list
                if (aminoAt(currAltAminoIndex) == Amino3.aminoList.charAt(currAminoIndex)) {
                    binaryAminoNum += (int) Math.pow(2, currAminoIndex);
                }
            }


        }
        String binaryAminoString = "";

        Integer numFiller = 20 - Integer.toBinaryString(binaryAminoNum).length();
        for (int j = 0; j < numFiller; j++) {
            binaryAminoString += "0";
        }
        binaryAminoString += Integer.toBinaryString(binaryAminoNum);

        return binaryAminoString;
    }
}

