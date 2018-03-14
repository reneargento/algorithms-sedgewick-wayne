package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;
import util.FileUtil;

/**
 * Created by Rene Argento on 23/04/17.
 */
public class Exercise8 {

    public static void main(String[] args) {
        String filePath = args[0];

        StdOut.println("Most frequently used word of ten letters or more in Tale of Two Cities: "
                + new Exercise8().readBookAndGetMaxFrequency(filePath));
    }

    private String readBookAndGetMaxFrequency(String filePath) {
        String[] words = FileUtil.getAllStringsFromFile(filePath);
        return frequencyCounter(words, 10);
    }

    private String frequencyCounter(String[] words, int minLength) {

        BinarySearchSymbolTable<String, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        for(String word : words) {
            if (word.length() < minLength) {
                continue;
            }

            if (!binarySearchSymbolTable.contains(word)) {
                binarySearchSymbolTable.put(word, 1);
            } else {
                binarySearchSymbolTable.put(word, binarySearchSymbolTable.get(word) + 1);
            }
        }

        String max = "";
        binarySearchSymbolTable.put(max, 0);

        for(String word : binarySearchSymbolTable.keys()) {
            if (binarySearchSymbolTable.get(word) > binarySearchSymbolTable.get(max)) {
                max = word;
            }
        }

        return max + " Frequency: " + binarySearchSymbolTable.get(max);
    }

}
