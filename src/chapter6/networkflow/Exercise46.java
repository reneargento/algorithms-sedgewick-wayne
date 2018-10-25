package chapter6.networkflow;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/10/18.
 */
// Reference:
// https://math.stackexchange.com/questions/2594120/maximal-length-of-an-augmenting-path-in-a-flow-network-bipartite-graph
public class Exercise46 {

    // Construct a bipartite graph with even id vertices in one set and odd id vertices in the other set
    // v0 - v1
    //    /
    // v2 - v3
    //    /
    // v4 - v5
    public FlowNetwork constructMaxLengthBipartiteMatchingProblem(int vertices) {
        FlowNetwork flowNetwork = new FlowNetwork(vertices);

        for(int i = 0; i < vertices - 1; i++) {
            flowNetwork.addEdge(new FlowEdge(i, i + 1, 1));
        }

        return flowNetwork;
    }

    private int countNumberOfEdgesInAugmentingPath(FlowNetwork flowNetwork) {
        int numberOfEdgesInAugmentingPath = 0;

        for (int vertex = 0; vertex < flowNetwork.vertices(); vertex++) {
            for (FlowEdge edge : flowNetwork.adjacent(vertex)) {
                if (vertex == edge.from() && edge.flow() > 0) {
                    numberOfEdgesInAugmentingPath++;
                }
            }
        }

        return numberOfEdgesInAugmentingPath;
    }

    public static void main(String[] args) {
        Exercise46 exercise46 = new Exercise46();
        int vertices = 100;
        int source = 0;
        int target = 99;
        int expectedAugmentingPathLength = vertices - 1;
        FlowNetwork flowNetwork = exercise46.constructMaxLengthBipartiteMatchingProblem(vertices);
        new FordFulkerson(flowNetwork, source, target);

        int augmentingPathLength = exercise46.countNumberOfEdgesInAugmentingPath(flowNetwork);
        StdOut.println("Augmenting path length: " + augmentingPathLength);
        StdOut.println("Expected: " + expectedAugmentingPathLength);
    }

}
