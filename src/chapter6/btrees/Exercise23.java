package chapter6.btrees;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

/**
 * Created by Rene Argento on 18/08/18.
 */
public class Exercise23 {

    private static final int HUGE_SYMBOL_TABLE_SIZE = 10000000;

    private void doExperiment() {
        int[] keys = ArrayGenerator.generateRandomIntegerArray(HUGE_SYMBOL_TABLE_SIZE, Integer.MAX_VALUE);
        int[] orderValues = {100, 200, 400, 1000, 1500, 2000};
        int[] numberOfSearchValues = {1000, 100000, 10000000};

        StdOut.printf("%11s %17s %10s\n", "Order M | ", "Number of searches | ", "Total time");

        for (int order : orderValues) {
            Exercise15.BTreeSETWithBinarySearchSTPage<Integer> bTreeSET = createBTree(keys, order);

            for(int numberOfSearches : numberOfSearchValues) {
                double totalTimeSpent = 0;

                for (int i = 0; i < numberOfSearches; i++) {
                    int keyToSearch = StdRandom.uniform(Integer.MAX_VALUE);

                    Stopwatch stopwatch = new Stopwatch();
                    bTreeSET.contains(keyToSearch);
                    totalTimeSpent += stopwatch.elapsedTime();
                }

                printResults(order, numberOfSearches, totalTimeSpent);
            }
        }
    }

    private Exercise15.BTreeSETWithBinarySearchSTPage<Integer> createBTree(int[] keys, int order) {
        Exercise15.BTreeSETWithBinarySearchSTPage<Integer> bTreeSET =
                new Exercise15().new BTreeSETWithBinarySearchSTPage<>(0, order, false);

        for (int key : keys) {
            bTreeSET.add(key);
        }

        return bTreeSET;
    }

    private void printResults(int order, int numberOfSearches, double totalTimeSpent) {
        StdOut.printf("%8d %21d %13.3f\n", order, numberOfSearches, totalTimeSpent);
    }

    public static void main(String[] args) {
        new Exercise23().doExperiment();
    }

}
