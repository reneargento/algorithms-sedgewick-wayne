package chapter5.section3;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 08/03/18.
 */
public class Exercise35_BoyerMooreInBinaryStrings {

    public class BoyerMooreBinaryStrings {

        private int[] right;
        private String pattern;
        private int bitsSize;
        private SeparateChainingHashTable<String, Integer> binaryStringToIntegerMap;

        public BoyerMooreBinaryStrings(String pattern) {
            if (pattern == null) {
                throw new IllegalArgumentException("Invalid pattern");
            }
            // Alphabet size will have length equal to 2^(pattern.length() / 3)
            // So (pattern.length() / 3) must be at most 30 and pattern.length() must be at most 90.
            if (pattern.length() > 90) {
                throw new IllegalArgumentException("Pattern must have length less than or equal to 90");
            }

            this.pattern = pattern;

            bitsSize = (int) (pattern.length() / 3.0);

            if (bitsSize == 0) {
                bitsSize = 1;
            }

            int alphabetSize = (int) Math.pow(2, bitsSize);

            // Used to store previously computed binary string -> integer value mappings
            binaryStringToIntegerMap = new SeparateChainingHashTable<>();

            right = new int[alphabetSize];

            for (int currentChar = 0; currentChar < alphabetSize; currentChar++) {
                right[currentChar] = -1; // -1 for chars not in pattern
            }

            for (int patternIndex = 0; patternIndex + bitsSize <= pattern.length(); patternIndex++)  {
                String binaryString = pattern.substring(patternIndex, patternIndex + bitsSize);

                if (!binaryStringToIntegerMap.contains(binaryString)) {
                    int integerValue = binaryStringToIntegerValue(binaryString);
                    binaryStringToIntegerMap.put(binaryString, integerValue);
                }

                int index = binaryStringToIntegerMap.get(binaryString);
                right[index] = patternIndex; // rightmost position for chars in pattern
            }
        }

        private int binaryStringToIntegerValue(String binaryString) {

            int integerValue = 0;
            int powerOf2 = 1;

            for (int index = binaryString.length() - 1; index >= 0; index--) {

                boolean isDigitSet = binaryString.charAt(index) == '1';

                if (isDigitSet) {
                    if (index == binaryString.length() - 1) {
                        integerValue++;
                    } else {
                        integerValue += powerOf2;
                    }
                }

                powerOf2 = powerOf2 * 2;
            }

            return integerValue;
        }

        // Search for pattern in the text.
        // Returns the index of the first occurrence of the pattern in the text or textLength if no such match.
        public int search(String text) {
            int textLength = text.length();
            int patternLength = pattern.length();

            int skip;

            for (int textIndex = 0; textIndex <= textLength - patternLength; textIndex += skip) {
                // Does the pattern match the text at position textIndex?
                skip = 0;

                for (int patternIndex = patternLength - bitsSize; true; patternIndex -= bitsSize) {

                    if (patternIndex < 0) {
                        patternIndex = 0;
                    }

                    String binaryStringInText =
                            text.substring(textIndex + patternIndex, textIndex + patternIndex + bitsSize);
                    String binaryStringInPattern = pattern.substring(patternIndex, patternIndex + bitsSize);
                    int index;

                    if (!binaryStringToIntegerMap.contains(binaryStringInText)) {
                        index = binaryStringToIntegerValue(binaryStringInText);
                        binaryStringToIntegerMap.put(binaryStringInText, index);
                    } else {
                        index = binaryStringToIntegerMap.get(binaryStringInText);
                    }

                    if (!binaryStringInPattern.equals(binaryStringInText)) {
                        skip = Math.max(1, patternIndex - right[index]);
                        break;
                    }

                    if (patternIndex == 0) {
                        break;
                    }
                }
                if (skip == 0) {
                    return textIndex; // found
                }
            }

            return textLength;        // not found
        }
    }

    public static void main(String[] args) {
        Exercise35_BoyerMooreInBinaryStrings boyerMooreInBinaryStrings = new Exercise35_BoyerMooreInBinaryStrings();

        String text = "0101011101001110100011";

        String pattern1 = "1001110";
        BoyerMooreBinaryStrings boyerMooreBinaryStrings1 = boyerMooreInBinaryStrings.new BoyerMooreBinaryStrings(pattern1);
        int search1 = boyerMooreBinaryStrings1.search(text);
        StdOut.println("Index 1: " + search1 + " Expected: 9");

        String pattern2 = "010101110";
        BoyerMooreBinaryStrings boyerMooreBinaryStrings2 = boyerMooreInBinaryStrings.new BoyerMooreBinaryStrings(pattern2);
        int search2 = boyerMooreBinaryStrings2.search(text);
        StdOut.println("Index 2: " + search2 + " Expected: 0");

        String pattern3 = "10100011";
        BoyerMooreBinaryStrings boyerMooreBinaryStrings3 = boyerMooreInBinaryStrings.new BoyerMooreBinaryStrings(pattern3);
        int search3 = boyerMooreBinaryStrings3.search(text);
        StdOut.println("Index 3: " + search3 + " Expected: 14");

        String pattern4 = "0";
        BoyerMooreBinaryStrings boyerMooreBinaryStrings4 = boyerMooreInBinaryStrings.new BoyerMooreBinaryStrings(pattern4);
        int search4 = boyerMooreBinaryStrings4.search(text);
        StdOut.println("Index 4: " + search4 + " Expected: 0");

        String pattern5 = "111";
        BoyerMooreBinaryStrings boyerMooreBinaryStrings5 = boyerMooreInBinaryStrings.new BoyerMooreBinaryStrings(pattern5);
        int search5 = boyerMooreBinaryStrings5.search(text);
        StdOut.println("Index 5: " + search5 + " Expected: 5");

        String pattern6 = "1111";
        BoyerMooreBinaryStrings boyerMooreBinaryStrings6 = boyerMooreInBinaryStrings.new BoyerMooreBinaryStrings(pattern6);
        int search6 = boyerMooreBinaryStrings6.search(text);
        StdOut.println("Index 6: " + search6 + " Expected: 22");

        String pattern7 = "0101011101001110100011";
        BoyerMooreBinaryStrings boyerMooreBinaryStrings7 = boyerMooreInBinaryStrings.new BoyerMooreBinaryStrings(pattern7);
        int search7 = boyerMooreBinaryStrings7.search(text);
        StdOut.println("Index 7: " + search7 + " Expected: 0");

        String pattern8 = "01010111010011101000111010010101";
        BoyerMooreBinaryStrings boyerMooreBinaryStrings8 = boyerMooreInBinaryStrings.new BoyerMooreBinaryStrings(pattern8);
        int search8 = boyerMooreBinaryStrings8.search(text);
        StdOut.println("Index 8: " + search8 + " Expected: 22");
    }

}
