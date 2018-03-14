package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

/**
 * Created by Rene Argento on 12/06/17.
 */
public class Exercise43_PutGetRatio {

    public static void main(String[] args) {
        new Exercise43_PutGetRatio().doExperiments();
    }

    private void doExperiments() {
        int numberOfKeys = 1000000;
        StdOut.printf("%15s %20s %20s %8s\n", "Number of Keys | ","Running Time Put | ", "Running Time Get | ", "Ratio");

        int[] randomValues = ArrayGenerator.generateRandomIntegerArray(numberOfKeys, Integer.MAX_VALUE);
        double[] result = frequencyCounter(randomValues);

        printResults(numberOfKeys, result[0], result[1], result[2]);
    }

    private double[] frequencyCounter(int[] values) {

        double totalTimeSpentInPut = 0;
        double totalTimeSpentInGet = 0;
        Stopwatch timer;

        BinarySearchTree<Integer, Integer> binarySearchTree = new BinarySearchTree<>();

        for(Integer value : values) {

            timer = new Stopwatch();
            boolean containsValue = binarySearchTree.contains(value); //contains() uses get() internally
            totalTimeSpentInGet += timer.elapsedTime();

            if (!containsValue) {
                timer = new Stopwatch();
                binarySearchTree.put(value, 1);
                totalTimeSpentInPut += timer.elapsedTime();
            } else {
                timer = new Stopwatch();
                int currentFrequency = binarySearchTree.get(value);
                totalTimeSpentInGet += timer.elapsedTime();

                timer = new Stopwatch();
                binarySearchTree.put(value, currentFrequency + 1);
                totalTimeSpentInPut += timer.elapsedTime();
            }
        }

        int max = 0;
        timer = new Stopwatch();
        binarySearchTree.put(max, 0);
        totalTimeSpentInPut += timer.elapsedTime();

        for(Integer value : binarySearchTree.keys()) {
            timer = new Stopwatch();
            if (binarySearchTree.get(value) > binarySearchTree.get(max)) {
                totalTimeSpentInGet += timer.elapsedTime();
                max = value;
            }
        }

        timer = new Stopwatch();
        String maxFrequency = max + " " + binarySearchTree.get(max);
        totalTimeSpentInGet += timer.elapsedTime();

        double ratio = totalTimeSpentInPut / totalTimeSpentInGet;

        return new double[]{totalTimeSpentInPut, totalTimeSpentInGet, ratio};
    }

    private void printResults(int numberOfKeys, double totalTimeSpentInPut, double totalTimeSpentInGet, double ratio) {
        StdOut.printf("%14d %20.2f %20.2f %11.2f\n", numberOfKeys, totalTimeSpentInPut, totalTimeSpentInGet, ratio);
    }

}
