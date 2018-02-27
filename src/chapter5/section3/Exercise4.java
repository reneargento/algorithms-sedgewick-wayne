package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/02/18.
 */
// The method to find blank characters uses the Knuth-Morris-Pratt algorithm, so the number of character compares is
// the same as the number mentioned in the book:
    // Typical case: 1.1N
    // Worst case: 2N
public class Exercise4 {

    public int findBlankCharacters(String text, int mSpaces) {

        StringBuilder pattern = new StringBuilder();

        for (int space = 0; space < mSpaces; space++) {
            pattern.append(" ");
        }

        KnuthMorrisPratt knuthMorrisPratt = new KnuthMorrisPratt(pattern.toString());
        return knuthMorrisPratt.search(text);
    }

    public static void main(String[] args) {
        Exercise4 exercise4 = new Exercise4();

        String text = " abacada  abr   braca  brabrabracad";

        int index1 = exercise4.findBlankCharacters(text, 1);
        StdOut.println("Index 1: " + index1 + " Expected: 0");

        int index2 = exercise4.findBlankCharacters(text, 2);
        StdOut.println("Index 2: " + index2 + " Expected: 8");

        int index3 = exercise4.findBlankCharacters(text, 3);
        StdOut.println("Index 3: " + index3 + " Expected: 13");

        int index4 = exercise4.findBlankCharacters(text, 4);
        StdOut.println("Index 4: " + index4 + " Expected: 35");
    }

}
