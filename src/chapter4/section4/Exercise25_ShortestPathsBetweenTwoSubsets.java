package chapter4.section4;

import chapter1.section3.Stack;
import chapter2.section4.IndexMinPriorityQueue;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 07/12/17.
 */
public class Exercise25_ShortestPathsBetweenTwoSubsets {

    public class DijkstraSPTwoSubsets {

        private DirectedEdge[] edgeTo;  // last edge on path to vertex
        private double[] distTo;        // length of path to vertex
        private IndexMinPriorityQueue<Double> priorityQueue;
        private int closestVertexInSubsetT;

        DijkstraSPTwoSubsets(EdgeWeightedDigraph edgeWeightedDigraph, HashSet<Integer> subsetS, HashSet<Integer> subsetT) {
            edgeTo = new DirectedEdge[edgeWeightedDigraph.vertices()];
            distTo = new double[edgeWeightedDigraph.vertices()];
            priorityQueue = new IndexMinPriorityQueue<>(edgeWeightedDigraph.vertices());
            closestVertexInSubsetT = -1;

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }

            for(int source : subsetS.keys()) {
                distTo[source] = 0;
                priorityQueue.insert(source, 0.0);
            }

            while (!priorityQueue.isEmpty()) {
                int vertexToRelax = priorityQueue.deleteMin();
                relax(edgeWeightedDigraph, vertexToRelax);

                if (subsetT.contains(vertexToRelax)) {
                    closestVertexInSubsetT = vertexToRelax;
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

        public Iterable<DirectedEdge> getShortestPathFromStoT() {
            if (closestVertexInSubsetT == -1) {
                return null;
            }

            Stack<DirectedEdge> path = new Stack<>();
            for(DirectedEdge edge = edgeTo[closestVertexInSubsetT]; edge != null; edge = edgeTo[edge.from()]) {
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

        HashSet<Integer> subsetS = new HashSet<>();
        subsetS.add(0);
        subsetS.add(1);

        HashSet<Integer> subsetT = new HashSet<>();
        subsetT.add(5);
        subsetT.add(6);

        DijkstraSPTwoSubsets dijkstraSPTwoSubsets =
                new Exercise25_ShortestPathsBetweenTwoSubsets().new DijkstraSPTwoSubsets(edgeWeightedDigraph, subsetS, subsetT);
        StdOut.print("Shortest path from subset S to subset T: ");

        for(DirectedEdge edge : dijkstraSPTwoSubsets.getShortestPathFromStoT()) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected: 0->4 4->5");
    }

}
