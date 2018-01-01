package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 07/10/17.
 */
public class Exercise40_RandomGraphs {

    public Graph erdosRenyiGraph(int vertices, int edges) {
        Graph randomGraph = new Graph(vertices);

        for(int edge = 0; edge < edges; edge++) {
            int vertexId1 = StdRandom.uniform(vertices);
            int vertexId2 = StdRandom.uniform(vertices);

            randomGraph.addEdge(vertexId1, vertexId2);
        }

        return randomGraph;
    }

    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        int edges = Integer.parseInt(args[1]);

        Graph randomGraph = new Exercise40_RandomGraphs().erdosRenyiGraph(vertices, edges);
        StdOut.println(randomGraph);
    }

}
