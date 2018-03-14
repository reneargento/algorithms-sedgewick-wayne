package chapter4.section2;

import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 25/10/17.
 */
@SuppressWarnings("unchecked")
public class Exercise34_2Satisfiability {

    public class TwoSATSolver {

        public SeparateChainingHashTable<Character, Boolean> solve2SAT(String formula) {

            // First pass to find the number of variables in the formula
            HashSet<Character> variables = new HashSet<>();
            char[] charsInFormula = formula.toCharArray();
            for(int i = 0; i < charsInFormula.length; i++) {
                if (charsInFormula[i] != '('
                        && charsInFormula[i] != ')'
                        && charsInFormula[i] != 'V'
                        && charsInFormula[i] != '^'
                        && charsInFormula[i] != ' '
                        && charsInFormula[i] != '!') {
                    variables.add(charsInFormula[i]);
                }
            }

            Digraph digraph = new Digraph(variables.size() * 2);

            String[] values = formula.split(" ");

            SeparateChainingHashTable<String, Integer> variableToIdMap = new SeparateChainingHashTable<>();
            SeparateChainingHashTable<Integer, String> idToVariableMap = new SeparateChainingHashTable<>();

            // Second pass to get vertices
            for(int i = 0; i < values.length; i += 2) {
                boolean isVariable1Negation;
                boolean isVariable2Negation;

                // Read variables
                String variable1;
                String variable2;

                String variable1Negation;
                String variable2Negation;

                if (values[i].charAt(1) == '!') {
                    variable1 = values[i].substring(2, 3);
                    isVariable1Negation = true;
                } else {
                    variable1 = String.valueOf(values[i].charAt(1));
                    isVariable1Negation = false;
                }
                variable1Negation = "!" + variable1;

                i += 2;

                if (values[i].charAt(0) == '!') {
                    variable2 = values[i].substring(1, 2);
                    isVariable2Negation = true;
                } else {
                    variable2 = String.valueOf(values[i].charAt(0));
                    isVariable2Negation = false;
                }
                variable2Negation = "!" + variable2;

                // Add variables to mappings if they do not exist yet
                if (!variableToIdMap.contains(variable1)) {
                    addVariableToMappings(variable1, variableToIdMap, idToVariableMap);
                    addVariableToMappings(variable1Negation, variableToIdMap, idToVariableMap);
                }
                if (!variableToIdMap.contains(variable2)) {
                    addVariableToMappings(variable2, variableToIdMap, idToVariableMap);
                    addVariableToMappings(variable2Negation, variableToIdMap, idToVariableMap);
                }

                // Add edges to implication digraph
                // Map (A V B) to (A -> !B) and (B -> !A)
                // based on http://www.geeksforgeeks.org/2-satisfiability-2-sat-problem/
                int variable1Id = variableToIdMap.get(variable1);
                int variable1NegationId = variableToIdMap.get(variable1Negation);
                int variable2Id = variableToIdMap.get(variable2);
                int variable2NegationId = variableToIdMap.get(variable2Negation);

                if (!isVariable1Negation) {
                    if (!isVariable2Negation) {
                        digraph.addEdge(variable1Id, variable2NegationId);
                        digraph.addEdge(variable2Id, variable1NegationId);
                    } else {
                        digraph.addEdge(variable1Id, variable2Id);
                        digraph.addEdge(variable2NegationId, variable1NegationId);
                    }
                } else {
                    if (!isVariable2Negation) {
                        digraph.addEdge(variable1NegationId, variable2NegationId);
                        digraph.addEdge(variable2Id, variable1Id);
                    } else {
                        digraph.addEdge(variable1NegationId, variable2Id);
                        digraph.addEdge(variable2NegationId, variable1Id);
                    }
                }
            }

            // Compute strongly connected components
            KosarajuSharirSCC kosarajuSharirSCC = new KosarajuSharirSCC(digraph);

            // Check if formula is satisfiable
            if (!isFormulaSatisfiable(digraph, kosarajuSharirSCC)) {
                return null;
            }

            // Solve 2-SAT by assigning variables to true using the strongly connected components topological order
            List<Integer>[] stronglyConnectedComponents = (List<Integer>[]) new ArrayList[kosarajuSharirSCC.count()];

            for(int scc = 0; scc < stronglyConnectedComponents.length; scc++) {
                stronglyConnectedComponents[scc] = new ArrayList<>();
            }

            for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
                int stronglyConnectedComponentId = kosarajuSharirSCC.id(vertex);
                stronglyConnectedComponents[stronglyConnectedComponentId].add(vertex);
            }

            SeparateChainingHashTable<Character, Boolean> solution = new SeparateChainingHashTable<>();

