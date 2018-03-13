package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Rene Argento on 11/04/17.
 */
//Based on http://algs4.cs.princeton.edu/25applications/California.java.html
public class Exercise16_UnbiasedElection {

    private class CandidateOrder implements Comparator<String> {

        private String order = "RWQOJMVAHBSGZXNTCIEKUPDYFL";

        @Override
        public int compare(String name1, String name2) {
            int minimumLength = Math.min(name1.length(), name2.length());

            for(int i = 0; i < minimumLength; i++) {
                char currentChar1 = name1.charAt(i);
                char currentChar2 = name2.charAt(i);

                if (order.indexOf(currentChar1) < order.indexOf(currentChar2)) {
                    return -1;
                } else if (order.indexOf(currentChar1) > order.indexOf(currentChar2)) {
                    return 1;
                }
            }

            return name1.length() - name2.length();
        }
    }

    public static void main(String[] args) {
        new Exercise16_UnbiasedElection().california();
    }

    private void california() {

        String candidate1 = "RENE";
        String candidate2 = "LAST CANDIDATE";
        String candidate3 = "VIP";
        String candidate4 = "VIP 2";
        String candidate5 = "ZORD";
        String candidate6 = "OBAMA";
        String candidate7 = "TRUMP";
        String candidate8 = "TRYMP";

        String[] candidates = {candidate1, candidate2, candidate3, candidate4, candidate5, candidate6, candidate7, candidate8};
        Arrays.sort(candidates, new CandidateOrder());

        for(String candidate : candidates) {
            StdOut.println(candidate);
        }

        StdOut.println();
        StdOut.println("Expected:");
        StdOut.println("RENE\n" +
                "OBAMA\n" +
                "VIP\n" +
                "VIP 2\n" +
                "ZORD\n" +
                "TRUMP\n" +
                "TRYMP\n" +
                "LAST CANDIDATE");
    }
}
