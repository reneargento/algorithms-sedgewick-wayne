package chapter6.btrees;

import chapter3.section3.RedBlackBST;
import chapter3.section4.LinearProbingHashTable;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

/**
 * Created by Rene Argento on 18/08/18.
 */
public class Exercise24 {

    private static final int HUGE_SYMBOL_TABLE_SIZE = 10000000;

    private static final int B_TREE = 0;
    private static final int LINEAR_PROBING_HASH_TABLE = 1;
    private static final int RED_BLACK_TREE = 2;

    // M value found on exercise 6.23
    private static final int BEST_B_TREE_ORDER_FOUND = 1500;

    private void doExperiment() {
        int[] keys = ArrayGenerator.generateRandomIntegerArray(HUGE_SYMBOL_TABLE_SIZE, Integer.MAX_VALUE);
        int[] numberOfSearchValues = {1000, 100000, 10000000};
        int[] dataStructures = {B_TREE, LINEAR_PROBING_HASH_TABLE, RED_BLACK_TREE};

        BTreeSET<Integer> bTreeSET = createBTree(keys);
        LinearProbingHashTable<Integer, Integer> linearProbingHashTable = createLinearProbingHashTable(keys);
        RedBlackBST<Integer, Integer> redBlackBST = createRedBlackTree(keys);

        StdOut.printf("%20s %14s %24s %18s\n", "Number of searches | ", "B-tree time | ", "Linear probing hashing time | ",
                "Red-Black tree time");

        for(int numberOfSearches : numberOfSearchValues) {
            double[] results = new double[3];

            int[] randomKeysToSearch =
                    ArrayGenerator.generateRandomIntegerArray(numberOfSearches, Integer.MAX_VALUE);

            for (int dataStructure : dataStructures) {
                double totalTimeSpent = 0;

                for (int i = 0; i < numberOfSearches; i++) {
                    int keyToSearch = randomKeysToSearch[i];

                    if (dataStructure == B_TREE) {
                        Stopwatch stopwatch = new Stopwatch();
                        bTreeSET.contains(keyToSearch);
                        totalTimeSpent += stopwatch.elapsedTime();
                    } else if (dataStructure == LINEAR_PROBING_HASH_TABLE) {
                        Stopwatch stopwatch = new Stopwatch();
                        linearProbingHashTable.contains(keyToSearch);
                        totalTimeSpent += stopwatch.elapsedTime();
                    } else {
                        Stopwatch stopwatch = new Stopwatch();
                        redBlackBST.contains(keyToSearch);
                        totalTimeSpent += stopwatch.elapsedTime();
                    }
                }

                if (dataStructure == B_TREE) {
                    results[B_TREE] = totalTimeSpent;
                } else if (dataStructure == LINEAR_PROBING_HASH_TABLE) {
                    results[LINEAR_PROBING_HASH_TABLE] = totalTimeSpent;
                } else {
                    results[RED_BLACK_TREE] = totalTimeSpent;
                }
            }

            printResults(numberOfSearches, results[B_TREE], results[LINEAR_PROBING_HASH_TABLE], results[RED_BLACK_TREE]);
        }
    }

    private BTreeSET<Integer> createBTree(int[] keys) {
        BTreeSET<Integer> bTreeSET = new BTreeSET<>(0, BEST_B_TREE_ORDER_FOUND, false);

        for (int key : keys) {
            bTreeSET.add(key);
        }

        return bTreeSET;
    }

    private LinearProbingHashTable<Integer, Integer> createLinearProbingHashTable(int[] keys) {
        LinearProbingHashTable<Integer, Integer> linearProbingHashTable = new LinearProbingHashTable<>(100);

        for (int key : keys) {
            linearProbingHashTable.put(key, key);
        }

        return linearProbingHashTable;
    }

    private RedBlackBST<Integer, Integer> createRedBlackTree(int[] keys) {
        RedBlackBST<Integer, Integer> redBlackBST = new RedBlackBST<>();

        for (int key : keys) {
            redBlackBST.put(key, key);
        }

        return redBlackBST;
    }

    private void printResults(int numberOfSearches, double bTreeTotalTime, double linearProbingHashingTotalTime,
                              double redBlackTreeTotalTime) {
        StdOut.printf("%18d %14.3f %30.3f %22.3f\n", numberOfSearches, bTreeTotalTime, linearProbingHashingTotalTime,
                redBlackTreeTotalTime);
    }

    public static void main(String[] args) {
        new Exercise24().doExperiment();
    }

}
