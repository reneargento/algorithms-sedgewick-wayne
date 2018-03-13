package chapter4.section2;

import chapter1.section3.Queue;
import chapter1.section3.Stack;

/**
 * Created by Rene Argento on 17/10/17.
 */
public class DepthFirstOrder {

    private boolean[] visited;

    private Queue<Integer> preOrder;
    private Queue<Integer> postOrder;
    private Stack<Integer> reversePostOrder;

    public DepthFirstOrder(DigraphInterface digraph) {
        preOrder = new Queue<>();
        postOrder = new Queue<>();
        reversePostOrder = new Stack<>();
        visited = new boolean[digraph.vertices()];

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            if (!visited[vertex]) {
                dfs(digraph, vertex);
            }
        }
    }

    private void dfs(DigraphInterface digraph, int vertex) {
        preOrder.enqueue(vertex);

        visited[vertex] = true;

        for(int neighbor : digraph.adjacent(vertex)) {
            if (!visited[neighbor]) {
                dfs(digraph, neighbor);
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
