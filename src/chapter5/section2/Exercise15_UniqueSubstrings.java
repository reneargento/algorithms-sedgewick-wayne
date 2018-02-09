package chapter5.section2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 06/02/18.
 */
public class Exercise15_UniqueSubstrings {

    public int countUniqueSubstrings(String text) {
        TernarySearchTrie<Integer> ternarySearchTrie = new TernarySearchTrie<>();

        for (int substringLength = 1; substringLength <= text.length(); substringLength++) {
            for (int i = 0; i < text.length() - substringLength + 1; i++) {
                String substring = text.substring(i, i + substringLength);
                ternarySearchTrie.put(substring, 0); // Key value is not used
            }
        }

        return ternarySearchTrie.size();
    }

    // Parameter example 1: cgcgggcgcg
    // Output expected: 37

    // Parameter example 2: renerene
    // Output expected: 25
    public static void main(String[] args) {
        Exercise15_UniqueSubstrings uniqueSubstrings = new Exercise15_UniqueSubstrings();

        String text = StdIn.readString();
        StdOut.println("Number of unique substrings: " + uniqueSubstrings.countUniqueSubstrings(text));
    }

}
