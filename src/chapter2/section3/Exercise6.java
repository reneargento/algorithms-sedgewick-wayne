package chapter2.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.ArraySortUtil;
import util.RandomArrayGenerator;

/**
 * Created by rene on 04/03/17.
 */
public class Exercise6 {

    private static int numberOfCompares;

    public static void main(String[] args) {

        int[] arraySizes = {100, 1000, 10000};

        StdOut.printf("%13s %12s %17s\n", "Array Size | ", "Compares | ", "Expected Compares");

        for(int i=0; i < arraySizes.length; i++) {

            int arraySize = arraySizes[i];

            Comparable array[] = RandomArrayGenerator.generateArray(arraySize);
            quickSort(array);

            double ln = Math.log(arraySize) / Math.log(10);
            //2N ln N
            double numberOfExpectedCompares = 2 * arraySize * ln;

            printResults(arraySize, numberOfCompares, numberOfExpectedCompares);

            numberOfCompares = 0;
        }
    }

    private static void quickSort(Comparable[] array) {
        StdRandom.shuffle(array);
        quickSort(array, 0, array.length - 1);
    }

    private static void quickSort(Comparable[] array, int low, int high) {

        if(low >= high) {
            return;
        }

        int partition = partition(array, low, high);
        quickSort(array, low, partition - 1);
        quickSort(array, partition + 1, high);
    }

    private static int partition(Comparable[] array, int low, int high) {
        Comparable pivot = array[low];

        int i = low;
        int j = high + 1;

        while(true) {
            numberOfCompares++;

            while (ArraySortUtil.less(array[++i], pivot)) {
                if(i == high) {
                    break;
                }
                numberOfCompares++;
            }

            numberOfCompares++;
            while(ArraySortUtil.less(pivot, array[--j])) {
                if(j == low) {
                    break;
                }
                numberOfCompares++;
            }

            if(i >= j) {
                break;
            }

            ArraySortUtil.exchange(array, i, j);
        }

        //Place pivot in the right place
        ArraySortUtil.exchange(array, low, j);
        return j;
    }

    private static void printResults(int arraySize, int numberOfCompares, double numberOfExpectedCompares) {
        StdOut.printf("%10d %12d %20.0f\n", arraySize, numberOfCompares, numberOfExpectedCompares);
    }

}
