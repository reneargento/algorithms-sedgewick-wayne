package chapter6.networkflow;

import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 22/10/18.
 */
public class Exercise44_ProductDistribution {

    private FlowNetwork generateCitiesMap(int vertices, int edges, int maxRoadCapacity) {
        FlowNetwork flowNetwork = new FlowNetwork(vertices);
        Exercise41.RandomNetworkGenerator randomNetworkGenerator = new Exercise41().new RandomNetworkGenerator();

        for (int i = 0; i < edges; i++) {
            Exercise41.VertexPair vertexPair = randomNetworkGenerator.getRandomVerticesPair(vertices);
            int randomCapacity = StdRandom.uniform(maxRoadCapacity);
            flowNetwork.addEdge(new FlowEdge(vertexPair.vertex1, vertexPair.vertex2, randomCapacity));
        }

        return flowNetwork;
    }

    private boolean generateDailyOrder(FlowNetwork citiesMap, int vertices, int maxNumberOfSources,
                                       int maxNumberOfTargets, int maxRoadCapacity) {
        int maxNumberOfSourcesMinus2 = maxNumberOfSources - 2;
        int maxNumberOfTargetsMinus2 = maxNumberOfTargets - 2;
        int numberOfSources = 2 + StdRandom.uniform(maxNumberOfSourcesMinus2);
        int numberOfTargets = 2 + StdRandom.uniform(maxNumberOfTargetsMinus2);

        int[] sources = new int[numberOfSources];
        int[] productsToPickUp = new int[numberOfSources];
        int productsToDistribute = 0;
        int maxFlowExpected;

        int[] targets = new int[numberOfTargets];
        int[] productsToDeliver = new int[numberOfTargets];

        HashSet<Integer> usedVertices = new HashSet<>();

        // Add random sources
        for (int i = 0; i < numberOfSources; i++) {
            sources[i] = getRandomUnusedVertex(vertices, usedVertices);
            productsToPickUp[i] = StdRandom.uniform(maxRoadCapacity);
            productsToDistribute += productsToPickUp[i];
        }

        maxFlowExpected = productsToDistribute;

        // Add random targets
        for (int i = 0; i < numberOfTargets; i++) {
            targets[i] = getRandomUnusedVertex(vertices, usedVertices);

            if (i != numberOfTargets - 1) {
                productsToDeliver[i] = StdRandom.uniform(productsToDistribute);
                productsToDistribute -= productsToDeliver[i];
            } else {
                // Make sure all products are delivered
                productsToDeliver[i] = productsToDistribute;
            }
        }

        FlowNetwork flowNetwork = new FlowNetwork(citiesMap.vertices() + 2);

        for (FlowEdge edge : citiesMap.edges()) {
            flowNetwork.addEdge(new FlowEdge(edge.from(), edge.to(), edge.capacity()));
        }

        // Add super source and super target
        int source = citiesMap.vertices();
        int target = source + 1;

        for (int i = 0; i < sources.length; i++) {
            flowNetwork.addEdge(new FlowEdge(source, sources[i], productsToPickUp[i]));
        }

        for (int i = 0; i < targets.length; i++) {
            flowNetwork.addEdge(new FlowEdge(targets[i], target, productsToDeliver[i]));
        }

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, source, target);
        int maxFlow = (int) fordFulkerson.maxFlowValue();

        if (maxFlow == maxFlowExpected) {
            printMaxFlow(flowNetwork, fordFulkerson.maxFlowValue(), sources, targets, productsToPickUp,
                    productsToDeliver);
            return true;
        } else {
            // It is not possible to complete the daily order in the current map configuration
            return false;
        }
    }

    private int getRandomUnusedVertex(int vertices, HashSet<Integer> usedVertices) {
        int randomVertex = StdRandom.uniform(vertices);

        while (usedVertices.contains(randomVertex)) {
            randomVertex = StdRandom.uniform(vertices);
        }

        usedVertices.add(randomVertex);
        return randomVertex;
    }

    private void printMaxFlow(FlowNetwork flowNetwork, double maxFlowValue, int[] sources, int[] targets,
                              int[] productsToPickUp, int[] productsToDeliver) {
        StdOut.println("*** Daily order ***");

        StdOut.println("Pick up products:");
        for (int i = 0; i < sources.length; i++) {
            if (productsToPickUp[i] == 0) {
                continue;
            }

            StdOut.printf("City: %3d Products: %4d\n", sources[i], productsToPickUp[i]);
        }

        StdOut.println("\nDeliver products:");
        for (int i = 0; i < targets.length; i++) {
            if (productsToDeliver[i] == 0) {
                continue;
            }

            StdOut.printf("City: %3d Products: %4d\n", targets[i], productsToDeliver[i]);
        }

        StdOut.println();

        int source = flowNetwork.vertices() - 2;
        int target = source + 1;

        for (int vertex = 0; vertex < flowNetwork.vertices(); vertex++) {
            for (FlowEdge edge : flowNetwork.adjacent(vertex)) {
                if (edge.from() == source || edge.to() == target) {
                    continue;
                }

                if (vertex == edge.from() && edge.flow() > 0) {
                    StdOut.println("    " + edge);
                }
            }
        }
        StdOut.println("Products delivered = " + (int) maxFlowValue);
    }

    public static void main(String[] args) {
        int vertices = 30;
        int edges = 100;
        int days = 5;
        int maxNumberOfSources = 5;
        int maxNumberOfTargets = 5;
        int maxRoadCapacity = 200;

        Exercise44_ProductDistribution productDistribution = new Exercise44_ProductDistribution();
        FlowNetwork citiesMap = productDistribution.generateCitiesMap(vertices, edges, maxRoadCapacity);

        for (int day = 0; day < days; day++) {
            boolean possibleToDistribute = productDistribution.generateDailyOrder(citiesMap, vertices,
                    maxNumberOfSources, maxNumberOfTargets, maxRoadCapacity);

            if (!possibleToDistribute) {
                day--;
                continue;
            }

            if (day != days - 1) {
                StdOut.println();
            }
        }
    }

}