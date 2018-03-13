package chapter4.section1;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 16/09/17.
 */
@SuppressWarnings("unchecked")
public class Exercise15 {

    public class Graph {

        private final int vertices;
        private int edges;
        private Bag<Integer>[] adjacent;

        public Graph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
            adjacent = (Bag<Integer>[]) new Bag[vertices];

            for(int i = 0; i < vertices; i++) {
                adjacent[i] = new Bag<>();
            }
        }

        public Graph(In in) {
            this(in.readInt());
            int edges = in.readInt(); //Not used
            in.readLine(); //Move to next line

            while (in.hasNextLine()) {
                String[] vertices = in.readLine().split(" ");

                int vertex = Integer.parseInt(vertices[0]);
                for(int i = 1; i < vertices.length; i++) {
                   addEdge(vertex, Integer.parseInt(vertices[i]));
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
        Exercise15 exercise15 = new Exercise15();

        String filePath = Constants.FILES_PATH + "4.1.15.txt";
        Graph graph = exercise15.new Graph(new In(filePath));
        StdOut.println(graph);

        StdOut.println("Expected:\n" +
                "0: 6 5 2 1\n" +
                "1: 0\n" +
                "2: 0\n" +
                "3: 5 4\n" +
                "4: 6 5 3\n" +
                "5: 4 3 0\n" +
                "6: 4 0\n" +
                "7: 8\n" +
                "8: 7\n" +
                "9: 12 11 10\n" +
                "10: 9\n" +
                "11: 12 9\n" +
                "12: 11 9");
    }

}
