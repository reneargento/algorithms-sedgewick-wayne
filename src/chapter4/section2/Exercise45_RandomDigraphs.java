package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 31/10/17.
 */
public class Exercise45_RandomDigraphs {

    public Digraph erdosRenyiDigraph(int vertices, int edges) {
        Digraph randomDigraph = new Digraph(vertices);

        for(int edge = 0; edge < edges; edge++) {
            int vertexId1 = StdRandom.uniform(vertices);
            int vertexId2 = StdRandom.uniform(vertices);

            randomDigraph.addEdge(vertexId1, vertexId2);
        }

        return randomDigraph;
    }

    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        int edges = Integer.parseInt(args[1]);

        Digraph randomDigraph = new Exercise45_RandomDigraphs().erdosRenyiDigraph(vertices, edges);
        StdOut.println(randomDigraph);
    }

}
