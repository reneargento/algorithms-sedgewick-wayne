package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 02/02/17.
 */
@SuppressWarnings("unchecked")
public class Exercise27_ShellsortSubquadratic {

    public static void main(String[] args) {
        sortCompare();
    }

    private static void sortCompare() {
        int arrayLength = 128;
        int numberOfExperiments = 11;

        double timeSelectionSort = timeRandomInput(SortTypes.SELECTION, arrayLength, numberOfExperiments);
        double timeInsertionSort = timeRandomInput(SortTypes.INSERTION, arrayLength, numberOfExperiments);
        double timeShellSort = timeRandomInput(SortTypes.SHELL, arrayLength, numberOfExperiments);

        StdOut.printf("Shell Sort is %.1f times faster than Selection Sort", timeSelectionSort / timeShellSort);
        StdOut.println();
        StdOut.printf("Shell Sort is %.1f times faster than Insertion Sort", timeInsertionSort / timeShellSort);
    }

    private static double timeRandomInput(SortTypes sortType, int initialLength, int numberOfExperiments) {
        double total = 0;
        int length = initialLength;

        for(int experiment = 0; experiment < numberOfExperiments; experiment++) {
            Comparable[] array = new Comparable[length];

            for(int i = 0; i < length; i++) {
                array[i] = StdRandom.uniform();
            }

            total += time(sortType, array);

            length *= 2;
        }

        return total;
    }

    public static double time(SortTypes sortType, Comparable[] array) {
        Stopwatch timer = new Stopwatch();

        if (sortType == SortTypes.SELECTION) {
            SelectionSort.selectionSort(array);
        } else if (sortType == SortTypes.INSERTION) {
            InsertionSort.insertionSort(array);
        } else if (sortType == SortTypes.SHELL) {
            ShellSort.shellSort(array);
        }

        return timer.elapsedTime();
    }

}
