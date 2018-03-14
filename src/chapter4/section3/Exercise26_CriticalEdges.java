package chapter4.section3;

import chapter1.section3.Queue;
import chapter1.section5.UnionFind;
import chapter2.section4.PriorityQueueResize;
import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 09/11/17.
 */
// Based on the nice answer given by j_random_hacker here:
    // https://stackoverflow.com/questions/15720155/find-all-critical-edges-of-an-mst
public class Exercise26_CriticalEdges {

    /**
     * An edge e is critical if and only if it is a bridge in the subgraph containing all edges with weights
     * less than or equal to the weight of edge e.
     *
     * Proof:
     * 1st part: If an edge e is critical then it is a bridge in the subgraph containing all edges with weights
     * less than or equal to the weight of edge e.
     * Consider by contradiction that edge e is not a bridge in such subgraph. If it is not a bridge, then there is another
     * edge f that connects the same components as e in the subgraph and it has weight less than or equal to e.
     * In this case, edge e could be replaced by edge f in an MST and the MST weight would not increase.
     * However, since e is critical and cannot be replaced by an edge with weight less than or equal to it,
     * it must be a bridge in the subgraph.
     *
     * 2nd part: If an edge e is a bridge in the subgraph containing all edges with weights less than or equal to its
     * weight then e is critical.
     * Consider by contradiction that e is not critical. If e is not critical, then there must be another edge that
     * could replace it in an MST and would not cause the MST weight to increase.
     * However, if this edge existed, it would be part of the subgraph containing all edges with weights
     * less than or equal to the weight of edge e. It would also connect both components C1 and C2 that are connected
     * by edge e. However, e is a bridge and its removal would split components C1 and C2. So no such edge exists.
     * Therefore, edge e is critical.
     *
     */
    //O(E lg E)
    public Queue<Edge> findCriticalEdges(EdgeWeightedGraph edgeWeightedGraph) {
        Queue<Edge> criticalEdges = new Queue<>();

        // Modified Kruskal's algorithm
        Queue<Edge> minimumSpanningTree = new Queue<>();
        PriorityQueueResize<Edge> priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

        for(Edge edge : edgeWeightedGraph.edges()) {
            priorityQueue.insert(edge);
        }

        UnionFind unionFind = new UnionFind(edgeWeightedGraph.vertices());

        // Subgraph with components
        EdgeWeightedGraphWithDelete componentsSubGraph = new EdgeWeightedGraphWithDelete(unionFind.count());

        while (!priorityQueue.isEmpty() && minimumSpanningTree.size() < edgeWeightedGraph.vertices() - 1) {
            Edge edge = priorityQueue.deleteTop();

            int vertex1 = edge.either();
            int vertex2 = edge.other(vertex1);

            // Ineligible edges are never critical edges
            if (unionFind.connected(vertex1, vertex2)) {
                continue;
            }

            // Get next equal-weight edge block
            double currentWeight = edge.weight();

            HashSet<Edge> equalWeightEdges = new HashSet<>();
            equalWeightEdges.add(edge);

            while (!priorityQueue.isEmpty() && priorityQueue.peek().weight() == currentWeight) {
                equalWeightEdges.add(priorityQueue.deleteTop());
            }

            if (equalWeightEdges.size() == 1) {
                criticalEdges.enqueue(edge); // There is no cycle, so this is a critical edge

                unionFind.union(vertex1, vertex2);
                minimumSpanningTree.enqueue(edge);

                continue;
            }

            List<Edge> edgesToAddToComponentsSubGraph = new ArrayList<>();

            // Map to make the mapping between edges in the components subgraph and the original graph
            int averageMapListSize = Math.max(2, equalWeightEdges.size() / 20);
            SeparateChainingHashTable<Edge, Edge> subGraphToGraphEdgeMap =
                    new SeparateChainingHashTable<>(equalWeightEdges.size(), averageMapListSize);
            HashSet<Integer> verticesInSubGraph = new HashSet<>();

            // Generate subgraph with the current components
            for(Edge edgeInCurrentBlock : equalWeightEdges.keys()) {
                vertex1 = edgeInCurrentBlock.either();
                vertex2 = edgeInCurrentBlock.other(vertex1);

                int component1 = unionFind.find(vertex1);
                int component2 = unionFind.find(vertex2);

                Edge subGraphEdge = new Edge(component1, component2, currentWeight);
                edgesToAddToComponentsSubGraph.add(subGraphEdge);

                subGraphToGraphEdgeMap.put(subGraphEdge, edgeInCurrentBlock);
                verticesInSubGraph.add(component1);
                verticesInSubGraph.add(component2);
            }

            for(Edge edgeToAddToComponentSubGraph : edgesToAddToComponentsSubGraph) {
                componentsSubGraph.addEdge(edgeToAddToComponentSubGraph);
            }

            // Run DFS to check if there is a cycle. Any edges in the cycle are non-critical.
            // Every edge in the original graph will be visited by a DFS at most once.
            HashSet<Edge> nonCriticalEdges = new HashSet<>();

            // Use a different constructor for EdgeWeightedCycle to avoid O(E * V) runtime
            EdgeWeightedCycle edgeWeightedCycle = new EdgeWeightedCycle(componentsSubGraph, verticesInSubGraph);
            if (edgeWeightedCycle.hasCycle()) {
                for(Edge edgeInCycle : edgeWeightedCycle.cycle()) {
                    Edge edgeInGraph = subGraphToGraphEdgeMap.get(edgeInCycle);

                    nonCriticalEdges.add(edgeInGraph);
                }
            }

            // Clear components subgraph edges
            for(Edge edgeToAddToComponentSubGraph : edgesToAddToComponentsSubGraph) {
                componentsSubGraph.deleteEdge(edgeToAddToComponentSubGraph);
            }

            // Add all critical edges to the queue
            // Add all edges that belong to an MST to the MST
            for(Edge edgeInCurrentBlock : equalWeightEdges.keys()) {

                if (!nonCriticalEdges.contains(edgeInCurrentBlock)) {
                    criticalEdges.enqueue(edgeInCurrentBlock);
                }

                vertex1 = edgeInCurrentBlock.either();
                vertex2 = edgeInCurrentBlock.other(vertex1);

                if (!unionFind.connected(vertex1, vertex2)) {
                    unionFind.union(vertex1, vertex2);
                    minimumSpanningTree.enqueue(edge); // Add edge to the minimum spanning tree
                }
            }
        }

        return criticalEdges;
    }

