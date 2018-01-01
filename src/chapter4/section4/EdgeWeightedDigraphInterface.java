package chapter4.section4;

/**
 * Created by Rene Argento on 27/12/17.
 */
public interface EdgeWeightedDigraphInterface {

    int vertices();
    int edgesCount();
    int outdegree(int vertex);
    void addEdge(DirectedEdge edge);
    Iterable<DirectedEdge> adjacent(int vertex);
    Iterable<DirectedEdge> edges();
    EdgeWeightedDigraphInterface reverse();

}
