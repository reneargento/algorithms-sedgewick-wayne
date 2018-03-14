package chapter4.section4;

import chapter1.section5.UnionFind;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.MathUtil;

import java.util.*;

/**
 * Created by Rene Argento on 27/12/17.
 */
@SuppressWarnings("unchecked")
public class Exercise50_RandomEuclideanEdgeWeightedDigraphs {

    public List<EdgeWeightedDigraphInterface> generateRandomEuclideanEdgeWeightedDigraphs(int numberOfDigraphs,
                                                                                          int vertices, double radius) {
        if (numberOfDigraphs < 0) {
            throw new IllegalArgumentException("Number of digraphs cannot be negative");
        }

        List<EdgeWeightedDigraphInterface> randomEuclideanEdgeWeightedDigraphs = new ArrayList<>();

        for(int digraph = 0; digraph < numberOfDigraphs; digraph++) {
            EdgeWeightedDigraphInterface randomEuclideanEdgeWeightedDigraph =
                    randomEuclideanEdgeWeightedDigraph(vertices, radius);
            randomEuclideanEdgeWeightedDigraphs.add(randomEuclideanEdgeWeightedDigraph);
        }

        return randomEuclideanEdgeWeightedDigraphs;
    }

    public EdgeWeightedDigraphInterface randomEuclideanEdgeWeightedDigraph(int vertices, double radius) {

        Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph randomEuclideanEdgeWeightedDigraph =
                new Exercise27_ShortestPathsInEuclideanGraphs().new EuclideanEdgeWeightedDigraph(vertices);

        Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex[] allVertices =
                new Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex[vertices];

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            double randomXCoordinate = StdRandom.uniform();
            double randomYCoordinate = StdRandom.uniform();

            Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex vertex =
                    randomEuclideanEdgeWeightedDigraph.new Vertex(vertexId, randomXCoordinate, randomYCoordinate);
            allVertices[vertexId] = vertex;

            randomEuclideanEdgeWeightedDigraph.addVertex(vertex);
        }

        for(int vertexId1 = 0; vertexId1 < vertices; vertexId1++) {
            for(int vertexId2 = vertexId1 + 1; vertexId2 < vertices; vertexId2++) {

                double distance = MathUtil.distanceBetweenPoints(allVertices[vertexId1].coordinates.getXCoordinate(),
                        allVertices[vertexId1].coordinates.getYCoordinate(),
                        allVertices[vertexId2].coordinates.getXCoordinate(),
                        allVertices[vertexId2].coordinates.getYCoordinate());

                if (distance <= radius) {
                    DirectedEdge edge;

                    int randomDirection = StdRandom.uniform(2);
                    if (randomDirection == 0) {
                        edge = new DirectedEdge(vertexId1, vertexId2, distance);
                    } else {
                        edge = new DirectedEdge(vertexId2, vertexId1, distance);
                    }

                    randomEuclideanEdgeWeightedDigraph.addEdge(edge);
                }
            }
        }

        return randomEuclideanEdgeWeightedDigraph;
    }

    //Parameters example: 6 0.5 100
    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        double radius = Double.parseDouble(args[1]);
        int numberOfDigraphs = Integer.parseInt(args[2]);

        List<EdgeWeightedDigraphInterface> randomEuclideanEdgeWeightedDigraphs =
                new Exercise50_RandomEuclideanEdgeWeightedDigraphs().generateRandomEuclideanEdgeWeightedDigraphs(numberOfDigraphs,
                        vertices, radius);

        EdgeWeightedDigraphInterface firstEuclideanEdgeWeightedDigraph = randomEuclideanEdgeWeightedDigraphs.get(0);
        // Not used in EdgeWeightedDigraphInterface
        //firstEuclideanEdgeWeightedDigraph.show(-0.1, 1.1, -0.1, 1.1, 0.03, 0.01, 0.03);

        UnionFind unionFind = new UnionFind(vertices);

        for(int vertex = 0; vertex < vertices; vertex++) {
            for(DirectedEdge edge : firstEuclideanEdgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();
                unionFind.union(vertex, neighbor);
            }
        }

        // Check theory that if "radius" is larger than threshold value SQRT(ln(V) / (Math.PI * V)) then the graph is
        // almost certainly connected. Otherwise, it is almost certainly disconnected.
        double thresholdValue = Math.sqrt(Math.log(vertices) / (Math.PI * vertices));

        StdOut.println("Expected to be connected (if it were an undirected graph): " + (radius > thresholdValue));
        StdOut.println("Would be connected (if it were an undirected graph): " + (unionFind.count() == 1));

        StdOut.println("\nRandom Euclidean edge-weighted digraph:\n");
        StdOut.println(firstEuclideanEdgeWeightedDigraph);
    }

}
