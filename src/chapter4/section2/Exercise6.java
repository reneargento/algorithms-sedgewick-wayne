package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 19/10/17.
 */
public class Exercise6 {

    public static void main(String[] args) {

        Digraph digraph = new Digraph(5);
        digraph.addEdge(0, 1);
        digraph.addEdge(0, 2);
        digraph.addEdge(0, 3);
        digraph.addEdge(1, 2);
        digraph.addEdge(1, 4);
        digraph.addEdge(2, 3);

        StdOut.println("Vertices in digraph: " + digraph.vertices() + " Expected: 5");
        StdOut.println("Edges in digraph: " + digraph.edges() + " Expected: 6");

        digraph.addEdge(0, 4);
        StdOut.println("Edges in digraph after addEdge(): " + digraph.edges() + " Expected: 7");

        StdOut.println("\nDigraph: ");
        StdOut.println(digraph);

        StdOut.println("Expected:\n" +
                "0: 4 3 2 1\n" +
                "1: 4 2\n" +
                "2: 3\n" +
                "3: \n" +
                "4: ");

        StdOut.println("\nReverse digraph: ");
        Digraph reverseDigraph = digraph.reverse();
        StdOut.println(reverseDigraph);

        StdOut.println("Expected:\n" +
                "0: \n" +
                "1: 0\n" +
                "2: 1 0\n" +
                "3: 2 0\n" +
                "4: 1 0");
    }

}
