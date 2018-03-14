package chapter4.section4;

import chapter1.section3.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 09/12/17.
 */
// Johnson's algorithm
public class Exercise30_AllPairsShortestPathsDigraphsWithoutNegativeCycles {

    public interface AllPairsShortestPathsDigraphsWithoutNegativeCyclesInterface {

        Iterable<DirectedEdge> path(int source, int target);
        double dist(int source, int target);
        boolean hasPathTo(int source, int target);
    }

    // O(V * E lg V)
    public class AllPairsShortestPathsDigraphsWithoutNegativeCycles
            implements AllPairsShortestPathsDigraphsWithoutNegativeCyclesInterface {

        private double[][] distances;
        private DirectedEdge[][] edgeTo;

        AllPairsShortestPathsDigraphsWithoutNegativeCycles(EdgeWeightedDigraph edgeWeightedDigraph) {
            distances = new double[edgeWeightedDigraph.vertices()][edgeWeightedDigraph.vertices()];
            edgeTo = new DirectedEdge[edgeWeightedDigraph.vertices()][edgeWeightedDigraph.vertices()];
            double[] newWeight = new double[edgeWeightedDigraph.vertices()];

            // 0 - Initialize all distances to Double.POSITIVE_INFINITY
            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                for(int neighbor = 0; neighbor < edgeWeightedDigraph.vertices(); neighbor++) {
                    distances[vertex][neighbor] = Double.POSITIVE_INFINITY;
                }
            }

            // 1- Add a new vertex to the graph, connected to all other vertices through edges of weight 0
            // O(V + E)
            EdgeWeightedDigraph edgeWeightedDigraphWithSource = new EdgeWeightedDigraph(edgeWeightedDigraph.vertices() + 1);
            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                    edgeWeightedDigraphWithSource.addEdge(edge);
                }
            }

            int newVertexId = edgeWeightedDigraph.vertices();

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                edgeWeightedDigraphWithSource.addEdge(new DirectedEdge(newVertexId, vertex, 0));
            }

            // 2- Run Bellman-Ford to get the distances from the new vertex to every other vertex.
            // Also check if there is any negative cycle
            // O(V * E)
            BellmanFordSP bellmanFordSP = new BellmanFordSP(edgeWeightedDigraphWithSource, newVertexId);

            if (bellmanFordSP.hasNegativeCycle()) {
                throw new IllegalArgumentException("Graph has a negative cycle");
            }

            // 3- Compute new weights, which are the distance from the new vertex to every other vertex
            // O(V)
            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                newWeight[vertex] = bellmanFordSP.distTo(vertex);
            }

            // 4- Generate a new graph with the new weights
            // O(V + E)
            EdgeWeightedDigraph edgeWeightedDigraphWithNewWeights = new EdgeWeightedDigraph(edgeWeightedDigraph.vertices());
            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                    double edgeWeight = edge.weight() + newWeight[edge.from()] - newWeight[edge.to()];
                    edgeWeightedDigraphWithNewWeights.addEdge(new DirectedEdge(edge.from(), edge.to(), edgeWeight));
                }
            }

            // 5- Run Dijkstra to compute all pairs shortest paths on the new graph.
            // Also compute the real all-pairs-shortest-path distances by adjusting the new weights
            // O(V * E lg V) + O(V^2) = O(V * E lg V)
            for(int source = 0; source < edgeWeightedDigraph.vertices(); source++) {
                DijkstraSP dijkstraSP = new DijkstraSP(edgeWeightedDigraphWithNewWeights, source);

                for(int target = 0; target < edgeWeightedDigraph.vertices(); target++) {
                    double realShortestPathDistance = dijkstraSP.distTo(target) - newWeight[source] + newWeight[target];
                    distances[source][target] = realShortestPathDistance;

                    DirectedEdge currentEdge = dijkstraSP.edgeTo(target);

                    if (currentEdge == null) {
                        continue;
                    }

                    int vertexFrom = currentEdge.from();
                    int vertexTo = currentEdge.to();
                    double realWeight = currentEdge.weight() - newWeight[vertexFrom] + newWeight[vertexTo];

                    DirectedEdge realEdgeTo = new DirectedEdge(vertexFrom, vertexTo, realWeight);
                    edgeTo[source][target] = realEdgeTo;
                }
            }
        }

        @Override
        public Iterable<DirectedEdge> path(int source, int target) {
            if (!hasPathTo(source, target)) {
                return null;
            }

            Stack<DirectedEdge> path = new Stack<>();
            for(DirectedEdge edge = edgeTo[source][target]; edge != null; edge = edgeTo[source][edge.from()]) {
                path.push(edge);
            }

            return path;
        }

        @Override
        public double dist(int source, int target) {
            return distances[source][target];
        }

        @Override
        public boolean hasPathTo(int source, int target) {
            return distances[source][target] != Double.POSITIVE_INFINITY;
        }
    }

    public static void main(String[] args) {
        Exercise30_AllPairsShortestPathsDigraphsWithoutNegativeCycles exercise30_allPairsShortestPathsDigraphsWithoutNegativeCycles =
                new Exercise30_AllPairsShortestPathsDigraphsWithoutNegativeCycles();

        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(6);
        edgeWeightedDigraph.addEdge(new DirectedEdge(0, 1, -2));
        edgeWeightedDigraph.addEdge(new DirectedEdge(1, 2, -1));
        edgeWeightedDigraph.addEdge(new DirectedEdge(2, 0, 4));
        edgeWeightedDigraph.addEdge(new DirectedEdge(2, 3, -3));
        edgeWeightedDigraph.addEdge(new DirectedEdge(2, 4, 2));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 3, -4));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 4, 1));

        AllPairsShortestPathsDigraphsWithoutNegativeCycles allPairsShortestPathsDigraphsWithoutNegativeCycles
                = exercise30_allPairsShortestPathsDigraphsWithoutNegativeCycles
                .new AllPairsShortestPathsDigraphsWithoutNegativeCycles(edgeWeightedDigraph);

        double[][] expectedDistances = {
                {0, -2, -3, -6, 1, Double.POSITIVE_INFINITY},
                {3, 0, -1, -4, 1, Double.POSITIVE_INFINITY},
                {4, 2, 0, -3, 2, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
                        0, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
                        Double.POSITIVE_INFINITY, 0, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
                        -4, 1, 0}
        };

        for(int source = 0; source < edgeWeightedDigraph.vertices(); source++) {
            for(int target = 0; target < edgeWeightedDigraph.vertices(); target++) {
                StdOut.println("Distance from " + source + " to " + target + ": " +
                        allPairsShortestPathsDigraphsWithoutNegativeCycles.dist(source, target)
                        + " Expected: " + expectedDistances[source][target]);
            }
        }

        StdOut.println();

        for(int source = 0; source < edgeWeightedDigraph.vertices(); source++) {
            for(int target = 0; target < edgeWeightedDigraph.vertices(); target++) {
                StdOut.print("Shortest path from " + source + " to " + target + ": ");

                if (!allPairsShortestPathsDigraphsWithoutNegativeCycles.hasPathTo(source, target)) {
                    StdOut.println("No path exists");
                    continue;
                }

                for(DirectedEdge edge : allPairsShortestPathsDigraphsWithoutNegativeCycles.path(source, target)) {
                    StdOut.print(edge.from() + "->" + edge.to() + " (" + edge.weight() + ") ");
                }

                StdOut.println();
            }
        }

        StdOut.println("\nExpected:");
        StdOut.println("Shortest path from 0 to 0: \n" +
                "Shortest path from 0 to 1: 0->1 (-2.0) \n" +
                "Shortest path from 0 to 2: 0->1 (-2.0) 1->2 (-1.0) \n" +
                "Shortest path from 0 to 3: 0->1 (-2.0) 1->2 (-1.0) 2->3 (-3.0) \n" +
                "Shortest path from 0 to 4: 0->1 (-2.0) 1->2 (-1.0) 2->4 (2.0) \n" +
                "Shortest path from 0 to 5: No path exists\n" +
                "Shortest path from 1 to 0: 1->2 (-1.0) 2->0 (4.0) \n" +
                "Shortest path from 1 to 1: \n" +
                "Shortest path from 1 to 2: 1->2 (-1.0) \n" +
                "Shortest path from 1 to 3: 1->2 (-1.0) 2->3 (-3.0) \n" +
                "Shortest path from 1 to 4: 1->2 (-1.0) 2->4 (2.0) \n" +
                "Shortest path from 1 to 5: No path exists\n" +
                "Shortest path from 2 to 0: 2->0 (4.0) \n" +
                "Shortest path from 2 to 1: 2->0 (4.0) 0->1 (-2.0) \n" +
                "Shortest path from 2 to 2: \n" +
                "Shortest path from 2 to 3: 2->3 (-3.0) \n" +
                "Shortest path from 2 to 4: 2->4 (2.0) \n" +
                "Shortest path from 2 to 5: No path exists\n" +
                "Shortest path from 3 to 0: No path exists\n" +
                "Shortest path from 3 to 1: No path exists\n" +
                "Shortest path from 3 to 2: No path exists\n" +
                "Shortest path from 3 to 3: \n" +
                "Shortest path from 3 to 4: No path exists\n" +
                "Shortest path from 3 to 5: No path exists\n" +
                "Shortest path from 4 to 0: No path exists\n" +
                "Shortest path from 4 to 1: No path exists\n" +
                "Shortest path from 4 to 2: No path exists\n" +
                "Shortest path from 4 to 3: No path exists\n" +
                "Shortest path from 4 to 4: \n" +
                "Shortest path from 4 to 5: No path exists\n" +
                "Shortest path from 5 to 0: No path exists\n" +
                "Shortest path from 5 to 1: No path exists\n" +
                "Shortest path from 5 to 2: No path exists\n" +
                "Shortest path from 5 to 3: 5->3 (-4.0) \n" +
                "Shortest path from 5 to 4: 5->4 (1.0) \n" +
                "Shortest path from 5 to 5: ");
    }

}
