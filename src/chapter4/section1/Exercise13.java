package chapter4.section1;

import chapter1.section3.Queue;
import chapter1.section3.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 16/09/17.
 */
public class Exercise13 {

    public class BreadthFirstPathsDistTo {

        private boolean[] visited;
        private int[] edgeTo;
        private final int source;

        private int[] distTo;

        public BreadthFirstPathsDistTo(Graph graph, int source) {
            visited = new boolean[graph.vertices()];
            edgeTo = new int[graph.vertices()];
            this.source = source;

            distTo = new int[graph.vertices()];

            distTo[source] = 0;
            for(int vertex = 0; vertex < graph.vertices(); vertex++) {
                if (vertex == source) {
                    continue;
                }
                distTo[vertex] = Integer.MAX_VALUE;
            }

            bfs(graph, source);
        }

        private void bfs(Graph graph, int sourceVertex) {
            Queue<Integer> queue = new Queue<>();
            visited[sourceVertex] = true;

            queue.enqueue(sourceVertex);

            while (!queue.isEmpty()) {
                int currentVertex = queue.dequeue();

                for(int neighbor : graph.adjacent(currentVertex)) {
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

    public static void main(String[] args) {
        //tinyGex2 graph from the book
        Graph graph = new Graph(12);
        graph.addEdge(8 ,4);
        graph.addEdge(2 ,3);
        graph.addEdge(1 ,11);
        graph.addEdge(0 ,6);
        graph.addEdge(3 ,6);
        graph.addEdge(10 ,3);
        graph.addEdge(7 ,11);
        graph.addEdge(7 ,8);
        graph.addEdge(11 ,8);
        graph.addEdge(2 ,0);
        graph.addEdge(6 ,2);
        graph.addEdge(5 ,2);
        graph.addEdge(5 ,10);
        graph.addEdge(5 ,0);
        graph.addEdge(8 ,1);
        graph.addEdge(4 ,1);

        BreadthFirstPathsDistTo breadthFirstPathsDistTo = new Exercise13().new BreadthFirstPathsDistTo(graph, 0);
        StdOut.println("Distance from 0 to 0: " + breadthFirstPathsDistTo.distTo(0) + " Expected: 0");
        StdOut.println("Distance from 0 to 6: " + breadthFirstPathsDistTo.distTo(6) + " Expected: 1");
        StdOut.println("Distance from 0 to 10: " + breadthFirstPathsDistTo.distTo(10) + " Expected: 2");
        StdOut.println("Distance from 0 to 3: " + breadthFirstPathsDistTo.distTo(3) + " Expected: 2");
        StdOut.println("Distance from 0 to 8: " + breadthFirstPathsDistTo.distTo(8) + " Expected: 2147483647");
        StdOut.println("Distance from 0 to 9: " + breadthFirstPathsDistTo.distTo(9) + " Expected: 2147483647");
    }

}
