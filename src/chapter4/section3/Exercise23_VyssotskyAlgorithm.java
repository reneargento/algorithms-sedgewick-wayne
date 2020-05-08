package chapter4.section3;

import chapter1.section3.Queue;
import chapter1.section3.Stack;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 09/11/17.
 */
public class Exercise23_VyssotskyAlgorithm {

    public class Vyssotsky {

        // O(E * (V + E)) = O(E^2)
        public Queue<Edge> minimumSpanningTree(EdgeWeightedGraph edgeWeightedGraph) {
            EdgeWeightedGraphWithDelete putativeTree = new EdgeWeightedGraphWithDelete(edgeWeightedGraph.vertices());

            for(Edge edge : edgeWeightedGraph.edges()) {
                putativeTree.addEdge(edge);
                HashSet<Integer> vertexToSearch = new HashSet<>();
                vertexToSearch.add(edge.either());

                EdgeWeightedCycle edgeWeightedCycle = new EdgeWeightedCycle(putativeTree, vertexToSearch);

                // If a cycle was formed, delete the maximum-weight edge in it
                if (edgeWeightedCycle.hasCycle()) {
                    Stack<Edge> cycle = edgeWeightedCycle.cycle();
                    Edge maxWeightEdge = null;
                    double maxWeight = Double.NEGATIVE_INFINITY;

                    for(Edge edgeInCycle : cycle) {
                        if (edgeInCycle.weight() > maxWeight) {
                            maxWeight = edgeInCycle.weight();
                            maxWeightEdge = edgeInCycle;
                        }
                    }

                    putativeTree.deleteEdge(maxWeightEdge);
                }
            }

            Queue<Edge> minimumSpanningTree = new Queue<>();

            for(Edge edge : putativeTree.edges()) {
                minimumSpanningTree.enqueue(edge);
            }

            return minimumSpanningTree;
        }
    }

    public static void main(String[] args) {
        Exercise23_VyssotskyAlgorithm.Vyssotsky vyssotsky = new Exercise23_VyssotskyAlgorithm().new Vyssotsky();

        EdgeWeightedGraph edgeWeightedGraph1 = new EdgeWeightedGraph(5);
        edgeWeightedGraph1.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraph1.addEdge(new Edge(1, 2, 0.12));
        edgeWeightedGraph1.addEdge(new Edge(2, 3, 0.5));
        edgeWeightedGraph1.addEdge(new Edge(3, 4, 0.8));
        edgeWeightedGraph1.addEdge(new Edge(3, 4, 0.82));
        edgeWeightedGraph1.addEdge(new Edge(4, 4, 0.1));

        EdgeWeightedGraph edgeWeightedGraph2 = new EdgeWeightedGraph(11);
        // Component 1
        edgeWeightedGraph2.addEdge(new Edge(0, 1, 0.3));
        edgeWeightedGraph2.addEdge(new Edge(1, 2, 0.41));
        // Component 2
        edgeWeightedGraph2.addEdge(new Edge(5, 7, 0.2));
        edgeWeightedGraph2.addEdge(new Edge(6, 7, 0.11));
        edgeWeightedGraph2.addEdge(new Edge(4, 4, 0.1));
        // Component 3
        edgeWeightedGraph2.addEdge(new Edge(8, 10, 0.99));
        edgeWeightedGraph2.addEdge(new Edge(8, 9, 0.77));
        edgeWeightedGraph2.addEdge(new Edge(9, 10, 0.2));

        StdOut.println("Vyssotsky Minimum Spanning Tree");

        Queue<Edge> minimumSpanningTree = vyssotsky.minimumSpanningTree(edgeWeightedGraph1);

        for(Edge edge : minimumSpanningTree) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "3-4 0.80000\n" +
                "2-3 0.50000\n" +
                "1-2 0.12000\n" +
                "0-1 0.42000\n");

        StdOut.println("Vyssotsky Minimum Spanning Forest");

        Queue<Edge> minimumSpanningForest = vyssotsky.minimumSpanningTree(edgeWeightedGraph2);

        for(Edge edge : minimumSpanningForest) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "9-10 0.20000\n" +
                "8-9 0.77000\n" +
                "6-7 0.11000\n" +
                "5-7 0.20000\n" +
                "1-2 0.41000\n" +
                "0-1 0.30000");
    }

}
