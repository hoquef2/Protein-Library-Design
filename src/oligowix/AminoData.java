/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oligowix;

import java.util.HashMap;

/**
 *
 * @author faiza
 */
public class AminoData {


    //name of amino acid. Eg: Serine
    private final String name;

    //Eg: Serine -> Ser
    private final String threeLetterAbbreviation;

    //Eg: Serine -> S
    private final Character oneLetterAbbreviation;

    //Eg: Serine -> {"AGC", "AGT", "TCA", "TCC", "TCG", "TCT"}
    private final String[] codons;

    //the codon with the minimum number of C/G's, which codes for the amino
    //stored as a three bit binary string. with C/G == 1 and A/T == 0 
    //Eg: Serine -> "010"
    private final String minCGcodonBinary;
    //the codon with the minimum number of C/G's, which codes for the amino
    //stored as a three bit binary string. with C/G == 1 and A/T == 0 
    //Eg: Serine -> "011"
    private final String maxCGcodonBinary;

    private AminoData(String name, String threeLetterAbbreviation, Character oneLetterAbbreviation
            , String[] codons, String minCGcodonBinary, String maxCGcodonBinary) {
        this.name = name;
        this.threeLetterAbbreviation = threeLetterAbbreviation;
        this.oneLetterAbbreviation = oneLetterAbbreviation;
        this.codons = codons;
        this.minCGcodonBinary = minCGcodonBinary;
        this.maxCGcodonBinary = maxCGcodonBinary;
    }



    public String getName() {
        return name;
    }

    public Character getOneLetterAbbreviation() {
        return oneLetterAbbreviation;
    }

    public String getThreeLetterAbbreviation() {
        return threeLetterAbbreviation;
    }

    public String[] getCodons() {
        return codons;
    }

    public String getMinCGcodonBinary() {
        return minCGcodonBinary;
    }

    public String getMaxCGcodonBinary() {
        return maxCGcodonBinary;
    }

    public static final Integer NUM_AMINOS = 20;
    public static final String aminoList = "ARNDCQEGHILKMFPSTWYV";


    private static final AminoData A = new AminoData("Alanine","Ala",'A',
            new String[]{"GCA", "GCC", "GCG", "GCT"},"110","111");
    private static final AminoData C = new AminoData("Cystein","Cys",'C',
            new String[]{"TGC", "TGT"},"010","011");
    private static final AminoData D = new AminoData("Aspartic Acid","Asp",'D',
            new String[]{"GAC", "GAT"},"100","101");
    private static final AminoData E = new AminoData("Glutamic Acid","Glu",'E',
            new String[]{"GAA", "GAG"},"100","101");
    private static final AminoData F = new AminoData("Phenilalanine","Phe",'F',
            new String[]{"TTC", "TTT"},"000","001");
    private static final AminoData G = new AminoData("Glycine","Gly",'G',
            new String[]{"GGA", "GGC", "GGG", "GGT"},"110","111");
    private static final AminoData H = new AminoData("Histidine","His",'H',
            new String[]{"CAC", "CAT"},"100","101");
    private static final AminoData I = new AminoData("Isoleucine","Ile",'I',
            new String[]{"ATA", "ATC", "ATT"},"000","001");
    private static final AminoData K = new AminoData("Lysine","Lys",'K',
            new String[]{"AAA", "AAG"},"000","001");
    private static final AminoData L = new AminoData("Leucine","Leu",'L',
            new String[]{"CTA", "CTC", "CTG", "CTT", "TTA", "TTG"},"000","101");
    private static final AminoData M = new AminoData("Metionine","Met",'M',
            new String[]{"ATG"},"001","001");
    private static final AminoData N = new AminoData("Asparagine","Asn",'N',
            new String[]{"AAC", "AAT"},"000","001");
    private static final AminoData P = new AminoData("Proline","Pro",'P',
            new String[]{"CCA", "CCC", "CCG", "CCT"},"110","111");
    private static final AminoData Q = new AminoData("Glutamine","Gln",'Q',
            new String[]{"CAA", "CAG"},"100","101");
    private static final AminoData R = new AminoData("Arginine","Arg",'R',
            new String[]{"AGA", "AGG", "CGA", "CGC", "CGG", "CGT"},"010","111");
    private static final AminoData S = new AminoData("Serine","Ser",'S',
            new String[]{"AGC", "AGT", "TCA", "TCC", "TCG", "TCT"},"010","011");
    private static final AminoData T = new AminoData("Threonine","Thr",'T',
            new String[]{"ACA", "ACC", "ACG", "ACT"},"010","011");
    private static final AminoData V = new AminoData("Valine","Val",'V',
            new String[]{"GTA", "GTC", "GTG", "GTT"},"100","101");
    private static final AminoData W = new AminoData("Tryptophan","Trp",'W',
            new String[]{"TGG"},"011","011");
    private static final AminoData Y = new AminoData("Tyrosine","Tyr",'Y',
            new String[]{"TAC", "TAT"},"000","001");

