package chapter4.section1;

import chapter1.section5.UnionFind;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 08/10/17.
 */
public class Exercise43_RandomEuclideanGraphs {

    public List<Exercise37_EuclideanGraphs.EuclideanGraph> generateRandomEuclideanGraphs(int numberOfGraphs,
                                                                                         int vertices, double radius) {
        if (numberOfGraphs < 0) {
            throw new IllegalArgumentException("Number of graphs cannot be negative");
        }

        List<Exercise37_EuclideanGraphs.EuclideanGraph> randomEuclideanGraphs = new ArrayList<>();
        for(int graph = 0; graph < numberOfGraphs; graph++) {
            Exercise37_EuclideanGraphs.EuclideanGraph randomEuclideanGraph = randomEuclideanGraph(vertices, radius);
            randomEuclideanGraphs.add(randomEuclideanGraph);
        }

        return randomEuclideanGraphs;
    }

    public Exercise37_EuclideanGraphs.EuclideanGraph randomEuclideanGraph(int vertices, double radius) {
        Exercise37_EuclideanGraphs.EuclideanGraph randomEuclideanGraph =
                new Exercise37_EuclideanGraphs().new EuclideanGraph(vertices);

        Exercise37_EuclideanGraphs.EuclideanGraph.Vertex[] allVertices =
                new Exercise37_EuclideanGraphs.EuclideanGraph.Vertex[vertices];

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            double randomXCoordinate = StdRandom.uniform();
            double randomYCoordinate = StdRandom.uniform();

            Exercise37_EuclideanGraphs.EuclideanGraph.Vertex vertex =
                    randomEuclideanGraph.new Vertex(vertexId, randomXCoordinate, randomYCoordinate);
            allVertices[vertexId] = vertex;

            randomEuclideanGraph.addVertex(vertex);
        }

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            for(int otherVertex = vertexId + 1; otherVertex < vertices; otherVertex++) {
                double distance = MathUtil.distanceBetweenPoints(allVertices[vertexId].xCoordinate,
                        allVertices[vertexId].yCoordinate, allVertices[otherVertex].xCoordinate,
                        allVertices[otherVertex].yCoordinate);

                if (distance <= radius) {
                    randomEuclideanGraph.addEdge(vertexId, otherVertex);
                }
            }
        }

        return randomEuclideanGraph;
    }

    //Parameters example: 6 0.5 100
    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        double radius = Double.parseDouble(args[1]);
        int numberOfGraphs = Integer.parseInt(args[2]);

        List<Exercise37_EuclideanGraphs.EuclideanGraph> randomEuclideanGraphs =
                new Exercise43_RandomEuclideanGraphs().generateRandomEuclideanGraphs(numberOfGraphs, vertices, radius);

        Exercise37_EuclideanGraphs.EuclideanGraph firstEuclideanGraph = randomEuclideanGraphs.get(0);
        firstEuclideanGraph.show(-0.1, 1.1, -0.1, 1.1, 0.03);

        UnionFind unionFind = new UnionFind(vertices);

        for(int vertex = 0; vertex < vertices; vertex++) {
            for(int neighbor : firstEuclideanGraph.adjacent(vertex)) {
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
