package chapter4.section2;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.In;

/**
 * Created by Rene Argento on 17/10/17.
 */
@SuppressWarnings("unchecked")
public class Digraph implements DigraphInterface {

    private final int vertices;
    private int edges;
    private Bag<Integer>[] adjacent;
    private int[] indegrees;
    private int[] outdegrees;

    public Digraph(int vertices) {
        this.vertices = vertices;
        this.edges = 0;

        indegrees = new int[vertices];
        outdegrees = new int[vertices];

        adjacent = (Bag<Integer>[]) new Bag[vertices];

        for(int vertex = 0; vertex < vertices; vertex++) {
            adjacent[vertex] = new Bag<>();
        }
    }

    public Digraph(In in) {
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
        adjacent[vertex1].add(vertex2);
        edges++;

        outdegrees[vertex1]++;
        indegrees[vertex2]++;
    }

    public Bag<Integer>[] getAdjacencyList() {
        return adjacent;
    }

    public Iterable<Integer> adjacent(int vertex) {
        return adjacent[vertex];
    }

    public int indegree(int vertex) {
        return indegrees[vertex];
    }

    public int outdegree(int vertex) {
        return outdegrees[vertex];
    }

    public Digraph reverse() {
        Digraph reverse = new Digraph(vertices);

        for(int vertex = 0; vertex < vertices; vertex++) {
            for(int neighbor : adjacent(vertex)) {
                reverse.addEdge(neighbor, vertex);
            }
        }

        return reverse;
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
