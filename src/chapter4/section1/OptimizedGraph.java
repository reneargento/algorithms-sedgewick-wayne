package chapter4.section1;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.In;

/**
 * Created by Rene Argento on 29/10/17.
 */
@SuppressWarnings("unchecked")
// This graph data structure initializes its adjacency lists lazily to avoid initializing lists that will not be used
public class OptimizedGraph implements GraphInterface {

    private final int vertices;
    private int edges;
    private Bag<Integer>[] adjacent;

    public OptimizedGraph(int vertices) {
        this.vertices = vertices;
        this.edges = 0;
        adjacent = (Bag<Integer>[]) new Bag[vertices];
    }

    public OptimizedGraph(In in) {
        this(in.readInt());
        int edges = in.readInt();

        for(int i = 0; i < edges; i++) {
            int vertex1 = in.readInt();
            int vertex2 = in.readInt();
            addEdge(vertex1, vertex2);
        }
    }

    public int vertices() {
        return vertices;
    }

    public int edges() {
        return edges;
    }

    public void addEdge(int vertex1, int vertex2) {
        if (adjacent[vertex1] == null) {
            adjacent[vertex1] = new Bag<>();
        }
        if (adjacent[vertex2] == null) {
            adjacent[vertex2] = new Bag<>();
        }

        adjacent[vertex1].add(vertex2);
        adjacent[vertex2].add(vertex1);
        edges++;
    }

    public Bag<Integer>[] getAdjacencyList() {
        return adjacent;
    }

    public void updateAdjacencyList(int vertex, Bag adjacencyList) {
        adjacent[vertex] = adjacencyList;
    }

    public Iterable<Integer> adjacent(int vertex) {
        return adjacent[vertex];
    }

    public int degree(int vertex) {
        return adjacent[vertex].size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(int vertex = 0; vertex < vertices(); vertex++) {
            stringBuilder.append(vertex).append(": ");

            for(int neighbor : adjacent(vertex)) {
                stringBuilder.append(neighbor).append(" ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

}
