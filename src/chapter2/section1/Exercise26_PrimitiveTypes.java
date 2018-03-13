package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 01/02/17.
 */
public class Exercise26_PrimitiveTypes {

    private enum InsertionSortType {
        PRIMITIVE, NON_PRIMITIVE;
    }

    public static void main(String[] args) {
        sortCompare();
    }

    private static void insertionSortPrimitive(int[] array) {

        for (int i = 0; i < array.length; i++) {
            for (int j = i; j > 0 && array[j] < array[j - 1]; j--) {
                int temp = array[j];
                array[j] = array[j - 1];
                array[j - 1] = temp;
            }
        }
    }

    private static void insertionSortNonPrimitive(Integer[] array) {

        for (int i = 0; i < array.length; i++) {
            for (int j = i; j > 0 && array[j] < array[j - 1]; j--) {
                Integer temp = array[j];
                array[j] = array[j - 1];
                array[j - 1] = temp;
            }
        }

    }

    private static void sortCompare() {
        int arrayLength = 100000;
        int numberOfExperiments = 10;

        double timeInsertionSortWithInts = timeRandomInputInt(InsertionSortType.PRIMITIVE, arrayLength, numberOfExperiments);
        double timeInsertionSortWithIntegers = timeRandomInputInteger(InsertionSortType.NON_PRIMITIVE, arrayLength, numberOfExperiments);

        StdOut.printf("For %d random doubles\n Insertion Sort with primitives is", arrayLength);
        StdOut.printf(" %.1f times faster than Insertion Sort with objects", timeInsertionSortWithIntegers / timeInsertionSortWithInts);
    }

    private static double timeRandomInputInt(InsertionSortType insertionSortType, int length, int numberOfExperiments) {
        double total = 0;
        int[] array = new int[length];

        for(int experiment=0; experiment < numberOfExperiments; experiment++) {
            for(int i = 0; i < length; i++) {
                array[i] = StdRandom.uniform(100);
            }

            total += time(insertionSortType, array, null);
        }

        return total;
    }

    private static double timeRandomInputInteger(InsertionSortType insertionSortType, int length, int numberOfExperiments) {
        double total = 0;
        Integer[] array = new Integer[length];

        for(int experiment=0; experiment < numberOfExperiments; experiment++) {
            for(int i = 0; i < length; i++) {
                array[i] = StdRandom.uniform(100);
            }

            total += time(insertionSortType, null, array);
        }

        return total;
    }

    public static double time(InsertionSortType insertionSortType, int[] arrayInt, Integer[] arrayInteger) {
        Stopwatch timer = new Stopwatch();

        if (insertionSortType == InsertionSortType.PRIMITIVE) {
            insertionSortPrimitive(arrayInt);
        } else if (insertionSortType == InsertionSortType.NON_PRIMITIVE) {
            insertionSortNonPrimitive(arrayInteger);
        }

        return timer.elapsedTime();
    }

}
