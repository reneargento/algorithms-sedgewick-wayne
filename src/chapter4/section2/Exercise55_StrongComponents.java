package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.List;

/**
 * Created by Rene Argento on 03/11/17.
 */
public class Exercise55_StrongComponents {

    private void generateDigraphsAndDoExperiments(int experiments, int vertices, int edges) {

        int[] histogram = new int[vertices + 1];

        // Digraph model 1: Random digraphs
        StdOut.println("Random digraph experiments");

        Exercise45_RandomDigraphs exercise45_randomDigraphs = new Exercise45_RandomDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            DigraphInterface randomDigraph = exercise45_randomDigraphs.erdosRenyiDigraph(vertices, edges);
            doExperiment(randomDigraph, histogram);
        }

        printResults(histogram);

        //Reset histogram
        histogram = new int[vertices + 1];

        // Digraph model 2: Random simple digraphs
        StdOut.println("\nRandom simple digraph experiments");

        Exercise46_RandomSimpleDigraphs exercise46_randomSimpleDigraphs = new Exercise46_RandomSimpleDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            DigraphInterface randomSimpleDigraph = exercise46_randomSimpleDigraphs.randomDigraph(vertices, edges);
            doExperiment(randomSimpleDigraph, histogram);
        }

        printResults(histogram);

        //Reset histogram
        histogram = new int[vertices + 1];

        // Digraph model 3: Random sparse digraphs
        StdOut.println("\nRandom sparse digraph experiments");

        Exercise47_RandomSparseDigraphs exercise47_randomSparseDigraphs = new Exercise47_RandomSparseDigraphs();

        List<DigraphInterface> randomSparseDigraphs = exercise47_randomSparseDigraphs.randomSparseDigraph(experiments, vertices);

        for(DigraphInterface randomSparseDigraph : randomSparseDigraphs) {
            doExperiment(randomSparseDigraph, histogram);
        }

        printResults(histogram);
    }

    private void doExperiment(DigraphInterface digraph, int[] histogram) {
        KosarajuSharirSCC kosarajuSharirSCC = new KosarajuSharirSCC(digraph);

        int numberOfStrongComponents = kosarajuSharirSCC.count();
        histogram[numberOfStrongComponents]++;
    }

    private void printResults(int[] histogram) {
        for(int i = 1; i < histogram.length; i++) {
            StdOut.printf("Strong components: %5d   Frequency: %5d\n", i, histogram[i]);
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
