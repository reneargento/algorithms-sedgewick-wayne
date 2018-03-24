package chapter5.section4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 18/03/18.
 */
public class Grep {

    public static void main(String[] args) {
        String regularExpression = "(.*" + args[0] + ".*)";
        RegularExpressionMatcher regularExpressionMatcher = new RegularExpressionMatcher(regularExpression);

        while (StdIn.hasNextLine()) {
            String text = StdIn.readLine();

            if (regularExpressionMatcher.recognizes(text)) {
                StdOut.println(text);
            }
        }
    }

}
