package chapter4.section4;

import chapter1.section3.Queue;
import chapter4.section3.Edge;
import chapter4.section3.EdgeWeightedGraph;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 09/12/17.
 */
public class Exercise31_AllPairsShortestPathsOnALine {

    public class AllPairsShortestPathsOnALine {

        private double[] distanceFromSource;

        AllPairsShortestPathsOnALine(EdgeWeightedGraph edgeWeightedGraph) {

            boolean isLineGraph = true;
            int numberOfVerticesWithDegree1 = 0;
            int sourceVertex = -1;

            // Find one of the two sources
            for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
                int outdegree = 0;

                for(Edge edge : edgeWeightedGraph.adjacent(vertex)) {
                    if (edge.weight() < 0) {
                        throw new IllegalArgumentException("Edge weights cannot be negative");
                    }

                    outdegree++;
                }

                if (outdegree == 1) {
                    if (sourceVertex == -1) {
                        sourceVertex = vertex;
                    }

                    numberOfVerticesWithDegree1++;
                } else if (outdegree == 0 || outdegree > 2) {
                    isLineGraph = false;
                    break;
                }
            }

            if (numberOfVerticesWithDegree1 != 2) {
                isLineGraph = false;
            }

            if (!isLineGraph) {
                throw new IllegalArgumentException("Graph is not a line graph");
            }

            distanceFromSource = new double[edgeWeightedGraph.vertices()];
            boolean[] visited = new boolean[edgeWeightedGraph.vertices()];

            for(int vertex = 0; vertex < distanceFromSource.length; vertex++) {
                distanceFromSource[vertex] = Double.POSITIVE_INFINITY;
            }

            // Do a breadth-first-search to compute the distances from the source in O(V + E)
            Queue<Integer> queue = new Queue<>();
            queue.enqueue(sourceVertex);
            visited[sourceVertex] = true;

            distanceFromSource[sourceVertex] = 0;

            while (!queue.isEmpty()) {
                int currentVertex = queue.dequeue();

                for(Edge edge : edgeWeightedGraph.adjacent(currentVertex)) {
                    int neighbor = edge.other(currentVertex);

                    if (!visited[neighbor]) {
                        distanceFromSource[neighbor] = distanceFromSource[currentVertex] + edge.weight();
                        queue.enqueue(neighbor);
                        visited[neighbor] = true;
                    }
                }
            }
        }

        public double dist(int source, int target) {
            return Math.abs(distanceFromSource[source] - distanceFromSource[target]);
        }
    }

    public static void main(String[] args) {
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(5);
        edgeWeightedGraph.addEdge(new Edge(0, 1, 2));
        edgeWeightedGraph.addEdge(new Edge(1, 2, 3));
        edgeWeightedGraph.addEdge(new Edge(2, 3, 4));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 1));

        AllPairsShortestPathsOnALine allPairsShortestPathsOnALine =
                new Exercise31_AllPairsShortestPathsOnALine().new AllPairsShortestPathsOnALine(edgeWeightedGraph);

        double[][] expectedDistances = {
                {0, 2, 5, 9, 10},
                {2, 0, 3, 7, 8},
                {5, 3, 0, 4, 5},
                {9, 7, 4, 0, 1},
                {10, 8, 5, 1, 0}
        };

        for(int source = 0; source < edgeWeightedGraph.vertices(); source++) {
            for(int target = 0; target < edgeWeightedGraph.vertices(); target++) {
                StdOut.println("Distance from " + source + " to " + target + ": " +
                        allPairsShortestPathsOnALine.dist(source, target)
                        + " Expected: " + expectedDistances[source][target]);
            }
        }
    }

}
