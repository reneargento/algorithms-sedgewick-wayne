package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 19/10/17.
 */
public class Exercise5 {

    public class DigraphNoParallelEdgesOrSelfLoops extends Digraph {

        public DigraphNoParallelEdgesOrSelfLoops(int vertices) {
            super(vertices);
        }

        @Override
        public void addEdge(int vertex1, int vertex2) {
            // Self-loops and parallel edges not allowed
            if (vertex1 == vertex2
                    || hasEdge(vertex1, vertex2)) {
                return;
            }

            super.addEdge(vertex1, vertex2);
        }

        private boolean hasEdge(int vertex1, int vertex2) {
            for(int neighbor: adjacent(vertex1)) {
                if (neighbor == vertex2) {
                    return true;
                }
            }

            return false;
        }
    }

    public static void main(String[] args) {
        Exercise5 exercise5 = new Exercise5();
        DigraphNoParallelEdgesOrSelfLoops digraphNoParallelEdgesOrSelfLoops =
                exercise5.new DigraphNoParallelEdgesOrSelfLoops(5);

        digraphNoParallelEdgesOrSelfLoops.addEdge(0, 1);
        digraphNoParallelEdgesOrSelfLoops.addEdge(1, 4);
        digraphNoParallelEdgesOrSelfLoops.addEdge(2, 3);
        StdOut.println("Number of edges: " + digraphNoParallelEdgesOrSelfLoops.edges() + " Expected: 3");

        digraphNoParallelEdgesOrSelfLoops.addEdge(0, 1);
        StdOut.println("Number of edges: " + digraphNoParallelEdgesOrSelfLoops.edges() + " Expected: 3");

        digraphNoParallelEdgesOrSelfLoops.addEdge(2, 4);
        StdOut.println("Number of edges: " + digraphNoParallelEdgesOrSelfLoops.edges() + " Expected: 4");
        digraphNoParallelEdgesOrSelfLoops.addEdge(4, 4);
        StdOut.println("Number of edges: " + digraphNoParallelEdgesOrSelfLoops.edges() + " Expected: 4");
    }

}
