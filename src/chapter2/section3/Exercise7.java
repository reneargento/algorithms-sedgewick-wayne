package chapter2.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.ArrayUtil;
import util.ArrayGenerator;

import java.util.Map;

/**
 * Created by Rene Argento on 04/03/17.
 */
// Thanks to MrScislowski (https://github.com/MrScislowski) for fixing the rightSubArraySize computation.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/161
public class Exercise7 {

    private static int subArraysOfSize0;
    private static int subArraysOfSize1;
    private static int subArraysOfSize2;

    public static void main(String[] args) {
        Map<Integer, Comparable[]> allInputArrays = ArrayGenerator.generateAllArrays(5, 1000, 2);

        StdOut.printf("%13s %17s %17s %17s\n", "Array Size | ", "SubArrays Size 0 | ", "SubArrays Size 1 | ", "SubArrays Size 2");

        for(int i = 0; i < allInputArrays.size(); i++) {
            Comparable[] array = allInputArrays.get(i);

            quickSort(array);

            printResults(array.length);

            subArraysOfSize0 = 0;
            subArraysOfSize1 = 0;
            subArraysOfSize2 = 0;
        }
    }

    private static void quickSort(Comparable[] array) {
        StdRandom.shuffle(array);
        quickSort(array, 0, array.length - 1);
    }

    private static void quickSort(Comparable[] array, int low, int high) {
        if (low >= high) {
            return;
        }

        int partition = partition(array, low, high);

        // size = high - low + 1
        int leftSubArraySize = partition - low;
        int rightSubArraySize = high - partition;

        checkSubArraySize(leftSubArraySize);
        checkSubArraySize(rightSubArraySize);

        quickSort(array, low, partition - 1);
        quickSort(array, partition + 1, high);
    }

    private static void checkSubArraySize(int subArraySize) {
        if (subArraySize == 0) {
            subArraysOfSize0++;
        } else if (subArraySize == 1) {
            subArraysOfSize1++;
        } else if (subArraySize == 2) {
            subArraysOfSize2++;
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

        //Place pivot in the right place
        ArrayUtil.exchange(array, low, j);
        return j;
    }

    private static void printResults(int arraySize) {
        StdOut.printf("%10d %19d %19d %20d\n", arraySize, subArraysOfSize0, subArraysOfSize1, subArraysOfSize2);
    }

}