    //HashMap with a key of an amino acid in single letter form and a value of said amino acid
    public static final HashMap<Character, AminoData> aminoByLetter = initAminoByLetter();
    private static HashMap<Character, AminoData> initAminoByLetter() {
        HashMap<Character, AminoData> aminoByLetter = new HashMap<>();
        aminoByLetter.put('A',A); aminoByLetter.put('C',C); aminoByLetter.put('D',D); aminoByLetter.put('E',E);
        aminoByLetter.put('F',F); aminoByLetter.put('G',G); aminoByLetter.put('H',H); aminoByLetter.put('I',I);
        aminoByLetter.put('K',K); aminoByLetter.put('L',L); aminoByLetter.put('M',M); aminoByLetter.put('N',N);
        aminoByLetter.put('P',P); aminoByLetter.put('Q',Q); aminoByLetter.put('R',R); aminoByLetter.put('S',S);
        aminoByLetter.put('T',T); aminoByLetter.put('V',V); aminoByLetter.put('W',W); aminoByLetter.put('Y',Y);
        return aminoByLetter;
    }


    //HashMap with a codon key and amino acid value 
    public static final HashMap<String, AminoData> aminoByCodon = initAminoByCodon();
    private static HashMap<String, AminoData> initAminoByCodon() {
        HashMap<String, AminoData> aminoByCodon = new HashMap<>();
        aminoByCodon.put("TTT", F); aminoByCodon.put("TTC", F); aminoByCodon.put("TTA", L); aminoByCodon.put("TTG", L);
        aminoByCodon.put("TCT", S); aminoByCodon.put("TCC", S); aminoByCodon.put("TCA", S); aminoByCodon.put("TCG", S);
        aminoByCodon.put("TAT", Y); aminoByCodon.put("TAC", Y); aminoByCodon.put("TGT", C); aminoByCodon.put("TGC", C);
        aminoByCodon.put("TGG", W); aminoByCodon.put("CTT", L); aminoByCodon.put("CTC", L); aminoByCodon.put("CTA", L);
        aminoByCodon.put("CTG", L); aminoByCodon.put("CCT", P); aminoByCodon.put("CCC", P); aminoByCodon.put("CCA", P);
        aminoByCodon.put("CCG", P); aminoByCodon.put("CAT", H); aminoByCodon.put("CAC", H); aminoByCodon.put("CAA", Q);
        aminoByCodon.put("CAG", Q); aminoByCodon.put("CGT", R); aminoByCodon.put("CGC", R); aminoByCodon.put("CGA", R);
        aminoByCodon.put("CGG", R); aminoByCodon.put("ATT", I); aminoByCodon.put("ATC", I); aminoByCodon.put("ATA", I);
        aminoByCodon.put("ATG", M); aminoByCodon.put("ACT", T); aminoByCodon.put("ACC", T); aminoByCodon.put("ACA", T);
        aminoByCodon.put("ACG", T); aminoByCodon.put("AAT", N); aminoByCodon.put("AAC", N); aminoByCodon.put("AAA", K);
        aminoByCodon.put("AAG", K); aminoByCodon.put("AGT", S); aminoByCodon.put("AGC", S); aminoByCodon.put("AGA", R);
        aminoByCodon.put("AGG", R); aminoByCodon.put("GTT", V); aminoByCodon.put("GTC", V); aminoByCodon.put("GTA", V);
        aminoByCodon.put("GTG", V); aminoByCodon.put("GCT", A); aminoByCodon.put("GCC", A); aminoByCodon.put("GCA", A);
        aminoByCodon.put("GCG", A); aminoByCodon.put("GAT", D); aminoByCodon.put("GAC", D); aminoByCodon.put("GAA", E);
        aminoByCodon.put("GAG", E); aminoByCodon.put("GGT", G); aminoByCodon.put("GGC", G); aminoByCodon.put("GGA", G);
        aminoByCodon.put("GGG", G);
        return aminoByCodon;
    }
}

    
 
