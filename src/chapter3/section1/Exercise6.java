package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rene Argento on 23/04/17.
 */
public class Exercise6 {

    public static void main(String[] args) {
        String[] words = {"Test0",
                "Test1",
                "Test2",
                "Test3",
                "Test2",
                "Algorithms",
                "MergeSort",
                "QuickSort",
                "HeapSort",
                "MergeSort",
                "MergeSort",
                "Stack",
                "Queue",
                "List",
                "Abc",
                "Test99"};
        int minLength = 4;

        Exercise6 exercise6 = new Exercise6();

        int[] wordsAndDistinctWordsCount = exercise6.countWordsAndDistinctWords(words);
        int totalWords = wordsAndDistinctWordsCount[0];
        int distinctWords = wordsAndDistinctWordsCount[1];

        int[] callsToPutAndGet = exercise6.frequencyCounter(words, minLength);
        int callsToPut = callsToPutAndGet[0];
        int callsToGet = callsToPutAndGet[1];

        double callsToPutAsAFunctionOfWords = ((double) callsToPut) / ((double) totalWords);
        double callsToPutAsAFunctionOfDistinctWords = ((double) callsToPut) / ((double) distinctWords);
        double callsToGetAsAFunctionOfWords = ((double) callsToGet) / ((double) totalWords);
        double callsToGetAsAFunctionOfDistinctWords = ((double) callsToGet) / ((double) distinctWords);

        StdOut.printf("Calls to put() as a function of words: %.2f", callsToPutAsAFunctionOfWords);
        StdOut.printf("\nCalls to put() as a function of distinct words: %.2f", callsToPutAsAFunctionOfDistinctWords);
        StdOut.printf("\nCalls to get() as a function of words: %.2f", callsToGetAsAFunctionOfWords);
        StdOut.printf("\nCalls to get() as a function of distinct words: %.2f", callsToGetAsAFunctionOfDistinctWords);
    }

    private int[] countWordsAndDistinctWords(String[] words) {
        Set<String> distinctWords = new HashSet<>();

        for(String word : words) {
            distinctWords.add(word);
        }

        return new int[]{words.length, distinctWords.size()};
    }

    private int[] frequencyCounter(String[] words, int minLength) {
        int callsToPut = 0;
        int callsToGet = 0;

        BinarySearchSymbolTable<String, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        for(String word : words) {
            if (word.length() < minLength) {
                continue;
            }

            if (!binarySearchSymbolTable.contains(word)) {
                binarySearchSymbolTable.put(word, 1);
            } else {
                binarySearchSymbolTable.put(word, binarySearchSymbolTable.get(word) + 1);
                callsToGet++;
            }
            callsToPut++;
        }

        String max = "";
        binarySearchSymbolTable.put(max, 0);
        callsToPut++;

        for(String word : binarySearchSymbolTable.keys()) {
            callsToGet += 2;
            if (binarySearchSymbolTable.get(word) > binarySearchSymbolTable.get(max)) {
                max = word;
            }
        }

        return new int[]{callsToPut, callsToGet};
    }

}
