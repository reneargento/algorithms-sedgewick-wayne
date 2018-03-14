package chapter4.section4;

import chapter1.section3.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 02/12/17.
 */
public class Exercise12 {

    public class EdgeWeightedDirectedCycle {

        private boolean visited[];
        private DirectedEdge[] edgeTo;
        private Stack<DirectedEdge> cycle; // vertices on a cycle (if one exists)
        private boolean[] onStack; // vertices on recursive call stack

        public EdgeWeightedDirectedCycle(EdgeWeightedDigraph edgeWeightedDigraph) {
            onStack = new boolean[edgeWeightedDigraph.vertices()];
            edgeTo = new DirectedEdge[edgeWeightedDigraph.vertices()];
            visited = new boolean[edgeWeightedDigraph.vertices()];

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                if (!visited[vertex]) {
                    dfs(edgeWeightedDigraph, vertex);
                }
            }
        }

        private void dfs(EdgeWeightedDigraph edgeWeightedDigraph, int vertex) {
            onStack[vertex] = true;
            visited[vertex] = true;

            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();

                if (hasCycle()) {
                    return;
                } else if (!visited[neighbor]) {
                    edgeTo[neighbor] = edge;
                    dfs(edgeWeightedDigraph, neighbor);
                } else if (onStack[neighbor]) {
                    cycle = new Stack<>();

                    DirectedEdge edgeInCycle = edge;

                    while (edgeInCycle.from() != neighbor) {
                        cycle.push(edgeInCycle);
                        edgeInCycle = edgeTo[edgeInCycle.from()];
                    }

                    cycle.push(edgeInCycle);
                    return;
                }
            }

            onStack[vertex] = false;
        }

        public boolean hasCycle() {
            return cycle != null;
        }

        public Iterable<DirectedEdge> cycle() {
            return cycle;
        }
    }

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

    public static void main(String[] args) {
        Exercise12 exercise12 = new Exercise12();

        EdgeWeightedDigraph edgeWeightedDigraphWithCycle = new EdgeWeightedDigraph(8);
        edgeWeightedDigraphWithCycle.addEdge(new DirectedEdge(0, 1, 0.35));
        edgeWeightedDigraphWithCycle.addEdge(new DirectedEdge(1, 2, 0.35));
        edgeWeightedDigraphWithCycle.addEdge(new DirectedEdge(2, 3, 0.37));
        edgeWeightedDigraphWithCycle.addEdge(new DirectedEdge(3, 4, 0.28));
        edgeWeightedDigraphWithCycle.addEdge(new DirectedEdge(4, 1, 0.28));
        edgeWeightedDigraphWithCycle.addEdge(new DirectedEdge(6, 7, 0.32));
        edgeWeightedDigraphWithCycle.addEdge(new DirectedEdge(7, 5, 0.38));

        EdgeWeightedDigraph edgeWeightedDAG = new EdgeWeightedDigraph(5);
        edgeWeightedDAG.addEdge(new DirectedEdge(0, 1, 0.35));
        edgeWeightedDAG.addEdge(new DirectedEdge(1, 2, 0.22));
        edgeWeightedDAG.addEdge(new DirectedEdge(3, 4, 0.31));
        edgeWeightedDAG.addEdge(new DirectedEdge(4, 0, 0.29));

        StdOut.println("Cycle:");
        EdgeWeightedDirectedCycle edgeWeightedDirectedCycle =
                exercise12.new EdgeWeightedDirectedCycle(edgeWeightedDigraphWithCycle);
        for(DirectedEdge edge : edgeWeightedDirectedCycle.cycle()) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected: 1->2 2->3 3->4 4->1 ");

        Topological topological = exercise12.new Topological(edgeWeightedDAG);
        StdOut.println("\nTopological order:");
        for(int vertex : topological.order()) {
            StdOut.print(vertex + " ");
        }
        StdOut.println("\nExpected: 3 4 0 1 2");
    }

}
