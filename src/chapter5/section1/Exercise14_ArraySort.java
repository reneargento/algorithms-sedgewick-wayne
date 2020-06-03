package chapter5.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 12/01/18.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for reporting a bug related to negative values in arrays:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/157
public class Exercise14_ArraySort {

    public class ArraySort {

        public void threeWayStringQuickSortArrays(int[][] arrays) {
            threeWayStringQuickSortArrays(arrays, 0, arrays.length - 1, 0);
        }

        private void threeWayStringQuickSortArrays(int[][] arrays, int low, int high, int valueIndex) {
            if (low >= high) {
                return;
            }

            int lowerThan = low;
            int greaterThan = high;

            int pivotIndex = StdRandom.uniform(low, high + 1);
            swapArrays(arrays, low, pivotIndex);
            Integer pivot = valueAt(arrays[low], valueIndex);

            int index = low + 1;

            while (index <= greaterThan) {
                Integer currentValue = valueAt(arrays[index], valueIndex);

                if ((currentValue == null && pivot != null) ||
                        (pivot != null && currentValue < pivot)) {
                    swapArrays(arrays, lowerThan++, index++);
                } else if ((pivot == null && currentValue != null) ||
                        (pivot != null && currentValue > pivot)) {
                    swapArrays(arrays, index, greaterThan--);
                } else {
                    index++;
                }
            }

            // Now arrays[low..lowerThan - 1] < pivot = arrays[lowerThan..greaterThan] < arrays[greaterThan + 1..high]
            threeWayStringQuickSortArrays(arrays, low, lowerThan - 1, valueIndex);
            if (pivot != null) {
                threeWayStringQuickSortArrays(arrays, lowerThan, greaterThan, valueIndex + 1);
            }
            threeWayStringQuickSortArrays(arrays, greaterThan + 1, high, valueIndex);
        }

        private Integer valueAt(int[] array, int index) {
            if (index < array.length) {
                return array[index];
            } else {
                return null;
            }
        }

        private void swapArrays(int[][] arrays, int index1, int index2) {
            int[] temp = arrays[index1];
            arrays[index1] = arrays[index2];
            arrays[index2] = temp;
        }
    }

    public static void main(String[] args) {
        ArraySort arraySort = new Exercise14_ArraySort().new ArraySort();

        int[] array1 = {10, 999, 1, 9, 5};
        int[] array2 = {10, 999, 2, 10, 5};
        int[] array3 = {1, 70, 2, 10, 5, 90};
        int[] array4 = {15};
        int[] array5 = {20, 10};
        int[] array6 = {20, 10, 1};
        int[] array7 = {20, 10, 0};
        int[] array8 = {20, 10, -10};
        int[] array9 = {20, 10, -10, 30};
        int[] array10 = {20, 10, -10, 1};

        int[][] arrays = {array1, array2, array3, array4, array5, array6, array7, array8, array9, array10};

        arraySort.threeWayStringQuickSortArrays(arrays);

        StringJoiner sortedArrays = new StringJoiner("\n");

        for(int[] array : arrays) {
            StringJoiner sortedArray = new StringJoiner(", ");

            for(int value : array) {
                sortedArray.add(String.valueOf(value));
            }
            sortedArrays.add(sortedArray.toString());
        }

        StdOut.println("Sorted array");
        StdOut.println(sortedArrays);
        StdOut.println("\nExpected:");
        StdOut.print("1, 70, 2, 10, 5, 90\n" +
                "10, 999, 1, 9, 5\n" +
                "10, 999, 2, 10, 5\n" +
                "15\n" +
                "20, 10\n" +
                "20, 10, -10\n" +
                "20, 10, -10 1\n" +
                "20, 10, -10 30\n" +
                "20, 10, 0\n" +
                "20, 10, 1");
    }

}
