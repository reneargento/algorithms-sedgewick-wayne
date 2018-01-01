package chapter3.section5;

import chapter3.section3.RedBlackBST;
import chapter3.section4.LinearProbingHashTable;
import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.Constants;
import util.FileUtil;

/**
 * Created by Rene Argento on 19/08/17.
 */
public class Exercise31_SpellChecker {

    private class BlackFilter {

        public HashSet<String> filterUsingRedBlackBST(String filePath) {
            HashSet<String> filteredWords = new HashSet<>();

            //The value is not used, but it is required by the red-black BST
            RedBlackBST<String, Boolean> redBlackBST = new RedBlackBST<>();

            In in = new In(filePath);
            while (!in.isEmpty()) {
                redBlackBST.put(in.readString(), true);
            }

            String[] allWords = FileUtil.getAllStringsFromFile(WARS_AND_PEACE_FILE_PATH);

            for(String word : allWords) {
                if(!redBlackBST.contains(word)) {
                    filteredWords.add(word);
                }
            }

            return filteredWords;
        }

        public HashSet<String> filterUsingSeparateChainingHashST(String filePath) {
            HashSet<String> filteredWords = new HashSet<>();

            //The value is not used, but it is required by the separate chaining hash table
            SeparateChainingHashTable<String, Boolean> separateChainingHashTable = new SeparateChainingHashTable<>();

            In in = new In(filePath);
            while (!in.isEmpty()) {
                separateChainingHashTable.put(in.readString(), true);
            }

            String[] allWords = FileUtil.getAllStringsFromFile(WARS_AND_PEACE_FILE_PATH);

            for(String word : allWords) {
                if(!separateChainingHashTable.contains(word)) {
                    filteredWords.add(word);
                }
            }

            return filteredWords;
        }

        public HashSet<String> filterUsingLinearProbingHashST(String filePath) {
            HashSet<String> filteredWords = new HashSet<>();

            //The value is not used, but it is required by the linear probing hash table
            LinearProbingHashTable<String, Boolean> linearProbingHashTable = new LinearProbingHashTable<>(997);

            In in = new In(filePath);
            while (!in.isEmpty()) {
                linearProbingHashTable.put(in.readString(), true);
            }

            String[] allWords = FileUtil.getAllStringsFromFile(WARS_AND_PEACE_FILE_PATH);

            for(String word : allWords) {
                if(!linearProbingHashTable.contains(word)) {
                    filteredWords.add(word);
                }
            }

            return filteredWords;
        }

    }

    //There was no dictionary.txt file on the booksite, so I suspect it has been renamed to commonwords.txt
   // private static final String DICTIONARY_FILE_PATH = Constants.FILES_PATH + "common_words.txt";
    private static final String WARS_AND_PEACE_FILE_PATH = Constants.FILES_PATH + "war_and_peace.txt";

    //Command line argument:
    // /Users/rene/Desktop/Algorithms/Books/Algorithms, 4th ed. - Exercises/Data/common_words.txt

    private void doExperiment(String[] args) {
        String commonWordsFilePath = args[0];

        BlackFilter blackFilter = new BlackFilter();

        Stopwatch stopwatch = new Stopwatch();
        blackFilter.filterUsingRedBlackBST(commonWordsFilePath);
        double timeSpentWithRedBlackBST = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        blackFilter.filterUsingSeparateChainingHashST(commonWordsFilePath);
        double timeSpentWithSeparateChainingST = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        blackFilter.filterUsingLinearProbingHashST(commonWordsFilePath);
        double timeSpentWithLinearProbingST = stopwatch.elapsedTime();

        StdOut.printf("%20s %20s %20s\n", "Time spent red-black BST | ", "Time spent separate chaining | ",
                "Time spent linear probing");
        printResults(timeSpentWithRedBlackBST, timeSpentWithSeparateChainingST, timeSpentWithLinearProbingST);
    }

    private void printResults(double redBlackBSTTime, double separateChainingTime, double linearProbingTime) {
        StdOut.printf("%24.2f %31.2f %28.2f\n", redBlackBSTTime, separateChainingTime, linearProbingTime);
    }

    public static void main(String[] args) {
        new Exercise31_SpellChecker().doExperiment(args);
    }

}
