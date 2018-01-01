package chapter4.section3;

/**
 * Created by Rene Argento on 07/11/17.
 */
public class Edge implements Comparable<Edge> {

    private final int vertex1;
    private final int vertex2;
    private final double weight;

    public Edge(int vertex1, int vertex2, double weight) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    public int either() {
        return vertex1;
    }

    public int other(int vertex) {
        if (vertex == vertex1) {
            return vertex2;
        } else if (vertex == vertex2) {
            return vertex1;
        } else {
            throw new RuntimeException("Inconsistent edge");
        }
    }

    public int compareTo(Edge that) {
        if (this.weight < that.weight) {
            return -1;
        } else if (this.weight > that.weight) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("%d-%d %.5f", vertex1, vertex2, weight);
    }

}
