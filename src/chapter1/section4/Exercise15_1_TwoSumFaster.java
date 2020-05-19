package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 9/30/16.
 */
// Thanks to ontvzla (https://github.com/ontvzla) for suggesting an improvement when there are equal numbers in the array:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/150
public class Exercise15_1_TwoSumFaster {

    public static void main(String[] args) {
        int[] array = {-10, -10, -5, 0, 5, 10, 10, 15, 20};
        int[] arrayTest1 = {-3, -2, 2, 3, 5, 99};
        int[] arrayTest2 = {-10, -10, -10, 10, 10};
        int[] arrayTest3 = {0, 0, 0, 0, 0};
        int[] arrayTest4 = {-2, -1, 0, 0, 0, 0, 0, 0, 1};

        StdOut.println("Method 1");
        StdOut.println("TwoSumFaster: " + twoSumFaster(array) + " Expected: 5");
        StdOut.println("TwoSumFaster: " + twoSumFaster(arrayTest1) + " Expected: 2");
        StdOut.println("TwoSumFaster: " + twoSumFaster(arrayTest2) + " Expected: 6");
        StdOut.println("TwoSumFaster: " + twoSumFaster(arrayTest3) + " Expected: 10");
        StdOut.println("TwoSumFaster: " + twoSumFaster(arrayTest4) + " Expected: 16");

        StdOut.println("\nMethod 2");
        StdOut.println("TwoSumFaster: " + twoSumFaster2(array) + " Expected: 5");
        StdOut.println("TwoSumFaster: " + twoSumFaster2(arrayTest1) + " Expected: 2");
        StdOut.println("TwoSumFaster: " + twoSumFaster2(arrayTest2) + " Expected: 6");
        StdOut.println("TwoSumFaster: " + twoSumFaster2(arrayTest3) + " Expected: 10");
        StdOut.println("TwoSumFaster: " + twoSumFaster2(arrayTest4) + " Expected: 16");
    }

    // O(n)
    private static int twoSumFaster(int[] array) {
        Map<Integer, Integer> elementsMap = new HashMap<>();

        for (int key : array) {
            int frequency;

            frequency = elementsMap.getOrDefault(key, 0);
            elementsMap.put(key, frequency + 1);
        }

        int count = 0;

        for (int key : array) {

            if (elementsMap.containsKey(-key)) {
                count += elementsMap.get(-key);

                // Make sure not to consider the same element 0 twice
                if (key == 0) {
                    count--;
                }
            }
        }

        return count / 2;
    }

    // Considering that the array is already sorted.
    // O(n)
    private static int twoSumFaster2(int[] array) {
        int start = 0;
        int end = array.length - 1;

        int count = countZeroMatches(array);

        if ((array[start] > 0 && array[end] > 0) || (array[start] < 0 && array[end] < 0)) {
            return 0;
        }

        while(start < end) {
            if (array[start] + array[end] > 0) {
                end--;
            } else if (array[start] + array[end] < 0) {
                start++;
            } else {
                if (array[start] == 0 && array[end] == 0) {
                    start++;
                    end--;
                    continue;
                }

                int startElement = array[start];
                int equalStartElements = 1;

                // Compare all following elements with startElement
                while (start + 1 < end && array[start + 1] == startElement) {
                    equalStartElements++;
                    start++;
                }

                int endElement = array[end];
                int equalEndElements = 1;

                // Compare all previous elements with endElement
                while (end - 1 > start && array[end - 1] == endElement) {
                    equalEndElements++;
                    end--;
                }

                count += equalStartElements * equalEndElements;

                start++;
                end--;
            }
        }
        return count;
    }

    private static int countZeroMatches(int[] array) {
        int count = 0;

        for (int value : array) {
            if (value == 0) {
                count++;
            }
        }
        return handleZerosEdgeCase(count);
    }

    private static int handleZerosEdgeCase(int numberOfZeros) {
        return (numberOfZeros * (numberOfZeros - 1)) / 2;
    }

}
