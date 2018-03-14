package chapter4.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 08/12/17.
 */
public class Exercise28_LongestPathsInDAGs {

    public class AcyclicLP {

        private AcyclicSP acyclicSP;

        public AcyclicLP(EdgeWeightedDigraph edgeWeightedDigraph, int source) {
            EdgeWeightedDigraph negatedEdgesDigraph = new EdgeWeightedDigraph(edgeWeightedDigraph.vertices());

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                    DirectedEdge negatedEdge = new DirectedEdge(edge.from(), edge.to(), edge.weight() * -1);
                    negatedEdgesDigraph.addEdge(negatedEdge);
                }
            }

            acyclicSP = new AcyclicSP(negatedEdgesDigraph, source);
        }

        public double distTo(int vertex) {
            if (acyclicSP.distTo(vertex) == 0) {
                return 0;
            }

            return acyclicSP.distTo(vertex) * -1;
        }

        public boolean hasPathTo(int vertex) {
            return acyclicSP.hasPathTo(vertex);
        }

        public Iterable<DirectedEdge> pathTo(int vertex) {
            return acyclicSP.pathTo(vertex);
        }

    }

    public static void main(String[] args) {
        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(9);
        edgeWeightedDigraph.addEdge(new DirectedEdge(0, 1, 1));
        edgeWeightedDigraph.addEdge(new DirectedEdge(0, 2, 2));
        edgeWeightedDigraph.addEdge(new DirectedEdge(1, 3, 3));
        edgeWeightedDigraph.addEdge(new DirectedEdge(3, 4, -3));
        edgeWeightedDigraph.addEdge(new DirectedEdge(3, 5, 4));
        edgeWeightedDigraph.addEdge(new DirectedEdge(4, 6, 1));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 6, 2));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 7, 2));

        AcyclicLP acyclicLP = new Exercise28_LongestPathsInDAGs().new AcyclicLP(edgeWeightedDigraph, 0);

        int furthestVertex = -1;
        double longestDistance = Double.NEGATIVE_INFINITY;

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            if (acyclicLP.distTo(vertex) > longestDistance) {
                longestDistance = acyclicLP.distTo(vertex);
                furthestVertex = vertex;
            }
        }

        StdOut.println("Dist to 1: " + acyclicLP.distTo(1) + " Expected: 1.0");
        StdOut.println("Dist to 8: " + acyclicLP.distTo(8) + " Expected: -Infinity");

        StdOut.print("\nLongest path: ");

        for(DirectedEdge edge : acyclicLP.pathTo(furthestVertex)) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }

        StdOut.println("\nExpected: 0->1 1->3 3->5 5->6 6->7");
    }

}
