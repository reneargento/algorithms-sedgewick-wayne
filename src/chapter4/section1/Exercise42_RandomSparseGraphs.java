package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 07/10/17.
 */
public class Exercise42_RandomSparseGraphs {

    //A graph is considered sparse if its number of different edges is within a small constant factor of V
    public List<Graph> randomSparseGraph(int numberOfGraphs) {

        if (numberOfGraphs < 0) {
            throw new IllegalArgumentException("Number of graphs cannot be negative");
        }

        Exercise40_RandomGraphs randomGraphs = new Exercise40_RandomGraphs();

        List<Graph> randomSparseGraphs = new ArrayList<>();
        int[] graphVerticesCount = {10, 100, 1000, 10000};

        for(int graph = 0; graph < numberOfGraphs; graph++) {
            int vertices = graphVerticesCount[graph % 4];
            int edges = vertices * 3;

            Graph randomSparseGraph = randomGraphs.erdosRenyiGraph(vertices, edges);
            randomSparseGraphs.add(randomSparseGraph);
        }

        return randomSparseGraphs;
    }

    // Parameter example: 100
    public static void main(String[] args) {
        int numberOfGraphs = Integer.parseInt(args[0]);

        List<Graph> randomSparseGraphs = new Exercise42_RandomSparseGraphs().randomSparseGraph(numberOfGraphs);
        StdOut.println("Random sparse graphs generated: " + (randomSparseGraphs.size() == numberOfGraphs));
    }

}
