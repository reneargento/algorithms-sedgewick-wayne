package chapter4.section1;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

import java.util.Arrays;


/**
 * Created by Rene Argento on 17/09/17.
 */
public class Exercise24 {

    //A graph that allows directed edges
    private class ExtendedGraph extends Graph {
        public ExtendedGraph(int vertices) {
            super(vertices);
        }

        public void addDirectedEdge(int vertex1, int vertex2) {
            getAdjacencyList()[vertex1].add(vertex2);
            edges++;
        }
    }

    public static void main(String[] args) {
        String filePath = Constants.FILES_PATH + Constants.MOVIES_FILE;
        String separator = "/";
        SymbolGraph movieSymbolGraph = new SymbolGraph(filePath, separator);
        Graph graph = movieSymbolGraph.graph();

        String kevinBaconName = "Bacon, Kevin";
        int kevinBaconId = movieSymbolGraph.index(kevinBaconName);

        //Used for tests: tinyGex2 graph from the book

//        Graph graph = new Graph(12);
//        graph.addEdge(8 ,4);
//        graph.addEdge(2 ,3);
//        graph.addEdge(1 ,11);
//        graph.addEdge(0 ,6);
//        graph.addEdge(3 ,6);
//        graph.addEdge(10 ,3);
//        graph.addEdge(7 ,11);
//        graph.addEdge(7 ,8);
//        graph.addEdge(11 ,8);
//        graph.addEdge(2 ,0);
//        graph.addEdge(6 ,2);
//        graph.addEdge(5 ,2);
//        graph.addEdge(5 ,10);
//        graph.addEdge(5 ,0);
//        graph.addEdge(8 ,1);
//        graph.addEdge(4 ,1);
//        int kevinBaconId = 0;

        new Exercise24().computeComponentsInformation(graph, kevinBaconId);
    }

    private void computeComponentsInformation(Graph graph, int kevinBaconId) {

        ConnectedComponents connectedComponents = new ConnectedComponentsIterativeDFS(graph);
        int[] componentSizes = new int[connectedComponents.count()];

        for(int vertex = 0; vertex < graph.vertices(); vertex++) {
            componentSizes[connectedComponents.id(vertex)]++;
        }

        int largestComponentId = 0;
        int largestComponentSize = 0;
        int numberOfComponentsOfSizeLessThan10 = 0;

        for(int componentId = 0; componentId < componentSizes.length; componentId++) {
            int currentComponentSize = componentSizes[componentId];

            if (currentComponentSize > largestComponentSize) {
                largestComponentSize = currentComponentSize;
                largestComponentId = componentId;
            }

            if (currentComponentSize < 10) {
                numberOfComponentsOfSizeLessThan10++;
            }
        }

        StdOut.println("Number of connected components: " + connectedComponents.count());
        StdOut.println("Size of the largest component: " + largestComponentSize);
        StdOut.println("Number of components of size less than 10: " + numberOfComponentsOfSizeLessThan10);

        getLargestComponentInformationUsingDomainKnowledge(graph, connectedComponents, largestComponentSize,
                largestComponentId, kevinBaconId);
    }

    private void getLargestComponentInformationUsingDomainKnowledge(Graph graph, ConnectedComponents connectedComponents,
                                                                    int largestComponentSize, int largestComponentId, int kevinBaconId) {
        // Create subGraph with the largest component

        // Used to map the vertex indices from the graph to the subGraph and vice-versa
        SeparateChainingHashTable<Integer, Integer> subGraphToGraphVertexMap =
                new SeparateChainingHashTable<>(largestComponentSize, 10);
        SeparateChainingHashTable<Integer, Integer> graphToSubGraphVertexMap =
                new SeparateChainingHashTable<>(largestComponentSize, 10);

        Graph largestComponentSubGraph = createSubGraph(graph, connectedComponents, largestComponentSize,
                largestComponentId, subGraphToGraphVertexMap, graphToSubGraphVertexMap);

        int kevinBaconIdInSubGraph = graphToSubGraphVertexMap.get(kevinBaconId);

        GraphProperties graphProperties = new GraphProperties(largestComponentSubGraph, true);
        int[] verticesFurthestFromTheCenter = getVerticesFurthestFromTheCenter(largestComponentSubGraph, kevinBaconIdInSubGraph);
        int[] verticesToComputeProperties = Arrays.copyOf(verticesFurthestFromTheCenter, verticesFurthestFromTheCenter.length + 1);

        //Get the eccentricities of the vertices furthest from the center and of Kevin Bacon
        verticesToComputeProperties[verticesToComputeProperties.length - 1] = kevinBaconIdInSubGraph;

        graphProperties.computeProperties(verticesToComputeProperties);

        StdOut.println("\nEccentricities of Kevin Bacon and of vertices furthest from the center in the largest component:");
        for(int vertex = 0; vertex < verticesToComputeProperties.length; vertex++) {
            int originalVertexId = subGraphToGraphVertexMap.get(verticesToComputeProperties[vertex]);
            StdOut.println("Eccentricity of vertex " + originalVertexId + ": " +
                    graphProperties.eccentricity(verticesToComputeProperties[vertex]));
        }

        StdOut.println("\nDiameter of largest component: " + graphProperties.diameter());
        StdOut.println("Radius of largest component: " + graphProperties.radius());
        StdOut.println("Center of largest component: " + subGraphToGraphVertexMap.get(graphProperties.center()));

        //Girth - the smallest possible cycle has size 4:
        // Actor -- Movie -- Actor
        //     \         /
        //      \ Movie /

        // Seems like a common scenario, so we check for a girth of this value
        int smallestPossibleCycle = 4;
        int girth = graphProperties.computeGirthLessOrEqualTo(smallestPossibleCycle);
        StdOut.println("Girth of largest component: " + girth);

        boolean doesTheLargestComponentContainKevinBacon = connectedComponents.id(kevinBaconId) == largestComponentId;
        StdOut.print("Does the largest component contain Kevin Bacon: ");
        if (doesTheLargestComponentContainKevinBacon) {
            StdOut.println("Yes");
        } else {
            StdOut.println("No");
        }
    }

