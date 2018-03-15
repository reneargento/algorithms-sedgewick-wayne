package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Rene Argento on 24/02/18.
 */
public class Exercise33_RandomPrimes {

    public class RabinKarpWithLongRandomPrime extends RabinKarp {

        RabinKarpWithLongRandomPrime(String pattern) {
            super(pattern, true);
        }

        @Override
        protected long longRandomPrime() {
            BigInteger prime = BigInteger.probablePrime(31, new Random());
            return prime.longValue();
        }

        // An alternative method
        protected long longRandomPrime2() {
            // A random n-digit number is prime with probability proportional to 1 / n
            // Therefore, generating n numbers has a high probability of finding at least 1 prime number

            long longRandomPrime = 0;
            long numberOfDigits = 14;

            boolean foundLongRandomPrime = false;

            // Only works to check numbers up to 10^14
            List<Integer> primeNumbers = eratosthenesSieveGetOnlyPrimes();

            while (!foundLongRandomPrime) {
                StringBuilder number = new StringBuilder();

                for (int digit = 0; digit < numberOfDigits; digit++) {
                    int randomValue = StdRandom.uniform(10);
                    number.append(randomValue);
                }

                long parsedLongNumber = Long.parseLong(number.toString());
                if (isPrime(parsedLongNumber, primeNumbers)) {
                    foundLongRandomPrime = true;
                    longRandomPrime = parsedLongNumber;
                }
            }

            return longRandomPrime;
        }

        private boolean isPrime(long number, List<Integer> primeNumbers) {
            for (int primeNumber : primeNumbers) {
                if (number % primeNumber == 0) {
                    return false;
                }
            }

            return true;
        }

        // Returns a list containing the prime numbers up to 10^7.
        // Runs in O(n) and can be used to verify the primality of numbers up to 10^14
        private List<Integer> eratosthenesSieveGetOnlyPrimes() {
            int arraySize = 10000000;

            List<Integer> primeNumbers = new ArrayList<>();
            boolean[] isPrime = new boolean[arraySize + 1];

            // 1- Mark all numbers as prime
            for(int i = 2; i < isPrime.length; i++) {
                isPrime[i] = true;
            }

            // 2- Remove numbers multiple of the current element
            // 3- Repeat until we finish verifying all numbers

            for(long i = 2; i <= arraySize; i++) {

                if (isPrime[(int) i]) {
                    for (long j = i * i; j < isPrime.length; j += i) {
                        isPrime[(int) j] = false;
                    }

                    primeNumbers.add((int) i);
                }
            }

            return primeNumbers;
        }
    }

    public static void main(String[] args) {
        Exercise33_RandomPrimes randomPrimes = new Exercise33_RandomPrimes();

        String text = "abacadabrabracabracadabrabrabracad";

        String pattern1 = "abracadabra";
        RabinKarpWithLongRandomPrime rabinKarpWithLongRandomPrime1 = randomPrimes.new RabinKarpWithLongRandomPrime(pattern1);
        int index1 = rabinKarpWithLongRandomPrime1.search(text);
        StdOut.println("Index 1: " + index1 + " Expected: 14");

        String pattern2 = "rab";
        RabinKarpWithLongRandomPrime rabinKarpWithLongRandomPrime2 = randomPrimes.new RabinKarpWithLongRandomPrime(pattern2);
        int index2 = rabinKarpWithLongRandomPrime2.search(text);
        StdOut.println("Index 2: " + index2 + " Expected: 8");

        String pattern3 = "bcara";
        RabinKarpWithLongRandomPrime rabinKarpWithLongRandomPrime3 = randomPrimes.new RabinKarpWithLongRandomPrime(pattern3);
        int index3 = rabinKarpWithLongRandomPrime3.search(text);
        StdOut.println("Index 3: " + index3 + " Expected: 34");

        String pattern4 = "rabrabracad";
        RabinKarpWithLongRandomPrime rabinKarpWithLongRandomPrime4 = randomPrimes.new RabinKarpWithLongRandomPrime(pattern4);
        int index4 = rabinKarpWithLongRandomPrime4.search(text);
        StdOut.println("Index 4: " + index4 + " Expected: 23");

        String pattern5 = "abacad";
        RabinKarpWithLongRandomPrime rabinKarpWithLongRandomPrime5 = randomPrimes.new RabinKarpWithLongRandomPrime(pattern5);
        int index5 = rabinKarpWithLongRandomPrime5.search(text);
        StdOut.println("Index 5: " + index5 + " Expected: 0");
    }

}
