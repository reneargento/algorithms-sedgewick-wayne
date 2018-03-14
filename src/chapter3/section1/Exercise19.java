package chapter3.section1;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/04/17.
 */
public class Exercise19 {

    public static void main(String[] args) {
        String[] words = {"Algorithms",
                "QuickSort",
                "Algorithms",
                "Union Find",
                "Djikstra",
                "Rabin",
                "Sedgewick",
                "QuickSort",
                "Djikstra",
                "Algorithms",
                "Djikstra",
                "QuickSort"};
        int minLength = 6;

        Queue<String> highestFrequencyWords = new Exercise19().frequencyCounter(words, minLength);
        for(String word : highestFrequencyWords) {
            StdOut.println(word);
        }

        StdOut.println("\nExpected: \nAlgorithms \nDjikstra \nQuickSort");
    }

    private Queue<String> frequencyCounter(String[] words, int minLength) {

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
        int highestFrequency = 0;

        for(String word : binarySearchSymbolTable.keys()) {
            if (binarySearchSymbolTable.get(word) > binarySearchSymbolTable.get(max)) {
                max = word;
                highestFrequency = binarySearchSymbolTable.get(word);
            }
        }

        Queue<String> highestFrequencyWords = new Queue<>();
        for(String word : binarySearchSymbolTable.keys()) {
            if (binarySearchSymbolTable.get(word) == highestFrequency) {
                highestFrequencyWords.enqueue(word);
            }
        }

        return highestFrequencyWords;
    }

}
