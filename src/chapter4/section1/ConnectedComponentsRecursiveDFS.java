package chapter4.section1;

/**
 * Created by Rene Argento on 15/09/17.
 */
public class ConnectedComponentsRecursiveDFS implements ConnectedComponents {

    private boolean[] visited;
    private int[] id;
    private int count;

    public ConnectedComponentsRecursiveDFS(GraphInterface graph) {
        visited = new boolean[graph.vertices()];
        id = new int[graph.vertices()];

        for(int source = 0; source < graph.vertices(); source++) {
            if (!visited[source]) {
                dfs(graph, source);
                count++;
            }
        }
    }

    private void dfs(GraphInterface graph, int vertex) {
        visited[vertex] = true;
        id[vertex] = count;

        for(int neighbor : graph.adjacent(vertex)) {
            if (!visited[neighbor]) {
                dfs(graph, neighbor);
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
