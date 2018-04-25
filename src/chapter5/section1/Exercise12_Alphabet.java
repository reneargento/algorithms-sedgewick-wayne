package chapter5.section1;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 11/01/18.
 */
public class Exercise12_Alphabet {

    public interface AlphabetAPI {
        char toChar(int index);            // convert index to corresponding alphabet char
        int toIndex(char character);       // convert character to an index between 0 and R - 1
        boolean contains(char character);  // is character in the alphabet?
        int R();                           // radix (number of characters in alphabet)
        int lgR();                         // number of bits to represent an index
        int[] toIndices(String string);    // convert string to base-R integer
        String toChars(int[] indices);     // convert base-R integer to string over this alphabet
    }

    public class Alphabet implements AlphabetAPI {

        private SeparateChainingHashTable<Character, Integer> charToIndexMap;
        private char[] indexToChar;
        private int size;

        public Alphabet(String string) {
            indexToChar = string.toCharArray();
            Arrays.sort(indexToChar);
            size = indexToChar.length;

            charToIndexMap = new SeparateChainingHashTable<>();

            for(int index = 0; index < indexToChar.length; index++) {
                charToIndexMap.put(indexToChar[index], index);
            }
        }

        public char toChar(int index) {
            if (index < 0 || index >= R()) {
                throw new IllegalArgumentException("Index must be between 0 and " + (R() - 1));
            }

            return indexToChar[index];
        }

        public int toIndex(char character) {
            if (!contains(character)) {
                throw new IllegalArgumentException("Character " + character + " is not in the alphabet");
            }

            return charToIndexMap.get(character);
        }

        public boolean contains(char character) {
            return charToIndexMap.contains(character);
        }

        public int R() {
            return size;
        }

        public int lgR() {
            return (int) (Math.log(size) / Math.log(2));
        }

        public int[] toIndices(String string) {
            int[] indices = new int[string.length()];
            char[] chars = string.toCharArray();

            for(int i = 0; i < chars.length; i++) {
                indices[i] = toIndex(chars[i]);
            }

            return indices;
        }

        public String toChars(int[] indices) {
            StringBuilder chars = new StringBuilder();

            for(int i = 0; i < indices.length; i++) {
                chars.append(toChar(indices[i]));
            }

            return chars.toString();
        }
    }

    public class LSDGeneralAlphabet {

        public void lsdSort(AlphabetAPI alphabet, String[] array, int stringsLength) {

            int alphabetSize = alphabet.R();
            String[] auxArray = new String[array.length];

            for(int digit = stringsLength - 1; digit >= 0; digit--) {
                // Sort by key-indexed counting on digitTh char

                // Compute frequency counts
                int count[] = new int[alphabetSize + 1];
                for(int i = 0; i < array.length; i++) {
                    int digitIndex = alphabet.toIndex(array[i].charAt(digit));
                    count[digitIndex + 1]++;
                }

                // Transform counts to indices
                for(int r = 0; r < alphabetSize; r++) {
                    count[r + 1] += count[r];
                }

                // Distribute
                for(int i = 0; i < array.length; i++) {
                    int digitIndex = alphabet.toIndex(array[i].charAt(digit));
                    int indexInAuxArray = count[digitIndex]++;
                    auxArray[indexInAuxArray] = array[i];
                }

                // Copy back
                for(int i = 0; i < array.length; i++) {
                    array[i] = auxArray[i];
                }
            }
        }
    }

    public class MSDGeneralAlphabet {

        private int alphabetSize;
        private final int CUTOFF_FOR_SMALL_SUBARRAYS = 15;
        private String[] auxArray;

        private AlphabetAPI alphabet;

        public void msdSort(AlphabetAPI alphabet, String[] array) {
            alphabetSize = alphabet.R();
            this.alphabet = alphabet;

            auxArray = new String[array.length];
            sort(array, 0, array.length - 1, 0);
        }

        private void sort(String[] array, int low, int high, int digit) {
            // Sort from array[low] to array[high], starting at the digitTh character
            if (low + CUTOFF_FOR_SMALL_SUBARRAYS >= high) {
                MostSignificantDigit.InsertionSort.sort(array, low, high, digit);
                return;
            }

            // Compute frequency counts
            int[] count = new int[alphabetSize + 2];
            for(int i = low; i <= high; i++) {
                int digitIndex = charAt(array[i], digit) + 2;
                count[digitIndex]++;
            }

            // Transform counts to indices
            for(int r = 0; r < alphabetSize + 1; r++) {
                count[r + 1] += count[r];
            }

            // Distribute
            for(int i = low; i <= high; i++) {
                int digitIndex = charAt(array[i], digit) + 1;
                int indexInAuxArray = count[digitIndex]++;
                auxArray[indexInAuxArray] = array[i];
            }

            // Copy back
            for(int i = low; i <= high; i++) {
                array[i] = auxArray[i - low];
            }

            // Recursively sort for each character value
            for(int r = 0; r < alphabetSize; r++) {
                sort(array, low + count[r], low + count[r + 1] - 1,digit + 1);
            }
        }

