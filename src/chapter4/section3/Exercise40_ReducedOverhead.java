package chapter4.section3;

import chapter1.section3.Bag;
import chapter1.section3.Queue;
import chapter1.section5.UnionFind;
import chapter2.section4.IndexMinPriorityQueue;
import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.MathUtil;

import java.awt.*;

import static chapter4.section3.Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficient.NO_CONNECTION;

/**
 * Created by Rene Argento on 13/11/17.
 */
public class Exercise40_ReducedOverhead {

    // Instance used to optimize object creation
    private Exercise28_SpaceEfficientDataStructures spaceEfficientDataStructures = new Exercise28_SpaceEfficientDataStructures();

    // Space efficient version of eager Prim's algorithm
    // Trade-off: it has runtime complexity of O(V^2)
    public class PrimMSTSpaceEfficient {
        private Edge[] edgeTo; // shortest edge from tree vertex
        private double[] distTo; // distTo[vertex] = edgeTo[vertex].weight()
        private boolean[] marked; // true if vertex is on the minimum spanning tree
        private IndexMinPriorityQueue<Double> priorityQueue; // eligible crossing edges

        private double weight;

        public PrimMSTSpaceEfficient(Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficientInterface edgeWeightedGraphSpaceEfficient) {
            edgeTo = new Edge[edgeWeightedGraphSpaceEfficient.vertices()];
            distTo = new double[edgeWeightedGraphSpaceEfficient.vertices()];
            marked = new boolean[edgeWeightedGraphSpaceEfficient.vertices()];

            for(int vertex = 0; vertex < edgeWeightedGraphSpaceEfficient.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }
            priorityQueue = new IndexMinPriorityQueue<>(edgeWeightedGraphSpaceEfficient.vertices());

            // Initialize priority queue with 0, weight 0
            distTo[0] = 0;
            priorityQueue.insert(0, 0.0);

            while (!priorityQueue.isEmpty()) {
                visit(edgeWeightedGraphSpaceEfficient, priorityQueue.deleteMin()); // Add closest vertex to the minimum spanning tree
            }
        }

        private void visit(Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficientInterface edgeWeightedGraphSpaceEfficient,
                           int vertex) {
            // Add vertex to the minimum spanning tree; update data structures
            marked[vertex] = true;

            double[] adjacentEdges = edgeWeightedGraphSpaceEfficient.adjacent(vertex);

            for(int otherVertex = 0; otherVertex < adjacentEdges.length; otherVertex++) {
                if (marked[otherVertex] || adjacentEdges[otherVertex] == NO_CONNECTION) {
                    continue; // vertex-otherVertex is ineligible or non-existent
                }

                double edgeWeight = adjacentEdges[otherVertex];

                if (edgeWeight < distTo[otherVertex]) {
                    // This is the new best connection from the minimum spanning tree to otherVertex
                    if (distTo[otherVertex] != Double.POSITIVE_INFINITY) {
                        weight -= distTo[otherVertex];
                    }
                    weight += edgeWeight;

                    edgeTo[otherVertex] = new Edge(vertex, otherVertex, edgeWeight);
                    distTo[otherVertex] = edgeWeight;

                    if (priorityQueue.contains(otherVertex)) {
                        priorityQueue.decreaseKey(otherVertex, distTo[otherVertex]);
                    } else {
                        priorityQueue.insert(otherVertex, distTo[otherVertex]);
                    }
                }
            }
        }

        public Iterable<Edge> edges() {
            Queue<Edge> minimumSpanningTree = new Queue<>();

            for(int vertex = 1; vertex < edgeTo.length; vertex++) {
                minimumSpanningTree.enqueue(edgeTo[vertex]);
            }

            return minimumSpanningTree;
        }

        public double lazyWeight() {
            double weight = 0;

            for(Edge edge : edges()) {
                weight += edge.weight();
            }

            return weight;
        }

        public double eagerWeight() {
            return weight;
        }
    }

    // Space efficient version of Kruskal's algorithm
    // Trade-off: it has runtime complexity of O(V^2)
    public class KruskalMSTSpaceEfficient {

        private Queue<Edge> minimumSpanningTree;
        private double weight;

