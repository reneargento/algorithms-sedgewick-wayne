package chapter4.section2;

import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 31/10/17.
 */
@SuppressWarnings("unchecked")
public class Exercise46_RandomSimpleDigraphs {

    private class Digraph implements DigraphInterface {

        private final int vertices;
        private int edges;
        private HashSet<Integer>[] adjacent;
        private int[] indegrees;
        private int[] outdegrees;

        public Digraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;

            indegrees = new int[vertices];
            outdegrees = new int[vertices];

            adjacent = (HashSet<Integer>[]) new HashSet[vertices];

            for(int vertex = 0; vertex < vertices; vertex++) {
                adjacent[vertex] = new HashSet<>();
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

        public Iterable<Integer> adjacent(int vertex) {
            return adjacent[vertex].keys();
        }

        public HashSet<Integer> adjacentSetOfValues(int vertex) {
            return adjacent[vertex];
        }

        public int indegree(int vertex) {
            return indegrees[vertex];
        }

        public int outdegree(int vertex) {
            return outdegrees[vertex];
        }

        public DigraphInterface reverse() {
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

    public Digraph randomDigraph(int vertices, int edges) {
        // A complete digraph has n * (n - 1) edges
        int maxNumberOfEdges = vertices * (vertices - 1);

        if (edges > maxNumberOfEdges) {
            throw new IllegalArgumentException("The maximum number of edges a simple digraph with " + vertices
                    + " vertices may have is " + maxNumberOfEdges);
        }

        Digraph randomSimpleDigraph = new Digraph(vertices);

        while (randomSimpleDigraph.edges() < edges) {
            int vertexId1 = StdRandom.uniform(vertices);
            int vertexId2 = StdRandom.uniform(vertices);

            if (vertexId1 != vertexId2
                    && !randomSimpleDigraph.adjacentSetOfValues(vertexId1).contains(vertexId2)) {
                randomSimpleDigraph.addEdge(vertexId1, vertexId2);
            }
        }
        return randomSimpleDigraph;
    }

    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        int edges = Integer.parseInt(args[1]);

        Digraph randomSimpleDigraph = new Exercise46_RandomSimpleDigraphs().randomDigraph(vertices, edges);
        StdOut.println(randomSimpleDigraph);
    }

}
