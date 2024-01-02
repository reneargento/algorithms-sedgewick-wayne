package chapter4.section3;

import chapter1.section3.Bag;
import chapter1.section3.Queue;
import chapter1.section5.UnionFind;
import chapter2.section4.PriorityQueueResize;
import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 09/11/17.
 */
// Based on the nice answer given by j_random_hacker here:
// https://stackoverflow.com/questions/15720155/find-all-critical-edges-of-an-mst

// Thanks to qiuhaha (https://github.com/qiuhaha) for reporting issues with the previous implementation and
// suggesting the use of GraphX and BridgeFinder classes.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/303
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
    // O(E lg E)
    public Queue<Edge> findCriticalEdges(EdgeWeightedGraph edgeWeightedGraph) {
        Queue<Edge> criticalEdges = new Queue<>();

        // Modified Kruskal's algorithm
        Queue<Edge> minimumSpanningTree = new Queue<>();
        PriorityQueueResize<Edge> priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

        for (Edge edge : edgeWeightedGraph.edges()) {
            priorityQueue.insert(edge);
        }
        UnionFind unionFind = new UnionFind(edgeWeightedGraph.vertices());

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

            Queue<Edge> equalWeightEdgesBlock = new Queue<>();
            equalWeightEdgesBlock.enqueue(edge);

            while (!priorityQueue.isEmpty() && priorityQueue.peek().weight() == currentWeight) {
                equalWeightEdgesBlock.enqueue(priorityQueue.deleteTop());
            }

            if (equalWeightEdgesBlock.size() == 1) {
                criticalEdges.enqueue(edge); // There is no cycle, so this is a critical edge

                unionFind.union(vertex1, vertex2);
                minimumSpanningTree.enqueue(edge);
            } else {
                // Generate subgraph with the current components
                GraphX subgraph = new GraphX();

                for (Edge edgeInCurrentBlock : equalWeightEdgesBlock) {
                    vertex1 = edgeInCurrentBlock.either();
                    vertex2 = edgeInCurrentBlock.other(vertex1);

                    int component1 = unionFind.find(vertex1);
                    int component2 = unionFind.find(vertex2);
                    if (component1 == component2) {
                        continue;
                    }

                    MarkedEdge subGraphEdge = new MarkedEdge(component1, component2, currentWeight);
                    subgraph.addEdge(subGraphEdge);
                }

                BridgeFinder bridgeFinder = new BridgeFinder(subgraph);
                // All bridges are critical edges
                for (Edge bridgeEdge : bridgeFinder.bridges()) {
                    criticalEdges.enqueue(bridgeEdge);
                }

                // Add all edges that belong to an MST to the MST
                for (Edge edgeInCurrentBlock : equalWeightEdgesBlock) {
                    vertex1 = edgeInCurrentBlock.either();
                    vertex2 = edgeInCurrentBlock.other(vertex1);

                    if (!unionFind.connected(vertex1, vertex2)) {
                        unionFind.union(vertex1, vertex2);
                        minimumSpanningTree.enqueue(edge); // Add edge to the minimum spanning tree
                    }
                }
            }
        }
        return criticalEdges;
    }

    private static class MarkedEdge extends Edge {
        boolean marked;

        public MarkedEdge(int vertex1, int vertex2, double weight) {
            super(vertex1, vertex2, weight);
        }
    }

    private static class GraphX {
        private final SeparateChainingHashTable<Integer, Bag<MarkedEdge>> adjacent; // optimal for very sparse graphs

        public GraphX() {
            adjacent = new SeparateChainingHashTable<>();
        }

        public void addEdge(MarkedEdge edge) {
            int vertex1 = edge.either();
            int vertex2 = edge.other(vertex1);
            int[] vertices = new int[] { vertex1, vertex2 };

            for (int vertex : vertices) {
                if (!adjacent.contains(vertex)) {
                    adjacent.put(vertex, new Bag<>());
                }
                adjacent.get(vertex).add(edge);
            }
        }

        public Iterable<MarkedEdge> adjacent(int vertex) {
            return adjacent.get(vertex);
        }

        // Only include vertices with degree > 0
        public Iterable<Integer> vertices() {
            return adjacent.keys();
        }
    }

    private static class BridgeFinder {
        private final Queue<Edge> bridges = new Queue<>();
        private int dfsCount;          // dfs invocation count
        private final SeparateChainingHashTable<Integer, Integer> order;     // order[v] = order in which dfs examines v
        private final SeparateChainingHashTable<Integer, Integer> low;       // low[v] = lowest preorder of any vertex connected to v

        public BridgeFinder(GraphX graph) {
            order = new SeparateChainingHashTable<>();
            low = new SeparateChainingHashTable<>();

            for (Integer vertex : graph.vertices())
                if (order.get(vertex) == null) {
                    dfs(graph, vertex, vertex);
                }
        }

        private void dfs(GraphX graph, int from, int vertex) {
            order.put(vertex, ++dfsCount);
            low.put(vertex, dfsCount);

            for (MarkedEdge edge : graph.adjacent(vertex)) {
                if (edge.marked) {
                    continue;
                }
                edge.marked = true;

                int otherVertex = edge.other(vertex);
                if (order.get(otherVertex) == null) { // new edges, otherVertex is a descendant of vertex
                    dfs(graph, vertex, otherVertex);
                    low.put(vertex, Math.min(low.get(vertex), low.get(otherVertex)));

                    if (low.get(otherVertex).equals(order.get(otherVertex))) {
                        bridges.enqueue(edge);
                    }
                } else { // back edges, otherVertex is an ancestor of vertex
                    low.put(vertex, Math.min(low.get(vertex), order.get(otherVertex)));
                }
            }
        }

        public Iterable<Edge> bridges() {
            return bridges;
        }
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
        for (Edge edge : criticalEdges1) {
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
        for (Edge edge : criticalEdges2) {
            StdOut.println(edge);
        }

        StdOut.println("Expected:\n" +
                "2-3 3.00000");
    }
}
