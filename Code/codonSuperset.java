// A Java program to find the minimum cardinality supersets by using fewer decodons for every set of amino acids.

import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.Scanner;

public class codonSuperset
{
	public codonSuperset()
	{
		//make a hash from AA subset decodons
		Map<Integer,minDecodon> decodonHash = makeDecodonHash();

		//create file to output to
		try{
		PrintStream o = new PrintStream(new File("codonSupersetOut.txt"));
		PrintStream console = System.out;
		System.setOut(o); }
		catch (FileNotFoundException e){
			e.printStackTrace();
		}

		//iterate over the hash
		for (Map.Entry<Integer,minDecodon> entry : decodonHash.entrySet()){
			int binaryCode = entry.getKey();
			int numDecodon = entry.getValue().getNum();
			String example = entry.getValue().getExample();

			if (numDecodon == 1)		// if the minimum number of degenerate codons is 1, continue
				continue;

			// create an array to save the maximum cardinality for each number of codons
			int[] minCardinality = new int[numDecodon - 1];

			// initialize the minimums to Integer.MAX_VALUE
			for (int i = 0; i < numDecodon - 1; i++){
				minCardinality[i] = Integer.MAX_VALUE;
			}		

			// create an array to save the best subsets for each number of codons
			String[] bestExamples = new String[numDecodon];
			bestExamples[numDecodon - 1] = example;

			// create an array to save the binary codes for the sets in bestExamples
			int[] minCardCodes = new int[numDecodon - 1];						

			for (Map.Entry<Integer,minDecodon> entry2 : decodonHash.entrySet()) {

				int tempCard = 0;
				boolean isSuperset = true;
				int binaryCode2 = entry2.getKey();
				int numDecodon2 = entry2.getValue().getNum();

				// if this potential superset uses the same number or more codons that the original set, disregard it
				if (numDecodon2 >= numDecodon)
					continue;

				int minCard = minCardinality[numDecodon2 - 1];

				// find the number of amino acids in this set
				// remember it has to contain all amino acids from the original set
				for (int i = 0; i < 20; i++)	{
					int bit1 = getBit(binaryCode,i);
					int bit2 = getBit(binaryCode2,i);

					// if the potential superset has a 1, add 1 to the cardinality
					if (bit2 == 1)
						tempCard++;

					// if original set 1 and new set 0, it's not a superset
					if ( (bit1 == 1) && (bit2 == 0) )
						isSuperset = false;
					

					// if both 0, do nothing						
				}
				//System.out.println(tempCard);
				if ( (isSuperset) && (tempCard < minCard) ){
					minCardinality[numDecodon2 - 1] = tempCard;
					minCardCodes[numDecodon2 - 1] = entry2.getKey();
					bestExamples[numDecodon2 - 1] = entry2.getValue().getExample();
				}
			}

			System.out.print(numDecodon +" "+Integer.toString(binaryCode,2)+" "+example+"	"); // re-output what was in the file

			// Print out the maximum cardinality subsets with an example
			int i = numDecodon - 1;
			while (i > 0){

				// if you cannot get the whole set with this number of decodons, skip
				if (minCardinality[i - 1] == Integer.MAX_VALUE) {
					i--;
					continue;
				}

				// If you can do as well or better with less codons, don't print the line
				boolean skipLine = false;
				for (int j = i - 1; j > 0; j--){
					if (minCardinality[j - 1] <= minCardinality[i - 1]){
						i = j;
						skipLine = true;
					}
				}

				if (!skipLine){
					System.out.print(i+" "+aminoAcidsToString(minCardCodes[i - 1])+" "+minCardinality[i - 1]+" "+bestExamples[i - 1]+"	");
					i--;
				}
			}			
		System.out.println();
		}				
	}

	public int getBit(int n, int k) {
    	return (n >> k) & 1;
	}

	// returns a string with the 1 letter representations of the amino acids in the binary code
	public String aminoAcidsToString(int binaryCode){
		StringBuilder str = new StringBuilder();

		if (getBit(binaryCode,0) == 1)
			str.append("A");
		if (getBit(binaryCode,1) == 1)
			str.append("R");
		if (getBit(binaryCode,2) == 1)
			str.append("N");
		if (getBit(binaryCode,3) == 1)
			str.append("D");
		if (getBit(binaryCode,4) == 1)
			str.append("C");
		if (getBit(binaryCode,5) == 1)
			str.append("Q");
		if (getBit(binaryCode,6) == 1)
			str.append("E");
		if (getBit(binaryCode,7) == 1)
			str.append("G");
		if (getBit(binaryCode,8) == 1)
			str.append("H");
		if (getBit(binaryCode,9) == 1)
			str.append("I");
		if (getBit(binaryCode,10) == 1)
			str.append("L");	
		if (getBit(binaryCode,11) == 1)
			str.append("K");
		if (getBit(binaryCode,12) == 1)
			str.append("M");
		if (getBit(binaryCode,13) == 1)
			str.append("F");
		if (getBit(binaryCode,14) == 1)
			str.append("P");
		if (getBit(binaryCode,15) == 1)
			str.append("S");
		if (getBit(binaryCode,16) == 1)
			str.append("T");
		if (getBit(binaryCode,17) == 1)
			str.append("W");
		if (getBit(binaryCode,18) == 1)
			str.append("Y");
		if (getBit(binaryCode,19) == 1)
			str.append("V");
		if (getBit(binaryCode,20) == 1)
			str.append("X");	

		return str.toString();																														
	}

	public HashMap makeDecodonHash()
	{
		HashMap<Integer,minDecodon> map = new HashMap<Integer,minDecodon>();

		File file = new File("AA_subset_decodons.txt");
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()){
				int num = sc.nextInt();
				int binaryCode = Integer.parseInt(sc.next(),2);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < num; i++)
				{
					sb.append(sc.next());
					sb.append(" ");
				}
				String example = sb.toString();
				map.put(binaryCode,new minDecodon(num,example));
			}
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String args[])
	{
		codonSuperset app = new codonSuperset();
	}	
}