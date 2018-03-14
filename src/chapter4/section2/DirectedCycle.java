package chapter4.section2;

import chapter1.section3.Stack;

/**
 * Created by Rene Argento on 17/10/17.
 */
public class DirectedCycle {

    private boolean visited[];
    private int[] edgeTo;
    private Stack<Integer> cycle; // vertices on  a cycle (if one exists)
    private boolean[] onStack; // vertices on recursive call stack

    public DirectedCycle(Digraph digraph) {
        onStack = new boolean[digraph.vertices()];
        edgeTo = new int[digraph.vertices()];
        visited = new boolean[digraph.vertices()];

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            if (!visited[vertex]) {
                dfs(digraph, vertex);
            }
        }
    }

    private void dfs(Digraph digraph, int vertex) {
        onStack[vertex] = true;
        visited[vertex] = true;

        for(int neighbor : digraph.adjacent(vertex)) {
            if (hasCycle()) {
                return;
            } else if (!visited[neighbor]) {
                edgeTo[neighbor] = vertex;
                dfs(digraph, neighbor);
            } else if (onStack[neighbor]) {
                cycle = new Stack<>();

                for(int currentVertex = vertex; currentVertex != neighbor; currentVertex = edgeTo[currentVertex]) {
                    cycle.push(currentVertex);
                }

                cycle.push(neighbor);
                cycle.push(vertex);
            }
        }

        onStack[vertex] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<Integer> cycle() {
        return cycle;
    }

}
