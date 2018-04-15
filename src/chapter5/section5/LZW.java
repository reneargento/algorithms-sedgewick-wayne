package chapter5.section5;

import chapter5.section2.TernarySearchTrie;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Created by Rene Argento on 13/04/18.
 */

/**
 *  WARNING: Starting with Oracle Java 6, Update 7 the substring method takes time and space
 *  linear in the size of the extracted substring (instead of constant space and time as
 *  in earlier implementations).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 */
public class LZW {

    private static final int R = 256;  // Number of input characters
    private static final int W = 12;   // Codeword width
    private static final int L = 4096; // Number of codewords = 2^W

    public static void compress() {
        String input = BinaryStdIn.readString();
        TernarySearchTrie<Integer> ternarySearchTrie = new TernarySearchTrie<>();

        for (int i = 0; i < R; i++) {
            ternarySearchTrie.put(String.valueOf((char) i), i);
        }

        int codeword = R + 1; // R is the codeword for EOF

        while (input.length() > 0) {
            String key = ternarySearchTrie.longestPrefixOf(input); // Find max prefix match
            BinaryStdOut.write(ternarySearchTrie.get(key), W); // Print key's encoding

            int length = key.length();
            if (length < input.length() && codeword < L) {
                ternarySearchTrie.put(input.substring(0, length + 1), codeword); // Add key to symbol table
                codeword++;
            }

            input = input.substring(length);
        }

        BinaryStdOut.write(R, W); // Write EOF
        BinaryStdOut.close();
    }

    public static void expand() {
        String[] codeTable = new String[L];

        int nextCodeword;

        // Initialize code table with all 1-character strings
        for (nextCodeword = 0; nextCodeword < R; nextCodeword++) {
            codeTable[nextCodeword] = String.valueOf((char) nextCodeword);
        }
        codeTable[nextCodeword++] = ""; // (unused) lookahead for EOF

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

            if (nextCodeword < L) {
                codeTable[nextCodeword++] = value + string.charAt(0); // Add new entry to code table
            }

            value = string; // Update current value
        }

        BinaryStdOut.close();
    }

}
