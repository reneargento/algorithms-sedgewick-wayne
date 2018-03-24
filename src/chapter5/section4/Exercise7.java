package chapter5.section4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 19/03/18.
 */
public class Exercise7 {

    public static class GrepMatch {

        public static void match (String pattern) {
            String regularExpression = "(" + pattern + ")";
            RegularExpressionMatcher regularExpressionMatcher = new RegularExpressionMatcher(regularExpression);

            while (StdIn.hasNextLine()) {
                String text = StdIn.readLine();

                if (regularExpressionMatcher.recognizes(text)) {
                    StdOut.println(text);
                }
            }
        }
    }

    public static void main(String[] args) {
        String pattern1 = "(A|B)(C|D)";
        String pattern2 = "A(B|C)*D";
        String pattern3 = "(A*B|AC)D";

        StdOut.println("Pattern 1:");
        GrepMatch.match(pattern1);
        StdOut.println();

//        StdOut.println("Pattern 2:");
//        GrepMatch.match(pattern2);
//        StdOut.println();

//        StdOut.println("Pattern 3:");
//        GrepMatch.match(pattern3);
//        StdOut.println();
    }

}
