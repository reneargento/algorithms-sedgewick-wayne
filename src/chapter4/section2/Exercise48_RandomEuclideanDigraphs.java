package chapter4.section2;

import chapter1.section5.UnionFind;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 31/10/17.
 */
public class Exercise48_RandomEuclideanDigraphs {

    public List<Exercise38_EuclideanDigraphs.EuclideanDigraph> generateRandomEuclideanDigraphs(int numberOfDigraphs,
                                                                                         int vertices, double radius) {

        if (numberOfDigraphs < 0) {
            throw new IllegalArgumentException("Number of digraphs cannot be negative");
        }

        List<Exercise38_EuclideanDigraphs.EuclideanDigraph> randomEuclideanDigraphs = new ArrayList<>();
        for(int digraph = 0; digraph < numberOfDigraphs; digraph++) {
            Exercise38_EuclideanDigraphs.EuclideanDigraph randomEuclideanDigraph = randomEuclideanDigraph(vertices, radius);
            randomEuclideanDigraphs.add(randomEuclideanDigraph);
        }

        return randomEuclideanDigraphs;
    }

    public Exercise38_EuclideanDigraphs.EuclideanDigraph randomEuclideanDigraph(int vertices, double radius) {
        Exercise38_EuclideanDigraphs.EuclideanDigraph randomEuclideanDigraph =
                new Exercise38_EuclideanDigraphs().new EuclideanDigraph(vertices);

        Exercise38_EuclideanDigraphs.EuclideanDigraph.Vertex[] allVertices =
                new Exercise38_EuclideanDigraphs.EuclideanDigraph.Vertex[vertices];

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            double randomXCoordinate = StdRandom.uniform();
            double randomYCoordinate = StdRandom.uniform();

            Exercise38_EuclideanDigraphs.EuclideanDigraph.Vertex vertex =
                    randomEuclideanDigraph.new Vertex(vertexId, randomXCoordinate, randomYCoordinate);
            allVertices[vertexId] = vertex;

            randomEuclideanDigraph.addVertex(vertex);
        }

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            for(int otherVertex = vertexId + 1; otherVertex < vertices; otherVertex++) {
                double distance = MathUtil.distanceBetweenPoints(allVertices[vertexId].coordinates.getXCoordinate(),
                        allVertices[vertexId].coordinates.getYCoordinate(),
                        allVertices[otherVertex].coordinates.getXCoordinate(),
                        allVertices[otherVertex].coordinates.getYCoordinate());

                if (distance <= radius) {
                    int randomDirection = StdRandom.uniform(2);
                    if (randomDirection == 0) {
                        randomEuclideanDigraph.addEdge(vertexId, otherVertex);
                    } else {
                        randomEuclideanDigraph.addEdge(otherVertex, vertexId);
                    }
                }
            }
        }

        return randomEuclideanDigraph;
    }

    //Parameters example: 6 0.5 100
    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        double radius = Double.parseDouble(args[1]);
        int numberOfDigraphs = Integer.parseInt(args[2]);

        List<Exercise38_EuclideanDigraphs.EuclideanDigraph> randomEuclideanDigraphs =
                new Exercise48_RandomEuclideanDigraphs().generateRandomEuclideanDigraphs(numberOfDigraphs, vertices,
                        radius);

        Exercise38_EuclideanDigraphs.EuclideanDigraph firstEuclideanDigraph = randomEuclideanDigraphs.get(0);
        firstEuclideanDigraph.show(-0.1, 1.1, -0.1, 1.1, 0.03, 0.01, 0.03);

        UnionFind unionFind = new UnionFind(vertices);

        for(int vertex = 0; vertex < vertices; vertex++) {
            for(int neighbor : firstEuclideanDigraph.adjacent(vertex)) {
                unionFind.union(vertex, neighbor);
            }
        }

        // Check theory that if "radius" is larger than threshold value SQRT(ln(V) / (Math.PI * V)) then the graph is
        // almost certainly connected. Otherwise, it is almost certainly disconnected.
        double thresholdValue = Math.sqrt(Math.log(vertices) / (Math.PI * vertices));

        StdOut.println("Expected to be connected (if it were an undirected graph): " + (radius > thresholdValue));
        StdOut.println("Would be connected (if it were an undirected graph): " + (unionFind.count() == 1));
    }

}
