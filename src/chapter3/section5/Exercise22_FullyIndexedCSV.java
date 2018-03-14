package chapter3.section5;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rene Argento on 15/08/17.
 */
@SuppressWarnings("unchecked")
public class Exercise22_FullyIndexedCSV {

    private class FullLookupCSV {

        private Map<String, List<String>>[] hashMapArray;

        public void buildHashMapArray(String csvFilePath) {
            In in = new In(csvFilePath);

            boolean isFirstLine = true;

            while (in.hasNextLine()) {
                String line = in.readLine();
                String[] tokens = line.split(",");

                if (isFirstLine) {
                    hashMapArray = (HashMap<String, List<String>>[]) new HashMap[tokens.length];

                    for(int i = 0; i < hashMapArray.length; i++) {
                        hashMapArray[i] = new HashMap<>();
                    }

                    isFirstLine = false;
                }

                for(int keyField = 0; keyField < tokens.length; keyField++) {
                    List<String> values = new ArrayList<>();

                    for(int valueField = 0; valueField < tokens.length; valueField++) {
                        if (valueField != keyField) {
                            values.add(tokens[valueField]);
                        }
                    }

                    hashMapArray[keyField].put(tokens[keyField], values);
                }
            }
        }

        public String get(int keyField, int valueField, String query) {

            if (keyField < 0 || valueField < 0) {
                throw new IllegalArgumentException("Fields must be equal or higher than 0");
            }

            if (keyField == valueField) {
                return query;
            } else if (keyField < valueField) {
                valueField--;
            }

            return hashMapArray[keyField].get(query).get(valueField);
        }
    }

    // Parameters example: csv_file.txt
    public static void main(String[] args) {
        //CSV file - csv_file.txt
        // rene,1,abc
        // sedgewick,9,aaa
        // dijkstra,10,dgs
        // rene,3,asfa
        // wayne,9,lpa
        // rene,5,lll
        // wayne,10,zzp
        // arnold,200,aab
        // dwayne,201,bba
        // fenwick,202,bbc

        //Queries
        // 0 1 wayne
        // 0 2 rene
        // 1 0 3
        // 1 2 200
        // 2 0 aaa
        // 2 1 lpa
        // 0 0 fenwick

        //Expected output
        // 10
        // lll
        // rene
        // aab
        // sedgewick
        // 9
        // fenwick

        String csvFilePath = Constants.FILES_PATH + args[0];

        Exercise22_FullyIndexedCSV fullyIndexedCSV = new Exercise22_FullyIndexedCSV();
        FullLookupCSV fullLookupCSV = fullyIndexedCSV.new FullLookupCSV();
        fullLookupCSV.buildHashMapArray(csvFilePath);

        while (StdIn.hasNextLine()) {
            String line = StdIn.readLine();
            String[] words = line.split(" ");

            int keyField = Integer.parseInt(words[0]);
            int valueField = Integer.parseInt(words[1]);
            String query = words[2];

            StdOut.println(fullLookupCSV.get(keyField, valueField, query));
        }
    }

}
