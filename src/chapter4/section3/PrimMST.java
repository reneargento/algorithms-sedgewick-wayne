package chapter4.section3;

import chapter2.section4.IndexMinPriorityQueue;

/**
 * Created by rene on 07/11/17.
 */
public class PrimMST {

    private Edge[] edgeTo; // shortest edge from tree vertex
    private double[] distTo; // distTo[vertex] = edgeTo[vertex].weight()
    private boolean[] marked; // true if vertex is on the minimum spanning tree
    private IndexMinPriorityQueue<Double> priorityQueue; // eligible crossing edges

    public PrimMST(EdgeWeightedGraph edgeWeightedGraph) {
        edgeTo = new Edge[edgeWeightedGraph.vertices()];
        distTo = new double[edgeWeightedGraph.vertices()];
        marked = new boolean[edgeWeightedGraph.vertices()];

        for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
            distTo[vertex] = Double.POSITIVE_INFINITY;
        }
        priorityQueue = new IndexMinPriorityQueue<>(edgeWeightedGraph.vertices());

        // Initialize priority queue with 0, weight 0
        distTo[0] = 0;
        priorityQueue.insert(0, 0.0);

        while (!priorityQueue.isEmpty()) {
            visit(edgeWeightedGraph, priorityQueue.deleteMin()); // Add closest vertex to the minimum spanning tree
        }
    }

    private void visit(EdgeWeightedGraph edgeWeightedGraph, int vertex) {
        // Add vertex to the minimum spanning tree; update data structures
        marked[vertex] = true;

        for(Edge edge : edgeWeightedGraph.adjacent(vertex)) {
            int otherVertex = edge.other(vertex);
            if(marked[otherVertex]) {
                continue; // vertex-otherVertex is ineligible
            }

            if(edge.weight() < distTo[otherVertex]) {
                // Edge edge is the new best connection from the minimum spanning tree to otherVertex
                edgeTo[otherVertex] = edge;
                distTo[otherVertex] = edge.weight();

                if(priorityQueue.contains(otherVertex)) {
                    priorityQueue.changeKey(otherVertex, distTo[otherVertex]);
                } else {
                    priorityQueue.insert(otherVertex, distTo[otherVertex]);
                }
            }
        }
    }

    public Iterable<Edge> edges() {
        // Will be done in exercise 4.3.21
        return null;
    }

    public double weight() {
        // Will be done in exercise 4.3.31
        return 0;
    }

}