        private int charAt(String string, int digit) {
            if (digit < string.length()) {
                return alphabet.toIndex(string.charAt(digit));
            } else {
                return -1;
            }
        }
    }

    public static void main(String[] args) {
        Exercise12_Alphabet exercise12_alphabet = new Exercise12_Alphabet();

        String alphabetChars1 = "3ABDFJLMQRSWacdeghijklmnorstuwy";
        AlphabetAPI alphabet1 = exercise12_alphabet.new Alphabet(alphabetChars1);

        StdOut.println("Alphabet API tests");

        char toChar1 = alphabet1.toChar(0);
        StdOut.println("Index 0 toChar: " + toChar1 + " Expected: 3");

        char toChar2 = alphabet1.toChar(10);
        StdOut.println("Index 10 toChar: " + toChar2 + " Expected: S");

        char toChar3 = alphabet1.toChar(alphabet1.R() - 1);
        StdOut.println("Index R() - 1 toChar: " + toChar3 + " Expected: y");

        StdOut.println();

        int toIndex1 = alphabet1.toIndex('3');
        StdOut.println("Char 3 toIndex: " + toIndex1 + " Expected: 0");

        int toIndex2 = alphabet1.toIndex('S');
        StdOut.println("Char S toIndex: " + toIndex2 + " Expected: 10");

        int toIndex3 = alphabet1.toIndex('y');
        StdOut.println("Char y toIndex: " + toIndex3 + " Expected: 30");

        StdOut.println();

        StdOut.println("Contains R: " + alphabet1.contains('R') + " Expected: true");
        StdOut.println("Contains Z: " + alphabet1.contains('Z') + " Expected: false");

        StdOut.println();

        StdOut.println("R: " + alphabet1.R() + " Expected: 31");
        StdOut.println("lgR: " + alphabet1.lgR() + " Expected: 4");

        StdOut.println();

        String string = "Aemj";
        int[] toIndices = alphabet1.toIndices(string);

        StdOut.print("toIndices(Aemj): ");
        StringJoiner toIndicesStringJoiner = new StringJoiner(" ");

        for(int index : toIndices) {
            toIndicesStringJoiner.add(String.valueOf(index));
        }

        StdOut.println(toIndicesStringJoiner.toString() + " Expected: 1 15 22 19");

        int[] toChars = {0, 10, 30, 1};
        String toCharsString = alphabet1.toChars(toChars);

        StdOut.println("toChars {0, 10, 30, 1}: " + toCharsString + " Expected: 3SyA");


        StdOut.println("\nLSDGeneralAlphabet tests");

        String[] array1 = {"4PGC938",
                "2IYE230",
                "3CIO720",
                "1ICK750",
                "1OHV845",
                "4JZY524",
                "1ICK750",
                "3CIO720",
                "1OHV845",
                "1OHV845",
                "2RLA629",
                "2RLA629",
                "3ATW723"};
        String alphabetChars2 = "0123456789ACEGIJHKLOPRTVWYZ";
        AlphabetAPI alphabet2 = exercise12_alphabet.new Alphabet(alphabetChars2);

        LSDGeneralAlphabet lsdGeneralAlphabet = exercise12_alphabet.new LSDGeneralAlphabet();
        int stringsLength = 7;
        lsdGeneralAlphabet.lsdSort(alphabet2, array1, stringsLength);

        StringJoiner sortedArray1 = new StringJoiner(" ");

        for(String stringInSortedArray : array1) {
            sortedArray1.add(stringInSortedArray);
        }
        StdOut.println("Sorted array 1");
        StdOut.println(sortedArray1);
        StdOut.println("Expected: \n" +
                "1ICK750 1ICK750 1OHV845 1OHV845 1OHV845 2IYE230 2RLA629 2RLA629 3ATW723 3CIO720 3CIO720 " +
                "4JZY524 4PGC938");


        StdOut.println("\nMSDGeneralAlphabet tests");

        String[] array2 = {"Rene", "Argento", "Arg", "Alg", "Algorithms", "LSD", "MSD", "3WayStringQuickSort",
                "Dijkstra", "Floyd", "Warshall", "Johnson", "Sedgewick", "Wayne", "Bellman", "Ford", "BFS", "DFS"};

        MSDGeneralAlphabet msdGeneralAlphabet = exercise12_alphabet.new MSDGeneralAlphabet();
        msdGeneralAlphabet.msdSort(alphabet1, array2);

        StringJoiner sortedArray2 = new StringJoiner(" ");

        for(String stringInSortedArray : array2) {
            sortedArray2.add(stringInSortedArray);
        }
        StdOut.println("Sorted array 2");
        StdOut.println(sortedArray2);
        StdOut.println("Expected: \n3WayStringQuickSort Alg Algorithms Arg Argento BFS Bellman DFS Dijkstra Floyd Ford " +
                "Johnson LSD MSD Rene Sedgewick Warshall Wayne\n");
    }

}
