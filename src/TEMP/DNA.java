import java.util.ArrayList;

public class DNA {
    private DNAdecodon[] decodonSet;

    public DNA(Protein protein, Organism organism) {


        DNAdecodon[] decodonSet = new DNAdecodon[aminoList.size()];
        for (int alternateAmino = 0; alternateAmino < aminoList.size(); alternateAmino++) {
            String aminoSequence = aminoList.get(alternateAmino).toBinary();
            Integer location = aminoList.get(alternateAmino).getLocation();
            String[] decodonList = organism.getDecodons(aminoSequence).getDecodonList();
            DNAdecodon dnaDecodon = new DNAdecodon(decodonList, location);
            decodonSet[alternateAmino] = dnaDecodon;
        }

        this.decodonSet = decodonSet;
    }
}