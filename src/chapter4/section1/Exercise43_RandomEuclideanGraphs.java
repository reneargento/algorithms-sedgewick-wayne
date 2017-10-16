package chapter4.section1;

import chapter1.section5.UnionFind;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by rene on 08/10/17.
 */
public class Exercise43_RandomEuclideanGraphs {

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

        UnionFind unionFind = new UnionFind(vertices);

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            for(int otherVertex = vertexId + 1; otherVertex < vertices; otherVertex++) {
                double distance = distanceBetweenPoints(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                        allVertices[otherVertex].xCoordinate, allVertices[otherVertex].yCoordinate);

                if(distance <= radius) {
                    randomEuclideanGraph.addEdge(allVertices[vertexId], allVertices[otherVertex]);
                    unionFind.union(vertexId, otherVertex);
                }
            }
        }

        //Check theory that if "radius" is larger than threshold value SQRT(ln(V) / (Math.PI * V)) then it is
        // almost certainly connected. Otherwise, it is almost certainly disconnected.
        double thresholdValue = Math.sqrt(Math.log(vertices) / (Math.PI * vertices));

        StdOut.println("Expected to be connected: " + (radius > thresholdValue));
        StdOut.println("Is connected: " + (unionFind.count() == 1));

        return randomEuclideanGraph;
    }

    private double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    //Parameters example: 6 0.5
    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        double radius = Double.parseDouble(args[1]);

        Exercise37_EuclideanGraphs.EuclideanGraph randomEuclideanGraph =
                new Exercise43_RandomEuclideanGraphs().randomEuclideanGraph(vertices, radius);
        randomEuclideanGraph.show(-0.1, 1.1, -0.1, 1.1, 0.03);
    }

}
