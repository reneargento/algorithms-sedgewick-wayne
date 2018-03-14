package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 16/09/17.
 */
public class Exercise5 {

    public class GraphNoParallelEdgesOrSelfLoops extends Graph {

        public GraphNoParallelEdgesOrSelfLoops(int vertices) {
            super(vertices);
        }

        @Override
        public void addEdge(int vertex1, int vertex2) {
            //Self-loops and parallel edges not allowed
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
        GraphNoParallelEdgesOrSelfLoops graphNoParallelEdgesOrSelfLoops =
                exercise5.new GraphNoParallelEdgesOrSelfLoops(5);

        graphNoParallelEdgesOrSelfLoops.addEdge(0, 1);
        graphNoParallelEdgesOrSelfLoops.addEdge(1, 4);
        graphNoParallelEdgesOrSelfLoops.addEdge(2, 3);
        StdOut.println("Number of edges: " + graphNoParallelEdgesOrSelfLoops.edges() + " Expected: 3");

        graphNoParallelEdgesOrSelfLoops.addEdge(1, 0);
        StdOut.println("Number of edges: " + graphNoParallelEdgesOrSelfLoops.edges() + " Expected: 3");

        graphNoParallelEdgesOrSelfLoops.addEdge(2, 4);
        StdOut.println("Number of edges: " + graphNoParallelEdgesOrSelfLoops.edges() + " Expected: 4");
        graphNoParallelEdgesOrSelfLoops.addEdge(4, 4);
        StdOut.println("Number of edges: " + graphNoParallelEdgesOrSelfLoops.edges() + " Expected: 4");
    }

}
