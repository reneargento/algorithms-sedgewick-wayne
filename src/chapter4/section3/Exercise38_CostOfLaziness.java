package chapter4.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 13/11/17.
 */
public class Exercise38_CostOfLaziness {

    private void generateGraphsAndDoExperiments(int experiments, int vertices, int edges, int euclideanGraphVertices) {

        StdOut.printf("%47s %18s %12s %10s %10s\n", "Edge Weighted Graph type | ", "MST Algorithm | ", "Vertices | ",
                "Edges | ", "Average time spent");

        String[] mstAlgorithms = {"Lazy Prim MST", "Eager Prim MST"};

        double totalTimeSpentLazyPrim = 0;
        double totalTimeSpentEagerPrim = 0;

        // Graph model 1: Random edge weighted graphs with uniform weight distribution
        String graphType = "Random graph w/ uniform weight distribution";

        Exercise34_RandomSparseEdgeWeightedGraphs.RandomEdgeWeightedGraphs randomEdgeWeightedGraphs =
                new Exercise34_RandomSparseEdgeWeightedGraphs().new RandomEdgeWeightedGraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedGraph randomEdgeWeightedGraphUniformWeights =
                    randomEdgeWeightedGraphs.erdosRenyiGraphUniformWeights(vertices, edges);

            for(int mstAlgorithmType = 0; mstAlgorithmType < 2; mstAlgorithmType++) {
                boolean isLazyPrim = mstAlgorithmType == 0;

                if (isLazyPrim) {
                    totalTimeSpentLazyPrim += doExperiment(randomEdgeWeightedGraphUniformWeights, true);
                } else {
                    totalTimeSpentEagerPrim += doExperiment(randomEdgeWeightedGraphUniformWeights, false);
                }
            }
        }

        computeAndPrintResults(graphType, mstAlgorithms, experiments, vertices, edges, totalTimeSpentLazyPrim,
                totalTimeSpentEagerPrim);

        totalTimeSpentLazyPrim = 0;
        totalTimeSpentEagerPrim = 0;

        // Graph model 2: Random edge weighted graphs with gaussian weight distribution
        graphType = "Random graph w/ gaussian weight distribution";

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedGraph randomEdgeWeightedGraphGaussianWeights =
                    randomEdgeWeightedGraphs.erdosRenyiGraphGaussianWeights(vertices, edges);

            for(int mstAlgorithmType = 0; mstAlgorithmType < 2; mstAlgorithmType++) {
                boolean isLazyPrim = mstAlgorithmType == 0;

                if (isLazyPrim) {
                    totalTimeSpentLazyPrim += doExperiment(randomEdgeWeightedGraphGaussianWeights, true);
                } else {
                    totalTimeSpentEagerPrim += doExperiment(randomEdgeWeightedGraphGaussianWeights, false);
                }
            }
        }

        computeAndPrintResults(graphType, mstAlgorithms, experiments, vertices, edges, totalTimeSpentLazyPrim,
                totalTimeSpentEagerPrim);

        totalTimeSpentLazyPrim = 0;
        totalTimeSpentEagerPrim = 0;

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

            for(int mstAlgorithmType = 0; mstAlgorithmType < 2; mstAlgorithmType++) {
                boolean isLazyPrim = mstAlgorithmType == 0;

                if (isLazyPrim) {
                    totalTimeSpentLazyPrim += doExperiment(randomEdgeWeightedEuclideanGraph, true);
                } else {
                    totalTimeSpentEagerPrim += doExperiment(randomEdgeWeightedEuclideanGraph, false);
                }
            }
        }

        computeAndPrintResults(graphType, mstAlgorithms, experiments, euclideanGraphVertices, edges, totalTimeSpentLazyPrim,
                totalTimeSpentEagerPrim);
    }

    private double doExperiment(EdgeWeightedGraphInterface edgeWeightedGraph, boolean isLazyPrim) {
        Stopwatch stopwatch = new Stopwatch();

        if (isLazyPrim) {
            new LazyPrimMST(edgeWeightedGraph);
        } else {
            new PrimMST(edgeWeightedGraph);
        }

        return stopwatch.elapsedTime();
    }

    private void computeAndPrintResults(String graphType, String[] mstAlgorithms, int experiments, int vertices,
                                        int edges, double totalTimeSpentLazyPrim, double totalTimeSpentEagerPrim) {
        double averageTimeSpentLazyPrim = totalTimeSpentLazyPrim / experiments;
        double averageTimeSpentEagerPrim = totalTimeSpentEagerPrim / experiments;

        printResults(graphType, mstAlgorithms[0], vertices, edges, averageTimeSpentLazyPrim);
        printResults(graphType, mstAlgorithms[1], vertices, edges, averageTimeSpentEagerPrim);
    }

    private void printResults(String graphType, String mstAlgorithm, int vertices, int edges, double averageTimeSpent) {
        StdOut.printf("%44s %18s %12d %10d %21.2f\n", graphType, mstAlgorithm, vertices, edges, averageTimeSpent);
    }

    // Parameters example: 10 100000 300000 1000
    public static void main(String[] args) {
        //Arguments example:
        int experiments = Integer.parseInt(args[0]);
        int vertices = Integer.parseInt(args[1]);
        int edges = Integer.parseInt(args[2]);

        // In the Euclidean graph all vertices are connected to all vertices.
        // So this requires a separate number of vertices to avoid a very high number of edges while still having a dense graph.
        int euclideanGraphVertices = Integer.parseInt(args[3]);

        new Exercise38_CostOfLaziness().generateGraphsAndDoExperiments(experiments, vertices, edges, euclideanGraphVertices);
    }

}
