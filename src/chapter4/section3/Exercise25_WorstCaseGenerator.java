package chapter4.section3;

import chapter1.section3.Bag;
import chapter1.section3.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 09/11/17.
 */
public class Exercise25_WorstCaseGenerator {

    // This edge-weighted graph implementation keeps the adjacent neighbors in the order added.
    // This is necessary to guarantee a worst-case for the eager version of Prim's algorithm.
    @SuppressWarnings("unchecked")
    private class EdgeWeightedGraphWithNeighborsInOrder implements EdgeWeightedGraphInterface {

        private final int vertices;
        private int edges;
        private Queue<Edge>[] adjacent;

        public EdgeWeightedGraphWithNeighborsInOrder(int vertices) {
            this.vertices = vertices;
            edges = 0;
            adjacent = (Queue<Edge>[]) new Queue[vertices];

            for(int vertex = 0; vertex < vertices; vertex++) {
                adjacent[vertex] = new Queue<>();
            }
        }

        public EdgeWeightedGraphWithNeighborsInOrder(In in) {
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

            adjacent[vertex1].enqueue(edge);
            adjacent[vertex2].enqueue(edge);
            edges++;
        }

        public Iterable<Edge> adjacent(int vertex) {
            return adjacent[vertex];
        }

        public Iterable<Edge> edges() {
            Bag<Edge> edges = new Bag<>();

            for(int vertex = 0; vertex < vertices; vertex++) {
                for(Edge edge : adjacent[vertex]) {
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

    //Worst-case for the lazy version of Prim's algorithm -> a complete graph, leading to a priority queue of size E = V^2.
    // The complexity is O(E lg E)
    public EdgeWeightedGraph generateWorstCaseGraphForLazyPrim(int vertices) {
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(vertices);

        for(int vertex1 = 0; vertex1 < vertices; vertex1++) {
            for(int vertex2 = vertex1 + 1; vertex2 < vertices; vertex2++) {
                double weight = StdRandom.uniform();
                Edge edge = new Edge(vertex1, vertex2, weight);
                edgeWeightedGraph.addEdge(edge);
            }
        }

        return edgeWeightedGraph;
    }

    //Worst-case for the eager version of Prim's algorithm -> a complete graph, where each new vertex analyzed that
    // is not in the current MST is closer to the other vertices that are not in the MST than any other vertex in
    // the current MST is (for each v and w that are not in the MST, v-w weight is less than distTo[w]).
    // This causes edgeTo, distTo and the priority queue to be updated E times.
    // The complexity is O(E lg V)
    public EdgeWeightedGraphWithNeighborsInOrder generateWorstCaseGraphForEagerPrim(int vertices) {
        EdgeWeightedGraphWithNeighborsInOrder edgeWeightedGraph = new EdgeWeightedGraphWithNeighborsInOrder(vertices);

        double maxWeight = StdRandom.uniform();
        double valueToDecrease = 0.00001;

        for(int vertex1 = 0; vertex1 < vertices; vertex1++) {
            valueToDecrease = valueToDecrease + 0.00001;

            for(int vertex2 = vertex1 + 1; vertex2 < vertices; vertex2++) {
                double weight = maxWeight - valueToDecrease;
                Edge edge = new Edge(vertex1, vertex2, weight);
                edgeWeightedGraph.addEdge(edge);
            }
        }

        return edgeWeightedGraph;
    }

    public static void main(String[] args) {
        Exercise25_WorstCaseGenerator worstCaseGenerator = new Exercise25_WorstCaseGenerator();

        int numberOfVertices = 10;
        EdgeWeightedGraph worstCaseForLazyPrim = worstCaseGenerator.generateWorstCaseGraphForLazyPrim(numberOfVertices);
        StdOut.println(worstCaseForLazyPrim);

        EdgeWeightedGraphWithNeighborsInOrder worstCaseForEagerPrim =
                worstCaseGenerator.generateWorstCaseGraphForEagerPrim(numberOfVertices);
        StdOut.println(worstCaseForEagerPrim);
    }

}
