package chapter4.section1;

import chapter1.section3.Bag;
import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 01/10/17.
 */
public class Exercise34_SymbolGraph {

    private class Graph {

        private int vertices;
        private int edges;
        private SeparateChainingHashTable<Integer, Bag<Integer>> adjacent;

        public Graph() {
            this.vertices = 0;
            this.edges = 0;
            adjacent = new SeparateChainingHashTable<>();
        }

        public Graph(In in) {
            this();
            int edges = in.readInt();

            for (int i = 0; i < edges; i++) {
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
            if (!adjacent.contains(vertex1)) {
                adjacent.put(vertex1, new Bag<>());
                vertices++;
            }
            if (!adjacent.contains(vertex2)) {
                adjacent.put(vertex2, new Bag<>());
                vertices++;
            }

            adjacent.get(vertex1).add(vertex2);
            adjacent.get(vertex2).add(vertex1);
            edges++;
        }

        public int degree(int vertex) {
            return adjacent.get(vertex).size();
        }

        public Iterable<Integer> adjacent(int vertex) {
            return adjacent.get(vertex);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex : adjacent.keys()) {
                stringBuilder.append(vertex).append(": ");

                for(int neighbor : adjacent(vertex)) {
                    stringBuilder.append(neighbor).append(" ");
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public class SymbolGraphOnePass {

        private SeparateChainingHashTable<String, Integer> vertexNameToIdMap;
        private String[] keys;
        private Graph graph;

        public SymbolGraphOnePass(String stream, String separator) {
            vertexNameToIdMap = new SeparateChainingHashTable<>();
            graph = new Graph();

            In in = new In(stream);

            while (in.hasNextLine()) {
                String[] vertices = in.readLine().split(separator);

                // Create vertices
                for(int i = 0; i < vertices.length; i++) {
                    if (!vertexNameToIdMap.contains(vertices[i])) {
                        vertexNameToIdMap.put(vertices[i], vertexNameToIdMap.size());
                    }
                }

                // Add edges
                int vertex = vertexNameToIdMap.get(vertices[0]);
                for(int i = 1; i < vertices.length; i++) {
                    graph.addEdge(vertex, vertexNameToIdMap.get(vertices[i]));
                }
            }

            keys = new String[vertexNameToIdMap.size()];

            for(String vertexName : vertexNameToIdMap.keys()) {
                keys[vertexNameToIdMap.get(vertexName)] = vertexName;
            }
        }

        public boolean contains(String vertexName) {
            return vertexNameToIdMap.contains(vertexName);
        }

        public int index(String vertexName) {
            return vertexNameToIdMap.get(vertexName);
        }

        public String name(int vertexId) {
            return keys[vertexId];
        }

        public Graph graph() {
            return graph;
        }

    }

    public static void main(String[] args) {
        Exercise34_SymbolGraph exercise34_symbolGraph = new Exercise34_SymbolGraph();

        String filePath = Constants.FILES_PATH + Constants.MOVIES_FILE;
        String separator = "/";

        SymbolGraphOnePass symbolGraph = exercise34_symbolGraph.new SymbolGraphOnePass(filePath, separator);
        Graph graph = symbolGraph.graph();

        StdOut.println("Vertices: " + graph.vertices() + " Expected: 119429");
        StdOut.println("Edges: " + graph.edges() + " Expected: 202927");
    }

}
