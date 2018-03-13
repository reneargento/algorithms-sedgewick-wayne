package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Rene Argento on 19/02/17.
 */
public class Exercise25_MultiwayMergesort {

    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]); // 8
        int initialArraySize = Integer.parseInt(args[1]); // 131072

        Exercise25_MultiwayMergesort multiwayMergesort = new Exercise25_MultiwayMergesort();

        Map<Integer, Comparable[]> allInputArrays = ArrayGenerator.generateAllArrays(numberOfExperiments, initialArraySize, 2);

        for(int k = 2; k <= 10; k++) {
            String text = k + "-WAY MERGESORT";
            StdOut.println(text);

            multiwayMergesort.doExperiment(numberOfExperiments, initialArraySize, allInputArrays, k);

            StdOut.println();
        }
    }

    private void doExperiment(int numberOfExperiments, int initialArraySize,
                              Map<Integer, Comparable[]> allInputArrays, int subArraysToMerge) {

        StdOut.printf("%13s %12s\n", "Array Size | ", "Running Time");

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            Stopwatch timer = new Stopwatch();

            kWayMergeSort(array, subArraysToMerge);

            double runningTime = timer.elapsedTime();

            printResults(arraySize, runningTime);

            arraySize *= 2;
        }
    }

    private void kWayMergeSort(Comparable[] array, int k) {
        Comparable[] aux = new Comparable[array.length];

        kWayMergeSort(array, aux, k, 0, array.length - 1);
    }

    private void kWayMergeSort(Comparable[] array, Comparable[] aux, int k, int low, int high) {
        if (low >= high) {
            return;
        }

        List<int[]> indexes = generateIndexes(k, low, high);

        int[] startIndexes = indexes.get(0);
        int[] endIndexes = indexes.get(1);

        for(int i = 0; i < k; i++) {
            kWayMergeSort(array, aux, k, startIndexes[i], endIndexes[i]);
        }

        merge(array, aux, startIndexes, endIndexes);
    }

    private List<int[]> generateIndexes(int k, int low, int high) {
        int length = high - low + 1;

        int subArraySizes = length / k;
        int remainingValues = length % k;

        List<int[]> indexes = new ArrayList<>();
        int[] startIndexes = new int[k];
        int[] endIndexes = new int[k];

        for(int i = 0; i < startIndexes.length; i++) {
            if (i == 0) {
                startIndexes[i] = low;
            } else {
                startIndexes[i] = Math.min(endIndexes[i - 1] + 1, high);
            }

            int extraValue = 0;

            if (remainingValues > 0) {
                extraValue = 1;
                remainingValues--;
            }

            endIndexes[i] = Math.min(startIndexes[i] + subArraySizes - 1 + extraValue, high);
        }

        indexes.add(startIndexes);
        indexes.add(endIndexes);

        return indexes;
    }

    @SuppressWarnings("unchecked")
    private void merge(Comparable[] array, Comparable[] aux, int[] startIndexes, int[] endIndexes) {

        int low = startIndexes[0];
        int high = -1;

        //We may not have all arrays filled
        for(int i = 0; i < endIndexes.length; i++) {
            if (endIndexes[i] > high) {
                high = endIndexes[i];
            }
        }

        for(int i = low; i <= high; i++) {
            aux[i] = array[i];
        }

        for(int i = low; i <= high; i++) {
            Comparable bestValue = -1;
            int bestIndex = -1;

            for(int j = 0; j < startIndexes.length; j++) {
                int index = startIndexes[j];

                if (index <= endIndexes[j] && (bestIndex == -1 || aux[index].compareTo(bestValue) < 0)) {
                    bestValue = aux[index];
                    bestIndex = j;
                }
            }

            if (bestIndex != -1) {
                array[i] = bestValue;
                startIndexes[bestIndex]++;
            }
        }
    }

    private void printResults(int arraySize, double runningTime) {
        StdOut.printf("%10d %15.1f\n", arraySize, runningTime);
    }

}
