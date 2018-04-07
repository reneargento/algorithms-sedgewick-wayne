package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 9/30/16.
 */
public class Exercise15_1_TwoSumFaster {

    public static void main(String[] args) {
        int[] array = {-10, -10, -5, 0, 5, 10, 10, 15, 20};
        int[] arrayTest1 = {-3, -2, 2, 3, 5, 99};
        int[] arrayTest2 = {-10, -10, -10, 10};

        StdOut.println("Method 1");
        StdOut.println("TwoSumFaster: " + twoSumFaster(array) + " Expected: 5");
        StdOut.println("TwoSumFaster: " + twoSumFaster(arrayTest1) + " Expected: 2");
        StdOut.println("TwoSumFaster: " + twoSumFaster(arrayTest2) + " Expected: 3");

        StdOut.println("\nMethod 2");
        StdOut.println("TwoSumFaster: " + twoSumFaster2(array) + " Expected: 5");
        StdOut.println("TwoSumFaster: " + twoSumFaster2(arrayTest1) + " Expected: 2");
        StdOut.println("TwoSumFaster: " + twoSumFaster2(arrayTest2) + " Expected: 3");
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
    // Runs in linear time when there are no duplicate keys and in O(n^2) when there are duplicate keys.
    // The exercise asks to avoid using binary search. If we used binary search to get the ranges we could
    // reduce the complexity to O(n lg n) when there are duplicate keys.
    private static int twoSumFaster2(int[] array) {
        int start = 0;
        int end = array.length - 1;

        int tempIndex;

        int count = 0;

        if ((array[start] > 0 && array[end] > 0) || (array[start] < 0 && array[end] < 0)) {
            return 0;
        }

        while(start < end) {
            if (array[start] + array[end] > 0) {
                end--;
            } else if (array[start] + array[end] < 0) {
                start++;
            } else {
                count++;

                // Compare all following elements with array[end]
                // Could be improved with binary search
                tempIndex = start + 1;
                while(tempIndex < end && array[tempIndex] + array[end] == 0) {
                    count++;
                    tempIndex++;
                }

                //Compare all previous elements with array[start]
                // Could be improved with binary search
                tempIndex = end - 1;
                while(tempIndex > start && array[start] + array[tempIndex] == 0) {
                    count++;
                    tempIndex--;
                }

                start++;
                end--;
            }
        }

        return count;
    }

}
