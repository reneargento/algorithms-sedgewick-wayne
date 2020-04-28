package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 14/10/17.
 */
public class Exercise49_PathLengthsInBFS {

    private void generateGraphsAndDoExperiments(int experiments, int vertices, int edges) {
        StdOut.printf("%25s %31s %15s\n", "Graph type | ", "Probability of finding path | ", "Average path length");

        int totalPathsFound = 0;
        int totalPathLengths = 0;

        // Graph model 1: Random graphs
        String graphType = "Random graph";

        Exercise40_RandomGraphs exercise40_randomGraphs = new Exercise40_RandomGraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomGraph = exercise40_randomGraphs.erdosRenyiGraph(vertices, edges);
            int[] experimentResults = doExperiment(randomGraph);

            totalPathsFound += experimentResults[0];
            totalPathLengths += experimentResults[1];
        }

        computeAndPrintResults(graphType, experiments, totalPathsFound, totalPathLengths);

        totalPathsFound = 0;
        totalPathLengths = 0;

        // Graph model 2: Random simple graphs
        graphType = "Random simple graph";

        Exercise41_RandomSimpleGraphs exercise41_randomSimpleGraphs = new Exercise41_RandomSimpleGraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomSimpleGraph = exercise41_randomSimpleGraphs.randomSimpleGraph(vertices, edges);
            int[] experimentResults = doExperiment(randomSimpleGraph);

            totalPathsFound += experimentResults[0];
            totalPathLengths += experimentResults[1];
        }

        computeAndPrintResults(graphType, experiments, totalPathsFound, totalPathLengths);

        totalPathsFound = 0;
        totalPathLengths = 0;

        // Graph model 3: Random interval graphs
        graphType = "Random interval graph";

        Exercise46_RandomIntervalGraphs exercise46_randomIntervalGraphs = new Exercise46_RandomIntervalGraphs();
        double defaultLength = 0.3;

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomIntervalGraph = exercise46_randomIntervalGraphs.generateIntervalGraph(vertices, defaultLength);
            int[] experimentResults = doExperiment(randomIntervalGraph);

            totalPathsFound += experimentResults[0];
            totalPathLengths += experimentResults[1];
        }

        computeAndPrintResults(graphType, experiments, totalPathsFound, totalPathLengths);
    }

    private int[] doExperiment(GraphInterface graph) {

        int[] experimentResult = new int[2];
        int totalPathLength = 0;

        int randomSourceVertex = StdRandom.uniform(graph.vertices());
        int randomDestinationVertex = StdRandom.uniform(graph.vertices());

        BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, randomSourceVertex);
        boolean foundPath = breadthFirstPaths.hasPathTo(randomDestinationVertex);

        if (foundPath) {
            for(int vertexInPath : breadthFirstPaths.pathTo(randomDestinationVertex)) {
                totalPathLength++;
            }
        }

        experimentResult[0] = foundPath ? 1 : 0;
        experimentResult[1] = totalPathLength;
        return experimentResult;
    }

    private void computeAndPrintResults(String graphType, int experiments, int pathsFound, int totalPathLength) {
        double probabilityOfFindingPath = ((double) pathsFound) / ((double) experiments);
        double averageLength = ((double) totalPathLength) / ((double) pathsFound);
        printResults(graphType, probabilityOfFindingPath, averageLength);
    }

    private void printResults(String graphType, double probabilityOfFindingPath, double averageLength) {
        StdOut.printf("%22s %31.2f %22.2f\n", graphType, probabilityOfFindingPath, averageLength);
    }

    // Parameters example: 1000 100 300
    //                     1000 300 100
    public static void main(String[] args) {
        int experiments = Integer.parseInt(args[0]);
        int vertices = Integer.parseInt(args[1]);
        int edges = Integer.parseInt(args[2]);

        new Exercise49_PathLengthsInBFS().generateGraphsAndDoExperiments(experiments, vertices, edges);
    }

}
