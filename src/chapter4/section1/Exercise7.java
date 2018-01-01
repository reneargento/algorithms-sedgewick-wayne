package chapter4.section1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 16/09/17.
 */
public class Exercise7 {

    //Path /Users/rene/Desktop/Algorithms/Books/Algorithms, 4th ed. - Exercises/Data/4.1.7.txt
    public static void main(String[] args) {
        String file = args[0];
        Graph graph = new Graph(new In(file));
        StdOut.println(graph);
    }

}
