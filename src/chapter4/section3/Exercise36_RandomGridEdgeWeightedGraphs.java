package chapter4.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by rene on 12/11/17.
 */
public class Exercise36_RandomGridEdgeWeightedGraphs {

    public Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph randomGridEdgeWeightedGraph(int vertices,
                                                                                                     int extraEdges) {

        if(Math.sqrt(vertices) != (int) Math.sqrt(vertices)) {
            throw new IllegalArgumentException("Vertex number must have an integer square root");
        }

        // Instance used only to create the vertices and edges
        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph euclideanEdgeWeightedGraph =
                new Exercise30_EuclideanWeightedGraphs().new EuclideanEdgeWeightedGraph(0);

        int vertexNumberSqrt = (int) Math.sqrt(vertices);
        int originalVertexNumberSqrt = vertexNumberSqrt;

        // Create the grid
        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex[][] verticesGrid =
                new Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex[vertexNumberSqrt][vertexNumberSqrt];

        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex[] allVertices =
                new Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex[vertices];

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            double randomXCoordinate = StdRandom.uniform();
            double randomYCoordinate = StdRandom.uniform();

            Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex vertex =
                    euclideanEdgeWeightedGraph.new Vertex(vertexId, randomXCoordinate, randomYCoordinate);
            allVertices[vertexId] = vertex;

            int[] cellRowAndColumn = getCellRowAndColumn(vertexId, vertexNumberSqrt);
            verticesGrid[cellRowAndColumn[0]][cellRowAndColumn[1]] = vertex;
        }

        // Create the random extra edges
        List<Edge> extraEdgesList = new ArrayList<>();

        while(extraEdges > 0) {
            int randomVertexId1 = StdRandom.uniform(vertices);
            int randomVertexId2 = StdRandom.uniform(vertices);

            //The probability of connecting a vertex s to a vertex t has probability inversely proportional
            // to the Euclidean distance between s and t
            double distance = distanceBetweenPoints(allVertices[randomVertexId1].xCoordinate,
                    allVertices[randomVertexId1].yCoordinate, allVertices[randomVertexId2].xCoordinate,
                    allVertices[randomVertexId2].yCoordinate);

            boolean shouldConnect = 1 - StdRandom.uniform() >= distance;
            if(shouldConnect) {
                double randomEdgeWeight = StdRandom.uniform();

                extraEdgesList.add(new Edge(randomVertexId1, randomVertexId2, randomEdgeWeight));
                extraEdges--;
            }
        }

        int shrunkTimes = 0;

        // Check if the grid will have to be shrunk
        while (true) {
            //Each cell in the bottom row and right column has one edge
            // Bottom row cells have edges on their right (except the cell n - 1, n - 1)
            int bottomRowEdges = vertexNumberSqrt - 1;
            // Right column cells have edges bellow them (except the cell n - 1, n - 1)
            int rightColumnEdges = vertexNumberSqrt - 1;

            // All other cells have 2 edges (below them and to the right), except cell (n - 1, n - 1)
            int otherCellsEdges = (vertices - bottomRowEdges - rightColumnEdges - 1) * 2;

            int totalNonExtraEdges = bottomRowEdges + rightColumnEdges + otherCellsEdges;
            int totalEdges = totalNonExtraEdges + extraEdgesList.size();

            // We are assuming that the "about 2V" in the exercise description is within 20% of 2V
            // If R is too large, we shrink the grid to decrease the number of vertices and edges
            if(totalEdges > 1.2 * (2 * vertices)) {
                vertexNumberSqrt--;
                vertices = vertexNumberSqrt * vertexNumberSqrt;
                shrunkTimes++;

                HashSet<Integer> removedVertices = new HashSet<>();
                int row = 0;

                for(int i = 0; i < vertexNumberSqrt; i++) {
                    row++;
                    int removedVertexId = (row * originalVertexNumberSqrt) - shrunkTimes;
                    removedVertices.add(removedVertexId);
                }

                int nextVertexRemoved = row * originalVertexNumberSqrt;

                //Add last row
                for(int i = 0; i < vertexNumberSqrt + 1; i++) {
                    removedVertices.add(nextVertexRemoved);
                    nextVertexRemoved++;
                }

                List<Edge> extraEdgesToRemoveAfterShrinking = new ArrayList<>();
                for(Edge extraEdge : extraEdgesList) {
                    int vertexId1 = extraEdge.either();
                    int vertexId2 = extraEdge.other(vertexId1);

                    if(removedVertices.contains(vertexId1) || removedVertices.contains(vertexId2)) {
                        extraEdgesToRemoveAfterShrinking.add(extraEdge);
                    }
                }

                extraEdgesList.removeAll(extraEdgesToRemoveAfterShrinking);
            } else {
                break;
            }

            // The smallest grid we can have is a 2x2 grid, so if we reach this grid dimensions we break
            if(vertexNumberSqrt == 2) {
                break;
            }
        }

