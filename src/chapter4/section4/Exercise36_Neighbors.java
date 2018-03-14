package chapter4.section4;

import chapter1.section3.Stack;
import chapter2.section4.IndexMinPriorityQueue;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 17/12/17.
 */
// It seems that the problem statement is not very clear regarding the expected running time of this problem.
    // The problem of finding all the vertices of distance equal to or less than d from a source vertex can be reduced to
    // the general shortest-paths problem:
    // 1- Iterate through all edges and sum their distance. Let's call the total distance T.
    // 2- Set d = T as a parameter for this problem.
    // It will now have to check the distance from the source vertex to all other vertices.
    // In order to check such distances, the algorithm will also have to compute the shortest-paths from the source vertex
    // to all the other vertices.
    // Therefore, this problem can be reduced to the general shortest-paths problem.
    // Currently the best runtime complexity for the general shortest-paths problem is O(E + V lg(V)).
    // So a linear runtime complexity for this problem does not seem possible.

    // The "running time of your method should be proportional to the number of vertices and edges in the subgraph
    // induced by those vertices and the edges incident to them, plus V (to initialize data structures)" statement
    // will be considered as "the running time of your method should be related to V' and E',
    // which are the vertices and edges in the subgraph induced by the vertices of distance equal to or less than d
    // from a source vertex and the edges incident to them, respectively, plus V (to initialize data structures)" instead.

    // This algorithm runs in O(E' lg V') + V
public class Exercise36_Neighbors {

    public class DijkstraSPMaxDistance {

        private DirectedEdge[] edgeTo;  // last edge on path to vertex
        private double[] distTo;        // length of path to vertex
        private IndexMinPriorityQueue<Double> priorityQueue;

        public DijkstraSPMaxDistance(EdgeWeightedDigraph edgeWeightedDigraph, int source, int maxDistance) {
            edgeTo = new DirectedEdge[edgeWeightedDigraph.vertices()];
            distTo = new double[edgeWeightedDigraph.vertices()];
            priorityQueue = new IndexMinPriorityQueue<>(edgeWeightedDigraph.vertices());

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }
            distTo[source] = 0;
            priorityQueue.insert(source, 0.0);

            while (!priorityQueue.isEmpty()) {
                relax(edgeWeightedDigraph, priorityQueue.deleteMin(), maxDistance);
            }
        }

        private void relax(EdgeWeightedDigraph edgeWeightedDigraph, int vertex, int maxDistance) {
            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();

                if (distTo[neighbor] > distTo[vertex] + edge.weight()
                        && distTo[vertex] + edge.weight() <= maxDistance) {
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

        public double distTo(int vertex) {
            return distTo[vertex];
        }

        public DirectedEdge edgeTo(int vertex) {
            return edgeTo[vertex];
        }

        public boolean hasPathTo(int vertex) {
            return distTo[vertex] < Double.POSITIVE_INFINITY;
        }

        public HashSet<Integer> verticesWithinMaxDistance() {
            HashSet<Integer> verticesWithinMaxDistance = new HashSet<>();

            for(int vertex = 0; vertex < distTo.length; vertex++) {
                if (hasPathTo(vertex)) {
                    verticesWithinMaxDistance.add(vertex);
                }
            }

            return verticesWithinMaxDistance;
        }

        public Iterable<DirectedEdge> pathTo(int vertex) {
            if (!hasPathTo(vertex)) {
                return null;
            }

            Stack<DirectedEdge> path = new Stack<>();
            for(DirectedEdge edge = edgeTo[vertex]; edge != null; edge = edgeTo[edge.from()]) {
                path.push(edge);
            }

            return path;
        }

    }

    public static void main(String[] args) {
        Exercise36_Neighbors neighbors = new Exercise36_Neighbors();

        EdgeWeightedDigraph edgeWeightedDigraph1 = new EdgeWeightedDigraph(4);
        edgeWeightedDigraph1.addEdge(new DirectedEdge(0, 1, 20));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(0, 2, 5));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(2, 1, 1));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(1, 3, 10));

        int maxDistance1 = 20;

        DijkstraSPMaxDistance dijkstraSPMaxDistance1 =
                neighbors.new DijkstraSPMaxDistance(edgeWeightedDigraph1, 0, maxDistance1);

        StdOut.println("Vertices within distance 20 from digraph 1:");

        for(int vertex : dijkstraSPMaxDistance1.verticesWithinMaxDistance().keys()) {
            StdOut.print(vertex + " ");
        }
        StdOut.println("\nExpected: 0 1 2 3");

        EdgeWeightedDigraph edgeWeightedDigraph2 = new EdgeWeightedDigraph(8);
        edgeWeightedDigraph2.addEdge(new DirectedEdge(0, 1, 5));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(1, 3, 2));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(3, 5, 3));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(5, 6, 1));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(0, 2, 9));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(2, 3, 2));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(2, 4, 3));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(4, 7, 1));

        int maxDistance2 = 10;

        DijkstraSPMaxDistance dijkstraSPMaxDistance2 =
                neighbors.new DijkstraSPMaxDistance(edgeWeightedDigraph2, 0, maxDistance2);

        StdOut.println("\nVertices within distance 10 from digraph 2:");

        for(int vertex : dijkstraSPMaxDistance2.verticesWithinMaxDistance().keys()) {
            StdOut.print(vertex + " ");
        }
        StdOut.println("\nExpected: 0 1 2 3 5");
    }

}
