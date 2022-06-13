// A Java program to find the maximum cardinality subsets using fewer decodons for each set of amino acids.

import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.Scanner;

public class codonSubset
{
	public codonSubset()
	{
		//make a hash from AA subset decodons
		Map<Integer,minDecodon> decodonHash = makeDecodonHash();

		/* //print whole hash
		for (Map.Entry<Integer,minDecodon> entry : decodonHash.entrySet()){
			System.out.println(entry.getKey() + " = " + entry.getValue());
		} */

		//create file to output to
		try{
		PrintStream o = new PrintStream(new File("codonSubsetOut.txt"));
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

			System.out.print(numDecodon +" "+Integer.toString(binaryCode,2)+" "+example+"	"); // re-output what was in the file

			if (numDecodon == 1)		// if the minimum number of degenerate codons is 1, continue
				continue;

			// create an array to save the maximum cardinality for each number of codons
			int[] maxCardinality = new int[numDecodon];
			maxCardinality[numDecodon - 1] = numDecodon; // The cardinality of the whole set

			// create an array to save the best subsets for each number of codons
			String[] bestExamples = new String[numDecodon];
			bestExamples[numDecodon - 1] = example;

			// create an array to save the binary codes for the best subsets
			int[] maxCardCodes = new int[numDecodon];
			maxCardCodes[numDecodon - 1] = binaryCode;

			// otherwise loop through fewer codons
			for (int c = 1; c < numDecodon; c++)
			{
				// otherwise lets search through ALL of the amino acid sets with c codons. then find the one with maximum cardinality.
				int maxCard = 0;
				int maxCardCode = 0;
				String bestSubset = "";
				for (Map.Entry<Integer,minDecodon> entry2 : decodonHash.entrySet()){
					int tempCard = 0;
					boolean isSubset = true;
					int newNumDecodon = entry2.getValue().getNum();
					if (newNumDecodon != c)
						continue;

					// compare the bits in the original string and the test string
					for (int i = 0; i < 20; i++)	{
						int bit1 = getBit(binaryCode,i);
						int bit2 = getBit(entry2.getKey(),i);

						// if both are 1, add 1 to the count
						if ((bit1 == 1) && (bit2 == 1) )
							tempCard++;
						// if original 0, new 1, it's not a subset
						if ((bit1 == 0) && (bit2 == 1))
							isSubset = false;
						// if original 1, new 0, do nothing
						// if original and new 0, do nothing						
					}

					if ( (tempCard > maxCard) && (isSubset) ){
						maxCard = tempCard;
						maxCardCode = entry2.getKey();
						bestSubset = entry2.getValue().getExample();
					}
				}
				//System.out.print(c+"codon:Card"+maxCard+" "+bestSubset.getExample()+" ");
				maxCardinality[c -1] = maxCard;
				bestExamples[c - 1] = bestSubset;
				maxCardCodes[c - 1] = maxCardCode;
			}

			// Print out the maximum cardinality subsets with an example
			int i = numDecodon - 1;
			while (i > 0){

				// If you can do as well or better with less codons, don't print the line
				boolean skipLine = false;
				for (int j = i - 1; j > 0; j--){
					if (maxCardinality[j - 1] >= maxCardinality[i - 1]){
						i = j;
						skipLine = true;
					}
				}

				if (!skipLine){
					System.out.print(i+" "+aminoAcidsToString(maxCardCodes[i - 1])+" "+maxCardinality[i - 1]+" "+bestExamples[i - 1]+"	");
					i--;
				}
			}
		System.out.println();
		}
	}


	public int getBit(int n, int k) {
    	return (n >> k) & 1;
	}

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

	public int[] getOnePositions(int num,int numOnes)
	{
		String str = Integer.toString(num);
		int pos = 0;
		int[] arr = new int[numOnes];

		for (int i = 0; i < str.length(); i++){
			if (str.charAt(i) == '1')
			{
				arr[pos] = i;
				pos++;
			}
		}

		return arr;
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
		codonSubset app = new codonSubset();
	}
}