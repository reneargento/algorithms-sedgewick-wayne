package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 04/02/17.
 */
public class Exercise31_DoublingTest {

    public static void main(String[] args) {
        int arrayLength = 1000;
        int numberOfExperiments = Integer.parseInt(args[0]);

        StdOut.printf("%6s %15s %7s %5s\n", "N", "Predicted Time", "Time", "Ratio");

        StdOut.println();

        StdOut.println("Selection sort");
        doExperiments(SortTypes.SELECTION, arrayLength, numberOfExperiments);

        StdOut.println();
        StdOut.println("Insertion sort");
        doExperiments(SortTypes.INSERTION, arrayLength, numberOfExperiments);

        StdOut.println();
        StdOut.println("Shell sort");
        doExperiments(SortTypes.SHELL, arrayLength, numberOfExperiments * 2);
    }

    private static void doExperiments(SortTypes sortType, int arrayLength, int numberOfExperiments) {
        //Previous time
        Comparable[] array = generateRandomArray(arrayLength);

        Stopwatch initialTimer = new Stopwatch();

        sortArray(array, sortType);

        double previousRunningTime = initialTimer.elapsedTime();

        for(int i = 0; i < numberOfExperiments; i++) {

            arrayLength *= 2;
            array = generateRandomArray(arrayLength);

            Stopwatch timer = new Stopwatch();
            sortArray(array, sortType);
            double runningTime = timer.elapsedTime();

            /**
             * Predicted seconds
             * Selection and Insertion sort - N ^ 2
             *     Expected doubling ratio = 2 ^ 2 = 4
             *
             * Shell sort - N ^ 3/2
             *     Expected doubling ratio = 2 ^ 3/2 = 2.82
             */

            double doublingRatio = 0;
            if (sortType == SortTypes.SELECTION || sortType == SortTypes.INSERTION) {
                doublingRatio = 4;
            } else if (sortType == SortTypes.SHELL) {
                doublingRatio = 2.82;
            }

            double predictedSeconds = previousRunningTime * doublingRatio;
            double ratio = runningTime / previousRunningTime;

            printExperiment(arrayLength, predictedSeconds, runningTime, ratio);

            previousRunningTime = runningTime;
        }
    }

    private static Comparable[] generateRandomArray(int arrayLength) {
        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = StdRandom.uniform();
        }

        return array;
    }

    private static void sortArray(Comparable[] array, SortTypes sortType) {
        switch (sortType) {
            case SELECTION: SelectionSort.selectionSort(array);
                break;
            case INSERTION: InsertionSort.insertionSort(array);
                break;
            case SHELL: ShellSort.shellSort(array);
                break;
        }
    }

    private static void printExperiment(int arrayLength, double predictedSeconds, double seconds, double ratio) {
        StdOut.printf("%6d %15.1f %7.1f %5.1f\n", arrayLength, predictedSeconds, seconds, ratio);
    }
}
