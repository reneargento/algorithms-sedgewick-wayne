package chapter5.section5;

import chapter5.section2.TernarySearchTrie;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Created by Rene Argento on 24/04/18.
 */
// To test this code, use a file with enough character sequences to generate 4096 or more codewords
//
// % javac -cp algs4.jar:. Exercise26_RebuildingTheLZWDictionary.java
// % java -cp algs4.jar:. Exercise26_RebuildingTheLZWDictionary - < 5.5_26_input.txt | java -cp algs4.jar:. Exercise26_RebuildingTheLZWDictionary +
public class Exercise26_RebuildingTheLZWDictionary {

    public static class LZWWithDictionaryRebuild {

        private static final int R = 256;  // Number of input characters
        private static final int W = 12;   // Codeword width
        private static final int L = 4096; // Number of codewords = 2^W

        public static void compress() {
            String input = BinaryStdIn.readString();
            TernarySearchTrie<Integer> ternarySearchTrie = resetDictionary();

            int codeword = R + 1; // R is the codeword for EOF

            while (input.length() > 0) {
                String key = ternarySearchTrie.longestPrefixOf(input); // Find max prefix match
                BinaryStdOut.write(ternarySearchTrie.get(key), W); // Print key's encoding

                int length = key.length();
                if (length < input.length()) {

                    // If dictionary is full, empty it and start over
                    if (codeword == L) {
                        ternarySearchTrie = resetDictionary();
                        codeword = R + 1;
                    }

                    ternarySearchTrie.put(input.substring(0, length + 1), codeword); // Add key to symbol table
                    codeword++;
                }

                input = input.substring(length);
            }

            BinaryStdOut.write(R, W); // Write EOF
            BinaryStdOut.close();
        }

        public static void expand() {
            String[] codeTable = resetCodeTable();

            int nextCodeword = R + 1;

            int codeword = BinaryStdIn.readInt(W);

            if (codeword == R) {
                return;
            }

            String value = codeTable[codeword];

            while (true) {
                BinaryStdOut.write(value); // Write current substring
                codeword = BinaryStdIn.readInt(W);

                if (codeword == R) {
                    break;
                }

                String string = codeTable[codeword]; // Get next string

                // Check if lookahead is invalid
                if (nextCodeword == codeword) {
                    string = value + value.charAt(0); // If it is, make codeword from last one
                }

                // If dictionary is full, empty it and start over
                if (nextCodeword == L) {
                    codeTable = resetCodeTable();
                    nextCodeword = R + 1;
                }

                codeTable[nextCodeword++] = value + string.charAt(0); // Add new entry to code table

                value = string; // Update current value
            }

            BinaryStdOut.close();
        }

        private static TernarySearchTrie<Integer> resetDictionary() {
            TernarySearchTrie<Integer> ternarySearchTrie = new TernarySearchTrie<>();

            for (int i = 0; i < R; i++) {
                ternarySearchTrie.put(String.valueOf((char) i), i);
            }

            return ternarySearchTrie;
        }

        private static String[] resetCodeTable() {
            String[] codeTable = new String[L];

            int nextCodeword;

            // Initialize code table with all 1-character strings
            for (nextCodeword = 0; nextCodeword < R; nextCodeword++) {
                codeTable[nextCodeword] = String.valueOf((char) nextCodeword);
            }
            codeTable[nextCodeword] = ""; // (unused) lookahead for EOF

            return codeTable;
        }

    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            LZWWithDictionaryRebuild.compress();
        } else if (args[0].equals("+")) {
            LZWWithDictionaryRebuild.expand();
        }
    }

}
