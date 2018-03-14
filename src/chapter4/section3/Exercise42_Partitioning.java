package chapter4.section3;

import chapter1.section3.Queue;
import chapter1.section3.Stack;
import chapter1.section5.UnionFind;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 14/11/17.
 */
// Based on the work of Rodrigo Paredes and Gonzalo Navarro, available at
// https://www.researchgate.net/publication/242328466_Optimal_Incremental_Sorting
public class Exercise42_Partitioning {

    // O(E + V lg^2 V) average time
    public class KruskalMSTPartitioning {

        private Queue<Edge> minimumSpanningTree;
        private double weight;

        public KruskalMSTPartitioning(EdgeWeightedGraph edgeWeightedGraph) {
            minimumSpanningTree = new Queue<>();

            // Total number of edges, not counting self-loops
            int totalNumberOfEdges = 0;

            // First pass required to count the number of edges (EdgeWeightedGraph.edgesCount() also counts self-loops, which
            // are not returned in EdgeWeightedGraph.edge())
            for(Edge edge : edgeWeightedGraph.edges()) {
                totalNumberOfEdges++;
            }

            Edge[] edges = new Edge[totalNumberOfEdges];
            int edgesIndex = 0;

            for(Edge edge : edgeWeightedGraph.edges()) {
                edges[edgesIndex++] = edge;
            }

            UnionFind unionFind = new UnionFind(edgeWeightedGraph.vertices());
            int kthLightestEdgeToFind = 0;

            Stack<Integer> pivots = new Stack<>();
            pivots.push(totalNumberOfEdges);

            while (minimumSpanningTree.size() < edgeWeightedGraph.vertices() - 1) {
                Edge edge = (Edge) incrementalQuickSelect(edges, kthLightestEdgeToFind, pivots);
                kthLightestEdgeToFind++;

                int vertex1 = edge.either();
                int vertex2 = edge.other(vertex1);

                // Ignore ineligible edges
                if (unionFind.connected(vertex1, vertex2)) {
                    continue;
                }

                unionFind.union(vertex1, vertex2);
                minimumSpanningTree.enqueue(edge); // Add edge to the minimum spanning tree

                weight += edge.weight();
            }
        }

        private Comparable incrementalQuickSelect(Comparable[] array, int indexToSearch, Stack<Integer> pivots) {
            int smallestCorrectPivotIndex = pivots.peek();

            // Pre-condition: indexToSearch <= smallestCorrectPivotIndex
            if (indexToSearch > smallestCorrectPivotIndex) {
                throw new IllegalArgumentException("Searching for an element in the wrong range");
            }

            if (indexToSearch == smallestCorrectPivotIndex) {
                pivots.pop();
                return array[indexToSearch];
            }

            int pivotIndex = StdRandom.uniform(indexToSearch, smallestCorrectPivotIndex);
            int correctPivotIndexAfterPartition = partition(array, pivotIndex, indexToSearch, smallestCorrectPivotIndex - 1);

            // Invariant: array[0] <= ... <= array[indexToSearch - 1] <= array[indexToSearch, correctPivotIndexAfterPartition - 1]
            // <= array[correctPivotIndexAfterPartition] <= array[correctPivotIndexAfterPartition + 1, pivots.peek() - 1]
            // <= array[pivots.peek(), array.length - 1]
            pivots.push(correctPivotIndexAfterPartition);
            return incrementalQuickSelect(array, indexToSearch, pivots);
        }

        private int partition(Comparable[] array, int pivotIndex, int low, int high) {
            if (low == high) {
                return low;
            }

            Comparable pivot = array[pivotIndex];

            ArrayUtil.exchange(array, low, pivotIndex);

            int lowIndex = low;
            int highIndex = high + 1;

            while (true) {
                while (ArrayUtil.less(array[++lowIndex], pivot)) {
                    if (lowIndex == high) {
                        break;
                    }
                }

                while (ArrayUtil.less(pivot, array[--highIndex])) {
                    if (highIndex == low) {
                        break;
                    }
                }

                if (lowIndex >= highIndex) {
                    break;
                }

                ArrayUtil.exchange(array, lowIndex, highIndex);
            }

            // Place pivot in the correct place
            ArrayUtil.exchange(array, low, highIndex);
            return highIndex;
        }

        public Iterable<Edge> edges() {
            return minimumSpanningTree;
        }

        public double lazyWeight() {
            double weight = 0;

            for(Edge edge : edges()) {
                weight += edge.weight();
            }

            return weight;
        }

        public double eagerWeight() {
            return weight;
        }
    }

    public static void main(String[] args) {
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(5);
        edgeWeightedGraph.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraph.addEdge(new Edge(0, 3, 0.5));
        edgeWeightedGraph.addEdge(new Edge(1, 2, 0.12));
        edgeWeightedGraph.addEdge(new Edge(1, 4, 0.91));
        edgeWeightedGraph.addEdge(new Edge(2, 3, 0.72));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.8));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.82));
        edgeWeightedGraph.addEdge(new Edge(4, 4, 0.1));

        Exercise42_Partitioning.KruskalMSTPartitioning kruskalMSTPartitioning =
                new Exercise42_Partitioning().new KruskalMSTPartitioning(edgeWeightedGraph);

        for(Edge edge : kruskalMSTPartitioning.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "1-2 0.12000\n" +
                "0-1 0.42000\n" +
                "0-3 0.50000\n" +
                "3-4 0.80000");
    }

}
