package chapter4.section2;

import chapter1.section5.UnionFind;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by rene on 31/10/17.
 */
public class Exercise48_RandomEuclideanDigraphs {

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

        UnionFind unionFind = new UnionFind(vertices);

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            for(int otherVertex = vertexId + 1; otherVertex < vertices; otherVertex++) {
                double distance = distanceBetweenPoints(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                        allVertices[otherVertex].xCoordinate, allVertices[otherVertex].yCoordinate);

                if(distance <= radius) {
                    int randomDirection = StdRandom.uniform(2);
                    if(randomDirection == 0) {
                        randomEuclideanDigraph.addEdge(vertexId, otherVertex);
                    } else {
                        randomEuclideanDigraph.addEdge(otherVertex, vertexId);
                    }

                    unionFind.union(vertexId, otherVertex);
                }
            }
        }

        //Check theory that if "radius" is larger than threshold value SQRT(ln(V) / (Math.PI * V)) then it is
        // almost certainly connected. Otherwise, it is almost certainly disconnected.
        double thresholdValue = Math.sqrt(Math.log(vertices) / (Math.PI * vertices));

        StdOut.println("Expected to be connected (if it were an undirected graph): " + (radius > thresholdValue));
        StdOut.println("Would be connected (if it were an undirected graph): " + (unionFind.count() == 1));

        return randomEuclideanDigraph;
    }

    private double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    //Parameters example: 6 0.5
    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        double radius = Double.parseDouble(args[1]);

        Exercise38_EuclideanDigraphs.EuclideanDigraph randomEuclideanDigraph =
                new Exercise48_RandomEuclideanDigraphs().randomEuclideanDigraph(vertices, radius);
        randomEuclideanDigraph.show(-0.1, 1.1, -0.1, 1.1, 0.01, 0.03);
    }

}
