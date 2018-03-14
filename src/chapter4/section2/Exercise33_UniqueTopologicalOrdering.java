package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 25/10/17.
 */
public class Exercise33_UniqueTopologicalOrdering {

    // A DAG has a unique topological ordering if and only if there is a directed edge between each pair of consecutive
    // vertices in its topological order (i.e, the digraph has a Hamiltonian path)
    // If the DAG has multiple topological orderings, then a second topological order can be obtained by swapping
    // any pair of consecutive and nonadjacent vertices
    public boolean hasUniqueTopologicalOrdering(Digraph digraph) {
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("Digraph is not a DAG");
        }

        Topological topological = new Topological(digraph);
        int[] topologicalOrder = new int[digraph.vertices()];
        int arrayIndex = 0;

        for(int vertex : topological.order()) {
            topologicalOrder[arrayIndex++] = vertex;
        }

        for(int i = 0; i < topologicalOrder.length - 1; i++) {
            boolean hasEdgeToNextVertex = false;

            for(int neighbor : digraph.adjacent(topologicalOrder[i])) {
                if (neighbor == topologicalOrder[i + 1]) {
                    hasEdgeToNextVertex = true;
                    break;
                }
            }

            if (!hasEdgeToNextVertex) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        Exercise33_UniqueTopologicalOrdering uniqueTopologicalOrdering = new Exercise33_UniqueTopologicalOrdering();

        Digraph digraph1 = new Digraph(5);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(0, 2);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(2, 3);
        digraph1.addEdge(3, 4);
        StdOut.println("Has unique topological ordering: " +
                uniqueTopologicalOrdering.hasUniqueTopologicalOrdering(digraph1) + " Expected: true");

        Digraph digraph2 = new Digraph(6);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(1, 2);
        digraph2.addEdge(3, 4);
        digraph2.addEdge(4, 5);
        StdOut.println("Has unique topological ordering: " +
                uniqueTopologicalOrdering.hasUniqueTopologicalOrdering(digraph2) + " Expected: false");

        Digraph digraph3 = new Digraph(9);
        digraph3.addEdge(0, 1);
        digraph3.addEdge(1, 2);
        digraph3.addEdge(1, 3);

        digraph3.addEdge(4, 5);
        digraph3.addEdge(5, 6);
        digraph3.addEdge(6, 8);
        digraph3.addEdge(6, 7);
        digraph3.addEdge(7, 2);
        digraph3.addEdge(8, 3);
        StdOut.println("Has unique topological ordering: " +
                uniqueTopologicalOrdering.hasUniqueTopologicalOrdering(digraph3) + " Expected: false");

        Digraph digraph4 = new Digraph(5);
        digraph4.addEdge(0, 2);
        digraph4.addEdge(1, 2);
        digraph4.addEdge(1, 3);
        digraph4.addEdge(2, 4);
        digraph4.addEdge(3, 4);
        StdOut.println("Has unique topological ordering: " +
                uniqueTopologicalOrdering.hasUniqueTopologicalOrdering(digraph4) + " Expected: false");
    }

}
