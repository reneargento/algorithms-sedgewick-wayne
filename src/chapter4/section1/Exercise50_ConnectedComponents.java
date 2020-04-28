package chapter4.section1;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import util.StatsUtil;

import java.awt.*;

/**
 * Created by Rene Argento on 14/10/17.
 */
public class Exercise50_ConnectedComponents {

    private void generateGraphsAndDoExperiments(int experiments, int vertices, int edges) {
        // Graph model 1: Random graphs
        double[] histogramRandomGraphs = new double[vertices + 1];
        String randomGraphType = "Random graph";

        Exercise40_RandomGraphs exercise40_randomGraphs = new Exercise40_RandomGraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomGraph = exercise40_randomGraphs.erdosRenyiGraph(vertices, edges);
            doExperiment(randomGraph, histogramRandomGraphs);
        }

        StdOut.println("Random graph experiments");
        printResults(histogramRandomGraphs);

        drawHistogram(histogramRandomGraphs, randomGraphType, vertices, edges);
        sleep();

        // Graph model 2: Random simple graphs
        double[] histogramRandomSimpleGraphs = new double[vertices + 1];
        String randomSimpleGraphType = "Random simple graph";

        Exercise41_RandomSimpleGraphs exercise41_randomSimpleGraphs = new Exercise41_RandomSimpleGraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomSimpleGraph = exercise41_randomSimpleGraphs.randomSimpleGraph(vertices, edges);
            doExperiment(randomSimpleGraph, histogramRandomSimpleGraphs);
        }

        StdOut.println("\nRandom simple graph experiments");
        printResults(histogramRandomSimpleGraphs);

        drawHistogram(histogramRandomSimpleGraphs, randomSimpleGraphType, vertices, edges);
        sleep();

        // Graph model 3: Random interval graphs
        double[] histogramRandomIntervalGraphs = new double[vertices + 1];
        String randomIntervalGraphType = "Random interval graph";

        Exercise46_RandomIntervalGraphs exercise46_randomIntervalGraphs = new Exercise46_RandomIntervalGraphs();
        double defaultLength = 0.3;

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomIntervalGraph = exercise46_randomIntervalGraphs.generateIntervalGraph(vertices, defaultLength);
            doExperiment(randomIntervalGraph, histogramRandomIntervalGraphs);
        }

        StdOut.println("\nRandom interval graph experiments");
        printResults(histogramRandomIntervalGraphs);

        drawHistogram(histogramRandomIntervalGraphs, randomIntervalGraphType, vertices, edges);
    }

    private void doExperiment(GraphInterface graph, double[] histogram) {
        ConnectedComponentsRecursiveDFS connectedComponentsRecursiveDFS = new ConnectedComponentsRecursiveDFS(graph);
        int numberOfComponents = connectedComponentsRecursiveDFS.count();
        histogram[numberOfComponents]++;
    }

    private void drawHistogram(double[] histogram, String graphType, int vertices, int edges) {
        StdDraw.setCanvasSize(1500, 512);

        double maxFrequency = 0;

        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > maxFrequency) {
                maxFrequency = histogram[i];
            }
        }

        double minX;
        double frequencyX;
        double yLabelsX;

        if (histogram.length < 150) {
            minX = -5;
            frequencyX = -4;
            yLabelsX = -1;
        } else {
            minX = -20;
            frequencyX = -15;
            yLabelsX = -5;
        }

        double maxX = histogram.length + 5;
        double middleX = minX + (maxX - minX) / 2;

        double minY = -100;
        double maxY = maxFrequency + 100;
        double middleY = minY + (maxY - minY) / 2;

        StdDraw.setXscale(minX, maxX);
        StdDraw.setYscale(minY, maxY);

        // Labels
        String fontName = "Verdana";
        Font titlesFont = new Font(fontName, Font.PLAIN, 14);
        StdDraw.setFont(titlesFont);

        StdDraw.text(middleX, maxFrequency + 80, graphType + ": Frequency vs Number of Components");
        StdDraw.text(middleX, maxFrequency + 45, "Vertices: " + vertices);
        StdDraw.text(middleX, maxFrequency + 10, "Edges: " + edges);
        StdDraw.text(frequencyX, middleY, "Frequency", 90);
        StdDraw.text(middleX, -75, "Number of Components");

        Font graphLabelsFont = new Font(fontName, Font.PLAIN, 10);
        StdDraw.setFont(graphLabelsFont);

        // Y labels
        for (int y = 0; y <= maxFrequency; y += 50) {
            StdDraw.text(yLabelsX, y, String.valueOf(y));
        }

        // X labels
        for (int x = 1; x < histogram.length;) {
            StdDraw.text(x, -20, String.valueOf(x));

            if (x < 10) {
                x += 3;
            } else {
                x += 10;
            }
        }

        StatsUtil.plotBars(histogram, 0.25);
    }

    private void printResults(double[] histogram) {
        for(int i = 1; i < histogram.length; i++) {
            StdOut.printf("Components: %5d   Frequency: %5.0f\n", i, histogram[i]);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // No need to take any action if sleep is interrupted
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