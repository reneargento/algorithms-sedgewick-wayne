package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Rene Argento on 01/03/18.
 */
// Based on https://www.geeksforgeeks.org/online-algorithm-for-checking-palindrome-in-a-stream/

// Monte Carlo version - O(N + M) with a probabilistic guarantee of giving the correct output.
// Las Vegas version - Always gives the correct output with a probabilistic guarantee of running in N + M.
public class Exercise23 {

    public class PalindromeStreamChecker {

        // Pattern
        private StringBuilder currentString;

        private long largePrimeNumber; // prime number used to evaluate Rabin-Karp's rolling hash
        private int alphabetSize;

        private long hash;

        // Hash of the left half of the pattern reversed
        private long leftHalfReversedHash;
        // Hash of the right half of the pattern
        private long rightHalfHash;

        PalindromeStreamChecker() {
            currentString = new StringBuilder();

            alphabetSize = 256;
            hash = 1;

            largePrimeNumber = longRandomPrime();
        }

        // A random 31-bit prime
        private long longRandomPrime() {
            BigInteger prime = BigInteger.probablePrime(31, new Random());
            return prime.longValue();
        }

        public boolean checkPalindromeOnline(char character) {

            currentString.append(character);
            int patternLength = currentString.length();

            // Base cases: strings of lengths 1 and 2
            if (patternLength == 1) {
                leftHalfReversedHash = character % largePrimeNumber;

                return true;
            } else if (patternLength == 2) {
                rightHalfHash = character % largePrimeNumber;

                return currentString.charAt(0) == currentString.charAt(1);
            }

            if (patternLength % 2 == 0) {
                // Left string -> add trailing character in left half
                // Right string -> add trailing character in right half
                char characterToBeAddedInLeftString = currentString.charAt((patternLength - 1) / 2);

                hash = (hash * alphabetSize) % largePrimeNumber;

                leftHalfReversedHash = (leftHalfReversedHash + hash * characterToBeAddedInLeftString) % largePrimeNumber;
                rightHalfHash = (rightHalfHash * alphabetSize + character) % largePrimeNumber;
            } else {
                // Left string -> no changes
                // Right string -> remove leading character and add trailing character
                char characterToRemove = currentString.charAt(patternLength / 2);

                rightHalfHash = (alphabetSize * (rightHalfHash + largePrimeNumber
                        - characterToRemove * hash) % largePrimeNumber
                        + character) % largePrimeNumber;
            }

            // Monte Carlo version - If hashes match, a palindrome was found.
//            if (leftHalfReversedHash == rightHalfHash) {
//                return true;
//            }

            // Las Vegas version - If hashes match, compare characters.
            if (leftHalfReversedHash == rightHalfHash) {
                boolean isPalindrome = true;

                for (int index = 0; index < currentString.length() / 2; index++) {
                    if (currentString.charAt(index) != currentString.charAt(currentString.length() - 1 - index)) {
                        isPalindrome = false;
                        break;
                    }
                }

                return isPalindrome;
            }

            return false;
        }
    }

    public static void main(String[] args) {
        Exercise23 exercise23 = new Exercise23();

        StdOut.println("Test 1:");
        PalindromeStreamChecker palindromeStreamChecker1 = exercise23.new PalindromeStreamChecker();
        StdOut.println("Check r: " + palindromeStreamChecker1.checkPalindromeOnline('r') + " Expected: true");
        StdOut.println("Check re: " + palindromeStreamChecker1.checkPalindromeOnline('e') + " Expected: false");
        StdOut.println("Check ree: " + palindromeStreamChecker1.checkPalindromeOnline('e') + " Expected: false");
        StdOut.println("Check reer: " + palindromeStreamChecker1.checkPalindromeOnline('r') + " Expected: true");

        StdOut.println();

        StdOut.println("Test 2:");
        PalindromeStreamChecker palindromeStreamChecker2 = exercise23.new PalindromeStreamChecker();
        StdOut.println("Check a: " + palindromeStreamChecker2.checkPalindromeOnline('a') + " Expected: true");
        StdOut.println("Check ab: " + palindromeStreamChecker2.checkPalindromeOnline('b') + " Expected: false");
        StdOut.println("Check abc: " + palindromeStreamChecker2.checkPalindromeOnline('c') + " Expected: false");
        StdOut.println("Check abcb: " + palindromeStreamChecker2.checkPalindromeOnline('b') + " Expected: false");
        StdOut.println("Check abcba: " + palindromeStreamChecker2.checkPalindromeOnline('a') + " Expected: true");

        StdOut.println();

        StdOut.println("Test 3:");
        PalindromeStreamChecker palindromeStreamChecker3 = exercise23.new PalindromeStreamChecker();
        StdOut.println("Check L: " + palindromeStreamChecker3.checkPalindromeOnline('L') + " Expected: true");
        StdOut.println("Check LE: " + palindromeStreamChecker3.checkPalindromeOnline('E') + " Expected: false");
        StdOut.println("Check LEV: " + palindromeStreamChecker3.checkPalindromeOnline('V') + " Expected: false");
        StdOut.println("Check LEVE: " + palindromeStreamChecker3.checkPalindromeOnline('E') + " Expected: false");
        StdOut.println("Check LEVEL: " + palindromeStreamChecker3.checkPalindromeOnline('L') + " Expected: true");
        StdOut.println("Check LEVEL0: " + palindromeStreamChecker3.checkPalindromeOnline('0') + " Expected: false");
    }

}
