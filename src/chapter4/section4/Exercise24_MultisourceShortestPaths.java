package chapter4.section4;

import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 07/12/17.
 */
public class Exercise24_MultisourceShortestPaths {

    public interface DijkstraMultisourceSPAPI {
        double distTo(int vertex);
        boolean hasPathTo(int vertex);
        Iterable<DirectedEdge> pathTo(int vertex);
    }

    public class DijkstraMultisourceSP implements DijkstraMultisourceSPAPI {

        private DijkstraSP dijkstraSP;

        DijkstraMultisourceSP(EdgeWeightedDigraph edgeWeightedDigraph, HashSet<Integer> sources) {

            EdgeWeightedDigraph edgeWeightedDigraphWithExtraSource =
                    new EdgeWeightedDigraph(edgeWeightedDigraph.vertices() + 1);

            // Copy graph
            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                    edgeWeightedDigraphWithExtraSource.addEdge(edge);
                }
            }

            // Add extra source connected to all sources
            int dummyVertexId = edgeWeightedDigraphWithExtraSource.vertices() - 1;

            for(int source : sources.keys()) {
                edgeWeightedDigraphWithExtraSource.addEdge(new DirectedEdge(dummyVertexId, source, 0));
            }

            dijkstraSP = new DijkstraSP(edgeWeightedDigraphWithExtraSource, dummyVertexId);
        }

        public double distTo(int vertex) {
            return dijkstraSP.distTo(vertex);
        }

        public boolean hasPathTo(int vertex) {
            return dijkstraSP.hasPathTo(vertex);
        }

        public Iterable<DirectedEdge> pathTo(int vertex) {
            return dijkstraSP.pathTo(vertex);
        }

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

        HashSet<Integer> sources = new HashSet<>();
        sources.add(0);
        sources.add(1);
        sources.add(7);

        DijkstraMultisourceSP dijkstraMultisourceSP =
                new Exercise24_MultisourceShortestPaths().new DijkstraMultisourceSP(edgeWeightedDigraph, sources);

        StdOut.println("Distance to 5: " + dijkstraMultisourceSP.distTo(5) + " Expected: 0.28");
        StdOut.println("Has path to 5: " + dijkstraMultisourceSP.hasPathTo(5) + " Expected: true");

        StdOut.print("Path to 5: ");

        for(DirectedEdge edge : dijkstraMultisourceSP.pathTo(5)) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected:  8->7 7->5");

        StdOut.println("\nDistance to 6: " + dijkstraMultisourceSP.distTo(6) + " Expected: 0.81");
        StdOut.println("Has path to 6: " + dijkstraMultisourceSP.hasPathTo(6) + " Expected: true");

        StdOut.print("Path to 6: ");

        for(DirectedEdge edge : dijkstraMultisourceSP.pathTo(6)) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected:  8->1 1->3 3->6");
    }

}
