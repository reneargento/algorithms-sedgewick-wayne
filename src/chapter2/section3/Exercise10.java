package chapter2.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.ArraySortUtil;
import util.RandomArrayGenerator;

/**
 * Created by rene on 04/03/17.
 */
public class Exercise10 {

    private static int numberOfCompares;
    private static final int ARRAY_SIZE = 1000000;

    public static void main(String[] args) {

        StdOut.printf("%13s %9s\n", "Array Size | ", "Compares");

        for(int i=0; i < 5; i++) {

            Comparable array[] = RandomArrayGenerator.generateArray(ARRAY_SIZE);
            quickSort(array);

            printResults(ARRAY_SIZE, numberOfCompares);

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

    private static void printResults(int arraySize, int numberOfCompares) {
        StdOut.printf("%10d %12d\n", arraySize, numberOfCompares);
    }

}
