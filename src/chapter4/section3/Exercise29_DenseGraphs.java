package chapter4.section3;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 11/11/17.
 */
public class Exercise29_DenseGraphs {

    // O(V^2)
    public class PrimMSTDenseGraphs {

        private Edge[] edgeTo; // shortest edge from tree vertex
        private double[] distTo; // distTo[vertex] = edgeTo[vertex].weight()
        private boolean[] marked; // true if vertex is on the minimum spanning tree

        private double weight;

        public PrimMSTDenseGraphs(EdgeWeightedGraph edgeWeightedGraph) {
            edgeTo = new Edge[edgeWeightedGraph.vertices()];
            distTo = new double[edgeWeightedGraph.vertices()];
            marked = new boolean[edgeWeightedGraph.vertices()];

            for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }

            distTo[0] = 0;
            int visitedVertices = 0;
            int nextVertexToVisit = 0;

            while (visitedVertices != edgeWeightedGraph.vertices()) {
                nextVertexToVisit = visit(edgeWeightedGraph, nextVertexToVisit);
                visitedVertices++;
            }
        }

        private int visit(EdgeWeightedGraph edgeWeightedGraph, int vertex) {
            // Add vertex to the minimum spanning tree; update data structures
            marked[vertex] = true;

            for(Edge edge : edgeWeightedGraph.adjacent(vertex)) {
                int otherVertex = edge.other(vertex);
                if (marked[otherVertex]) {
                    continue; // vertex-otherVertex is ineligible
                }

                if (edge.weight() < distTo[otherVertex]) {
                    // Edge edge is the new best connection from the minimum spanning tree to otherVertex
                    if (distTo[otherVertex] != Double.POSITIVE_INFINITY) {
                        weight -= distTo[otherVertex];
                    }
                    weight += edge.weight();

                    edgeTo[otherVertex] = edge;
                    distTo[otherVertex] = edge.weight();
                }
            }

            int nextVertexToVisit = -1;
            double minEdgeWeight = Double.POSITIVE_INFINITY;

            for(int vertexToVisit = 0; vertexToVisit < edgeWeightedGraph.vertices(); vertexToVisit++) {
                if (!marked[vertexToVisit] && distTo[vertexToVisit] < minEdgeWeight) {
                    nextVertexToVisit = vertexToVisit;
                    minEdgeWeight = distTo[vertexToVisit];
                }
            }

            return nextVertexToVisit;
        }

        public Iterable<Edge> edges() {
            Queue<Edge> minimumSpanningTree = new Queue<>();

            for(int vertex = 1; vertex < edgeTo.length; vertex++) {
                minimumSpanningTree.enqueue(edgeTo[vertex]);
            }

            return minimumSpanningTree;
        }

        public double lazyWeight() {
            double weight = 0;

            for(Edge edge : edges()) {
                weight += edge.weight();
            }

            return weight;
        }

        public double eagerWeight() {
            return weight;
        }
    }

    public static void main(String[] args) {
        Exercise29_DenseGraphs exercise29_denseGraphs = new Exercise29_DenseGraphs();

        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(5);
        edgeWeightedGraph.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraph.addEdge(new Edge(0, 3, 0.5));
        edgeWeightedGraph.addEdge(new Edge(1, 2, 0.12));
        edgeWeightedGraph.addEdge(new Edge(1, 4, 0.91));
        edgeWeightedGraph.addEdge(new Edge(2, 3, 0.72));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.8));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.82));
        edgeWeightedGraph.addEdge(new Edge(4, 4, 0.1));

        PrimMSTDenseGraphs primMSTDenseGraphs =
                exercise29_denseGraphs.new PrimMSTDenseGraphs(edgeWeightedGraph);

        for(Edge edge : primMSTDenseGraphs.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "0-1 0.42000\n" +
                "1-2 0.12000\n" +
                "0-3 0.50000\n" +
                "3-4 0.80000");
    }

}
