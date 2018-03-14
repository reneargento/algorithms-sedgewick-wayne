package chapter4.section4;

import chapter1.section3.Queue;
import chapter1.section3.Stack;

/**
 * Created by Rene Argento on 28/11/17.
 */
public class BellmanFordSP {

    private double[] distTo;               // length of path to vertex
    private DirectedEdge[] edgeTo;         // last edge on path to vertex
    private boolean[] onQueue;             // is this vertex on the queue?
    private Queue<Integer> queue;          // vertices being relaxed
    private int callsToRelax;              // number of calls to relax()
    private Iterable<DirectedEdge> cycle;  // if there is a negative cycle in edgeTo[], return it

    public BellmanFordSP(EdgeWeightedDigraph edgeWeightedDigraph, int source) {
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
