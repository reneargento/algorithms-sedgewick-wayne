package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 25/02/18.
 */
public class Exercise15 {

    public class BruteForceRL {

        private String pattern;
        private int patternLength;

        BruteForceRL(String pattern) {
            this.pattern = pattern;
            patternLength = pattern.length();
        }

        public int search(String text) {

            int textLength = text.length();

            for (int textIndex = 0; textIndex <= textLength - patternLength; textIndex++) {

                int patternIndex;
                for (patternIndex = patternLength - 1; patternIndex >= 0; patternIndex--) {
                    if (text.charAt(textIndex + patternIndex) != pattern.charAt(patternIndex)) {
                        break;
                    }
                }

                if (patternIndex == -1) {
                    return textIndex;  // found
                }
            }

            return textLength;         // not found
        }

    }

    public static void main(String[] args) {
        Exercise15 exercise15 = new Exercise15();

        String text = "abacadabrabracabracadabrabrabracad";

        String pattern1 = "abracadabra";
        BruteForceRL bruteForceRL1 = exercise15.new BruteForceRL(pattern1);
        int index1 = bruteForceRL1.search(text);
        StdOut.println("Index 1: " + index1 + " Expected: 14");

        String pattern2 = "rab";
        BruteForceRL bruteForceRL2 = exercise15.new BruteForceRL(pattern2);
        int index2 = bruteForceRL2.search(text);
        StdOut.println("Index 2: " + index2 + " Expected: 8");

        String pattern3 = "bcara";
        BruteForceRL bruteForceRL3 = exercise15.new BruteForceRL(pattern3);
        int index3 = bruteForceRL3.search(text);
        StdOut.println("Index 3: " + index3 + " Expected: 34");

        String pattern4 = "rabrabracad";
        BruteForceRL bruteForceRL4 = exercise15.new BruteForceRL(pattern4);
        int index4 = bruteForceRL4.search(text);
        StdOut.println("Index 4: " + index4 + " Expected: 23");

        String pattern5 = "abacad";
        BruteForceRL bruteForceRL5 = exercise15.new BruteForceRL(pattern5);
        int index5 = bruteForceRL5.search(text);
        StdOut.println("Index 5: " + index5 + " Expected: 0");
    }

}
