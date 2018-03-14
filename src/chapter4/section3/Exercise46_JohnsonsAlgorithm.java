package chapter4.section3;

import chapter1.section3.Queue;
import chapter1.section5.UnionFind;
import chapter2.section4.DWayPriorityQueue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 16/11/17.
 */
@SuppressWarnings("unchecked")
public class Exercise46_JohnsonsAlgorithm {

    private static final int LAZY_PRIM_ID = 0;
    private static final int KRUSKAL_ID = 1;

    public class LazyPrimMSTDWayHeap {

        private boolean[] marked; // minimum spanning tree vertices
        private Queue<Edge> minimumSpanningTree;
        private DWayPriorityQueue<Edge> dWayPriorityQueue; // crossing (and ineligible) edges

        private double weight;

        public LazyPrimMSTDWayHeap(EdgeWeightedGraphInterface edgeWeightedGraph, int numberOfChildrenPerNodeInHeap) {
            dWayPriorityQueue = new DWayPriorityQueue<>(DWayPriorityQueue.Orientation.MIN, numberOfChildrenPerNodeInHeap);
            marked = new boolean[edgeWeightedGraph.vertices()];
            minimumSpanningTree = new Queue<>();

            visit(edgeWeightedGraph, 0); // assumes the graph is connected

            while (!dWayPriorityQueue.isEmpty()) {
                Edge edge = dWayPriorityQueue.deleteTop(); // Get lowest-weight edge from priority queue
                int vertex1 = edge.either();
                int vertex2 = edge.other(vertex1);

                // Skip if ineligible
                if (marked[vertex1] && marked[vertex2]) {
                    continue;
                }

                // Add edge to the minimum spanning tree
                minimumSpanningTree.enqueue(edge);
                weight += edge.weight();

                // Add vertex to the minimum spanning tree
                if (!marked[vertex1]) {
                    visit(edgeWeightedGraph, vertex1);
                }
                if (!marked[vertex2]) {
                    visit(edgeWeightedGraph, vertex2);
                }
            }
        }

        private void visit(EdgeWeightedGraphInterface edgeWeightedGraph, int vertex) {
            // Mark vertex and add to priority queue all edges from vertex to unmarked vertices
            marked[vertex] = true;

            for(Edge edge : edgeWeightedGraph.adjacent(vertex)) {
                if (!marked[edge.other(vertex)]) {
                    dWayPriorityQueue.insert(edge);
                }
            }
        }

        public Iterable<Edge> edges() {
            return minimumSpanningTree;
        }

        public double eagerWeight() {
            return weight;
        }
    }

    public class KruskalMSTDWayHeap {

        private Queue<Edge> minimumSpanningTree;
        private double weight;

        public KruskalMSTDWayHeap(EdgeWeightedGraphInterface edgeWeightedGraph, int numberOfChildrenPerNodeInHeap) {
            minimumSpanningTree = new Queue<>();
            DWayPriorityQueue<Edge> dWayPriorityQueue = new DWayPriorityQueue<>(DWayPriorityQueue.Orientation.MIN,
                    numberOfChildrenPerNodeInHeap);

            for(Edge edge : edgeWeightedGraph.edges()) {
                dWayPriorityQueue.insert(edge);
            }

            UnionFind unionFind = new UnionFind(edgeWeightedGraph.vertices());

            while (!dWayPriorityQueue.isEmpty() && minimumSpanningTree.size() < edgeWeightedGraph.vertices() - 1) {
                Edge edge = dWayPriorityQueue.deleteTop(); // Get lowest-weight edge from priority queue
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

    private void generateGraphsAndDoExperiments(int experiments, int vertices, int edges, int euclideanGraphVertices) {

        StdOut.printf("%47s %18s %12s %10s %15s %12s\n", "Edge Weighted Graph type | ", "MST Algorithm | ", "Vertices | ",
                "Edges | ", "D-way heap | ", "Average time spent");

        String[] mstAlgorithms = {"Lazy Prim MST", "Kruskal MST"};
        String[] dWayHeaps = {"2-Way Heap", "3-Way Heap", "4-Way Heap", "5-Way Heap"};

        double[][] totalTimeSpent = new double[mstAlgorithms.length][dWayHeaps.length];
        for(int timeArray = 0; timeArray < totalTimeSpent.length; timeArray++) {
            totalTimeSpent[timeArray] = new double[dWayHeaps.length];
        }

        // Graph model 1: Random edge weighted graphs with uniform weight distribution
        String graphType = "Random graph w/ uniform weight distribution";

        Exercise34_RandomSparseEdgeWeightedGraphs.RandomEdgeWeightedGraphs randomEdgeWeightedGraphs =
                new Exercise34_RandomSparseEdgeWeightedGraphs().new RandomEdgeWeightedGraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedGraph randomEdgeWeightedGraphUniformWeights =
                    randomEdgeWeightedGraphs.erdosRenyiGraphUniformWeights(vertices, edges);

            for(int mstAlgorithmType = 0; mstAlgorithmType < mstAlgorithms.length; mstAlgorithmType++) {

                for(int dWayHeapType = 0; dWayHeapType < dWayHeaps.length; dWayHeapType++) {
                    int dWayHeapChildrenNumber = dWayHeapType + 2;

                    double timeSpent = doExperiment(randomEdgeWeightedGraphUniformWeights, mstAlgorithmType, dWayHeapChildrenNumber);
                    totalTimeSpent[mstAlgorithmType][dWayHeapType] += timeSpent;
                }
            }
        }

        printResultsAndResetTimer(totalTimeSpent, graphType, mstAlgorithms, dWayHeaps, experiments, vertices, edges);

        // Graph model 2: Random edge weighted graphs with gaussian weight distribution
        graphType = "Random graph w/ gaussian weight distribution";

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedGraph randomEdgeWeightedGraphGaussianWeights =
                    randomEdgeWeightedGraphs.erdosRenyiGraphGaussianWeights(vertices, edges);

            for(int mstAlgorithmType = 0; mstAlgorithmType < mstAlgorithms.length; mstAlgorithmType++) {

                for(int dWayHeapType = 0; dWayHeapType < dWayHeaps.length; dWayHeapType++) {
                    int dWayHeapChildrenNumber = dWayHeapType + 2;

                    double timeSpent = doExperiment(randomEdgeWeightedGraphGaussianWeights, mstAlgorithmType, dWayHeapChildrenNumber);
                    totalTimeSpent[mstAlgorithmType][dWayHeapType] += timeSpent;
                }
            }
        }

        printResultsAndResetTimer(totalTimeSpent, graphType, mstAlgorithms, dWayHeaps, experiments, vertices, edges);

        // Graph model 3: Random edge weighted Euclidean graphs
        graphType = "Random Euclidean graph";

        Exercise35_RandomEuclideanEdgeWeightedGraphs randomEuclideanEdgeWeightedGraphs =
                new Exercise35_RandomEuclideanEdgeWeightedGraphs();
        // Running the experiment on a complete graph
        double radius = 1;

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedGraphInterface randomEdgeWeightedEuclideanGraph =
                    randomEuclideanEdgeWeightedGraphs.randomEuclideanEdgeWeightedGraph(euclideanGraphVertices, radius);

            edges = randomEdgeWeightedEuclideanGraph.edgesCount();

            for(int mstAlgorithmType = 0; mstAlgorithmType < mstAlgorithms.length; mstAlgorithmType++) {

                for(int dWayHeapType = 0; dWayHeapType < dWayHeaps.length; dWayHeapType++) {
                    int dWayHeapChildrenNumber = dWayHeapType + 2;

                    double timeSpent = doExperiment(randomEdgeWeightedEuclideanGraph, mstAlgorithmType, dWayHeapChildrenNumber);
                    totalTimeSpent[mstAlgorithmType][dWayHeapType] += timeSpent;
                }
            }
        }

