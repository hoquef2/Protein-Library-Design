import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SystemUtil {
    public static String[] loadFasta(String path) throws FileNotFoundException {
        String title = null;
        String content = null;
        
        try (FileReader fastaFileReader = new FileReader(path)) {
            StringBuffer rawData = new StringBuffer();
            
            int i;
            while ((i = fastaFileReader.read()) != -1) {
                char currentChar = (char) i;
                rawData.append(currentChar);
            }
            int startOfTitle = rawData.indexOf(">");
            int firstLineBreak = Math.min(rawData.indexOf("\r\n"), Math.min(rawData.indexOf("\r"), rawData.indexOf("\n")));
            title = rawData.substring(startOfTitle + 1, firstLineBreak);

            content = rawData.substring(firstLineBreak + 1);
            content = content.replaceAll("\\s", "");
        } catch (IOException e) {
            String errorMessage = "File did not load correctly.";
            System.err.println(errorMessage);
            throw new FileNotFoundException(errorMessage);
        }
        return new String[] {title, content};
    }
    
    public static HashMap<Integer, String> loadAlternateAminos(String path) {
        HashMap<Integer, String> locationAaPairs = new HashMap<>();
        
        try (FileReader alternateAminosFileReader = new FileReader(path)) {
            
            StringBuffer rawData = new StringBuffer();

            //TODO remove redundant FileReader
            int i;
            while ((i = alternateAminosFileReader.read()) != -1) {

                char currentChar = (char) i;
                rawData.append(currentChar);
            }
            String rawDataString = rawData.toString();
            Scanner dataScanner = new Scanner(rawDataString);
            ArrayList<String> data = new ArrayList<String>();
            while (dataScanner.hasNext()) {
                data.add(dataScanner.next());
            }

            //parsing data to pull out position and value information
            //data stores both position and value information, so one must divide by two in order to parse
            for (i = 0; i < data.size() / 2; i++) {
                Integer position = Integer.parseInt(data.get(i * 2));
                String value = data.get((i * 2) + 1);
                locationAaPairs.put(position, value);
            }
        } catch (IOException e) {
            String errorMessage = "File did not load correctly.";
            System.err.println(errorMessage);
        }
        return locationAaPairs;
    }

    public static HashMap<String, Integer> loadCodonFrequencies(String path) {
        
        //86 is perfect size for this array, as there are 64 unique codons
        //and Hashtables have a default resize factor of 1.33
        HashMap<String, Integer> codonFrequencies = new HashMap<>(86);
        
        //populating codonAmounts
        File codonFrequenciesFile = new File(path);
        try (Scanner fileScanner = new Scanner(codonFrequenciesFile)) {

            while(fileScanner.hasNext()) {
                fileScanner.next();
                String codon = fileScanner.next();
                String amount = fileScanner.next();
                fileScanner.next();
                fileScanner.next();
                codonFrequencies.put(codon, Math.round(Float.valueOf(amount)));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return codonFrequencies;
    }

    public static HashMap<String, String[][]> loadDegenerateCodons(String path) {
        
        
        HashMap <String, String[][]> degenerateCodons = new HashMap<String, String[][]>(1394605);
        
        File degenerateCodonFile = new File(path);
        try (Scanner fileScanner = new Scanner(degenerateCodonFile)) {

            int currentLine = 0;
            while(fileScanner.hasNext()) {

                Integer numCodons = Integer.valueOf(fileScanner.next());
                String aminoList = fileScanner.next();
                String[] decodonList = new String[numCodons];
                for (int i = 0; i < numCodons; i++) {
                    decodonList[i] = fileScanner.next();
                }
                String correctnessFactor = fileScanner.next();

                String[][] content = new String[2][numCodons];
                content[0] = decodonList;
                content[1][0] = correctnessFactor;
                degenerateCodons.put(aminoList, content);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return degenerateCodons;
    }
}


