package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 03/03/18.
 */
public class Exercise26_CyclicRotationCheck {

    public boolean isCyclicRotation(String string1, String string2) {
        if (string1.length() != string2.length()) {
            return false;
        }

        String concatenatedString = string1 + string1;

        RabinKarp rabinKarp = new RabinKarp(string2, true);
        return rabinKarp.search(concatenatedString) != concatenatedString.length();
    }

    public static void main(String[] args) {
        Exercise26_CyclicRotationCheck cyclicRotationCheck = new Exercise26_CyclicRotationCheck();

        boolean check1 = cyclicRotationCheck.isCyclicRotation("example", "ampleex");
        StdOut.println("Check 1: " + check1 + " Expected: true");

        boolean check2 = cyclicRotationCheck.isCyclicRotation("example", "ample");
        StdOut.println("Check 2: " + check2 + " Expected: false");

        boolean check3 = cyclicRotationCheck.isCyclicRotation("stackoverflow", "tackoverflows");
        StdOut.println("Check 3: " + check3 + " Expected: true");

        boolean check4 = cyclicRotationCheck.isCyclicRotation("stackoverflow", "ackoverflowst");
        StdOut.println("Check 4: " + check4 + " Expected: true");

        boolean check5 = cyclicRotationCheck.isCyclicRotation("stackoverflow", "overflowstack");
        StdOut.println("Check 5: " + check5 + " Expected: true");

        boolean check6 = cyclicRotationCheck.isCyclicRotation("stackoverflow", "stackoverflwo");
        StdOut.println("Check 6: " + check6 + " Expected: false");
    }

}
