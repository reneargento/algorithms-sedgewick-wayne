package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 21/10/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for suggesting a more efficient solution:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/142
public class Exercise9 {

    private class CheckTopologicalOrder {

        public boolean isTopologicalOrder(Digraph digraph, List<Integer> topologicalOrder) {
            DirectedCycle directedCycle = new DirectedCycle(digraph);
            if (directedCycle.hasCycle()) {
                throw new IllegalArgumentException("Digraph is not a DAG");
            }

            if (topologicalOrder.size() != digraph.vertices()) {
                return false;
            }

            boolean[] visited = new boolean[digraph.vertices()];
            for (int i = topologicalOrder.size() - 1; i >= 0; i--) {
                int vertex = topologicalOrder.get(i);
                if (visited[vertex]) {
                    return false;
                }

                visited[vertex] = true;

                for (int neighbor : digraph.adjacent(vertex)) {
                    if (!visited[neighbor]) {
                        return false;
                    }
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
        boolean isTopologicalOrder1 = checkTopologicalOrder.isTopologicalOrder(digraph1, topologicalOrder1);

        StdOut.println("Is topological order: " + isTopologicalOrder1 + " Expected: true");

        List<Integer> topologicalOrder2 = new ArrayList<>();
        topologicalOrder2.add(1);
        topologicalOrder2.add(0);
        topologicalOrder2.add(2);
        boolean isTopologicalOrder2 = checkTopologicalOrder.isTopologicalOrder(digraph1, topologicalOrder2);

        StdOut.println("Is topological order: " + isTopologicalOrder2 + " Expected: false");

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
        boolean isTopologicalOrder3 = checkTopologicalOrder.isTopologicalOrder(digraph2, topologicalOrder3);

        StdOut.println("Is topological order: " + isTopologicalOrder3 + " Expected: true");
    }

}