    public static void main(String[] args) {
        Exercise26_CriticalEdges criticalEdges = new Exercise26_CriticalEdges();

        EdgeWeightedGraph edgeWeightedGraph1 = new EdgeWeightedGraph(4);
        edgeWeightedGraph1.addEdge(new Edge(0, 1, 1)); // Critical edge
        edgeWeightedGraph1.addEdge(new Edge(1, 2, 3));
        edgeWeightedGraph1.addEdge(new Edge(2, 3, 2)); // Critical edge
        edgeWeightedGraph1.addEdge(new Edge(3, 0, 3));

        StdOut.println("Critical edges 1:");

        Queue<Edge> criticalEdges1 = criticalEdges.findCriticalEdges(edgeWeightedGraph1);
        for(Edge edge : criticalEdges1) {
            StdOut.println(edge);
        }

        StdOut.println("Expected:\n" +
                "0-1 1.00000\n" +
                "2-3 2.00000\n");

        EdgeWeightedGraph edgeWeightedGraph2 = new EdgeWeightedGraph(4);
        edgeWeightedGraph2.addEdge(new Edge(0, 1, 5));
        edgeWeightedGraph2.addEdge(new Edge(1, 2, 5));
        edgeWeightedGraph2.addEdge(new Edge(0, 2, 5));
        edgeWeightedGraph2.addEdge(new Edge(2, 3, 3)); // Critical edge

        StdOut.println("Critical edges 2:");

        Queue<Edge> criticalEdges2 = criticalEdges.findCriticalEdges(edgeWeightedGraph2);
        for(Edge edge : criticalEdges2) {
            StdOut.println(edge);
        }

        StdOut.println("Expected:\n" +
                "2-3 3.00000");
    }

}
