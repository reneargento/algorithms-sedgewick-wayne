package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 12/04/17.
 */
public class Exercise19_KendallTauDistance {

    public static void main(String[] args) {
        //Book example
        int[] array1 = {0, 3, 1, 6, 2, 5, 4};
        int[] array2 = {1, 0, 3, 6, 4, 2, 5};

        int kendallTauDistance = new Exercise19_KendallTauDistance().kendallTau(array1, array2);

        StdOut.println("Kendall tau Distance: " + kendallTauDistance + " Expected: 4");
    }

    private int kendallTau(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            throw new IllegalArgumentException("Array dimensions disagree");
        }

        int[] array1Inverted = new int[array1.length];
        for(int i = 0; i < array1.length; i++) {
            array1Inverted[array1[i]] = i;
        }

        int[] inversions = new int[array2.length];
        for(int i = 0; i < array2.length; i++) {
            inversions[i] = array1Inverted[array2[i]];
        }

        return countInversions(inversions);
    }

    private int countInversions(int[] array) {
        int[] aux = new int[array.length];
        return splitArrayAndCountInversions(array, aux, 0, array.length - 1);
    }

    private int splitArrayAndCountInversions(int[] array, int[] aux, int low, int high) {

        if (low >= high) {
            return 0;
        }

        int middle = low + (high - low) / 2;
        int inversionsInPart1 = splitArrayAndCountInversions(array, aux, low, middle);
        int inversionsInPart2 = splitArrayAndCountInversions(array, aux, middle + 1, high);

        int inversionsOnBothPartsCombined = countInversionsComparingBothParts(array, aux, low, middle, high);

        return inversionsInPart1 + inversionsInPart2 + inversionsOnBothPartsCombined;
    }

    private int countInversionsComparingBothParts(int[] array, int[] aux, int low, int middle, int high) {
        int leftIndex = low;
        int rightIndex = middle + 1;
        int arrayIndex = low;

        for(int i = low; i <= high; i++) {
            aux[i] = array[i];
        }

        int inversions = 0;

        while (leftIndex <= middle && rightIndex <= high) {

            if (aux[leftIndex] <= aux[rightIndex]) {
                array[arrayIndex] = aux[leftIndex++];
            } else {
                inversions += middle - leftIndex + 1;

                array[arrayIndex] = aux[rightIndex++];
            }
            arrayIndex++;
        }

        while (leftIndex <= middle) {
            array[arrayIndex++] = aux[leftIndex++];
        }

        return inversions;
    }

}
