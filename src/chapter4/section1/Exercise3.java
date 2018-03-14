package chapter4.section1;

import chapter1.section3.Bag;
import chapter1.section3.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 15/09/17.
 */
@SuppressWarnings("unchecked")
public class Exercise3 {

    public class CopyGraph {
        private final int vertices;
        private int edges;
        private Bag<Integer>[] adjacent;

        public CopyGraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
            adjacent = (Bag<Integer>[]) new Bag[vertices];

            for(int i = 0; i < vertices; i++) {
                adjacent[i] = new Bag<>();
            }
        }

        public CopyGraph(In in) {
            this(in.readInt());
            int edges = in.readInt();

            for(int i = 0; i < edges; i++) {
                int vertex1 = in.readInt();
                int vertex2 = in.readInt();
                addEdge(vertex1, vertex2);
            }
        }

        public CopyGraph(Graph graph) {
            if (graph == null) {
                vertices = 0;
            } else {
                this.vertices = graph.vertices();
                this.edges = graph.edges();
                adjacent = (Bag<Integer>[]) new Bag[vertices];

                for(int i = 0; i < vertices; i++) {
                    adjacent[i] = new Bag<>();
                }

                for(int vertex = 0; vertex < graph.vertices(); vertex++) {
                    // Reverse so that adjacency list is in the same order as original
                    Stack<Integer> stack = new Stack<>();
                    for (int neighbor : graph.getAdjacencyList()[vertex]) {
                        stack.push(neighbor);
                    }
                    for (int neighbor : stack) {
                        adjacent[vertex].add(neighbor);
                    }
                }
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

        public Iterable<Integer> adjacent(int vertex) {
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

    public static void main(String[] args) {
        Exercise3 exercise3 = new Exercise3();

        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);

        CopyGraph copyGraph = exercise3.new CopyGraph(graph);
        StdOut.println(copyGraph);

        StdOut.println("Expected:\n" +
                "0: 3 2 1\n" +
                "1: 4 2 0\n" +
                "2: 3 1 0\n" +
                "3: 2 0\n" +
                "4: 1\n");

        copyGraph.addEdge(0, 4);
        StdOut.println("Edges in original graph: " + graph.edges() + " Expected: 6");
        StdOut.println("Edges in copy graph: " + copyGraph.edges() + " Expected: 7");
    }

}
