package chapter2.section3;

import chapter2.section1.Exercise35_NonuniformDistributions;
import chapter2.section1.Exercise36_NonuniformData;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 12/03/17.
 */
public class Exercise30_CornerCases {

    public static void main(String[] args) {
        int largeArraySize = (int) Math.pow(10, 7);

        Map<Integer, Comparable[]> allInputArrays = generateAllInputArrays(largeArraySize);

        doExperiment(allInputArrays);
    }

    private static Map<Integer, Comparable[]> generateAllInputArrays(int largeArraySize) {

        Map<Integer, Comparable[]> allInputArrays = new HashMap<>();

        Comparable[] arrayGaussianDistribution = Exercise35_NonuniformDistributions.generateGaussianDistributionArray(largeArraySize);
        allInputArrays.put(0, arrayGaussianDistribution);

        Comparable[] arrayPoissonDistribution = Exercise35_NonuniformDistributions.generatePoissonDistributionArray(largeArraySize);
        allInputArrays.put(1, arrayPoissonDistribution);

        Comparable[] arrayGeometricDistribution = Exercise35_NonuniformDistributions.generateGeometricDistributionArray(largeArraySize);
        allInputArrays.put(2, arrayGeometricDistribution);

        Comparable[] arrayDiscreteDistribution = Exercise35_NonuniformDistributions.generateDiscreteDistributionArray(largeArraySize);
        allInputArrays.put(3, arrayDiscreteDistribution);

        Comparable[] arrayHalfZeroHalfOneValues = Exercise36_NonuniformData.generateHalfZeroHalfOneValuesArray(largeArraySize);
        allInputArrays.put(4, arrayHalfZeroHalfOneValues);

        Comparable[] arrayHalfIncrementingValues = Exercise36_NonuniformData.generateHalfIncrementingValuesArray(largeArraySize);
        allInputArrays.put(5, arrayHalfIncrementingValues);

        Comparable[] arrayHalfZeroHalfRandomValues = Exercise36_NonuniformData.generateHalfZeroHalfRandomValuesArray(largeArraySize);
        allInputArrays.put(6, arrayHalfZeroHalfRandomValues);

        return allInputArrays;
    }

    private static void doExperiment(Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %25s %23s %30s\n", "Array Size | ", "Type | ","QuickSort W/ Random Shuffle |", "QuickSort W/O Random Shuffle");

        for(int i = 0; i < allInputArrays.size(); i++) {

            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            //QuickSort with initial random shuffle
            Stopwatch quickSortWithRandomShuffleTimer = new Stopwatch();
            QuickSort.quickSort(originalArray);
            double quickSortWithRandomShuffleRunningTime = quickSortWithRandomShuffleTimer.elapsedTime();

            //QuickSort without initial random shuffle
            Stopwatch quickSortWithoutRandomShuffleTimer = new Stopwatch();
            quickSortWithoutRandomShuffle(array);
            double quickSortWithoutRandomShuffleRunningTime = quickSortWithoutRandomShuffleTimer.elapsedTime();

            String typeOfArray = getTypeOfArray(i);

            printResults(originalArray.length, typeOfArray, quickSortWithRandomShuffleRunningTime, quickSortWithoutRandomShuffleRunningTime);
        }
    }

    private static String getTypeOfArray(int index) {
        String arrayType = null;

        switch (index) {
            case 0: arrayType = "GaussianDistribution";
                break;
            case 1: arrayType = "PoissonDistribution";
                break;
            case 2: arrayType = "GeometricDistribution";
                break;
            case 3: arrayType = "DiscreteDistribution";
                break;
            case 4: arrayType = "HalfZeroHalfOneValues";
                break;
            case 5: arrayType = "HalfIncrementingValues";
                break;
            case 6: arrayType = "HalfZeroHalfRandomValues";
                break;
        }

        return arrayType;
    }

    private static void quickSortWithoutRandomShuffle(Comparable[] array) {
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

    private static void printResults(int arraySize, String typeOfArray, double quickSortWithRandomShuffleRunningTime,
                                     double quickSortWithoutRandomShuffleRunningTime) {
        StdOut.printf("%10d %25s %30.1f %32.1f\n", arraySize, typeOfArray, quickSortWithRandomShuffleRunningTime, quickSortWithoutRandomShuffleRunningTime);
    }

}
