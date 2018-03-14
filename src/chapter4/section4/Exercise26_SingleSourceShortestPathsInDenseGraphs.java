package chapter4.section4;

import chapter1.section3.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 07/12/17.
 */
public class Exercise26_SingleSourceShortestPathsInDenseGraphs {

    // O(V^2)
    public class DijkstraSPDenseGraph {

        private DirectedEdge[] edgeTo;  // last edge on path to vertex
        private double[] distTo;        // length of path to vertex

        public DijkstraSPDenseGraph(Exercise3.EdgeWeightedDigraphAdjacencyMatrix edgeWeightedDigraph, int source) {
            edgeTo = new DirectedEdge[edgeWeightedDigraph.vertices()];
            distTo = new double[edgeWeightedDigraph.vertices()];

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }
            distTo[source] = 0;

            boolean[] visited = new boolean[edgeWeightedDigraph.vertices()];
            int nextVertexToRelax = source;

            for(int relaxedVertices = 0; relaxedVertices < edgeWeightedDigraph.vertices(); relaxedVertices++) {
                nextVertexToRelax = relax(edgeWeightedDigraph, nextVertexToRelax, visited);
            }
        }

        private int relax(Exercise3.EdgeWeightedDigraphAdjacencyMatrix edgeWeightedDigraph, int vertex, boolean[] visited) {
            visited[vertex] = true;

            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();

                if (distTo[neighbor] > distTo[vertex] + edge.weight()) {
                    distTo[neighbor] = distTo[vertex] + edge.weight();
                    edgeTo[neighbor] = edge;
                }
            }

            int nextVertexToRelax = -1;
            double minimumWeight = Double.POSITIVE_INFINITY;

            for(int vertexToCheck = 0; vertexToCheck < edgeWeightedDigraph.vertices(); vertexToCheck++) {
                if (!visited[vertexToCheck] && distTo[vertexToCheck] < minimumWeight) {
                    nextVertexToRelax = vertexToCheck;
                    minimumWeight = distTo[vertexToCheck];
                }
            }

            return nextVertexToRelax;
        }

        public double distTo(int vertex) {
            return distTo[vertex];
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

        public DirectedEdge edgeTo(int vertex) {
            return edgeTo[vertex];
        }

    }

    public static void main(String[] args) {
        Exercise3.EdgeWeightedDigraphAdjacencyMatrix edgeWeightedDigraph =
                new Exercise3().new EdgeWeightedDigraphAdjacencyMatrix(8);
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

        DijkstraSPDenseGraph dijkstraSPDenseGraph =
                new Exercise26_SingleSourceShortestPathsInDenseGraphs()
                        .new DijkstraSPDenseGraph(edgeWeightedDigraph, 0);
        StdOut.println("Shortest-paths-tree\n");
        StdOut.printf("%13s %10s\n", "edgeTo[]",  "distTo[]");

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            StdOut.printf("%d %11s %9.2f\n", vertex, dijkstraSPDenseGraph.edgeTo(vertex), dijkstraSPDenseGraph.distTo(vertex));
        }

        StdOut.print("\nPath from 0 to 6: ");

        for(DirectedEdge edge : dijkstraSPDenseGraph.pathTo(6)) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected: 0->2 2->7 7->3 3->6");
    }

}
