package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayGenerator;

import java.util.Map;

/**
 * Created by Rene Argento on 20/02/17.
 */
// Thanks to YRFT (https://github.com/YRFT) for reporting that the computation of the average length was incorrect:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/177
public class Exercise27_SubarrayLengths {

    private double totalNumberOfSubArrays;
    private double totalSubArrayProportions;

    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]); // 8
        int initialArraySize = Integer.parseInt(args[1]); // 131072

        Exercise27_SubarrayLengths subarrayLengths = new Exercise27_SubarrayLengths();

        Map<Integer, Comparable[]> allInputArrays = ArrayGenerator.generateAllArrays(numberOfExperiments, initialArraySize, 2);
        subarrayLengths.doExperiment(numberOfExperiments, initialArraySize, allInputArrays);
    }

    private void doExperiment(int numberOfExperiments, int initialArraySize, Map<Integer, Comparable[]> allInputArrays) {
        StdOut.printf("%13s %28s\n", "Array Size | ", "AVG Length of other SubArray(%)");

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {
            totalNumberOfSubArrays = 0;
            totalSubArrayProportions = 0;

            Comparable[] array = allInputArrays.get(i);

            mergesort(array);

            double averageLengthOfOtherSubArray = totalSubArrayProportions / totalNumberOfSubArrays * 100;
            StdOut.printf("%10d %31.1f\n", arraySize, averageLengthOfOtherSubArray);

            arraySize *= 2;
        }
    }

    private void mergesort(Comparable[] array) {
        Comparable[] aux = new Comparable[array.length];
        mergesort(array, aux, 0, array.length - 1);
    }

    private void mergesort(Comparable[] array, Comparable[] aux, int low, int high) {

        if (low >= high) {
            return;
        }

        int middle = low + (high - low) / 2;

        mergesort(array, aux, low, middle);
        mergesort(array, aux, middle + 1, high);

        merge(array, aux, low, middle, high);
    }

    @SuppressWarnings("unchecked")
    private void merge(Comparable[] array, Comparable[] aux, int low, int middle, int high) {
        totalNumberOfSubArrays++;

        for(int i = low; i <= high; i++) {
            aux[i] = array[i];
        }

        int leftIndex = low;
        int rightIndex = middle + 1;

        boolean exhausted = false;

        for(int i = low; i <= high; i++) {

            if (leftIndex > middle) {
                array[i] = aux[rightIndex];
                rightIndex++;
            } else if (rightIndex > high) {
                array[i] = aux[leftIndex];
                leftIndex++;
            } else if (aux[leftIndex].compareTo(aux[rightIndex]) <= 0) {
                array[i] = aux[leftIndex];
                leftIndex++;
            } else {
                array[i] = aux[rightIndex];
                rightIndex++;
            }

            if (!exhausted && (leftIndex > middle || rightIndex > high)) {
                exhausted = true;
                double lengthOfOtherSubArray = 0;

                if (leftIndex > middle) {
                    lengthOfOtherSubArray += high - rightIndex + 1;
                } else {
                    lengthOfOtherSubArray += middle - leftIndex + 1;
                }

                double lengthOfBothSubArrays = high - low + 1;
                double proportionOfSubArrayOverArray = lengthOfOtherSubArray / lengthOfBothSubArrays;
                totalSubArrayProportions += proportionOfSubArrayOverArray;
            }
        }
    }
}
