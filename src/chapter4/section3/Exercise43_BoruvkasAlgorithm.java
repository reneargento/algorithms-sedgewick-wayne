package chapter4.section3;

import chapter1.section3.Queue;
import chapter1.section5.UnionFind;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 14/11/17.
 */
// Based on https://algs4.cs.princeton.edu/43mst/BoruvkaMST.java.html
    // O(E lg V lg* E) where lg* is the inverse Ackermann function
public class Exercise43_BoruvkasAlgorithm {

    public class BoruvkaMST {

        private Queue<Edge> minimumSpanningTree;
        private double weight;

        public BoruvkaMST(EdgeWeightedGraph edgeWeightedGraph) {
            minimumSpanningTree = new Queue<>();

            UnionFind unionFind = new UnionFind(edgeWeightedGraph.vertices());

            // Repeats at most lg(V) times or until minimum spanning tree is complete
            for(int stage = 0; stage < edgeWeightedGraph.vertices(); stage = stage + stage) {
                if (minimumSpanningTree.size() == edgeWeightedGraph.vertices() - 1) {
                    break;
                }

                // For each tree in the forest, find its closest edge.
                // If edge weights are equal, ties are broken in favor of the first edge in EdgeWeightedGraph.edges()
                Edge[] closestEdges = new Edge[edgeWeightedGraph.vertices()];

                for(Edge edge : edgeWeightedGraph.edges()) {
                    int vertex1 = edge.either();
                    int vertex2 = edge.other(vertex1);

                    int treeIdentifier1 = unionFind.find(vertex1);
                    int treeIdentifier2 = unionFind.find(vertex2);

                    // Check if vertices are part of the same tree
                    if (treeIdentifier1 == treeIdentifier2) {
                        continue;
                    }

                    if (closestEdges[treeIdentifier1] == null || edge.weight() < closestEdges[treeIdentifier1].weight()) {
                        closestEdges[treeIdentifier1] = edge;
                    }
                    if (closestEdges[treeIdentifier2] == null || edge.weight() < closestEdges[treeIdentifier2].weight()) {
                        closestEdges[treeIdentifier2] = edge;
                    }
                }

                // Add newly discovered edges to the MST
                for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
                    Edge closestEdge = closestEdges[vertex];

                    if (closestEdge != null) {
                        int vertex1 = closestEdge.either();
                        int vertex2 = closestEdge.other(vertex1);

                        if (!unionFind.connected(vertex1, vertex2)) {
                            minimumSpanningTree.enqueue(closestEdge);
                            weight += closestEdge.weight();
                            unionFind.union(vertex1, vertex2);
                        }
                    }
                }
            }
        }

        public Iterable<Edge> edges() {
            return minimumSpanningTree;
        }

        public double weight() {
            return weight;
        }

    }

    public static void main(String[] args) {
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(5);
        edgeWeightedGraph.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraph.addEdge(new Edge(0, 3, 0.5));
        edgeWeightedGraph.addEdge(new Edge(1, 2, 0.12));
        edgeWeightedGraph.addEdge(new Edge(1, 4, 0.91));
        edgeWeightedGraph.addEdge(new Edge(2, 3, 0.72));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.8));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.82));
        edgeWeightedGraph.addEdge(new Edge(4, 4, 0.1));

        Exercise43_BoruvkasAlgorithm.BoruvkaMST boruvkaMST =
                new Exercise43_BoruvkasAlgorithm().new BoruvkaMST(edgeWeightedGraph);

        for(Edge edge : boruvkaMST.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "0-1 0.42000\n" +
                "1-2 0.12000\n" +
                "0-3 0.50000\n" +
                "3-4 0.80000");
    }

}
