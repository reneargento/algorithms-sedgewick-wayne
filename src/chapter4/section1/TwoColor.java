package chapter4.section1;

/**
 * Created by rene on 15/09/17.
 */
public class TwoColor {

    private boolean[] visited;
    private boolean[] color;
    private boolean isTwoColorable = true;

    public TwoColor(Graph graph) {
        visited = new boolean[graph.vertices()];
        color = new boolean[graph.vertices()];

        for(int source = 0; source < graph.vertices(); source++) {
            if(!visited[source]) {
                dfs(graph, source);
            }
        }
    }

    private void dfs(Graph graph, int vertex) {
        visited[vertex] = true;

        for(int neighbor : graph.adjacent(vertex)) {
            if(!visited[neighbor]) {
                color[neighbor] = !color[vertex];
                dfs(graph, neighbor);
            } else if(color[neighbor] == color[vertex]) {
                isTwoColorable = false;
            }
        }
    }

    public boolean isBipartite() {
        return isTwoColorable;
    }

}
