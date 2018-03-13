package chapter5.section1;

import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 07/01/18.
 */
public class Exercise1 {

    public class StringSortDynamicAlphabet {

        private static final int CUTOFF_FOR_SMALL_SUBARRAYS = 15;
        private String[] auxArray;
        private int alphabetSize;

        SeparateChainingHashTable<Character, Integer> keysIndex;

        // O(N * w), where w is the average length of the strings in the array
        public void sortStringArray(String[] array) {

            HashSet<Character> uniqueKeys = new HashSet<>();

            for(String string : array) {
                for(char charValue : string.toCharArray()) {
                    if (!uniqueKeys.contains(charValue)) {
                        uniqueKeys.add(charValue);
                    }
                }
            }

            char[] allKeys = new char[uniqueKeys.size()];
            int allKeysIndex = 0;
            for(char key : uniqueKeys.keys()) {
                allKeys[allKeysIndex++] = key;
            }

            // O(R lg R), where R is the alphabet size
            Arrays.sort(allKeys);

            keysIndex = new SeparateChainingHashTable<>();

            for(int keyIndex = 0; keyIndex < allKeys.length; keyIndex++) {
                keysIndex.put(allKeys[keyIndex], keyIndex);
            }

            auxArray = new String[array.length];
            alphabetSize = uniqueKeys.size();

            sortStringArray(array, 0, array.length - 1, 0);
        }

        private void sortStringArray(String[] array, int low, int high, int digit) {
            // Sort from array[low] to array[high], starting at the digitTh character
            if (low + CUTOFF_FOR_SMALL_SUBARRAYS >= high) {
                MostSignificantDigit.InsertionSort.sort(array, low, high, digit);
                return;
            }

            // Compute frequency count
            int[] count = new int[alphabetSize + 2];

            for(int i = low; i <= high; i++) {
                int keyIndex = charAt(array[i], digit) + 2;
                count[keyIndex]++;
            }

            // Transform counts to indices
            for(int r = 0; r < alphabetSize + 1; r++) {
                count[r + 1] += count[r];
            }

            // Distribute
            for(int i = low; i <= high; i++) {
                int keyIndex = charAt(array[i], digit) + 1;
                int indexInAuxArray = count[keyIndex]++;
                auxArray[indexInAuxArray] = array[i];
            }

            // Copy back
            for(int i = low; i <= high; i++) {
                array[i] = auxArray[i - low];
            }

            // Recursively sort for each character value
            for(int r = 0; r < alphabetSize; r++) {
                sortStringArray(array, low + count[r], low + count[r + 1] - 1, digit + 1);
            }
        }

        private int charAt(String string, int digit) {
            if (digit < string.length()) {
                return keysIndex.get(string.charAt(digit));
            } else {
                return -1;
            }
        }
    }

    public static void main(String[] args) {
        StringSortDynamicAlphabet stringSortDynamicAlphabet = new Exercise1().new StringSortDynamicAlphabet();

        String[] array1 = {"Rene", "Argento", "Arg", "Alg", "Algorithms", "LSD", "MSD", "3WayStringQuickSort",
        "Dijkstra", "Floyd", "Warshall", "Johnson", "Sedgewick", "Wayne", "Bellman", "Ford", "BFS", "DFS"};
        stringSortDynamicAlphabet.sortStringArray(array1);

        StringJoiner sortedArray1 = new StringJoiner(" ");

        for(String string : array1) {
            sortedArray1.add(string);
        }
        StdOut.println("Sorted array 1");
        StdOut.println(sortedArray1);
        StdOut.println("Expected: \n3WayStringQuickSort Alg Algorithms Arg Argento BFS Bellman DFS Dijkstra Floyd Ford " +
                "Johnson LSD MSD Rene Sedgewick Warshall Wayne\n");

        String[] array2 = {"QuickSort", "MergeSort", "ShellSort", "InsertionSort", "BubbleSort", "SelectionSort"};
        stringSortDynamicAlphabet.sortStringArray(array2);

        StringJoiner sortedArray2 = new StringJoiner(" ");

        for(String string : array2) {
            sortedArray2.add(string);
        }
        StdOut.println("Sorted array 2");
        StdOut.println(sortedArray2);
        StdOut.println("Expected: \nBubbleSort InsertionSort MergeSort QuickSort SelectionSort ShellSort");
    }

}
