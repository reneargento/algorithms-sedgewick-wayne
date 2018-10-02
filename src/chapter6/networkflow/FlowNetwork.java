package chapter6.networkflow;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.In;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 25/09/18.
 */
@SuppressWarnings("unchecked")
public class FlowNetwork {

    private final int vertices;
    private int edges;
    private Bag<FlowEdge>[] adjacent;

    public FlowNetwork(int vertices) {
        if (vertices < 0) {
            throw new IllegalArgumentException("Number of vertices must be nonnegative");
        }

        this.vertices = vertices;
        edges = 0;
        adjacent = (Bag<FlowEdge>[]) new Bag[vertices];

        for(int vertex = 0; vertex < vertices; vertex++) {
            adjacent[vertex] = new Bag<>();
        }
    }

    public FlowNetwork(In in) {
        this(in.readInt());
        int edges = in.readInt();

        if (edges < 0) {
            throw new IllegalArgumentException("Number of edges must be nonnegative");
        }

        for(int i = 0; i < edges; i++) {
            int vertex1 = in.readInt();
            int vertex2 = in.readInt();
            double capacity = in.readDouble();

            FlowEdge edge = new FlowEdge(vertex1, vertex2, capacity);
            addEdge(edge);
        }
    }

    public int vertices() {
        return vertices;
    }

    public int edgesCount() {
        return edges;
    }

    public void addEdge(FlowEdge edge) {
        int vertex1 = edge.from();
        int vertex2 = edge.to();

        adjacent[vertex1].add(edge);
        adjacent[vertex2].add(edge);
        edges++;
    }

    public Iterable<FlowEdge> adjacent(int vertex) {
        return adjacent[vertex];
    }

    public Iterable<FlowEdge> edges() {
        Bag<FlowEdge> edges = new Bag<>();

        for(int vertex = 0; vertex < vertices; vertex++) {
            for(FlowEdge edge : adjacent[vertex]) {
                if (edge.to() != vertex) {
                    edges.add(edge);
                }
            }
        }

        return edges;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(int vertex = 0; vertex < vertices(); vertex++) {
            stringBuilder.append(vertex).append(": ");

            StringJoiner neighbors = new StringJoiner(" ");
            for(FlowEdge edge : adjacent(vertex)) {
                if (edge.from() == vertex) {
                    neighbors.add(edge.toString());
                }
            }
            stringBuilder.append(neighbors.toString()).append("\n");
        }

        return stringBuilder.toString();
    }

}
