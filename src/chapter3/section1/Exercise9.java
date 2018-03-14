package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;
import util.FileUtil;

/**
 * Created by Rene Argento on 23/04/17.
 */
public class Exercise9 {

    public static void main(String[] args) {
        String filePath = args[0];
        new Exercise9().readBookAndGetLastWordInserted(filePath);
    }

    private void readBookAndGetLastWordInserted(String filePath) {
        int[] minLengths = {1, 8, 10};

        String[] words = FileUtil.getAllStringsFromFile(filePath);

        StdOut.printf("%12s %16s %16s\n", "Length cutoff", "Last word inserted", "Words processed");
        for(int i = 0; i < minLengths.length; i++) {
            frequencyCounter(words, minLengths[i]);
        }
    }

    private void frequencyCounter(String[] words, int minLength) {

        int totalWordsProcessed = 0;
        int wordsProcessedPriorToLastInsertion = 0;
        String lastWordInserted = "";

        BinarySearchSymbolTable<String, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        for(String word : words) {
            totalWordsProcessed++;

            if (word.length() < minLength) {
                continue;
            }

            if (!binarySearchSymbolTable.contains(word)) {
                binarySearchSymbolTable.put(word, 1);
            } else {
                binarySearchSymbolTable.put(word, binarySearchSymbolTable.get(word) + 1);
            }
            lastWordInserted = word;
            wordsProcessedPriorToLastInsertion = totalWordsProcessed - 1;
        }

        String max = "";
        binarySearchSymbolTable.put(max, 0);

        for(String word : binarySearchSymbolTable.keys()) {
            if (binarySearchSymbolTable.get(word) > binarySearchSymbolTable.get(max)) {
                max = word;
            }
        }

        printResults(minLength, lastWordInserted, wordsProcessedPriorToLastInsertion);
    }

    private void printResults(int lengthCutoff, String lastWordInserted, int wordsProcessedPriorToLastInsertion) {
        StdOut.printf("%13d %18s %16d\n", lengthCutoff, lastWordInserted, wordsProcessedPriorToLastInsertion);
    }

}
