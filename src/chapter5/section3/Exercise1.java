package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/02/18.
 */
public class Exercise1 {

    public class Brute {

        private String pattern;
        private int patternLength;

        public Brute(String pattern) {
            this.pattern = pattern;
            patternLength = pattern.length();
        }

        // Search for pattern in text.
        // Returns the index of the first occurrence of the pattern string in the text string or textLength if no such match.
        public int search(String text) {
            int textLength = text.length();

            for (int textIndex = 0; textIndex <= textLength - patternLength; textIndex++) {
                int patternIndex;

                for (patternIndex = 0; patternIndex < patternLength; patternIndex++) {
                    if (text.charAt(textIndex + patternIndex) != pattern.charAt(patternIndex)) {
                        break;
                    }
                }

                if (patternIndex == patternLength) {
                    return textIndex;  // found
                }
            }

            return textLength;        // not found
        }

        // Alternate implementation
        public int search2(String text) {
            int textLength = text.length();

            int textIndex;
            int patternIndex;

            for (textIndex = 0, patternIndex = 0; textIndex < textLength && patternIndex < patternLength; textIndex++) {

                if (text.charAt(textIndex) == pattern.charAt(patternIndex)) {
                    patternIndex++;
                } else {
                    textIndex -= patternIndex;
                    patternIndex = 0;
                }
            }

            if (patternIndex == patternLength) {
                return textIndex - patternLength;  // found
            } else {
                return textLength;                 // not found
            }
        }
    }

    public static void main(String[] args) {
        Exercise1 exercise1 = new Exercise1();

        String text = "abacadabrabracabracadabrabrabracad";

        String pattern1 = "abracadabra";
        Brute bruteforce1 = exercise1.new Brute(pattern1);
        int index1 = bruteforce1.search(text);
        StdOut.println("Index 1: " + index1 + " Expected: 14");

        String pattern2 = "rab";
        Brute bruteforce2 = exercise1.new Brute(pattern2);
        int index2 = bruteforce2.search(text);
        StdOut.println("Index 2: " + index2 + " Expected: 8");

        String pattern3 = "bcara";
        Brute bruteforce3 = exercise1.new Brute(pattern3);
        int index3 = bruteforce3.search(text);
        StdOut.println("Index 3: " + index3 + " Expected: 34");

        String pattern4 = "rabrabracad";
        Brute bruteforce4 = exercise1.new Brute(pattern4);
        int index4 = bruteforce4.search(text);
        StdOut.println("Index 4: " + index4 + " Expected: 23");

        String pattern5 = "abacad";
        Brute bruteforce5 = exercise1.new Brute(pattern5);
        int index5 = bruteforce5.search(text);
        StdOut.println("Index 5: " + index5 + " Expected: 0");
    }

}
