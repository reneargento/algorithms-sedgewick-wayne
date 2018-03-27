package chapter5.section4;

import chapter1.section3.Stack;
import chapter3.section5.HashSet;
import chapter4.section2.Digraph;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/03/18.
 */
public class Exercise18_OneOrMore {

    public class RegularExpressionMatcherOneOrMore extends RegularExpressionMatcher {

        public RegularExpressionMatcherOneOrMore(String regularExpressionString) {
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

                if (i < numberOfStates - 1) {
                    if (regularExpression[i + 1] == '*') {
                        digraph.addEdge(leftParenthesis, i + 1);
                        digraph.addEdge(i + 1, leftParenthesis);
                    } else if (regularExpression[i + 1] == '+') {
                        digraph.addEdge(i + 1, leftParenthesis);
                    }
                }

                if (regularExpression[i] == '(' || regularExpression[i] == '*' || regularExpression[i] == ')'
                        || regularExpression[i] == '+') {
                    digraph.addEdge(i, i + 1);
                }
            }

        }
    }

    public static void main(String[] args) {
        Exercise18_OneOrMore oneOrMore = new Exercise18_OneOrMore();

        String pattern1 = "(RE)+NE";
        RegularExpressionMatcherOneOrMore regularExpressionMatcherOneOrMore1 =
                oneOrMore.new RegularExpressionMatcherOneOrMore(pattern1);
        String text1 = "RERERENE";
        boolean matches1 = regularExpressionMatcherOneOrMore1.recognizes(text1);
        StdOut.println("Text 1 check: " + matches1 + " Expected: true");

        String text2 = "RONE";
        boolean matches2 = regularExpressionMatcherOneOrMore1.recognizes(text2);
        StdOut.println("Text 2 check: " + matches2 + " Expected: false");

        String text3 = "RENE";
        boolean matches3 = regularExpressionMatcherOneOrMore1.recognizes(text3);
        StdOut.println("Text 3 check: " + matches3 + " Expected: true");

        String pattern2 = "ABCR+PQR";
        RegularExpressionMatcherOneOrMore regularExpressionMatcherOneOrMore2 =
                oneOrMore.new RegularExpressionMatcherOneOrMore(pattern2);
        String text4 = "ABCRPQR";
        boolean matches4 = regularExpressionMatcherOneOrMore2.recognizes(text4);
        StdOut.println("Text 4 check: " + matches4 + " Expected: true");

        String text5 = "ABCRRRRRRRRRRRRRRPQR";
        boolean matches5 = regularExpressionMatcherOneOrMore2.recognizes(text5);
        StdOut.println("Text 5 check: " + matches5 + " Expected: true");

        String text6 = "ABCPQR";
        boolean matches6 = regularExpressionMatcherOneOrMore2.recognizes(text6);
        StdOut.println("Text 6 check: " + matches6 + " Expected: false");
    }

}
