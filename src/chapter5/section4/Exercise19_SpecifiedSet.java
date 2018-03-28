package chapter5.section4;

import chapter1.section3.Bag;
import chapter1.section3.Stack;
import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import chapter4.section2.Digraph;
import chapter4.section2.DirectedDFS;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/03/18.
 */
public class Exercise19_SpecifiedSet {

    private SeparateChainingHashTable<Integer, Integer> setsMatchMap;

    public class RegularExpressionMatcherSpecifiedSet extends RegularExpressionMatcher {

        public RegularExpressionMatcherSpecifiedSet(String regularExpressionString) {
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
                    HashSet<Integer> orOperatorIndexes = new HashSet<>();

                    while (regularExpression[operators.peek()] == '|') {
                        int or = operators.pop();
                        orOperatorIndexes.add(or);
                    }

                    leftOperator = operators.pop();

                    for (int orOperatorIndex : orOperatorIndexes.keys()) {
                        digraph.addEdge(orOperatorIndex, i);
                        digraph.addEdge(leftOperator, orOperatorIndex + 1);
                    }
                } else if (regularExpression[i] == ']') {
                    leftOperator = operators.pop();

                    for (int indexInsideBrackets = leftOperator + 1; indexInsideBrackets < i; indexInsideBrackets++) {
                        digraph.addEdge(leftOperator, indexInsideBrackets);

                        // If a match occurs while checking the characters in this set, the DFA will go to
                        // the right square bracket state.
                        setsMatchMap.put(indexInsideBrackets, i);
                    }
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

                        if (setsMatchMap.contains(vertex) &&
                                (regularExpression[vertex] == text.charAt(i) || regularExpression[vertex] == '.')) {
                            int indexOfRightSquareBracket = setsMatchMap.get(vertex);
                            states.add(indexOfRightSquareBracket);
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
        Exercise19_SpecifiedSet specifiedSet = new Exercise19_SpecifiedSet();

        String pattern1 = ".*[ABC]Z";
        Exercise19_SpecifiedSet.RegularExpressionMatcherSpecifiedSet regularExpressionMatcherSpecifiedSet1 =
                specifiedSet.new RegularExpressionMatcherSpecifiedSet(pattern1);
        String text1 = "This is a text AZ";
        boolean matches1 = regularExpressionMatcherSpecifiedSet1.recognizes(text1);
        StdOut.println("Text 1 check: " + matches1 + " Expected: true");

        String text2 = "This is a text BZ";
        boolean matches2 = regularExpressionMatcherSpecifiedSet1.recognizes(text2);
        StdOut.println("Text 2 check: " + matches2 + " Expected: true");

        String text3 = "This is a text CZ";
        boolean matches3 = regularExpressionMatcherSpecifiedSet1.recognizes(text3);
        StdOut.println("Text 3 check: " + matches3 + " Expected: true");

        String text4 = "This is a text DZ";
        boolean matches4 = regularExpressionMatcherSpecifiedSet1.recognizes(text4);
        StdOut.println("Text 4 check: " + matches4 + " Expected: false");

        String pattern2 = "R[AEIOU]N";
        Exercise19_SpecifiedSet.RegularExpressionMatcherSpecifiedSet regularExpressionMatcherSpecifiedSet2 =
                specifiedSet.new RegularExpressionMatcherSpecifiedSet(pattern2);
        String text5 = "RAN";
        boolean matches5 = regularExpressionMatcherSpecifiedSet2.recognizes(text5);
        StdOut.println("Text 5 check: " + matches5 + " Expected: true");

        String text6 = "RIN";
        boolean matches6 = regularExpressionMatcherSpecifiedSet2.recognizes(text6);
        StdOut.println("Text 6 check: " + matches6 + " Expected: true");

        String text7 = "RUN";
        boolean matches7 = regularExpressionMatcherSpecifiedSet2.recognizes(text7);
        StdOut.println("Text 7 check: " + matches7 + " Expected: true");

        String text8 = "RAEN";
        boolean matches8 = regularExpressionMatcherSpecifiedSet2.recognizes(text8);
        StdOut.println("Text 8 check: " + matches8 + " Expected: false");

        String text9 = "RAZN";
        boolean matches9 = regularExpressionMatcherSpecifiedSet2.recognizes(text9);
        StdOut.println("Text 9 check: " + matches9 + " Expected: false");

        String pattern3 = "R[AEIOU]*N";
        Exercise19_SpecifiedSet.RegularExpressionMatcherSpecifiedSet regularExpressionMatcherSpecifiedSet3 =
                specifiedSet.new RegularExpressionMatcherSpecifiedSet(pattern3);
        String text10 = "RAEIOUN";
        boolean matches10 = regularExpressionMatcherSpecifiedSet3.recognizes(text10);
        StdOut.println("Text 10 check: " + matches10 + " Expected: true");

        String text11 = "RN";
        boolean matches11 = regularExpressionMatcherSpecifiedSet3.recognizes(text11);
        StdOut.println("Text 11 check: " + matches11 + " Expected: true");

        String pattern4 = "R[AEIOU]+N";
        Exercise19_SpecifiedSet.RegularExpressionMatcherSpecifiedSet regularExpressionMatcherSpecifiedSet4 =
                specifiedSet.new RegularExpressionMatcherSpecifiedSet(pattern4);
        String text12 = "RAEIOUN";
        boolean matches12 = regularExpressionMatcherSpecifiedSet4.recognizes(text12);
        StdOut.println("Text 12 check: " + matches12 + " Expected: true");

        String text13 = "RN";
        boolean matches13 = regularExpressionMatcherSpecifiedSet4.recognizes(text13);
        StdOut.println("Text 13 check: " + matches13 + " Expected: false");

        String pattern5 = "R(0|1|[AEIOU]+Z)+N";
        Exercise19_SpecifiedSet.RegularExpressionMatcherSpecifiedSet regularExpressionMatcherSpecifiedSet5 =
                specifiedSet.new RegularExpressionMatcherSpecifiedSet(pattern5);
        String text14 = "RAEIOUZN";
        boolean matches14 = regularExpressionMatcherSpecifiedSet5.recognizes(text14);
        StdOut.println("Text 14 check: " + matches14 + " Expected: true");

        String text15 = "RAEIOUN";
        boolean matches15 = regularExpressionMatcherSpecifiedSet5.recognizes(text15);
        StdOut.println("Text 15 check: " + matches15 + " Expected: false");

        String text16 = "RUZN";
        boolean matches16 = regularExpressionMatcherSpecifiedSet5.recognizes(text16);
        StdOut.println("Text 16 check: " + matches16 + " Expected: true");

        String text17 = "RUN";
        boolean matches17 = regularExpressionMatcherSpecifiedSet5.recognizes(text17);
        StdOut.println("Text 17 check: " + matches17 + " Expected: false");

        String text18 = "R0N";
        boolean matches18 = regularExpressionMatcherSpecifiedSet5.recognizes(text18);
        StdOut.println("Text 18 check: " + matches18 + " Expected: true");

        String text19 = "R1N";
        boolean matches19 = regularExpressionMatcherSpecifiedSet5.recognizes(text19);
        StdOut.println("Text 19 check: " + matches19 + " Expected: true");
    }

}
