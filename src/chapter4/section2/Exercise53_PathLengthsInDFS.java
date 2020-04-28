package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.List;

/**
 * Created by Rene Argento on 02/11/17.
 */
public class Exercise53_PathLengthsInDFS {

    private void generateDigraphsAndDoExperiments(int experiments, int vertices, int edges) {
        StdOut.printf("%25s %15s %8s %8s %31s %20s\n", "Digraph type | ", "Experiments | ", "Vertices | ", "Edges | ",
                "Probability of finding path | ", "Average path length");

        int totalPathsFound = 0;
        int totalPathLengths = 0;

        // Digraph model 1: Random digraphs
        String digraphType = "Random digraph";

        Exercise45_RandomDigraphs exercise45_randomDigraphs = new Exercise45_RandomDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            DigraphInterface randomDigraph = exercise45_randomDigraphs.erdosRenyiDigraph(vertices, edges);
            int[] experimentResults = doExperiment(randomDigraph);

            totalPathsFound += experimentResults[0];
            totalPathLengths += experimentResults[1];
        }

        computeAndPrintResults(digraphType, experiments, vertices, edges, totalPathsFound, totalPathLengths);

        totalPathsFound = 0;
        totalPathLengths = 0;

        // Digraph model 2: Random simple digraphs
        digraphType = "Random simple digraph";

        Exercise46_RandomSimpleDigraphs exercise46_randomSimpleDigraphs = new Exercise46_RandomSimpleDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            DigraphInterface randomSimpleDigraph = exercise46_randomSimpleDigraphs.randomDigraph(vertices, edges);
            int[] experimentResults = doExperiment(randomSimpleDigraph);

            totalPathsFound += experimentResults[0];
            totalPathLengths += experimentResults[1];
        }

        computeAndPrintResults(digraphType, experiments, vertices, edges, totalPathsFound, totalPathLengths);

        totalPathsFound = 0;
        totalPathLengths = 0;

        // Digraph model 3: Random sparse digraphs
        digraphType = "Random sparse digraph";

        Exercise47_RandomSparseDigraphs exercise47_randomSparseDigraphs = new Exercise47_RandomSparseDigraphs();

        List<DigraphInterface> randomSparseDigraphs = exercise47_randomSparseDigraphs.randomSparseDigraph(experiments, vertices);

        for(DigraphInterface randomSparseDigraph : randomSparseDigraphs) {
            int[] experimentResults = doExperiment(randomSparseDigraph);

            totalPathsFound += experimentResults[0];
            totalPathLengths += experimentResults[1];
        }

        int edgesInSparseDigraphs = vertices * 3;

        computeAndPrintResults(digraphType, experiments, vertices, edgesInSparseDigraphs, totalPathsFound, totalPathLengths);
    }

    private int[] doExperiment(DigraphInterface digraph) {

        int[] experimentResult = new int[2];
        int totalPathLength = 0;

        int randomSourceVertex = StdRandom.uniform(digraph.vertices());
        int randomDestinationVertex = StdRandom.uniform(digraph.vertices());

        DepthFirstDirectedPaths depthFirstDirectedPaths = new DepthFirstDirectedPaths(digraph, randomSourceVertex);
        boolean foundPath = depthFirstDirectedPaths.hasPathTo(randomDestinationVertex);

        if (foundPath) {
            for(int vertexInPath : depthFirstDirectedPaths.pathTo(randomDestinationVertex)) {
                totalPathLength++;
            }
        }

        experimentResult[0] = foundPath ? 1 : 0;
        experimentResult[1] = totalPathLength;
        return experimentResult;
    }

    private void computeAndPrintResults(String digraphType, int experiments, int vertices, int edges,
                                        int pathsFound, int totalPathLength) {
        double probabilityOfFindingPath = ((double) pathsFound) / ((double) experiments);
        double averageLength = ((double) totalPathLength) / ((double) pathsFound);
        printResults(digraphType, experiments, vertices, edges, probabilityOfFindingPath, averageLength);
    }

    private void printResults(String digraphType, int experiments, int vertices, int edges,
                              double probabilityOfFindingPath, double averageLength) {
        StdOut.printf("%22s %15d %11d %8d %31.2f %23.2f\n", digraphType, experiments, vertices, edges,
                probabilityOfFindingPath, averageLength);
    }

    // Parameters example: 1000 100 300
    //                     1000 300 100
    public static void main(String[] args) {
        int experiments = Integer.parseInt(args[0]);
        int vertices = Integer.parseInt(args[1]);
        int edges = Integer.parseInt(args[2]);

        new Exercise53_PathLengthsInDFS().generateDigraphsAndDoExperiments(experiments, vertices, edges);
    }

}
