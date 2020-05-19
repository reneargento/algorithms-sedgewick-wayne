package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rene Argento on 9/30/16.
 */
public class Exercise15_2_ThreeSumFaster {

    public static void main(String[] args) {
        int[] array = {-10, -10, -5, 0, 5, 10, 10, 15, 20};
        int[] arrayTest1 = {-3, -2, 2, 3, 5, 99};
        int[] arrayTest2 = {-10, -10, -10, 10};
        int[] arrayTest3 = {0, 0, 0, 0, 0, 0, 0};
        int[] arrayTest4 = {-2, -1, 0, 0, 0, 0, 0, 0, 3};

        StdOut.println("Method 1");
        StdOut.println("ThreeSumFaster: " + threeSumFaster(array) + " Expected: 8");
        StdOut.println("ThreeSumFaster: " + threeSumFaster(arrayTest1) + " Expected: 1");
        StdOut.println("ThreeSumFaster: " + threeSumFaster(arrayTest2) + " Expected: 0");
        StdOut.println("ThreeSumFaster: " + threeSumFaster(arrayTest3) + " Expected: 35");
        StdOut.println("ThreeSumFaster: " + threeSumFaster(arrayTest4) + " Expected: 21");

        StdOut.println("\nMethod 2");
        StdOut.println("ThreeSumFaster: " + threeSumFaster2(array) + " Expected: 8");
        StdOut.println("ThreeSumFaster: " + threeSumFaster2(arrayTest1) + " Expected: 1");
        StdOut.println("ThreeSumFaster: " + threeSumFaster2(arrayTest2) + " Expected: 0");
        StdOut.println("ThreeSumFaster: " + threeSumFaster2(arrayTest3) + " Expected: 35");
        StdOut.println("ThreeSumFaster: " + threeSumFaster2(arrayTest4) + " Expected: 21");
    }

    // Runs in O(n^2) when there are no duplicate keys and in O(n^3) when there are duplicate keys.
    private static int threeSumFaster(int[] array) {
        Map<Integer, List<Integer>> elementIndexes = new HashMap<>();

        for (int i = 0; i < array.length; i++) {
            if (!elementIndexes.containsKey(array[i])) {
                elementIndexes.put(array[i], new ArrayList<>());
            }

            elementIndexes.get(array[i]).add(i);
        }

        int count = 0;

        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                int sum = array[i] + array[j];

                if (elementIndexes.containsKey(-sum)) {
                    for (int elementIndex : elementIndexes.get(-sum)) {
                        if (elementIndex > j) {
                            count++;
                        }
                    }

                }
            }
        }
        return count;
    }

    // Considering that the array is already sorted.
    // Runs in O(n^2).
    private static int threeSumFaster2(int[] array) {
        int start = 0;
        int end = array.length - 1;

        int count = countZeroMatches(array);

        if ((array[start] > 0 && array[end] > 0) || (array[start] < 0 && array[end] < 0)) {
            return 0;
        }

        for (int i = 0; i < array.length; i++) {
            start = i + 1;
            end = array.length - 1;

            while (start < end) {
                if (array[i] + array[start] + array[end] > 0) {
                    end--;
                } else if (array[i] + array[start] + array[end] < 0) {
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

    // Based on the tetrahedral numbers formula
    private static int handleZerosEdgeCase(int numberOfZeros) {
        return ((numberOfZeros - 2) * (numberOfZeros - 1) * numberOfZeros) / 6;
    }

}