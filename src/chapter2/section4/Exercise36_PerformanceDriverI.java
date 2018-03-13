package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.Map;

/**
 * Created by Rene Argento on 28/03/17.
 */
@SuppressWarnings("unchecked")
public class Exercise36_PerformanceDriverI {

    // Parameters example: 7 10
    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);
        int initialArraySize = Integer.parseInt(args[1]);

        Map<Integer, Comparable[]> allInputArrays = ArrayGenerator.generateAllArrays(numberOfExperiments, initialArraySize, 10);
        doExperiment(numberOfExperiments, allInputArrays);
    }

    private static void doExperiment(int numberOfExperiments, Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %12s\n", "Array Size | ", "Average Running Time");

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] array = allInputArrays.get(i);
            PriorityQueue<Double> priorityQueue = new PriorityQueue<>(array.length, PriorityQueue.Orientation.MAX);

            double totalTime = 0;

            for(int trial = 0; trial < 5; trial++) {
                Stopwatch timer = new Stopwatch();
                performanceTest(priorityQueue, array);
                double runningTime = timer.elapsedTime();

                totalTime += runningTime;
            }

            double averageRunningTime = totalTime / 4;
            printResults(array.length, averageRunningTime);
        }
    }

    private static void performanceTest(PriorityQueue<Double> priorityQueue, Comparable[] keys) {
        //Fill the priority queue
        for(int i = 0; i < keys.length; i++) {
            priorityQueue.insert((double) keys[i]);
        }

        //Remove half the keys
        for(int i = 0; i < keys.length / 2; i++) {
            priorityQueue.deleteTop();
        }

        //Fill the priority queue again
        for(int i = keys.length / 2; i < keys.length; i++) {
            priorityQueue.insert((double) keys[i]);
        }

        //Remove all the keys
        while (priorityQueue.size() > 0) {
            priorityQueue.deleteTop();
        }
    }

    private static void printResults(int arraySize, double runningTime) {
        StdOut.printf("%10d %23.1f\n", arraySize, runningTime);
    }

}
