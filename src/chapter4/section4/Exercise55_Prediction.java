package chapter4.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.List;

/**
 * Created by Rene Argento on 30/12/17.
 */
public class Exercise55_Prediction {

    private void doublingRatioExperiments() {

        int digraphsPerExperiment = 5;
        int initialVertices = 125;
        int initialEdges = initialVertices * 10;

        Exercise49_RandomSparseEdgeWeightedDigraphs.RandomEdgeWeightedDigraphs randomEdgeWeightedDigraphs =
                new Exercise49_RandomSparseEdgeWeightedDigraphs().new RandomEdgeWeightedDigraphs();

        double totalTimePrevious = 0;

        for(int digraph = 0; digraph < digraphsPerExperiment; digraph++) {
            EdgeWeightedDigraphInterface randomEdgeWeightedDigraphUniformWeights =
                    randomEdgeWeightedDigraphs.erdosRenyiDigraphUniformWeights(initialVertices, initialEdges);

            totalTimePrevious += doExperiment(randomEdgeWeightedDigraphUniformWeights);
        }

        double averagePreviousTime = totalTimePrevious / digraphsPerExperiment;

        StdOut.println("Doubling ratio experiments:\n");
        StdOut.printf("%6s %6s %6s %6s\n", "Vertices | ", "Edges | ","Time | ", "Ratio");

        for(int vertices = 250; vertices <= 4000; vertices += vertices) {
            int edges = vertices * 10;

            double totalTimeSpent = 0;

            for(int digraph = 0; digraph < digraphsPerExperiment; digraph++) {
                EdgeWeightedDigraphInterface randomEdgeWeightedDigraphUniformWeights =
                        randomEdgeWeightedDigraphs.erdosRenyiDigraphUniformWeights(vertices, edges);

                totalTimeSpent += doExperiment(randomEdgeWeightedDigraphUniformWeights);
            }

            double averageTimeSpent = totalTimeSpent / digraphsPerExperiment;

            StdOut.printf("%8d %8s %7.1f ", vertices, edges, averageTimeSpent);
            StdOut.printf("%9.1f\n", averageTimeSpent / averagePreviousTime);
            averagePreviousTime = averageTimeSpent;
        }
    }

    private void empiricalExperiments() {

        StdOut.println("\nEmpirical experiments:\n");
        StdOut.printf("%12s %8s %10s\n", "Vertices | ", "Edges | ", "Average time spent");

        int vertices = 0;
        int edges = 0;
        int digraphsPerExperiment = 5;

        Exercise49_RandomSparseEdgeWeightedDigraphs.RandomEdgeWeightedDigraphs randomEdgeWeightedDigraphs =
                new Exercise49_RandomSparseEdgeWeightedDigraphs().new RandomEdgeWeightedDigraphs();

        boolean targetTimeReached = false;

        while (!targetTimeReached) {
            double totalTimeSpent = 0;
            vertices += 1000;
            edges = vertices * 10;

            for(int digraph = 0; digraph < digraphsPerExperiment; digraph++) {
                EdgeWeightedDigraphInterface randomEdgeWeightedDigraphUniformWeights =
                        randomEdgeWeightedDigraphs.erdosRenyiDigraphUniformWeights(vertices, edges);

                totalTimeSpent += doExperiment(randomEdgeWeightedDigraphUniformWeights);
            }

            double averageTimeSpent = totalTimeSpent / digraphsPerExperiment;
            printResults(vertices, edges, averageTimeSpent);

            // Stop when we get close to the target time
            if (averageTimeSpent >= 9) {
                targetTimeReached = true;
            }
        }

        StdOut.println("\n(Approximately) largest graph that could be handled using Dijkstra's algorithm to compute all " +
                "its shortest paths in 10 seconds: \nVertices: " + vertices + "\nEdges: " + edges);
    }

    private double doExperiment(EdgeWeightedDigraphInterface edgeWeightedDigraph) {
        Stopwatch stopwatch = new Stopwatch();

        // Compute all shortest paths in the graph
        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            new DijkstraSP(edgeWeightedDigraph, vertex);
        }

        return stopwatch.elapsedTime();
    }

    private void printResults(int vertices, int edges, double averageTimeSpent) {
        StdOut.printf("%9d %8d %21.2f\n", vertices, edges, averageTimeSpent);
    }

    public static void main(String[] args) {
        Exercise55_Prediction prediction = new Exercise55_Prediction();
        prediction.doublingRatioExperiments();
        prediction.empiricalExperiments();
    }

}
