package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 26/07/17.
 */
public class Exercise32_HashAttack {

    public int hashCode(String string) {
        int hash = 0;

        for(int i = 0; i < string.length(); i++) {
            hash = (hash * 31) + string.charAt(i);
        }

        return hash;
    }

    private List<String> generateStringsInput(int n) {
        String[] values = {"Aa", "BB"};

        List<String> strings = new ArrayList<>();
        generateStrings(n, 0, strings, "", values);

        return strings;
    }

    private void generateStrings(int n, int currentIndex, List<String> strings, String currentString, String[] values) {
        if (currentIndex == n) {
            strings.add(currentString);
            return;
        }

        for(String value : values) {
            String newValue = currentString + value;
            int newIndex = currentIndex + 1;

            generateStrings(n, newIndex, strings, newValue, values);
        }
    }

    public static void main(String[] args) {
        Exercise32_HashAttack hashAttack = new Exercise32_HashAttack();
        List<String> hashAttackInput = hashAttack.generateStringsInput(3);

        for(String string : hashAttackInput) {
            StdOut.println(string);
        }
    }

}
