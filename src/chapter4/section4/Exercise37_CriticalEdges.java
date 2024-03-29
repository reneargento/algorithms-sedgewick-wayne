package chapter4.section4;

import chapter1.section3.Queue;
import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 18/12/17.
 */
// Based on http://bababadalgharaghtakamminarronnkonnbro.blogspot.com.br/2012/06/interviewstreet-going-office.html
// Thanks to qiuhaha (https://github.com/qiuhaha) for reporting a bug on the getIslands() method.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/303
public class Exercise37_CriticalEdges {

    // O(E lg V)
    public DirectedEdge getCriticalEdge(EdgeWeightedDigraph edgeWeightedDigraph, int source, int target) {
        // 1- Compute reverse digraph
        // It will be used to compute the shortest paths using the target vertex as a source.
        EdgeWeightedDigraph reverseDigraph = new EdgeWeightedDigraph(edgeWeightedDigraph.vertices());

        for (int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            for (DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                reverseDigraph.addEdge(new DirectedEdge(edge.to(), edge.from(), edge.weight()));
            }
        }

        // 2- Get shortest paths from source
        DijkstraSP dijkstraSP = new DijkstraSP(edgeWeightedDigraph, source);
        if (!dijkstraSP.hasPathTo(target)) {
            return null;
        }

        // 3- Get shortest paths from the target
        DijkstraSP dijkstraSPFromTarget = new DijkstraSP(reverseDigraph, target);

        // 4- Get the islands in the graph.
        // The i-th island is the set of all vertices v, such that there exists a shortest path
        // from source to v using no more than i shortest-path vertices.
        int[] islands = getIslands(reverseDigraph, dijkstraSP, target);

        // 5- Compute bypass path lengths
        SegmentTree bypassPathLengths = computeBypassPathLengths(edgeWeightedDigraph, islands, dijkstraSP,
                dijkstraSPFromTarget, target);

        // 6- Return a critical edge, which is an edge that has the highest bypass length
        return getCriticalEdge(bypassPathLengths, dijkstraSP, target);
    }

    private int[] getIslands(EdgeWeightedDigraph reverseDigraph, DijkstraSP dijkstraSP, int target) {
        int[] islands = new int[reverseDigraph.vertices()];
        for (int vertex = 0; vertex < reverseDigraph.vertices(); vertex++) {
            islands[vertex] = -1;
        }

        boolean[] isInShortestPath = new boolean[reverseDigraph.vertices()];
        int islandId = 0;
        for (DirectedEdge edge : dijkstraSP.pathTo(target)) {
            if (islands[edge.from()] == -1) {
                islands[edge.from()] = islandId++;
            }
            islands[edge.to()] = islandId++;
            isInShortestPath[edge.from()] = true;
            isInShortestPath[edge.to()] = true;
        }

        // Do a breadth-first walk to find the island number of vertices that are not on the shortest path
        // from source to target.
        // These vertices are on a path from source to target that is not a shortest path.
        boolean[] visited = new boolean[reverseDigraph.vertices()];
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(target);
        visited[target] = true;

        while (!queue.isEmpty()) {
            int currentVertex = queue.dequeue();

            for (DirectedEdge edge : reverseDigraph.adjacent(currentVertex)) {
                int neighbor = edge.to();

                if (!visited[neighbor]) {
                    visited[neighbor] = true;

                    if (!isInShortestPath[neighbor] && islands[currentVertex] > islands[neighbor]) {
                        islands[neighbor] = islands[currentVertex];
                    }
                    queue.enqueue(edge.to());
                }
            }
        }
        return islands;
    }

