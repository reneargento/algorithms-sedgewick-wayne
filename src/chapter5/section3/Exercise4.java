package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/02/18.
 */
// Thanks to AdamShamaa (https://github.com/AdamShamaa) for suggesting an optimization to this exercise.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/174

// Number of character compares:
    // Typical case: N / 2
    // Worst case: N
public class Exercise4 {

    public int findBlankCharacters(String text, int mSpaces) {
        int textLength = text.length();
        int consecutiveBlanks;
        int i;

        for (i = 0, consecutiveBlanks = 0; i < textLength && consecutiveBlanks < mSpaces; i++) {
            if (text.charAt(i) == ' ') {
                consecutiveBlanks++;
            } else {
                consecutiveBlanks = 0;
            }
        }

        if (consecutiveBlanks == mSpaces) {
            return i - mSpaces;	  //found
        } else {
            return textLength;    //not found
        }
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
