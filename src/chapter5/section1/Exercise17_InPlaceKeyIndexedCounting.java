package chapter5.section1;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 13/01/18.
 */
public class Exercise17_InPlaceKeyIndexedCounting {

    private class Element implements Comparable<Element> {
        String value;
        int originalIndex;

        Element(String value, int originalIndex) {
            this.value = value;
            this.originalIndex = originalIndex;
        }

        @Override
        public int compareTo(Element other) {
            return this.value.compareTo(other.value);
        }
    }

    public class LeastSignificantDigitInPlace {

        public void lsdSort(Element[] array, int stringsLength) {

            int alphabetSize = 256; // Extended ASCII characters

            for(int digit = stringsLength - 1; digit >= 0; digit--) {
                // Sort by key-indexed counting on digitTh char

                // Compute frequency counts
                int count[] = new int[alphabetSize + 1];
                int[] startIndex = new int[alphabetSize + 1];

                for(int i = 0; i < array.length; i++) {
                    int digitIndex = array[i].value.charAt(digit);
                    count[digitIndex + 1]++;
                    startIndex[digitIndex + 1]++;
                }

                // Transform counts to indices
                for(int r = 0; r < alphabetSize; r++) {
                    count[r + 1] += count[r];
                    startIndex[r + 1] += startIndex[r];
                }

                // Distribute
                for(int i = 0; i < array.length; i++) {

                    // Continue placing items in the correct place until array[i] is in the correct place
                    while (true) {
                        int digitIndex = array[i].value.charAt(digit);

                        // Do not move items that are already in the correct place
                        if (startIndex[digitIndex] <= i && i < count[digitIndex]) {
                            break;
                        }

                        int newIndex = count[digitIndex]++;
                        ArrayUtil.exchange(array, i, newIndex);
                    }
                }
            }
        }

    }

    public class MostSignificantDigitInPlace {

        private int alphabetSize = 256; // Extended ASCII characters; radix
        private final int CUTOFF_FOR_SMALL_SUBARRAYS = 15;

        public void msdSort(Element[] array) {
            sort(array, 0, array.length - 1, 0);
        }

        private void sort(Element[] array, int low, int high, int digit) {

            // Do not use Insertion sort in this case to prove that the sort is not stable

            // Sort from array[low] to array[high], starting at the digitTh character
//            if (low + CUTOFF_FOR_SMALL_SUBARRAYS >= high) {
//                InsertionSort insertionSort = new InsertionSort();
//                insertionSort.sort(array, low, high, digit);
//                return;
//            }

            if (low > high) {
                return;
            }

            // Compute frequency counts
            int[] count = new int[alphabetSize + 2];
            int[] startIndex = new int[alphabetSize + 2];

            for(int i = low; i <= high; i++) {
                int digitIndex = charAt(array[i].value, digit) + 2;
                count[digitIndex]++;
                startIndex[digitIndex]++;
            }

            // Transform counts to indices
            for(int r = 0; r < alphabetSize + 1; r++) {
                count[r + 1] += count[r];
                startIndex[r + 1] += startIndex[r];
            }

            // Distribute
            for(int i = low; i <= high; i++) {

                // Continue placing items in the correct place until array[i] is in the correct place
                while (true) {
                    int digitIndex = charAt(array[i].value, digit) + 1;

                    // Do not move items that are already in the correct place
                    if (startIndex[digitIndex] + low <= i && i < count[digitIndex] + low) {
                        break;
                    }

                    int newIndex = count[digitIndex]++;
                    ArrayUtil.exchange(array, i, newIndex + low);
                }
            }

            // Recursively sort for each character value
            for(int r = 0; r < alphabetSize; r++) {
                sort(array, low + count[r], low + count[r + 1] - 1,digit + 1);
            }
        }

        private int charAt(String string, int digit) {
            if (digit < string.length()) {
                return string.charAt(digit);
            } else {
                return -1;
            }
        }

        // Insertion sort for Strings whose first digit characters are equal
        public class InsertionSort {

            public void sort(Element[] array, int low, int high, int digit) {
                // Sort from array[low] to array[high], starting at the digitTh character
                for(int i = low; i <= high; i++) {
                    for(int j = i; j > low && less(array[j].value, array[j - 1].value, digit); j--) {
                        ArrayUtil.exchange(array, j, j - 1);
                    }
                }
            }

            private boolean less(String string1, String string2, int digit) {
                for(int i = digit; i < Math.min(string1.length(), string2.length()); i++) {
                    if (string1.charAt(i) < string2.charAt(i)) {
                        return true;
                    } else if (string1.charAt(i) > string2.charAt(i)) {
                        return false;
                    }
                }

                return string1.length() < string2.length();
            }
        }

    }

