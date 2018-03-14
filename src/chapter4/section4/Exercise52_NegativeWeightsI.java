package chapter4.section4;

import chapter1.section5.UnionFind;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 28/12/17.
 */
public class Exercise52_NegativeWeightsI {

    // 1- Random sparse edge-weighted digraphs
    public class RandomEdgeWeightedDigraphs {

        public EdgeWeightedDigraphInterface erdosRenyiDigraphUniformWeights(int vertices, int edges, double minWeightInScale,
                                                                            double maxWeightInScale) {
            List<Double> originalEdgeWeights = new ArrayList<>();

            for(int edge = 0; edge < edges; edge++) {
                double uniformRandomWeight = StdRandom.uniform();
                originalEdgeWeights.add(uniformRandomWeight);
            }

            List<Double> rescaledEdgeWeights = rescaleWeights(originalEdgeWeights, minWeightInScale,
                    maxWeightInScale);
            return generateRandomEdgeWeightedDigraph(vertices, edges, rescaledEdgeWeights);
        }

        public EdgeWeightedDigraphInterface erdosRenyiDigraphGaussianWeights(int vertices, int edges, double minWeightInScale,
                                                                             double maxWeightInScale) {
            List<Double> originalEdgeWeights = new ArrayList<>();

            for(int edge = 0; edge < edges; edge++) {
                double gaussianRandomWeight = StdRandom.gaussian();
                originalEdgeWeights.add(gaussianRandomWeight);
            }

            List<Double> rescaledEdgeWeights = rescaleWeights(originalEdgeWeights, minWeightInScale,
                    maxWeightInScale);
            return generateRandomEdgeWeightedDigraph(vertices, edges, rescaledEdgeWeights);
        }

        private EdgeWeightedDigraphInterface generateRandomEdgeWeightedDigraph(int vertices, int edges,
                                                                               List<Double> rescaledEdgeWeights) {
            EdgeWeightedDigraphInterface randomEdgeWeightedDigraph = new EdgeWeightedDigraph(vertices);

            for(int edge = 0; edge < edges; edge++) {
                int vertexId1 = StdRandom.uniform(vertices);
                int vertexId2 = StdRandom.uniform(vertices);

                double rescaledEdgeWeight = rescaledEdgeWeights.get(edge);

                DirectedEdge newEdge;

                int randomDirection = StdRandom.uniform(2);
                if (randomDirection == 0) {
                    newEdge = new DirectedEdge(vertexId1, vertexId2, rescaledEdgeWeight);
                } else {
                    newEdge = new DirectedEdge(vertexId2, vertexId1, rescaledEdgeWeight);
                }

                randomEdgeWeightedDigraph.addEdge(newEdge);
            }

            return randomEdgeWeightedDigraph;
        }
    }

    public class RandomSparseEdgeWeightedDigraphsGenerator {
        //A graph is considered sparse if its number of edges is within a small constant factor of V
        public List<EdgeWeightedDigraphInterface> randomSparseEdgeWeightedDigraphsGenerator(int numberOfDigraphs,
                                                                                            boolean uniformWeightDistribution,
                                                                                            double minWeightInScale,
                                                                                            double maxWeightInScale) {
            if (numberOfDigraphs < 0) {
                throw new IllegalArgumentException("Number of digraphs cannot be negative");
            }

            RandomEdgeWeightedDigraphs randomEdgeWeightedDigraphsGenerator = new RandomEdgeWeightedDigraphs();

            List<EdgeWeightedDigraphInterface> randomEdgeWeightedDigraphs = new ArrayList<>();
            int[] digraphVerticesCount = {10, 100, 1000, 10000};

            for(int digraph = 0; digraph < numberOfDigraphs; digraph++) {
                int vertices = digraphVerticesCount[digraph % 4];
                int edges = vertices * 3;

                EdgeWeightedDigraphInterface randomEdgeWeightedDigraph;

                if (uniformWeightDistribution) {
                    randomEdgeWeightedDigraph =
                            randomEdgeWeightedDigraphsGenerator.erdosRenyiDigraphUniformWeights(vertices, edges,
                                    minWeightInScale, maxWeightInScale);
                } else {
                    randomEdgeWeightedDigraph =
                            randomEdgeWeightedDigraphsGenerator.erdosRenyiDigraphGaussianWeights(vertices, edges,
                                    minWeightInScale, maxWeightInScale);
                }

                randomEdgeWeightedDigraphs.add(randomEdgeWeightedDigraph);
            }

            return randomEdgeWeightedDigraphs;
        }
    }

