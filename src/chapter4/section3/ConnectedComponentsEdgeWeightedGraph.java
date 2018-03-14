package chapter4.section3;

/**
 * Created by Rene Argento on 09/11/17.
 */
public class ConnectedComponentsEdgeWeightedGraph {

    private boolean[] visited;
    private int[] id;
    private int count;

    public ConnectedComponentsEdgeWeightedGraph(EdgeWeightedGraphInterface graph) {
        visited = new boolean[graph.vertices()];
        id = new int[graph.vertices()];

        for(int source = 0; source < graph.vertices(); source++) {
            if (!visited[source]) {
                dfs(graph, source);
                count++;
            }
        }
    }

    private void dfs(EdgeWeightedGraphInterface graph, int vertex) {
        visited[vertex] = true;
        id[vertex] = count;

        for(Edge neighbor : graph.adjacent(vertex)) {
            int neighborVertex = neighbor.other(vertex);

            if (!visited[neighborVertex]) {
                dfs(graph, neighborVertex);
            }
        }
    }

    public boolean connected(int vertex1, int vertex2) {
        return id[vertex1] == id[vertex2];
    }

    public int id(int vertex) {
        return id[vertex];
    }

    public int count() {
        return count;
    }

}
