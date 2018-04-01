package chapter5.section4;

import chapter1.section3.Bag;
import chapter1.section3.Stack;
import chapter4.section2.Digraph;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 25/03/18.
 */
@SuppressWarnings("unchecked")
public class Exercise22_Proof {

    private class State {
        private int id;
        private State previous;

        State(int id, State previous) {
            this.id = id;
            this.previous = previous;
        }
    }

    public class RegularExpressionMatcherWithProof extends RegularExpressionMatcher {

        public RegularExpressionMatcherWithProof(String regularExpression) {
            super(regularExpression);
        }

        @Override
        public boolean recognizes(String text) {
            Bag<State> allPossibleStates = new Bag<>();
            State sourceState = new State(0, null);
            DirectedDFS directedDFS = new DirectedDFS(digraph, sourceState);

            for (State newSate : directedDFS.getNewStates()) {
                allPossibleStates.add(newSate);
            }

            for (int i = 0; i < text.length(); i++) {
                // Compute possible NFA states for text[i + 1]
                Bag<State> states = new Bag<>();

                for (State state : allPossibleStates) {
                    if (state.id < numberOfStates) {

                        if (setsMatchMap.contains(state.id)) {
                            recognizeSet(text, i, state, states);
                        } else if (regularExpression[state.id] == text.charAt(i)
                                || regularExpression[state.id] == '.') {
                            addNextState(states, state, state.id + 1);
                        }
                    }
                }

                allPossibleStates = new Bag<>();
                directedDFS = new DirectedDFS(digraph, states);

                if (directedDFS.getNewStates() != null) {
                    for (State newSate : directedDFS.getNewStates()) {
                        allPossibleStates.add(newSate);
                    }
                } else {
                    // Optimization if no states are reachable
                    StdOut.println("Text was not recognized by the DFA");
                    return false;
                }
            }

            for (State state : allPossibleStates) {
                if (state.id == numberOfStates) {
                    printProof(state);
                    return true;
                }
            }

            StdOut.println("Text was not recognized by the DFA");
            return false;
        }

        private void recognizeSet(String text, int index, State state, Bag<State> states) {
            int indexOfRightSquareBracket = setsMatchMap.get(state.id);

            // Is it a range?
            if (regularExpression[state.id + 1] == '-') { // No need to worry about out of bounds indexes
                char leftRangeIndex = regularExpression[state.id];
                char rightRangeIndex = regularExpression[state.id + 2];

                if (leftRangeIndex <= text.charAt(index) && text.charAt(index) <= rightRangeIndex) {
                    if (!isCharPartOfComplementSet(text, index, state.id)) {
                        addNextState(states, state, indexOfRightSquareBracket);
                    }
                } else if (setsComplementMap.contains(state.id)
                        && !isCharPartOfComplementSet(text, index, state.id)) {
                    addNextState(states, state, indexOfRightSquareBracket);
                }
            } else if (regularExpression[state.id] == text.charAt(index) || regularExpression[state.id] == '.') {
                if (!isCharPartOfComplementSet(text, index, state.id)) {
                    addNextState(states, state, indexOfRightSquareBracket);
                }
            } else if (setsComplementMap.contains(state.id) && !isCharPartOfComplementSet(text, index, state.id)) {
                addNextState(states, state, indexOfRightSquareBracket);
            }
        }

        private void addNextState(Bag<State> states, State currentState, int nextStateId) {
            states.add(new State(nextStateId, currentState));
        }

        private void printProof(State state) {
            Stack<State> states = new Stack<>();

            states.push(state);
            while (state.previous != null) {
                states.push(state.previous);
                state = state.previous;
            }

            StringJoiner proof = new StringJoiner(" -> ");
            while (!states.isEmpty()) {
                proof.add(String.valueOf(states.pop().id));
            }

            StdOut.println(proof);
        }
    }

    public class DirectedDFS {

        private boolean[] visited;
        private Bag<State> newStates;

        public DirectedDFS(Digraph digraph, State source) {
            visited = new boolean[digraph.vertices()];
            newStates = new Bag<>();

            dfs(digraph, source);
        }

        public DirectedDFS(Digraph digraph, Iterable<State> sources) {
            visited = new boolean[digraph.vertices()];
            newStates = new Bag<>();

            for(State source : sources) {
                if (!visited[source.id]) {
                    dfs(digraph, source);
                }
            }
        }

        private void dfs(Digraph digraph, State source) {
            visited[source.id] = true;
            newStates.add(source);

            for(int neighbor : digraph.adjacent(source.id)) {
                if (!visited[neighbor]) {
                    State newState = new State(neighbor, source);
                    newStates.add(newState);

                    dfs(digraph, newState);
                }
            }
        }

        public boolean marked(int vertex) {
            return visited[vertex];
        }

        public Bag<State> getNewStates() {
            return newStates;
        }
    }

