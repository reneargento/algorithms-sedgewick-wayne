package chapter5.section4;

import chapter1.section3.Bag;
import chapter1.section3.Stack;
import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import chapter4.section2.Digraph;
import chapter4.section2.DirectedDFS;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 25/03/18.
 */
public class Exercise20_Range {

    public class RegularExpressionMatcherRange extends RegularExpressionMatcher {

        public RegularExpressionMatcherRange(String regularExpressionString) {
            super(regularExpressionString);

            // Create the nondeterministic finite automaton for the given regular expression
            Stack<Integer> operators = new Stack<>();
            regularExpression = regularExpressionString.toCharArray();
            numberOfStates = regularExpression.length;

            setsMatchMap = new SeparateChainingHashTable<>();

            digraph = new Digraph(numberOfStates + 1);

            for (int i = 0; i < numberOfStates; i++) {
                int leftOperator = i;

                if (regularExpression[i] == '(' || regularExpression[i] == '|' || regularExpression[i] == '[') {
                    operators.push(i);
                } else if (regularExpression[i] == ')') {
                    leftOperator = handleRightParenthesis(operators, i);
                } else if (regularExpression[i] == ']') {
                    leftOperator = operators.pop();
                    handleSets(leftOperator, i);
                }

                if (i < numberOfStates - 1) {
                    if (regularExpression[i + 1] == '*') {
                        digraph.addEdge(leftOperator, i + 1);
                        digraph.addEdge(i + 1, leftOperator);
                    } else if (regularExpression[i + 1] == '+') {
                        digraph.addEdge(i + 1, leftOperator);
                    }
                }

                if (regularExpression[i] == '(' || regularExpression[i] == '*' || regularExpression[i] == ')'
                        || regularExpression[i] == '+' || regularExpression[i] == '[' || regularExpression[i] == ']') {
                    digraph.addEdge(i, i + 1);
                }
            }
        }

        private int handleRightParenthesis(Stack<Integer> operators, int index) {
            HashSet<Integer> orOperatorIndexes = new HashSet<>();

            while (regularExpression[operators.peek()] == '|') {
                int or = operators.pop();
                orOperatorIndexes.add(or);
            }

            int leftOperator = operators.pop();

            for (int orOperatorIndex : orOperatorIndexes.keys()) {
                digraph.addEdge(orOperatorIndex, index);
                digraph.addEdge(leftOperator, orOperatorIndex + 1);
            }

            return leftOperator;
        }

