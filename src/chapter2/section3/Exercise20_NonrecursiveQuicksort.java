package chapter2.section3;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayUtil;
import util.ArrayGenerator;

import java.util.Map;

/**
 * Created by Rene Argento on 07/03/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for noticing that some redundant checks could be removed.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/110
public class Exercise20_NonrecursiveQuicksort {

    private static class QuickSortRange {
        int low;
        int high;

        QuickSortRange(int low, int high) {
            this.low = low;
            this.high = high;
        }
    }

    // Parameters example: 8 131072
    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);
        int initialArraySize = Integer.parseInt(args[1]);

        Map<Integer, Comparable[]> allInputArrays = ArrayGenerator.generateAllArrays(numberOfExperiments, initialArraySize, 2);

        doExperiment(numberOfExperiments, initialArraySize, allInputArrays);
    }

    private static void doExperiment(int numberOfExperiments, int initialArraySize, Map<Integer, Comparable[]> allInputArrays) {
        StdOut.printf("%13s %23s %22s\n", "Array Size | ", "QuickSort Running Time |", "Nonrecursive QuickSort");

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {

            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] arrayCopy1 = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, arrayCopy1, 0, originalArray.length);

            // Default QuickSort
            Stopwatch defaultQuickSortTimer = new Stopwatch();

            QuickSort.quickSort(originalArray);

            double defaultQuickSortRunningTime = defaultQuickSortTimer.elapsedTime();

            // Nonrecursive QuickSort
            Stopwatch nonRecursiveQuickSortTimer = new Stopwatch();

            nonRecursiveQuickSort(arrayCopy1);

            double nonRecursiveQuickSortRunningTime = nonRecursiveQuickSortTimer.elapsedTime();

            printResults(arraySize, defaultQuickSortRunningTime, nonRecursiveQuickSortRunningTime);

            arraySize *= 2;
        }
    }

    private static void nonRecursiveQuickSort(Comparable[] array) {
        StdRandom.shuffle(array);
        quickSort(array, 0, array.length - 1);
    }

    private static void quickSort(Comparable[] array, int low, int high) {
        Stack<QuickSortRange> stack = new Stack<>();

        QuickSortRange quickSortRange = new QuickSortRange(low, high);
        stack.push(quickSortRange);

        while (stack.size() > 0) {
            QuickSortRange currentQuickSortRange = stack.pop();

            int partition = partition(array, currentQuickSortRange.low, currentQuickSortRange.high);
            QuickSortRange leftQuickSortRange = new QuickSortRange(currentQuickSortRange.low, partition - 1);
            QuickSortRange rightQuickSortRange = new QuickSortRange(partition + 1, currentQuickSortRange.high);

            // Size = right - left + 1
            int leftSubArraySize = partition - currentQuickSortRange.low;
            int rightSubArraySize = currentQuickSortRange.high - partition;

            // Push the larger sub array first to guarantee that the stack will have at most lg N entries
            if (leftSubArraySize > rightSubArraySize) {
                if (leftSubArraySize > 1) {
                    stack.push(leftQuickSortRange);
                }
                if (rightSubArraySize > 1) {
                    stack.push(rightQuickSortRange);
                }
            } else {
                if (rightSubArraySize > 1) {
                    stack.push(rightQuickSortRange);
                }
                if (leftSubArraySize > 1) {
                    stack.push(leftQuickSortRange);
                }
            }
        }
    }

    private static int partition(Comparable[] array, int low, int high) {
        Comparable pivot = array[low];

        int i = low;
        int j = high + 1;

        while(true) {
            while (ArrayUtil.less(array[++i], pivot)) {
                if (i == high) {
                    break;
                }
            }

            while(ArrayUtil.less(pivot, array[--j])) {
                if (j == low) {
                    break;
                }
            }

            if (i >= j) {
                break;
            }

            ArrayUtil.exchange(array, i, j);
        }

        // Place pivot in the right place
        ArrayUtil.exchange(array, low, j);
        return j;
    }

    private static void printResults(int arraySize, double defaultQuickSortRunningTime, double nonRecursiveQuickSort) {
        StdOut.printf("%10d %25.1f %24.1f\n", arraySize, defaultQuickSortRunningTime, nonRecursiveQuickSort);
    }
}
