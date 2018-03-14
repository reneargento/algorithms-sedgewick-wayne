package chapter4.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.MathUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Rene Argento on 28/12/17.
 */
public class Exercise51_RandomGridEdgeWeightedDigraphs {

    public List<EdgeWeightedDigraphInterface> generateRandomGridEdgeWeightedDigraphs(int numberOfDigraphs,
                                                                                     int vertices, int extraEdges) {

        if (numberOfDigraphs < 0) {
            throw new IllegalArgumentException("Number of digraphs cannot be negative");
        }

        List<EdgeWeightedDigraphInterface> randomGridEdgeWeightedDigraphs = new ArrayList<>();

        for(int digraph = 0; digraph < numberOfDigraphs; digraph++) {
            EdgeWeightedDigraphInterface randomGridEdgeWeightedDigraph = randomGridEdgeWeightedDigraph(vertices,
                    extraEdges);
            randomGridEdgeWeightedDigraphs.add(randomGridEdgeWeightedDigraph);
        }

        return randomGridEdgeWeightedDigraphs;
    }

    public EdgeWeightedDigraphInterface randomGridEdgeWeightedDigraph(int vertices, int extraEdges) {

        if (Math.sqrt(vertices) != (int) Math.sqrt(vertices)) {
            throw new IllegalArgumentException("Vertex number must have an integer square root");
        }

        // Instance used only to create the vertices and edges
        Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph euclideanEdgeWeightedDigraph =
                new Exercise27_ShortestPathsInEuclideanGraphs().new EuclideanEdgeWeightedDigraph(0);

        int vertexNumberSqrt = (int) Math.sqrt(vertices);

        // Create the grid
        Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex[][] verticesGrid =
                new Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.
                        Vertex[vertexNumberSqrt][vertexNumberSqrt];

        Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex[] allVertices =
                new Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex[vertices];

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            double randomXCoordinate = StdRandom.uniform();
            double randomYCoordinate = StdRandom.uniform();

            Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex vertex =
                    euclideanEdgeWeightedDigraph.new Vertex(vertexId, randomXCoordinate, randomYCoordinate);
            allVertices[vertexId] = vertex;

            int[] cellRowAndColumn = getCellRowAndColumn(vertexId, vertexNumberSqrt);
            verticesGrid[cellRowAndColumn[0]][cellRowAndColumn[1]] = vertex;
        }

        // Generate extra random edges
        List<DirectedEdge> extraEdgesList = getRandomExtraEdges(extraEdges, vertices, allVertices);

        int[] shrinkResult = shrinkDigraphIfNecessary(vertexNumberSqrt, vertices, extraEdgesList);
        int shrinkTimes = shrinkResult[0];
        vertexNumberSqrt = shrinkResult[1];
        vertices = shrinkResult[2];

        // Update vertex IDs in the grid if any shrink occurred
        if (shrinkTimes > 0) {
            updateVerticesIds(vertexNumberSqrt, allVertices, verticesGrid);
        }

        // Create the digraph
        Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph randomEuclideanGridEdgeWeightedDigraph =
                new Exercise27_ShortestPathsInEuclideanGraphs().new EuclideanEdgeWeightedDigraph(vertices);

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            int[] cellRowAndColumn = getCellRowAndColumn(vertexId, vertexNumberSqrt);
            Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex vertex =
                    verticesGrid[cellRowAndColumn[0]][cellRowAndColumn[1]];

