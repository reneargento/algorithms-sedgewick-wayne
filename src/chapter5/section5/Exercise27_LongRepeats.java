package chapter5.section5;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayGenerator;

/**
 * Created by Rene Argento on 25/04/18.
 */
// Generates a random ASCII string of length 1000 and concatenates it to itself so that it can be used as input
// in the tests in exercise 27
public class Exercise27_LongRepeats {

    public static void main(String[] args) {
        int size = 1000;
        String randomString = ArrayGenerator.generateRandomStringOfSpecifiedLengthAllCharacters(size);
        StdOut.println(randomString + randomString);
    }

}
