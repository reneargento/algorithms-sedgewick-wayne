package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 31/10/17.
 */
public class Exercise47_RandomSparseDigraphs {

    //A digraph is considered sparse if its number of different edges is within a small constant factor of V
    public List<DigraphInterface> randomSparseDigraph(int numberOfDigraphs) {

        if (numberOfDigraphs < 0) {
            throw new IllegalArgumentException("Number of digraphs cannot be negative");
        }

        Exercise45_RandomDigraphs randomDigraphs = new Exercise45_RandomDigraphs();

        List<DigraphInterface> randomSparseDigraphs = new ArrayList<>();
        int[] digraphVerticesCount = {10, 100, 1000, 10000};

        for(int digraph = 0; digraph < numberOfDigraphs; digraph++) {
            int vertices = digraphVerticesCount[digraph % 4];
            int edges = vertices * 3;

            Digraph randomSparseDigraph = randomDigraphs.erdosRenyiDigraph(vertices, edges);
            randomSparseDigraphs.add(randomSparseDigraph);
        }

        return randomSparseDigraphs;
    }

    // Used for experiments in exercises 4.2.52, 4.2.53, 4.2.54 and 4.2.55
    public List<DigraphInterface> randomSparseDigraph(int numberOfDigraphs, int vertices) {

        if (numberOfDigraphs < 1) {
            throw new IllegalArgumentException("Number of digraphs must be positive");
        }

        Exercise45_RandomDigraphs randomDigraphs = new Exercise45_RandomDigraphs();

        List<DigraphInterface> randomSparseDigraphs = new ArrayList<>();

        for(int digraph = 0; digraph < numberOfDigraphs; digraph++) {
            int edges = vertices * 3;

            Digraph randomSparseDigraph = randomDigraphs.erdosRenyiDigraph(vertices, edges);
            randomSparseDigraphs.add(randomSparseDigraph);
        }

        return randomSparseDigraphs;
    }

    //Parameter example: 100
    public static void main(String[] args) {
        int numberOfDigraphs = Integer.parseInt(args[0]);

        List<DigraphInterface> randomSparseDigraphs = new Exercise47_RandomSparseDigraphs().randomSparseDigraph(numberOfDigraphs);
        StdOut.println("Random sparse digraphs generated: " + (randomSparseDigraphs.size() == numberOfDigraphs));
    }

}
