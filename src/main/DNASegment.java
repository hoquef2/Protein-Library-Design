public class DNASegment {
    private String[] sequences;
    private int startIndex;
    private int numVersions;

    public DNASegment (int startIndex, int numVersions, String[] sequences) {
        this.startIndex = startIndex;
        this.sequences = sequences;
        this.numVersions = numVersions;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getNumVersions() {
        return numVersions;
    }

    public String getSequence(int sequenceNum) {
        if(sequenceNum <= numVersions) {
            return sequences[sequenceNum];
        }
        return null;
    }

    public int getLength() {
        return sequences[0].length();
    }
}