        public KruskalMSTSpaceEfficient(Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficientInterface edgeWeightedGraphSpaceEfficient) {
            minimumSpanningTree = new Queue<>();
            PriorityQueueResize<Edge> priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

            // The O(V^2) comes from the EdgeWeightedGraphSpaceEfficient.edges() method
            for(Edge edge : edgeWeightedGraphSpaceEfficient.edges()) {
                priorityQueue.insert(edge);
            }

            UnionFind unionFind = new UnionFind(edgeWeightedGraphSpaceEfficient.vertices());

            while (!priorityQueue.isEmpty() && minimumSpanningTree.size() < edgeWeightedGraphSpaceEfficient.vertices() - 1) {
                Edge edge = priorityQueue.deleteTop(); // Get lowest-weight edge from priority queue
                int vertex1 = edge.either();
                int vertex2 = edge.other(vertex1);

                // Ignore ineligible edges
                if (unionFind.connected(vertex1, vertex2)) {
                    continue;
                }

                unionFind.union(vertex1, vertex2);
                minimumSpanningTree.enqueue(edge); // Add edge to the minimum spanning tree

                weight += edge.weight();
            }
        }

        public Iterable<Edge> edges() {
            return minimumSpanningTree;
        }

        public double lazyWeight() {
            double weight = 0;

            for(Edge edge : edges()) {
                weight += edge.weight();
            }

            return weight;
        }

        public double eagerWeight() {
            return weight;
        }
    }

    public class RandomEdgeWeightedGraphsSpaceEfficient {

        public Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficient erdosRenyiGraphUniformWeights(int vertices,
                                                                                                                     int edges) {
            Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficient randomEdgeWeightedGraphSpaceEfficient =
                    spaceEfficientDataStructures.new EdgeWeightedGraphSpaceEfficient(vertices);

            for(int edge = 0; edge < edges; edge++) {
                int vertexId1 = StdRandom.uniform(vertices);
                int vertexId2 = StdRandom.uniform(vertices);

                double uniformRandomWeight = StdRandom.uniform();
                Edge newEdge = new Edge(vertexId1, vertexId2, uniformRandomWeight);

                randomEdgeWeightedGraphSpaceEfficient.addEdge(newEdge);
            }

            return randomEdgeWeightedGraphSpaceEfficient;
        }

        public Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficient erdosRenyiGraphGaussianWeights(int vertices,
                                                                                                                      int edges) {
            Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficient randomEdgeWeightedGraphSpaceEfficient =
                    spaceEfficientDataStructures.new EdgeWeightedGraphSpaceEfficient(vertices);

            for(int edge = 0; edge < edges; edge++) {
                int vertexId1 = StdRandom.uniform(vertices);
                int vertexId2 = StdRandom.uniform(vertices);

                double gaussianRandomWeight = StdRandom.gaussian();
                Edge newEdge = new Edge(vertexId1, vertexId2, gaussianRandomWeight);

                randomEdgeWeightedGraphSpaceEfficient.addEdge(newEdge);
            }

            return randomEdgeWeightedGraphSpaceEfficient;
        }
    }

