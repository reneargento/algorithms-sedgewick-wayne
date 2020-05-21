package chapter4.section4;

import chapter1.section3.Stack;
import chapter2.section4.IndexMinPriorityQueue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/12/17.
 */
// This exercise description asks us to use the same priority queue for both shortest-path-trees.
// This requires a lot of extra bookkeeping.
// If two priority queues could be used, the code could be significantly simplified.

// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for fixing a bug when updating the priority queue.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/152
public class Exercise41_BidirectionalSearch {

    public class DijkstraSPSourceSinkBidirectional {

        private DirectedEdge[] edgeToSource;  // last edge on path from source to vertex
        private double[] distToSource;        // length of path from source to vertex
        private boolean[] relaxedFromSource;  // vertices that have been relaxed in SPT rooted at source

        private DirectedEdge[] edgeToTarget;  // last edge on inverse path from target to vertex
        private double[] distToTarget;        // length of inverse path from target to vertex
        private boolean[] relaxedFromTarget;  // vertices that have been relaxed in SPT rooted at target

        private IndexMinPriorityQueue<Double> priorityQueue;

        public DijkstraSPSourceSinkBidirectional(EdgeWeightedDigraph edgeWeightedDigraph, int source, int target) {
            edgeToSource = new DirectedEdge[edgeWeightedDigraph.vertices()];
            distToSource = new double[edgeWeightedDigraph.vertices()];
            relaxedFromSource = new boolean[edgeWeightedDigraph.vertices()];

            edgeToTarget = new DirectedEdge[edgeWeightedDigraph.vertices()];
            distToTarget = new double[edgeWeightedDigraph.vertices()];
            relaxedFromTarget = new boolean[edgeWeightedDigraph.vertices()];

            // Reverse digraph will be used to create the shortest-path-tree from the target vertex
            EdgeWeightedDigraph reverseDigraph = new EdgeWeightedDigraph(edgeWeightedDigraph.vertices());

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                    reverseDigraph.addEdge(new DirectedEdge(edge.to(), edge.from(), edge.weight()));
                }
            }

