package chapter4.section3;

import chapter1.section3.Bag;
import chapter1.section3.Queue;
import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import static chapter4.section3.Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficient.NO_CONNECTION;

/**
 * Created by Rene Argento on 11/11/17.
 */
public class Exercise28_SpaceEfficientDataStructures {

    // Also used on exercise 4.3.40
    public interface EdgeWeightedGraphSpaceEfficientInterface {
        int vertices();
        int edgesCount();
        void addEdge(Edge edge);
        double[] adjacent(int vertex);
        Iterable<Edge> edges();
    }

    // Space efficient version of EdgeWeightedGraph
    // Trade-offs: does not support parallel edges, adjacent() operation takes O(V) instead of O(degree(V))
    // In case of parallel edges, only the edge with minimum weight will be stored
    @SuppressWarnings("unchecked")
    public class EdgeWeightedGraphSpaceEfficient implements EdgeWeightedGraphSpaceEfficientInterface {

        private final int vertices;
        private int edges;
        private double[][] adjacent;

        public static final double NO_CONNECTION = Double.POSITIVE_INFINITY;

        public EdgeWeightedGraphSpaceEfficient(int vertices) {
            this.vertices = vertices;
            edges = 0;
            adjacent = new double[vertices][vertices];

            for(int vertex = 0; vertex < vertices; vertex++) {
                adjacent[vertex] = new double[vertices];

                for(int adjacentVertex = 0; adjacentVertex < vertices; adjacentVertex++) {
                    adjacent[vertex][adjacentVertex] = NO_CONNECTION;
                }
            }
        }

        public EdgeWeightedGraphSpaceEfficient(In in) {
            this(in.readInt());
            int edges = in.readInt();

            if (edges < 0) {
                throw new IllegalArgumentException("Number of edges must be nonnegative");
            }

            for(int i = 0; i < edges; i++) {
                int vertex1 = in.readInt();
                int vertex2 = in.readInt();
                double weight = in.readDouble();

                Edge edge = new Edge(vertex1, vertex2, weight);
                addEdge(edge);
            }
        }

        public int vertices() {
            return vertices;
        }

        public int edgesCount() {
            return edges;
        }

        public void addEdge(Edge edge) {
            int vertex1 = edge.either();
            int vertex2 = edge.other(vertex1);

            if (adjacent[vertex1][vertex2] != NO_CONNECTION) {
                if (adjacent[vertex1][vertex2] <= edge.weight()) {
                    return;
                }
                edges--;
            }

            adjacent[vertex1][vertex2] = edge.weight();
            adjacent[vertex2][vertex1] = edge.weight();
            edges++;
        }

        public double[] adjacent(int vertex) {
            return adjacent[vertex];
        }

        public Iterable<Edge> edges() {
            Bag<Edge> edges = new Bag<>();

            for(int vertex1 = 0; vertex1 < vertices; vertex1++) {
                for(int vertex2 = vertex1 + 1; vertex2 < vertices; vertex2++) {
                    if (adjacent[vertex1][vertex2] != NO_CONNECTION) {
                        edges.add(new Edge(vertex1, vertex2, adjacent[vertex1][vertex2]));
                    }
                }
            }

            return edges;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex1 = 0; vertex1 < vertices; vertex1++) {
                stringBuilder.append(vertex1).append(": ");

                for(int vertex2 = 0; vertex2 < vertices; vertex2++) {
                    if (adjacent[vertex1][vertex2] != NO_CONNECTION) {
                        String formattedEdge = String.format("%d-%d %.5f", vertex1, vertex2, adjacent[vertex1][vertex2]);
                        stringBuilder.append(formattedEdge).append(" ");
                    }
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    // Space efficient version of lazy Prim's algorithm
    // Trade-off: it has runtime complexity of O(E * V^2)
    public class LazyPrimMSTSpaceEfficient {

        private boolean[] marked; // minimum spanning tree vertices
        private Queue<Edge> minimumSpanningTree;
        private PriorityQueueResize<Double> priorityQueue; // crossing (and ineligible) edge weights

        private double weight;

        public LazyPrimMSTSpaceEfficient(EdgeWeightedGraphSpaceEfficientInterface edgeWeightedGraph) {
            priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);
            marked = new boolean[edgeWeightedGraph.vertices()];
            minimumSpanningTree = new Queue<>();

            visit(edgeWeightedGraph, 0); // assumes the graph is connected

            while (!priorityQueue.isEmpty()) {
                Double minEdgeWeight = priorityQueue.deleteTop(); // Get lowest-weight edge from priority queue

                boolean isMinWeightEdgeEligible = false;

                Edge edgeToAddInMST = null;

                for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
                    double[] adjacentEdges = edgeWeightedGraph.adjacent(vertex);

                    for(int otherVertex = 0; otherVertex < adjacentEdges.length; otherVertex++) {
                        double edgeWeight = adjacentEdges[otherVertex];

                        if (edgeWeight == NO_CONNECTION) {
                            continue;
                        }

                        if (edgeWeight == minEdgeWeight
                                && ((marked[vertex] && !marked[otherVertex]) || (marked[otherVertex] && !marked[vertex]))) {
                            isMinWeightEdgeEligible = true;
                            edgeToAddInMST = new Edge(vertex, otherVertex, edgeWeight);

                            break;
                        }
                    }

                    if (isMinWeightEdgeEligible) {
                        break;
                    }
                }

                if (!isMinWeightEdgeEligible) {
                    continue;
                }

                int vertex1 = edgeToAddInMST.either();
                int vertex2 = edgeToAddInMST.other(vertex1);

                // Add edge to the minimum spanning tree
                minimumSpanningTree.enqueue(edgeToAddInMST);
                weight += edgeToAddInMST.weight();

                // Add vertex to the minimum spanning tree
                if (!marked[vertex1]) {
                    visit(edgeWeightedGraph, vertex1);
                }
                if (!marked[vertex2]) {
                    visit(edgeWeightedGraph, vertex2);
                }
            }
        }

        private void visit(EdgeWeightedGraphSpaceEfficientInterface edgeWeightedGraph, int vertex) {
            // Mark vertex and add to priority queue all edge weights from vertex to unmarked vertices
            marked[vertex] = true;

            double[] adjacentEdges = edgeWeightedGraph.adjacent(vertex);

            for (int otherVertex = 0; otherVertex < adjacentEdges.length; otherVertex++) {
                double edgeWeight = adjacentEdges[otherVertex];

                if (edgeWeight != NO_CONNECTION && !marked[otherVertex]) {
                    priorityQueue.insert(edgeWeight);
                }
            }
        }

        public Iterable<Edge> edges() {
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
        Exercise28_SpaceEfficientDataStructures spaceEfficientDataStructures = new Exercise28_SpaceEfficientDataStructures();

        EdgeWeightedGraphSpaceEfficient edgeWeightedGraphSpaceEfficient =
                spaceEfficientDataStructures.new EdgeWeightedGraphSpaceEfficient(5);
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(0, 3, 0.5));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(1, 2, 0.12));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(1, 4, 0.91));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(2, 3, 0.72));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(3, 4, 0.8));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(3, 4, 0.82));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(4, 4, 0.1));

        LazyPrimMSTSpaceEfficient lazyPrimMSTSpaceEfficient =
                spaceEfficientDataStructures.new LazyPrimMSTSpaceEfficient(edgeWeightedGraphSpaceEfficient);

        for(Edge edge : lazyPrimMSTSpaceEfficient.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "0-1 0.42000\n" +
                "1-2 0.12000\n" +
                "0-3 0.50000\n" +
                "3-4 0.80000");
    }

}
