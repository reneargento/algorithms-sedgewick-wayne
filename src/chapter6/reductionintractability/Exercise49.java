package chapter6.reductionintractability;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 11/11/18.
 */
public class Exercise49 {

    // O(sqrt(N)) where N is the number.
    // O(2^B) where B is the number of bits needed to represent the input number.
    // Returns a nontrivial factor of the number or -1 if none exists.
    public long getNonTrivialFactor(long number) {
        long numberSqrt = (long) Math.sqrt(number);

        for (int factor = 2; factor <= numberSqrt; factor++) {
            if (number % factor == 0) {
                return factor;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        long number = 37703491;
        long nonTrivialFactor = new Exercise49().getNonTrivialFactor(number);
        String result;

        if (nonTrivialFactor != -1) {
            result = String.valueOf(nonTrivialFactor);
        } else {
            result = "The number does not have nontrivial factors";
        }

        StdOut.println("Nontrivial factor of " + number + ": " + result);
    }

}
