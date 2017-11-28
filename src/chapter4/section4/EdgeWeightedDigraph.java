package chapter4.section4;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.In;

/**
 * Created by rene on 27/11/17.
 */
@SuppressWarnings("unchecked")
public class EdgeWeightedDigraph {

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
        // Will be implemented in exercise 4.4.2
        this(0);
    }

    public int vertices() {
        return vertices;
    }

    public int edgesCount() {
        return edges;
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

}
