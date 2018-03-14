package chapter4.section1;

/**
 * Created by Rene Argento on 12/09/17.
 */
public class DepthFirstSearch {

    private boolean[] visited;
    private int count;

    public DepthFirstSearch(Graph graph, int sourceVertex) {
        visited = new boolean[graph.vertices()];
        dfs(graph, sourceVertex);
    }

    private void dfs(Graph graph, int vertex) {
        visited[vertex] = true;
        count++;

        for(int neighbor : graph.adjacent(vertex)) {
            if (!visited[neighbor]) {
                dfs(graph, neighbor);
            }
        }
    }

    public boolean marked(int vertex) {
        return visited[vertex];
    }

    public int count() {
        return count;
    }

}
