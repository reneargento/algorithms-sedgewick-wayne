package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 19/10/17.
 */
public class Exercise4 {

    public class DigraphWithHasEdge extends Digraph {

        public DigraphWithHasEdge(int vertices) {
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
        DigraphWithHasEdge digraphWithHasEdge = exercise4.new DigraphWithHasEdge(5);

        digraphWithHasEdge.addEdge(0, 1);
        digraphWithHasEdge.addEdge(1, 4);
        digraphWithHasEdge.addEdge(2, 3);

        StdOut.println("Has edge 0 -> 1: " + digraphWithHasEdge.hasEdge(0 ,1) + " Expected: true");
        StdOut.println("Has edge 0 -> 0: " + digraphWithHasEdge.hasEdge(0 ,0) + " Expected: false");
        StdOut.println("Has edge 1 -> 0: " + digraphWithHasEdge.hasEdge(1 ,0) + " Expected: false");
        StdOut.println("Has edge 2 -> 3: " + digraphWithHasEdge.hasEdge(2 ,3) + " Expected: true");
        StdOut.println("Has edge 2 -> 4: " + digraphWithHasEdge.hasEdge(2 ,4) + " Expected: false");
    }

}
