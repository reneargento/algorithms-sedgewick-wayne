package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayGenerator;
import util.ArrayUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 30/03/17.
 */
//Use the VM Option of -Xmx20g to create a 10^9 size array
//This implementation uses array sizes of 10^3, 10^6 and 10^8 for the experiments
public class Exercise40_FloydsMethod {

    private static long numberOfCompares;

    public static void main(String[] args) {
        int[] arraySizes = {1000, 1000000, 100000000};

        Map<Integer, Comparable[]> allInputArrays = new HashMap<>();
        for(int i = 0; i < 3; i++) {
            Comparable[] array = ArrayGenerator.generateDistinctValuesShuffledArray(arraySizes[i]);
            array[0] = null; //0 index is not used on heaps
            allInputArrays.put(i, array);
        }

        doExperiment(allInputArrays);
    }

    private static void doExperiment(Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %25s %20s\n", "Array Size | ","Number of Compares Std Impl | ", "Number of Compares Floyd Enhancement");

        for(int i = 0; i < 3; i++) {
            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            numberOfCompares = 0;
            //Standard implementation
            heapSort(originalArray, false);
            long numberOfComparesStandardImpl = numberOfCompares;

            numberOfCompares = 0;
            //Using Floyd's sink-to-the-bottom-and-then-swim idea
            heapSort(array, true);

            printResults(originalArray.length, numberOfComparesStandardImpl, numberOfCompares);
        }
    }

    private static void heapSort(Comparable[] array, boolean useFloydEnhancement) {
        constructHeap(array);
        sortdown(array, useFloydEnhancement);
    }

    private static void constructHeap(Comparable[] array) {
        for(int i = array.length / 2; i >= 1; i--) {
            sink(array, i, array.length - 1);
        }
    }

    private static void sortdown(Comparable[] array, boolean useFloydEnhancement) {
        int endIndex = array.length - 1;

        for(int i = 1; i < array.length; i++) {
            ArrayUtil.exchange(array, 1, endIndex);
            endIndex--;

            if (!useFloydEnhancement) {
                sink(array, 1, endIndex);
            } else {
                sinkToTheBottomAndSwim(array, 1, endIndex);
            }
        }
    }

    private static void sink(Comparable[] array, int index, int endIndex) {
        while (index * 2 <= endIndex) {
            int biggestChildIndex = index * 2;

            if (index * 2 + 1 <= endIndex) {
                numberOfCompares++;
                if (ArrayUtil.more(array[index * 2 + 1], array[index * 2])) {
                    biggestChildIndex = index * 2 + 1;
                }
            }

            numberOfCompares++;
            if (ArrayUtil.less(array[index], array[biggestChildIndex])) {
                ArrayUtil.exchange(array, index, biggestChildIndex);
            } else {
                break;
            }

            index = biggestChildIndex;
        }
    }

    //Floyd's enhancement
    // Decreases the number of compares by a factor of 2 asymptotically.
    // Used when the cost of comparisons is high.
    private static void sinkToTheBottomAndSwim(Comparable[] array, int index, int endIndex) {
        //Sink to the bottom
        while (index * 2 <= endIndex) {
            int biggestChildIndex = index * 2;

            if (index * 2 + 1 <= endIndex) {
                numberOfCompares++;
                if (ArrayUtil.more(array[index * 2 + 1], array[index * 2])) {
                    biggestChildIndex = index * 2 + 1;
                }
            }

            //Promote the larger of the two children
            ArrayUtil.exchange(array, index, biggestChildIndex);

            index = biggestChildIndex;
        }

        //Bottom of the heap was reached
        //Move back up the heap to the proper positions
        swim(array, index);
    }

    private static void swim(Comparable[] array, int index) {
        while(index / 2 >= 1) {
            numberOfCompares++;
            if (ArrayUtil.less(array[index / 2], array[index])) {
                ArrayUtil.exchange(array, index / 2, index);
            } else {
                break;
            }

            index = index / 2;
        }
    }

    private static void printResults(int arraySize, long numberOfComparesStandardImpl, long numberOfComparesFloydEnhancement) {
        StdOut.printf("%10d %30d %39d\n", arraySize, numberOfComparesStandardImpl, numberOfComparesFloydEnhancement);
    }

}
