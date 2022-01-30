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

    private class Word {

        private String wordValue;
        private int frequency;
        private int orderFoundInFile;

        private Word(String wordValue) {
            this.wordValue = wordValue;
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

        BinarySearchSymbolTable<String, Word> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        int orderOfWordsFound = 0;

        for (String word : words) {
            if (!wordsSet.contains(word)) {
                continue;
            }

            if (!binarySearchSymbolTable.contains(word)) {
                Word newWord = new Word(word);
                newWord.orderFoundInFile = orderOfWordsFound;
                newWord.frequency = 1;
                orderOfWordsFound++;

                binarySearchSymbolTable.put(word, newWord);
            } else {
                Word curWord = binarySearchSymbolTable.get(word);
                curWord.frequency += 1;
                binarySearchSymbolTable.put(word, curWord);
            }
        }

        Word[] wordsFromSymbolTable = new Word[binarySearchSymbolTable.size()];
        int wordsArrayIndex = 0;
        for (String word : binarySearchSymbolTable.keys()) {
            wordsFromSymbolTable[wordsArrayIndex++] = binarySearchSymbolTable.get(word);
        }

        // Sort word arrays by frequency
        Arrays.sort(wordsFromSymbolTable, new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                return word1.frequency - word2.frequency;
            }
        });

        // Print results by frequency
        StdOut.println("Words sorted by frequency");
        StdOut.printf("%14s %15s %17s\n", "Word | ","Frequency | ", "Order found in file");
        printWordFrequencies(wordsFromSymbolTable);

        // Sort word arrays by order
        Arrays.sort(wordsFromSymbolTable, new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                return word1.orderFoundInFile - word2.orderFoundInFile;
            }
        });

        // Print results by order
        StdOut.println("\nWords sorted by order found in the dictionary file");
        StdOut.printf("%14s %15s %17s\n", "Word | ","Frequency | ", "Order found in file");
        printWordFrequencies(wordsFromSymbolTable);
    }

    private void printWordFrequencies(Word[] words) {
        for (Word word : words) {
            StdOut.printf("%11s %15d %22d\n", word.wordValue, word.frequency, word.orderFoundInFile);
        }
    }
}
