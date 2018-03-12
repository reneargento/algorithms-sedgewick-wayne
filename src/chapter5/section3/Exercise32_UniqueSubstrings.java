package chapter5.section3;

import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Rene Argento on 06/03/18.
 */
public class Exercise32_UniqueSubstrings {

    public class RabinKarpUniqueSubstringsLengthL {

        private int alphabetSize = 256;
        private int substringLength;
        private long largePrimeNumber;
        private long rm;               // rm = alphabetSize^(substringLength - 1) % largePrimeNumber

        public RabinKarpUniqueSubstringsLengthL(int substringLength) {
            this.substringLength = substringLength;
            largePrimeNumber = longRandomPrime();

            rm = 1;
            for (int patternIndex = 1; patternIndex <= substringLength - 1; patternIndex++) {
                rm = (rm * alphabetSize) % largePrimeNumber;  // Compute alphabetSize^(substringLength - 1) % largePrimeNumber
            }
        }

        // A random 31-bit prime
        private long longRandomPrime() {
            BigInteger prime = BigInteger.probablePrime(31, new Random());
            return prime.longValue();
        }

        public int countUniqueSubstrings(String text) {
            if (text == null) {
                throw new IllegalArgumentException("Text cannot be null");
            }

            if (text.length() < substringLength) {
                return 0;
            }

            HashSet<Long> hashesSet = new HashSet<>();

            long substringHash = hash(text);
            hashesSet.add(substringHash);

            for (int textIndex = substringLength; textIndex < text.length(); textIndex++) {
                // Rolling hash computation: remove leading character, add trailing character
                substringHash = (substringHash + largePrimeNumber - rm * text.charAt(textIndex - substringLength)
                        % largePrimeNumber) % largePrimeNumber;
                substringHash = (substringHash * alphabetSize + text.charAt(textIndex)) % largePrimeNumber;

                // Monte Carlo version - probability of a hash collision is less than 10^(-20) because largePrimeNumber
                // is greater than 10^20
                hashesSet.add(substringHash);
            }

            return hashesSet.size();
        }

        // Horner's method applied to modular hashing
        private long hash(String key) {
            // Compute hash for key[0..substringLength - 1]
            long hash = 0;

            for (int patternIndex = 0; patternIndex < substringLength; patternIndex++) {
                hash = (hash * alphabetSize + key.charAt(patternIndex)) % largePrimeNumber;
            }

            return hash;
        }
    }

    // Parameter example 1: cgcgggcgcg
    // Substring length argument: 3
    // Output expected: 5

    // Parameter example 2: renerene
    // Substring length argument: 4
    // Output expected: 4
    public static void main(String[] args) {
        int substringLength = Integer.parseInt(args[0]);

        RabinKarpUniqueSubstringsLengthL uniqueSubstringsOfLengthL =
                new Exercise32_UniqueSubstrings().new RabinKarpUniqueSubstringsLengthL(substringLength);

        String text = StdIn.readString();
        StdOut.println("Number of unique substrings of length " + substringLength + ": " +
                uniqueSubstringsOfLengthL.countUniqueSubstrings(text));
    }

}