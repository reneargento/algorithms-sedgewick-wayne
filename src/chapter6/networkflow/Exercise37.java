package chapter6.networkflow;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 27/09/18.
 */
public class Exercise37 {

    // Runtime complexity: O(V + E)
    public double getMaxFlow(FlowNetwork flowNetwork, int source, int target) {
        double maxflow = 0;
        // No need for a visited[] array because intermediate vertices are not directly connected

        for (FlowEdge edge : flowNetwork.adjacent(source)) {
            if (source == edge.from()) {
                maxflow += depthFirstSearch(flowNetwork, edge.to(), target, edge.capacity());
            }
        }

        return maxflow;
    }

    private double depthFirstSearch(FlowNetwork flowNetwork, int vertex, int target, double currentMinCapacity) {
        if (vertex == target) {
            return currentMinCapacity;
        }

        for (FlowEdge edge : flowNetwork.adjacent(vertex)) {
            if (vertex == edge.from()) {
                currentMinCapacity = Math.min(currentMinCapacity, edge.capacity());
                return depthFirstSearch(flowNetwork, edge.to(), target, currentMinCapacity);
            }
        }

        return currentMinCapacity;
    }

    public static void main(String[] args) {
        FlowNetwork flowNetwork = new FlowNetwork(7);

        // Path 1 from source to target
        flowNetwork.addEdge(new FlowEdge(0, 1, 5));
        flowNetwork.addEdge(new FlowEdge(1, 4, 4));
        flowNetwork.addEdge(new FlowEdge(4, 6, 3));

        // Path 2 from source to target
        flowNetwork.addEdge(new FlowEdge(0, 2, 8));
        flowNetwork.addEdge(new FlowEdge(2, 5, 10));
        flowNetwork.addEdge(new FlowEdge(5, 6, 7));

        // Path 3 from source to target
        flowNetwork.addEdge(new FlowEdge(0, 3, 2));
        flowNetwork.addEdge(new FlowEdge(3, 6, 2));

        int source = 0;
        int target = 6;
        double maxflow = new Exercise37().getMaxFlow(flowNetwork, source, target);
        StdOut.println("Max flow: " + maxflow);
        StdOut.println("Expected: 12.0");
    }

}
