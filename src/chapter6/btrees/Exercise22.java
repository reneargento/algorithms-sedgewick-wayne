package chapter6.btrees;

import chapter1.section4.BinarySearch;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

/**
 * Created by Rene Argento on 16/08/18.
 */
public class Exercise22 {

    private static final int HUGE_SYMBOL_TABLE_SIZE = 10000000;

    private static final int SEARCH_TYPE_B_TREE = 0;
    private static final int SEARCH_TYPE_BINARY_SEARCH = 1;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void doExperiment() {
        int[] keys = ArrayGenerator.generateIntOrderedArray(HUGE_SYMBOL_TABLE_SIZE);
        int[] numberOfSearchValues = {1000, 100000, 10000000};
        int[] searchTypes = {SEARCH_TYPE_B_TREE, SEARCH_TYPE_BINARY_SEARCH};

        BTreeSET<Integer> bTreeSET = createBTree(keys);

        StdOut.printf("%20s %14s %16s\n", "Number of searches | ", "B-tree time | ", "Binary search time");

        for(int numberOfSearches : numberOfSearchValues) {
            double[] results = new double[2];

            int[] randomKeysToSearch =
                    ArrayGenerator.generateRandomIntegerArray(numberOfSearches, Integer.MAX_VALUE);

            for (int searchType : searchTypes) {
                double totalTimeSpent = 0;

                for (int i = 0; i < numberOfSearches; i++) {
                    int keyToSearch = randomKeysToSearch[i];

                    if (searchType == SEARCH_TYPE_B_TREE) {
                        Stopwatch stopwatch = new Stopwatch();
                        bTreeSET.contains(keyToSearch);
                        totalTimeSpent += stopwatch.elapsedTime();
                    } else {
                        Stopwatch stopwatch = new Stopwatch();
                        BinarySearch.binarySearch(keys, keyToSearch);
                        totalTimeSpent += stopwatch.elapsedTime();
                    }
                }

                if (searchType == SEARCH_TYPE_B_TREE) {
                    results[SEARCH_TYPE_B_TREE] = totalTimeSpent;
                } else {
                    results[SEARCH_TYPE_BINARY_SEARCH] = totalTimeSpent;
                }
            }

            printResults(numberOfSearches, results[SEARCH_TYPE_B_TREE], results[SEARCH_TYPE_BINARY_SEARCH]);
        }
    }

    private BTreeSET<Integer> createBTree(int[] keys) {
        BTreeSET<Integer> bTreeSET = new BTreeSET<>(0);

        for (int key : keys) {
            bTreeSET.add(key);
        }

        return bTreeSET;
    }

    private void printResults(int numberOfSearches, double bTreeTotalTime, double binarySearchTotalTime) {
        StdOut.printf("%18d %14.3f %21.3f\n", numberOfSearches, bTreeTotalTime, binarySearchTotalTime);
    }

    public static void main(String[] args) {
        new Exercise22().doExperiment();
    }

}
