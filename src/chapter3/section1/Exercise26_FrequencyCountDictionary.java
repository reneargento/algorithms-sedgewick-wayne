package chapter3.section1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;
import util.FileUtil;

import java.util.*;

/**
 * Created by Rene Argento on 27/04/17.
 */
public class Exercise26_FrequencyCountDictionary {

    private class Word implements Comparable<Word>{

        private String wordValue;
        private int frequency;
        private int orderFoundInFile;

        private Word(String wordValue) {
            this.wordValue = wordValue;
        }

        @Override
        public int compareTo(Word that) {
            return wordValue.compareTo(that.wordValue);
        }
    }

    private static final String DICTIONARY_FILE_PATH = Constants.FILES_PATH + "3.1.26_Dictionary.txt";

    public static void main(String[] args) {
        /**
         * Sample input
         *
         Algorithms Algorithms
         Djikstra Rene Dictionary Quicksort
         Mergesort Binary Search
         Heap Algorithms
         Graphs Graphs
         Rene Test
         Algorithms Algorithms
         */
        String[] words = StdIn.readAllStrings();
        new Exercise26_FrequencyCountDictionary().frequencyCounter(DICTIONARY_FILE_PATH, words);
    }

    private void frequencyCounter(String dictionaryFile, String[] words) {

        String[] wordsInDictionary = FileUtil.getAllStringsFromFile(dictionaryFile);

        if (wordsInDictionary == null) {
            return;
        }

        Set<String> wordsSet = new HashSet<>();
        Collections.addAll(wordsSet, wordsInDictionary);

        BinarySearchSymbolTable<Word, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        int orderOfWordsFound = 0;

        for(String word : words) {

            if (!wordsSet.contains(word)) {
                continue;
            }

            Word newWord = new Word(word);

            if (!binarySearchSymbolTable.contains(newWord)) {
                newWord.orderFoundInFile = orderOfWordsFound;
                orderOfWordsFound++;

                binarySearchSymbolTable.put(newWord, 1);
            } else {
                binarySearchSymbolTable.put(newWord, binarySearchSymbolTable.get(newWord) + 1);
            }
        }

        Word[] wordsSortedByFrequency = new Word[binarySearchSymbolTable.size()];
        int wordsArrayIndex = 0;
        for(Word word : binarySearchSymbolTable.keys()) {
            word.frequency = binarySearchSymbolTable.get(word);

            wordsSortedByFrequency[wordsArrayIndex++] = word;
        }

        Word[] wordsSortedByOrderFound = new Word[binarySearchSymbolTable.size()];
        System.arraycopy(wordsSortedByFrequency, 0, wordsSortedByOrderFound, 0, wordsSortedByFrequency.length);

        //Sort word arrays
        Arrays.sort(wordsSortedByFrequency, new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                return word1.frequency - word2.frequency;
            }
        });
        Arrays.sort(wordsSortedByOrderFound, new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                return word1.orderFoundInFile - word2.orderFoundInFile;
            }
        });

        //Print results
        StdOut.println("Words sorted by frequency");
        StdOut.printf("%14s %15s %17s\n", "Word | ","Frequency | ", "Order found in file");
        printWordFrequencies(wordsSortedByFrequency);

        StdOut.println("\nWords sorted by order found in the dictionary file");
        StdOut.printf("%14s %15s %17s\n", "Word | ","Frequency | ", "Order found in file");
        printWordFrequencies(wordsSortedByOrderFound);
    }

    private void printWordFrequencies(Word[] words) {
        for(Word word : words) {
            StdOut.printf("%11s %15d %22d\n", word.wordValue, word.frequency, word.orderFoundInFile);
        }
    }

}
