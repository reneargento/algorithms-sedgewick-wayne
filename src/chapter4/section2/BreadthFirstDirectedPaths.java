package chapter4.section2;

import chapter1.section3.Queue;
import chapter1.section3.Stack;

/**
 * Created by Rene Argento on 28/10/17.
 */
public class BreadthFirstDirectedPaths {

    private boolean[] visited;
    private int[] edgeTo;
    private final int source;

    private int[] distTo;

    public BreadthFirstDirectedPaths(DigraphInterface digraph, int source) {
        visited = new boolean[digraph.vertices()];
        edgeTo = new int[digraph.vertices()];
        this.source = source;

        distTo = new int[digraph.vertices()];

        distTo[source] = 0;
        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            if (vertex == source) {
                continue;
            }
            distTo[vertex] = Integer.MAX_VALUE;
        }

        bfs(digraph, source);
    }

    private void bfs(DigraphInterface digraph, int sourceVertex) {
        Queue<Integer> queue = new Queue<>();
        visited[sourceVertex] = true;

        queue.enqueue(sourceVertex);

        while (!queue.isEmpty()) {
            int currentVertex = queue.dequeue();

            for(int neighbor : digraph.adjacent(currentVertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;

                    edgeTo[neighbor] = currentVertex;
                    distTo[neighbor] = distTo[currentVertex] + 1;

                    queue.enqueue(neighbor);
                }
            }
        }
    }

    //O(1)
    public int distTo(int vertex) {
        return distTo[vertex];
    }

    //O(1)
    public int edgeTo(int vertex) {
        return edgeTo[vertex];
    }

    public boolean hasPathTo(int vertex) {
        return visited[vertex];
    }

    public Iterable<Integer> pathTo(int vertex) {
        if (!hasPathTo(vertex)) {
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
