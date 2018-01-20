package chapter5.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;

/**
 * Created by Rene Argento on 15/01/18.
 */
public class Exercise21_RandomItems {

    public static String[] randomItems(int numberOfStrings, char[] givenValues) {
        String[] strings = new String[numberOfStrings];

        String[] fourCharacterFixedStrings =
                Exercise20_RandomFixedLengthWords.randomFixedLengthWords(10, 4);
        String[] tenCharacterFixedStrings =
                Exercise20_RandomFixedLengthWords.randomFixedLengthWords(50, 10);

        for(int string = 0; string < numberOfStrings; string++) {
            StringBuilder currentString = new StringBuilder();

            // Field 1: 4-character field
            int random4CharacterStringIndex = StdRandom.uniform(fourCharacterFixedStrings.length);
            currentString.append(fourCharacterFixedStrings[random4CharacterStringIndex]);

            // Field 2: 10-character field
            int random10CharacterStringIndex = StdRandom.uniform(tenCharacterFixedStrings.length);
            currentString.append(tenCharacterFixedStrings[random10CharacterStringIndex]);

            // Field 3: 1-character field
            int givenValueId = StdRandom.uniform(2);
            currentString.append(givenValues[givenValueId]);

            // Field 4: Variable 4 to 15 characters field
            int variableLengthFieldSize = StdRandom.uniform(4, 15 + 1);

            for(int character = 0; character < variableLengthFieldSize; character++) {
                char characterValue;
                int isUpperCaseLetter = StdRandom.uniform(2);

                if (isUpperCaseLetter == 0) {
                    characterValue = (char) StdRandom.uniform(Constants.ASC_II_LOWERCASE_LETTERS_INITIAL_INDEX,
                            Constants.ASC_II_LOWERCASE_LETTERS_FINAL_INDEX + 1);
                } else {
                    characterValue = (char) StdRandom.uniform(Constants.ASC_II_UPPERCASE_LETTERS_INITIAL_INDEX,
                            Constants.ASC_II_UPPERCASE_LETTERS_FINAL_INDEX + 1);
                }

                currentString.append(characterValue);
            }

            strings[string] = currentString.toString();
        }

        return strings;
    }

    public static void main(String[] args) {
        int numberOfStrings = 10;
        char[] givenValues = {'A', 'B'};

        String[] randomStrings = Exercise21_RandomItems.randomItems(numberOfStrings, givenValues);
        StdOut.println("Random strings generated:");

        for(String randomString : randomStrings) {
            StdOut.println(randomString);
        }
    }

}
