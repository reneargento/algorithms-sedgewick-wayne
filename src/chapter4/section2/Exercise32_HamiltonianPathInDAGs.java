package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 25/10/17.
 */
public class Exercise32_HamiltonianPathInDAGs {

    // A DAG has a Hamiltonian path if and only if there is a directed edge between each pair of consecutive vertices
    // in its topological order
    public boolean hasHamiltonianPath(Digraph digraph) {
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
        Exercise32_HamiltonianPathInDAGs hamiltonianPathInDAGs = new Exercise32_HamiltonianPathInDAGs();

        Digraph digraph1 = new Digraph(5);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(0, 2);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(2, 3);
        digraph1.addEdge(3, 4);
        StdOut.println("Has Hamiltonian path: " + hamiltonianPathInDAGs.hasHamiltonianPath(digraph1) + " Expected: true");

        Digraph digraph2 = new Digraph(6);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(1, 2);
        digraph2.addEdge(3, 4);
        digraph2.addEdge(4, 5);
        StdOut.println("Has Hamiltonian path: " + hamiltonianPathInDAGs.hasHamiltonianPath(digraph2) + " Expected: false");

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
        StdOut.println("Has Hamiltonian path: " + hamiltonianPathInDAGs.hasHamiltonianPath(digraph3) + " Expected: false");

        Digraph digraph4 = new Digraph(5);
        digraph4.addEdge(0, 2);
        digraph4.addEdge(1, 2);
        digraph4.addEdge(1, 3);
        digraph4.addEdge(2, 4);
        digraph4.addEdge(3, 4);
        StdOut.println("Has Hamiltonian path: " + hamiltonianPathInDAGs.hasHamiltonianPath(digraph4) + " Expected: false");
    }

}
