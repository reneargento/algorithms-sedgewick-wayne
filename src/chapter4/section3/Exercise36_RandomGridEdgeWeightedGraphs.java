package chapter4.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.MathUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Rene Argento on 12/11/17.
 */
public class Exercise36_RandomGridEdgeWeightedGraphs {

    public List<Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph> generateRandomGridEdgeWeightedGraphs(
            int numberOfGraphs, int vertices, int extraEdges) {

        if (numberOfGraphs < 0) {
            throw new IllegalArgumentException("Number of graphs cannot be negative");
        }

        List<Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph> randomGridEdgeWeightedGraphs =
                new ArrayList<>();

        for(int graph = 0; graph < numberOfGraphs; graph++) {
            Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph randomGridEdgeWeightedGraph =
                    randomGridEdgeWeightedGraph(vertices, extraEdges);
            randomGridEdgeWeightedGraphs.add(randomGridEdgeWeightedGraph);
        }

        return randomGridEdgeWeightedGraphs;
    }

    public Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph randomGridEdgeWeightedGraph(int vertices,
                                                                                                     int extraEdges) {
        if (Math.sqrt(vertices) != (int) Math.sqrt(vertices)) {
            throw new IllegalArgumentException("Vertex number must have an integer square root");
        }

        // Instance used only to create the vertices and edges
        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph euclideanEdgeWeightedGraph =
                new Exercise30_EuclideanWeightedGraphs().new EuclideanEdgeWeightedGraph(0);

        int vertexNumberSqrt = (int) Math.sqrt(vertices);

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

        // Generate extra random edges
        List<Edge> extraEdgesList = getRandomExtraEdges(extraEdges, vertices, allVertices);

        int[] shrinkResult = shrinkGraphIfNecessary(vertexNumberSqrt, vertices, extraEdgesList);
        int shrinkTimes = shrinkResult[0];
        vertexNumberSqrt = shrinkResult[1];
        vertices = shrinkResult[2];

        // Update vertex IDs in the grid if any shrink occurred
        if (shrinkTimes > 0) {
            updateVerticesIds(vertexNumberSqrt, allVertices, verticesGrid);
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
        connectVerticesToNeighbors(randomEuclideanGridEdgeWeightedGraph, vertexNumberSqrt);

        // Add extra edges to the graph
        addExtraEdges(randomEuclideanGridEdgeWeightedGraph, extraEdgesList, allVertices);

        return randomEuclideanGridEdgeWeightedGraph;
    }

    private List<Edge> getRandomExtraEdges(int extraEdges, int vertices,
                                           Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex[] allVertices) {
        List<Edge> extraEdgesList = new ArrayList<>();

        while(extraEdges > 0) {
            int randomVertexId1 = StdRandom.uniform(vertices);
            int randomVertexId2 = StdRandom.uniform(vertices);

            // The probability of connecting a vertex s to a vertex t is inversely proportional
            // to the Euclidean distance between s and t
            double distance = MathUtil.distanceBetweenPoints(allVertices[randomVertexId1].xCoordinate,
                    allVertices[randomVertexId1].yCoordinate, allVertices[randomVertexId2].xCoordinate,
                    allVertices[randomVertexId2].yCoordinate);

            boolean shouldConnect = 1 - StdRandom.uniform() >= distance;
            if (shouldConnect) {
                double randomEdgeWeight = StdRandom.uniform();

                extraEdgesList.add(new Edge(randomVertexId1, randomVertexId2, randomEdgeWeight));
                extraEdges--;
            }
        }

        return extraEdgesList;
    }

    private int[] shrinkGraphIfNecessary(int vertexNumberSqrt, int vertices, List<Edge> extraEdgesList) {
        int shrinkTimes = 0;
        boolean shouldCheckIfNeedsToShrink = true;

        int originalVertexNumberSqrt = vertexNumberSqrt;

        // Check if the grid will have to be shrunk
        while (shouldCheckIfNeedsToShrink) {
            // Each cell in the bottom row and right column has one edge
            // Bottom row cells have edges on their right (except the cell n - 1, n - 1)
            int bottomRowEdges = vertexNumberSqrt - 1;
            // Right column cells have edges bellow them (except the cell n - 1, n - 1)
            int rightColumnEdges = vertexNumberSqrt - 1;

            // All other cells have 2 edges (below them and to the right), except cell (n - 1, n - 1)
            int otherCellsEdges = (vertices - bottomRowEdges - rightColumnEdges - 1) * 2;

            int totalNonExtraEdges = bottomRowEdges + rightColumnEdges + otherCellsEdges;
            int totalEdges = totalNonExtraEdges + extraEdgesList.size();

            // We are assuming that the "about 2V" in the exercise description is within 20% of 2V.
            // If R is too large, we shrink the grid to decrease the number of vertices and edges.
            if (totalEdges > 1.2 * (2 * vertices)) {
                vertexNumberSqrt--;
                vertices = vertexNumberSqrt * vertexNumberSqrt;
                shrinkTimes++;

                HashSet<Integer> removedVertices = new HashSet<>();
                int row = 0;

                // Remove right column
                for(int i = 0; i < vertexNumberSqrt; i++) {
                    row++;
                    int removedVertexId = (row * originalVertexNumberSqrt) - shrinkTimes;
                    removedVertices.add(removedVertexId);
                }

                int nextVertexRemoved = row * originalVertexNumberSqrt;

                // Remove last row
                for(int i = 0; i < vertexNumberSqrt + 1; i++) {
                    removedVertices.add(nextVertexRemoved);
                    nextVertexRemoved++;
                }

                List<Edge> extraEdgesToRemoveAfterShrinking = new ArrayList<>();
                for(Edge extraEdge : extraEdgesList) {
                    int vertexId1 = extraEdge.either();
                    int vertexId2 = extraEdge.other(vertexId1);

                    if (removedVertices.contains(vertexId1) || removedVertices.contains(vertexId2)) {
                        extraEdgesToRemoveAfterShrinking.add(extraEdge);
                    }
                }

                extraEdgesList.removeAll(extraEdgesToRemoveAfterShrinking);
            } else {
                shouldCheckIfNeedsToShrink = false;
            }

            // The smallest grid we can have is a 2x2 grid, so if we reach this grid dimensions we break
            if (vertexNumberSqrt == 2) {
                shouldCheckIfNeedsToShrink = false;
            }
        }

        return new int[] {shrinkTimes, vertexNumberSqrt, vertices};
    }

    private void updateVerticesIds(int vertexNumberSqrt,
                                   Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex[] allVertices,
                                   Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex[][] verticesGrid) {
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

    private void connectVerticesToNeighbors(Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph randomEuclideanGridEdgeWeightedGraph,
                                            int vertexNumberSqrt) {
        int[] neighborRows = {-1, 1, 0 ,0};
        int[] neighborColumns = {0, 0, -1 ,1};

        for(int row = 0; row < vertexNumberSqrt; row++) {
            for(int column = 0; column < vertexNumberSqrt; column++) {
                for(int i = 0; i < 4; i++) {
                    int neighborRow = row + neighborRows[i];
                    int neighborColumn = column + neighborColumns[i];

                    if (isValidCell(vertexNumberSqrt, neighborRow, neighborColumn)) {
                        int vertexId1 = getVertexId(row, column, vertexNumberSqrt);
                        int vertexId2 = getVertexId(neighborRow, neighborColumn, vertexNumberSqrt);

                        //Used to avoid connecting vertices more than once
                        if (vertexId1 < vertexId2) {
                            double randomEdgeWeight = StdRandom.uniform();
                            Edge edge = new Edge(vertexId1, vertexId2, randomEdgeWeight);
                            randomEuclideanGridEdgeWeightedGraph.addEdge(edge);
                        }
                    }
                }
            }
        }
    }

    private void addExtraEdges(Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph randomEuclideanGridEdgeWeightedGraph,
                               List<Edge> extraEdgesList,
                               Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph.Vertex[] allVertices) {
        for(Edge extraEdge : extraEdgesList) {
            int vertexId1 = extraEdge.either();
            int vertexId2 = extraEdge.other(vertexId1);

            // We have to access allVertices[] here because it has the updated vertex ids (in the cases where graph
            // shrinking occurred).
            Edge edge = new Edge(allVertices[vertexId1].id, allVertices[vertexId2].id, extraEdge.weight());
            randomEuclideanGridEdgeWeightedGraph.addEdge(edge);
        }
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

    // Parameters example: 9 5 100
    //                    16 40 100
    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        int extraEdges = Integer.parseInt(args[1]);
        int numberOfGraphs = Integer.parseInt(args[2]);

        List<Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph> randomGridEdgeWeightedGraphs =
                new Exercise36_RandomGridEdgeWeightedGraphs().generateRandomGridEdgeWeightedGraphs(numberOfGraphs,
                        vertices, extraEdges);
        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph firstRandomGridEdgeWeightedGraph =
                randomGridEdgeWeightedGraphs.get(0);
        firstRandomGridEdgeWeightedGraph.show(-0.1, 1.1, -0.1, 1.1, 0.03);

        StdOut.println(firstRandomGridEdgeWeightedGraph);
    }

}
