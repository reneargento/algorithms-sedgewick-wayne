package chapter6.networkflow;

/**
 * Created by Rene Argento on 25/09/18.
 */
public class FlowEdge {

    private final int vertex1;
    private final int vertex2;
    private final double capacity;
    private double flow;

    // Deal with floating-point roundoff errors
    private static final double FLOATING_POINT_EPSILON = 1E-10;

    public FlowEdge(int vertex1, int vertex2, double capacity) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.capacity = capacity;
        this.flow = 0;
    }

    public int from() {
        return vertex1;
    }

    public int to() {
        return vertex2;
    }

    public double capacity() {
        return capacity;
    }

    public double flow() {
        return flow;
    }

    public void setFlow(double flow) {
        this.flow = flow;
    }

    public int other(int vertex) {
        if (vertex == vertex1) {
            return vertex2;
        } else if (vertex == vertex2) {
            return vertex1;
        } else {
            throw new IllegalArgumentException("Invalid endpoint");
        }
    }

    public double residualCapacityTo(int vertex) {
        if (vertex == vertex1) {        // backward edge
            return flow;
        } else if (vertex == vertex2) { // forward edge
            return capacity - flow;
        } else {
            throw new IllegalArgumentException("Invalid endpoint");
        }
    }

    public void addResidualFlowTo(int vertex, double delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("Delta must be nonnegative");
        }

        if (vertex == vertex1) {        // backward edge
            flow -= delta;
        } else if (vertex == vertex2) { // forward edge
            flow += delta;
        } else {
            throw new IllegalArgumentException("Invalid endpoint");
        }

        // Round flow to 0 or capacity if within floating-point precision
        if (Math.abs(flow) <= FLOATING_POINT_EPSILON) {
            flow = 0;
        }
        if (Math.abs(flow - capacity) <= FLOATING_POINT_EPSILON) {
            flow = capacity;
        }

        if (flow < 0) {
            throw new IllegalArgumentException("Resulting flow cannot be negative");
        }
        if (flow > capacity) {
            throw new IllegalArgumentException("Resulting flow cannot be higher than capacity");
        }
    }

    public String toString() {
        return String.format("%d->%d %.2f %.2f", vertex1, vertex2, capacity, flow);
    }

}
