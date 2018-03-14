package chapter4.section4;

import chapter1.section3.Queue;
import chapter1.section3.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 10/12/17.
 */
public class Exercise32_ParentCheckingHeuristic {

    public class BellmanFordParentCheckingHeuristic {

        private double[] distTo;               // length of path to vertex
        private DirectedEdge[] edgeTo;         // last edge on path to vertex
        private boolean[] onQueue;             // is this vertex on the queue?
        private Queue<Integer> queue;          // vertices being relaxed
        private int callsToRelax;              // number of calls to relax()
        private Iterable<DirectedEdge> cycle;  // if there is a negative cycle in edgeTo[], return it

        public BellmanFordParentCheckingHeuristic(EdgeWeightedDigraph edgeWeightedDigraph, int source) {
            distTo = new double[edgeWeightedDigraph.vertices()];
            edgeTo = new DirectedEdge[edgeWeightedDigraph.vertices()];
            onQueue = new boolean[edgeWeightedDigraph.vertices()];
            queue = new Queue<>();

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }

            distTo[source] = 0;
            queue.enqueue(source);
            onQueue[source] = true;

            while (!queue.isEmpty() && !hasNegativeCycle()) {
                int vertex = queue.dequeue();
                onQueue[vertex] = false;

                // Parent-checking heuristic: visit vertex v only if its parent is not currently on the queue
                if (edgeTo[vertex] != null) {
                    int parentVertex = edgeTo[vertex].from();
                    if (onQueue[parentVertex]) {
                        continue;
                    }
                }

                relax(edgeWeightedDigraph, vertex);
            }
        }

        private void relax(EdgeWeightedDigraph edgeWeightedDigraph, int vertex) {

            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();

                if (distTo[neighbor] > distTo[vertex] + edge.weight()) {
                    distTo[neighbor] = distTo[vertex] + edge.weight();
                    edgeTo[neighbor] = edge;

                    if (!onQueue[neighbor]) {
                        queue.enqueue(neighbor);
                        onQueue[neighbor] = true;
                    }
                }

                if (callsToRelax++ % edgeWeightedDigraph.vertices() == 0) {
                    findNegativeCycle();
                }
            }
        }

        public double distTo(int vertex) {
            return distTo[vertex];
        }

        public DirectedEdge edgeTo(int vertex) {
            return edgeTo[vertex];
        }

        public boolean hasPathTo(int vertex) {
            return distTo[vertex] < Double.POSITIVE_INFINITY;
        }

        public Iterable<DirectedEdge> pathTo(int vertex) {
            if (!hasPathTo(vertex)) {
                return null;
            }

            Stack<DirectedEdge> path = new Stack<>();
            for(DirectedEdge edge = edgeTo[vertex]; edge != null; edge = edgeTo[edge.from()]) {
                path.push(edge);
            }

            return path;
        }

        private void findNegativeCycle() {
            int vertices = edgeTo.length;
            EdgeWeightedDigraph shortestPathsTree = new EdgeWeightedDigraph(vertices);

            for(int vertex = 0; vertex < vertices; vertex++) {
                if (edgeTo[vertex] != null) {
                    shortestPathsTree.addEdge(edgeTo[vertex]);
                }
            }

            EdgeWeightedDirectedCycle edgeWeightedCycleFinder = new EdgeWeightedDirectedCycle(shortestPathsTree);
            cycle = edgeWeightedCycleFinder.cycle();
        }

        public boolean hasNegativeCycle() {
            return cycle != null;
        }

        public Iterable<DirectedEdge> negativeCycle() {
            return cycle;
        }

    }

    public static void main(String[] args) {
        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(8);
        edgeWeightedDigraph.addEdge(new DirectedEdge(4, 5, 0.35));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 4, 0.35));
        edgeWeightedDigraph.addEdge(new DirectedEdge(4, 7, 0.37));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 7, 0.28));
        edgeWeightedDigraph.addEdge(new DirectedEdge(7, 5, 0.28));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 1, 0.32));
        edgeWeightedDigraph.addEdge(new DirectedEdge(0, 4, 0.38));
        edgeWeightedDigraph.addEdge(new DirectedEdge(0, 2, 0.26));
        edgeWeightedDigraph.addEdge(new DirectedEdge(7, 3, 0.39));
        edgeWeightedDigraph.addEdge(new DirectedEdge(1, 3, 0.29));
        edgeWeightedDigraph.addEdge(new DirectedEdge(2, 7, 0.34));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 2, -1.20));
        edgeWeightedDigraph.addEdge(new DirectedEdge(3, 6, 0.52));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 0, -1.40));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 4, -1.25));

        BellmanFordParentCheckingHeuristic bellmanFordParentCheckingHeuristic =
                new Exercise32_ParentCheckingHeuristic().new BellmanFordParentCheckingHeuristic(edgeWeightedDigraph, 0);
        StdOut.println("Shortest-paths-tree\n");
        StdOut.printf("%13s %10s\n", "edgeTo[]",  "distTo[]");

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            StdOut.printf("%d %11s %9.2f\n", vertex, bellmanFordParentCheckingHeuristic.edgeTo(vertex),
                    bellmanFordParentCheckingHeuristic.distTo(vertex));
        }

        StdOut.println("\nExpected:\n" +
                "     edgeTo[]   distTo[]\n" +
                "0        null      0.00\n" +
                "1   5->1 0.32      0.93\n" +
                "2   0->2 0.26      0.26\n" +
                "3   7->3 0.39      0.99\n" +
                "4  6->4 -1.25      0.26\n" +
                "5   4->5 0.35      0.61\n" +
                "6   3->6 0.52      1.51\n" +
                "7   2->7 0.34      0.60");

        StdOut.print("\nPath from 0 to 1: ");

        for(DirectedEdge edge : bellmanFordParentCheckingHeuristic.pathTo(1)) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected:         0->2 2->7 7->3 3->6 6->4 4->5 5->1");

        // Edge weighted digraph in which the parent-checking heuristic improves performance
        EdgeWeightedDigraph edgeWeightedDigraph2 = new EdgeWeightedDigraph(4);
        edgeWeightedDigraph2.addEdge(new DirectedEdge(0, 3, 4));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(0, 1, 2));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(1, 2, 2));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(3, 1, -3));

        BellmanFordParentCheckingHeuristic bellmanFordParentCheckingHeuristic2 =
                new Exercise32_ParentCheckingHeuristic().new BellmanFordParentCheckingHeuristic(edgeWeightedDigraph2, 0);
        StdOut.println("\nShortest-paths-tree 2\n");
        StdOut.printf("%13s %10s\n", "edgeTo[]",  "distTo[]");

        for(int vertex = 0; vertex < edgeWeightedDigraph2.vertices(); vertex++) {
            StdOut.printf("%d %11s %9.2f\n", vertex, bellmanFordParentCheckingHeuristic2.edgeTo(vertex),
                    bellmanFordParentCheckingHeuristic2.distTo(vertex));
        }

        StdOut.println("\nExpected:\n" +
                "     edgeTo[]   distTo[]\n" +
                "0        null      0.00\n" +
                "1  3->1 -3.00      1.00\n" +
                "2   1->2 2.00      3.00\n" +
                "3   0->3 4.00      4.00");
    }

}
