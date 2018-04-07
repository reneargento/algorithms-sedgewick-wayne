package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Rene Argento on 9/29/16.
 */
public class Exercise14_4Sum {

    public static void main(String[] args) {

        // Method 1
        StdOut.println("Method 1");
        int[] array1 = {5, 2, -2, -5, -2};
        StdOut.println("4 sum: " + fourSum(array1));
        StdOut.println("Expected: 2");

        int[] array2 = {1, 2, 3, 4, -4, -5, -6, 2, 4, -1};
        StdOut.println("4 sum: " + fourSum(array2));
        StdOut.println("Expected: 13");

        // Method 2
        StdOut.println("\nMethod 2");
        StdOut.println("4 sum: " + fourSum2(array1));
        StdOut.println("Expected: 2");

        StdOut.println("4 sum: " + fourSum2(array2));
        StdOut.println("Expected: 13");
    }

    private static class Pair {
        int index1;
        int index2;

        Pair(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
        }
    }

    // O(n^3)
    private static int fourSum(int[] array) {
        Map<Integer, List<Pair>> sumMap = new HashMap<>();

        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                int sum = array[i] + array[j];

                if (!sumMap.containsKey(sum)) {
                    sumMap.put(sum, new ArrayList<>());
                }

                sumMap.get(sum).add(new Pair(i, j));
            }
        }

        int count = 0;

        for (int key : sumMap.keySet()) {

            if (sumMap.containsKey(-key)) {
                List<Pair> pairs = sumMap.get(key);
                List<Pair> pairsComplement = sumMap.get(-key);

                for (Pair pair1 : pairs) {
                    for (Pair pair2 : pairsComplement) {
                        if (pair1.index2 < pair2.index1) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    // O(n^3 lg n)
    private static int fourSum2(int[] array) {
        Arrays.sort(array);

        int count = 0;

        for(int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                for(int k = j + 1; k < array.length; k++) {

                    int searchElement = -1 * (array[i] + array[j] + array[k]);
                    int elementIndexLeft = binarySearch(array, searchElement, 0, array.length - 1, true);

                    if (elementIndexLeft == -1) {
                        continue;
                    }

                    int elementIndexRight = binarySearch(array, searchElement, 0, array.length - 1, false);

                    if (elementIndexLeft < k + 1) {
                        if (elementIndexRight >= k +1) {
                            elementIndexLeft = k + 1;
                        } else {
                            continue;
                        }
                    }

                    // Debug
//                    for (int d = elementIndexLeft; d <= elementIndexRight; d++) {
//                        StdOut.println(array[i] +  " " + array[j] +  " " + array[k] +  " " + array[d]);
//                    }

                    count += elementIndexRight - elementIndexLeft + 1;
                }
            }
        }

        return count;
    }

    private static int binarySearch(int[] array, int target, int low, int high, boolean searchLow) {

        if (low > high) {
            return -1;
        }

        int middle = low + (high - low) / 2;

        if (array[middle] > target) {
            return binarySearch(array, target, low, middle - 1, searchLow);
        } else if (array[middle] < target) {
            return binarySearch(array, target, middle + 1, high, searchLow);
        } else {
            int nextIndex;

            if (searchLow) {
                nextIndex = binarySearch(array, target, low, middle - 1, true);
            } else {
                nextIndex = binarySearch(array, target, middle + 1, high, false);
            }

            if (nextIndex != -1) {
                return nextIndex;
            } else {
                return middle;
            }
        }
    }
}
