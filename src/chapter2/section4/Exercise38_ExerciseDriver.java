package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 28/03/17.
 */
@SuppressWarnings("unchecked")
public class Exercise38_ExerciseDriver {

    private static final String ORDERED_INPUT = "Ordered";
    private static final String REVERSE_ORDERED_INPUT = "Reverse Ordered";
    private static final String SAME_KEYS_INPUT = "Same Keys";
    private static final String KEYS_WITH_ONLY_2_VALUES_INPUT = "Keys with only 2 Values";

    // Parameters example: 6 1000000
    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);
        int initialArraySize = Integer.parseInt(args[1]);

        doExperiment(numberOfExperiments, initialArraySize);
    }

    private static void doExperiment(int numberOfExperiments, int initialArraySize) {

        StdOut.printf("%28s %13s %12s\n", "Input Type | ", "Array Size | ","Running Time");
        String[] inputType = {ORDERED_INPUT, REVERSE_ORDERED_INPUT, SAME_KEYS_INPUT, KEYS_WITH_ONLY_2_VALUES_INPUT};

        for(int input = 0; input < 4; input++) {
            Map<Integer, Comparable[]> allInputArrays = generateAllArrays(inputType[input], numberOfExperiments, initialArraySize);

            for(int i = 0; i < numberOfExperiments; i++) {
                Comparable[] originalArray = allInputArrays.get(i);
                Comparable[] array = new Comparable[originalArray.length];
                System.arraycopy(originalArray, 0, array, 0, originalArray.length);

                PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(array.length, PriorityQueue.Orientation.MAX);

                Stopwatch timer = new Stopwatch();
                testInput(priorityQueue, array);
                double runningTime = timer.elapsedTime();

                printResults(inputType[input], array.length, runningTime);
            }
        }
    }

    private static Map<Integer, Comparable[]> generateAllArrays(String inputType, int numberOfExperiments, int initialArraySize) {
        Map<Integer, Comparable[]> allArrays = new HashMap<>();

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] array;

            switch (inputType) {
                case ORDERED_INPUT:
                    array = ArrayGenerator.generateOrderedArray(arraySize);
                    break;
                case REVERSE_ORDERED_INPUT:
                    array = ArrayGenerator.generateReverseOrderedArray(arraySize);
                    break;
                case SAME_KEYS_INPUT:
                    array = ArrayGenerator.generateArrayWithAllKeysEqual(arraySize);
                    break;
                default:
                    array = ArrayGenerator.generateRandomArrayWith2Values(arraySize);
                    break;
            }

            allArrays.put(i, array);
            arraySize *= 2;
        }

        return allArrays;
    }

    private static void testInput(PriorityQueue<Integer> priorityQueue, Comparable[] keys) {
        //Fill the priority queue
        for(int i = 0; i < keys.length; i++) {
            priorityQueue.insert((int) keys[i]);
        }

        priorityQueue.isEmpty();
        priorityQueue.size();

        //Remove all keys
        while (priorityQueue.size() > 0) {
            priorityQueue.deleteTop();
        }
    }

    private static void printResults(String inputType, int arraySize, double runningTime) {
        StdOut.printf("%25s %13d %15.1f\n", inputType, arraySize, runningTime);
    }
}
