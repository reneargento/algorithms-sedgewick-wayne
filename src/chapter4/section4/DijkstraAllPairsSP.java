package chapter4.section4;

/**
 * Created by Rene Argento on 27/11/17.
 */
public class DijkstraAllPairsSP {

    private DijkstraSP[] allPairsShortestPaths;

    DijkstraAllPairsSP(EdgeWeightedDigraph edgeWeightedDigraph) {
        allPairsShortestPaths = new DijkstraSP[edgeWeightedDigraph.vertices()];

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            allPairsShortestPaths[vertex] = new DijkstraSP(edgeWeightedDigraph, vertex);
        }
    }

    public Iterable<DirectedEdge> path(int source, int target) {
        return allPairsShortestPaths[source].pathTo(target);
    }

    public double distance (int source, int target) {
        return allPairsShortestPaths[source].distTo(target);
    }

}
