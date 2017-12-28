package chapter4.section4;

/**
 * Created by rene on 27/12/17.
 */
public interface EdgeWeightedDigraphInterface {

    int vertices();
    int edgesCount();
    void addEdge(DirectedEdge edge);
    Iterable<DirectedEdge> adjacent(int vertex);
    Iterable<DirectedEdge> edges();

}
