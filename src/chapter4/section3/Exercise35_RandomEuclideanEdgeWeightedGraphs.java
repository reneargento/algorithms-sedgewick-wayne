package chapter4.section3;

import chapter1.section5.UnionFind;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 12/11/17.
 */
public class Exercise35_RandomEuclideanEdgeWeightedGraphs {

    public List<Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph> generateRandomEuclideanEdgeWeightedGraphs(
            int numberOfGraphs, int vertices, double radius) {

        if (numberOfGraphs < 0) {
            throw new IllegalArgumentException("Number of graphs cannot be negative");
        }

        List<Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph> randomEuclideanEdgeWeightedGraphs =
                new ArrayList<>();

        for(int graph = 0; graph < numberOfGraphs; graph++) {
            Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph randomEuclideanEdgeWeightedGraph =
                    randomEuclideanEdgeWeightedGraph(vertices, radius);
            randomEuclideanEdgeWeightedGraphs.add(randomEuclideanEdgeWeightedGraph);
        }

        return randomEuclideanEdgeWeightedGraphs;
    }

    public Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph randomEuclideanEdgeWeightedGraph(int vertices,
                                                                                                          double radius) {
        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph randomEuclideanEdgeWeightedGraph =
                new Exercise30_EuclideanWeightedGraphs().new EuclideanEdgeWeightedGraph(vertices);

        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex[] allVertices =
                new Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex[vertices];

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            double randomXCoordinate = StdRandom.uniform();
            double randomYCoordinate = StdRandom.uniform();

            Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex vertex =
                    randomEuclideanEdgeWeightedGraph.new Vertex(vertexId, randomXCoordinate, randomYCoordinate);
            allVertices[vertexId] = vertex;

            randomEuclideanEdgeWeightedGraph.addVertex(vertex);
        }

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            for(int otherVertexId = vertexId + 1; otherVertexId < vertices; otherVertexId++) {
                double distance = MathUtil.distanceBetweenPoints(allVertices[vertexId].xCoordinate,
                        allVertices[vertexId].yCoordinate,
                        allVertices[otherVertexId].xCoordinate, allVertices[otherVertexId].yCoordinate);

                if (distance <= radius) {
                    Edge edge = new Edge(vertexId, otherVertexId, distance);
                    randomEuclideanEdgeWeightedGraph.addEdge(edge);
                }
            }
        }

        return randomEuclideanEdgeWeightedGraph;
    }

    //Parameters example: 6 0.5 100
    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        double radius = Double.parseDouble(args[1]);
        int numberOfGraphs = Integer.parseInt(args[2]);

        List<Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph> randomEuclideanGraphs =
                new Exercise35_RandomEuclideanEdgeWeightedGraphs()
                        .generateRandomEuclideanEdgeWeightedGraphs(numberOfGraphs, vertices, radius);

        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph firstEuclideanEdgeWeightedGraph =
                randomEuclideanGraphs.get(0);
        firstEuclideanEdgeWeightedGraph.show(-0.1, 1.1, -0.1, 1.1, 0.03);

        UnionFind unionFind = new UnionFind(vertices);

        for(int vertex = 0; vertex < vertices; vertex++) {
            for(Edge edge : firstEuclideanEdgeWeightedGraph.adjacent(vertex)) {
                int neighbor = edge.other(vertex);
                unionFind.union(vertex, neighbor);
            }
        }

        // Check theory that if "radius" is larger than threshold value SQRT(ln(V) / (Math.PI * V)) then the graph is
        // almost certainly connected. Otherwise, it is almost certainly disconnected.
        double thresholdValue = Math.sqrt(Math.log(vertices) / (Math.PI * vertices));

        StdOut.println("Expected to be connected: " + (radius > thresholdValue));
        StdOut.println("Is connected: " + (unionFind.count() == 1));
    }

}
