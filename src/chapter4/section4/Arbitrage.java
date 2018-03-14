package chapter4.section4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 28/11/17.
 */
public class Arbitrage {

    public static void main(String[] args) {
        int vertices = StdIn.readInt();
        String[] name = new String[vertices];

        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(vertices);

        for(int vertex = 0; vertex < vertices; vertex++) {
            name[vertex] = StdIn.readString();

            for(int neighbor = 0; neighbor < vertices; neighbor++) {
                double rate = StdIn.readDouble();
                DirectedEdge edge = new DirectedEdge(vertex, neighbor, -Math.log(rate));
                edgeWeightedDigraph.addEdge(edge);
            }
        }

        BellmanFordSP bellmanFordSP = new BellmanFordSP(edgeWeightedDigraph, 0);

        if (bellmanFordSP.hasNegativeCycle()) {
            double stake = 1000.0;

            for(DirectedEdge edge : bellmanFordSP.negativeCycle()) {
                StdOut.printf("%10.5f %s ", stake, name[edge.from()]);
                stake *= Math.exp(-edge.weight());
                StdOut.printf("= %10.5f %s\n", stake, name[edge.to()]);
            }
        } else {
            StdOut.println("No arbitrage opportunity");
        }
    }
}
