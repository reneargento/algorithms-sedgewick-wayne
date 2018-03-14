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

        public HashSet<String> filterUsingRedBlackBST(String dictionaryFilePath, String warAndPeaceFilePath) {
            HashSet<String> filteredWords = new HashSet<>();

            // The value field is not used, but it is required by the red-black BST
            RedBlackBST<String, Boolean> redBlackBST = new RedBlackBST<>();

            In in = new In(dictionaryFilePath);
            while (!in.isEmpty()) {
                redBlackBST.put(in.readString(), true);
            }

            String[] allWords = FileUtil.getAllStringsFromFile(warAndPeaceFilePath);

            if (allWords == null) {
                return filteredWords;
            }

            for(String word : allWords) {
                if (!redBlackBST.contains(word)) {
                    filteredWords.add(word);
                }
            }

            return filteredWords;
        }

        public HashSet<String> filterUsingSeparateChainingHashST(String dictionaryFilePath, String warAndPeaceFilePath) {
            HashSet<String> filteredWords = new HashSet<>();

            // The value field is not used, but it is required by the separate chaining hash table
            SeparateChainingHashTable<String, Boolean> separateChainingHashTable = new SeparateChainingHashTable<>();

            In in = new In(dictionaryFilePath);
            while (!in.isEmpty()) {
                separateChainingHashTable.put(in.readString(), true);
            }

            String[] allWords = FileUtil.getAllStringsFromFile(warAndPeaceFilePath);

            if (allWords == null) {
                return filteredWords;
            }

            for(String word : allWords) {
                if (!separateChainingHashTable.contains(word)) {
                    filteredWords.add(word);
                }
            }

            return filteredWords;
        }

        public HashSet<String> filterUsingLinearProbingHashST(String dictionaryFilePath, String warAndPeaceFilePath) {
            HashSet<String> filteredWords = new HashSet<>();

            // The value field is not used, but it is required by the linear probing hash table
            LinearProbingHashTable<String, Boolean> linearProbingHashTable = new LinearProbingHashTable<>(997);

            In in = new In(dictionaryFilePath);
            while (!in.isEmpty()) {
                linearProbingHashTable.put(in.readString(), true);
            }

            String[] allWords = FileUtil.getAllStringsFromFile(warAndPeaceFilePath);

            if (allWords == null) {
                return filteredWords;
            }

            for(String word : allWords) {
                if (!linearProbingHashTable.contains(word)) {
                    filteredWords.add(word);
                }
            }

            return filteredWords;
        }

    }

    // There was no dictionary.txt file on the booksite, so I suspect it has been renamed to commonwords.txt
    // Parameter example: common_words.txt

    private void doExperiment(String[] args) {
        String dictionaryFileName = args[0];
        String dictionaryFilePath = Constants.FILES_PATH + dictionaryFileName;

        String warAndPeaceFilePath = Constants.FILES_PATH + Constants.WAR_AND_PEACE_FILE;

        BlackFilter blackFilter = new BlackFilter();

        Stopwatch stopwatch = new Stopwatch();
        blackFilter.filterUsingRedBlackBST(dictionaryFilePath, warAndPeaceFilePath);
        double timeSpentWithRedBlackBST = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        blackFilter.filterUsingSeparateChainingHashST(dictionaryFilePath, warAndPeaceFilePath);
        double timeSpentWithSeparateChainingST = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        blackFilter.filterUsingLinearProbingHashST(dictionaryFilePath, warAndPeaceFilePath);
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
