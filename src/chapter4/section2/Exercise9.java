package chapter4.section2;

import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 21/10/17.
 */
//Based on https://stackoverflow.com/questions/36381100/verify-that-given-list-of-nodes-of-a-graph-is-a-correct-topological-order
public class Exercise9 {

    private class CheckTopologicalOrder {

        public boolean isTopologicalOrder(Digraph digraph, List<Integer> topologicalOrder) {

            DirectedCycle directedCycle = new DirectedCycle(digraph);
            if(directedCycle.hasCycle()) {
                throw new IllegalArgumentException("Digraph is not a DAG");
            }

            HashSet<Integer> visitedVertices = new HashSet<>();
            for(int vertex : topologicalOrder) {
                visitedVertices.add(vertex);

                if(!dfs(vertex, digraph, visitedVertices)) {
                    return false;
                }
            }

            return true;
        }

        // Check if any reachable neighbor from vertex has already been visited
        // If it has, this is not a valid topological sort
        private boolean dfs(int vertex, Digraph digraph, HashSet<Integer> visitedVertices) {

            for(int neighbor : digraph.adjacent(vertex)) {
                if(visitedVertices.contains(neighbor)) {
                    return false;
                }

                boolean isValid = dfs(neighbor, digraph, visitedVertices);
                if(!isValid) {
                    return false;
                }
            }

            return true;
        }
    }

    public static void main(String[] args) {
        CheckTopologicalOrder checkTopologicalOrder = new Exercise9().new CheckTopologicalOrder();

        Digraph digraph1 = new Digraph(3);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(0, 2);
        digraph1.addEdge(1, 2);

        List<Integer> topologicalOrder1 = new ArrayList<>();
        topologicalOrder1.add(0);
        topologicalOrder1.add(1);
        topologicalOrder1.add(2);

        StdOut.println(checkTopologicalOrder.isTopologicalOrder(digraph1, topologicalOrder1) + " Expected: true");

        List<Integer> topologicalOrder2 = new ArrayList<>();
        topologicalOrder2.add(1);
        topologicalOrder2.add(0);
        topologicalOrder2.add(2);

        StdOut.println(checkTopologicalOrder.isTopologicalOrder(digraph1, topologicalOrder2) + " Expected: false");

        Digraph digraph2 = new Digraph(6);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(1, 2);
        digraph2.addEdge(2, 3);
        digraph2.addEdge(4, 5);

        List<Integer> topologicalOrder3 = new ArrayList<>();
        topologicalOrder3.add(0);
        topologicalOrder3.add(4);
        topologicalOrder3.add(1);
        topologicalOrder3.add(2);
        topologicalOrder3.add(5);
        topologicalOrder3.add(3);

        StdOut.println(checkTopologicalOrder.isTopologicalOrder(digraph2, topologicalOrder3) + " Expected: true");
    }

}
