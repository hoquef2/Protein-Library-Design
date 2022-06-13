import java.lang.*;
import java.util.*;

public class MeltingTemp {
      static void calcTemp(String str) {
        int wA = 0; // number of A's in sequence
        int xT = 0; // number of T's in sequence
        int yG = 0; // number of G's in sequence
        int zC = 0; // number of C's in sequence
        double Na = 0.05; // sodium concentration (can be changed)

        String seq = str; // holds sequence

        char c; // will hold each char in sequence
        double temp = 0; // holds calculated temp

        seq = seq.replaceAll("\\s", ""); // gets rid of spaces in sequence

        // loops through each letter in sequence
        for(int i = 0; i < seq.length(); i++) {
            c = seq.charAt(i);

            if(c == 'A') {
                wA++;
            } else if(c == 'T') {
                xT++;
            } else if(c == 'G') {
                yG++;
            } else if(c == 'C') {
                zC++;
            } else {
                continue;
            }
        }

        // equation is different if sequence length is greater than 13
        if(seq.length() <= 13 ) {
            temp = (wA + xT)*2 + (yG + zC) *4 -(16.6 * Math.log10(.050)) + (16.6* Math.log10(Na));
        } else {
            temp = 64.9 + (41 * ((yG+zC-16.4) / (wA+xT+yG+zC)) );
        }

        System.out.println("Melting temp: ");
        // limits the number of decimal places
        System.out.format("%.1f", temp);
      }


      public static void main(String args[]){
          Scanner scan = new Scanner(System.in);
          System.out.println("Enter your sequence: ");
          String seq = scan.nextLine();
          calcTemp(seq);
      }
    
}


// reference website:
// http://biotools.nubic.northwestern.edu/OligoCalc.html
