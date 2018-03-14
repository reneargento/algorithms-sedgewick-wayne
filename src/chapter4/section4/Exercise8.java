package chapter4.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 01/12/17.
 */
public class Exercise8 {

    public double findDiameter(EdgeWeightedDigraph edgeWeightedDigraph) {
        double diameter = Double.NEGATIVE_INFINITY;

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            DijkstraSP dijkstraSP = new DijkstraSP(edgeWeightedDigraph, vertex);

            for(int vertex2 = 0; vertex2 < edgeWeightedDigraph.vertices(); vertex2++) {
                if (dijkstraSP.distTo(vertex2) > diameter) {
                    diameter = dijkstraSP.distTo(vertex2);
                }
            }
        }

        return diameter;
    }

    public static void main(String[] args) {
        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(8);
        edgeWeightedDigraph.addEdge(new DirectedEdge(4, 5, 0.35));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 4, 0.35));
        edgeWeightedDigraph.addEdge(new DirectedEdge(4, 7, 0.37));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 7, 0.28));
        edgeWeightedDigraph.addEdge(new DirectedEdge(7, 5, 0.28));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 1, 0.32));
        edgeWeightedDigraph.addEdge(new DirectedEdge(0, 4, 0.38));
        edgeWeightedDigraph.addEdge(new DirectedEdge(0, 2, 0.26));
        edgeWeightedDigraph.addEdge(new DirectedEdge(7, 3, 0.39));
        edgeWeightedDigraph.addEdge(new DirectedEdge(1, 3, 0.29));
        edgeWeightedDigraph.addEdge(new DirectedEdge(2, 7, 0.34));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 2, 0.40));
        edgeWeightedDigraph.addEdge(new DirectedEdge(3, 6, 0.52));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 0, 0.58));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 4, 0.93));

        double diameter = new Exercise8().findDiameter(edgeWeightedDigraph);

        StdOut.println("Diameter: " + diameter + " Expected: 1.89");
    }

}
