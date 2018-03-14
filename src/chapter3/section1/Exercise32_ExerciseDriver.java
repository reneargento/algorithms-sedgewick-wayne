package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 28/04/17.
 */
public class Exercise32_ExerciseDriver {

    private static final String ORDERED_INPUT = "Ordered";
    private static final String REVERSE_ORDERED_INPUT = "Reverse Ordered";
    private static final String SAME_KEYS_INPUT = "Same Keys";
    private static final String KEYS_WITH_ONLY_2_VALUES_INPUT = "Keys with only 2 Values";

    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]); // 3
        int initialArraySize = Integer.parseInt(args[1]); // 100000

        doExperiment(numberOfExperiments, initialArraySize);
    }

    private static void doExperiment(int numberOfExperiments, int initialArraySize) {

        StdOut.printf("%28s %13s %12s\n", "Input Type | ", "Array Size | ","Running Time");
        String[] inputType = {ORDERED_INPUT, REVERSE_ORDERED_INPUT, SAME_KEYS_INPUT, KEYS_WITH_ONLY_2_VALUES_INPUT};

        for(int input = 0; input < 4; input++) {
            Map<Integer, Comparable[]> allInputArrays = generateAllArrays(inputType[input], numberOfExperiments, initialArraySize);

            for(int i = 0; i < numberOfExperiments; i++) {
                Comparable[] array = allInputArrays.get(i);

                Stopwatch timer = new Stopwatch();
                testInput(array, false);
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

    private static void testInput(Comparable[] keys, boolean verbose) {
        BinarySearchSymbolTable<Integer, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        for(int i = 0; i < keys.length; i++) {
            binarySearchSymbolTable.put((Integer) keys[i], i);
        }

        int size = binarySearchSymbolTable.size();
        int min = binarySearchSymbolTable.min();
        int max = binarySearchSymbolTable.max();

        if (verbose) {
            StdOut.println("size = " + size);
            StdOut.println("min  = " + min);
            StdOut.println("max  = " + max);
            StdOut.println();
        }

        // Print keys in order using keys()
        if (verbose) {
            StdOut.println("Testing keys()");
            StdOut.println("--------------------------------");
        }
        for (int key : binarySearchSymbolTable.keys()) {
            Integer value = binarySearchSymbolTable.get(key);

            if (verbose) {
                StdOut.println(key + " " + value);
            }
        }
        if (verbose) {
            StdOut.println();
        }

        // Print keys in order using select
        if (verbose) {
            StdOut.println("Testing select");
            StdOut.println("--------------------------------");
        }
        for (int i = 0; i < binarySearchSymbolTable.size(); i++) {
            int key = binarySearchSymbolTable.select(i);
            if (verbose) {
                StdOut.println(i + " " + key);
            }
        }
        if (verbose) {
            StdOut.println();
        }

        // Test rank, floor, ceiling
        if (verbose) {
            StdOut.println("key rank floor ceil");
            StdOut.println("-------------------");
        }
        for (int key = binarySearchSymbolTable.get(0); key < binarySearchSymbolTable.size(); key *= binarySearchSymbolTable.size() / 5) {
            if (key == 0) {
                key = 1;
            }

            Integer rank = binarySearchSymbolTable.rank(key);
            Integer floor = binarySearchSymbolTable.floor(key);
            Integer ceiling = binarySearchSymbolTable.ceiling(key);

            if (verbose) {
                StdOut.printf("%2s %4d %4s %4s\n", key, rank, floor, ceiling);
            }
        }
        if (verbose) {
            StdOut.println();
        }

        // Test range search and range count
        int[] from = {0, keys.length / 2};
        int[] to   = {keys.length, keys.length};
        if (verbose) {
            StdOut.println("range search");
            StdOut.println("-------------------");
        }
        for (int i = 0; i < from.length; i++) {
            int rangeSize = binarySearchSymbolTable.size(from[i], to[i]);

            if (verbose) {
                StdOut.printf("%s-%s (%2d) : ", from[i], to[i], rangeSize);
            }

            for (int key : binarySearchSymbolTable.keys(from[i], to[i])) {
                if (verbose) {
                    StdOut.print(key + " ");
                }
            }
            if (verbose) {
                StdOut.println();
            }
        }
        if (verbose) {
            StdOut.println();
        }

        // Delete the smallest keys
        for (int i = 0; i < binarySearchSymbolTable.size() / 2; i++) {
            binarySearchSymbolTable.deleteMin();
        }
        if (verbose) {
            StdOut.println("After deleting the smallest " + binarySearchSymbolTable.size() / 2 + " keys");
            StdOut.println("--------------------------------");
            for (int key : binarySearchSymbolTable.keys()) {
                StdOut.println(key + " " + binarySearchSymbolTable.get(key));
            }
            StdOut.println();
        }

        // Delete the max key
        if (!binarySearchSymbolTable.isEmpty()) {
            binarySearchSymbolTable.deleteMax();

            if (verbose && !binarySearchSymbolTable.isEmpty()) {
                StdOut.println("After deleting the max key");
                StdOut.println("--------------------------------");
                for (int key : binarySearchSymbolTable.keys()) {
                    StdOut.println(key + " " + binarySearchSymbolTable.get(key));
                }
                StdOut.println();
            }
        }

        // Delete all the remaining keys
        while (!binarySearchSymbolTable.isEmpty()) {
            binarySearchSymbolTable.delete(binarySearchSymbolTable.select(binarySearchSymbolTable.size() / 2));
        }
        if (verbose) {
            StdOut.println("After deleting the remaining keys");
            StdOut.println("Size: " + binarySearchSymbolTable.size());
            StdOut.println();

            StdOut.println("After adding back N keys");
            StdOut.println("--------------------------------");
        }

        for (int i = 0; i < keys.length; i++) {
            binarySearchSymbolTable.put((Integer) keys[i], i);
        }

        if (verbose) {
            for (int key : binarySearchSymbolTable.keys()) {
                StdOut.println(key + " " + binarySearchSymbolTable.get(key));
            }
            StdOut.println();
        }
    }

    private static void printResults(String inputType, int arraySize, double runningTime) {
        StdOut.printf("%25s %13d %15.1f\n", inputType, arraySize, runningTime);
    }

}