    // 2- Random Euclidean edge-weighted digraphs
    public class RandomEuclideanEdgeWeightedDigraphsGenerator {

        public List<EdgeWeightedDigraphInterface> generateRandomEuclideanEdgeWeightedDigraphs(int numberOfDigraphs,
                                                                                              int vertices,
                                                                                              double radius,
                                                                                              double minWeightInScale,
                                                                                              double maxWeightInScale) {
            if (numberOfDigraphs < 0) {
                throw new IllegalArgumentException("Number of digraphs cannot be negative");
            }

            List<EdgeWeightedDigraphInterface> randomEuclideanEdgeWeightedDigraphs = new ArrayList<>();

            for(int digraph = 0; digraph < numberOfDigraphs; digraph++) {
                EdgeWeightedDigraphInterface randomEuclideanEdgeWeightedDigraph =
                        randomEuclideanEdgeWeightedDigraph(vertices, radius, minWeightInScale, maxWeightInScale);
                randomEuclideanEdgeWeightedDigraphs.add(randomEuclideanEdgeWeightedDigraph);
            }

            return randomEuclideanEdgeWeightedDigraphs;
        }

        public EdgeWeightedDigraphInterface randomEuclideanEdgeWeightedDigraph(int vertices, double radius,
                                                                               double minWeightInScale,
                                                                               double maxWeightInScale) {
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

            List<Double> originalWeights = new ArrayList<>();

            // First pass to get original edge weights
            for(int vertexId1 = 0; vertexId1 < vertices; vertexId1++) {
                for(int vertexId2 = vertexId1 + 1; vertexId2 < vertices; vertexId2++) {

                    double distance = MathUtil.distanceBetweenPoints(allVertices[vertexId1].coordinates.getXCoordinate(),
                            allVertices[vertexId1].coordinates.getYCoordinate(),
                            allVertices[vertexId2].coordinates.getXCoordinate(),
                            allVertices[vertexId2].coordinates.getYCoordinate());

                    if (distance <= radius) {
                        originalWeights.add(distance);
                    }
                }
            }

            List<Double> rescaledEdgeWeights = rescaleWeights(originalWeights, minWeightInScale,
                    maxWeightInScale);
            int rescaledWeightsIndex = 0;

            // Second pass to add rescaled edges
            for(int vertexId1 = 0; vertexId1 < vertices; vertexId1++) {
                for(int vertexId2 = vertexId1 + 1; vertexId2 < vertices; vertexId2++) {

                    double distance = MathUtil.distanceBetweenPoints(allVertices[vertexId1].coordinates.getXCoordinate(),
                            allVertices[vertexId1].coordinates.getYCoordinate(),
                            allVertices[vertexId2].coordinates.getXCoordinate(),
                            allVertices[vertexId2].coordinates.getYCoordinate());

                    if (distance <= radius) {
                        DirectedEdge edge;

                        double rescaledEdgeWeight = rescaledEdgeWeights.get(rescaledWeightsIndex++);

                        int randomDirection = StdRandom.uniform(2);
                        if (randomDirection == 0) {
                            edge = new DirectedEdge(vertexId1, vertexId2, rescaledEdgeWeight);
                        } else {
                            edge = new DirectedEdge(vertexId2, vertexId1, rescaledEdgeWeight);
                        }

                        randomEuclideanEdgeWeightedDigraph.addEdge(edge);
                    }
                }
            }

            return randomEuclideanEdgeWeightedDigraph;
        }
    }

