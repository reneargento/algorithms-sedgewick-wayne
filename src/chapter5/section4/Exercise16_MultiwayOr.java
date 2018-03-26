package chapter5.section4;

import chapter1.section3.Stack;
import chapter3.section5.HashSet;
import chapter4.section2.Digraph;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/03/18.
 */
public class Exercise16_MultiwayOr {

    public class RegularExpressionMatcherMultiwayOr extends RegularExpressionMatcher {

        public RegularExpressionMatcherMultiwayOr(String regularExpressionString) {
            super(regularExpressionString);

            // Create the nondeterministic finite automaton for the given regular expression
            Stack<Integer> operators = new Stack<>();
            regularExpression = regularExpressionString.toCharArray();
            numberOfStates = regularExpression.length;

            digraph = new Digraph(numberOfStates + 1);

            for (int i = 0; i < numberOfStates; i++) {
                int leftParenthesis = i;

                if (regularExpression[i] == '(' || regularExpression[i] == '|') {
                    operators.push(i);
                } else if (regularExpression[i] == ')') {
                    HashSet<Integer> orOperatorIndexes = new HashSet<>();

                    while (regularExpression[operators.peek()] == '|') {
                        int or = operators.pop();
                        orOperatorIndexes.add(or);
                    }

                    leftParenthesis = operators.pop();

                    for (int orOperatorIndex : orOperatorIndexes.keys()) {
                        digraph.addEdge(orOperatorIndex, i);
                        digraph.addEdge(leftParenthesis, orOperatorIndex + 1);
                    }
                }

                if (i < numberOfStates - 1 && regularExpression[i + 1] == '*') {
                    digraph.addEdge(leftParenthesis, i + 1);
                    digraph.addEdge(i + 1, leftParenthesis);
                }

                if (regularExpression[i] == '(' || regularExpression[i] == '*' || regularExpression[i] == ')') {
                    digraph.addEdge(i, i + 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        Exercise16_MultiwayOr multiwayOr = new Exercise16_MultiwayOr();

        String pattern1 = "(A|B)(C|D)*";
        RegularExpressionMatcherMultiwayOr regularExpressionMatcherMultiwayOr1 =
                multiwayOr.new RegularExpressionMatcherMultiwayOr(pattern1);
        String text1 = "ACCD";
        boolean matches1 = regularExpressionMatcherMultiwayOr1.recognizes(text1);
        StdOut.println("Text 1 check: " + matches1 + " Expected: true");

        String pattern2 = "(.*AB((C|D|E)F)*G)";
        RegularExpressionMatcherMultiwayOr regularExpressionMatcherMultiwayOr2 =
                multiwayOr.new RegularExpressionMatcherMultiwayOr(pattern2);
        String text2 = "RENEABCFDFG";
        boolean matches2 = regularExpressionMatcherMultiwayOr2.recognizes(text2);
        StdOut.println("Text 2 check: " + matches2 + " Expected: true");

        String text3 = "RENEABDFEFG";
        boolean matches3 = regularExpressionMatcherMultiwayOr2.recognizes(text3);
        StdOut.println("Text 3 check: " + matches3 + " Expected: true");

        String text4 = "RENEABCFDFEFG";
        boolean matches4 = regularExpressionMatcherMultiwayOr2.recognizes(text4);
        StdOut.println("Text 4 check: " + matches4 + " Expected: true");

        String text5 = "RENEABCDEFG";
        boolean matches5 = regularExpressionMatcherMultiwayOr2.recognizes(text5);
        StdOut.println("Text 5 check: " + matches5 + " Expected: false");

        String pattern3 = "(A|B|C|D|E|F)";
        RegularExpressionMatcherMultiwayOr regularExpressionMatcherMultiwayOr3 =
                multiwayOr.new RegularExpressionMatcherMultiwayOr(pattern3);
        String text6 = "A";
        boolean matches6 = regularExpressionMatcherMultiwayOr3.recognizes(text6);
        StdOut.println("Text 6 check: " + matches6 + " Expected: true");

        String text7 = "E";
        boolean matches7 = regularExpressionMatcherMultiwayOr3.recognizes(text7);
        StdOut.println("Text 7 check: " + matches7 + " Expected: true");

        String text8 = "Z";
        boolean matches8 = regularExpressionMatcherMultiwayOr3.recognizes(text8);
        StdOut.println("Text 8 check: " + matches8 + " Expected: false");
    }

}
