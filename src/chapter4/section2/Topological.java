package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 17/10/17.
 */
public class Topological {

    private Iterable<Integer> topologicalOrder;

    public Topological(Digraph digraph) {
        DirectedCycle cycleFinder = new DirectedCycle(digraph);

        if (!cycleFinder.hasCycle()) {
            DepthFirstOrder depthFirstOrder = new DepthFirstOrder(digraph);
            topologicalOrder = depthFirstOrder.reversePostOrder();
        }
    }

    public Iterable<Integer> order() {
        return topologicalOrder;
    }

    public boolean isDAG() {
        return topologicalOrder != null;
    }

    public static void main(String[] args) {
        String filename = args[0];
        String separator = args[1];

        SymbolDigraph symbolDigraph = new SymbolDigraph(filename, separator);
        Topological topological = new Topological(symbolDigraph.digraph());

        for(int vertex : topological.topologicalOrder) {
            StdOut.println(symbolDigraph.name(vertex));
        }
    }

}