        // Update vertex IDs in the grid if any shrink occurred
        if(shrunkTimes > 0) {
            int currentVertexId = 0;

            for(int row = 0; row < vertexNumberSqrt; row++) {
                for (int column = 0; column < vertexNumberSqrt; column++) {
                    allVertices[verticesGrid[row][column].id].id = currentVertexId;
                    verticesGrid[row][column].id = currentVertexId;
                    verticesGrid[row][column].updateName(String.valueOf(currentVertexId));

                    currentVertexId++;
                }
            }
        }

        // Create the graph
        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph randomEuclideanGridEdgeWeightedGraph =
                new Exercise30_EuclideanWeightedGraphs().new EuclideanEdgeWeightedGraph(vertices);

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            int[] cellRowAndColumn = getCellRowAndColumn(vertexId, vertexNumberSqrt);
            Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex vertex =
                    verticesGrid[cellRowAndColumn[0]][cellRowAndColumn[1]];

            randomEuclideanGridEdgeWeightedGraph.addVertex(vertex);
        }

        // Connect vertices to their neighbors
        int[] neighborRows = {-1, 1, 0 ,0};
        int[] neighborColumns = {0, 0, -1 ,1};

        for(int row = 0; row < vertexNumberSqrt; row++) {
            for(int column = 0; column < vertexNumberSqrt; column++) {
                for(int i = 0; i < 4; i++) {
                    int neighborRow = row + neighborRows[i];
                    int neighborColumn = column + neighborColumns[i];

                    if(isValidCell(vertexNumberSqrt, neighborRow, neighborColumn)) {
                        int vertexId1 = getVertexId(row, column, vertexNumberSqrt);
                        int vertexId2 = getVertexId(neighborRow, neighborColumn, vertexNumberSqrt);

                        //Used to avoid connecting vertices more than once
                        if(vertexId1 < vertexId2) {
                            double randomEdgeWeight = StdRandom.uniform();
                            Edge edge = new Edge(vertexId1, vertexId2, randomEdgeWeight);
                            randomEuclideanGridEdgeWeightedGraph.addEdge(edge);
                        }
                    }
                }
            }
        }

        //Add extra edges
        for(Edge extraEdge : extraEdgesList) {
            int vertexId1 = extraEdge.either();
            int vertexId2 = extraEdge.other(vertexId1);

            // We have to access allVertices[] here because it has the updated vertex ids (in the cases where graph
            // shrinking occurred).
            Edge edge = new Edge(allVertices[vertexId1].id, allVertices[vertexId2].id, extraEdge.weight());
            randomEuclideanGridEdgeWeightedGraph.addEdge(edge);
        }

        return randomEuclideanGridEdgeWeightedGraph;
    }

    private boolean isValidCell(int dimensionSize, int row, int column) {
        return row >= 0 && row < dimensionSize && column >= 0 && column < dimensionSize;
    }

    private int getVertexId(int row, int column, int columns) {
        return row * columns + column;
    }

    private int[] getCellRowAndColumn(int vertexId, int columns) {
        int[] cellRowAndColumn = new int[2];
        cellRowAndColumn[0] = vertexId / columns; // Row
        cellRowAndColumn[1] = vertexId % columns; // Column

        return cellRowAndColumn;
    }

    private double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    //Parameters example: 9 5
    //                    16 40
    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        int extraEdges = Integer.parseInt(args[1]);

        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph randomGridEdgeWeightedGraph =
                new Exercise36_RandomGridEdgeWeightedGraphs().randomGridEdgeWeightedGraph(vertices, extraEdges);
        randomGridEdgeWeightedGraph.show(-0.1, 1.1, -0.1, 1.1, 0.03);

        StdOut.println(randomGridEdgeWeightedGraph);
    }

}
