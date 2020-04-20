package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 9/28/16.
 */
// Thanks to Vivek Bhojawala (https://github.com/VBhojawala) for fixing a bug and suggesting improvements
// in the binarySearch() method at https://github.com/reneargento/algorithms-sedgewick-wayne/issues/6
// Thanks to ajfg93 (https://github.com/ajfg93) for suggesting an iterative solution for the problem at
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/27
// Thanks to emergencyd (https://github.com/emergencyd) for suggesting an improvement on the iterative solution.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/124
public class Exercise10 {

    public static void main(String[] args) {
        int[] testArray1 = {3, 4, 4, 5, 6, 10, 15, 20, 20, 20, 20, 21};
        int elementToSearch1 = 4;
        int elementToSearch2 = 20;
        int elementToSearch3 = -5;

        StdOut.println("Binary search: " + binarySearch(testArray1, elementToSearch1, 0, testArray1.length) +
                " Expected: 1");
        StdOut.println("Binary search: " + binarySearch(testArray1, elementToSearch2, 0, testArray1.length) +
                " Expected: 7");
        StdOut.println("Binary search: " + binarySearch(testArray1, elementToSearch3, 0, testArray1.length) +
                " Expected: -1");

        int[] testArray2 = {4, 4, 4, 4, 4, 4, 15, 20, 20, 20, 20, 21};
        int elementToSearch4 = 4;

        StdOut.println("Binary search: " + binarySearch(testArray2, elementToSearch4, 0, testArray2.length) +
                " Expected: 0");
    }

    private static int binarySearch(int[] array, int element, int low, int high) {
        if (low > high) {
            return -1;
        }

        int middle = low + (high - low) / 2;

        if (array[middle] < element) {
            return binarySearch(array, element, middle + 1, high);
        } else if (array[middle] > element) {
            return binarySearch(array, element, low, middle - 1);
        } else {
            int possibleSmallestIndex = binarySearch(array, element, low, middle - 1);

            if (possibleSmallestIndex == -1) {
                return middle;
            } else {
                return possibleSmallestIndex;
            }
        }
    }

    private static int binarySearchIterative(int[] array, int element, int low, int high) {
        while (low <= high) {
            int middle = low + (high - low) / 2;

            if (array[middle] < element) {
                low = middle + 1;
            } else if (array[middle] > element
                    || (middle > 0 && array[middle - 1] == element)) {
                high = middle - 1;
            } else {
                return middle;
            }
        }
        return -1;
    }
}
