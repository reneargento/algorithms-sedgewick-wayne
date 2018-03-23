package chapter5.section4;

import chapter1.section3.Bag;
import chapter1.section3.Stack;
import chapter4.section2.Digraph;
import chapter4.section2.DirectedDFS;

/**
 * Created by Rene Argento on 17/03/18.
 */
public class RegularExpressionMatcher {

    private char[] regularExpression;  // Match transitions
    private Digraph digraph;           // Epsilon transitions
    private int numberOfStates;

    public RegularExpressionMatcher(String regularExpressionString) {
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
                int or = operators.pop();

                if (regularExpression[or] == '|') {
                    leftParenthesis = operators.pop();
                    digraph.addEdge(leftParenthesis, or + 1);
                    digraph.addEdge(or, i);
                } else {
                    leftParenthesis = or;
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
        }


        for (int vertex : allPossibleStates) {
            if (vertex == numberOfStates) {
                return true;
            }
        }

        return false;
    }

}
