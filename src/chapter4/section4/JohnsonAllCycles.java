package chapter4.section4;

import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Rene Argento on 30/12/17.
 */
// Runtime: O((E + V) * (C + 1), where C is the number of cycles in the graph.
// The number of cycles in a graph can be exponential.
// Space complexity: O(E + V + S) where S is the sum of the number of vertices in all cycles.

// Based on Johnson's original paper http://www.cs.tufts.edu/comp/150GA/homeworks/hw1/Johnson%2075.PDF and
// Tushar Roy's excellent video https://www.youtube.com/watch?v=johyrWospv0 and
// https://github.com/mission-peace/interview/blob/master/src/com/interview/graph/AllCyclesInDirectedGraphJohnson.java
public class JohnsonAllCycles {

    private HashSet<Integer> blockedVerticesSet;
    private SeparateChainingHashTable<Integer, HashSet<Integer>> blockedVerticesMap;
    private Deque<Integer> stack;
    private List<List<Integer>> allCyclesByVertices;

    private Deque<DirectedEdge> stackOfEdges;
    private List<List<DirectedEdge>> allCyclesByEdges;

    private int verticesCount;

    public void findAllCycles(EdgeWeightedDigraphInterface edgeWeightedDigraph) {
        blockedVerticesSet = new HashSet<>();
        blockedVerticesMap = new SeparateChainingHashTable<>();
        stack = new ArrayDeque<>();
        allCyclesByVertices = new ArrayList<>();

        stackOfEdges = new ArrayDeque<>();
        allCyclesByEdges = new ArrayList<>();

        verticesCount = edgeWeightedDigraph.vertices();

        KosarajuSharirSCCWeighted kosarajuSharirSCCWeighted = new KosarajuSharirSCCWeighted(edgeWeightedDigraph);
        List<Integer>[] stronglyConnectedComponents = kosarajuSharirSCCWeighted.getSCCs();

        for(List<Integer> stronglyConnectedComponent : stronglyConnectedComponents) {

            if (stronglyConnectedComponent.size() == 1) {
                continue;
            }

            EdgeWeightedDigraphInterface sccSubGraph = createSubGraphFromSCC(edgeWeightedDigraph, stronglyConnectedComponent);

            for(int vertexToProcess : stronglyConnectedComponent) {
                if (sccSubGraph.outdegree(vertexToProcess) == 0) {
                    continue;
                }

                // Clear blockedVerticesSet and blockedVerticesMap
                blockedVerticesSet = new HashSet<>();
                blockedVerticesMap = new SeparateChainingHashTable<>();

                findCycles(sccSubGraph, vertexToProcess, vertexToProcess, null);

                sccSubGraph = createSubGraphByRemovingVertex(sccSubGraph, vertexToProcess);
            }
        }
    }

    private boolean findCycles(EdgeWeightedDigraphInterface edgeWeightedDigraph, int startVertex, int currentVertex,
                               DirectedEdge currentEdge) {
        boolean foundCycle = false;

        stack.push(currentVertex);
        blockedVerticesSet.add(currentVertex);

        if (currentEdge != null) {
            stackOfEdges.push(currentEdge);
        }

        for(DirectedEdge edge : edgeWeightedDigraph.adjacent(currentVertex)) {
            int neighbor = edge.to();

            // If neighbor is the same as the start vertex, a cycle was found.
            if (neighbor == startVertex) {
                // Add cycle with vertices to the cycles list
                List<Integer> cycle = new ArrayList<>();
                stack.push(startVertex);
                cycle.addAll(stack);

                Collections.reverse(cycle);
                stack.pop();

                allCyclesByVertices.add(cycle);

                // Add cycle with edges to the cycles list
                List<DirectedEdge> cycleByEdges = new ArrayList<>();
                stackOfEdges.push(edge);
                cycleByEdges.addAll(stackOfEdges);

                Collections.reverse(cycleByEdges);
                stackOfEdges.pop();

                allCyclesByEdges.add(cycleByEdges);

                foundCycle = true;
            } else if (!blockedVerticesSet.contains(neighbor)) {
                boolean foundCycleThroughNeighbor = findCycles(edgeWeightedDigraph, startVertex, neighbor, edge);
                foundCycle = foundCycle || foundCycleThroughNeighbor;
            }
        }

        // If a cycle was found with the current vertex, then recursively unblock it and all vertices which are
        // dependent on it
        if (foundCycle) {
            unblock(currentVertex);
        } else {
            // If no cycle was found, add the current vertex to its neighbors blockedVerticesMap.
            // If any of those neighbors ever get unblocked, then unblock the current vertex as well.
            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(currentVertex)) {
                int neighbor = edge.to();

                HashSet<Integer> dependentVerticesFromNeighbor = blockedVerticesMap.get(neighbor);
                if (dependentVerticesFromNeighbor == null) {
                    dependentVerticesFromNeighbor = new HashSet<>();
                    dependentVerticesFromNeighbor.add(currentVertex);
                    blockedVerticesMap.put(neighbor, dependentVerticesFromNeighbor);
                } else {
                    dependentVerticesFromNeighbor.add(currentVertex);
                }
            }
        }

        stack.pop();

        if (!stackOfEdges.isEmpty()) {
            stackOfEdges.pop();
        }

