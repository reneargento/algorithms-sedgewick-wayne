package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 09/03/18.
 */
public class Exercise36_RandomText {

    // This method uses Knuth-Morris-Pratt algorithm for small (less than text.length / 3) pattern lengths
    // and Rabin-Karp for large (equal to or higher than text.length / 3) pattern lengths.
    // Knuth-Morris-Pratt guarantees a performance of O(M + N) but uses memory O (R * M).
    // Rabin-Karp (Monte Carlo version) also runs in O(N + M) and has a probabilistic guarantee of giving
    // the correct output. It uses O(1) memory, so it is the best choice for large patterns.
    public int countOccurrencesInRandomBinaryText(int patternLength, int textLength) {
        if (patternLength > textLength) {
            throw new IllegalArgumentException("Pattern length cannot be higher than text length");
        }

        StringBuilder randomBinaryText = new StringBuilder();

        for (int i = 0; i < textLength; i++) {
            int randomBit = StdRandom.uniform(2);
            randomBinaryText.append(randomBit);
        }

        int occurrences;
        String pattern = randomBinaryText.substring(textLength - patternLength, textLength);

        if (patternLength < textLength / 3) {
            KnuthMorrisPratt knuthMorrisPratt = new KnuthMorrisPratt(pattern);
            occurrences = knuthMorrisPratt.count(randomBinaryText.toString());
        } else {
            RabinKarp rabinKarp = new RabinKarp(pattern, true);
            occurrences = rabinKarp.count(randomBinaryText.toString());
        }

        // Subtract 1 because we are not counting the last patternLength bits in the text.
        return occurrences - 1;
    }

    // Parameters example: 5 100
    //                     500 1000
    public static void main(String[] args) {
        int patternLength = Integer.parseInt(args[0]);
        int textLength = Integer.parseInt(args[1]);

        int occurrences = new Exercise36_RandomText().countOccurrencesInRandomBinaryText(patternLength, textLength);
        StdOut.println("Number of occurrences: " + occurrences);
    }

}
