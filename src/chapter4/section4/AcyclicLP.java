package chapter4.section4;

/**
 * Created by Rene Argento on 28/11/17.
 */
public class AcyclicLP {

    private AcyclicSP acyclicSP;

    public AcyclicLP(EdgeWeightedDigraph edgeWeightedDigraph, int source) {
        EdgeWeightedDigraph negatedEdgesDigraph = new EdgeWeightedDigraph(edgeWeightedDigraph.vertices());

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                DirectedEdge negatedEdge = new DirectedEdge(edge.from(), edge.to(), edge.weight() * -1);
                negatedEdgesDigraph.addEdge(negatedEdge);
            }
        }

        acyclicSP = new AcyclicSP(negatedEdgesDigraph, source);
    }

    public double distTo(int vertex) {
        if (acyclicSP.distTo(vertex) == 0) {
            return 0;
        }

        return acyclicSP.distTo(vertex) * -1;
    }

    public boolean hasPathTo(int vertex) {
        return acyclicSP.hasPathTo(vertex);
    }

    public Iterable<DirectedEdge> pathTo(int vertex) {
        return acyclicSP.pathTo(vertex);
    }

}
