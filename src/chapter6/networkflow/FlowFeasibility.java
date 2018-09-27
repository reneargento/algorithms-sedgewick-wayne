package chapter6.networkflow;

/**
 * Created by Rene Argento on 25/09/18.
 */
public class FlowFeasibility {

    private static final double FLOATING_POINT_EPSILON = 1E-11;

    private boolean isFeasible(FlowNetwork flowNetwork, int sourceVertex, int targetVertex) {
        // Check that flow on each edge is nonnegative and not greater than capacity.
        for (int vertex = 0; vertex < flowNetwork.vertices(); vertex++) {
            for (FlowEdge flowEdge : flowNetwork.adjacent(vertex)) {
                if (flowEdge.flow() < -FLOATING_POINT_EPSILON
                        || flowEdge.flow() > flowEdge.capacity() + FLOATING_POINT_EPSILON) {
                    return false;
                }
            }
        }

        // Check local equilibrium at each vertex.
        for (int vertex = 0; vertex < flowNetwork.vertices(); vertex++) {
            if (vertex != sourceVertex && vertex != targetVertex && !localEquilibrium(flowNetwork, vertex)) {
                return false;
            }
        }

        return true;
    }

    private boolean localEquilibrium(FlowNetwork flowNetwork, int vertex) {
        double netflow = 0;

        for (FlowEdge flowEdge : flowNetwork.adjacent(vertex)) {
            if (vertex == flowEdge.from()) {
                netflow -= flowEdge.flow();
            } else {
                netflow += flowEdge.flow();
            }
        }

        return Math.abs(netflow) < FLOATING_POINT_EPSILON;
    }

}
