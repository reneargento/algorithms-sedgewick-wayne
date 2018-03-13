package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.MathUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Rene Argento on 08/10/17.
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

    public List<Exercise37_EuclideanGraphs.EuclideanGraph> generateRandomGridGraphs(int numberOfGraphs,
                                                                                    int vertices, int extraEdges) {
        if (numberOfGraphs < 0) {
            throw new IllegalArgumentException("Number of graphs cannot be negative");
        }

        List<Exercise37_EuclideanGraphs.EuclideanGraph> randomGridGraphs = new ArrayList<>();

        for(int graph = 0; graph < numberOfGraphs; graph++) {
            Exercise37_EuclideanGraphs.EuclideanGraph randomGridGraph = randomGridGraph(vertices, extraEdges);
            randomGridGraphs.add(randomGridGraph);
        }

        return randomGridGraphs;
    }

    public Exercise37_EuclideanGraphs.EuclideanGraph randomGridGraph(int vertices, int extraEdges) {

        if (Math.sqrt(vertices) != (int) Math.sqrt(vertices)) {
            throw new IllegalArgumentException("Vertex number must have an integer square root");
        }

        // Instance used only to create the vertices
        Exercise37_EuclideanGraphs.EuclideanGraph euclideanGraph =
                new Exercise37_EuclideanGraphs().new EuclideanGraph(0);

        int vertexNumberSqrt = (int) Math.sqrt(vertices);

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
        Exercise37_EuclideanGraphs.EuclideanGraph randomEuclideanGridGraph =
                new Exercise37_EuclideanGraphs().new EuclideanGraph(vertices);

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            int[] cellRowAndColumn = getCellRowAndColumn(vertexId, vertexNumberSqrt);
            Exercise37_EuclideanGraphs.EuclideanGraph.Vertex vertex = verticesGrid[cellRowAndColumn[0]][cellRowAndColumn[1]];

            randomEuclideanGridGraph.addVertex(vertex);
        }

        // Connect vertices to their neighbors
        connectVerticesToNeighbors(randomEuclideanGridGraph, vertexNumberSqrt);

        // Add extra edges to the graph
        addExtraEdges(randomEuclideanGridGraph, extraEdgesList, allVertices);

        return randomEuclideanGridGraph;
    }

    private List<Edge> getRandomExtraEdges(int extraEdges, int vertices,
                                           Exercise37_EuclideanGraphs.EuclideanGraph.Vertex[] allVertices) {
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
                extraEdgesList.add(new Edge(randomVertexId1, randomVertexId2));
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
                    if (removedVertices.contains(extraEdge.vertex1) || removedVertices.contains(extraEdge.vertex2)) {
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
                                   Exercise37_EuclideanGraphs.EuclideanGraph.Vertex[] allVertices,
                                   Exercise37_EuclideanGraphs.EuclideanGraph.Vertex[][] verticesGrid) {
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

    private void connectVerticesToNeighbors(Exercise37_EuclideanGraphs.EuclideanGraph randomEuclideanGridGraph,
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
                            randomEuclideanGridGraph.addEdge(vertexId1, vertexId2);
                        }
                    }
                }
            }
        }
    }

    private void addExtraEdges(Exercise37_EuclideanGraphs.EuclideanGraph randomEuclideanGridGraph,
                               List<Edge> extraEdgesList,
                               Exercise37_EuclideanGraphs.EuclideanGraph.Vertex[] allVertices) {
        for(Edge extraEdge : extraEdgesList) {
            // We have to access allVertices[] here because it has the updated vertex ids (in the cases where graph
            // shrinking occurred).
            randomEuclideanGridGraph.addEdge(allVertices[extraEdge.vertex1].id, allVertices[extraEdge.vertex2].id);
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

    //Parameters example: 9 5 100
    //                    16 40 100
    public static void main(String[] args) {
        int vertices = Integer.parseInt(args[0]);
        int extraEdges = Integer.parseInt(args[1]);
        int numberOfGraphs = Integer.parseInt(args[2]);

        List<Exercise37_EuclideanGraphs.EuclideanGraph> randomGridGraphs =
                new Exercise44_RandomGridGraphs().generateRandomGridGraphs(numberOfGraphs, vertices, extraEdges);
        Exercise37_EuclideanGraphs.EuclideanGraph firstRandomGridGraph = randomGridGraphs.get(0);
        firstRandomGridGraph.show(-0.1, 1.1, -0.1, 1.1, 0.03);

        StdOut.println(firstRandomGridGraph);
    }

}
