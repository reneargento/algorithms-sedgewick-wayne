package chapter4.section4;

import chapter1.section3.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by Rene Argento on 23/12/17.
 */
// Based on https://algs4.cs.princeton.edu/44sp/LazyDijkstraSP.java.html
// O (E lg E)
public class Exercise39_LazyImplementationDijkstra {

    public class LazyDijkstraSP {

        private DirectedEdge[] edgeTo;  // last edge on path to vertex
        private double[] distTo;        // length of path to vertex
        private PriorityQueue<DirectedEdge> priorityQueue;
        private boolean marked[];       // relaxed vertices

        private class ByDistanceFromSourceComparator implements Comparator<DirectedEdge> {
            @Override
            public int compare(DirectedEdge edge1, DirectedEdge edge2) {
                double distance1 = distTo(edge1.from()) + edge1.weight();
                double distance2 = distTo(edge2.from()) + edge2.weight();

                return Double.compare(distance1, distance2);
            }
        }

        public LazyDijkstraSP(EdgeWeightedDigraphInterface edgeWeightedDigraph, int source) {
            edgeTo = new DirectedEdge[edgeWeightedDigraph.vertices()];
            distTo = new double[edgeWeightedDigraph.vertices()];
            priorityQueue = new PriorityQueue<>(new ByDistanceFromSourceComparator());
            marked = new boolean[edgeWeightedDigraph.vertices()];

            checkPresenceOfNegativeEdges(edgeWeightedDigraph);

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }
            distTo[source] = 0;

            relax(edgeWeightedDigraph, source);

            while (!priorityQueue.isEmpty()) {
                DirectedEdge edge = priorityQueue.poll();
                int vertexToVisit = edge.to();

                if (!marked[vertexToVisit]) {
                    relax(edgeWeightedDigraph, vertexToVisit);
                }
            }
        }

        private void relax(EdgeWeightedDigraphInterface edgeWeightedDigraph, int vertex) {
            marked[vertex] = true;

            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();

                if (distTo[neighbor] > distTo[vertex] + edge.weight()) {
                    distTo[neighbor] = distTo[vertex] + edge.weight();
                    edgeTo[neighbor] = edge;

                    priorityQueue.offer(edge);
                }
            }
        }

        private void checkPresenceOfNegativeEdges(EdgeWeightedDigraphInterface edgeWeightedDigraph) {
            for (DirectedEdge edge : edgeWeightedDigraph.edges()) {
                if (edge.weight() < 0) {
                    throw new IllegalArgumentException("Edge " + edge + " has negative weight");
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
            return marked[vertex];
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

    }

    public static void main(String[] args) {
        // tinyEWD.txt
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
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 2, 0.40));
        edgeWeightedDigraph.addEdge(new DirectedEdge(3, 6, 0.52));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 0, 0.58));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 4, 0.93));

        Exercise39_LazyImplementationDijkstra lazyImplementationDijkstra = new Exercise39_LazyImplementationDijkstra();
        int source = 0;
        LazyDijkstraSP lazyDijkstraSP = lazyImplementationDijkstra.new LazyDijkstraSP(edgeWeightedDigraph, source);

        StdOut.println("Shortest-paths-tree");

        for (int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            if (lazyDijkstraSP.hasPathTo(vertex)) {
                StdOut.printf("%d to %d (%.2f)  ", source, vertex, lazyDijkstraSP.distTo(vertex));

                for (DirectedEdge edge : lazyDijkstraSP.pathTo(vertex)) {
                    StdOut.print(edge + "   ");
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d         no path\n", source, vertex);
            }
        }

        StdOut.println("\nExpected:");
        StdOut.println("0 to 0 (0.00)  \n" +
                "0 to 1 (1.05)  0->4 0.38   4->5 0.35   5->1 0.32   \n" +
                "0 to 2 (0.26)  0->2 0.26   \n" +
                "0 to 3 (0.99)  0->2 0.26   2->7 0.34   7->3 0.39   \n" +
                "0 to 4 (0.38)  0->4 0.38   \n" +
                "0 to 5 (0.73)  0->4 0.38   4->5 0.35   \n" +
                "0 to 6 (1.51)  0->2 0.26   2->7 0.34   7->3 0.39   3->6 0.52   \n" +
                "0 to 7 (0.60)  0->2 0.26   2->7 0.34 ");
    }

}
