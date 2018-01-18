package chapter5.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 15/01/18.
 */
public class Exercise18_RandomDecimalKeys {

    public static String[] randomDecimalKeys(int numberOfStrings, int numberOfDigits) {
        String[] strings = new String[numberOfStrings];

        for(int string = 0; string < numberOfStrings; string++) {
            StringBuilder currentString = new StringBuilder();

            for(int digit = 0; digit < numberOfDigits; digit++) {
                int digitValue = StdRandom.uniform(10);
                currentString.append(digitValue);
            }

            strings[string] = currentString.toString();
        }

        return strings;
    }

    public static void main(String[] args) {
        int numberOfStrings = 10;
        int numberOfDigits = 5;

        String[] randomStrings = Exercise18_RandomDecimalKeys.randomDecimalKeys(numberOfStrings, numberOfDigits);
        StdOut.println("Random strings generated:");

        for(String randomString : randomStrings) {
            StdOut.println(randomString);
        }
    }

}
