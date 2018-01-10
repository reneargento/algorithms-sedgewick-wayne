package chapter5.section1;

import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 09/01/18.
 */
public class Exercise9 {

    // O(N * W), where N is the number of strings in the array and W is the longest string length.
    public class LeastSignificantDigitVariableLength {

        public void lsdSort(String[] array) {

            if (array == null || array.length == 0) {
                return;
            }

            int alphabetSize = 256; // Extended ASCII characters

            String[] auxArray = new String[array.length];
            int maxStringLength = getMaxStringLength(array);

            for(int digit = maxStringLength - 1; digit >= 0; digit--) {
                // Sort by key-indexed counting on digitTh char

                // Compute frequency counts
                int count[] = new int[alphabetSize + 1];
                for(int i = 0; i < array.length; i++) {
                    int digitIndex = charAt(array[i], digit);
                    count[digitIndex + 1]++;
                }

                // Transform counts to indices
                for(int r = 0; r < alphabetSize; r++) {
                    count[r + 1] += count[r];
                }

                // Distribute
                for(int i = 0; i < array.length; i++) {
                    int digitIndex = charAt(array[i], digit);
                    int indexInAuxArray = count[digitIndex]++;
                    auxArray[indexInAuxArray] = array[i];
                }

                // Copy back
                for(int i = 0; i < array.length; i++) {
                    array[i] = auxArray[i];
                }
            }
        }

        private int getMaxStringLength(String[] strings) {
            int maxLength = -1;

            for(String string : strings) {
                if (string.length() > maxLength) {
                    maxLength = string.length();
                }
            }

            return maxLength;
        }

        // If digit is non-existent, return 0, which is the smallest value possible
        private int charAt(String string, int digit) {
            if (digit < string.length()) {
                return string.charAt(digit);
            } else {
                return 0;
            }
        }

    }

    public static void main(String[] args) {
        LeastSignificantDigitVariableLength leastSignificantDigitVariableLength =
                new Exercise9().new LeastSignificantDigitVariableLength();

        String[] array1 = {"Rene", "Argento", "Arg", "Alg", "Algorithms", "LSD", "MSD", "3WayStringQuickSort",
                "Dijkstra", "Floyd", "Warshall", "Johnson", "Sedgewick", "Wayne", "Bellman", "Ford", "BFS", "DFS"};
        leastSignificantDigitVariableLength.lsdSort(array1);

        StringJoiner sortedArray1 = new StringJoiner(" ");

        for(String string : array1) {
            sortedArray1.add(string);
        }
        StdOut.println("Sorted array 1");
        StdOut.println(sortedArray1);
        StdOut.println("Expected: \n3WayStringQuickSort Alg Algorithms Arg Argento BFS Bellman DFS Dijkstra Floyd Ford " +
                "Johnson LSD MSD Rene Sedgewick Warshall Wayne\n");

        String[] array2 = {"QuickSort", "MergeSort", "ShellSort", "InsertionSort", "BubbleSort", "SelectionSort"};
        leastSignificantDigitVariableLength.lsdSort(array2);

        StringJoiner sortedArray2 = new StringJoiner(" ");

        for(String string : array2) {
            sortedArray2.add(string);
        }
        StdOut.println("Sorted array 2");
        StdOut.println(sortedArray2);
        StdOut.println("Expected: \nBubbleSort InsertionSort MergeSort QuickSort SelectionSort ShellSort");
    }

}
