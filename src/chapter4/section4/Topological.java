package chapter4.section4;

/**
 * Created by rene on 27/11/17.
 */
public class Topological {

    private Iterable<Integer> topologicalOrder;

    public Topological(EdgeWeightedDigraph edgeWeightedDigraph) {
        DirectedCycle cycleFinder = new DirectedCycle(edgeWeightedDigraph);

        if(!cycleFinder.hasCycle()) {
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
