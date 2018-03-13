package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;
import util.ArrayUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 29/03/17.
 */
//Use the VM Option of -Xmx20g to create a 10^9 size array
//This implementation uses array sizes of 10^3, 10^6 and 10^8 for the experiments
@SuppressWarnings("unchecked")
public class Exercise39_CostOfConstruction {

    public static void main(String[] args) {
        int[] arraySizes = {1000, 1000000, 100000000};

        Map<Integer, Comparable[]> allInputArrays = new HashMap<>();
        for(int i = 0; i < 3; i++) {
            Comparable[] array = ArrayGenerator.generateRandomArray(arraySizes[i]);
            array[0] = null; //0 index is not used on heaps
            allInputArrays.put(i, array);
        }

        doExperiment(allInputArrays);
    }

    private static void doExperiment(Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %12s\n", "Array Size | ","% of Time Spent on Construction");

        for(int i = 0; i < 3; i++) {
            Comparable[] array = allInputArrays.get(i);

            Stopwatch constructHeapTimer = new Stopwatch();
            constructHeap(array);
            double constructHeapRunningTime = constructHeapTimer.elapsedTime();

            Stopwatch sortdownTimer = new Stopwatch();
            sortdown(array);
            double sortdownRunningTime = sortdownTimer.elapsedTime();

            double totalRunningTime = constructHeapRunningTime + sortdownRunningTime;
            double percentageOfTimeSpentOnConstruction = constructHeapRunningTime / totalRunningTime * 100;

            printResults(array.length, percentageOfTimeSpentOnConstruction);
        }
    }

    private static void constructHeap(Comparable[] array) {
        for(int i = array.length / 2; i >= 1; i--) {
            sink(array, i, array.length - 1);
        }
    }

    private static void sortdown(Comparable[] array) {
        int endIndex = array.length - 1;

        for(int i = 1; i < array.length; i++) {
            ArrayUtil.exchange(array, 1, endIndex);
            endIndex--;
            sink(array, 1, endIndex);
        }
    }

    private static void sink(Comparable[] array, int index, int endIndex) {
        while (index * 2 <= endIndex) {
            int biggestChildIndex = index * 2;

            if (index * 2 + 1 <= endIndex
                    && ArrayUtil.more(array[index * 2 + 1], array[index * 2])) {
                biggestChildIndex = index * 2 + 1;
            }

            if (ArrayUtil.less(array[index], array[biggestChildIndex])) {
                ArrayUtil.exchange(array, index, biggestChildIndex);
            } else {
                break;
            }

            index = biggestChildIndex;
        }
    }

    private static void printResults(int arraySize, double percentageOfTimeSpentOnConstruction) {
        StdOut.printf("%10d %34.1f\n", arraySize, percentageOfTimeSpentOnConstruction);
    }

}
