package chapter4.section3;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.In;

/**
 * Created by Rene Argento on 07/11/17.
 */
@SuppressWarnings("unchecked")
public class EdgeWeightedGraph implements EdgeWeightedGraphInterface {

    private final int vertices;
    private int edges;
    private Bag<Edge>[] adjacent;

    public EdgeWeightedGraph(int vertices) {
        this.vertices = vertices;
        edges = 0;
        adjacent = (Bag<Edge>[]) new Bag[vertices];

        for(int vertex = 0; vertex < vertices; vertex++) {
            adjacent[vertex] = new Bag<>();
        }
    }

    public EdgeWeightedGraph(In in) {
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

        adjacent[vertex1].add(edge);
        adjacent[vertex2].add(edge);
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
