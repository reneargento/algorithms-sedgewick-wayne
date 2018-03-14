package chapter4.section2;

import chapter1.section3.Bag;
import chapter1.section3.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 19/10/17.
 */
@SuppressWarnings("unchecked")
public class Exercise3 {

    public class CopyDigraph {
        private final int vertices;
        private int edges;
        private Bag<Integer>[] adjacent;

        public CopyDigraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
            adjacent = (Bag<Integer>[]) new Bag[vertices];

            for(int vertex = 0; vertex < vertices; vertex++) {
                adjacent[vertex] = new Bag<>();
            }
        }

        public CopyDigraph(In in) {
            this(in.readInt());
            int edges = in.readInt();

            for(int i = 0; i < edges; i++) {
                int vertex1 = in.readInt();
                int vertex2 = in.readInt();
                addEdge(vertex1, vertex2);
            }
        }

        public CopyDigraph(Digraph digraph) {
            if (digraph == null) {
                vertices = 0;
            } else {
                this.vertices = digraph.vertices();
                this.edges = digraph.edges();
                adjacent = (Bag<Integer>[]) new Bag[vertices];

                for(int vertex = 0; vertex < vertices; vertex++) {
                    adjacent[vertex] = new Bag<>();
                }

                for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
                    // Reverse so that adjacency list is in the same order as original
                    Stack<Integer> stack = new Stack<>();
                    for (int neighbor : digraph.getAdjacencyList()[vertex]) {
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
            edges++;
        }

        public Iterable<Integer> adjacent(int vertex) {
            return adjacent[vertex];
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

    public static void main(String[] args) {
        Exercise3 exercise3 = new Exercise3();

        Digraph digraph = new Digraph(5);
        digraph.addEdge(0, 1);
        digraph.addEdge(0, 2);
        digraph.addEdge(0, 3);
        digraph.addEdge(1, 2);
        digraph.addEdge(1, 4);
        digraph.addEdge(2, 3);

        CopyDigraph copyDigraph = exercise3.new CopyDigraph(digraph);
        StdOut.println(copyDigraph);

        StdOut.println("Expected:\n" +
                "0: 3 2 1\n" +
                "1: 4 2\n" +
                "2: 3\n" +
                "3: \n" +
                "4: \n");

        copyDigraph.addEdge(0, 4);
        StdOut.println("Edges in original digraph: " + digraph.edges() + " Expected: 6");
        StdOut.println("Edges in copy digraph: " + copyDigraph.edges() + " Expected: 7");
    }

}
