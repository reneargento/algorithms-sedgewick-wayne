package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 04/03/18.
 */
// Searches for a 2D pattern in a 2D text. Assumes that both the pattern and the text are rectangles of characters.
// O(Mr * Nr * Nc), where Mr is the pattern row length, Nr is the text row length and Nc is the text column length
public class Exercise30_TwoDimensionalSearch {

    public class RabinKarp2D {

        private String[] pattern;
        private long patternHash;
        private int patternRowLength;
        private int patternColumnLength;
        private long largePrimeNumber = 1000000007L;
        private int alphabetSize = 256;
        private long[] rm;  // rm[i] = alphabetSize^i % largePrimeNumber

        public RabinKarp2D(String[] pattern) {

            if (pattern == null || pattern.length == 0) {
                throw new IllegalArgumentException("Invalid pattern");
            }

            this.pattern = pattern;
            patternRowLength = pattern.length;
            patternColumnLength = pattern[0].length(); // Since the pattern is a rectangle of characters,
                                                       // all pattern columns have the same length.
            rm = new long[patternColumnLength];

            rm[0] = 1;
            for (int patternIndex = 1; patternIndex < rm.length; patternIndex++) {
                // Compute alphabetSize^patternIndex % largePrimeNumber for use in removing leading digits.
                rm[patternIndex] = (rm[patternIndex - 1] * alphabetSize) % largePrimeNumber;
            }

            patternHash = hash(pattern);
        }

        private boolean check(String[] text, int textRow, int textColumn) {

            for (int row = 0; row < patternRowLength; row++) {
                for (int patternIndex = 0; patternIndex < patternColumnLength; patternIndex++) {

                    if (text.length <= textRow + row || text[textRow + row].length() <= textColumn + patternIndex) {
                        return false;
                    }

                    if (pattern[row].charAt(patternIndex) != text[textRow + row].charAt(textColumn + patternIndex)) {
                        return false;
                    }
                }
            }

            return true;
        }

        // Horner's method applied to modular hashing
        // O(Mr * Mc), where Mr is the pattern row length and Mc is the pattern column length
        private long hash(String[] key) {
            // Compute hash for key[0            ..       patternColumnLength - 1]
            //                     [.                                            ]
            //                     [.                                            ]
            //                     [patternRowLength - 1..patternColumnLength - 1]
            long hash = 0;
            long currentHash;

            for (int patternRowIndex = 0; patternRowIndex < patternRowLength; patternRowIndex++) {
                currentHash = 0;

                for (int patternColumnIndex = 0; patternColumnIndex < patternColumnLength; patternColumnIndex++) {
                    currentHash = (currentHash * alphabetSize + key[patternRowIndex].charAt(patternColumnIndex))
                            % largePrimeNumber;
                }

                hash += currentHash;
            }

            return hash;
        }

        // Search for a hash match in the text.
        // Returns the index of the first occurrence of the pattern in the text or [textRowLength, textColumnLength]
        // if no such match.
        public int[] search(String[] text) {

            if (text == null || text.length == 0) {
                throw new IllegalArgumentException("Invalid text");
            }

            int textRowLength = text.length;
            int textColumnLength = text[0].length();
            int[] notFoundResult = {textRowLength, textColumnLength};
            int[] result = new int[2];

            if (textRowLength < patternRowLength || textColumnLength < patternColumnLength) {
                return notFoundResult;  // no match
            }

            long initialColumnsHash = hash(text);
            long textHash;

            for (int textRow = 0; textRow <= textRowLength - patternRowLength; textRow++) {

                if (textRow > 0) {
                    // Remove previous row from rolling hash
                    for (int patternColumn = 0; patternColumn < patternColumnLength; patternColumn++) {
                        initialColumnsHash = (initialColumnsHash + largePrimeNumber -
                                rm[patternColumnLength - 1 - patternColumn] *
                                        text[textRow - 1].charAt(patternColumn) % largePrimeNumber) % largePrimeNumber;
                    }

                    // Add next row in rolling hash
                    for (int patternColumn = 0; patternColumn < patternColumnLength; patternColumn++) {
                            initialColumnsHash = (initialColumnsHash +
                                    rm[patternColumnLength - 1 - patternColumn] * text[textRow + patternRowLength - 1].charAt(patternColumn))
                                    % largePrimeNumber;
                    }
                }

                textHash = initialColumnsHash;

                if (textHash == patternHash && check(text, textRow, 0)) {
                    result[0] = textRow;
                    result[1] = 0;

                    return result;  // match
                }

                for (int textColumn = patternColumnLength; textColumn < textColumnLength; textColumn++) {

                    // Remove previous column from rolling hash
                    for (int patternRow = 0; patternRow < patternRowLength; patternRow++) {
                        textHash = (textHash + largePrimeNumber -
                                rm[patternColumnLength - 1] * text[textRow + patternRow].charAt(textColumn - patternColumnLength)
                                        % largePrimeNumber) % largePrimeNumber;
                    }

                    // Add next column in rolling hash
                    for (int patternRow = 0; patternRow < patternRowLength; patternRow++) {

                        if (patternRow == 0) {
                            textHash = (textHash * alphabetSize + text[textRow + patternRow].charAt(textColumn))
                                    % largePrimeNumber;
                        } else {
                            textHash = (textHash + text[textRow + patternRow].charAt(textColumn)) % largePrimeNumber;
                        }
                    }

                    int columnOffset = textColumn - patternColumnLength + 1;

                    if (textHash == patternHash && check(text, textRow, columnOffset)) {
                        result[0] = textRow;
                        result[1] = columnOffset;

                        return result;  // match
                    }
                }
            }

            return notFoundResult;     // no match
        }
    }

