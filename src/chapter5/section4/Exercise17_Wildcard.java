package chapter5.section4;

import chapter1.section3.Bag;
import chapter4.section2.DirectedDFS;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/03/18.
 */
public class Exercise17_Wildcard {

    public class RegularExpressionMatcherWildcard extends RegularExpressionMatcher {

        public RegularExpressionMatcherWildcard(String regularExpressionString) {
            super(regularExpressionString);
        }

        @Override
        public boolean recognizes(String text) {
            Bag<Integer> allPossibleStates = new Bag<>();
            DirectedDFS directedDFS = new DirectedDFS(digraph, 0);

            for (int vertex = 0; vertex < digraph.vertices(); vertex++) {
                if (directedDFS.marked(vertex)) {
                    allPossibleStates.add(vertex);
                }
            }

            for (int i = 0; i < text.length(); i++) {
                // Compute possible NFA states for text[i + 1]
                Bag<Integer> states = new Bag<>();

                for (int vertex : allPossibleStates) {
                    if (vertex < numberOfStates) {
                        if (regularExpression[vertex] == text.charAt(i) || regularExpression[vertex] == '.') {
                            states.add(vertex + 1);
                        }
                    }
                }

                allPossibleStates = new Bag<>();
                directedDFS = new DirectedDFS(digraph, states);

                for (int vertex = 0; vertex < digraph.vertices(); vertex++) {
                    if (directedDFS.marked(vertex)) {
                        allPossibleStates.add(vertex);
                    }
                }

                // Optimization if no states are reachable
                if (allPossibleStates.size() == 0) {
                    return false;
                }
            }


            for (int vertex : allPossibleStates) {
                if (vertex == numberOfStates) {
                    return true;
                }
            }

            return false;
        }

    }

    public static void main(String[] args) {
        Exercise17_Wildcard wildcard = new Exercise17_Wildcard();

        String pattern1 = ".*NEEDLE.*";
        RegularExpressionMatcherWildcard regularExpressionMatcherWildcard1 =
                wildcard.new RegularExpressionMatcherWildcard(pattern1);
        String text1 = "A HAYSTACK NEEDLE IN";
        boolean matches1 = regularExpressionMatcherWildcard1.recognizes(text1);
        StdOut.println("Text 1 check: " + matches1 + " Expected: true");

        String pattern2 = "R.N.1.3";
        RegularExpressionMatcherWildcard regularExpressionMatcherWildcard2 =
                wildcard.new RegularExpressionMatcherWildcard(pattern2);
        String text2 = "RENE123";
        boolean matches2 = regularExpressionMatcherWildcard2.recognizes(text2);
        StdOut.println("Text 2 check: " + matches2 + " Expected: true");

        String text3 = "RRNN193";
        boolean matches3 = regularExpressionMatcherWildcard2.recognizes(text3);
        StdOut.println("Text 3 check: " + matches3 + " Expected: true");

        String text4 = "RENE333";
        boolean matches4 = regularExpressionMatcherWildcard2.recognizes(text4);
        StdOut.println("Text 4 check: " + matches4 + " Expected: false");
    }

}