    public class EuclideanEdgeWeightedGraphSpaceEfficient implements
            Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficientInterface {

        public class Vertex {
            private int id;
            private String name;
            private double xCoordinate;
            private double yCoordinate;

            Vertex(int id, double xCoordinate, double yCoordinate) {
                this(id, String.valueOf(id), xCoordinate, yCoordinate);
            }

            Vertex(int id, String name, double xCoordinate, double yCoordinate) {
                this.id = id;
                this.name = name;
                this.xCoordinate = xCoordinate;
                this.yCoordinate = yCoordinate;
            }

            public void updateName(String name) {
                this.name = name;
            }
        }

        private final int vertices;
        private int edges;
        private Vertex[] allVertices;
        private double[][] adjacent;

        public EuclideanEdgeWeightedGraphSpaceEfficient(int vertices) {
            this.vertices = vertices;
            edges = 0;
            allVertices = new Vertex[vertices];
            adjacent = new double[vertices][vertices];

            for(int vertex = 0; vertex < vertices; vertex++) {
                adjacent[vertex] = new double[vertices];

                for(int adjacentVertex = 0; adjacentVertex < vertices; adjacentVertex++) {
                    adjacent[vertex][adjacentVertex] = NO_CONNECTION;
                }
            }
        }

        public int vertices() {
            return vertices;
        }

        public int edgesCount() {
            return edges;
        }

        public void addVertex(Vertex vertex) {
            allVertices[vertex.id] = vertex;
        }

        public void addEdge(Edge edge) {
            int vertexId1 = edge.either();
            int vertexId2 = edge.other(vertexId1);

            if (allVertices[vertexId1] == null || allVertices[vertexId2] == null) {
                throw new IllegalArgumentException("Vertex id not found");
            }

            if (adjacent[vertexId1][vertexId2] != NO_CONNECTION) {
                if (adjacent[vertexId1][vertexId2] <= edge.weight()) {
                    return;
                }
                edges--;
            }

            adjacent[vertexId1][vertexId2] = edge.weight();
            adjacent[vertexId2][vertexId1] = edge.weight();
            edges++;
        }

        public void show(double xScaleLow, double xScaleHigh, double yScaleLow, double yScaleHigh, double radiusOfCircleAroundVertex) {
            // Set canvas size
            StdDraw.setCanvasSize(500, 400);
            StdDraw.setXscale(xScaleLow, xScaleHigh);
            StdDraw.setYscale(yScaleLow, yScaleHigh);

            StdDraw.setPenRadius(0.002D);
            StdDraw.setPenColor(Color.BLACK);

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                double[] adjacentEdges = adjacent(vertexId);

                for(int otherVertexId = 0; otherVertexId < adjacentEdges.length; otherVertexId++) {
                    double edgeWeight = adjacentEdges[otherVertexId];

                    if (edgeWeight == NO_CONNECTION) {
                        continue;
                    }

                    Vertex otherVertex = allVertices[otherVertexId];

                    if (otherVertexId >= vertexId) {
                        StdDraw.line(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                                otherVertex.xCoordinate, otherVertex.yCoordinate);
                    }
                }
            }

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                if (allVertices[vertexId] != null) {

                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.filledCircle(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                            radiusOfCircleAroundVertex);

                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.circle(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                            radiusOfCircleAroundVertex);

                    StdDraw.setPenColor(Color.BLUE);
                    StdDraw.text(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                            allVertices[vertexId].name);
                }
            }
        }

        public double[] adjacent(int vertexId) {
            return adjacent[vertexId];
        }

        public Iterable<Edge> edges() {
            Bag<Edge> edges = new Bag<>();

            for(int vertexId1 = 0; vertexId1 < vertices; vertexId1++) {
                for(int vertexId2 = vertexId1 + 1; vertexId2 < vertices; vertexId2++) {
                    if (adjacent[vertexId1][vertexId2] != NO_CONNECTION) {
                        edges.add(new Edge(vertexId1, vertexId2, adjacent[vertexId1][vertexId2]));
                    }
                }
            }

            return edges;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertexId1 = 0; vertexId1 < vertices; vertexId1++) {
                stringBuilder.append(vertexId1).append(": ");

                for(int vertexId2 = 0; vertexId2 < vertices; vertexId2++) {
                    if (adjacent[vertexId1][vertexId2] != NO_CONNECTION) {
                        String formattedEdge = String.format("%d-%d %.5f", vertexId1, vertexId2, adjacent[vertexId1][vertexId2]);
                        stringBuilder.append(formattedEdge).append(" ");
                    }
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public class RandomEuclideanEdgeWeightedGraphSpaceEfficient {

        public Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficientInterface randomEuclideanEdgeWeightedGraphSpaceEfficient(int vertices) {
            Exercise40_ReducedOverhead.EuclideanEdgeWeightedGraphSpaceEfficient euclideanEdgeWeightedGraphSpaceEfficient =
                    new Exercise40_ReducedOverhead().new EuclideanEdgeWeightedGraphSpaceEfficient(vertices);

            EuclideanEdgeWeightedGraphSpaceEfficient.Vertex[] allVertices =
                    new EuclideanEdgeWeightedGraphSpaceEfficient.Vertex[vertices];

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                double randomXCoordinate = StdRandom.uniform();
                double randomYCoordinate = StdRandom.uniform();

                EuclideanEdgeWeightedGraphSpaceEfficient.Vertex vertex =
                        euclideanEdgeWeightedGraphSpaceEfficient.new Vertex(vertexId, randomXCoordinate, randomYCoordinate);
                allVertices[vertexId] = vertex;

                euclideanEdgeWeightedGraphSpaceEfficient.addVertex(vertex);
            }

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                for(int otherVertexId = vertexId + 1; otherVertexId < vertices; otherVertexId++) {
                    double distance = MathUtil.distanceBetweenPoints(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                            allVertices[otherVertexId].xCoordinate, allVertices[otherVertexId].yCoordinate);

                    Edge edge = new Edge(vertexId, otherVertexId, distance);
                    euclideanEdgeWeightedGraphSpaceEfficient.addEdge(edge);
                }
            }

            return euclideanEdgeWeightedGraphSpaceEfficient;
        }
    }

    private static final int DEFAULT_EDGE_WEIGHTED_GRAPH_ID = 0;
    private static final int SPACE_EFFICIENT_EDGE_WEIGHTED_GRAPH_ID = 1;

    private static final int LAZY_PRIM_ID = 0;
    private static final int EAGER_PRIM_ID = 1;
    private static final int KRUSKAL_ID = 2;

    private void generateGraphsAndDoExperiments(int experiments, int vertices, int edges, int euclideanGraphVertices) {

        StdOut.printf("%63s %18s %12s %10s %10s\n", "Edge Weighted Graph type | ", "MST Algorithm | ", "Vertices | ",
                "Edges | ", "Average time spent");

        String[] mstAlgorithms = {"Lazy Prim MST", "Eager Prim MST", "Kruskal MST"};
        String[][] graphTypes = {
                {"Random graph w/ uniform weight distribution",
                        "Random graph w/ gaussian weight distribution",
                        "Random Euclidean graph"},
                {"Random space-efficient graph w/ uniform weight distribution",
                        "Random-space efficient graph w/ gaussian weight distribution",
                        "Random-space efficient Euclidean graph"}
        };

        double totalTimeSpent = 0;

        // Graph model 1: Random edge weighted graphs with uniform weight distribution

        Exercise34_RandomSparseEdgeWeightedGraphs.RandomEdgeWeightedGraphs randomEdgeWeightedGraphs =
                new Exercise34_RandomSparseEdgeWeightedGraphs().new RandomEdgeWeightedGraphs();
        RandomEdgeWeightedGraphsSpaceEfficient randomEdgeWeightedGraphsSpaceEfficient =
                new RandomEdgeWeightedGraphsSpaceEfficient();

        int graphTypeId = 0;

        for(int mstAlgorithmType = 0; mstAlgorithmType < 3; mstAlgorithmType++) {
            for(int graphTypeSpaceEfficientId = 0; graphTypeSpaceEfficientId < 2; graphTypeSpaceEfficientId++) {
                String mstAlgorithm = mstAlgorithms[mstAlgorithmType];

                for(int experiment = 0; experiment < experiments; experiment++) {

                    if (graphTypeSpaceEfficientId == DEFAULT_EDGE_WEIGHTED_GRAPH_ID) {
                        EdgeWeightedGraph randomEdgeWeightedGraphUniformWeights =
                                randomEdgeWeightedGraphs.erdosRenyiGraphUniformWeights(vertices, edges);

                        totalTimeSpent += doExperiment(randomEdgeWeightedGraphUniformWeights, mstAlgorithmType);
                    } else if (graphTypeSpaceEfficientId == SPACE_EFFICIENT_EDGE_WEIGHTED_GRAPH_ID) {
                        Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficient
                                randomEdgeWeightedGraphUniformWeightsSpaceEfficient =
                                randomEdgeWeightedGraphsSpaceEfficient.erdosRenyiGraphUniformWeights(vertices, edges);

                        totalTimeSpent += doExperiment(randomEdgeWeightedGraphUniformWeightsSpaceEfficient, mstAlgorithmType);
                    }
                }

                double averageTimeSpent = totalTimeSpent / experiments;
                printResults(graphTypes[graphTypeSpaceEfficientId][graphTypeId], mstAlgorithm, vertices, edges,
                        averageTimeSpent);

                totalTimeSpent = 0;
            }
        }

        // Graph model 2: Random edge weighted graphs with gaussian weight distribution
        graphTypeId = 1;

        for(int mstAlgorithmType = 0; mstAlgorithmType < 3; mstAlgorithmType++) {
            for(int graphTypeSpaceEfficientId = 0; graphTypeSpaceEfficientId < 2; graphTypeSpaceEfficientId++) {
                String mstAlgorithm = mstAlgorithms[mstAlgorithmType];

                for(int experiment = 0; experiment < experiments; experiment++) {

                    if (graphTypeSpaceEfficientId == DEFAULT_EDGE_WEIGHTED_GRAPH_ID) {
                        EdgeWeightedGraph randomEdgeWeightedGraphUniformWeights =
                                randomEdgeWeightedGraphs.erdosRenyiGraphGaussianWeights(vertices, edges);

                        totalTimeSpent += doExperiment(randomEdgeWeightedGraphUniformWeights, mstAlgorithmType);
                    } else if (graphTypeSpaceEfficientId == SPACE_EFFICIENT_EDGE_WEIGHTED_GRAPH_ID) {
                        Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficient
                                randomEdgeWeightedGraphGaussianWeightsSpaceEfficient =
                                randomEdgeWeightedGraphsSpaceEfficient.erdosRenyiGraphGaussianWeights(vertices, edges);

                        totalTimeSpent += doExperiment(randomEdgeWeightedGraphGaussianWeightsSpaceEfficient, mstAlgorithmType);
                    }
                }

                double averageTimeSpent = totalTimeSpent / experiments;
                printResults(graphTypes[graphTypeSpaceEfficientId][graphTypeId], mstAlgorithm, vertices, edges,
                        averageTimeSpent);

                totalTimeSpent = 0;
            }
        }

        // Graph model 3: Random edge weighted Euclidean graphs
        graphTypeId = 2;

        Exercise35_RandomEuclideanEdgeWeightedGraphs randomEuclideanEdgeWeightedGraphs =
                new Exercise35_RandomEuclideanEdgeWeightedGraphs();
        RandomEuclideanEdgeWeightedGraphSpaceEfficient randomEuclideanEdgeWeightedGraphSpaceEfficient =
                new RandomEuclideanEdgeWeightedGraphSpaceEfficient();
        // Running the experiment on a complete graph
        double radius = 1;

        for(int mstAlgorithmType = 0; mstAlgorithmType < 3; mstAlgorithmType++) {
            for(int graphTypeSpaceEfficientId = 0; graphTypeSpaceEfficientId < 2; graphTypeSpaceEfficientId++) {
                String mstAlgorithm = mstAlgorithms[mstAlgorithmType];

                for(int experiment = 0; experiment < experiments; experiment++) {

                    if (graphTypeSpaceEfficientId == DEFAULT_EDGE_WEIGHTED_GRAPH_ID) {
                        EdgeWeightedGraphInterface randomEdgeWeightedEuclideanGraph =
                                randomEuclideanEdgeWeightedGraphs.randomEuclideanEdgeWeightedGraph(euclideanGraphVertices,
                                        radius);

                        edges = randomEdgeWeightedEuclideanGraph.edgesCount();

                        totalTimeSpent += doExperiment(randomEdgeWeightedEuclideanGraph, mstAlgorithmType);
                    } else if (graphTypeSpaceEfficientId == SPACE_EFFICIENT_EDGE_WEIGHTED_GRAPH_ID) {
                        Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficientInterface
                                randomEdgeWeightedEuclideanGraphSpaceEfficient =
                                randomEuclideanEdgeWeightedGraphSpaceEfficient.randomEuclideanEdgeWeightedGraphSpaceEfficient(euclideanGraphVertices);

                        edges = randomEdgeWeightedEuclideanGraphSpaceEfficient.edgesCount();

                        totalTimeSpent += doExperiment(randomEdgeWeightedEuclideanGraphSpaceEfficient, mstAlgorithmType);
                    }
                }

                double averageTimeSpent = totalTimeSpent / experiments;
                printResults(graphTypes[graphTypeSpaceEfficientId][graphTypeId], mstAlgorithm, euclideanGraphVertices, edges,
                        averageTimeSpent);

                totalTimeSpent = 0;
            }
        }
    }

    private double doExperiment(EdgeWeightedGraphInterface edgeWeightedGraph, int mstAlgorithmType) {
        Stopwatch stopwatch = new Stopwatch();

        switch (mstAlgorithmType) {
            case LAZY_PRIM_ID: new LazyPrimMST(edgeWeightedGraph);
                break;
            case EAGER_PRIM_ID: new PrimMST(edgeWeightedGraph);
                break;
            case KRUSKAL_ID: new KruskalMST(edgeWeightedGraph);
                break;
        }

        return stopwatch.elapsedTime();
    }

    private double doExperiment(Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficientInterface edgeWeightedGraphSpaceEfficient,
                                int mstAlgorithmType) {
        Stopwatch stopwatch = new Stopwatch();

        switch (mstAlgorithmType) {
            case LAZY_PRIM_ID: spaceEfficientDataStructures.new LazyPrimMSTSpaceEfficient(edgeWeightedGraphSpaceEfficient);
                break;
            case EAGER_PRIM_ID: new PrimMSTSpaceEfficient(edgeWeightedGraphSpaceEfficient);
                break;
            case KRUSKAL_ID: new KruskalMSTSpaceEfficient(edgeWeightedGraphSpaceEfficient);
                break;
        }

        return stopwatch.elapsedTime();
    }

    private void printResults(String graphType, String mstAlgorithm, int vertices, int edges, double averageTimeSpent) {
        StdOut.printf("%60s %18s %12d %10d %21.2f\n", graphType, mstAlgorithm, vertices, edges, averageTimeSpent);
    }

    // Parameters example: 10 2500 5000 250
    public static void main(String[] args) {
        //new Exercise40_ReducedOverhead().tests();

        int experiments = Integer.parseInt(args[0]);
        int vertices = Integer.parseInt(args[1]);
        int edges = Integer.parseInt(args[2]);

        // In the Euclidean graph all vertices are connected to all vertices.
        // So this requires a separate number of vertices to avoid a very high number of edges while still having a dense graph.
        int euclideanGraphVertices = Integer.parseInt(args[3]);

        new Exercise40_ReducedOverhead().generateGraphsAndDoExperiments(experiments, vertices, edges, euclideanGraphVertices);
    }

    private void tests() {

        // Test PrimMSTSpaceEfficient
        StdOut.println("PrimMSTSpaceEfficient test");

        Exercise28_SpaceEfficientDataStructures.EdgeWeightedGraphSpaceEfficientInterface edgeWeightedGraphSpaceEfficient =
                spaceEfficientDataStructures.new EdgeWeightedGraphSpaceEfficient(5);
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(0, 3, 0.5));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(1, 2, 0.12));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(1, 4, 0.91));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(2, 3, 0.72));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(3, 4, 0.8));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(3, 4, 0.82));
        edgeWeightedGraphSpaceEfficient.addEdge(new Edge(4, 4, 0.1));

        PrimMSTSpaceEfficient primMSTSpaceEfficient = new PrimMSTSpaceEfficient(edgeWeightedGraphSpaceEfficient);

        for(Edge edge : primMSTSpaceEfficient.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "0-1 0.42000\n" +
                "1-2 0.12000\n" +
                "0-3 0.50000\n" +
                "3-4 0.80000\n");

        // Test KruskalSpaceEfficient

        StdOut.println("KruskalSpaceEfficient test");

        KruskalMSTSpaceEfficient kruskalMSTSpaceEfficient = new KruskalMSTSpaceEfficient(edgeWeightedGraphSpaceEfficient);

        for(Edge edge : kruskalMSTSpaceEfficient.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "1-2 0.12000\n" +
                "0-1 0.42000\n" +
                "0-3 0.50000\n" +
                "3-4 0.80000");
    }

}
