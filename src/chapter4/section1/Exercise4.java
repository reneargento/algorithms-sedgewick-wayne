package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 16/09/17.
 */
public class Exercise4 {

    public class GraphWithHasEdge extends Graph {

        public GraphWithHasEdge(int vertices) {
            super(vertices);
        }

        public boolean hasEdge(int vertex1, int vertex2) {
            for(int neighbor: adjacent(vertex1)) {
                if (neighbor == vertex2) {
                    return true;
                }
            }

            return false;
        }
    }

    public static void main(String[] args) {
        Exercise4 exercise4 = new Exercise4();
        GraphWithHasEdge graphWithHasEdge = exercise4.new GraphWithHasEdge(5);

        graphWithHasEdge.addEdge(0, 1);
        graphWithHasEdge.addEdge(1, 4);
        graphWithHasEdge.addEdge(2, 3);

        StdOut.println("Has edge 0 - 1: " + graphWithHasEdge.hasEdge(0 ,1) + " Expected: true");
        StdOut.println("Has edge 0 - 0: " + graphWithHasEdge.hasEdge(0 ,0) + " Expected: false");
        StdOut.println("Has edge 1 - 0: " + graphWithHasEdge.hasEdge(1 ,0) + " Expected: true");
        StdOut.println("Has edge 2 - 3: " + graphWithHasEdge.hasEdge(2 ,3) + " Expected: true");
        StdOut.println("Has edge 2 - 4: " + graphWithHasEdge.hasEdge(2 ,4) + " Expected: false");
    }

}