    private SegmentTree computeBypassPathLengths(EdgeWeightedDigraph edgeWeightedDigraph, int[] islands,
                                                 DijkstraSP dijkstraSP, DijkstraSP dijkstraSPFromTarget, int target) {
        // bypassPathLengths[i] denotes the length of the shortest path that bypasses ei, where ei is the ith edge in
        // the shortest path from source to target.
        double[] bypassPathLengths = new double[edgeWeightedDigraph.vertices()];

        for (int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            bypassPathLengths[vertex] = Double.POSITIVE_INFINITY;
        }

        HashSet<DirectedEdge> edgesInShortestPath = new HashSet<>();
        for (DirectedEdge edge : dijkstraSP.pathTo(target)) {
            edgesInShortestPath.add(edge);
        }

        SegmentTree segmentTree = new SegmentTree(bypassPathLengths);

        for (DirectedEdge edge : edgeWeightedDigraph.edges()) {
            if (!edgesInShortestPath.contains(edge)) {
                int island1 = islands[edge.from()];
                int island2 = islands[edge.to()];

                if (island1 < island2
                        && island1 != -1 && island2 != -1) {
                    double shortestPathLength = dijkstraSP.distTo(edge.from()) + edge.weight()
                            + dijkstraSPFromTarget.distTo(edge.to());

                    double currentShortestPathLength = segmentTree.rangeMinQuery(island1, island2 - 1);

                    if (shortestPathLength < currentShortestPathLength) {
                        segmentTree.update(island1, island2 - 1, shortestPathLength);
                    }
                }
            }
        }
        return segmentTree;
    }

    private DirectedEdge getCriticalEdge(SegmentTree bypassPathLengths, DijkstraSP dijkstraSP, int target) {
        // key = index of edge in the shortest path from source to target
        // value = edge in the shortest path from source to target
        SeparateChainingHashTable<Integer, DirectedEdge> edgesInShortestPath = new SeparateChainingHashTable<>();
        int edgeIndex = 0;

        for (DirectedEdge edge : dijkstraSP.pathTo(target)) {
            edgesInShortestPath.put(edgeIndex++, edge);
        }

        int criticalEdgeId = 0;
        double highestBypassPathLength = Double.NEGATIVE_INFINITY;

        for (int edgeId = 0; edgeId < edgeIndex; edgeId++) {
            double lengthBypassingEdge = bypassPathLengths.rangeSumQuery(edgeId, edgeId);
            if (lengthBypassingEdge > highestBypassPathLength) {
                highestBypassPathLength = lengthBypassingEdge;
                criticalEdgeId = edgeId;
            }
        }
        return edgesInShortestPath.get(criticalEdgeId);
    }

