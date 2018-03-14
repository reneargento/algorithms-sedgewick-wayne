package chapter4.section3;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 07/11/17.
 */
public class Exercise10 {

    public class EdgeWeightedGraphAdjacencyMatrix {

        private final int vertices;
        private int edges;
        private double[][] adjacent; // adjacency matrix that stores the edge weights
                                     // adjacent[i][j] = Double.POSITIVE_INFINITY if there is no direct edge
                                     // between vertex i and vertex j

        public EdgeWeightedGraphAdjacencyMatrix(int vertices) {
            this.vertices = vertices;
            edges = 0;
            adjacent = new double[vertices][vertices];

            for(int vertex1 = 0; vertex1 < vertices; vertex1++) {
                for(int vertex2 = 0; vertex2 < vertices; vertex2++) {
                    adjacent[vertex1][vertex2] = Double.POSITIVE_INFINITY;
                }
            }
        }

        public EdgeWeightedGraphAdjacencyMatrix(In in) {
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
            double weight = edge.weight();

            // Parallel edges are ignored
            if (hasEdge(vertex1, vertex2)) {
                return;
            }

            adjacent[vertex1][vertex2] = weight;
            adjacent[vertex2][vertex1] = weight;
            edges++;
        }

        public boolean hasEdge(int vertex1, int vertex2) {
            return adjacent[vertex1][vertex2] != Double.POSITIVE_INFINITY;
        }

        public Iterable<Edge> adjacent(int vertex) {
            Bag<Edge> adjacentEdges = new Bag<>();

            for(int i = 0; i < vertices; i++) {
                if (hasEdge(vertex, i)) {
                    adjacentEdges.add(new Edge(vertex, i, adjacent[vertex][i]));
                }
            }

            return adjacentEdges;
        }

        public Iterable<Edge> edges() {
            Bag<Edge> edges = new Bag<>();

            for(int vertex = 0; vertex < vertices; vertex++) {
                for(Edge edge : adjacent(vertex)) {
                    if (edge.other(vertex) > vertex) {
                        edges.add(edge);
                    }
                }
            }

            return edges;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex = 0; vertex < vertices(); vertex++) {
                stringBuilder.append(vertex).append(": ");

                for(Edge neighbor : adjacent(vertex)) {
                    stringBuilder.append(neighbor).append(" ");
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        Exercise10.EdgeWeightedGraphAdjacencyMatrix edgeWeightedGraphAdjacencyMatrix =
                new Exercise10().new EdgeWeightedGraphAdjacencyMatrix(5);
        edgeWeightedGraphAdjacencyMatrix.addEdge(new Edge(0, 2, 0.35));
        edgeWeightedGraphAdjacencyMatrix.addEdge(new Edge(0, 4, 0.12));
        edgeWeightedGraphAdjacencyMatrix.addEdge(new Edge(1, 2, 0.99));
        edgeWeightedGraphAdjacencyMatrix.addEdge(new Edge(3, 2, 0.58));
        edgeWeightedGraphAdjacencyMatrix.addEdge(new Edge(4, 4, 0.1));
        edgeWeightedGraphAdjacencyMatrix.addEdge(new Edge(2, 4, 0.34));
        // Parallel edge - should be ignored
        edgeWeightedGraphAdjacencyMatrix.addEdge(new Edge(0, 4, 0.55));

        StdOut.println("Vertices: " + edgeWeightedGraphAdjacencyMatrix.vertices() + " Expected: 5");
        StdOut.println("Edges: " + edgeWeightedGraphAdjacencyMatrix.edgesCount() + " Expected: 6");
        StdOut.println("Has edge 2-3: " + edgeWeightedGraphAdjacencyMatrix.hasEdge(2, 3) + " Expected: true");
        StdOut.println("Has edge 0-3: " + edgeWeightedGraphAdjacencyMatrix.hasEdge(0, 3) + " Expected: false");

        StdOut.println("\n" + edgeWeightedGraphAdjacencyMatrix);

        StdOut.println("Expected:\n" +
                "0: 0-4 0.12000 0-2 0.35000 \n" +
                "1: 1-2 0.99000 \n" +
                "2: 2-4 0.34000 2-3 0.58000 2-1 0.99000 2-0 0.35000 \n" +
                "3: 3-2 0.58000 \n" +
                "4: 4-4 0.10000 4-2 0.34000 4-0 0.12000");
    }

}
