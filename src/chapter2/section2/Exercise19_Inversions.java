package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 18/02/17.
 */
//Good explanation here: http://www.geeksforgeeks.org/counting-inversions/
//Related to the Kendall tau distance: https://en.wikipedia.org/wiki/Kendall_tau_distance
public class Exercise19_Inversions {

    public static void main(String[] args) {

        Comparable[] array1 = generateArray1();

        int numberOfInversions1 = countInversions(array1);

        StdOut.println("Number of inversions 1: " + numberOfInversions1
        + "\nExpected: 25");

        Comparable[] array2 = generateArray2();

        int numberOfInversions2 = countInversions(array2);

        StdOut.println("Number of inversions 2: " + numberOfInversions2
                + "\nExpected: 6");
    }

    private static Comparable[] generateArray1() {
        Comparable[] array = new Comparable[10];

        array[0] = 10; //9 inversions
        array[1] = 9; //7 inversions
        array[2] = 8; //5 inversions
        array[3] = 7; //3 inversions
        array[4] = 6; //1 inversion
        array[5] = 5; //0 inversions
        array[6] = 6; //0 inversions
        array[7] = 7; //0 inversions
        array[8] = 8; //0 inversions
        array[9] = 9; //0 inversions

        //Total: 25 inversions

        return array;
    }

    private static Comparable[] generateArray2() {

        Comparable[] array = new Comparable[4];

        array[0] = 4; //3 inversions
        array[1] = 3; //2 inversions
        array[2] = 2; //1 inversions
        array[3] = 1; //0 inversions

        //Total: 6 inversions

        return array;
    }

    private static int countInversions(Comparable[] array) {

        //Used to not modify the original array
        Comparable[] copy = Arrays.copyOf(array, array.length);
        Comparable[] aux = new Comparable[copy.length];

        return splitArrayAndCountInversions(copy, aux, 0, array.length - 1);
    }

    private static int splitArrayAndCountInversions(Comparable[] array, Comparable[] aux, int low, int high) {

        if (low >= high) {
            return 0;
        }

        int middle = low + (high - low) / 2;

        int inversions = splitArrayAndCountInversions(array, aux, low, middle);
        inversions += splitArrayAndCountInversions(array, aux, middle + 1, high);

        return inversions + countInversionsComparingBothParts(array, aux, low, middle, high);
    }

    @SuppressWarnings("unchecked")
    private static int countInversionsComparingBothParts(Comparable[] array, Comparable[] aux, int low, int middle,
                                                         int high) {

        for(int i = low; i <= high; i++) {
            aux[i] = array[i];
        }

        int leftIndex = low;
        int rightIndex = middle + 1;
        int arrayIndex = low;

        int inversions = 0;

        while (leftIndex <= middle && rightIndex <= high) {
            if (aux[leftIndex].compareTo(aux[rightIndex]) <= 0) {
                array[arrayIndex] = aux[leftIndex];
                leftIndex++;
            } else {
                inversions += middle - leftIndex + 1;

                array[arrayIndex] = aux[rightIndex];
                rightIndex++;
            }
            arrayIndex++;
        }

        while (leftIndex <= middle) {
            array[arrayIndex] = aux[leftIndex];

            leftIndex++;
            arrayIndex++;
        }

        return inversions;
    }
}
