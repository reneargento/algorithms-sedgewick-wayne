package chapter5.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;

/**
 * Created by Rene Argento on 15/01/18.
 */
public class Exercise19_RandomCALicensePlates {

    public static String[] randomPlatesCA(int numberOfStrings) {
        String[] plates = new String[numberOfStrings];

        for(int plate = 0; plate < numberOfStrings; plate++) {
            StringBuilder currentPlate = new StringBuilder();

            // California license plates have 1 initial digit, 3 uppercase letters and 3 final digits
            int initialDigit = StdRandom.uniform(10);
            currentPlate.append(initialDigit);

            // Letters
            for(int digit = 0; digit < 3; digit++) {
                char letter = (char) StdRandom.uniform(Constants.ASC_II_UPPERCASE_LETTERS_INITIAL_INDEX,
                        Constants.ASC_II_UPPERCASE_LETTERS_FINAL_INDEX + 1);
                currentPlate.append(letter);
            }

            // Final digits
            for(int digit = 0; digit < 3; digit++) {
                int digitValue = StdRandom.uniform(10);
                currentPlate.append(digitValue);
            }

            plates[plate] = currentPlate.toString();
        }

        return plates;
    }

    public static void main(String[] args) {
        int numberOfStrings = 10;

        String[] randomPlates = Exercise19_RandomCALicensePlates.randomPlatesCA(numberOfStrings);
        StdOut.println("Random CA license plates generated:");

        for(String randomPlate : randomPlates) {
            StdOut.println(randomPlate);
        }
    }

}
