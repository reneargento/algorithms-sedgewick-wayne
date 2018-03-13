package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.Map;

/**
 * Created by Rene Argento on 28/03/17.
 */
@SuppressWarnings("unchecked")
public class Exercise37_PerformanceDriverII {

    // Parameters example: 7 10
    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);
        int initialArraySize = Integer.parseInt(args[1]);

        Map<Integer, Comparable[]> allInputArrays = ArrayGenerator.generateAllArrays(numberOfExperiments, initialArraySize, 10);
        doExperiment(numberOfExperiments, allInputArrays);
    }

    private static void doExperiment(int numberOfExperiments, Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %12s\n", "Array Size | ", "Average Number of Remove Max");

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] array = allInputArrays.get(i);
            PriorityQueue<Double> priorityQueue = new PriorityQueue<>(array.length, PriorityQueue.Orientation.MAX);

            int totalNumberOfRemoveMax = 0;

            for(int trial = 0; trial < 5; trial++) {

                int numberOfRemoveMaxIn1Second = insertsAndRemovesIn1Second(priorityQueue, array);
                totalNumberOfRemoveMax += numberOfRemoveMaxIn1Second;
            }

            int averageNumberOfRemoveMax = totalNumberOfRemoveMax / 4;
            printResults(array.length, averageNumberOfRemoveMax);
        }
    }

    private static int insertsAndRemovesIn1Second(PriorityQueue<Double> priorityQueue, Comparable[] keys) {
        //Fill the priority queue
        for(int i = 0; i < keys.length; i++) {
            priorityQueue.insert((double) keys[i]);
        }

        int numberOfRemoveMaxIn1Second = 0;
        int randomIndex = StdRandom.uniform(keys.length);

        Stopwatch timer = new Stopwatch();
        while (timer.elapsedTime() <= 1) {
            priorityQueue.deleteTop();
            numberOfRemoveMaxIn1Second++;
            priorityQueue.insert((double) keys[randomIndex]);
        }

        return numberOfRemoveMaxIn1Second;
    }

    private static void printResults(int arraySize, int averageNumberOfRemoveMax) {
        StdOut.printf("%10d %31d\n", arraySize, averageNumberOfRemoveMax);
    }
}
