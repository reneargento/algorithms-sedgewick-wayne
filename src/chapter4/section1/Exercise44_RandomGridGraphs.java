package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by rene on 08/10/17.
 */
public class Exercise44_RandomGridGraphs {

    private class Edge {
        int vertex1;
        int vertex2;

        Edge(int vertex1, int vertex2) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
        }
    }

    public Exercise37_EuclideanGraphs.EuclideanGraph randomGridGraph(int vertices, int extraEdges) {

        if(Math.sqrt(vertices) != (int) Math.sqrt(vertices)) {
            throw new IllegalArgumentException("Vertex number must have an integer square root");
        }

        // Instance used only to create the vertices
        Exercise37_EuclideanGraphs.EuclideanGraph euclideanGraph =
                new Exercise37_EuclideanGraphs().new EuclideanGraph(0);

        int vertexNumberSqrt = (int) Math.sqrt(vertices);
        int originalVertexNumberSqrt = vertexNumberSqrt;

        // Create the grid
        Exercise37_EuclideanGraphs.EuclideanGraph.Vertex[][] verticesGrid =
                new Exercise37_EuclideanGraphs.EuclideanGraph.Vertex[vertexNumberSqrt][vertexNumberSqrt];

        Exercise37_EuclideanGraphs.EuclideanGraph.Vertex[] allVertices =
                new Exercise37_EuclideanGraphs.EuclideanGraph.Vertex[vertices];

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            double randomXCoordinate = StdRandom.uniform();
            double randomYCoordinate = StdRandom.uniform();

            Exercise37_EuclideanGraphs.EuclideanGraph.Vertex vertex =
                    euclideanGraph.new Vertex(vertexId, randomXCoordinate, randomYCoordinate);
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
                extraEdgesList.add(new Edge(randomVertexId1, randomVertexId2));
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
                    if(removedVertices.contains(extraEdge.vertex1) || removedVertices.contains(extraEdge.vertex2)) {
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
        Exercise37_EuclideanGraphs.EuclideanGraph randomEuclideanGridGraph =
                new Exercise37_EuclideanGraphs().new EuclideanGraph(vertices);

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            int[] cellRowAndColumn = getCellRowAndColumn(vertexId, vertexNumberSqrt);
            Exercise37_EuclideanGraphs.EuclideanGraph.Vertex vertex = verticesGrid[cellRowAndColumn[0]][cellRowAndColumn[1]];

            randomEuclideanGridGraph.addVertex(vertex);
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
                            randomEuclideanGridGraph.addEdge(vertexId1, vertexId2);
                        }
                    }
                }
            }
        }

        //Add extra edges
        for(Edge extraEdge : extraEdgesList) {
            // We have to access allVertices[] here because it has the updated vertex ids (in the cases where graph
            // shrinking occurred).
            randomEuclideanGridGraph.addEdge(allVertices[extraEdge.vertex1].id, allVertices[extraEdge.vertex2].id);
        }

        return randomEuclideanGridGraph;
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

        Exercise37_EuclideanGraphs.EuclideanGraph randomGridGraph =
                new Exercise44_RandomGridGraphs().randomGridGraph(vertices, extraEdges);
        randomGridGraph.show(-0.1, 1.1, -0.1, 1.1, 0.03);

        StdOut.println(randomGridGraph);
    }

}
