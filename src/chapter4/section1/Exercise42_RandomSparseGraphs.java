package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rene on 07/10/17.
 */
public class Exercise42_RandomSparseGraphs {

    //A graph is considered sparse if its number of different edges is within a small constant factor of V
    public List<Graph> randomSparseGraph(int numberOfGraphs) {

        if(numberOfGraphs < 1) {
            throw new IllegalArgumentException("Number of graphs must be positive");
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

    public static void main(String[] args) {
        List<Graph> randomSparseGraphs = new Exercise42_RandomSparseGraphs().randomSparseGraph(10);
        StdOut.println("Random sparse graphs generated: " + (randomSparseGraphs.size() > 0));
    }

}
