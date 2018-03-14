package chapter4.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 27/12/17.
 */
public class Exercise49_RandomSparseEdgeWeightedDigraphs {

    public class RandomEdgeWeightedDigraphs {

        public EdgeWeightedDigraphInterface erdosRenyiDigraphUniformWeights(int vertices, int edges) {
            EdgeWeightedDigraphInterface edgeWeightedDigraph = new EdgeWeightedDigraph(vertices);

            for(int edge = 0; edge < edges; edge++) {
                int vertexId1 = StdRandom.uniform(vertices);
                int vertexId2 = StdRandom.uniform(vertices);

                double uniformRandomWeight = StdRandom.uniform();
                DirectedEdge newEdge;

                int randomDirection = StdRandom.uniform(2);
                if (randomDirection == 0) {
                    newEdge = new DirectedEdge(vertexId1, vertexId2, uniformRandomWeight);
                } else {
                    newEdge = new DirectedEdge(vertexId2, vertexId1, uniformRandomWeight);
                }

                edgeWeightedDigraph.addEdge(newEdge);
            }

            return edgeWeightedDigraph;
        }

        public EdgeWeightedDigraphInterface erdosRenyiDigraphGaussianWeights(int vertices, int edges) {
            EdgeWeightedDigraphInterface randomEdgeWeightedDigraph = new EdgeWeightedDigraph(vertices);

            for(int edge = 0; edge < edges; edge++) {
                int vertexId1 = StdRandom.uniform(vertices);
                int vertexId2 = StdRandom.uniform(vertices);

                double gaussianRandomWeight = StdRandom.gaussian();
                DirectedEdge newEdge;

                int randomDirection = StdRandom.uniform(2);
                if (randomDirection == 0) {
                    newEdge = new DirectedEdge(vertexId1, vertexId2, gaussianRandomWeight);
                } else {
                    newEdge = new DirectedEdge(vertexId2, vertexId1, gaussianRandomWeight);
                }

                randomEdgeWeightedDigraph.addEdge(newEdge);
            }

            return randomEdgeWeightedDigraph;
        }
    }

    //A graph is considered sparse if its number of edges is within a small constant factor of V
    public List<EdgeWeightedDigraphInterface> randomSparseEdgeWeightedDigraphsGenerator(int numberOfDigraphs,
                                                                                        boolean uniformWeightDistribution) {
        if (numberOfDigraphs < 0) {
            throw new IllegalArgumentException("Number of digraphs cannot be negative");
        }

        RandomEdgeWeightedDigraphs randomEdgeWeightedDigraphsGenerator = new RandomEdgeWeightedDigraphs();

        List<EdgeWeightedDigraphInterface> randomEdgeWeightedDigraphs = new ArrayList<>();
        int[] digraphVerticesCount = {10, 100, 1000, 10000};

        for(int digraph = 0; digraph < numberOfDigraphs; digraph++) {
            int vertices = digraphVerticesCount[digraph % 4];
            int edges = vertices * 3;

            EdgeWeightedDigraphInterface randomEdgeWeightedDigraph;

            if (uniformWeightDistribution) {
                randomEdgeWeightedDigraph =
                        randomEdgeWeightedDigraphsGenerator.erdosRenyiDigraphUniformWeights(vertices, edges);
            } else {
                randomEdgeWeightedDigraph =
                        randomEdgeWeightedDigraphsGenerator.erdosRenyiDigraphGaussianWeights(vertices, edges);
            }

            randomEdgeWeightedDigraphs.add(randomEdgeWeightedDigraph);
        }

        return randomEdgeWeightedDigraphs;
    }

    //Parameter example: 100
    public static void main(String[] args) {
        int numberOfDigraphs = Integer.parseInt(args[0]);

        Exercise49_RandomSparseEdgeWeightedDigraphs randomSparseEdgeWeightedDigraphs =
                new Exercise49_RandomSparseEdgeWeightedDigraphs();

        List<EdgeWeightedDigraphInterface> randomSparseEdgeWeightedDigraphsUniform =
                randomSparseEdgeWeightedDigraphs.randomSparseEdgeWeightedDigraphsGenerator(numberOfDigraphs, true);
        StdOut.println("Random-sparse-edge-weighted-digraphs with uniform weights distribution generated: "
                + (randomSparseEdgeWeightedDigraphsUniform.size() == numberOfDigraphs));

        List<EdgeWeightedDigraphInterface> randomSparseEdgeWeightedDigraphsGaussian =
                randomSparseEdgeWeightedDigraphs.randomSparseEdgeWeightedDigraphsGenerator(numberOfDigraphs, false);
        StdOut.println("Random-sparse-edge-weighted-digraphs with gaussian weights distribution generated: "
                + (randomSparseEdgeWeightedDigraphsGaussian.size() == numberOfDigraphs));
    }

}
