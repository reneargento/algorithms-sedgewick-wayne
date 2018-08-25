package chapter6.suffixarrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/08/18.
 */
public class KeyWordInContext {

    public static void main(String[] args) {
        In in = new In(args[0]);
        int context = Integer.parseInt(args[1]);

        String text = in.readAll().replaceAll("\\s+", " ");
        int length = text.length();
        SuffixArray suffixArray = new SuffixArray(text);

        while (StdIn.hasNextLine()) {
            String query = StdIn.readLine();

            for (int i = suffixArray.rank(query); i < length; i++) {
                // Check if sorted suffix i is a match
                int from1 = suffixArray.index(i);
                int to1 = Math.min(length, from1 + query.length());
                if (!query.equals(text.substring(from1, to1))) {
                    break;
                }

                // Print context surrounding sorted suffix i
                int from2 = Math.max(0, suffixArray.index(i) - context);
                int to2 = Math.min(length, suffixArray.index(i) + query.length() + context);
                StdOut.println(text.substring(from2, to2));
            }
            StdOut.println();
        }
    }

}