            priorityQueue = new IndexMinPriorityQueue<>(edgeWeightedDigraph.vertices() * 2);

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                distToSource[vertex] = Double.POSITIVE_INFINITY;
                distToTarget[vertex] = Double.POSITIVE_INFINITY;
            }
            distToSource[source] = 0;
            distToTarget[target] = 0;
            priorityQueue.insert(source, 0.0);

            // When inserting vertices from the target SPT in the priority queue use a different index to avoid
            // collision with vertices from the source SPT
            priorityQueue.insert(target + edgeWeightedDigraph.vertices(), 0.0);

            while (!priorityQueue.isEmpty()) {
                int nextVertexToRelax = priorityQueue.deleteMin();

                relax(edgeWeightedDigraph, reverseDigraph, nextVertexToRelax);

                // If it is a vertex from the target SPT, find its original index
                if (nextVertexToRelax >= edgeWeightedDigraph.vertices()) {
                    nextVertexToRelax -= edgeWeightedDigraph.vertices();
                }

                // Combine shortest paths if there are shortest paths from source to vertex and from vertex to target
                if (relaxedFromSource[nextVertexToRelax] && relaxedFromTarget[nextVertexToRelax]) {
                    computeShortestPath(edgeWeightedDigraph);
                    break;
                }
            }
        }

        // Shortest-path distance calculation based on https://courses.csail.mit.edu/6.006/spring11/rec/rec16.pdf
        private void computeShortestPath(EdgeWeightedDigraph edgeWeightedDigraph) {
            double shortestDistance = Double.POSITIVE_INFINITY;
            int intermediateVertex = -1;

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                if (distToSource[vertex] + distToTarget[vertex] < shortestDistance) {
                    shortestDistance = distToSource[vertex] + distToTarget[vertex];
                    intermediateVertex = vertex;
                }
            }

            for(DirectedEdge edge = edgeToTarget[intermediateVertex]; edge != null; edge = edgeToTarget[edge.from()]) {
                distToSource[edge.from()] = distToSource[edge.to()] + edge.weight();
                edgeToSource[edge.from()] = new DirectedEdge(edge.to(), edge.from(), edge.weight());
            }
        }

        private void relax(EdgeWeightedDigraph edgeWeightedDigraph, EdgeWeightedDigraph reverseDigraph, int vertex) {

            // Shortest-path-tree from source
            if (vertex < edgeWeightedDigraph.vertices()) {
                for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                    int neighbor = edge.to();

                    if (distToSource[neighbor] > distToSource[vertex] + edge.weight()) {
                        distToSource[neighbor] = distToSource[vertex] + edge.weight();
                        edgeToSource[neighbor] = edge;

                        if (priorityQueue.contains(neighbor)) {
                            priorityQueue.decreaseKey(neighbor, distToSource[neighbor]);
                        } else {
                            priorityQueue.insert(neighbor, distToSource[neighbor]);
                        }
                    }
                }

                relaxedFromSource[vertex] = true;
            } else {
                // Shortest-path-tree from target
                int originalVertexId = vertex - edgeWeightedDigraph.vertices();

                for(DirectedEdge edge : reverseDigraph.adjacent(originalVertexId)) {
                    int neighbor = edge.to();

                    if (distToTarget[neighbor] > distToTarget[originalVertexId] + edge.weight()) {
                        distToTarget[neighbor] = distToTarget[originalVertexId] + edge.weight();
                        edgeToTarget[neighbor] = edge;

                        int adjustedNeighborIndex = neighbor + edgeWeightedDigraph.vertices();
                        if (priorityQueue.contains(adjustedNeighborIndex)) {
                            priorityQueue.decreaseKey(adjustedNeighborIndex, distToTarget[neighbor]);
                        } else {
                            priorityQueue.insert(adjustedNeighborIndex, distToTarget[neighbor]);
                        }
                    }
                }

                relaxedFromTarget[originalVertexId] = true;
            }
        }

        public double distTo(int vertex) {
            return distToSource[vertex];
        }

        public DirectedEdge edgeTo(int vertex) {
            return edgeToSource[vertex];
        }

        public boolean hasPathTo(int vertex) {
            return distToSource[vertex] < Double.POSITIVE_INFINITY;
        }

        public Iterable<DirectedEdge> pathTo(int vertex) {
            if (!hasPathTo(vertex)) {
                return null;
            }

            Stack<DirectedEdge> path = new Stack<>();
            for(DirectedEdge edge = edgeToSource[vertex]; edge != null; edge = edgeToSource[edge.from()]) {
                path.push(edge);
            }

            return path;
        }

    }

    public static void main(String[] args) {
        Exercise41_BidirectionalSearch bidirectionalSearch = new Exercise41_BidirectionalSearch();

        // tinyEWD.txt
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

        int source = 0;
        int target = 6;

        DijkstraSPSourceSinkBidirectional dijkstraSPSourceSinkBidirectional =
                bidirectionalSearch.new DijkstraSPSourceSinkBidirectional(edgeWeightedDigraph, source, target);

        StdOut.println("Shortest path from 0 to 6:");

        if (dijkstraSPSourceSinkBidirectional.hasPathTo(target)) {
            StdOut.printf("%d to %d (%.2f)  ", source, target, dijkstraSPSourceSinkBidirectional.distTo(target));

            for (DirectedEdge edge : dijkstraSPSourceSinkBidirectional.pathTo(target)) {
                StdOut.print(edge + "   ");
            }
            StdOut.println();
        } else {
            StdOut.printf("%d to %d         no path\n", source, target);
        }

        StdOut.println("Expected:");
        StdOut.println("0 to 6 (1.51)  0->2 0.26   2->7 0.34   7->3 0.39   3->6 0.52");

        // This digraph would cause a collision between vertices of different SPTs in the priority queue and would
        // lead to incorrect shortest-paths if extra bookkeeping were not used
        EdgeWeightedDigraph edgeWeightedDigraph2 = new EdgeWeightedDigraph(8);
        edgeWeightedDigraph2.addEdge(new DirectedEdge(0, 1, 1));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(0, 2, 4));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(1, 3, 5));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(2, 7, 1));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(3, 4, 3));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(4, 6, 1));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(5, 6, 5));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(7, 4, 2));

        int source2 = 0;
        int target2 = 6;

        DijkstraSPSourceSinkBidirectional dijkstraSPSourceSinkBidirectional2 =
                bidirectionalSearch.new DijkstraSPSourceSinkBidirectional(edgeWeightedDigraph2, source2, target2);

        StdOut.println("\nShortest path from 0 to 6:");

        if (dijkstraSPSourceSinkBidirectional2.hasPathTo(target2)) {
            StdOut.printf("%d to %d (%.2f)  ", source2, target2, dijkstraSPSourceSinkBidirectional2.distTo(target2));

            for (DirectedEdge edge : dijkstraSPSourceSinkBidirectional2.pathTo(target2)) {
                StdOut.print(edge + "   ");
            }
            StdOut.println();
        } else {
            StdOut.printf("%d to %d         no path\n", source2, target2);
        }

        StdOut.println("Expected:");
        StdOut.println("0 to 6 (8.00)  0->2 4.00   2->7 1.00   7->4 2.00   4->6 1.00");

        EdgeWeightedDigraph edgeWeightedDigraph3 = new EdgeWeightedDigraph(5);
        edgeWeightedDigraph3.addEdge(new DirectedEdge(0, 1, 3));
        edgeWeightedDigraph3.addEdge(new DirectedEdge(0, 2, 5));
        edgeWeightedDigraph3.addEdge(new DirectedEdge(1, 3, 3));
        edgeWeightedDigraph3.addEdge(new DirectedEdge(3, 4, 3));
        edgeWeightedDigraph3.addEdge(new DirectedEdge(2, 4, 5));

        int source3 = 0;
        int target3 = 4;

        DijkstraSPSourceSinkBidirectional dijkstraSPSourceSinkBidirectional3 =
                bidirectionalSearch.new DijkstraSPSourceSinkBidirectional(edgeWeightedDigraph3, source3, target3);

        StdOut.println("\nShortest path from 0 to 4:");

        if (dijkstraSPSourceSinkBidirectional3.hasPathTo(target3)) {
            StdOut.printf("%d to %d (%.2f)  ", source3, target3, dijkstraSPSourceSinkBidirectional3.distTo(target3));

            for (DirectedEdge edge : dijkstraSPSourceSinkBidirectional3.pathTo(target3)) {
                StdOut.print(edge + "   ");
            }
            StdOut.println();
        } else {
            StdOut.printf("%d to %d         no path\n", source3, target3);
        }

        StdOut.println("Expected:");
        StdOut.println("0 to 4 (9.00)  0->1 3.00   1->3 3.00   3->4 3.00");
    }

}
