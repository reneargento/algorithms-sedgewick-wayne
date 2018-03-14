package chapter4.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 12/11/17.
 */
public class Exercise34_RandomSparseEdgeWeightedGraphs {

    public class RandomEdgeWeightedGraphs {

        public EdgeWeightedGraph erdosRenyiGraphUniformWeights(int vertices, int edges) {
            EdgeWeightedGraph randomEdgeWeightedGraph = new EdgeWeightedGraph(vertices);

            for(int edge = 0; edge < edges; edge++) {
                int vertexId1 = StdRandom.uniform(vertices);
                int vertexId2 = StdRandom.uniform(vertices);

                double uniformRandomWeight = StdRandom.uniform();
                Edge newEdge = new Edge(vertexId1, vertexId2, uniformRandomWeight);

                randomEdgeWeightedGraph.addEdge(newEdge);
            }

            return randomEdgeWeightedGraph;
        }

        public EdgeWeightedGraph erdosRenyiGraphGaussianWeights(int vertices, int edges) {
            EdgeWeightedGraph randomEdgeWeightedGraph = new EdgeWeightedGraph(vertices);

            for(int edge = 0; edge < edges; edge++) {
                int vertexId1 = StdRandom.uniform(vertices);
                int vertexId2 = StdRandom.uniform(vertices);

                double gaussianRandomWeight = StdRandom.gaussian();
                Edge newEdge = new Edge(vertexId1, vertexId2, gaussianRandomWeight);

                randomEdgeWeightedGraph.addEdge(newEdge);
            }

            return randomEdgeWeightedGraph;
        }
    }

    //A graph is considered sparse if its number of edges is within a small constant factor of V
    public List<EdgeWeightedGraph> randomSparseEdgeWeightedGraphsGenerator(int numberOfGraphs,
                                                                           boolean uniformWeightDistribution) {
        if (numberOfGraphs < 0) {
            throw new IllegalArgumentException("Number of graphs cannot be negative");
        }

        RandomEdgeWeightedGraphs randomEdgeWeightedGraphsGenerator = new RandomEdgeWeightedGraphs();

        List<EdgeWeightedGraph> randomEdgeWeightedGraphs = new ArrayList<>();
        int[] graphVerticesCount = {10, 100, 1000, 10000};

        for(int graph = 0; graph < numberOfGraphs; graph++) {
            int vertices = graphVerticesCount[graph % 4];
            int edges = vertices * 3;

            EdgeWeightedGraph randomEdgeWeightedGraph;

            if (uniformWeightDistribution) {
                randomEdgeWeightedGraph =
                        randomEdgeWeightedGraphsGenerator.erdosRenyiGraphUniformWeights(vertices, edges);
            } else {
                randomEdgeWeightedGraph =
                        randomEdgeWeightedGraphsGenerator.erdosRenyiGraphGaussianWeights(vertices, edges);
            }

            randomEdgeWeightedGraphs.add(randomEdgeWeightedGraph);
        }

        return randomEdgeWeightedGraphs;
    }

    //Parameter example: 100
    public static void main(String[] args) {
        int numberOfGraphs = Integer.parseInt(args[0]);

        Exercise34_RandomSparseEdgeWeightedGraphs randomSparseEdgeWeightedGraphs =
                new Exercise34_RandomSparseEdgeWeightedGraphs();

        List<EdgeWeightedGraph> randomSparseEdgeWeightedGraphsUniform =
                randomSparseEdgeWeightedGraphs.randomSparseEdgeWeightedGraphsGenerator(numberOfGraphs, true);
        StdOut.println("Random-sparse-edge-weighted-graphs with uniform weights distribution generated: "
                + (randomSparseEdgeWeightedGraphsUniform.size() == numberOfGraphs));

        List<EdgeWeightedGraph> randomSparseEdgeWeightedGraphsGaussian =
                randomSparseEdgeWeightedGraphs.randomSparseEdgeWeightedGraphsGenerator(numberOfGraphs, false);
        StdOut.println("Random-sparse-edge-weighted-graphs with gaussian weights distribution generated: "
                + (randomSparseEdgeWeightedGraphsGaussian.size() == numberOfGraphs));
    }

}
