package chapter6.networkflow;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 25/09/18.
 */
public class FordFulkerson {

    private boolean[] visited;  // Is s -> v path in residual graph?
    private FlowEdge[] edgeTo;  // Last edge on shortest s -> v path
    private double maxFlowValue;

    public FordFulkerson(FlowNetwork flowNetwork, int source, int target) {
        // Find max flow in flowNetwork from source to target
        while (hasAugmentingPath(flowNetwork, source, target)) {
            // Compute bottleneck capacity
            double bottleneck = Double.POSITIVE_INFINITY;

            for (int vertex = target; vertex != source; vertex = edgeTo[vertex].other(vertex)) {
                bottleneck = Math.min(bottleneck, edgeTo[vertex].residualCapacityTo(vertex));
            }

            // Augment flow
            for (int vertex = target; vertex != source; vertex = edgeTo[vertex].other(vertex)) {
                edgeTo[vertex].addResidualFlowTo(vertex, bottleneck);
            }

            maxFlowValue += bottleneck;
        }
    }

    private boolean hasAugmentingPath(FlowNetwork flowNetwork, int source, int target) {
        visited = new boolean[flowNetwork.vertices()];
        edgeTo = new FlowEdge[flowNetwork.vertices()];

        Queue<Integer> queue = new Queue<>();

        visited[source] = true;
        queue.enqueue(source);

        while (!queue.isEmpty() && !visited[target]) {
            int vertex = queue.dequeue();

            for (FlowEdge edge : flowNetwork.adjacent(vertex)) {
                int neighbor = edge.other(vertex);

                if (edge.residualCapacityTo(neighbor) > 0 && !visited[neighbor]) {
                    edgeTo[neighbor] = edge;
                    visited[neighbor] = true;
                    queue.enqueue(neighbor);
                }
            }
        }

        return visited[target];
    }

    public double maxFlowValue() {
        return maxFlowValue;
    }

    // Returns true if the vertex is on the source side of the min cut
    public boolean inCut(int vertex) {
        return visited[vertex];
    }

    public static void main(String[] args) {
        FlowNetwork flowNetwork = new FlowNetwork(new In(args[0]));
        int source = 0;
        int target = flowNetwork.vertices() - 1;

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, source, target);

        StdOut.println("Max flow from " + source + " to " + target);
        for (int vertex = 0; vertex < flowNetwork.vertices(); vertex++) {
            for (FlowEdge edge : flowNetwork.adjacent(vertex)) {
                if (vertex == edge.from() && edge.flow() > 0) {
                    StdOut.println("    " + edge);
                }
            }
        }
        StdOut.println("Max flow value = " + fordFulkerson.maxFlowValue());
    }

}
