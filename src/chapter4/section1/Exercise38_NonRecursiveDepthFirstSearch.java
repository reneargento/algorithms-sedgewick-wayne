package chapter4.section1;

import chapter1.section3.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by Rene Argento on 07/10/17.
 */
@SuppressWarnings("unchecked")
public class Exercise38_NonRecursiveDepthFirstSearch {

    private class DepthFirstSearchIterative {

        public void depthFirstSearchIterative(Graph graph) {
            boolean[] visited = new boolean[graph.vertices()];

            StdOut.print("Visit order: ");

            for(int vertexId = 0; vertexId < graph.vertices(); vertexId++) {
                if (!visited[vertexId]) {
                    depthFirstSearchIterative(graph, vertexId, visited);
                }
            }
        }

        // Trade-off between time and memory
        // Takes longer because it has to create the iterators, but avoid stack overflows
        private void depthFirstSearchIterative(Graph graph, int sourceVertex, boolean[] visited) {
            Stack<Integer> stack = new Stack<>();
            stack.push(sourceVertex);
            visited[sourceVertex] = true;
            StdOut.print(sourceVertex + " ");

            // Used to be able to iterate over each adjacency list, keeping track of which
            // vertex in each adjacency list needs to be explored next
            Iterator<Integer>[] adjacentIterators = (Iterator<Integer>[]) new Iterator[graph.vertices()];

            for (int vertexId = 0; vertexId < adjacentIterators.length; vertexId++) {
                if (graph.adjacent(vertexId) != null) {
                    adjacentIterators[vertexId] = graph.adjacent(vertexId).iterator();
                }
            }

            while (!stack.isEmpty()) {
                int currentVertex = stack.peek();

                if (adjacentIterators[currentVertex].hasNext()) {
                    int neighbor = adjacentIterators[currentVertex].next();

                    if (!visited[neighbor]) {
                        StdOut.print(neighbor + " ");

                        stack.push(neighbor);
                        visited[neighbor] = true;
                    }
                } else {
                    stack.pop();
                }
            }
        }

    }

    public static void main(String[] args) {
        Exercise38_NonRecursiveDepthFirstSearch nonRecursiveDepthFirstSearch = new Exercise38_NonRecursiveDepthFirstSearch();
        DepthFirstSearchIterative depthFirstSearchIterative = nonRecursiveDepthFirstSearch.new DepthFirstSearchIterative();

        Graph graph = new Graph(12);
        //Component 1
        graph.addEdge(0, 7);
        graph.addEdge(0, 1);
        graph.addEdge(2, 1);
        graph.addEdge(0, 2);
        graph.addEdge(3, 6);
        graph.addEdge(4, 6);
        graph.addEdge(3, 4);
        graph.addEdge(1, 5);
        graph.addEdge(5, 6);

        //Component 2
        //vertex 9 (isolated vertex)

        //Component 3
        graph.addEdge(11, 10);
        graph.addEdge(10, 8);

        depthFirstSearchIterative.depthFirstSearchIterative(graph);

        StdOut.println("\nExpected: Visit order: 0 2 1 5 6 4 3 7 8 10 11 9");
    }

}