    private Graph createSubGraph(Graph graph, ConnectedComponents connectedComponents,
                                 int largestComponentSize, int largestComponentId,
                                 SeparateChainingHashTable<Integer, Integer> subGraphToGraphVertexMap,
                                 SeparateChainingHashTable<Integer, Integer> graphToSubGraphVertexMap) {
        // Create subGraph with the largest component
        ExtendedGraph largestComponentSubGraph = new ExtendedGraph(largestComponentSize);

        for(int vertex = 0; vertex < graph.vertices(); vertex++) {
            if (connectedComponents.id(vertex) == largestComponentId) {
                if (!graphToSubGraphVertexMap.contains(vertex)) {
                    int subGraphVertexId = graphToSubGraphVertexMap.size();

                    graphToSubGraphVertexMap.put(vertex, subGraphVertexId);
                    subGraphToGraphVertexMap.put(subGraphVertexId, vertex);
                }
            }
        }

        //Create subGraph
        for(int vertex = 0; vertex < graph.vertices(); vertex++) {
            if (connectedComponents.id(vertex) == largestComponentId) {
                int subGraphVertexId = graphToSubGraphVertexMap.get(vertex);

                for(int adjacentVertex : graph.adjacent(vertex)) {
                    int subGraphNeighborVertexId = graphToSubGraphVertexMap.get(adjacentVertex);
                    largestComponentSubGraph.addDirectedEdge(subGraphVertexId, subGraphNeighborVertexId);
                }
            }
        }

        return largestComponentSubGraph;
    }

    // We know Kevin Bacon is the center, so get the furthest vertices from him
    // to compute the diameter and eccentricities later
    private int[] getVerticesFurthestFromTheCenter(Graph largestComponentSubGraph, int kevinBaconId) {
        final int MAX_BACON = 100;

        //Get Kevin Bacon numbers
        BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(largestComponentSubGraph, kevinBaconId);

        int[] kevinBaconNumbers = new int[MAX_BACON + 1];
        int maxDistance = 0;

        for(int vertex = 0; vertex < largestComponentSubGraph.vertices(); vertex++) {
            int kevinBaconNumber = breadthFirstPaths.distTo(vertex);

            //The subGraph is connected, so this should never happen
            if (kevinBaconNumber == Integer.MAX_VALUE) {
                throw new RuntimeException("Graph is not connected");
            }

            kevinBaconNumbers[kevinBaconNumber]++;
            if (kevinBaconNumber > maxDistance) {
                maxDistance = kevinBaconNumber;
            }
        }

        int[] verticesFurthestFromTheCenter = new int[kevinBaconNumbers[maxDistance]];
        int farAwayVerticesIndex = 0;
        for(int vertex = 0; vertex < largestComponentSubGraph.vertices(); vertex++) {
            if (breadthFirstPaths.distTo(vertex) == maxDistance) {
                verticesFurthestFromTheCenter[farAwayVerticesIndex++] = vertex;
            }
        }

        return verticesFurthestFromTheCenter;
    }

    //This is the method that would be used if we did not have domain knowledge, or if the largest component were small
    private void getLargestComponentInformation(Graph graph, ConnectedComponents connectedComponents,
                                                int largestComponentSize, int largestComponentId, int kevinBaconId) {
        // Create subGraph with the largest component
        SeparateChainingHashTable<Integer, Integer> subGraphToGraphVertexMap =
                new SeparateChainingHashTable<>(largestComponentSize, 10);
        SeparateChainingHashTable<Integer, Integer> graphToSubGraphVertexMap =
                new SeparateChainingHashTable<>(largestComponentSize, 10);

        Graph largestComponentSubGraph = createSubGraph(graph, connectedComponents, largestComponentSize,
                largestComponentId, subGraphToGraphVertexMap, graphToSubGraphVertexMap);

        GraphProperties graphProperties = new GraphProperties(largestComponentSubGraph, true);
        StdOut.println("Eccentricities of largest component:");
        for(int vertex = 0; vertex < largestComponentSubGraph.vertices(); vertex++) {
            int originalVertexId = subGraphToGraphVertexMap.get(vertex);
            StdOut.println("Eccentricity of vertex " + originalVertexId + ": " + graphProperties.eccentricity(vertex));
        }

        StdOut.println("\nDiameter of largest component: " + graphProperties.diameter());
        StdOut.println("Radius of largest component: " + graphProperties.radius());
        StdOut.println("Center of largest component: " + graphProperties.center());
        StdOut.println("Girth of largest component: " + graphProperties.girth());

        boolean doesTheLargestComponentContainKevinBacon = connectedComponents.id(kevinBaconId) == largestComponentId;
        StdOut.print("Does the largest component contain Kevin Bacon: ");
        if (doesTheLargestComponentContainKevinBacon) {
            StdOut.println("Yes");
        } else {
            StdOut.println("No");
        }
    }

}