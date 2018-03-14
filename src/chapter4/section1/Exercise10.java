package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 16/09/17.
 */
public class Exercise10 {

    public class DepthFirstSearchConnected {

        private boolean[] visited;
        private int count;
        private int vertexThatCanBeRemoved;

        private Graph graph;
        private int sourceVertex;

        public DepthFirstSearchConnected(Graph graph, int sourceVertex) {
            visited = new boolean[graph.vertices()];

            this.graph = graph;
            this.sourceVertex = sourceVertex;
        }

        public int findVertexThatCanBeRemoved() {
            dfs(graph, sourceVertex);
            return vertexThatCanBeRemoved;
        }

        private void dfs(Graph graph, int vertex) {
            visited[vertex] = true;
            count++;

            boolean areAllNeighborsMarked = true;

            for(int neighbor : graph.adjacent(vertex)) {
                if (!visited[neighbor]) {
                    areAllNeighborsMarked = false;
                    dfs(graph, neighbor);
                }
            }

            if (areAllNeighborsMarked) {
                vertexThatCanBeRemoved = vertex;
            }
        }

        public boolean marked(int vertex) {
            return visited[vertex];
        }

        public int count() {
            return count;
        }

    }

    public static void main(String[] args) {
        Exercise10 exercise10 = new Exercise10();

        //Connected graph
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(0, 4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);

        DepthFirstSearchConnected depthFirstSearchConnected = exercise10.new DepthFirstSearchConnected(graph, 0);
        StdOut.println("Vertex that can be removed: " + depthFirstSearchConnected.findVertexThatCanBeRemoved() + " Expected: 1");
    }

}
