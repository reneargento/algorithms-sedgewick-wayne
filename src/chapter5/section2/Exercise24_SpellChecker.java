package chapter5.section2;

import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.Constants;
import util.FileUtil;

/**
 * Created by Rene Argento on 18/02/18.
 */
public class Exercise24_SpellChecker {

    private class BlackFilter {

        public HashSet<String> filterUsingTrie(String dictionaryFilePath, String warAndPeaceFilePath) {
            HashSet<String> filteredWords = new HashSet<>();

            // The value field is not used, but it is required by the trie
            Trie<Boolean> trie = new Trie<>();

            In in = new In(dictionaryFilePath);
            while (!in.isEmpty()) {
                trie.put(in.readString(), true);
            }

            String[] allWords = FileUtil.getAllStringsFromFile(warAndPeaceFilePath);

            if (allWords == null) {
                return filteredWords;
            }

            for(String word : allWords) {
                if (!trie.contains(word)) {
                    filteredWords.add(word);
                }
            }

            return filteredWords;
        }

        public HashSet<String> filterUsingTST(String dictionaryFilePath, String warAndPeaceFilePath) {
            HashSet<String> filteredWords = new HashSet<>();

            // The value field is not used, but it is required by the ternary search trie
            TernarySearchTrie<Boolean> ternarySearchTrie = new TernarySearchTrie<>();

            In in = new In(dictionaryFilePath);
            while (!in.isEmpty()) {
                ternarySearchTrie.put(in.readString(), true);
            }

            String[] allWords = FileUtil.getAllStringsFromFile(warAndPeaceFilePath);

            if (allWords == null) {
                return filteredWords;
            }

            for(String word : allWords) {
                if (!ternarySearchTrie.contains(word)) {
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
        blackFilter.filterUsingTrie(dictionaryFilePath, warAndPeaceFilePath);
        double timeSpentWithTrie = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        blackFilter.filterUsingTST(dictionaryFilePath, warAndPeaceFilePath);
        double timeSpentWithTST = stopwatch.elapsedTime();

        StdOut.printf("%19s %14s\n", "Time spent trie | ", "Time spent TST");
        printResults(timeSpentWithTrie, timeSpentWithTST);
    }

    private void printResults(double trieTime, double ternarySearchTrieTime) {
        StdOut.printf("%16.2f %17.2f\n", trieTime, ternarySearchTrieTime);
    }

    public static void main(String[] args) {
        new Exercise24_SpellChecker().doExperiment(args);
    }

}
