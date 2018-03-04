package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/02/18.
 */
public class Exercise12 {

    public class RabinKarpLasVegas extends RabinKarp {

        RabinKarpLasVegas(String pattern) {
            super(pattern, false);
        }

        @Override
        protected boolean check(String text, int textIndex) {

            for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
                if (pattern.charAt(patternIndex) != text.charAt(textIndex + patternIndex)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static void main(String[] args) {
        Exercise12 exercise12 = new Exercise12();

        String text = "abacadabrabracabracadabrabrabracad";

        String pattern1 = "abracadabra";
        RabinKarpLasVegas rabinKarpLasVegas1 = exercise12.new RabinKarpLasVegas(pattern1);
        int index1 = rabinKarpLasVegas1.search(text);
        StdOut.println("Index 1: " + index1 + " Expected: 14");

        String pattern2 = "rab";
        RabinKarpLasVegas rabinKarpLasVegas2 = exercise12.new RabinKarpLasVegas(pattern2);
        int index2 = rabinKarpLasVegas2.search(text);
        StdOut.println("Index 2: " + index2 + " Expected: 8");

        String pattern3 = "bcara";
        RabinKarpLasVegas rabinKarpLasVegas3 = exercise12.new RabinKarpLasVegas(pattern3);
        int index3 = rabinKarpLasVegas3.search(text);
        StdOut.println("Index 3: " + index3 + " Expected: 34");

        String pattern4 = "rabrabracad";
        RabinKarpLasVegas rabinKarpLasVegas4 = exercise12.new RabinKarpLasVegas(pattern4);
        int index4 = rabinKarpLasVegas4.search(text);
        StdOut.println("Index 4: " + index4 + " Expected: 23");

        String pattern5 = "abacad";
        RabinKarpLasVegas rabinKarpLasVegas5 = exercise12.new RabinKarpLasVegas(pattern5);
        int index5 = rabinKarpLasVegas5.search(text);
        StdOut.println("Index 5: " + index5 + " Expected: 0");
    }

}
