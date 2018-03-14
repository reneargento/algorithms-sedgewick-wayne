package chapter4.section3;

import chapter1.section3.Queue;
import chapter1.section5.UnionFind;
import chapter2.section4.IndexMinPriorityQueue;
import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 09/11/17.
 */
// Based on https://algs4.cs.princeton.edu/43mst/PrimMST.java.html
    // and https://algs4.cs.princeton.edu/43mst/KruskalMST.java.html
public class Exercise22_MinimumSpanningForest {

    public class PrimMinimumSpanningForest {

        private Edge[] edgeTo; // shortest edge from tree vertex
        private double[] distTo; // distTo[vertex] = edgeTo[vertex].weight()
        private boolean[] marked; // true if vertex is on the minimum spanning forest
        private IndexMinPriorityQueue<Double> priorityQueue; // eligible crossing edges

        private double weight;

        public PrimMinimumSpanningForest(EdgeWeightedGraph edgeWeightedGraph) {
            edgeTo = new Edge[edgeWeightedGraph.vertices()];
            distTo = new double[edgeWeightedGraph.vertices()];
            marked = new boolean[edgeWeightedGraph.vertices()];

            for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }
            priorityQueue = new IndexMinPriorityQueue<>(edgeWeightedGraph.vertices());

            ConnectedComponentsEdgeWeightedGraph connectedComponents = new ConnectedComponentsEdgeWeightedGraph(edgeWeightedGraph);

            for(int connectedComponent = 0; connectedComponent < connectedComponents.count(); connectedComponent++) {

                for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
                    if (connectedComponents.id(vertex) == connectedComponent) {
                        prim(edgeWeightedGraph, vertex);
                        break;
                    }
                }
            }
        }

        private void prim(EdgeWeightedGraph edgeWeightedGraph, int sourceVertex) {
            distTo[sourceVertex] = 0;
            priorityQueue.insert(sourceVertex, 0.0);

            while (!priorityQueue.isEmpty()) {
                visit(edgeWeightedGraph, priorityQueue.deleteMin()); // Add closest vertex to the minimum spanning forest
            }
        }

        private void visit(EdgeWeightedGraph edgeWeightedGraph, int vertex) {
            // Add vertex to the minimum spanning forest; update data structures
            marked[vertex] = true;

            for(Edge edge : edgeWeightedGraph.adjacent(vertex)) {
                int otherVertex = edge.other(vertex);
                if (marked[otherVertex]) {
                    continue; // vertex-otherVertex is ineligible
                }

                if (edge.weight() < distTo[otherVertex]) {
                    // Edge edge is the new best connection from the minimum spanning forest to otherVertex
                    if (distTo[otherVertex] != Double.POSITIVE_INFINITY) {
                        weight -= distTo[otherVertex];
                    }
                    weight += edge.weight();

                    edgeTo[otherVertex] = edge;
                    distTo[otherVertex] = edge.weight();

                    if (priorityQueue.contains(otherVertex)) {
                        priorityQueue.decreaseKey(otherVertex, distTo[otherVertex]);
                    } else {
                        priorityQueue.insert(otherVertex, distTo[otherVertex]);
                    }
                }
            }
        }

        public Iterable<Edge> edges() {
            Queue<Edge> minimumSpanningForest = new Queue<>();

            for(int vertex = 0; vertex < edgeTo.length; vertex++) {
                if (edgeTo[vertex] != null) {
                    minimumSpanningForest.enqueue(edgeTo[vertex]);
                }
            }

            return minimumSpanningForest;
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

    // Kruskal's algorithm does not need to be modified to compute minimum spanning forests.
    // Its original version already computes either a minimum spanning tree (if the graph is connected) or
    // a minimum spanning forest (if the graph is not connected)
    public class KruskalMinimumSpanningForest {

        private Queue<Edge> minimumSpanningForest;
        private double weight;

        public KruskalMinimumSpanningForest(EdgeWeightedGraph edgeWeightedGraph) {
            minimumSpanningForest = new Queue<>();
            PriorityQueueResize<Edge> priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

            for(Edge edge : edgeWeightedGraph.edges()) {
                priorityQueue.insert(edge);
            }

            UnionFind unionFind = new UnionFind(edgeWeightedGraph.vertices());

            while (!priorityQueue.isEmpty() && minimumSpanningForest.size() < edgeWeightedGraph.vertices() - 1) {
                Edge edge = priorityQueue.deleteTop(); // Get lowest-weight edge from priority queue
                int vertex1 = edge.either();
                int vertex2 = edge.other(vertex1);

                // Ignore ineligible edges
                if (unionFind.connected(vertex1, vertex2)) {
                    continue;
                }

                unionFind.union(vertex1, vertex2);
                minimumSpanningForest.enqueue(edge); // Add edge to the minimum spanning forest

                weight += edge.weight();
            }
        }

        public Iterable<Edge> edges() {
            return minimumSpanningForest;
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
        Exercise22_MinimumSpanningForest exercise22_minimumSpanningForest = new Exercise22_MinimumSpanningForest();

        EdgeWeightedGraph edgeWeightedGraph1 = new EdgeWeightedGraph(5);
        // Component 1
        edgeWeightedGraph1.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraph1.addEdge(new Edge(1, 2, 0.12));
        // Component 2
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

        StdOut.println("Prim Minimum Spanning Forest 1");

        PrimMinimumSpanningForest primMinimumSpanningForest1 =
                exercise22_minimumSpanningForest.new PrimMinimumSpanningForest(edgeWeightedGraph1);

        for(Edge edge : primMinimumSpanningForest1.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "0-1 0.42000\n" +
                "1-2 0.12000\n" +
                "3-4 0.80000\n");

        StdOut.println("Prim Minimum Spanning Forest 2");

        PrimMinimumSpanningForest primMinimumSpanningForest2 =
                exercise22_minimumSpanningForest.new PrimMinimumSpanningForest(edgeWeightedGraph2);

        for(Edge edge : primMinimumSpanningForest2.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "0-1 0.30000\n" +
                "1-2 0.41000\n" +
                "6-7 0.11000\n" +
                "5-7 0.20000\n" +
                "8-9 0.77000\n" +
                "9-10 0.20000\n");

        StdOut.println("Kruskal Minimum Spanning Forest 1");

        KruskalMinimumSpanningForest kruskalMinimumSpanningForest1 =
                exercise22_minimumSpanningForest.new KruskalMinimumSpanningForest(edgeWeightedGraph1);

        for(Edge edge : kruskalMinimumSpanningForest1.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "1-2 0.12000\n" +
                "0-1 0.42000\n" +
                "3-4 0.80000\n");

        StdOut.println("Kruskal Minimum Spanning Forest 2");

        KruskalMinimumSpanningForest kruskalMinimumSpanningForest2 =
                exercise22_minimumSpanningForest.new KruskalMinimumSpanningForest(edgeWeightedGraph2);

        for(Edge edge : kruskalMinimumSpanningForest2.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "6-7 0.11000\n" +
                "9-10 0.20000\n" +
                "5-7 0.20000\n" +
                "0-1 0.30000\n" +
                "1-2 0.41000\n" +
                "8-9 0.77000");
    }

}
