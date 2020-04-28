package chapter4.section1;

import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 07/10/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for fixing a bug on the random graph generation:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/139
@SuppressWarnings("unchecked")
public class Exercise41_RandomSimpleGraphs {

    private class Graph implements GraphInterface {

        private final int vertices;
        private int edges;
        private HashSet<Integer>[] adjacent;

        public Graph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
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
            adjacent[vertex2].add(vertex1);
            edges++;
        }

        public int degree(int vertex) {
            return adjacent[vertex].size();
        }

        public Iterable<Integer> adjacent(int vertex) {
            return adjacent[vertex].keys();
        }

        public HashSet<Integer> adjacentSetOfValues(int vertex) {
            return adjacent[vertex];
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

    public Graph randomSimpleGraph(int vertices, int edges) {
        // A complete graph has n * (n - 1) / 2 edges
        int maxNumberOfEdges = (vertices * (vertices - 1)) / 2;

        if (edges > maxNumberOfEdges) {
            throw new IllegalArgumentException("The maximum number of edges a simple graph with " + vertices
                    + " vertices may have is " + maxNumberOfEdges);
        }

        Graph randomSimpleGraph = new Graph(vertices);

        while (randomSimpleGraph.edges() < edges){
            int vertexId1 = StdRandom.uniform(vertices);
            int vertexId2 = StdRandom.uniform(vertices);

            if (vertexId1 != vertexId2
                    && !randomSimpleGraph.adjacentSetOfValues(vertexId1).contains(vertexId2)) {
                randomSimpleGraph.addEdge(vertexId1, vertexId2);
            }
        }
        return randomSimpleGraph;
    }

    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        int edges = Integer.parseInt(args[1]);

        Graph randomSimpleGraph = new Exercise41_RandomSimpleGraphs().randomSimpleGraph(vertices, edges);
        StdOut.println(randomSimpleGraph);
    }

}
