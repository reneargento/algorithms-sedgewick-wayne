package chapter4.section1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 16/09/17.
 */
public class Exercise7 {

    // Parameter example: 4.1.7.txt
    public static void main(String[] args) {
        String filePath = Constants.FILES_PATH + args[0];
        Graph graph = new Graph(new In(filePath));
        StdOut.println(graph);
    }

}
