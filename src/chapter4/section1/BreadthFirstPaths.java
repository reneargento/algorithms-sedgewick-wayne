package chapter4.section1;

import chapter1.section3.Queue;
import chapter1.section3.Stack;

/**
 * Created by rene on 15/09/17.
 */
public class BreadthFirstPaths {

    private boolean[] visited;
    private int[] edgeTo;
    private final int source;

    public BreadthFirstPaths(Graph graph, int source) {
        visited = new boolean[graph.vertices()];
        edgeTo = new int[graph.vertices()];
        this.source = source;
        bfs(graph, source);
    }

    private void bfs(Graph graph, int sourceVertex) {
        Queue<Integer> queue = new Queue<>();
        visited[sourceVertex] = true;

        queue.enqueue(sourceVertex);

        while (!queue.isEmpty()) {
            int currentVertex = queue.dequeue();

            for(int neighbor : graph.adjacent(currentVertex)) {
                if(!visited[neighbor]) {
                    edgeTo[neighbor] = currentVertex;
                    visited[neighbor] = true;
                    queue.enqueue(neighbor);
                }
            }
        }
    }

    public boolean hasPathTo(int vertex) {
        return visited[vertex];
    }

    public Iterable<Integer> pathTo(int vertex) {
        if(!hasPathTo(vertex)) {
            return null;
        }

        Stack<Integer> path = new Stack<>();

        for(int currentVertex = vertex; currentVertex != source; currentVertex = edgeTo[currentVertex]) {
            path.push(currentVertex);
        }

        path.push(source);
        return path;
    }

}
