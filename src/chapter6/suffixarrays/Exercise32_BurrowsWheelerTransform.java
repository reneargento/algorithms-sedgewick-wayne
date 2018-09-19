package chapter6.suffixarrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 15/09/18.
 */
// Based on https://www.coursera.org/lecture/algorithms-on-strings/inverting-burrows-wheeler-transform-C0opC
// and http://www.cs.princeton.edu/courses/archive/spr13/cos226/assignments/burrows.html
public class Exercise32_BurrowsWheelerTransform {

    private static final int ALPHABET_SIZE = 256;

    public String burrowsWheelerTransform(String string) {
        int[] circularSuffixArray = CircularSuffixArrayLinearTime.buildCircularSuffixArray(string);
        StringBuilder burrowsWheelerTransform = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            if (circularSuffixArray[i] == 0) {
                burrowsWheelerTransform.append(string.charAt(string.length() - 1));
            } else {
                burrowsWheelerTransform.append(string.charAt(circularSuffixArray[i] - 1));
            }
        }

        return burrowsWheelerTransform.toString();
    }

    public String burrowsWheelerInverseTransform(String burrowsWheelerTransform) {
        char[] firstColumn = new char[burrowsWheelerTransform.length()];
        int[] next = new int[firstColumn.length];

        // Counting sort
        int[] count = new int[ALPHABET_SIZE + 1];
        for (int i = 0; i < burrowsWheelerTransform.length(); i++) {
            count[burrowsWheelerTransform.charAt(i) + 1]++;
        }

        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // Finish sort and build next[] array
        for (int i = 0; i < burrowsWheelerTransform.length(); i++) {
            char currentChar = burrowsWheelerTransform.charAt(i);
            int index = count[currentChar]++;
            firstColumn[index] = currentChar;
            next[index] = i;
        }

        // Generate Burrows Wheeler Inverse transform
        StringBuilder burrowsWheelerInverseTransform = new StringBuilder();
        int index = next[0];

        for (int i = 0; i < next.length; i++) {
            burrowsWheelerInverseTransform.append(firstColumn[index]);
            index = next[index];
        }

        return burrowsWheelerInverseTransform.toString();
    }

    public static void main(String[] args) {
        Exercise32_BurrowsWheelerTransform burrowsWheelerTransform = new Exercise32_BurrowsWheelerTransform();

        StdOut.println("Burrows Wheeler Transform:");

        String string1 = "mississippi$";
        String bwt1 = burrowsWheelerTransform.burrowsWheelerTransform(string1);
        StdOut.println("BWT:      " + bwt1);
        StdOut.println("Expected: ipssm$pissii");

        String string2 = "barcelona$";
        String bwt2 = burrowsWheelerTransform.burrowsWheelerTransform(string2);
        StdOut.println("\nBWT:      " + bwt2);
        StdOut.println("Expected: anb$rceola");

        String string3 = "banana$";
        String bwt3 = burrowsWheelerTransform.burrowsWheelerTransform(string3);
        StdOut.println("\nBWT:      " + bwt3);
        StdOut.println("Expected: annb$aa");

        StdOut.println("\nBurrows Wheeler Inverse Transform:");

        String burrowsWheelerInverseTransform1 = burrowsWheelerTransform.burrowsWheelerInverseTransform(bwt1);
        StdOut.println("BWT Inverse: " + burrowsWheelerInverseTransform1);
        StdOut.println("Expected:    mississippi$");

        String burrowsWheelerInverseTransform2 = burrowsWheelerTransform.burrowsWheelerInverseTransform(bwt2);
        StdOut.println("\nBWT Inverse: " + burrowsWheelerInverseTransform2);
        StdOut.println("Expected:    barcelona$");

        String burrowsWheelerInverseTransform3 = burrowsWheelerTransform.burrowsWheelerInverseTransform(bwt3);
        StdOut.println("\nBWT Inverse: " + burrowsWheelerInverseTransform3);
        StdOut.println("Expected:    banana$");
    }

}
