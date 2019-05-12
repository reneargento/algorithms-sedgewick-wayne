package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/10/16.
 */
// Based on http://algs4.cs.princeton.edu/14analysis/:
// Answer: Instead of searching based on powers of two (binary search),
// use Fibonacci numbers (which also grow exponentially).
// Maintain the current search range to be [i, i + F(k)] and keep F(k), F(k-1) in two variables.
// At each step compute F(k-2) via subtraction, check element i + F(k-2),
// and update the range to either [i, i + F(k-2)] or [i + F(k-2), i + F(k-2) + F(k-1)].

// Thanks to shftdlt (https://github.com/shftdlt) for suggesting improvements in this exercise.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/47
public class Exercise22_BinarySearchAddSub {

    public static void main(String... args) {
        int[] array = {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7};

        Exercise22_BinarySearchAddSub exercise22_binarySearchAddSub = new Exercise22_BinarySearchAddSub();
        int index1 = exercise22_binarySearchAddSub.binarySearch(array, 2);
        int index2 = exercise22_binarySearchAddSub.binarySearch(array, 9);
        int index3 = exercise22_binarySearchAddSub.binarySearch(array, -3);
        int index4 = exercise22_binarySearchAddSub.binarySearch(array, 7);
        int index5 = exercise22_binarySearchAddSub.binarySearch(array, -2);

        StdOut.println("Is element in the array: " + (index1 != -1) + " Expected: true");
        StdOut.println("Is element in the array: " + (index2 != -1) + " Expected: false");
        StdOut.println("Is element in the array: " + (index3 != -1) + " Expected: false");
        StdOut.println("Is element in the array: " + (index4 != -1) + " Expected: true");
        StdOut.println("Is element in the array: " + (index5 != -1) + " Expected: true");
    }

    private int binarySearch(int[] array, int key) {
        int aux;
        int fibonacciBeforeN = 0;
        int fibonacciN = 1;

        // Compute F(k)
        while (fibonacciN < array.length - 1) {
            aux = fibonacciN;
            fibonacciN = fibonacciBeforeN + fibonacciN;
            fibonacciBeforeN = aux;
        }

        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            while (fibonacciBeforeN > 0 && fibonacciN >= high - low) {
                // Compute F(k-2)
                aux = fibonacciBeforeN;
                fibonacciBeforeN = fibonacciN - fibonacciBeforeN; // F(k-2)
                fibonacciN = aux; // F(k-1)
            }

            int elementToCheck = low + fibonacciBeforeN;

            if (key < array[elementToCheck]) {
                high = elementToCheck - 1;
            } else if (key > array[elementToCheck]) {
                low = elementToCheck + 1;
            } else {
                return elementToCheck;
            }
        }

        return -1;
    }

}
