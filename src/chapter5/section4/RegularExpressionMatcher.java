package chapter5.section4;

import chapter1.section3.Bag;
import chapter1.section3.Stack;
import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import chapter4.section2.Digraph;
import chapter4.section2.DirectedDFS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 17/03/18.
 */
public class RegularExpressionMatcher {

    protected class RangeComplement {
        protected char leftCharacter;
        protected char rightCharacter;

        RangeComplement(char leftCharacter, char rightCharacter) {
            this.leftCharacter = leftCharacter;;
            this.rightCharacter = rightCharacter;
        }
    }

    protected char[] regularExpression;  // Match transitions
    protected Digraph digraph;           // Epsilon transitions
    protected int numberOfStates;

    protected SeparateChainingHashTable<Integer, Integer> setsMatchMap;
    protected SeparateChainingHashTable<Integer, HashSet<Character>> setsComplementMap;
    protected SeparateChainingHashTable<Integer, List<RangeComplement>> setsComplementRangesMap;

    public RegularExpressionMatcher(String regularExpressionString) {
        // Create the nondeterministic finite automaton for the given regular expression
        Stack<Integer> operators = new Stack<>();
        regularExpression = regularExpressionString.toCharArray();
        numberOfStates = regularExpression.length;

        setsMatchMap = new SeparateChainingHashTable<>();
        setsComplementMap = new SeparateChainingHashTable<>();
        setsComplementRangesMap = new SeparateChainingHashTable<>();

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
        boolean isComplementSet = false;
        HashSet<Character> charactersToComplement = null;
        List<RangeComplement> rangesToComplement = null;

        // Handle complement set descriptors
        // If it is a complement operator, put all characters in a set to optimize the recognition later
        if (regularExpression[leftSquareBracket + 1] == '^') {
            isComplementSet = true;
            leftSquareBracket++; // No need to check this character in the next loop
            charactersToComplement = new HashSet<>();
            rangesToComplement = new ArrayList<>();

            for (int indexInsideBrackets = leftSquareBracket + 1; indexInsideBrackets < index; indexInsideBrackets++) {
                if (regularExpression[indexInsideBrackets + 1] == '-') {
                    char leftCharacter = regularExpression[indexInsideBrackets];
                    char rightCharacter = regularExpression[indexInsideBrackets + 2];

                    rangesToComplement.add(new RangeComplement(leftCharacter, rightCharacter));
                    indexInsideBrackets += 2;
                } else {
                    charactersToComplement.add(regularExpression[indexInsideBrackets]);
                }
            }
        }

        // Handle all set-of-character descriptors
        for (int indexInsideBrackets = leftSquareBracket + 1; indexInsideBrackets < index; indexInsideBrackets++) {
            digraph.addEdge(leftSquareBracket, indexInsideBrackets);

            // If a match occurs while checking the characters in this set, the DFA will go to
            // the right square bracket state.
            setsMatchMap.put(indexInsideBrackets, index);

            if (isComplementSet) {
                setsComplementMap.put(indexInsideBrackets, charactersToComplement);
                if (rangesToComplement.size() > 0) {
                    setsComplementRangesMap.put(indexInsideBrackets, rangesToComplement);
                }
            }

            // If it as a range, there is no need to process the next 2 characters
            if (regularExpression[indexInsideBrackets + 1] == '-') {
                indexInsideBrackets += 2;
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
                    if (setsMatchMap.contains(vertex)) {
                        recognizeSet(text, i, vertex, states);
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

    private void recognizeSet(String text, int index, int vertex, Bag<Integer> states) {
        int indexOfRightSquareBracket = setsMatchMap.get(vertex);

        // Is it a range?
        if (regularExpression[vertex + 1] == '-') { // No need to worry about out of bounds indexes
            char leftRangeIndex = regularExpression[vertex];
            char rightRangeIndex = regularExpression[vertex + 2];

            if (leftRangeIndex <= text.charAt(index) && text.charAt(index) <= rightRangeIndex) {
                if (!isCharPartOfComplementSet(text, index, vertex)) {
                    states.add(indexOfRightSquareBracket);
                }
            } else if (setsComplementMap.contains(vertex) && !isCharPartOfComplementSet(text, index, vertex)) {
                states.add(indexOfRightSquareBracket);
            }
        } else if (regularExpression[vertex] == text.charAt(index) || regularExpression[vertex] == '.') {
            if (!isCharPartOfComplementSet(text, index, vertex)) {
                states.add(indexOfRightSquareBracket);
            }
        } else if (setsComplementMap.contains(vertex) && !isCharPartOfComplementSet(text, index, vertex)) {
            states.add(indexOfRightSquareBracket);
        }
    }

    protected boolean isCharPartOfComplementSet(String text, int index, int vertex) {
        // Check complements map
        if (setsComplementMap.contains(vertex)
                && setsComplementMap.get(vertex).contains(text.charAt(index))) {
            return true;
        }

        // Check complement ranges map
        if (setsComplementRangesMap.contains(vertex)) {
            for (RangeComplement rangeComplement : setsComplementRangesMap.get(vertex)) {
                if (rangeComplement.leftCharacter <= text.charAt(index)
                        && text.charAt(index) <= rangeComplement.rightCharacter) {
                    return true;
                }
            }
        }

        return false;
    }

}
