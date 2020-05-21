package chapter4.section4;

import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 22/12/17.
 */
// Adapted from http://courses.csail.mit.edu/6.006/fall10/handouts/quiz2review.pdf section 8.3
//
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for fixing the time complexity of the
// computeSensitivity() method.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/151
public class Exercise38_Sensitivity {

    private class EdgeInformation {
        int from;
        int to;

        public EdgeInformation(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof EdgeInformation)) {
                return false;
            }

            EdgeInformation other = (EdgeInformation) object;
            return this.from == other.from && this.to == other.to;
        }

        @Override
        public int hashCode() {
            int result = 17;

            result += from * 31;
            result += to * 37;

            return result;
        }
    }

    // An edge is upwards critical if by increasing weight(e) by any value > 0 we increase the shortest path
    // distance from the source to some vertex v

    // An edge is downwards critical if by decreasing weight(e) by any value > 0 we decrease the shortest path
    // distance from the source to some vertex v (however, by definition, if weight(e) = 0 then e is not
    // downwards critical, because we can't decrease its weight below 0).

    // O(V^2 + E * lg V * V)
    public boolean[][] computeSensitivity(EdgeWeightedDigraph edgeWeightedDigraph) {

        // 0 - Initialize sensitivity matrix
        // We are considering that if an edge(v, w) does not exist, its weight is equal to infinity and any increase
        // in this weight does not increase any shortest-path distance between v and w.
        // Therefore, entries for non-existent edges are set to true.
        boolean[][] sensitivity = new boolean[edgeWeightedDigraph.vertices()][edgeWeightedDigraph.vertices()];

        HashSet<EdgeInformation> edgesInformation = new HashSet<>();

        for(DirectedEdge edge : edgeWeightedDigraph.edges()) {
            edgesInformation.add(new EdgeInformation(edge.from(), edge.to()));
        }

        for(int i = 0; i < sensitivity.length; i++) {
            for(int j = 0; j < sensitivity[0].length; j++) {
                if (!edgesInformation.contains(new EdgeInformation(i, j))) {
                    sensitivity[i][j] = true;
                }
            }
        }

        for(int source = 0; source < edgeWeightedDigraph.vertices(); source++) {
            // 1- Run Dijkstra from source
            DijkstraSP dijkstraSP = new DijkstraSP(edgeWeightedDigraph, source);

            // 2- Compute downwards critical edges in paths from source
            int[] downwardCriticalEdgesCount = new int[edgeWeightedDigraph.vertices()];

            for(DirectedEdge edge : edgeWeightedDigraph.edges()) {
                if (dijkstraSP.distTo(edge.from()) + edge.weight() == dijkstraSP.distTo(edge.to())) {
                    downwardCriticalEdgesCount[edge.to()]++;
                }
            }

            // 3- Check which edges adjacent to the source vertex are not upwards critical edges
            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(source)) {
                if (downwardCriticalEdgesCount[edge.to()] != 1) {
                    sensitivity[source][edge.to()] = true;
                }
            }
        }

        return sensitivity;
    }

    public static void main(String[] args) {
        Exercise38_Sensitivity sensitivity = new Exercise38_Sensitivity();

        EdgeWeightedDigraph edgeWeightedDigraph1 = new EdgeWeightedDigraph(6);
        edgeWeightedDigraph1.addEdge(new DirectedEdge(0, 1, 2));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(1, 2, 1));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(2, 3, 1));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(3, 4, 1));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(5, 0, 3));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(1, 0, 2));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(1, 3, 2));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(0, 2, 3));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(3, 4, 3));

        boolean[][] sensitivity1 = sensitivity.computeSensitivity(edgeWeightedDigraph1);

        StdOut.print("Upwards critical edges 1: ");
        for(int i = 0; i < sensitivity1.length; i++) {
            for(int j = 0; j < sensitivity1[0].length; j++) {
                if (!sensitivity1[i][j]) {
                    StdOut.print(i + "->" + j + " ");
                }
            }
        }

        StdOut.println("\nExpected: 0->1 1->0 1->2 2->3 3->4 5->0");

        EdgeWeightedDigraph edgeWeightedDigraph2 = new EdgeWeightedDigraph(5);
        edgeWeightedDigraph2.addEdge(new DirectedEdge(0, 1, 2));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(1, 2, 1));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(2, 3, 1)); // this is a bridge
        edgeWeightedDigraph2.addEdge(new DirectedEdge(0, 4, 1));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(4, 1, 1));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(1, 2, 1));

        boolean[][] sensitivity2 = sensitivity.computeSensitivity(edgeWeightedDigraph2);

        StdOut.print("\nUpwards critical edges 2: ");
        for(int i = 0; i < sensitivity2.length; i++) {
            for(int j = 0; j < sensitivity2[0].length; j++) {
                if (!sensitivity2[i][j]) {
                    StdOut.print(i + "->" + j + " ");
                }
            }
        }

        StdOut.println("\nExpected: 0->4 2->3 4->1");
    }

}
