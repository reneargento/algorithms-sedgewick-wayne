package chapter6.suffixarrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 16/09/18.
 */
// Based on https://www.quora.com/How-does-the-minimum-expression-algorithm-by-Zhou-Yuan-work and
// https://uva.onlinejudge.org/board/viewtopic.php?t=42601
public class Exercise33_CircularStringLinearization {

    public String smallestCyclicRotationMethod1(String string) {
        int[] circularSuffixArray = CircularSuffixArrayLinearTime.buildCircularSuffixArray(string);
        String concatenatedString = string + string;
        return concatenatedString.substring(circularSuffixArray[0], circularSuffixArray[0] + string.length());
    }

    public String smallestCyclicRotationMethod2(String string) {
        String concatenatedString = string + string;

        int originalStringLength = string.length();
        int length = concatenatedString.length();
        int i = 0;
        int j = 1;
        int k = 0;

        while (i + k < length && j + k < length) {
            if (concatenatedString.charAt(i + k) == concatenatedString.charAt(j + k)) {
                k++;
            } else if (concatenatedString.charAt(i + k) > concatenatedString.charAt(j + k)) {
                i = i + k + 1;
                if (i <= j) {
                    i = j + 1;
                }
                k = 0;
            } else if (concatenatedString.charAt(i + k) < concatenatedString.charAt(j + k)) {
                j = j + k + 1;
                if (j <= i) {
                    j = i + 1;
                }
                k = 0;
            }
        }

        int smallestCyclicRotationIndex = Math.min(i, j);
        return concatenatedString.substring(smallestCyclicRotationIndex, smallestCyclicRotationIndex + originalStringLength);
    }

    public static void main(String[] args) {
        Exercise33_CircularStringLinearization circularStringLinearization = new Exercise33_CircularStringLinearization();

        StdOut.println("Circular Suffix Array method");

        String string1 = "rene";
        String smallestCyclicRotation1 = circularStringLinearization.smallestCyclicRotationMethod1(string1);
        StdOut.println("Smallest Cyclic Rotation: " + smallestCyclicRotation1);
        StdOut.println("Expected:                 ener");

        String string2 = "mississippi";
        String smallestCyclicRotation2 = circularStringLinearization.smallestCyclicRotationMethod1(string2);
        StdOut.println("Smallest Cyclic Rotation: " + smallestCyclicRotation2);
        StdOut.println("Expected:                 imississipp");

        String string3 = "barcelona";
        String smallestCyclicRotation3 = circularStringLinearization.smallestCyclicRotationMethod1(string3);
        StdOut.println("Smallest Cyclic Rotation: " + smallestCyclicRotation3);
        StdOut.println("Expected:                 abarcelon");

        StdOut.println("\nZhou Yuan's minimum expression algorithm method");
        String smallestCyclicRotation4 = circularStringLinearization.smallestCyclicRotationMethod2(string1);
        StdOut.println("Smallest Cyclic Rotation: " + smallestCyclicRotation4);
        StdOut.println("Expected:                 ener");

        String smallestCyclicRotation5 = circularStringLinearization.smallestCyclicRotationMethod2(string2);
        StdOut.println("Smallest Cyclic Rotation: " + smallestCyclicRotation5);
        StdOut.println("Expected:                 imississipp");

        String smallestCyclicRotation6 = circularStringLinearization.smallestCyclicRotationMethod2(string3);
        StdOut.println("Smallest Cyclic Rotation: " + smallestCyclicRotation6);
        StdOut.println("Expected:                 abarcelon");
    }

}