    public static void main(String[] args) {
        Exercise17_InPlaceKeyIndexedCounting inPlaceKeyIndexedCounting = new Exercise17_InPlaceKeyIndexedCounting();

        StdOut.println("In-place LSD tests\n");

        LeastSignificantDigitInPlace leastSignificantDigitInplace =
                inPlaceKeyIndexedCounting.new LeastSignificantDigitInPlace();

        Element[] array1 = new Element[13];
        array1[0] = inPlaceKeyIndexedCounting.new Element("4PGC938", 0);
        array1[1] = inPlaceKeyIndexedCounting.new Element("2IYE230", 1);
        array1[2] = inPlaceKeyIndexedCounting.new Element("3CIO720", 2);
        array1[3] = inPlaceKeyIndexedCounting.new Element("1ICK750", 3);
        array1[4] = inPlaceKeyIndexedCounting.new Element("1OHV845", 4);
        array1[5] = inPlaceKeyIndexedCounting.new Element("4JZY524", 5);
        array1[6] = inPlaceKeyIndexedCounting.new Element("1ICK750", 6);
        array1[7] = inPlaceKeyIndexedCounting.new Element("3CIO720", 7);
        array1[8] = inPlaceKeyIndexedCounting.new Element("1OHV845", 8);
        array1[9] = inPlaceKeyIndexedCounting.new Element("1OHV845", 9);
        array1[10] = inPlaceKeyIndexedCounting.new Element("2RLA629", 10);
        array1[11] = inPlaceKeyIndexedCounting.new Element("2RLA629", 11);
        array1[12] = inPlaceKeyIndexedCounting.new Element("3ATW723", 12);

        int stringsLength1 = 7;
        leastSignificantDigitInplace.lsdSort(array1, stringsLength1);

        StringJoiner sortedArray1 = new StringJoiner(" ");

        for(Element element : array1) {
            sortedArray1.add(element.value);
        }
        StdOut.println("Sorted array 1 with lost of stability");
        StdOut.println(sortedArray1);
        StdOut.println("Expected if there was no lost of stability: \n" +
                "1ICK750 1ICK750 1OHV845 1OHV845 1OHV845 2IYE230 2RLA629 2RLA629 3ATW723 3CIO720 3CIO720 " +
                "4JZY524 4PGC938");


        Element[] array2 = new Element[3];
        array2[0] = inPlaceKeyIndexedCounting.new Element("CAA", 0);
        array2[1] = inPlaceKeyIndexedCounting.new Element("ABB", 1);
        array2[2] = inPlaceKeyIndexedCounting.new Element("ABB", 2);

        int stringsLength2 = 3;
        leastSignificantDigitInplace.lsdSort(array2, stringsLength2);

        StringJoiner sortedArray2 = new StringJoiner("\n");

        for(Element element : array2) {
            sortedArray2.add("Element: " + element.value + " Original index: " + element.originalIndex);
        }
        StdOut.println("\nSorted array 2 with lost of stability");
        StdOut.println(sortedArray2);

        leastSignificantDigitInplace.lsdSort(array2, 3);


        StdOut.println("\nIn-place MSD tests\n");

        MostSignificantDigitInPlace mostSignificantDigitInplace =
                inPlaceKeyIndexedCounting.new MostSignificantDigitInPlace();

        Element[] array3 = new Element[18];
        array3[0] = inPlaceKeyIndexedCounting.new Element("Rene", 0);
        array3[1] = inPlaceKeyIndexedCounting.new Element("Argento", 1);
        array3[2] = inPlaceKeyIndexedCounting.new Element("Arg", 2);
        array3[3] = inPlaceKeyIndexedCounting.new Element("Alg", 3);
        array3[4] = inPlaceKeyIndexedCounting.new Element("Algorithms", 4);
        array3[5] = inPlaceKeyIndexedCounting.new Element("LSD", 5);
        array3[6] = inPlaceKeyIndexedCounting.new Element("MSD", 6);
        array3[7] = inPlaceKeyIndexedCounting.new Element("3WayStringQuickSort", 7);
        array3[8] = inPlaceKeyIndexedCounting.new Element("Dijkstra", 8);
        array3[9] = inPlaceKeyIndexedCounting.new Element("Floyd", 9);
        array3[10] = inPlaceKeyIndexedCounting.new Element("Warshall", 10);
        array3[11] = inPlaceKeyIndexedCounting.new Element("Johnson", 11);
        array3[12] = inPlaceKeyIndexedCounting.new Element("Sedgewick", 12);
        array3[13] = inPlaceKeyIndexedCounting.new Element("Wayne", 13);
        array3[14] = inPlaceKeyIndexedCounting.new Element("Bellman", 14);
        array3[15] = inPlaceKeyIndexedCounting.new Element("Ford", 15);
        array3[16] = inPlaceKeyIndexedCounting.new Element("BFS", 16);
        array3[17] = inPlaceKeyIndexedCounting.new Element("DFS", 17);

        mostSignificantDigitInplace.msdSort(array3);

        StringJoiner sortedArray3 = new StringJoiner(" ");

        for(Element element : array3) {
            sortedArray3.add(element.value);
        }
        StdOut.println("Sorted array 3");
        StdOut.println(sortedArray3);
        StdOut.println("Expected: \n3WayStringQuickSort Alg Algorithms Arg Argento BFS Bellman DFS Dijkstra Floyd Ford " +
                "Johnson LSD MSD Rene Sedgewick Warshall Wayne\n");


        Element[] array4 = new Element[3];
        array4[0] = inPlaceKeyIndexedCounting.new Element("CAA", 0);
        array4[1] = inPlaceKeyIndexedCounting.new Element("ABB", 1);
        array4[2] = inPlaceKeyIndexedCounting.new Element("ABB", 2);

        mostSignificantDigitInplace.msdSort(array4);

        StringJoiner sortedArray4 = new StringJoiner("\n");

        for(Element element : array4) {
            sortedArray4.add("Element: " + element.value + " Original index: " + element.originalIndex);
        }
        StdOut.println("Sorted array 4 with lost of stability");
        StdOut.println(sortedArray4);
    }

}
