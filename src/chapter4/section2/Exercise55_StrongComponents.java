package chapter4.section2;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import util.StatsUtil;

import java.awt.*;
import java.util.List;

/**
 * Created by Rene Argento on 03/11/17.
 */
public class Exercise55_StrongComponents {

    private void generateDigraphsAndDoExperiments(int experiments, int vertices, int edges) {
        // Digraph model 1: Random digraphs
        double[] histogramRandomDigraphs = new double[vertices + 1];
        String randomDigraphType = "Random digraph";

        Exercise45_RandomDigraphs exercise45_randomDigraphs = new Exercise45_RandomDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            DigraphInterface randomDigraph = exercise45_randomDigraphs.erdosRenyiDigraph(vertices, edges);
            doExperiment(randomDigraph, histogramRandomDigraphs);
        }

        StdOut.println("Random digraph experiments");
        printResults(histogramRandomDigraphs);

        drawHistogram(histogramRandomDigraphs, randomDigraphType, vertices, edges);
        sleep();

        // Digraph model 2: Random simple digraphs
        double[] histogramRandomSimpleDigraphs = new double[vertices + 1];
        String randomSimpleDigraphType = "Random simple digraph";

        Exercise46_RandomSimpleDigraphs exercise46_randomSimpleDigraphs = new Exercise46_RandomSimpleDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            DigraphInterface randomSimpleDigraph = exercise46_randomSimpleDigraphs.randomDigraph(vertices, edges);
            doExperiment(randomSimpleDigraph, histogramRandomSimpleDigraphs);
        }

        StdOut.println("\nRandom simple digraph experiments");
        printResults(histogramRandomSimpleDigraphs);

        drawHistogram(histogramRandomSimpleDigraphs, randomSimpleDigraphType, vertices, edges);
        sleep();

        // Digraph model 3: Random sparse digraphs
        double[] histogramRandomSparseDigraphs = new double[vertices + 1];
        String randomSparseGraphType = "Random sparse digraph";

        Exercise47_RandomSparseDigraphs exercise47_randomSparseDigraphs = new Exercise47_RandomSparseDigraphs();

        List<DigraphInterface> randomSparseDigraphs = exercise47_randomSparseDigraphs.randomSparseDigraph(experiments, vertices);

        for(DigraphInterface randomSparseDigraph : randomSparseDigraphs) {
            doExperiment(randomSparseDigraph, histogramRandomSparseDigraphs);
        }

        StdOut.println("\nRandom sparse digraph experiments");
        printResults(histogramRandomSparseDigraphs);

        drawHistogram(histogramRandomSparseDigraphs, randomSparseGraphType, vertices, edges);
    }

    private void doExperiment(DigraphInterface digraph, double[] histogram) {
        KosarajuSharirSCC kosarajuSharirSCC = new KosarajuSharirSCC(digraph);

        int numberOfStrongComponents = kosarajuSharirSCC.count();
        histogram[numberOfStrongComponents]++;
    }

    private void printResults(double[] histogram) {
        for(int i = 1; i < histogram.length; i++) {
            StdOut.printf("Strong components: %5d   Frequency: %5.0f\n", i, histogram[i]);
        }
    }

    private void drawHistogram(double[] histogram, String digraphType, int vertices, int edges) {
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

        StdDraw.text(middleX, maxFrequency + 80, digraphType + ": Frequency vs Number of Strong Components");
        StdDraw.text(middleX, maxFrequency + 40, "Vertices: " + vertices);
        StdDraw.text(middleX, maxFrequency + 5, "Edges: " + edges);
        StdDraw.text(frequencyX, middleY, "Frequency", 90);
        StdDraw.text(middleX, -75, "Number of Strong Components");

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

        new Exercise55_StrongComponents().generateDigraphsAndDoExperiments(experiments, vertices, edges);
    }

}