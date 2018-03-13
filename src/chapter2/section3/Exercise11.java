package chapter2.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayUtil;
import util.ArrayGenerator;

/**
 * Created by Rene Argento on 05/03/17.
 */
public class Exercise11 {

    public static void main(String[] args) {

        StdOut.printf("%13s %12s %6s\n", "Array Size | ", "Running Time | ", "Ratio");

        int arraySize = 8000;

        Comparable[] initialArray = ArrayGenerator.generateRandomArrayWith3Values(arraySize / 2);
        Stopwatch initialTimer = new Stopwatch();

        quickSort(initialArray);

        double previousRunningTime = initialTimer.elapsedTime();

        for(int i = 0; i < 6; i++) {

            Comparable[] array = ArrayGenerator.generateRandomArrayWith3Values(arraySize);

            Stopwatch timer = new Stopwatch();

            quickSort(array);

            double runningTime = timer.elapsedTime();
            double runningTimeRatio = runningTime / previousRunningTime;

            printResults(arraySize, runningTime, runningTimeRatio);

            arraySize *= 2;
            previousRunningTime = runningTime;
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
        quickSort(array, low, partition - 1);
        quickSort(array, partition + 1, high);
    }

    @SuppressWarnings("unchecked")
    private static int partition(Comparable[] array, int low, int high) {
        Comparable pivot = array[low];

        int i = low;
        int j = high + 1;

        while(true) {
            while (array[++i].compareTo(pivot) <= 0) {
                if (i == high) {
                    break;
                }
            }

            while(pivot.compareTo(array[--j]) <= 0) {
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

    private static void printResults(int arraySize, double runningTime, double runningTimeRatio) {
        StdOut.printf("%10d %15.1f %9.1f\n", arraySize, runningTime, runningTimeRatio);
    }

}
