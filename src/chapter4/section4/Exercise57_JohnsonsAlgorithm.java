package chapter4.section4;

import chapter1.section3.Stack;
import chapter2.section4.IndexMinDWayPriorityQueue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 31/12/17.
 */
public class Exercise57_JohnsonsAlgorithm {

    public class DijkstraSPDWayHeap {

        private DirectedEdge[] edgeTo;  // last edge on path to vertex
        private double[] distTo;        // length of path to vertex
        private IndexMinDWayPriorityQueue<Double> priorityQueue;

        public DijkstraSPDWayHeap(EdgeWeightedDigraphInterface edgeWeightedDigraph, int source,
                                  int numberOfChildrenPerNodeInHeap) {
            edgeTo = new DirectedEdge[edgeWeightedDigraph.vertices()];
            distTo = new double[edgeWeightedDigraph.vertices()];
            priorityQueue = new IndexMinDWayPriorityQueue<>(edgeWeightedDigraph.vertices(), numberOfChildrenPerNodeInHeap);

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }
            distTo[source] = 0;
            priorityQueue.insert(source, 0.0);

            while (!priorityQueue.isEmpty()) {
                relax(edgeWeightedDigraph, priorityQueue.deleteMin());
            }
        }

        private void relax(EdgeWeightedDigraphInterface edgeWeightedDigraph, int vertex) {
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

        public double distTo(int vertex) {
            return distTo[vertex];
        }

        public DirectedEdge edgeTo(int vertex) {
            return edgeTo[vertex];
        }

        public boolean hasPathTo(int vertex) {
            return distTo[vertex] < Double.POSITIVE_INFINITY;
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

    private void generateDigraphsAndDoExperiments(int experiments, int uniformWeightDigraphVertices, int edges,
                                                  int euclideanDigraphVertices, int gridDigraphVertices,
                                                  int gridDigraphExtraEdges) {
        StdOut.printf("%49s %12s %10s %15s %12s\n", "Edge Weighted Digraph type | ", "Vertices | ",
                "Edges | ", "D-way heap | ", "Average time spent");

        String[] dWayHeaps = {"2-Way Heap", "3-Way Heap", "4-Way Heap", "5-Way Heap"};

        double[] totalTimeSpent = new double[dWayHeaps.length];

        // Digraph model 1: Random edge weighted digraphs with uniform weight distribution
        String digraphType = "Random digraph w/ uniform weight distribution";

        Exercise49_RandomSparseEdgeWeightedDigraphs.RandomEdgeWeightedDigraphs randomEdgeWeightedDigraphs =
                new Exercise49_RandomSparseEdgeWeightedDigraphs().new RandomEdgeWeightedDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedDigraphInterface randomEdgeWeightedDigraphUniformWeights =
                    randomEdgeWeightedDigraphs.erdosRenyiDigraphUniformWeights(uniformWeightDigraphVertices, edges);
            int randomSource = StdRandom.uniform(uniformWeightDigraphVertices);

            for(int dWayHeapType = 0; dWayHeapType < dWayHeaps.length; dWayHeapType++) {
                int dWayHeapChildrenNumber = dWayHeapType + 2;

                double timeSpent = doExperiment(randomEdgeWeightedDigraphUniformWeights, randomSource, dWayHeapChildrenNumber);
                totalTimeSpent[dWayHeapType] += timeSpent;
            }
        }

        computeAndPrintResults(totalTimeSpent, digraphType, dWayHeaps, experiments, uniformWeightDigraphVertices, edges);
        totalTimeSpent = new double[dWayHeaps.length];

        // Digraph model 2: Random edge weighted Euclidean digraphs
        digraphType = "Random Euclidean digraph";

        // Running the experiment on a digraph with all neighbors connected
        double radius = 1;

        Exercise50_RandomEuclideanEdgeWeightedDigraphs randomEuclideanEdgeWeightedDigraphs =
                new Exercise50_RandomEuclideanEdgeWeightedDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedDigraphInterface euclideanEdgeWeightedDigraph =
                    randomEuclideanEdgeWeightedDigraphs.randomEuclideanEdgeWeightedDigraph(euclideanDigraphVertices, radius);

            edges = euclideanEdgeWeightedDigraph.edgesCount();
            int randomSource = StdRandom.uniform(euclideanDigraphVertices);

            for(int dWayHeapType = 0; dWayHeapType < dWayHeaps.length; dWayHeapType++) {
                int dWayHeapChildrenNumber = dWayHeapType + 2;

                double timeSpent = doExperiment(euclideanEdgeWeightedDigraph, randomSource, dWayHeapChildrenNumber);
                totalTimeSpent[dWayHeapType] += timeSpent;
            }
        }

        computeAndPrintResults(totalTimeSpent, digraphType, dWayHeaps, experiments, euclideanDigraphVertices, edges);
        totalTimeSpent = new double[dWayHeaps.length];

        // Digraph model 3: Random edge weighted grid digraphs
        digraphType = "Random grid digraph";

        Exercise51_RandomGridEdgeWeightedDigraphs randomGridEdgeWeightedDigraphs =
                new Exercise51_RandomGridEdgeWeightedDigraphs();
        // It is not possible to be sure about the exact number of vertices that a grid digraph will have because
        // shrinking may occur. So fix the source node as 0, since vertex 0 will always be present.
        int sourceNode = 0;

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedDigraphInterface gridEdgeWeightedDigraph =
                    randomGridEdgeWeightedDigraphs.randomGridEdgeWeightedDigraph(gridDigraphVertices, gridDigraphExtraEdges);

            edges = gridEdgeWeightedDigraph.edgesCount();

            for(int dWayHeapType = 0; dWayHeapType < dWayHeaps.length; dWayHeapType++) {
                int dWayHeapChildrenNumber = dWayHeapType + 2;

                double timeSpent = doExperiment(gridEdgeWeightedDigraph, sourceNode, dWayHeapChildrenNumber);
                totalTimeSpent[dWayHeapType] += timeSpent;
            }
        }

