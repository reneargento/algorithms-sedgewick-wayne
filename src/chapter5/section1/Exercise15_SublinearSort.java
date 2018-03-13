package chapter5.section1;

import chapter2.section1.InsertionSort;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 13/01/18.
 */
public class Exercise15_SublinearSort {

    public class SublinearSort {

        public void sort(int[] array) {
            String[] bits = new String[array.length];
            int bitsArrayIndex = 0;

            // Transform all int values to 32 bits
            for(int value : array) {
                String binaryString = getBinaryStringWithInverseLeadingBit(value);
                bits[bitsArrayIndex++] = binaryString;
            }

            // Sort 16 leading bits (from 15 to 0)
            int initialDigitToSortInLSD = 15;
            lsdSort(bits, initialDigitToSortInLSD);

            // Sort 16 trailing bits with insertion sort
            InsertionSort.insertionSort(bits);

            // Transform bits to int values
            for(int i = 0; i < array.length; i++) {
                array[i] = getIntFromBinaryStringWithInverseLeadingBit(bits[i]);
            }
        }

        private void lsdSort(String[] bits, int initialDigit) {

            int alphabetSize = 2; // Binary digits

            String[] auxArray = new String[bits.length];

            for(int digit = initialDigit; digit >= 0; digit--) {

                // Compute frequency counts
                int[] count = new int[alphabetSize + 1];
                for(int i = 0; i < bits.length; i++) {
                    int bitValue = Character.getNumericValue(bits[i].charAt(digit));
                    count[bitValue + 1]++;
                }

                // Transform counts to indices
                for(int r = 0; r < alphabetSize; r++) {
                    count[r + 1] += count[r];
                }

                // Distribute
                for(int i = 0; i < bits.length; i++) {
                    int bitValue = Character.getNumericValue(bits[i].charAt(digit));
                    int indexInAuxArray = count[bitValue]++;
                    auxArray[indexInAuxArray] = bits[i];
                }

                // Copy back
                for(int i = 0; i < bits.length; i++) {
                    bits[i] = auxArray[i];
                }
            }
        }

        private String getBinaryStringWithInverseLeadingBit(int value) {
            StringBuilder binaryString = new StringBuilder();
            binaryString.append(Integer.toBinaryString(value));

            int zeroesToPadLeft = 31 - binaryString.length();

            for(int i = 0; i < zeroesToPadLeft; i++) {
                binaryString.insert(0, "0");
            }

            // The first bit represents if the number is positive (0) or negative (1)
            // It exists if the number is negative, but must be added if it is not.
            // Here we invert the leading bit value so that during the sort, negative values have a lower position
            // than positive values.
            if (value >= 0) {
                binaryString.insert(0, "1");
            } else {
                binaryString.replace(0, 1, "0");
            }

            return binaryString.toString();
        }

        private int getIntFromBinaryStringWithInverseLeadingBit(String bits) {
            int value = 0;
            int bitIndex = 0;

            for(int digit = bits.length() - 1; digit >= 1; digit--) {
                if (bits.charAt(digit) == '1') {
                    value += Math.pow(2, bitIndex);
                }
                bitIndex++;
            }

            // Leading bit is inverted
            boolean isNegativeValue = bits.charAt(0) == '0';

            if (isNegativeValue) {
                value -= Math.pow(2, 31);
            }

            return value;
        }

    }

    public static void main(String[] args) {
        SublinearSort sublinearSort = new Exercise15_SublinearSort().new SublinearSort();

        int[] array = {
                2147483647,
                1893288285,
                87997899,
                -30,
                2032847926,
                1000,
                0
        };

        sublinearSort.sort(array);

        StringJoiner sortedArray = new StringJoiner(", ");

        for(int value : array) {
            sortedArray.add(String.valueOf(value));
        }

        StdOut.println("Sorted array: " + sortedArray.toString());
        StdOut.println("Expected: -30, 0, 1000, 87997899, 1893288285, 2032847926, 2147483647");
    }

}
