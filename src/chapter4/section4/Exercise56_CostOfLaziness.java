package chapter4.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 31/12/17.
 */
public class Exercise56_CostOfLaziness {

    private static final int LAZY_DIJKSTRA_ID = 0;
    private static final int EAGER_DIJKSTRA_ID = 1;

    private void generateDigraphsAndDoExperiments(int experiments, int uniformWeightDigraphVertices, int edges,
                                                  int euclideanDigraphVertices, int gridDigraphVertices,
                                                  int gridDigraphExtraEdges) {
        StdOut.printf("%48s %18s %12s %10s %10s\n", "Edge Weighted Digraph type | ", "Dijkstra Version | ", "Vertices | ",
                "Edges | ", "Average time spent");

        String[] dijkstraAlgorithms = {"Lazy", "Eager"};

        double totalTimeSpentLazyDijkstra = 0;
        double totalTimeSpentEagerDijkstra = 0;

        // Digraph model 1: Random edge weighted digraphs with uniform weight distribution
        String digraphType = "Random digraph w/ uniform weight distribution";

        Exercise49_RandomSparseEdgeWeightedDigraphs.RandomEdgeWeightedDigraphs randomEdgeWeightedDigraphs =
                new Exercise49_RandomSparseEdgeWeightedDigraphs().new RandomEdgeWeightedDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedDigraphInterface randomEdgeWeightedDigraphUniformWeights =
                    randomEdgeWeightedDigraphs.erdosRenyiDigraphUniformWeights(uniformWeightDigraphVertices, edges);
            int randomSource = StdRandom.uniform(uniformWeightDigraphVertices);

            for(int dijkstraAlgorithmType = 0; dijkstraAlgorithmType < 2; dijkstraAlgorithmType++) {

                if (dijkstraAlgorithmType == LAZY_DIJKSTRA_ID) {
                    totalTimeSpentLazyDijkstra += doExperiment(randomEdgeWeightedDigraphUniformWeights, randomSource,
                            LAZY_DIJKSTRA_ID);
                } else {
                    totalTimeSpentEagerDijkstra += doExperiment(randomEdgeWeightedDigraphUniformWeights, randomSource,
                            EAGER_DIJKSTRA_ID);
                }
            }
        }

        computeAndPrintResults(digraphType, dijkstraAlgorithms, experiments, uniformWeightDigraphVertices, edges,
                totalTimeSpentLazyDijkstra, totalTimeSpentEagerDijkstra);

        totalTimeSpentLazyDijkstra = 0;
        totalTimeSpentEagerDijkstra = 0;

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

            for(int dijkstraAlgorithmType = 0; dijkstraAlgorithmType < 2; dijkstraAlgorithmType++) {

                if (dijkstraAlgorithmType == LAZY_DIJKSTRA_ID) {
                    totalTimeSpentLazyDijkstra += doExperiment(euclideanEdgeWeightedDigraph, randomSource, LAZY_DIJKSTRA_ID);
                } else {
                    totalTimeSpentEagerDijkstra += doExperiment(euclideanEdgeWeightedDigraph, randomSource, EAGER_DIJKSTRA_ID);
                }
            }
        }

        computeAndPrintResults(digraphType, dijkstraAlgorithms, experiments, euclideanDigraphVertices, edges,
                totalTimeSpentLazyDijkstra, totalTimeSpentEagerDijkstra);

        // Digraph model 3: Random edge weighted grid digraphs
        totalTimeSpentLazyDijkstra = 0;
        totalTimeSpentEagerDijkstra = 0;

        digraphType = "Random grid digraph";

        Exercise51_RandomGridEdgeWeightedDigraphs randomGridEdgeWeightedDigraphs =
                new Exercise51_RandomGridEdgeWeightedDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            EdgeWeightedDigraphInterface gridEdgeWeightedDigraph =
                    randomGridEdgeWeightedDigraphs.randomGridEdgeWeightedDigraph(gridDigraphVertices, gridDigraphExtraEdges);

            edges = gridEdgeWeightedDigraph.edgesCount();
            int randomSource = StdRandom.uniform(gridDigraphVertices);

            for(int dijkstraAlgorithmType = 0; dijkstraAlgorithmType < 2; dijkstraAlgorithmType++) {

                if (dijkstraAlgorithmType == LAZY_DIJKSTRA_ID) {
                    totalTimeSpentLazyDijkstra += doExperiment(gridEdgeWeightedDigraph, randomSource, LAZY_DIJKSTRA_ID);
                } else {
                    totalTimeSpentEagerDijkstra += doExperiment(gridEdgeWeightedDigraph, randomSource, EAGER_DIJKSTRA_ID);
                }
            }
        }

        computeAndPrintResults(digraphType, dijkstraAlgorithms, experiments, gridDigraphVertices, edges,
                totalTimeSpentLazyDijkstra, totalTimeSpentEagerDijkstra);
    }

    private double doExperiment(EdgeWeightedDigraphInterface edgeWeightedDigraph, int randomSource, int dijkstraAlgorithmType) {
        Stopwatch stopwatch = new Stopwatch();

        if (dijkstraAlgorithmType == LAZY_DIJKSTRA_ID) {
            new Exercise39_LazyImplementationDijkstra().new LazyDijkstraSP(edgeWeightedDigraph, randomSource);
        } else {
            new DijkstraSP(edgeWeightedDigraph, randomSource);
        }

        return stopwatch.elapsedTime();
    }

    private void computeAndPrintResults(String digraphType, String[] dijkstraAlgorithms, int experiments, int vertices,
                                        int edges, double totalTimeSpentLazyDijkstra, double totalTimeSpentEagerDijkstra) {
        double averageTimeSpentLazyDijkstra = totalTimeSpentLazyDijkstra / experiments;
        double averageTimeSpentEagerDijkstra = totalTimeSpentEagerDijkstra / experiments;

        printResults(digraphType, dijkstraAlgorithms[0], vertices, edges, averageTimeSpentLazyDijkstra);
        printResults(digraphType, dijkstraAlgorithms[1], vertices, edges, averageTimeSpentEagerDijkstra);
    }

    private void printResults(String digraphType, String dijkstraAlgorithm, int vertices, int edges, double averageTimeSpent) {
        StdOut.printf("%45s %19s %12d %10d %21.2f\n", digraphType, dijkstraAlgorithm, vertices, edges, averageTimeSpent);
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

        new Exercise56_CostOfLaziness().generateDigraphsAndDoExperiments(experiments, uniformWeightDigraphVertices,
                uniformWeightDigraphEdges, euclideanDigraphVertices, gridDigraphVertices, gridDigraphExtraEdges);
    }

}