    public static void main(String[] args) {
        Exercise30_TwoDimensionalSearch twoDimensionalSearch = new Exercise30_TwoDimensionalSearch();

        String[] pattern1 = {"RE",
                             "NE"};

        String[] text1 = {"ABCD",
                          "RERE",
                          "DRNE",
                          "XPQZ"};

        RabinKarp2D rabinKarp2D1 = twoDimensionalSearch.new RabinKarp2D(pattern1);
        int[] result1 = rabinKarp2D1.search(text1);
        StdOut.println("Result 1: " + result1[0] + ", " + result1[1] + " Expected: 1, 2");


        String[] text2 = {"RECDA",
                          "NEREB",
                          "DRNEC",
                          "XPQZD"};

        RabinKarp2D rabinKarp2D2 = twoDimensionalSearch.new RabinKarp2D(pattern1);
        int[] result2 = rabinKarp2D2.search(text2);
        StdOut.println("Result 2: " + result2[0] + ", " + result2[1] + " Expected: 0, 0");


        String[] text3 = {"ABCRE",
                          "RERNE",
                          "DRNEC",
                          "XPQZD"};

        RabinKarp2D rabinKarp2D3 = twoDimensionalSearch.new RabinKarp2D(pattern1);
        int[] result3 = rabinKarp2D3.search(text3);
        StdOut.println("Result 3: " + result3[0] + ", " + result3[1] + " Expected: 0, 3");


        String[] text4 = {"ABCDA",
                          "REREB",
                          "RENZC",
                          "NEQZD"};

        RabinKarp2D rabinKarp2D4 = twoDimensionalSearch.new RabinKarp2D(pattern1);
        int[] result4 = rabinKarp2D4.search(text4);
        StdOut.println("Result 4: " + result4[0] + ", " + result4[1] + " Expected: 2, 0");


        String[] text5 = {"ABCDA",
                          "REREB",
                          "DRNRE",
                          "XPQNE"};

        RabinKarp2D rabinKarp2D5 = twoDimensionalSearch.new RabinKarp2D(pattern1);
        int[] result5 = rabinKarp2D5.search(text5);
        StdOut.println("Result 5: " + result5[0] + ", " + result5[1] + " Expected: 2, 3");

        // Pattern with more rows than columns
        String[] pattern2 = {"ER",
                             "RZ",
                             "PQ"};

        String[] text6 = {"ABCDA",
                          "REREB",
                          "DRZEC",
                          "XPQZD"};

        RabinKarp2D rabinKarp2D6 = twoDimensionalSearch.new RabinKarp2D(pattern1);
        int[] result6 = rabinKarp2D6.search(text6);
        StdOut.println("Result 6: " + result6[0] + ", " + result6[1] + " Expected: 4, 5");


        RabinKarp2D rabinKarp2D7 = twoDimensionalSearch.new RabinKarp2D(pattern2);
        int[] result7 = rabinKarp2D7.search(text6);
        StdOut.println("Result 7: " + result7[0] + ", " + result7[1] + " Expected: 1, 1");


        // Pattern with more columns than rows
        String[] pattern3 = {"DRZEC",
                             "XPQZD"};

        RabinKarp2D rabinKarp2D8 = twoDimensionalSearch.new RabinKarp2D(pattern3);
        int[] result8 = rabinKarp2D8.search(text6);
        StdOut.println("Result 8: " + result8[0] + ", " + result8[1] + " Expected: 2, 0");


        // Pattern is not in the text, but a similar pattern exists in a different row order
        String[] pattern4 = {"DRZEC",
                             "REREB"};

        RabinKarp2D rabinKarp2D9 = twoDimensionalSearch.new RabinKarp2D(pattern4);
        int[] result9 = rabinKarp2D9.search(text6);
        StdOut.println("Result 9: " + result9[0] + ", " + result9[1] + " Expected: 4, 5");


        // Pattern == text
        RabinKarp2D rabinKarp2D10 = twoDimensionalSearch.new RabinKarp2D(text6);
        int[] result10 = rabinKarp2D10.search(text6);
        StdOut.println("Result 10: " + result10[0] + ", " + result10[1] + " Expected: 0, 0");
    }

}
