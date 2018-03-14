package chapter4.section3;

import chapter1.section3.Queue;
import chapter1.section5.UnionFind;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 11/11/17.
 */
public class Exercise33_Certification {

    // The order of growth of the running time of this method is O(V * E)
    public boolean check(EdgeWeightedGraph edgeWeightedGraph, Queue<Edge> proposedMinimumSpanningTree) {
        // 1- Check if it is a spanning tree
        UnionFind unionFind = new UnionFind(edgeWeightedGraph.vertices());

        // Check that it is acyclic
        // O(V)
        for(Edge edge : proposedMinimumSpanningTree) {
            int vertex1 = edge.either();
            int vertex2 = edge.other(vertex1);

            if (unionFind.connected(vertex1, vertex2)) {
                return false; // Cycle found
            }
            unionFind.union(vertex1, vertex2);
        }

        // Check that it is connected
        // O(1)
        if (unionFind.count() != 1) {
            return false;
        }

        // 2- Check that every edge is a minimum-weight edge in the cut defined by removing that edge from the tree
        // O(V * E)
        for(Edge edgeInMST : proposedMinimumSpanningTree) {
            unionFind = new UnionFind(edgeWeightedGraph.vertices());

            // Add all edges in the MST except edgeInMST
            for(Edge edge : proposedMinimumSpanningTree) {
                if (edge != edgeInMST) {
                    int vertex1 = edge.either();
                    int vertex2 = edge.other(vertex1);

                    unionFind.union(vertex1, vertex2);
                }
            }

            // Check that edgeInMST is the minimum-weight edge in the crossing cut
            for(Edge edge : edgeWeightedGraph.edges()) {
                int vertex1 = edge.either();
                int vertex2 = edge.other(vertex1);

                if (!unionFind.connected(vertex1, vertex2)) {
                    if (edge.weight() < edgeInMST.weight()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        Exercise33_Certification exercise33_certification = new Exercise33_Certification();

        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(5);
        edgeWeightedGraph.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraph.addEdge(new Edge(0, 3, 0.5));
        edgeWeightedGraph.addEdge(new Edge(1, 2, 0.12));
        edgeWeightedGraph.addEdge(new Edge(1, 4, 0.91));
        edgeWeightedGraph.addEdge(new Edge(2, 3, 0.72));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.8));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.82));
        edgeWeightedGraph.addEdge(new Edge(4, 4, 0.1));

        Queue<Edge> proposal1 = new Queue<>();
        proposal1.enqueue(new Edge(0, 1, 0.42));
        proposal1.enqueue(new Edge(1, 2, 0.12));
        proposal1.enqueue(new Edge(0, 3, 0.5));
        proposal1.enqueue(new Edge(3, 4, 0.8));

        boolean check1 = exercise33_certification.check(edgeWeightedGraph, proposal1);
        StdOut.println("Check 1: " + check1 + " Expected: true");

        // Fails on the cut optimality condition
        Queue<Edge> proposal2 = new Queue<>();
        proposal2.enqueue(new Edge(0, 1, 0.42));
        proposal2.enqueue(new Edge(1, 2, 0.12));
        proposal2.enqueue(new Edge(2, 3, 0.72));
        proposal2.enqueue(new Edge(3, 4, 0.8));

        boolean check2 = exercise33_certification.check(edgeWeightedGraph, proposal2);
        StdOut.println("Check 2: " + check2 + " Expected: false");

        // Fails because it is not a spanning tree
        Queue<Edge> proposal3 = new Queue<>();
        proposal3.enqueue(new Edge(0, 1, 0.42));
        proposal3.enqueue(new Edge(0, 3, 0.5));
        proposal3.enqueue(new Edge(3, 4, 0.8));

        boolean check3 = exercise33_certification.check(edgeWeightedGraph, proposal3);
        StdOut.println("Check 3: " + check3 + " Expected: false");
    }

}
