package chapter4.section4;

import chapter1.section3.Queue;
import chapter1.section3.Stack;

/**
 * Created by Rene Argento on 27/11/17.
 */
public class DepthFirstOrder {

    private boolean[] visited;

    private Queue<Integer> preOrder;
    private Queue<Integer> postOrder;
    private Stack<Integer> reversePostOrder;

    public DepthFirstOrder(EdgeWeightedDigraphInterface edgeWeightedDigraph) {
        preOrder = new Queue<>();
        postOrder = new Queue<>();
        reversePostOrder = new Stack<>();
        visited = new boolean[edgeWeightedDigraph.vertices()];

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            if (!visited[vertex]) {
                dfs(edgeWeightedDigraph, vertex);
            }
        }
    }

    private void dfs(EdgeWeightedDigraphInterface edgeWeightedDigraph, int vertex) {
        preOrder.enqueue(vertex);

        visited[vertex] = true;

        for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
            int neighbor = edge.to();

            if (!visited[neighbor]) {
                dfs(edgeWeightedDigraph, neighbor);
            }
        }

        postOrder.enqueue(vertex);
        reversePostOrder.push(vertex);
    }

    public Iterable<Integer> preOrder() {
        return preOrder;
    }

    public Iterable<Integer> postOrder() {
        return postOrder;
    }

    public Iterable<Integer> reversePostOrder() {
        return reversePostOrder;
    }

}
