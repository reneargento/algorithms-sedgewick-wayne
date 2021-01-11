package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 31/01/17.
 */
@SuppressWarnings("unchecked")
public class Exercise24_InsertionSortWithSentinel {

    private enum InsertionSortType {
        DEFAULT, SENTINEL;
    }

    public static void main(String[] args) {
        sortCompare();
    }

    private static void insertionSort(Comparable[] array) {

        for (int i = 1; i < array.length; i++) {
            for (int j = i; j > 0 && array[j].compareTo(array[j - 1]) < 0; j--) {
                Comparable temp = array[j];
                array[j] = array[j - 1];
                array[j - 1] = temp;
            }
        }
    }

    private static void insertionSortSentinel(Comparable[] array) {

        Comparable minimumElement = Double.MAX_VALUE;
        int minimumIndex = -1;

        for(int i = 0; i < array.length; i++) {
            if (array[i].compareTo(minimumElement) < 0) {
                minimumElement = array[i];
                minimumIndex = i;
            }
        }

        //Move smallest element to the first position
        Comparable temp = array[0];
        array[0] = array[minimumIndex];
        array[minimumIndex] = temp;

        for(int i = 1; i < array.length; i++) {
            for(int j = i; array[j].compareTo(array[j - 1]) < 0; j--) {
                Comparable temp2 = array[j];
                array[j] = array[j - 1];
                array[j - 1] = temp2;
            }
        }
    }

    private static void sortCompare() {
        int arrayLength = 100000;
        int numberOfExperiments = 10;

        double timeInsertionSortDefault = timeRandomInput(InsertionSortType.DEFAULT, arrayLength, numberOfExperiments);
        double timeInsertionSortSentinel = timeRandomInput(InsertionSortType.SENTINEL, arrayLength, numberOfExperiments);

        StdOut.printf("For %d random doubles\n Insertion Sort default is", arrayLength);
        StdOut.printf(" %.1f times faster than Insertion Sort with a sentinel", timeInsertionSortSentinel / timeInsertionSortDefault);
    }

    private static double timeRandomInput(InsertionSortType insertionSortType, int length, int numberOfExperiments) {
        double total = 0;
        Comparable[] array = new Comparable[length];

        for(int experiment = 0; experiment < numberOfExperiments; experiment++) {
            for(int i = 0; i < length; i++) {
                array[i] = StdRandom.uniform();
            }

            total += time(insertionSortType, array);
        }

        return total;
    }

    public static double time(InsertionSortType insertionSortType, Comparable[] array) {
        Stopwatch timer = new Stopwatch();

        if (insertionSortType == InsertionSortType.DEFAULT) {
            insertionSort(array);
        } else if (insertionSortType == InsertionSortType.SENTINEL) {
            insertionSortSentinel(array);
        }

        return timer.elapsedTime();
    }
}