    public static void main(String[] args) {
        Exercise22_Proof proof = new Exercise22_Proof();

        String pattern1 = "RENE[^ABC]";
        Exercise22_Proof.RegularExpressionMatcherWithProof regularExpressionMatcherWithProof1 =
                proof.new RegularExpressionMatcherWithProof(pattern1);
        String text1 = "RENED";
        StdOut.print("Proof 1: ");
        regularExpressionMatcherWithProof1.recognizes(text1);
        StdOut.println("Expected: 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 9 -> 10");

        String pattern2 = "A[^A-Z0]+Z";
        Exercise22_Proof.RegularExpressionMatcherWithProof regularExpressionMatcherWithProof2 =
                proof.new RegularExpressionMatcherWithProof(pattern2);
        String text2 = "AbZ";
        StdOut.print("\nProof 2: ");
        regularExpressionMatcherWithProof2.recognizes(text2);
        StdOut.println("Expected: 0 -> 1 -> 2 -> 3 -> 7 -> 8 -> 9 -> 10");

        String text3 = "AabcdeZ";
        StdOut.print("\nProof 3: ");
        regularExpressionMatcherWithProof2.recognizes(text3);
        StdOut.println("Expected: 0 -> 1 -> 2 -> 3 -> 7 -> 8 -> 1 -> 2 -> 3 -> 7 -> 8 " +
                "-> 1 -> 2 -> 3 -> 7 -> 8 -> 1 -> 2 -> 3 -> 7 -> 8 -> 1 -> 2 -> 3 -> 7 -> 8 -> 9 -> 10");

        String pattern3 = "A[^A-Z0]+ZZ";
        Exercise22_Proof.RegularExpressionMatcherWithProof regularExpressionMatcherWithProof3 =
                proof.new RegularExpressionMatcherWithProof(pattern3);
        String text4 = "AbcZZ";
        StdOut.print("\nProof 4: ");
        regularExpressionMatcherWithProof3.recognizes(text4);
        StdOut.println("Expected: 0 -> 1 -> 2 -> 3 -> 7 -> 8 -> 1 -> 2 -> 3 -> 7 -> 8 -> 9 -> 10 -> 11");

        String pattern4 = "A[^A-Z0]*ZZ";
        Exercise22_Proof.RegularExpressionMatcherWithProof regularExpressionMatcherWithProof4 =
                proof.new RegularExpressionMatcherWithProof(pattern4);
        String text5 = "AZZ";
        StdOut.print("\nProof 5: ");
        regularExpressionMatcherWithProof4.recognizes(text5);
        StdOut.println("Expected: 0 -> 1 -> 8 -> 9 -> 10 -> 11");

        String text6 = "Abcdef123ZZ";
        StdOut.print("\nProof 6: ");
        regularExpressionMatcherWithProof4.recognizes(text6);
        StdOut.println("Expected: 0 -> 1 -> 2 -> 3 -> 7 -> 8 " +
                "-> 1 -> 2 -> 3 -> 7 -> 8 " +
                "-> 1 -> 2 -> 3 -> 7 -> 8 " +
                "-> 1 -> 2 -> 3 -> 7 -> 8 " +
                "-> 1 -> 2 -> 3 -> 7 -> 8 " +
                "-> 1 -> 2 -> 3 -> 7 -> 8 " +
                "-> 1 -> 2 -> 3 -> 7 -> 8 " +
                "-> 1 -> 2 -> 3 -> 7 -> 8 " +
                "-> 9 -> 10 -> 11");

        String pattern5 = "A([^A-Z0]|[^a-f])+[^a-f]Z";
        Exercise22_Proof.RegularExpressionMatcherWithProof regularExpressionMatcherWithProof5 =
                proof.new RegularExpressionMatcherWithProof(pattern5);
        String text7 = "ABgZ";
        StdOut.print("\nProof 7: ");
        regularExpressionMatcherWithProof5.recognizes(text7);
        StdOut.println("Expected: 0 -> 1 -> 10 -> 11 -> 12 -> 15 -> 16 -> 17 -> 18 -> 19 -> 20 -> 23 -> 24 -> 25");

        String text8 = "ABCDEFGagZ";
        StdOut.print("\nProof 8: ");
        regularExpressionMatcherWithProof5.recognizes(text8);
        StdOut.println("Expected: 0 -> 1 -> 10 -> 11 -> 12 -> 15 -> 16 -> 17 -> " +
                "1 -> 10 -> 11 -> 12 -> 15 -> 16 -> 17 -> " +
                "1 -> 10 -> 11 -> 12 -> 15 -> 16 -> 17 -> " +
                "1 -> 10 -> 11 -> 12 -> 15 -> 16 -> 17 -> " +
                "1 -> 10 -> 11 -> 12 -> 15 -> 16 -> 17 -> " +
                "1 -> 10 -> 11 -> 12 -> 15 -> 16 -> 17 -> " +
                "1 -> 2 -> 3 -> 4 -> 8 -> 9 -> 16 -> 17 -> " +
                "18 -> 19 -> 20 -> 23 -> 24 -> 25");

        String text9 = "ABZ";
        StdOut.print("\nProof 9: ");
        regularExpressionMatcherWithProof5.recognizes(text9);
        StdOut.println("Expected: Text was not recognized by the DFA");
    }

}