        computeAndPrintResults(totalTimeSpent, digraphType, dWayHeaps, experiments, gridDigraphVertices, edges);
    }

    private double doExperiment(EdgeWeightedDigraphInterface edgeWeightedDigraph, int source, int dWayHeapChildrenNumber) {
        Stopwatch stopwatch = new Stopwatch();

        new DijkstraSPDWayHeap(edgeWeightedDigraph, source, dWayHeapChildrenNumber);
        return stopwatch.elapsedTime();
    }

    private void computeAndPrintResults(double[] totalTimeSpent, String digraphType, String[] dWayHeaps,
                                        int experiments, int vertices, int edges) {

        for(int dWayHeapType = 0; dWayHeapType < dWayHeaps.length; dWayHeapType++) {
            String dWayHeap = dWayHeaps[dWayHeapType];

            double averageTimeSpent = totalTimeSpent[dWayHeapType] / experiments;
            printResults(digraphType, vertices, edges, dWayHeap, averageTimeSpent);
        }
    }

    private void printResults(String digraphType, int vertices, int edges, String dWayHeap, double averageTimeSpent) {
        StdOut.printf("%46s %12d %10d %15s %21.2f\n", digraphType, vertices, edges, dWayHeap, averageTimeSpent);
    }

    // Parameters example: 10 1000000 3000000 1000 1000000 2000
    public static void main(String[] args) {
        int experiments = Integer.parseInt(args[0]);
        int uniformWeightDigraphVertices = Integer.parseInt(args[1]);
        int uniformWeightDigraphEdges = Integer.parseInt(args[2]);

        // In the Euclidean digraph all vertices are connected to all vertices.
        // So this requires a separate number of vertices to avoid a very high number of edges while still having a
        // dense digraph.
        int euclideanDigraphVertices = Integer.parseInt(args[3]);

        // Grid digraphs must have a number of vertices with an integer square root
        int gridDigraphVertices = Integer.parseInt(args[4]);
        int gridDigraphExtraEdges = Integer.parseInt(args[5]);

        new Exercise57_JohnsonsAlgorithm().generateDigraphsAndDoExperiments(experiments, uniformWeightDigraphVertices,
                uniformWeightDigraphEdges, euclideanDigraphVertices, gridDigraphVertices, gridDigraphExtraEdges);
    }

}