        private void handleSets(int leftSquareBracket, int index) {
            for (int indexInsideBrackets = leftSquareBracket + 1; indexInsideBrackets < index; indexInsideBrackets++) {
                digraph.addEdge(leftSquareBracket, indexInsideBrackets);

                // If a match occurs while checking the characters in this set, the DFA will go to
                // the right square bracket state.
                setsMatchMap.put(indexInsideBrackets, index);

                // If it as a range, there is no need to process the next 2 characters
                if (regularExpression[indexInsideBrackets + 1] == '-') {
                    indexInsideBrackets += 2;
                }
            }
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

                        if (setsMatchMap.contains(vertex)) {
                            // Is it a range?
                            if (regularExpression[vertex + 1] == '-') { // No need to worry about out of bounds indexes
                                int leftRangeIndex = regularExpression[vertex];
                                int rightRangeIndex = regularExpression[vertex + 2];

                                if (leftRangeIndex <= text.charAt(i) && text.charAt(i) <= rightRangeIndex) {
                                    int indexOfRightSquareBracket = setsMatchMap.get(vertex);
                                    states.add(indexOfRightSquareBracket);
                                }
                            } else if (regularExpression[vertex] == text.charAt(i) || regularExpression[vertex] == '.') {
                                int indexOfRightSquareBracket = setsMatchMap.get(vertex);
                                states.add(indexOfRightSquareBracket);
                            }
                        } else if (regularExpression[vertex] == text.charAt(i) || regularExpression[vertex] == '.') {
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
        Exercise20_Range range = new Exercise20_Range();

        String pattern1 = "RENE[A-Z]";
        Exercise20_Range.RegularExpressionMatcherRange regularExpressionMatcherRange1 =
                range.new RegularExpressionMatcherRange(pattern1);
        String text1 = "RENEA";
        boolean matches1 = regularExpressionMatcherRange1.recognizes(text1);
        StdOut.println("Text 1 check: " + matches1 + " Expected: true");

        String text2 = "RENER";
        boolean matches2 = regularExpressionMatcherRange1.recognizes(text2);
        StdOut.println("Text 2 check: " + matches2 + " Expected: true");

        String text3 = "RENEZ";
        boolean matches3 = regularExpressionMatcherRange1.recognizes(text3);
        StdOut.println("Text 3 check: " + matches3 + " Expected: true");

        String text4 = "RENEa";
        boolean matches4 = regularExpressionMatcherRange1.recognizes(text4);
        StdOut.println("Text 4 check: " + matches4 + " Expected: false");

        String text5 = "RENEb";
        boolean matches5 = regularExpressionMatcherRange1.recognizes(text5);
        StdOut.println("Text 5 check: " + matches5 + " Expected: false");

        String pattern2 = "[A-j]*RENE[0-8]+";
        Exercise20_Range.RegularExpressionMatcherRange regularExpressionMatcherRange2 =
                range.new RegularExpressionMatcherRange(pattern2);
        String text6 = "AbjRENE012345678";
        boolean matches6 = regularExpressionMatcherRange2.recognizes(text6);
        StdOut.println("Text 6 check: " + matches6 + " Expected: true");

        String text7 = "AbkRENE012345678";
        boolean matches7 = regularExpressionMatcherRange2.recognizes(text7);
        StdOut.println("Text 7 check: " + matches7 + " Expected: false");

        String text8 = "AbjRENE0123456789";
        boolean matches8 = regularExpressionMatcherRange2.recognizes(text8);
        StdOut.println("Text 8 check: " + matches8 + " Expected: false");

        String pattern3 = "[a-kA-K0-5]+";
        Exercise20_Range.RegularExpressionMatcherRange regularExpressionMatcherRange3 =
                range.new RegularExpressionMatcherRange(pattern3);
        String text9 = "aB234";
        boolean matches9 = regularExpressionMatcherRange3.recognizes(text9);
        StdOut.println("Text 9 check: " + matches9 + " Expected: true");

        String text10 = "mB2";
        boolean matches10 = regularExpressionMatcherRange3.recognizes(text10);
        StdOut.println("Text 10 check: " + matches10 + " Expected: false");

        String text11 = "aM234";
        boolean matches11 = regularExpressionMatcherRange3.recognizes(text11);
        StdOut.println("Text 11 check: " + matches11 + " Expected: false");

        String text12 = "aB236";
        boolean matches12 = regularExpressionMatcherRange3.recognizes(text12);
        StdOut.println("Text 12 check: " + matches12 + " Expected: false");

        String pattern4 = "([a-kA-K0-5]+ABC)*";
        Exercise20_Range.RegularExpressionMatcherRange regularExpressionMatcherRange4 =
                range.new RegularExpressionMatcherRange(pattern4);
        String text13 = "0ABC";
        boolean matches13 = regularExpressionMatcherRange4.recognizes(text13);
        StdOut.println("Text 13 check: " + matches13 + " Expected: true");

        String text14 = "0aAABC";
        boolean matches14 = regularExpressionMatcherRange4.recognizes(text14);
        StdOut.println("Text 14 check: " + matches14 + " Expected: true");

        String pattern5 = "[A-Zabc]+";
        Exercise20_Range.RegularExpressionMatcherRange regularExpressionMatcherRange5 =
                range.new RegularExpressionMatcherRange(pattern5);
        String text15 = "abbbbbcABCDEFZ";
        boolean matches15 = regularExpressionMatcherRange5.recognizes(text15);
        StdOut.println("Text 15 check: " + matches15 + " Expected: true");

        String text16 = "abcdA";
        boolean matches16 = regularExpressionMatcherRange5.recognizes(text16);
        StdOut.println("Text 16 check: " + matches16 + " Expected: false");
    }

}
