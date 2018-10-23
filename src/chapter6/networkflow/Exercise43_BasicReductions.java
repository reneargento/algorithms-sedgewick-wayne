package chapter6.networkflow;

import chapter3.section5.HashSet;
import chapter4.section3.Edge;
import chapter4.section3.EdgeWeightedGraph;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Rene Argento on 20/10/18.
 */
public class Exercise43_BasicReductions {

    private class FlowEdgeWithLowerBoundCapacity {
        private final int vertex1;
        private final int vertex2;
        private final double capacity;
        private final double lowerBoundCapacity;
        private double flow;

        public FlowEdgeWithLowerBoundCapacity(int vertex1, int vertex2, double capacity, double lowerBoundCapacity) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.capacity = capacity;
            this.lowerBoundCapacity = lowerBoundCapacity;
            this.flow = 0;
        }

        public int from() {
            return vertex1;
        }

        public int to() {
            return vertex2;
        }

        public double capacity() {
            return capacity;
        }

        public double lowerBoundCapacity() {
            return lowerBoundCapacity;
        }

        public double flow() {
            return flow;
        }

        public int other(int vertex) {
            if (vertex == vertex1) {
                return vertex2;
            } else if (vertex == vertex2) {
                return vertex1;
            } else {
                throw new IllegalArgumentException("Invalid endpoint");
            }
        }
    }

    private class NetworkFlowInformation {
        private boolean isFlowFeasible;
        private FlowNetwork flowNetwork;
        private SeparateChainingHashST<FlowEdge, FlowEdgeWithLowerBoundCapacity> edgeInformation;

        NetworkFlowInformation(boolean isFlowFeasible, FlowNetwork flowNetwork,
                               SeparateChainingHashST<FlowEdge, FlowEdgeWithLowerBoundCapacity> edgeInformation) {
            this.isFlowFeasible = isFlowFeasible;
            this.flowNetwork = flowNetwork;
            this.edgeInformation = edgeInformation;
        }
    }

    // Deal with floating-point roundoff errors
    private static final double FLOATING_POINT_EPSILON = 1E-6;

    public void maxFlowUndirectedGraph(EdgeWeightedGraph edgeWeightedGraph, int source, int target) {
        FlowNetwork flowNetwork = new FlowNetwork(edgeWeightedGraph.vertices());

        for (Edge edge : edgeWeightedGraph.edges()) {
            int vertex1 = edge.either();
            int vertex2 = edge.other(vertex1);

            if (vertex2 == source || vertex1 == target) {
                continue;
            }

            flowNetwork.addEdge(new FlowEdge(vertex1, vertex2, edge.weight()));
        }

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, source, target);
        printMaxFlow(flowNetwork, fordFulkerson.maxFlowValue(), source, target);
    }

    public void maxFlowMultipleSourcesSinks(FlowNetwork originalFlowNetwork, int[] sources, int[] targets) {
        if (sources == null || targets == null || sources.length == 0 || targets.length == 0) {
            throw new IllegalArgumentException("At least one source and one sink must be specified.");
        }

        FlowNetwork flowNetwork = new FlowNetwork(originalFlowNetwork.vertices() + 2);

        for (FlowEdge edge : originalFlowNetwork.edges()) {
            flowNetwork.addEdge(new FlowEdge(edge.from(), edge.to(), edge.capacity()));
        }

        // Add super source and super target
        int source = originalFlowNetwork.vertices();
        int target = source + 1;

        for (int s : sources) {
            flowNetwork.addEdge(new FlowEdge(source, s, Double.POSITIVE_INFINITY));
        }

        for (int t : targets) {
            flowNetwork.addEdge(new FlowEdge(t, target, Double.POSITIVE_INFINITY));
        }

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, source, target);
        printMaxFlow(flowNetwork, fordFulkerson.maxFlowValue(), source, target);
    }

    // Based on:
    // http://codeforces.com/blog/entry/10956
    // https://codeforces.com/blog/entry/48611
    // http://jeffe.cs.illinois.edu/teaching/algorithms/notes/25-maxflowext.pdf
    public void maxFlowLowerBoundCapacities(int vertices, FlowEdgeWithLowerBoundCapacity[] edges,
                                            int source, int target) {

        NetworkFlowInformation maxFlowNetworkFlowInformation = isFlowFeasible(vertices, edges, source, target, 0);

        if (!maxFlowNetworkFlowInformation.isFlowFeasible) {
            StdOut.println("\nFlow is not feasible in this network");
            return;
        }

        // Binary search to find the max flow
        double low = 0;
        double high = Double.MAX_VALUE;
        double maxFlow = 0;

        while (low + FLOATING_POINT_EPSILON < high) {
            double middle = low + (high - low) / 2;

            NetworkFlowInformation networkFlowInformation = isFlowFeasible(vertices, edges, source, target, middle);
            if (networkFlowInformation.isFlowFeasible) {
                low = middle;
                maxFlow = middle;
                maxFlowNetworkFlowInformation = networkFlowInformation;
            } else {
                high = middle;
            }
        }

        printMaxFlow(maxFlowNetworkFlowInformation, maxFlow, source, target);
    }

    private NetworkFlowInformation isFlowFeasible(int vertices, FlowEdgeWithLowerBoundCapacity[] edges, int source,
                                                  int target, double lowerBoundCapacitySearch) {
        FlowNetwork flowNetwork = new FlowNetwork(vertices + 2);

        int newSource = vertices;
        int newTarget = vertices + 1;

        double[] totalIngoingDemand = new double[vertices];
        double[] totalOutgoingDemand = new double[vertices];
        double saturatingFlow = 0;
        SeparateChainingHashST<FlowEdge, FlowEdgeWithLowerBoundCapacity> originalEdgeInformation =
                new SeparateChainingHashST<>();

        // Update all capacities
        for (FlowEdgeWithLowerBoundCapacity edge : edges) {
            if (edge.vertex1 == target && edge.vertex2 == source) {
                continue;
            }

            double newCapacity = edge.capacity - edge.lowerBoundCapacity;

            FlowEdge newEdge = new FlowEdge(edge.vertex1, edge.vertex2, newCapacity);
            flowNetwork.addEdge(newEdge);
            originalEdgeInformation.put(newEdge, edge);

            totalOutgoingDemand[edge.vertex1] += edge.lowerBoundCapacity;
            totalIngoingDemand[edge.vertex2] += edge.lowerBoundCapacity;
        }
        totalOutgoingDemand[target] += lowerBoundCapacitySearch;
        totalIngoingDemand[source] += lowerBoundCapacitySearch;

        // Add edges from the new source and to the new target
        for (int vertex = 0; vertex < vertices; vertex++) {
            double totalDemand = totalIngoingDemand[vertex] - totalOutgoingDemand[vertex];

            if (totalDemand > 0) {
                flowNetwork.addEdge(new FlowEdge(newSource, vertex, totalDemand));
                saturatingFlow += totalDemand;
            } else {
                flowNetwork.addEdge(new FlowEdge(vertex, newTarget, -totalDemand));
            }
        }

        flowNetwork.addEdge(new FlowEdge(target, source, Double.POSITIVE_INFINITY));

        // Check if a flow is feasible: if max flow in the new flow network is equal to the saturating flow
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, newSource, newTarget);
        boolean isFlowFeasible = fordFulkerson.maxFlowValue() == saturatingFlow;

        return new NetworkFlowInformation(isFlowFeasible, flowNetwork, originalEdgeInformation);
    }

    public void maxFlowVertexCapacities(FlowNetwork originalFlowNetwork, int source, int target,
                                        double[] vertexCapacities) {
        int originalNetworkSize = originalFlowNetwork.vertices();
        FlowNetwork flowNetwork = new FlowNetwork(originalNetworkSize * 2);

        // Transform vertex capacities into intermediate edges
        for (int i = 0; i < vertexCapacities.length; i++) {
            flowNetwork.addEdge(new FlowEdge(i, i + originalNetworkSize, vertexCapacities[i]));
        }

        // Add original edges
        for (FlowEdge edge : originalFlowNetwork.edges()) {
            int newOrigin = edge.from() + originalNetworkSize;
            flowNetwork.addEdge(new FlowEdge(newOrigin, edge.to(), edge.capacity()));
        }

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, source, target);
        printMaxFlow(flowNetwork, fordFulkerson.maxFlowValue(), source, target);
    }

    public static void main(String[] args) {
        Exercise43_BasicReductions basicReductions = new Exercise43_BasicReductions();
        Exercise41.RandomNetworkGenerator randomNetworkGenerator = new Exercise41().new RandomNetworkGenerator();
        Exercise41.RandomCapacity randomUniformCapacity = new Exercise41().new RandomUniformCapacity();

        int vertices = 30;
        int edges = 100;
        Exercise41.VertexPair sourceTarget = randomNetworkGenerator.getRandomVerticesPair(vertices);
        int source = sourceTarget.vertex1;
        int target = sourceTarget.vertex2;

        StdOut.println("*** Max flow with undirected graph ***");
        basicReductions.testUndirectedGraphReduction(vertices, edges, source, target, randomNetworkGenerator,
                randomUniformCapacity);

        StdOut.println("\n*** Max flow with multiple sources and sinks ***");
        basicReductions.testMultipleSourcesSinksReduction(vertices, edges, randomNetworkGenerator,
                randomUniformCapacity);

        StdOut.println("\n*** Max flow with lower bound capacities ***");
        basicReductions.testLowerBoundCapacitiesReduction(vertices, edges, source, target, randomNetworkGenerator,
                randomUniformCapacity);

        StdOut.println("\n*** Max flow with vertex capacities ***");
        basicReductions.testVertexCapacitiesReduction(vertices, edges, source, target, randomNetworkGenerator,
                randomUniformCapacity);
    }

    private void testUndirectedGraphReduction(int vertices, int edges, int source, int target,
                                              Exercise41.RandomNetworkGenerator randomNetworkGenerator,
                                              Exercise41.RandomCapacity randomUniformCapacity) {
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(vertices);

        for (int i = 0; i < edges; i++) {
            Exercise41.VertexPair vertexPair = randomNetworkGenerator.getRandomVerticesPair(vertices);
            double randomCapacity = randomUniformCapacity.getRandomCapacity();
            edgeWeightedGraph.addEdge(new Edge(vertexPair.vertex1, vertexPair.vertex2, randomCapacity));
        }

        maxFlowUndirectedGraph(edgeWeightedGraph, source, target);
    }

    private void testMultipleSourcesSinksReduction(int vertices, int edges,
                                                   Exercise41.RandomNetworkGenerator randomNetworkGenerator,
                                                   Exercise41.RandomCapacity randomUniformCapacity) {
        FlowNetwork flowNetwork = new FlowNetwork(vertices);
        int numberOfSourcesAndTargets = 3;

        for (int i = 0; i < edges; i++) {
            Exercise41.VertexPair vertexPair = randomNetworkGenerator.getRandomVerticesPair(vertices);
            double randomCapacity = randomUniformCapacity.getRandomCapacity();
            flowNetwork.addEdge(new FlowEdge(vertexPair.vertex1, vertexPair.vertex2, randomCapacity));
        }

        int[] sources = new int[numberOfSourcesAndTargets];
        int[] targets = new int[numberOfSourcesAndTargets];

        HashSet<Integer> usedVertices = new HashSet<>();

        // Add random sources
        for (int i = 0; i < numberOfSourcesAndTargets; i++) {
            sources[i] = getRandomUnusedVertex(vertices, usedVertices);
        }

        // Add random targets
        for (int i = 0; i < numberOfSourcesAndTargets; i++) {
            targets[i] = getRandomUnusedVertex(vertices, usedVertices);
        }

        maxFlowMultipleSourcesSinks(flowNetwork, sources, targets);
    }

    private int getRandomUnusedVertex(int vertices, HashSet<Integer> usedVertices) {
        int randomVertex = StdRandom.uniform(vertices);

        while (usedVertices.contains(randomVertex)) {
            randomVertex = StdRandom.uniform(vertices);
        }

        usedVertices.add(randomVertex);
        return randomVertex;
    }

    private void testLowerBoundCapacitiesReduction(int vertices, int edges, int source, int target,
                                                   Exercise41.RandomNetworkGenerator randomNetworkGenerator,
                                                   Exercise41.RandomCapacity randomUniformCapacity) {
        FlowEdgeWithLowerBoundCapacity[] edgesWithLowerBoundCapacities = new FlowEdgeWithLowerBoundCapacity[edges];

        for (int i = 0; i < edges; i++) {
            Exercise41.VertexPair vertexPair = randomNetworkGenerator.getRandomVerticesPair(vertices);
            double randomCapacity = randomUniformCapacity.getRandomCapacity();

            double randomLowerBoundCapacity = 0;
            boolean shouldHaveLowerBoundCapacity = StdRandom.uniform(50) == 0;
            if (shouldHaveLowerBoundCapacity) {
                double maxLowerBoundCapacity = randomCapacity * 0.2;
                randomLowerBoundCapacity = StdRandom.uniform(0, maxLowerBoundCapacity);
            }

            edgesWithLowerBoundCapacities[i] = new FlowEdgeWithLowerBoundCapacity(vertexPair.vertex1,
                    vertexPair.vertex2, randomCapacity, randomLowerBoundCapacity);
        }

        maxFlowLowerBoundCapacities(vertices, edgesWithLowerBoundCapacities, source, target);

        FlowEdgeWithLowerBoundCapacity[] edgesWithLowerBoundCapacities1 = new FlowEdgeWithLowerBoundCapacity[4];
        edgesWithLowerBoundCapacities1[0] = new FlowEdgeWithLowerBoundCapacity(0, 1, 3, 1);
        edgesWithLowerBoundCapacities1[1] = new FlowEdgeWithLowerBoundCapacity(1, 2, 3, 1);
        edgesWithLowerBoundCapacities1[2] = new FlowEdgeWithLowerBoundCapacity(2, 3, 4, 2);
        edgesWithLowerBoundCapacities1[3] = new FlowEdgeWithLowerBoundCapacity(3, 1, 2, 1);
        maxFlowLowerBoundCapacities(4, edgesWithLowerBoundCapacities1, 0, 3);
        StdOut.println("Expected max flow: 2");

        // Based on the example in https://codeforces.com/blog/entry/48611
        FlowEdgeWithLowerBoundCapacity[] edgesWithLowerBoundCapacities2 = new FlowEdgeWithLowerBoundCapacity[5];
        edgesWithLowerBoundCapacities2[0] = new FlowEdgeWithLowerBoundCapacity(0, 1, 1, 0);
        edgesWithLowerBoundCapacities2[1] = new FlowEdgeWithLowerBoundCapacity(0, 2, 1, 0);
        edgesWithLowerBoundCapacities2[2] = new FlowEdgeWithLowerBoundCapacity(1, 3, 1, 0);
        edgesWithLowerBoundCapacities2[3] = new FlowEdgeWithLowerBoundCapacity(2, 3, 1, 0);
        edgesWithLowerBoundCapacities2[4] = new FlowEdgeWithLowerBoundCapacity(1, 2, 1, 1);
        maxFlowLowerBoundCapacities(5, edgesWithLowerBoundCapacities2, 0, 3);
        StdOut.println("Expected max flow: 1");
    }

    private void testVertexCapacitiesReduction(int vertices, int edges, int source, int target,
                                               Exercise41.RandomNetworkGenerator randomNetworkGenerator,
                                               Exercise41.RandomCapacity randomUniformCapacity) {
        FlowNetwork flowNetwork = new FlowNetwork(vertices);

        for (int i = 0; i < edges; i++) {
            Exercise41.VertexPair vertexPair = randomNetworkGenerator.getRandomVerticesPair(vertices);
            double randomCapacity = randomUniformCapacity.getRandomCapacity();
            flowNetwork.addEdge(new FlowEdge(vertexPair.vertex1, vertexPair.vertex2, randomCapacity));
        }

        double[] vertexCapacities = new double[vertices];

        for (int i = 0; i < vertexCapacities.length; i++) {
            double randomVertexCapacity = randomUniformCapacity.getRandomCapacity();
            vertexCapacities[i] = randomVertexCapacity;
        }

        maxFlowVertexCapacities(flowNetwork, source, target, vertexCapacities);
    }

    private void printMaxFlow(FlowNetwork flowNetwork, double maxFlowValue, int source, int target) {
        StdOut.println("\nMax flow from " + source + " to " + target);

        for (int vertex = 0; vertex < flowNetwork.vertices(); vertex++) {
            for (FlowEdge edge : flowNetwork.adjacent(vertex)) {
                if (vertex == edge.from() && edge.flow() > 0) {
                    StdOut.println("    " + edge);
                }
            }
        }
        StdOut.println("Max flow value = " + maxFlowValue);
    }

    private void printMaxFlow(NetworkFlowInformation networkFlowInformation, double maxFlowValue, int source,
                              int target) {
        FlowNetwork flowNetwork = networkFlowInformation.flowNetwork;
        SeparateChainingHashST<FlowEdge, FlowEdgeWithLowerBoundCapacity> originalEdges =
                networkFlowInformation.edgeInformation;

        StdOut.println("\nMax flow from " + source + " to " + target);

        for (int vertex = 0; vertex < flowNetwork.vertices(); vertex++) {
            for (FlowEdge edge : flowNetwork.adjacent(vertex)) {
                FlowEdgeWithLowerBoundCapacity originalEdge = originalEdges.get(edge);

                // Filter invalid edges
                if (originalEdge == null) {
                    continue;
                }

                FlowEdge adjustedEdge = new FlowEdge(edge.from(), edge.to(), originalEdge.capacity());
                adjustedEdge.setFlow(edge.flow() + originalEdge.lowerBoundCapacity());

                if (vertex == adjustedEdge.from() && adjustedEdge.flow() > 0) {
                    StdOut.println("    " + adjustedEdge);
                }
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        StdOut.println("Max flow value = " + decimalFormat.format(maxFlowValue));
    }
}