        return foundCycle;
    }

    private void unblock(int vertex) {
        blockedVerticesSet.delete(vertex);

        HashSet<Integer> dependentVertices = blockedVerticesMap.get(vertex);
        if (dependentVertices != null) {

            for(int dependentVertex : dependentVertices.keys()) {
                if (blockedVerticesSet.contains(dependentVertex)) {
                    unblock(dependentVertex);
                }
            }

            blockedVerticesMap.delete(vertex);
        }
    }

    private EdgeWeightedDigraphInterface createSubGraphFromSCC(EdgeWeightedDigraphInterface edgeWeightedDigraph,
                                                               List<Integer> stronglyConnectedComponent) {
        HashSet<Integer> verticesInSCC = new HashSet<>();
        for(int vertex : stronglyConnectedComponent) {
            verticesInSCC.add(vertex);
        }

        EdgeWeightedDigraphInterface subGraph = new EdgeWeightedDigraph(edgeWeightedDigraph.vertices());

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();

                if (!verticesInSCC.contains(vertex) || !verticesInSCC.contains(neighbor)) {
                    continue;
                }

                subGraph.addEdge(new DirectedEdge(vertex, edge.to(), edge.weight()));
            }
        }

        return subGraph;
    }

    // Creates a subgraph with vertexToRemove removed
    private EdgeWeightedDigraphInterface createSubGraphByRemovingVertex(EdgeWeightedDigraphInterface edgeWeightedDigraph,
                                                                        int vertexToRemove) {
        EdgeWeightedDigraphInterface subGraph = new EdgeWeightedDigraph(edgeWeightedDigraph.vertices());

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();

                if (vertex == vertexToRemove || neighbor == vertexToRemove) {
                    continue;
                }

                subGraph.addEdge(new DirectedEdge(vertex, edge.to(), edge.weight()));
            }
        }

        return subGraph;
    }

    public List<List<Integer>> getAllCyclesByVertices() {
        return allCyclesByVertices;
    }

    @SuppressWarnings("unchecked")
    public List<List<Integer>> getAllCyclesByVerticesInOrder() {
        List<List<Integer>>[] cyclesByInitialVertex = (List<List<Integer>>[]) new ArrayList[verticesCount];

        for(int cycles = 0; cycles < cyclesByInitialVertex.length; cycles++) {
            cyclesByInitialVertex[cycles] = new ArrayList<>();
        }

        for(List<Integer> cycle : allCyclesByVertices) {
            int initialVertex = cycle.get(0);
            cyclesByInitialVertex[initialVertex].add(cycle);
        }

        List<List<Integer>> allCyclesInOrder = new ArrayList<>();

        for(List<List<Integer>> cycles : cyclesByInitialVertex) {
            allCyclesInOrder.addAll(cycles);
        }

        return allCyclesInOrder;
    }

    public List<List<DirectedEdge>> getAllCyclesByEdges() {
        return allCyclesByEdges;
    }

    public static void main(String[] args) {
        JohnsonAllCycles johnsonAllCycles = new JohnsonAllCycles();

        EdgeWeightedDigraphInterface edgeWeightedDigraph = new EdgeWeightedDigraph(9);
        edgeWeightedDigraph.addEdge(new DirectedEdge(0, 1, 2));
        edgeWeightedDigraph.addEdge(new DirectedEdge(0, 7, -1));
        edgeWeightedDigraph.addEdge(new DirectedEdge(0, 4, 1));
        edgeWeightedDigraph.addEdge(new DirectedEdge(1, 8, 4));
        edgeWeightedDigraph.addEdge(new DirectedEdge(1, 6, 2));
        edgeWeightedDigraph.addEdge(new DirectedEdge(1, 2, 1));
        edgeWeightedDigraph.addEdge(new DirectedEdge(2, 0, -3));
        edgeWeightedDigraph.addEdge(new DirectedEdge(2, 1, 6));
        edgeWeightedDigraph.addEdge(new DirectedEdge(2, 5, 2));
        edgeWeightedDigraph.addEdge(new DirectedEdge(2, 3, 1));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 3, -2));
        edgeWeightedDigraph.addEdge(new DirectedEdge(3, 4, 3));
        edgeWeightedDigraph.addEdge(new DirectedEdge(4, 1, -2));
        edgeWeightedDigraph.addEdge(new DirectedEdge(7, 8, 4));
        edgeWeightedDigraph.addEdge(new DirectedEdge(8, 7, 4));

        johnsonAllCycles.findAllCycles(edgeWeightedDigraph);

        List<List<Integer>> allCycles = johnsonAllCycles.getAllCyclesByVerticesInOrder();

        StdOut.println("All cycles");

        allCycles.forEach(cycle -> {
            StringJoiner joiner = new StringJoiner("->");

            cycle.forEach(vertex -> joiner.add(String.valueOf(vertex)));
            StdOut.println(joiner);
        });

        StdOut.println("\nExpected:");
        StdOut.println("0->1->2->0\n" +
                "0->4->1->2->0\n" +
                "1->2->3->4->1\n" +
                "1->2->5->3->4->1\n" +
                "1->2->1\n" +
                "7->8->7");

        List<List<DirectedEdge>> allCyclesByEdges = johnsonAllCycles.getAllCyclesByEdges();

        StdOut.println("\nAll cycles (and edge weights)");

        allCyclesByEdges.forEach(cycle -> {
            StringJoiner joiner = new StringJoiner(" ");

            cycle.forEach(edge -> joiner.add(String.valueOf(edge)));
            StdOut.println(joiner);
        });

        StdOut.println("\nExpected:");
        StdOut.println("7->8 4.00 8->7 4.00\n" +
                "0->1 2.00 1->2 1.00 2->0 -3.00\n" +
                "0->4 1.00 4->1 -2.00 1->2 1.00 2->0 -3.00\n" +
                "1->2 1.00 2->3 1.00 3->4 3.00 4->1 -2.00\n" +
                "1->2 1.00 2->5 2.00 5->3 -2.00 3->4 3.00 4->1 -2.00\n" +
                "1->2 1.00 2->1 6.00");
    }
}
