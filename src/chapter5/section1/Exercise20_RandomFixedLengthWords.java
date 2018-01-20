package chapter5.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 15/01/18.
 */
public class Exercise20_RandomFixedLengthWords {

    // Assuming that the alphabet includes numbers, letters and special characters
    public static String[] randomFixedLengthWords(int numberOfStrings, int numberOfCharacters) {
        String[] strings = new String[numberOfStrings];
        int initialCharInASCIITable = 40;
        int finalCharInASCIITable = 126;

        for(int string = 0; string < numberOfStrings; string++) {
            StringBuilder currentString = new StringBuilder();

            for(int character = 0; character < numberOfCharacters; character++) {
                char characterValue = (char) StdRandom.uniform(initialCharInASCIITable, finalCharInASCIITable + 1);
                currentString.append(characterValue);
            }

            strings[string] = currentString.toString();
        }

        return strings;
    }

    public static void main(String[] args) {
        int numberOfStrings = 10;
        int numberOfCharacters = 5;

        String[] randomStrings = Exercise20_RandomFixedLengthWords.randomFixedLengthWords(numberOfStrings,
                numberOfCharacters);
        StdOut.println("Random strings generated:");

        for(String randomString : randomStrings) {
            StdOut.println(randomString);
        }
    }

}
