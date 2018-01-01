package chapter4.section4;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.In;

/**
 * Created by Rene Argento on 27/11/17.
 */
@SuppressWarnings("unchecked")
public class EdgeWeightedDigraph implements EdgeWeightedDigraphInterface {

    private final int vertices;
    private int edges;
    private Bag<DirectedEdge>[] adjacent;

    public EdgeWeightedDigraph(int vertices) {
        this.vertices = vertices;
        edges = 0;
        adjacent = (Bag<DirectedEdge>[]) new Bag[vertices];

        for(int vertex = 0; vertex < vertices; vertex++) {
            adjacent[vertex] = new Bag<>();
        }
    }

    public EdgeWeightedDigraph(In in) {
        this(in.readInt());
        int edges = in.readInt();

        if (edges < 0) {
            throw new IllegalArgumentException("Number of edges must be nonnegative");
        }

        for(int i = 0; i < edges; i++) {
            int vertexFrom = in.readInt();
            int vertexTo = in.readInt();
            double weight = in.readDouble();

            DirectedEdge edge = new DirectedEdge(vertexFrom, vertexTo, weight);
            addEdge(edge);
        }
    }

    public int vertices() {
        return vertices;
    }

    public int edgesCount() {
        return edges;
    }

    public int outdegree(int vertex) {
        return adjacent[vertex].size();
    }

    public void addEdge(DirectedEdge edge) {
        adjacent[edge.from()].add(edge);
        edges++;
    }

    public Iterable<DirectedEdge> adjacent(int vertex) {
        return adjacent[vertex];
    }

    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> bag = new Bag<>();

        for(int vertex = 0; vertex < vertices; vertex++) {
            for(DirectedEdge edge : adjacent[vertex]) {
                bag.add(edge);
            }
        }

        return bag;
    }

    public EdgeWeightedDigraph reverse() {
        EdgeWeightedDigraph reverse = new EdgeWeightedDigraph(vertices);

        for(int vertex = 0; vertex < vertices; vertex++) {
            for(DirectedEdge edge : adjacent(vertex)) {
                int neighbor = edge.to();
                reverse.addEdge(new DirectedEdge(neighbor, vertex, edge.weight()));
            }
        }

        return reverse;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(int vertex = 0; vertex < vertices(); vertex++) {
            stringBuilder.append(vertex).append(": ");

            for(DirectedEdge neighbor : adjacent(vertex)) {
                stringBuilder.append(neighbor).append(" ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

}
