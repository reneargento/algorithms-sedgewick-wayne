package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 14/10/17.
 */
public class Exercise50_ConnectedComponents {

    private void generateGraphsAndDoExperiments(int experiments, int vertices, int edges) {

        int[] histogram = new int[vertices + 1];

        // Graph model 1: Random graphs
        StdOut.println("Random graph experiments");

        Exercise40_RandomGraphs exercise40_randomGraphs = new Exercise40_RandomGraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomGraph = exercise40_randomGraphs.erdosRenyiGraph(vertices, edges);
            doExperiment(randomGraph, histogram);
        }

        printResults(histogram);

        //Reset histogram
        histogram = new int[vertices + 1];

        // Graph model 2: Random simple graphs
        StdOut.println("\nRandom simple graph experiments");

        Exercise41_RandomSimpleGraphs exercise41_randomSimpleGraphs = new Exercise41_RandomSimpleGraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomSimpleGraph = exercise41_randomSimpleGraphs.randomSimpleGraph(vertices, edges);
            doExperiment(randomSimpleGraph, histogram);
        }

        printResults(histogram);

        //Reset histogram
        histogram = new int[vertices + 1];

        // Graph model 3: Random interval graphs
        StdOut.println("\nRandom interval graph experiments");

        Exercise46_RandomIntervalGraphs exercise46_randomIntervalGraphs = new Exercise46_RandomIntervalGraphs();
        double defaultLength = 0.3;

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomIntervalGraph = exercise46_randomIntervalGraphs.generateIntervalGraph(vertices, defaultLength);
            doExperiment(randomIntervalGraph, histogram);
        }

        printResults(histogram);
    }

    private void doExperiment(GraphInterface graph, int[] histogram) {
        ConnectedComponentsRecursiveDFS connectedComponentsRecursiveDFS = new ConnectedComponentsRecursiveDFS(graph);
        int numberOfComponents = connectedComponentsRecursiveDFS.count();
        histogram[numberOfComponents]++;
    }

    private void printResults(int[] histogram) {
        for(int i = 1; i < histogram.length; i++) {
            StdOut.printf("Components: %5d   Frequency: %5d\n", i, histogram[i]);
        }
    }

    // Parameters example: 1000 100 300
    //                     1000 300 100
    public static void main(String[] args) {
        int experiments = Integer.parseInt(args[0]);
        int vertices = Integer.parseInt(args[1]);
        int edges = Integer.parseInt(args[2]);

        new Exercise50_ConnectedComponents().generateGraphsAndDoExperiments(experiments, vertices, edges);
    }

}
