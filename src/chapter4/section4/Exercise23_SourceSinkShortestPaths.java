package chapter4.section4;

import chapter1.section3.Stack;
import chapter2.section4.IndexMinPriorityQueue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 06/12/17.
 */
public class Exercise23_SourceSinkShortestPaths {

    public interface DijkstraSPSourceSinkAPI {
        double distToTarget();
        boolean hasPathToTarget();
        Iterable<DirectedEdge> pathToTarget();
    }

    public class DijkstraSPSourceSink implements DijkstraSPSourceSinkAPI {

        private DirectedEdge[] edgeTo;  // last edge on path to vertex
        private double[] distTo;        // length of path to vertex
        private IndexMinPriorityQueue<Double> priorityQueue;
        private int target;

        public DijkstraSPSourceSink(EdgeWeightedDigraph edgeWeightedDigraph, int source, int target) {
            edgeTo = new DirectedEdge[edgeWeightedDigraph.vertices()];
            distTo = new double[edgeWeightedDigraph.vertices()];
            priorityQueue = new IndexMinPriorityQueue<>(edgeWeightedDigraph.vertices());
            this.target = target;

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }
            distTo[source] = 0;
            priorityQueue.insert(source, 0.0);

            while (!priorityQueue.isEmpty()) {
                int vertexToRelax = priorityQueue.deleteMin();
                relax(edgeWeightedDigraph, vertexToRelax);

                if (vertexToRelax == target) {
                    break;
                }
            }
        }

        private void relax(EdgeWeightedDigraph edgeWeightedDigraph, int vertex) {
            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();

                if (distTo[neighbor] > distTo[vertex] + edge.weight()) {
                    distTo[neighbor] = distTo[vertex] + edge.weight();
                    edgeTo[neighbor] = edge;

                    if (priorityQueue.contains(neighbor)) {
                        priorityQueue.decreaseKey(neighbor, distTo[neighbor]);
                    } else {
                        priorityQueue.insert(neighbor, distTo[neighbor]);
                    }
                }
            }
        }

        @Override
        public double distToTarget() {
            return distTo[target];
        }

        @Override
        public boolean hasPathToTarget() {
            return distTo[target] < Double.POSITIVE_INFINITY;
        }

        @Override
        public Iterable<DirectedEdge> pathToTarget() {
            if (!hasPathToTarget()) {
                return null;
            }

            Stack<DirectedEdge> path = new Stack<>();
            for(DirectedEdge edge = edgeTo[target]; edge != null; edge = edgeTo[edge.from()]) {
                path.push(edge);
            }

            return path;
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

        DijkstraSPSourceSink dijkstraSPSourceSink1 =
                new Exercise23_SourceSinkShortestPaths().new DijkstraSPSourceSink(edgeWeightedDigraph, 0, 2);

        StdOut.println("Distance to target 2: " + dijkstraSPSourceSink1.distToTarget() + " Expected: 0.26");
        StdOut.println("Has path to target 2: " + dijkstraSPSourceSink1.hasPathToTarget() + " Expected: true");

        StdOut.print("Path to target 2: ");

        for(DirectedEdge edge : dijkstraSPSourceSink1.pathToTarget()) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected: 0->2");

        DijkstraSPSourceSink dijkstraSPSourceSink2 =
                new Exercise23_SourceSinkShortestPaths().new DijkstraSPSourceSink(edgeWeightedDigraph, 4, 6);

        StdOut.println("\nDistance to target 6: " + dijkstraSPSourceSink2.distToTarget() + " Expected: 1.28");
        StdOut.println("Has path to target 6: " + dijkstraSPSourceSink2.hasPathToTarget() + " Expected: true");

        StdOut.print("Path to target 6: ");

        for(DirectedEdge edge : dijkstraSPSourceSink2.pathToTarget()) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected: 4->7 7->3 3->6");

    }

}