    public static void main(String[] args) {
        Exercise37_CriticalEdges criticalEdges = new Exercise37_CriticalEdges();

        EdgeWeightedDigraph edgeWeightedDigraph1 = new EdgeWeightedDigraph(8);
        // Shortest path
        edgeWeightedDigraph1.addEdge(new DirectedEdge(0, 1, 1));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(1, 2, 2));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(2, 3, 1));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(3, 4, 1));
        // Path to take when edge 0->1 is removed
        edgeWeightedDigraph1.addEdge(new DirectedEdge(0, 1, 1));
        // Path to take when edge 3->4 is removed
        edgeWeightedDigraph1.addEdge(new DirectedEdge(3, 5, 2));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(5, 4, 1));
        // Possible path to take when edge 2->3 is removed. Won't be taken because there is a shorter path from 0 to 4
        edgeWeightedDigraph1.addEdge(new DirectedEdge(2, 6, 3));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(6, 4, 3));
        // Path to take when edge 1->2 is removed
        edgeWeightedDigraph1.addEdge(new DirectedEdge(1, 7, 3));
        edgeWeightedDigraph1.addEdge(new DirectedEdge(7, 4, 4));

        int source1 = 0;
        int target1 = 4;

        StdOut.print("Critical edge 1: ");
        DirectedEdge criticalEdge1 = criticalEdges.getCriticalEdge(edgeWeightedDigraph1, source1, target1);
        printResult(criticalEdge1, source1, target1);
        StdOut.println("Expected: 1->2 2.00");

        int source2 = 7;
        int target2 = 3;

        StdOut.print("\nCritical edge 2: ");
        DirectedEdge criticalEdge2 = criticalEdges.getCriticalEdge(edgeWeightedDigraph1, source2, target2);
        printResult(criticalEdge2, source2, target2);
        StdOut.println("Expected: There is no path from 7 to 3");

        EdgeWeightedDigraph edgeWeightedDigraph2 = new EdgeWeightedDigraph(9);
        // Shortest path
        edgeWeightedDigraph2.addEdge(new DirectedEdge(0, 1, 1));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(1, 2, 1));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(2, 3, 1));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(3, 4, 1));
        // Path to take when edge 2->3 is removed
        edgeWeightedDigraph2.addEdge(new DirectedEdge(2, 5, 2));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(5, 4, 2));
        // Path to take when edge 0->1 is removed
        edgeWeightedDigraph2.addEdge(new DirectedEdge(0, 6, 4));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(6, 4, 3));
        // Another path from 0 to 4 when edge 0->1 is removed - but this path is longer and won't be taken
        edgeWeightedDigraph2.addEdge(new DirectedEdge(0, 7, 4));
        edgeWeightedDigraph2.addEdge(new DirectedEdge(7, 4, 5));
        // Adding a cycle just for fun
        edgeWeightedDigraph2.addEdge(new DirectedEdge(2, 1, 2));
        // Random edge
        edgeWeightedDigraph2.addEdge(new DirectedEdge(8, 2, 3));

        int source3 = 0;
        int target3 = 4;

        StdOut.print("\nCritical edge 3: ");
        DirectedEdge criticalEdge3 = criticalEdges.getCriticalEdge(edgeWeightedDigraph2, source3, target3);
        printResult(criticalEdge3, source3, target3);
        StdOut.println("Expected: 0->1 1.00");

        // Digraph with a bridge: 2->3 is a bridge which disconnects source and target vertices when removed
        EdgeWeightedDigraph edgeWeightedDigraph3 = new EdgeWeightedDigraph(9);
        // Shortest path
        edgeWeightedDigraph3.addEdge(new DirectedEdge(0, 1, 1));
        edgeWeightedDigraph3.addEdge(new DirectedEdge(1, 2, 1));
        edgeWeightedDigraph3.addEdge(new DirectedEdge(2, 3, 3));
        edgeWeightedDigraph3.addEdge(new DirectedEdge(3, 4, 1));
        edgeWeightedDigraph3.addEdge(new DirectedEdge(4, 5, 1));
        // Other paths
        edgeWeightedDigraph3.addEdge(new DirectedEdge(0, 1, 2));
        edgeWeightedDigraph3.addEdge(new DirectedEdge(1, 2, 2));
        edgeWeightedDigraph3.addEdge(new DirectedEdge(3, 4, 2));
        edgeWeightedDigraph3.addEdge(new DirectedEdge(4, 5, 2));

        int source4 = 0;
        int target4 = 5;

        StdOut.print("\nCritical edge 4: ");
        DirectedEdge criticalEdge4 = criticalEdges.getCriticalEdge(edgeWeightedDigraph3, source4, target4);
        printResult(criticalEdge4, source4, target4);
        StdOut.println("Expected: 2->3 3.00");

        EdgeWeightedDigraph edgeWeightedDigraph4 = new EdgeWeightedDigraph(9);
        edgeWeightedDigraph4.addEdge(new DirectedEdge(0, 1, 10));
        edgeWeightedDigraph4.addEdge(new DirectedEdge(1, 2, 10));
        edgeWeightedDigraph4.addEdge(new DirectedEdge(2, 3, 10));
        edgeWeightedDigraph4.addEdge(new DirectedEdge(3, 4, 10));
        edgeWeightedDigraph4.addEdge(new DirectedEdge(4, 5, 10));
        // Other path
        edgeWeightedDigraph4.addEdge(new DirectedEdge(3, 7, 1));
        edgeWeightedDigraph4.addEdge(new DirectedEdge(7, 2, 1));
        edgeWeightedDigraph4.addEdge(new DirectedEdge(2, 6, 10));
        edgeWeightedDigraph4.addEdge(new DirectedEdge(6, 8, 1));
        edgeWeightedDigraph4.addEdge(new DirectedEdge(8, 7, 1));
        edgeWeightedDigraph4.addEdge(new DirectedEdge(0, 2, 30));
        edgeWeightedDigraph4.addEdge(new DirectedEdge(3, 5, 30));
        int source5 = 0;
        int target5 = 5;

        StdOut.print("\nCritical edge 5: ");
        DirectedEdge criticalEdge5 = criticalEdges.getCriticalEdge(edgeWeightedDigraph4, source5, target5);
        printResult(criticalEdge5, source5, target5);
        StdOut.println("Expected: 2->3 10.00");
    }

    private static void printResult(DirectedEdge criticalEdge, int source, int target) {
        if (criticalEdge == null) {
            StdOut.println("There is no path from " + source + " to " + target);
        } else {
            StdOut.println(criticalEdge);
        }
    }
}