            // Iterate through strongly connected components in topological order to assign the variables.
            // There is a lot of conflicting literature as how the variables should be assigned and in which order.
            // The only correct literature that I found was in the Competitive Programmers Handbook, by Antti Laaksonen
            // This implementation uses a reverse approach of the approach described in the book, because it seems
            // more logical to iterate in topological order and assign a value of TRUE to variables X
            // and FALSE to variables !X found along the way.
            for(int scc = stronglyConnectedComponents.length - 1; scc >= 0; scc--) {
                for(int vertexId : stronglyConnectedComponents[scc]) {
                    String vertexVariable = idToVariableMap.get(vertexId);

                    char variable;

                    boolean isNegation = vertexVariable.charAt(0) == '!';
                    if (!isNegation) {
                        variable = vertexVariable.charAt(0);
                    } else {
                        variable = vertexVariable.charAt(1);
                    }

                    if (!solution.contains(variable)) {
                        if (!isNegation) {
                            solution.put(variable, true);
                        } else {
                            solution.put(variable, false);
                        }
                    }
                }
            }

            return solution;
        }

        private void addVariableToMappings(String variable, SeparateChainingHashTable<String, Integer> variableToIdMap,
                                           SeparateChainingHashTable<Integer, String> idToVariableMap) {
            int variableId = variableToIdMap.size();

            variableToIdMap.put(variable, variableId);
            idToVariableMap.put(variableId, variable);
        }

        private boolean isFormulaSatisfiable(Digraph digraph, KosarajuSharirSCC kosarajuSharirSCC) {
            for(int vertex = 0; vertex < digraph.vertices(); vertex += 2) {
                if (kosarajuSharirSCC.stronglyConnected(vertex, vertex + 1)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static void main(String[] args) {
        TwoSATSolver twoSATSolver = new Exercise34_2Satisfiability().new TwoSATSolver();

        String formula1 = "(A V B) ^ (C V !B)";
        StdOut.println("Formula 1: " + formula1);
        SeparateChainingHashTable<Character, Boolean> solution1 = twoSATSolver.solve2SAT(formula1);

        if (solution1 == null) {
            StdOut.print("The formula is not satisfiable");
        } else {
            for(char variable : solution1.keys()) {
                StdOut.print(variable + ": " + solution1.get(variable) + " ");
            }
        }
        StdOut.println("\nExpected: \nA: true B: true C: true");
        StdOut.println("OR A: false B: true C: true");
        StdOut.println("OR A: false B: true C: false");
        StdOut.println("OR A: true B: false C: false");

        String formula2 = "(X V Y) ^ (!X V Y) ^ (X V !Y) ^ (!X V !Y)";
        StdOut.println("\nFormula 2: " + formula2);
        SeparateChainingHashTable<Character, Boolean> solution2 = twoSATSolver.solve2SAT(formula2);

        if (solution2 == null) {
            StdOut.print("The formula is not satisfiable");
        } else {
            for(char variable : solution2.keys()) {
                StdOut.print(variable + ": " + solution2.get(variable) + " ");
            }
        }
        StdOut.println("\nExpected: \nThe formula is not satisfiable");

        String formula3 = "(A V B) ^ (B V C) ^ (C V D) ^ (!A V !C) ^ (!B V !D)";
        StdOut.println("\nFormula 3: " + formula3);
        SeparateChainingHashTable<Character, Boolean> solution3 = twoSATSolver.solve2SAT(formula3);

        if (solution3 == null) {
            StdOut.print("The formula is not satisfiable");
        } else {
            for(char variable : solution3.keys()) {
                StdOut.print(variable + ": " + solution3.get(variable) + " ");
            }
        }
        StdOut.println("\nExpected: \nA: false B: true C: true D: false");

        String formula4 = "(A V B) ^ (!A V B) ^ (B V !A) ^ (!A V !B)";
        StdOut.println("\nFormula 4: " + formula4);
        SeparateChainingHashTable<Character, Boolean> solution4 = twoSATSolver.solve2SAT(formula4);

        if (solution4 == null) {
            StdOut.print("The formula is not satisfiable");
        } else {
            for(char variable : solution4.keys()) {
                StdOut.print(variable + ": " + solution4.get(variable) + " ");
            }
        }
        StdOut.println("\nExpected: \nA: false B: true");

        String formula5 = "(A V D) ^ (!B V E) ^ (C V G) ^ (B V !E) ^ (!H V !B) ^ (C V !A) ^ (D V !C) ^ (E V !D) ^ " +
                "(!C V !G) ^ (F V G) ^ (A V G) ^ (!G V !A)";
        StdOut.println("\nFormula 5: " + formula5);
        SeparateChainingHashTable<Character, Boolean> solution5 = twoSATSolver.solve2SAT(formula5);

        if (solution5 == null) {
            StdOut.print("The formula is not satisfiable");
        } else {
            for(char variable : solution5.keys()) {
                StdOut.print(variable + ": " + solution5.get(variable) + " ");
            }
        }
        StdOut.println("\nExpected: \nA: true B: true C: true D: true E: true F: true G: false H: false");
        StdOut.println("OR A: false B: true C: false D: true E: true F: false G: true H: false");
        StdOut.println("OR A: false B: true C: false D: true E: true F: true G: true H: false");
    }

}
