package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 28/10/17.
 */
public class Exercise40_ShortestDirectedCycle {

    // O(V * (V + E)) -> O(V * E)
    public List<Integer> getShortestDirectedCycle(Digraph digraph) {

        int shortestCycleLength = Integer.MAX_VALUE;
        List<Integer> cycle = null;

        Digraph reverseDigraph = digraph.reverse();

        // For each edge u->v check if v has a path to u
        // If there is a path, it is part of a cycle. Then, check if it has the shortest length
        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            BreadthFirstDirectedPaths breadthFirstDirectedPaths = new BreadthFirstDirectedPaths(digraph, vertex);

            for(int neighbor : reverseDigraph.adjacent(vertex)) {
                if (breadthFirstDirectedPaths.hasPathTo(neighbor)) {
                    int cycleLength = breadthFirstDirectedPaths.distTo(neighbor) + 1;

                    if (cycleLength < shortestCycleLength) {
                        shortestCycleLength = cycleLength;
                        cycle = new ArrayList<>();

                        for(int vertexInCycle : breadthFirstDirectedPaths.pathTo(neighbor)) {
                            cycle.add(vertexInCycle);
                        }
                        cycle.add(vertex);
                    }
                }
            }
        }

        if (cycle == null) {
            //There is no cycle in the graph
            return null;
        }

        return cycle;
    }

    public static void main(String[] args) {
        Exercise40_ShortestDirectedCycle shortestDirectedCycle = new Exercise40_ShortestDirectedCycle();

        Digraph digraph1 = new Digraph(5);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(2, 3);
        digraph1.addEdge(3, 4);
        digraph1.addEdge(4, 0);

        List<Integer> directedCycle1 = shortestDirectedCycle.getShortestDirectedCycle(digraph1);

        if (directedCycle1 != null) {
            StdOut.println("Shortest cycle 1 length: " + (directedCycle1.size() - 1));
            for (int vertex : directedCycle1) {
                StdOut.print(vertex + " ");
            }
        } else {
            StdOut.println("The digraph is a DAG");
        }
        StdOut.println("\nExpected: 0 1 2 3 4 0\n");

        Digraph digraph2 = new Digraph(8);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(1, 2);
        digraph2.addEdge(2, 3);
        digraph2.addEdge(3, 4);
        digraph2.addEdge(4, 0);
        digraph2.addEdge(5, 6);
        digraph2.addEdge(6, 7);
        digraph2.addEdge(7, 5);

        List<Integer> directedCycle2 = shortestDirectedCycle.getShortestDirectedCycle(digraph2);

        if (directedCycle2 != null) {
            StdOut.println("Shortest cycle 2 length: " + (directedCycle2.size() - 1));
            for (int vertex : directedCycle2) {
                StdOut.print(vertex + " ");
            }
        } else {
            StdOut.println("The digraph is a DAG");
        }
        StdOut.println("\nExpected: 5 6 7 5\n");

        Digraph digraph3 = new Digraph(8);
        digraph3.addEdge(0, 1);
        digraph3.addEdge(1, 2);
        digraph3.addEdge(2, 3);
        digraph3.addEdge(3, 4);
        digraph3.addEdge(4, 3);
        digraph3.addEdge(4, 0);
        digraph3.addEdge(5, 6);
        digraph3.addEdge(6, 7);
        digraph3.addEdge(7, 5);

        List<Integer> directedCycle3 = shortestDirectedCycle.getShortestDirectedCycle(digraph3);

        if (directedCycle3 != null) {
            StdOut.println("Shortest cycle 3 length: " + (directedCycle3.size() - 1));
            for (int vertex : directedCycle3) {
                StdOut.print(vertex + " ");
            }
        } else {
            StdOut.println("The digraph is a DAG");
        }
        StdOut.println("\nExpected: 3 4 3\n");

        Digraph digraph4 = new Digraph(5);
        digraph4.addEdge(0, 1);
        digraph4.addEdge(1, 2);
        digraph4.addEdge(3, 4);
        digraph4.addEdge(4, 0);

        List<Integer> directedCycle4 = shortestDirectedCycle.getShortestDirectedCycle(digraph4);

        if (directedCycle4 != null) {
            StdOut.println("Shortest cycle 4 length: " + (directedCycle4.size() - 1));
            for(int vertex : directedCycle4) {
                StdOut.print(vertex + " ");
            }
        } else {
            StdOut.println("The digraph is a DAG");
        }
        StdOut.println("Expected: The digraph is a DAG");
    }

}