            randomEuclideanGridEdgeWeightedDigraph.addVertex(vertex);
        }

        // Connect vertices to their neighbors
        connectVerticesToNeighbors(randomEuclideanGridEdgeWeightedDigraph, vertexNumberSqrt);

        // Add extra edges to the digraph
        addExtraEdges(randomEuclideanGridEdgeWeightedDigraph, extraEdgesList, allVertices);

        return randomEuclideanGridEdgeWeightedDigraph;
    }

    public DirectedEdge getRandomWeightedDirectedEdge(int randomVertexId1, int randomVertexId2) {
        double randomEdgeWeight = StdRandom.uniform();

        DirectedEdge edge;

        int randomDirection = StdRandom.uniform(2);
        if (randomDirection == 0) {
            edge = new DirectedEdge(randomVertexId1, randomVertexId2, randomEdgeWeight);
        } else {
            edge = new DirectedEdge(randomVertexId2, randomVertexId1, randomEdgeWeight);
        }

        return edge;
    }

    public List<DirectedEdge> getRandomExtraEdges(int extraEdges, int vertices,
                                                  Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex[] allVertices) {
        List<DirectedEdge> extraEdgesList = new ArrayList<>();

        while(extraEdges > 0) {
            int randomVertexId1 = StdRandom.uniform(vertices);
            int randomVertexId2 = StdRandom.uniform(vertices);

            // The probability of connecting a vertex s to a vertex t is inversely proportional
            // to the Euclidean distance between s and t
            double distance = MathUtil.distanceBetweenPoints(allVertices[randomVertexId1].coordinates.getXCoordinate(),
                    allVertices[randomVertexId1].coordinates.getYCoordinate(),
                    allVertices[randomVertexId2].coordinates.getXCoordinate(),
                    allVertices[randomVertexId2].coordinates.getYCoordinate());

            boolean shouldConnect = 1 - StdRandom.uniform() >= distance;
            if (shouldConnect) {
                DirectedEdge edge = getRandomWeightedDirectedEdge(randomVertexId1, randomVertexId2);
                extraEdgesList.add(edge);
                extraEdges--;
            }
        }

        return extraEdgesList;
    }

    public int[] shrinkDigraphIfNecessary(int vertexNumberSqrt, int vertices, List<DirectedEdge> extraEdgesList) {
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

                List<DirectedEdge> extraEdgesToRemoveAfterShrinking = new ArrayList<>();
                for(DirectedEdge extraEdge : extraEdgesList) {
                    int vertexId1 = extraEdge.from();
                    int vertexId2 = extraEdge.to();

                    if (removedVertices.contains(vertexId1) || removedVertices.contains(vertexId2)) {
                        extraEdgesToRemoveAfterShrinking.add(extraEdge);
                    }
                }

                extraEdgesList.removeAll(extraEdgesToRemoveAfterShrinking);
            } else {
                shouldCheckIfNeedsToShrink = false;
            }

            // The smallest grid we can have is a 2x2 grid, so if we reach this grid dimension no more shrinks can occur
            if (vertexNumberSqrt == 2) {
                shouldCheckIfNeedsToShrink = false;
            }
        }

        return new int[] {shrinkTimes, vertexNumberSqrt, vertices};
    }

    public void updateVerticesIds(int vertexNumberSqrt,
                                  Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex[] allVertices,
                                  Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex[][] verticesGrid) {
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

    private void connectVerticesToNeighbors(EdgeWeightedDigraphInterface randomEuclideanGridEdgeWeightedDigraph,
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

                        // Used to avoid connecting vertices more than once
                        if (vertexId1 < vertexId2) {
                            DirectedEdge edge = getRandomWeightedDirectedEdge(vertexId1, vertexId2);
                            randomEuclideanGridEdgeWeightedDigraph.addEdge(edge);
                        }
                    }
                }
            }
        }
    }

    private void addExtraEdges(EdgeWeightedDigraphInterface randomEuclideanGridEdgeWeightedDigraph,
                               List<DirectedEdge> extraEdgesList,
                               Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex[] allVertices) {
        for(DirectedEdge extraEdge : extraEdgesList) {
            int vertexId1 = extraEdge.from();
            int vertexId2 = extraEdge.to();

            // We have to access allVertices[] here because it has the updated vertex ids (in the cases where graph
            // shrinking occurred).
            DirectedEdge edge = new DirectedEdge(allVertices[vertexId1].id, allVertices[vertexId2].id, extraEdge.weight());
            randomEuclideanGridEdgeWeightedDigraph.addEdge(edge);
        }
    }

    public boolean isValidCell(int dimensionSize, int row, int column) {
        return row >= 0 && row < dimensionSize && column >= 0 && column < dimensionSize;
    }

    public int getVertexId(int row, int column, int columns) {
        return row * columns + column;
    }

    public int[] getCellRowAndColumn(int vertexId, int columns) {
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
        int numberOfDigraphs = Integer.parseInt(args[2]);

        List<EdgeWeightedDigraphInterface> randomGridEdgeWeightedDigraphs =
                new Exercise51_RandomGridEdgeWeightedDigraphs().generateRandomGridEdgeWeightedDigraphs(numberOfDigraphs,
                        vertices, extraEdges);
        EdgeWeightedDigraphInterface firstRandomGridEdgeWeightedDigraph = randomGridEdgeWeightedDigraphs.get(0);
        // Not used in EdgeWeightedDigraphInterface
//        firstRandomGridEdgeWeightedDigraph.show(-0.1, 1.1, -0.1, 1.1, 0.03, 0.01, 0.02);

        StdOut.println("Random grid edge-weighted digraph:\n");
        StdOut.println(firstRandomGridEdgeWeightedDigraph);
    }

}