    // 3- Random grid edge-weighted digraphs
    public class RandomGridEdgeWeightedDigraphsGenerator {

        public List<EdgeWeightedDigraphInterface> generateRandomGridEdgeWeightedDigraphs(int numberOfDigraphs,
                                                                                         int vertices, int extraEdges,
                                                                                         double minWeightInScale,
                                                                                         double maxWeightInScale) {
            if (numberOfDigraphs < 0) {
                throw new IllegalArgumentException("Number of digraphs cannot be negative");
            }

            List<EdgeWeightedDigraphInterface> randomGridEdgeWeightedDigraphs = new ArrayList<>();

            for(int digraph = 0; digraph < numberOfDigraphs; digraph++) {
                EdgeWeightedDigraphInterface randomGridEdgeWeightedDigraph =
                        randomGridEdgeWeightedDigraph(vertices, extraEdges, minWeightInScale, maxWeightInScale);
                randomGridEdgeWeightedDigraphs.add(randomGridEdgeWeightedDigraph);
            }

            return randomGridEdgeWeightedDigraphs;
        }

        public EdgeWeightedDigraphInterface randomGridEdgeWeightedDigraph(int vertices, int extraEdges,
                                                                          double minWeightInScale, double maxWeightInScale) {
            if (Math.sqrt(vertices) != (int) Math.sqrt(vertices)) {
                throw new IllegalArgumentException("Vertex number must have an integer square root");
            }

            // Instance used only to create the vertices and edges
            Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph euclideanEdgeWeightedDigraph =
                    new Exercise27_ShortestPathsInEuclideanGraphs().new EuclideanEdgeWeightedDigraph(0);

            // Instance used only to call grid graph functions
            Exercise51_RandomGridEdgeWeightedDigraphs randomGridEdgeWeightedDigraphs =
                    new Exercise51_RandomGridEdgeWeightedDigraphs();

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

                int[] cellRowAndColumn = randomGridEdgeWeightedDigraphs.getCellRowAndColumn(vertexId, vertexNumberSqrt);
                verticesGrid[cellRowAndColumn[0]][cellRowAndColumn[1]] = vertex;
            }

            // Create the random extra edges
            List<DirectedEdge> extraEdgesList = randomGridEdgeWeightedDigraphs.getRandomExtraEdges(extraEdges, vertices,
                    allVertices);

            int[] shrinkResult = randomGridEdgeWeightedDigraphs.shrinkDigraphIfNecessary(vertexNumberSqrt, vertices,
                    extraEdgesList);
            int shrinkTimes = shrinkResult[0];
            vertexNumberSqrt = shrinkResult[1];
            vertices = shrinkResult[2];

            // Update vertex IDs in the grid if any shrink occurred
            if (shrinkTimes > 0) {
                randomGridEdgeWeightedDigraphs.updateVerticesIds(vertexNumberSqrt, allVertices, verticesGrid);
            }

            // Create the digraph
            Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph randomEuclideanGridEdgeWeightedDigraph =
                    new Exercise27_ShortestPathsInEuclideanGraphs().new EuclideanEdgeWeightedDigraph(vertices);

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                int[] cellRowAndColumn = randomGridEdgeWeightedDigraphs.getCellRowAndColumn(vertexId, vertexNumberSqrt);
                Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex vertex =
                        verticesGrid[cellRowAndColumn[0]][cellRowAndColumn[1]];

                randomEuclideanGridEdgeWeightedDigraph.addVertex(vertex);
            }

            // Connect vertices to their neighbors
            int[] neighborRows = {-1, 1, 0 ,0};
            int[] neighborColumns = {0, 0, -1 ,1};

            // First pass to get the original edge weights
            List<Double> originalEdgeWeights = getOriginalEdgeWeights(vertexNumberSqrt, neighborRows, neighborColumns,
                    randomGridEdgeWeightedDigraphs);

