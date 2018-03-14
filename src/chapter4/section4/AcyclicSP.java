package chapter4.section4;

import chapter1.section3.Stack;

/**
 * Created by Rene Argento on 27/11/17.
 */
public class AcyclicSP {

    private DirectedEdge[] edgeTo;
    private double[] distTo;

    public AcyclicSP(EdgeWeightedDigraph edgeWeightedDigraph, int source) {
        edgeTo = new DirectedEdge[edgeWeightedDigraph.vertices()];
        distTo = new double[edgeWeightedDigraph.vertices()];

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            distTo[vertex] = Double.POSITIVE_INFINITY;
        }

        distTo[source] = 0;

        Topological topological = new Topological(edgeWeightedDigraph);

        for(int vertex : topological.order()) {
            relax(edgeWeightedDigraph, vertex);
        }
    }

    private void relax(EdgeWeightedDigraph edgeWeightedDigraph, int vertex) {

        for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
            int neighbor = edge.to();

            if (distTo[neighbor] > distTo[vertex] + edge.weight()) {
                distTo[neighbor] = distTo[vertex] + edge.weight();
                edgeTo[neighbor] = edge;
            }
        }
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

}
