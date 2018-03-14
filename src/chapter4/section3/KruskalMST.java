package chapter4.section3;

import chapter1.section3.Queue;
import chapter1.section5.UnionFind;
import chapter2.section4.PriorityQueueResize;

/**
 * Created by Rene Argento on 07/11/17.
 */
// O(E lg E)
public class KruskalMST {

    private Queue<Edge> minimumSpanningTree;
    private double weight;

    public KruskalMST(EdgeWeightedGraphInterface edgeWeightedGraph) {
        minimumSpanningTree = new Queue<>();
        PriorityQueueResize<Edge> priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

        for(Edge edge : edgeWeightedGraph.edges()) {
            priorityQueue.insert(edge);
        }

        UnionFind unionFind = new UnionFind(edgeWeightedGraph.vertices());

        while (!priorityQueue.isEmpty() && minimumSpanningTree.size() < edgeWeightedGraph.vertices() - 1) {
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
