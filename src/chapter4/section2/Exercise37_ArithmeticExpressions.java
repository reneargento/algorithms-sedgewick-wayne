package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 26/10/17.
 */
public class Exercise37_ArithmeticExpressions {

    public double evaluateDAG(Digraph digraph, String[] values) {
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("Digraph is not a DAG");
        }

        return dfs(digraph, 0, values);
    }

    private double dfs(Digraph digraph, int sourceVertex, String[] values) {
        // the values array works as a visited array
        if (!values[sourceVertex].equals("+")
                && !values[sourceVertex].equals("-")
                && !values[sourceVertex].equals("*")
                && !values[sourceVertex].equals("/")) {
            return Double.parseDouble(values[sourceVertex]);
        }

        double result = Double.MIN_VALUE;

        for(int neighbor : digraph.adjacent(sourceVertex)) {
            if (values[sourceVertex].equals("+")) {
                if (result == Double.MIN_VALUE) {
                    result = 0;
                }

                result += dfs(digraph, neighbor, values);
            } else if (values[sourceVertex].equals("-")) {
                if (result == Double.MIN_VALUE) {
                    result = 0;
                }

                result -= dfs(digraph, neighbor, values);
            } else if (values[sourceVertex].equals("*")) {
                if (result == Double.MIN_VALUE) {
                    result = 1;
                }

                result *= dfs(digraph, neighbor, values);
            } else if (values[sourceVertex].equals("/")) {
                if (result == Double.MIN_VALUE) {
                    result = dfs(digraph, neighbor, values);
                } else {
                    result /= dfs(digraph, neighbor, values);
                }
            }
        }

        values[sourceVertex] = String.valueOf(result);
        return result;
    }

    public static void main(String[] args) {
        Exercise37_ArithmeticExpressions arithmeticExpressions = new Exercise37_ArithmeticExpressions();

        /**
         * Digraph 1:
         * 2 + (3 * 4) + (3 * 4) / 5
         *
         *      +
         *     / \
         *    v  v
         *   +   /(DIV)
         *   /\  / \
         *  v v  v v
         *  2  *   5
         *     /\
         *    v v
         *    3 4
         */

        // Right children should be added before the left children to maintain the expression order
        Digraph digraph1 = new Digraph(8);
        digraph1.addEdge(0, 2);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(1, 4);
        digraph1.addEdge(1, 3);
        digraph1.addEdge(4, 7);
        digraph1.addEdge(4, 6);
        digraph1.addEdge(2, 5);
        digraph1.addEdge(2, 4);

        String[] values1 = {"+", "+", "/", "2", "*", "5", "3", "4"};
        StdOut.println("2 + (3 * 4) + (3 * 4) / 5 = " + arithmeticExpressions.evaluateDAG(digraph1, values1));
        StdOut.println("Expected: 16.4");

        /**
         * Digraph 2:
         * (3 * 4) + (3 * 4) + (3 * 4) + (3 * 4) / 6
         *
         *         /(DIV)
         *       /   \
         *      v     v
         *      +     6
         *    /\ / \
         *   v v v v
         *     *
         *     /\
         *    v v
         *    3 4
         */

        Digraph digraph2 = new Digraph(6);
        digraph2.addEdge(0, 2);
        digraph2.addEdge(0, 1);

        digraph2.addEdge(1, 3);
        digraph2.addEdge(1, 3);
        digraph2.addEdge(1, 3);
        digraph2.addEdge(1, 3);
        digraph2.addEdge(3, 5);
        digraph2.addEdge(3, 4);

        String[] values2 = {"/", "+", "6", "*", "3", "4"};
        StdOut.println("(3 * 4) + (3 * 4) + (3 * 4) + (3 * 4) / 6 = " + arithmeticExpressions.evaluateDAG(digraph2, values2));
        StdOut.println("Expected: 8.0");
    }

}
