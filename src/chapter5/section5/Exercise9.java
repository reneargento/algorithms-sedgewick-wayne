package chapter5.section5;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayGenerator;

/**
 * Created by Rene Argento on 16/04/18.
 */
// Generates a random ASCII string of length 1000 to be used as input in the tests in exercise 9
public class Exercise9 {

    public static void main(String[] args) {
        int size = 1000;
        String randomString = ArrayGenerator.generateRandomStringOfSpecifiedLengthAllCharacters(size);
        StdOut.println(randomString);
    }

}
