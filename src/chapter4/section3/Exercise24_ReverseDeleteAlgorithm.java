package chapter4.section3;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rene Argento on 09/11/17.
 */
public class Exercise24_ReverseDeleteAlgorithm {

    // O(E * (V + E)) = O(E^2)
    public Queue<Edge> minimumSpanningTreeWithReverseDelete(EdgeWeightedGraphWithDelete edgeWeightedGraph) {

        // edgeWeightedGraph.edgesCount() counts self-loops so we need to compute the count in some other way
        // In this case the list edgesList is used, just to compute the count of edges that are not self-loops
        List<Edge> edgesList = new ArrayList<>();
        for(Edge edge : edgeWeightedGraph.edges()) {
            edgesList.add(edge);
        }

        Edge[] edges = new Edge[edgesList.size()];
        int edgesArrayIndex = 0;

        for(Edge edge : edgeWeightedGraph.edges()) {
            edges[edgesArrayIndex++] = edge;
        }

        Arrays.sort(edges, Collections.reverseOrder());

        for(Edge edge : edges) {
            edgeWeightedGraph.deleteEdge(edge);

            ConnectedComponentsEdgeWeightedGraph connectedComponentsEdgeWeightedGraph =
                    new ConnectedComponentsEdgeWeightedGraph(edgeWeightedGraph);
            // If deleting the edge disconnected the graph, re-add it
            if (connectedComponentsEdgeWeightedGraph.count() > 1) {
                edgeWeightedGraph.addEdge(edge);
            }
        }

        Queue<Edge> minimumSpanningTree = new Queue<>();

        for(Edge edge : edgeWeightedGraph.edges()) {
            minimumSpanningTree.enqueue(edge);
        }

        return minimumSpanningTree;
    }

    public static void main(String[] args) {
        Exercise24_ReverseDeleteAlgorithm reverseDeleteAlgorithm = new Exercise24_ReverseDeleteAlgorithm();

        EdgeWeightedGraphWithDelete edgeWeightedGraph1 = new EdgeWeightedGraphWithDelete(5);
        edgeWeightedGraph1.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraph1.addEdge(new Edge(1, 2, 0.12));
        edgeWeightedGraph1.addEdge(new Edge(2, 3, 0.5));
        edgeWeightedGraph1.addEdge(new Edge(3, 4, 0.8));
        edgeWeightedGraph1.addEdge(new Edge(3, 4, 0.82));
        edgeWeightedGraph1.addEdge(new Edge(4, 4, 0.1));

        EdgeWeightedGraphWithDelete edgeWeightedGraph2 = new EdgeWeightedGraphWithDelete(9);
        edgeWeightedGraph2.addEdge(new Edge(0, 1, 0.3));
        edgeWeightedGraph2.addEdge(new Edge(1, 2, 0.41));

        edgeWeightedGraph2.addEdge(new Edge(2, 5, 0.2));
        edgeWeightedGraph2.addEdge(new Edge(5, 3, 0.11));
        edgeWeightedGraph2.addEdge(new Edge(3, 4, 0.25));
        edgeWeightedGraph2.addEdge(new Edge(2, 4, 0.76));
        edgeWeightedGraph2.addEdge(new Edge(4, 4, 0.1));

        edgeWeightedGraph2.addEdge(new Edge(5, 6, 0.33));
        edgeWeightedGraph2.addEdge(new Edge(6, 8, 0.99));
        edgeWeightedGraph2.addEdge(new Edge(6, 7, 0.77));
        edgeWeightedGraph2.addEdge(new Edge(7, 8, 0.2));

        StdOut.println("Reverse-delete Minimum Spanning Tree 1");

        Queue<Edge> minimumSpanningTree1 = reverseDeleteAlgorithm.minimumSpanningTreeWithReverseDelete(edgeWeightedGraph1);

        for(Edge edge : minimumSpanningTree1) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "3-4 0.80000\n" +
                "2-3 0.50000\n" +
                "1-2 0.12000\n" +
                "0-1 0.42000\n");

        StdOut.println("Reverse-delete Minimum Spanning Tree 2");

        Queue<Edge> minimumSpanningTree2 = reverseDeleteAlgorithm.minimumSpanningTreeWithReverseDelete(edgeWeightedGraph2);

        for(Edge edge : minimumSpanningTree2) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "7-8 0.20000\n" +
                "6-7 0.77000\n" +
                "5-6 0.33000\n" +
                "3-4 0.25000\n" +
                "5-3 0.11000\n" +
                "2-5 0.20000\n" +
                "1-2 0.41000\n" +
                "0-1 0.30000");
    }

}
