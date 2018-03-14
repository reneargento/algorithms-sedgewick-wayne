package chapter4.section3;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 13/11/17.
 */
public class Exercise41_HeaviestMSTEdge {

    private void generateGraphsAndDoExperiments(int experiments, int vertices, int edges, int euclideanGraphVertices) {

        StdOut.printf("%47s %11s %10s %10s %10s\n", "Edge Weighted Graph type | ", "Vertices | ",
                "Edges | ", "Average heaviest edge weight | ", "AVG edges not heavier than heaviest edge");

        // Graph model 1: Random edge weighted graphs with uniform weight distribution
        String graphType = "Random graph w/ uniform weight distribution";

        Exercise34_RandomSparseEdgeWeightedGraphs.RandomEdgeWeightedGraphs randomEdgeWeightedGraphs =
                new Exercise34_RandomSparseEdgeWeightedGraphs().new RandomEdgeWeightedGraphs();

        double totalHeaviestEdgeWeight = 0;
        double totalNumberOfEdgesNotHeavierThanHeaviestEdge = 0;

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedGraph randomEdgeWeightedGraphUniformWeights =
                    randomEdgeWeightedGraphs.erdosRenyiGraphUniformWeights(vertices, edges);

            double[] experimentResult = doExperiment(randomEdgeWeightedGraphUniformWeights);
            totalHeaviestEdgeWeight += experimentResult[0];
            totalNumberOfEdgesNotHeavierThanHeaviestEdge += experimentResult[1];
        }

        computeAndPrintResults(graphType, experiments, vertices, edges, totalHeaviestEdgeWeight,
                totalNumberOfEdgesNotHeavierThanHeaviestEdge);

        totalHeaviestEdgeWeight = 0;
        totalNumberOfEdgesNotHeavierThanHeaviestEdge = 0;

        // Graph model 2: Random edge weighted graphs with gaussian weight distribution
        graphType = "Random graph w/ gaussian weight distribution";

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedGraph randomEdgeWeightedGraphGaussianWeights =
                    randomEdgeWeightedGraphs.erdosRenyiGraphGaussianWeights(vertices, edges);

            double[] experimentResult = doExperiment(randomEdgeWeightedGraphGaussianWeights);
            totalHeaviestEdgeWeight += experimentResult[0];
            totalNumberOfEdgesNotHeavierThanHeaviestEdge += experimentResult[1];
        }

        computeAndPrintResults(graphType, experiments, vertices, edges, totalHeaviestEdgeWeight,
                totalNumberOfEdgesNotHeavierThanHeaviestEdge);

        totalHeaviestEdgeWeight = 0;
        totalNumberOfEdgesNotHeavierThanHeaviestEdge = 0;

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

            double[] experimentResult = doExperiment(randomEdgeWeightedEuclideanGraph);
            totalHeaviestEdgeWeight += experimentResult[0];
            totalNumberOfEdgesNotHeavierThanHeaviestEdge += experimentResult[1];
        }

        computeAndPrintResults(graphType, experiments, euclideanGraphVertices, edges, totalHeaviestEdgeWeight,
                totalNumberOfEdgesNotHeavierThanHeaviestEdge);
    }

    private double[] doExperiment(EdgeWeightedGraphInterface edgeWeightedGraph) {
        KruskalMST kruskalMST = new KruskalMST(edgeWeightedGraph);

        int numberOfEdgesInMST = 0;

        // First pass to compute the number of edges in the MST (it may be a minimum-spanning forest)
        for(Edge edge : kruskalMST.edges()) {
            numberOfEdgesInMST++;
        }

        // Edge case: there are no edges, or only self-loops
        if (numberOfEdgesInMST == 0) {
            return new double[]{0, 0};
        }

        Edge[] edgesInMST = new Edge[numberOfEdgesInMST];
        int edgesInMSTIndex = 0;

        // Second pass to put the edges in MST in an array to be sorted
        for(Edge edge : kruskalMST.edges()) {
            edgesInMST[edgesInMSTIndex++] = edge;
        }

        Arrays.sort(edgesInMST);

        Edge heaviestEdge = edgesInMST[numberOfEdgesInMST - 1];
        double heaviestEdgeWeight = heaviestEdge.weight();

        int numberOfEdgesNotHeavierThanHeaviestEdge = 0;

        for(Edge edge : edgeWeightedGraph.edges()) {
            if (edge.weight() <= heaviestEdgeWeight && edge != heaviestEdge) {
                numberOfEdgesNotHeavierThanHeaviestEdge++;
            }
        }

        return new double[]{heaviestEdgeWeight, numberOfEdgesNotHeavierThanHeaviestEdge};
    }

    private void computeAndPrintResults(String graphType, int experiments, int vertices, int edges,
                                        double totalHeaviestEdgeWeight, double totalNumberOfEdgesNotHeavierThanHeaviestEdge) {
        double averageHeaviestEdgeWeight = totalHeaviestEdgeWeight / experiments;
        double averageNumberOfEdgesNotHeavierThanHeaviestEdge = totalNumberOfEdgesNotHeavierThanHeaviestEdge / experiments;
        printResults(graphType, vertices, edges, averageHeaviestEdgeWeight, averageNumberOfEdgesNotHeavierThanHeaviestEdge);
    }

    private void printResults(String graphType, int vertices, int edges, double averageHeaviestEdgeWeight,
                              double averageNumberOfEdgesNotHeavierThanHeaviestEdge) {
        StdOut.printf("%44s %11d %10d %31.4f %43.2f\n", graphType, vertices, edges, averageHeaviestEdgeWeight,
                averageNumberOfEdgesNotHeavierThanHeaviestEdge);
    }

    // Parameters example: 10 100000 300000 1000
    public static void main(String[] args) {
        int experiments = Integer.parseInt(args[0]);
        int vertices = Integer.parseInt(args[1]);
        int edges = Integer.parseInt(args[2]);

        // In the Euclidean graph all vertices are connected to all vertices.
        // So this requires a separate number of vertices to avoid a very high number of edges while still having a dense graph.
        int euclideanGraphVertices = Integer.parseInt(args[3]);

        new Exercise41_HeaviestMSTEdge().generateGraphsAndDoExperiments(experiments, vertices, edges, euclideanGraphVertices);
    }

}
