package chapter4.section2;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 27/10/17.
 */
public class Exercise39_QueueBasedTopologicalSort {

    private int[] ranks;

    // O(V + E)
    public int[] topologicalSort(Digraph digraph) {

        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            // Digraph is not a DAG so no topological order exists
            return null;
        }

        int[] indegree = new int[digraph.vertices()];
        Queue<Integer> sources = new Queue<>();

        ranks = new int[digraph.vertices()];

        int[] topologicalSort = new int[digraph.vertices()];
        int topologicalSortIndex = 0;

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            for(int neighbor : digraph.adjacent(vertex)) {
                indegree[neighbor]++;
            }
        }

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            if (indegree[vertex] == 0) {
                sources.enqueue(vertex);
            }
        }

        while (!sources.isEmpty()) {
            int currentSource = sources.dequeue();

            ranks[currentSource] = topologicalSortIndex;
            topologicalSort[topologicalSortIndex++] = currentSource;

            for(int neighbor : digraph.adjacent(currentSource)) {
                indegree[neighbor]--;

                if (indegree[neighbor] == 0) {
                    sources.enqueue(neighbor);
                }
            }
        }

        return topologicalSort;
    }

    public static void main(String[] args) {
        Exercise39_QueueBasedTopologicalSort queueBasedTopologicalSort = new Exercise39_QueueBasedTopologicalSort();

        Digraph digraph1 = new Digraph(5);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(0, 2);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(2, 3);
        digraph1.addEdge(3, 4);

        int[] topologicalOrder1 = queueBasedTopologicalSort.topologicalSort(digraph1);

        StdOut.println("Topological order 1: ");

        for(int vertex : topologicalOrder1) {
            StdOut.print(vertex + " ");
        }
        StdOut.println("\nExpected: 0 1 2 3 4");

        Digraph digraph2 = new Digraph(6);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(1, 2);
        digraph2.addEdge(3, 4);
        digraph2.addEdge(4, 5);

        int[] topologicalOrder2 = queueBasedTopologicalSort.topologicalSort(digraph2);

        StdOut.println("\nTopological order 2: ");

        for(int vertex : topologicalOrder2) {
            StdOut.print(vertex + " ");
        }
        StdOut.println("\nExpected: 0 3 1 4 2 5 ");

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

        int[] topologicalOrder3 = queueBasedTopologicalSort.topologicalSort(digraph3);

        StdOut.println("\nTopological order 3: ");

        for(int vertex : topologicalOrder3) {
            StdOut.print(vertex + " ");
        }
        StdOut.println("\nExpected: 0 4 1 5 6 7 8 2 3");
    }

}