        printResultsAndResetTimer(totalTimeSpent, graphType, mstAlgorithms, dWayHeaps, experiments,
                euclideanGraphVertices, edges);
    }

    private double doExperiment(EdgeWeightedGraphInterface edgeWeightedGraph, int mstAlgorithmType, int dWayHeapChildrenNumber) {
        Stopwatch stopwatch = new Stopwatch();

        switch (mstAlgorithmType) {
            case LAZY_PRIM_ID: new LazyPrimMSTDWayHeap(edgeWeightedGraph, dWayHeapChildrenNumber);
                break;
            case KRUSKAL_ID: new KruskalMSTDWayHeap(edgeWeightedGraph, dWayHeapChildrenNumber);
                break;
        }

        return stopwatch.elapsedTime();
    }

    private void printResultsAndResetTimer(double[][] totalTimeSpent, String graphType, String[] mstAlgorithms,
                                           String[] dWayHeaps, int experiments, int vertices, int edges) {
        for(int mstAlgorithmType = 0; mstAlgorithmType < mstAlgorithms.length; mstAlgorithmType++) {
            String mstAlgorithm = mstAlgorithms[mstAlgorithmType];

            for(int dWayHeapType = 0; dWayHeapType < dWayHeaps.length; dWayHeapType++) {
                String dWayHeap = dWayHeaps[dWayHeapType];

                double averageTimeSpent = totalTimeSpent[mstAlgorithmType][dWayHeapType] / experiments;
                printResults(graphType, mstAlgorithm, vertices, edges, dWayHeap, averageTimeSpent);
            }
        }

        for(int timeArray = 0; timeArray < totalTimeSpent.length; timeArray++) {
            totalTimeSpent[timeArray] = new double[dWayHeaps.length];
        }
    }

    private void printResults(String graphType, String mstAlgorithm, int vertices, int edges, String dWayHeap,
                              double averageTimeSpent) {
        StdOut.printf("%44s %18s %12d %10d %15s %21.2f\n", graphType, mstAlgorithm, vertices, edges, dWayHeap, averageTimeSpent);
    }

    // Parameters example: 10 1000000 3000000 1000
    public static void main(String[] args) {
        int experiments = Integer.parseInt(args[0]);
        int vertices = Integer.parseInt(args[1]);
        int edges = Integer.parseInt(args[2]);

        // In the Euclidean graph all vertices are connected to all vertices.
        // So this requires a separate number of vertices to avoid a very high number of edges while still having a dense graph.
        int euclideanGraphVertices = Integer.parseInt(args[3]);

        new Exercise46_JohnsonsAlgorithm().generateGraphsAndDoExperiments(experiments, vertices, edges, euclideanGraphVertices);
    }

}
