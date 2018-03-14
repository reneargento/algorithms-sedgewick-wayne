package chapter4.section3;

import chapter1.section3.Stack;
import chapter3.section5.HashSet;

/**
 * Created by Rene Argento on 09/11/17.
 */
public class EdgeWeightedCycle {

    private boolean visited[];
    private Edge[] edgeTo;
    private Stack<Edge> cycle; // edges on  a cycle (if one exists)
    private boolean[] onStack; // vertices on recursive call stack
    private HashSet<Edge> visitedEdges;
    private boolean cycleFound;

    public EdgeWeightedCycle(EdgeWeightedGraphInterface edgeWeightedGraph) {
        initVariables(edgeWeightedGraph);

        for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
            if (cycleFound) {
                break;
            }

            if (!visited[vertex]) {
                dfs(edgeWeightedGraph, vertex);
            }
        }
    }

    // Constructor that only searches for cycles using the vertices passed as parameter as sources.
    // Useful for subgraph search or when many searches have to be done and not all vertices need to analyzed.
    public EdgeWeightedCycle(EdgeWeightedGraphInterface edgeWeightedGraph, HashSet<Integer> vertices) {
        initVariables(edgeWeightedGraph);

        for(int vertex : vertices.keys()) {
            if (cycleFound) {
                break;
            }

            if (!visited[vertex]) {
                dfs(edgeWeightedGraph, vertex);
            }
        }
    }

    private void initVariables(EdgeWeightedGraphInterface edgeWeightedGraph) {
        visited = new boolean[edgeWeightedGraph.vertices()];
        edgeTo = new Edge[edgeWeightedGraph.vertices()];
        onStack = new boolean[edgeWeightedGraph.vertices()];
        visitedEdges = new HashSet<>();
        cycleFound = false;
        cycle = null;
    }

    private void dfs(EdgeWeightedGraphInterface edgeWeightedGraph, int vertex) {
        onStack[vertex] = true;
        visited[vertex] = true;

        for(Edge neighbor : edgeWeightedGraph.adjacent(vertex)) {
            if (visitedEdges.contains(neighbor)) {
                continue;
            }

            visitedEdges.add(neighbor);
            int neighborVertex = neighbor.other(vertex);

            if (hasCycle()) {
                return;
            } else if (!visited[neighborVertex]) {
                edgeTo[neighborVertex] = neighbor;
                dfs(edgeWeightedGraph, neighborVertex);
            } else if (onStack[neighborVertex]) {
                cycleFound = true;
                cycle = new Stack<>();

                for(int currentVertex = vertex; currentVertex != neighborVertex;
                    currentVertex = edgeTo[currentVertex].other(currentVertex)) {
                    cycle.push(edgeTo[currentVertex]);
                }

                cycle.push(neighbor);
            }
        }

        onStack[vertex] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Stack<Edge> cycle() {
        return cycle;
    }

}
