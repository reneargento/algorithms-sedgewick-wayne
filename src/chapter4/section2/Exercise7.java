package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 19/10/17.
 */
public class Exercise7 {

    public class Degrees {

        private int[] indegrees;
        private int[] outdegrees;
        private List<Integer> sources;
        private List<Integer> sinks;
        private boolean isMap;

        Degrees(Digraph digraph) {
            indegrees = new int[digraph.vertices()];
            outdegrees = new int[digraph.vertices()];
            sources = new ArrayList<>();
            sinks = new ArrayList<>();
            isMap = true;

            for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
                for(int neighbor : digraph.adjacent(vertex)) {
                    indegrees[neighbor]++;
                    outdegrees[vertex]++;
                }
            }

            for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
                if (indegrees[vertex] == 0) {
                    sources.add(vertex);
                }

                if (outdegrees[vertex] == 0) {
                    sinks.add(vertex);
                }

                if (outdegrees[vertex] != 1) {
                    isMap = false;
                }
            }
        }

        public int indegree(int vertex) {
            return indegrees[vertex];
        }

        public int outdegree(int vertex) {
            return outdegrees[vertex];
        }

        public Iterable<Integer> sources() {
            return sources;
        }

        public Iterable<Integer> sinks() {
            return sinks;
        }

        public boolean isMap() {
            return isMap;
        }
    }

    public static void main(String[] args) {
        Digraph digraph1 = new Digraph(6);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(2, 1);
        digraph1.addEdge(2, 3);
        digraph1.addEdge(3, 3);
        digraph1.addEdge(4, 3);

        StdOut.println("Test 1\n");

        Degrees degrees1 = new Exercise7().new Degrees(digraph1);
        StdOut.println("Indegree of vertex 0: " + degrees1.indegree(0) + " Expected: 0");
        StdOut.println("Indegree of vertex 1: " + degrees1.indegree(1) + " Expected: 2");
        StdOut.println("Indegree of vertex 2: " + degrees1.indegree(2) + " Expected: 0");
        StdOut.println("Indegree of vertex 3: " + degrees1.indegree(3) + " Expected: 3");
        StdOut.println("Indegree of vertex 4: " + degrees1.indegree(4) + " Expected: 0");
        StdOut.println("Indegree of vertex 5: " + degrees1.indegree(5) + " Expected: 0");

        StdOut.println();

        StdOut.println("Outdegree of vertex 0: " + degrees1.outdegree(0) + " Expected: 1");
        StdOut.println("Outdegree of vertex 1: " + degrees1.outdegree(1) + " Expected: 0");
        StdOut.println("Outdegree of vertex 2: " + degrees1.outdegree(2) + " Expected: 2");
        StdOut.println("Outdegree of vertex 3: " + degrees1.outdegree(3) + " Expected: 1");
        StdOut.println("Outdegree of vertex 4: " + degrees1.outdegree(4) + " Expected: 1");
        StdOut.println("Outdegree of vertex 5: " + degrees1.outdegree(5) + " Expected: 0");

        StdOut.println();

        StdOut.print("Sources: ");
        for(int source : degrees1.sources()) {
            StdOut.print(source + " ");
        }
        StdOut.print("\nExpected: 0 2 4 5");

        StdOut.print("\n\nSinks: ");
        for(int sink : degrees1.sinks()) {
            StdOut.print(sink + " ");
        }
        StdOut.print("\nExpected: 1 5");

        StdOut.println("\nIs map: " + degrees1.isMap() + " Expected: false");

        StdOut.println("\nTest 2\n");

        Digraph digraph2 = new Digraph(6);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(1, 5);
        digraph2.addEdge(2, 3);
        digraph2.addEdge(3, 3);
        digraph2.addEdge(4, 2);
        digraph2.addEdge(5, 0);

        Degrees degrees2 = new Exercise7().new Degrees(digraph2);

        StdOut.println("Indegree of vertex 0: " + degrees2.indegree(0) + " Expected: 1");
        StdOut.println("Indegree of vertex 1: " + degrees2.indegree(1) + " Expected: 1");
        StdOut.println("Indegree of vertex 2: " + degrees2.indegree(2) + " Expected: 1");
        StdOut.println("Indegree of vertex 3: " + degrees2.indegree(3) + " Expected: 2");
        StdOut.println("Indegree of vertex 4: " + degrees2.indegree(4) + " Expected: 0");
        StdOut.println("Indegree of vertex 5: " + degrees2.indegree(5) + " Expected: 1");

        StdOut.println();

        StdOut.println("Outdegree of vertex 0: " + degrees2.outdegree(0) + " Expected: 1");
        StdOut.println("Outdegree of vertex 1: " + degrees2.outdegree(1) + " Expected: 1");
        StdOut.println("Outdegree of vertex 2: " + degrees2.outdegree(2) + " Expected: 1");
        StdOut.println("Outdegree of vertex 3: " + degrees2.outdegree(3) + " Expected: 1");
        StdOut.println("Outdegree of vertex 4: " + degrees2.outdegree(4) + " Expected: 1");
        StdOut.println("Outdegree of vertex 5: " + degrees2.outdegree(5) + " Expected: 1");

        StdOut.println();

        StdOut.print("Sources: ");
        for(int source : degrees2.sources()) {
            StdOut.print(source + " ");
        }
        StdOut.print("\nExpected: 4");

        StdOut.print("\n\nSinks: ");
        for(int sink : degrees2.sinks()) {
            StdOut.print(sink + " ");
        }
        StdOut.print("\nExpected: ");

        StdOut.println("\nIs map: " + degrees2.isMap() + " Expected: true");
    }

}