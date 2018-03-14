package chapter4.section4;

/**
 * Created by Rene Argento on 27/11/17.
 */
public class Topological {

    private Iterable<Integer> topologicalOrder;

    public Topological(EdgeWeightedDigraph edgeWeightedDigraph) {
        EdgeWeightedDirectedCycle cycleFinder = new EdgeWeightedDirectedCycle(edgeWeightedDigraph);

        if (!cycleFinder.hasCycle()) {
            DepthFirstOrder depthFirstOrder = new DepthFirstOrder(edgeWeightedDigraph);
            topologicalOrder = depthFirstOrder.reversePostOrder();
        }
    }

    public Iterable<Integer> order() {
        return topologicalOrder;
    }

    public boolean isDAG() {
        return topologicalOrder != null;
    }

}
