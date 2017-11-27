package chapter4.section3;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by rene on 12/11/17.
 */
public class Exercise35_RandomEuclideanEdgeWeightedGraphs {

    public Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph randomEuclideanEdgeWeightedGraph(int vertices) {
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
                double distance = distanceBetweenPoints(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                        allVertices[otherVertexId].xCoordinate, allVertices[otherVertexId].yCoordinate);

                Edge edge = new Edge(vertexId, otherVertexId, distance);
                randomEuclideanEdgeWeightedGraph.addEdge(edge);
            }
        }

        return randomEuclideanEdgeWeightedGraph;
    }

    private double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    //Parameters example: 6
    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);

        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph euclideanEdgeWeightedGraph =
                new Exercise35_RandomEuclideanEdgeWeightedGraphs().randomEuclideanEdgeWeightedGraph(vertices);
        euclideanEdgeWeightedGraph.show(-0.1, 1.1, -0.1, 1.1, 0.03);
    }

}
