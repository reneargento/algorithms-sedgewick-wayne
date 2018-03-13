package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;

/**
 * Created by Rene Argento on 05/02/17.
 */
public class Exercise35_NonuniformDistributions {

    public static void main(String[] args) {

        int arrayLength = Integer.parseInt(args[0]);
        int experiments = Integer.parseInt(args[1]);

        runAllExperiments(arrayLength, experiments);
    }

    private static void runAllExperiments(int initialArrayLength, int experiments) {

        int arrayLength = initialArrayLength;

        StdOut.printf("%15s %17s %8s\n", "Sort Type", "Array Length", "Time");
        StdOut.println("GAUSSIAN DISTRIBUTION");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] gaussianDistributionArray = generateGaussianDistributionArray(arrayLength);
            runExperiments(gaussianDistributionArray);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("POISSON DISTRIBUTION");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] poissonDistributionArray = generatePoissonDistributionArray(arrayLength);
            runExperiments(poissonDistributionArray);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("GEOMETRIC DISTRIBUTION");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] geometricDistributionArray = generateGeometricDistributionArray(arrayLength);
            runExperiments(geometricDistributionArray);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("DISCRETE DISTRIBUTION");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] discreteDistributionArray = generateDiscreteDistributionArray(arrayLength);
            runExperiments(discreteDistributionArray);
            arrayLength *= 2;
        }
    }

    private static void runExperiments(Comparable[] array) {
        Comparable[] arrayCopy1 = Arrays.copyOf(array, array.length);
        Comparable[] arrayCopy2 = Arrays.copyOf(array, array.length);

        double timeSelection = time(array, SortTypes.SELECTION);
        printExperiment(SortTypes.SELECTION, array.length, timeSelection);

        double timeInsertion = time(arrayCopy1, SortTypes.INSERTION);
        printExperiment(SortTypes.INSERTION, arrayCopy1.length, timeInsertion);

        double timeShell = time(arrayCopy2, SortTypes.SHELL);
        printExperiment(SortTypes.SHELL, arrayCopy2.length, timeShell);
    }

    public static Comparable[] generateGaussianDistributionArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = StdRandom.gaussian();
        }

        return array;
    }

    public static Comparable[] generatePoissonDistributionArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = StdRandom.poisson(2);
        }

        return array;
    }

    public static Comparable[] generateGeometricDistributionArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = StdRandom.geometric(0.3);
        }

        return array;
    }

    public static Comparable[] generateDiscreteDistributionArray(int arrayLength) {

        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = i % 2 == 0? 0 : 1;
        }

        return array;
    }

    public static double time(Comparable[] array, SortTypes sortType) {
        Stopwatch timer = new Stopwatch();

        switch (sortType) {
            case SELECTION: SelectionSort.selectionSort(array);
                break;
            case INSERTION: InsertionSort.insertionSort(array);
                break;
            case SHELL: ShellSort.shellSort(array);
                break;
        }

        return timer.elapsedTime();
    }

    private static void printExperiment(SortTypes sortType, int arrayLength, double time) {
        StdOut.printf("%15s %17d %8.2f\n", sortType, arrayLength, time);
    }

}
