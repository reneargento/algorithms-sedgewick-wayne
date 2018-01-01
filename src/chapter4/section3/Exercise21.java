package chapter4.section3;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 09/11/17.
 */
public class Exercise21 {

    public class PrimMSTWithEdges extends PrimMST {

        public PrimMSTWithEdges(EdgeWeightedGraph edgeWeightedGraph) {
            super(edgeWeightedGraph);
        }

        public Iterable<Edge> edges() {
            Queue<Edge> minimumSpanningTree = new Queue<>();

            for(int vertex = 1; vertex < edgeTo.length; vertex++) {
                minimumSpanningTree.enqueue(edgeTo[vertex]);
            }

            return minimumSpanningTree;
        }

    }

    public static void main(String[] args) {
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(5);
        edgeWeightedGraph.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraph.addEdge(new Edge(1, 2, 0.12));
        edgeWeightedGraph.addEdge(new Edge(2, 3, 0.2));
        edgeWeightedGraph.addEdge(new Edge(2, 4, 0.7));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.8));

        Exercise21.PrimMSTWithEdges primMSTWithEdges = new Exercise21().new PrimMSTWithEdges(edgeWeightedGraph);

        for(Edge edge : primMSTWithEdges.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "0-1 0.42000\n" +
                "1-2 0.12000\n" +
                "2-3 0.20000\n" +
                "2-4 0.70000");
    }

}