            // Also get the extra edges' original weights
            for(DirectedEdge extraEdge : extraEdgesList) {
                originalEdgeWeights.add(extraEdge.weight());
            }

            List<Double> rescaledEdgeWeights = rescaleWeights(originalEdgeWeights, minWeightInScale, maxWeightInScale);

            addRescaledEdgesToDigraph(vertexNumberSqrt, neighborRows, neighborColumns, randomEuclideanGridEdgeWeightedDigraph,
                    rescaledEdgeWeights, randomGridEdgeWeightedDigraphs);

            int indexOfExtraEdges = originalEdgeWeights.size() - extraEdgesList.size();

            // Add extra edges with rescaled edge weights to the digraph
            addExtraEdges(randomEuclideanGridEdgeWeightedDigraph, extraEdgesList, allVertices, rescaledEdgeWeights,
                    indexOfExtraEdges);

            return randomEuclideanGridEdgeWeightedDigraph;
        }

        private List<Double> getOriginalEdgeWeights(int vertexNumberSqrt, int[] neighborRows, int[] neighborColumns,
                                                    Exercise51_RandomGridEdgeWeightedDigraphs randomGridEdgeWeightedDigraphs) {
            List<Double> originalWeights = new ArrayList<>();

            for(int row = 0; row < vertexNumberSqrt; row++) {
                for(int column = 0; column < vertexNumberSqrt; column++) {
                    for(int i = 0; i < 4; i++) {
                        int neighborRow = row + neighborRows[i];
                        int neighborColumn = column + neighborColumns[i];

                        if (randomGridEdgeWeightedDigraphs.isValidCell(vertexNumberSqrt, neighborRow, neighborColumn)) {
                            int vertexId1 = randomGridEdgeWeightedDigraphs.getVertexId(row, column, vertexNumberSqrt);
                            int vertexId2 = randomGridEdgeWeightedDigraphs.getVertexId(neighborRow, neighborColumn,
                                    vertexNumberSqrt);

                            // Used to avoid connecting vertices more than once
                            if (vertexId1 < vertexId2) {
                                DirectedEdge edge = randomGridEdgeWeightedDigraphs.
                                        getRandomWeightedDirectedEdge(vertexId1, vertexId2);
                                originalWeights.add(edge.weight());
                            }
                        }
                    }
                }
            }

            return originalWeights;
        }

        private void addRescaledEdgesToDigraph(int vertexNumberSqrt, int[] neighborRows, int[] neighborColumns,
                                               Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph randomEuclideanGridEdgeWeightedDigraph,
                                               List<Double> rescaledEdgeWeights,
                                               Exercise51_RandomGridEdgeWeightedDigraphs randomGridEdgeWeightedDigraphs) {
            int rescaledEdgeWeightsIndex = 0;

            for(int row = 0; row < vertexNumberSqrt; row++) {
                for(int column = 0; column < vertexNumberSqrt; column++) {
                    for(int i = 0; i < 4; i++) {
                        int neighborRow = row + neighborRows[i];
                        int neighborColumn = column + neighborColumns[i];

                        if (randomGridEdgeWeightedDigraphs.isValidCell(vertexNumberSqrt, neighborRow, neighborColumn)) {
                            int vertexId1 = randomGridEdgeWeightedDigraphs.getVertexId(row, column, vertexNumberSqrt);
                            int vertexId2 = randomGridEdgeWeightedDigraphs.getVertexId(neighborRow, neighborColumn,
                                    vertexNumberSqrt);

                            // Used to avoid connecting vertices more than once
                            if (vertexId1 < vertexId2) {
                                double rescaledEdgeWeight = rescaledEdgeWeights.get(rescaledEdgeWeightsIndex++);

                                DirectedEdge edge = new DirectedEdge(vertexId1, vertexId2, rescaledEdgeWeight);
                                randomEuclideanGridEdgeWeightedDigraph.addEdge(edge);
                            }
                        }
                    }
                }
            }
        }

        private void addExtraEdges(EdgeWeightedDigraphInterface randomEuclideanGridEdgeWeightedDigraph,
                                   List<DirectedEdge> extraEdgesList,
                                   Exercise27_ShortestPathsInEuclideanGraphs.EuclideanEdgeWeightedDigraph.Vertex[] allVertices,
                                   List<Double> rescaledEdgeWeights, int indexOfExtraEdges) {
            for(DirectedEdge extraEdge : extraEdgesList) {
                int vertexId1 = extraEdge.from();
                int vertexId2 = extraEdge.to();

                // We have to access allVertices[] here because it has the updated vertex ids (in the cases where graph
                // shrinking occurred).
                double rescaledEdgeWeight = rescaledEdgeWeights.get(indexOfExtraEdges++);

                DirectedEdge edge = new DirectedEdge(allVertices[vertexId1].id, allVertices[vertexId2].id,
                        rescaledEdgeWeight);
                randomEuclideanGridEdgeWeightedDigraph.addEdge(edge);
            }
        }
    }

    // Based on https://stackoverflow.com/questions/5294955/how-to-scale-down-a-range-of-numbers-with-a-known-min-and-max-value
    private List<Double> rescaleWeights(List<Double> weights, double minWeightInScale, double maxWeightInScale) {
        if (weights == null || weights.isEmpty()) {
            return new ArrayList<>();
        }

        double minWeightOriginal = weights.get(0);
        double maxWeightOriginal = weights.get(0);

        for(double weight : weights) {
            if (weight < minWeightOriginal) {
                minWeightOriginal = weight;
            }
            if (weight > maxWeightOriginal) {
                maxWeightOriginal = weight;
            }
        }

        List<Double> rescaledWeights = new ArrayList<>();

        for(double weight : weights) {
            double rescaledWeight = ((maxWeightInScale - minWeightInScale) * (weight - minWeightOriginal)
                    / (maxWeightOriginal - minWeightOriginal)) + minWeightInScale;
            rescaledWeights.add(rescaledWeight);
        }

        return rescaledWeights;
    }

    // Parameters example: -1 1 6 0.5 9 5 100
    //                     -1 1 6 0.5 16 40 100
    public static void main(String[] args) {
        Exercise52_NegativeWeightsI negativeWeightsI = new Exercise52_NegativeWeightsI();

        List<Double> weightsToRescale = new ArrayList<>();
        weightsToRescale.add(0.9);
        weightsToRescale.add(0.99);
        weightsToRescale.add(0.35);
        weightsToRescale.add(-0.2);
        weightsToRescale.add(-0.3);

        double minWeightInScaleTest = -1;
        double maxWeightInScaleTest = 1;

        StdOut.print("Rescaled weights: ");

        List<Double> rescaledWeights = negativeWeightsI.rescaleWeights(weightsToRescale, minWeightInScaleTest,
                maxWeightInScaleTest);
        for(double rescaledWeight : rescaledWeights) {
            StdOut.printf("%.2f ", rescaledWeight);
        }
        StdOut.println("\nExpected: 0.86 1.00 0.01 -0.84 -1.00 ");

        double minWeightInScale = Double.parseDouble(args[0]);
        double maxWeightInScale = Double.parseDouble(args[1]);

        int verticesInEuclideanDigraph = Integer.parseInt(args[2]);
        double radius = Double.parseDouble(args[3]);

        int verticesInGridDigraph = Integer.parseInt(args[4]);
        int extraEdgesInGridDigraph = Integer.parseInt(args[5]);

        int numberOfDigraphs = Integer.parseInt(args[6]);

        if (minWeightInScale < -1 || minWeightInScale > 1) {
            throw new IllegalArgumentException("Min weight in scale must be between -1 and 1");
        }

        if (maxWeightInScale < -1 || maxWeightInScale > 1) {
            throw new IllegalArgumentException("Max weight in scale must be between -1 and 1");
        }

        if (minWeightInScale > maxWeightInScale) {
            throw new IllegalArgumentException("Min weight in scale cannot be higher than max weight in scale");
        }

        // 1- Random sparse edge-weighted digraphs
        RandomSparseEdgeWeightedDigraphsGenerator randomSparseEdgeWeightedDigraphsGenerator =
                negativeWeightsI.new RandomSparseEdgeWeightedDigraphsGenerator();

        List<EdgeWeightedDigraphInterface> randomSparseEdgeWeightedDigraphsUniform =
                randomSparseEdgeWeightedDigraphsGenerator.randomSparseEdgeWeightedDigraphsGenerator(numberOfDigraphs,
                        true, minWeightInScale, maxWeightInScale);
        EdgeWeightedDigraphInterface firstRandomSparseEdgeWeightedDigraphUniform =
                randomSparseEdgeWeightedDigraphsUniform.get(0);

        StdOut.println("\nRandom sparse-edge-weighted-digraph with uniform weights distribution");
        StdOut.println(firstRandomSparseEdgeWeightedDigraphUniform);

        List<EdgeWeightedDigraphInterface> randomSparseEdgeWeightedDigraphsGaussian =
                randomSparseEdgeWeightedDigraphsGenerator.randomSparseEdgeWeightedDigraphsGenerator(numberOfDigraphs,
                        false, minWeightInScale, maxWeightInScale);
        EdgeWeightedDigraphInterface firstRandomSparseEdgeWeightedDigraphGaussian =
                randomSparseEdgeWeightedDigraphsGaussian.get(0);

        StdOut.println("Random sparse-edge-weighted-digraph with gaussian weights distribution:");
        StdOut.println(firstRandomSparseEdgeWeightedDigraphGaussian);

        // 2- Random Euclidean edge-weighted digraphs
        StdOut.println("Random Euclidean edge-weighted digraph:");

        List<EdgeWeightedDigraphInterface> randomEuclideanEdgeWeightedDigraphs =
                negativeWeightsI.new RandomEuclideanEdgeWeightedDigraphsGenerator().
                        generateRandomEuclideanEdgeWeightedDigraphs(numberOfDigraphs, verticesInEuclideanDigraph, radius,
                                minWeightInScale, maxWeightInScale);

        EdgeWeightedDigraphInterface firstEuclideanEdgeWeightedDigraph = randomEuclideanEdgeWeightedDigraphs.get(0);
        StdOut.println(firstEuclideanEdgeWeightedDigraph);

        UnionFind unionFind = new UnionFind(verticesInEuclideanDigraph);

        for(int vertex = 0; vertex < verticesInEuclideanDigraph; vertex++) {
            for(DirectedEdge edge : firstEuclideanEdgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();
                unionFind.union(vertex, neighbor);
            }
        }

        // Check theory that if "radius" is larger than threshold value SQRT(ln(V) / (Math.PI * V)) then the graph is
        // almost certainly connected. Otherwise, it is almost certainly disconnected.
        double thresholdValue = Math.sqrt(Math.log(verticesInEuclideanDigraph) / (Math.PI * verticesInEuclideanDigraph));

        StdOut.println("Euclidean digraph expected to be connected (if it were an undirected graph): "
                + (radius > thresholdValue));
        StdOut.println("Euclidean digraph would be connected (if it were an undirected graph): " + (unionFind.count() == 1));

        // 3- Random grid edge-weighted digraphs
        StdOut.println("\nRandom grid edge-weighted digraph:");

        List<EdgeWeightedDigraphInterface> randomGridEdgeWeightedDigraphs =
                negativeWeightsI.new RandomGridEdgeWeightedDigraphsGenerator().
                        generateRandomGridEdgeWeightedDigraphs(numberOfDigraphs, verticesInGridDigraph,
                                extraEdgesInGridDigraph, minWeightInScale, maxWeightInScale);

        EdgeWeightedDigraphInterface firstRandomGridEdgeWeightedDigraph = randomGridEdgeWeightedDigraphs.get(0);
        StdOut.println(firstRandomGridEdgeWeightedDigraph);
    }

}
