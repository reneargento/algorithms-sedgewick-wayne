package chapter5.section2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 06/02/18.
 */
public class Exercise14_UniqueSubstringsOfLengthL {

    public int countUniqueSubstrings(String text, int substringLength) {
        TernarySearchTrie<Integer> ternarySearchTrie = new TernarySearchTrie<>();

        int maxIndex = text.length() - substringLength + 1;

        for (int i = 0; i < maxIndex; i++) {
            String substring = text.substring(i, i + substringLength);
            ternarySearchTrie.put(substring, 0); // Key value is not used
        }

        return ternarySearchTrie.size();
    }

    // Parameter example 1: cgcgggcgcg
    // Substring length argument: 3
    // Output expected: 5

    // Parameter example 2: renerene
    // Substring length argument: 4
    // Output expected: 4
    public static void main(String[] args) {
        Exercise14_UniqueSubstringsOfLengthL uniqueSubstringsOfLengthL = new Exercise14_UniqueSubstringsOfLengthL();

        int substringLength = Integer.parseInt(args[0]);

        String text = StdIn.readString();
        StdOut.println("Number of unique substrings of length " + substringLength + ": " +
                uniqueSubstringsOfLengthL.countUniqueSubstrings(text, substringLength));
    }

}
