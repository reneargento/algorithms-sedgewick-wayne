package chapter4.section4;

/**
 * Created by Rene Argento on 27/11/17.
 */
public class DirectedEdge implements Comparable<DirectedEdge> {

    private final int vertex1;
    private final int vertex2;
    private final double weight;

    public DirectedEdge(int vertex1, int vertex2, double weight) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    public int from() {
        return vertex1;
    }

    public int to() {
        return vertex2;
    }

    public int compareTo(DirectedEdge that) {
        if (this.weight < that.weight) {
            return -1;
        } else if (this.weight > that.weight) {
            return 1;
        } else {
            return 0;
        }
    }

    public String toString() {
        return String.format("%d->%d %.2f", vertex1, vertex2, weight);
    }
}
