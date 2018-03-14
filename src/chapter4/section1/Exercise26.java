package chapter4.section1;

import chapter1.section3.Stack;
import com.sun.tools.internal.jxc.ap.Const;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

import java.util.Iterator;

/**
 * Created by Rene Argento on 19/09/17.
 */
@SuppressWarnings("unchecked")
public class Exercise26 {

    private class DegreesOfSeparationDFS {

        private class DepthFirstPathsIterative {

            private boolean[] visited;
            private int[] edgeTo;
            private final int source;

            public DepthFirstPathsIterative(Graph graph, int source) {
                visited = new boolean[graph.vertices()];
                edgeTo = new int[graph.vertices()];
                this.source = source;
                depthFirstSearchIterative(graph, source);
            }

            private void depthFirstSearchIterative(Graph graph, int sourceVertex) {
                Stack<Integer> stack = new Stack<>();
                stack.push(sourceVertex);
                visited[sourceVertex] = true;

                // Used to be able to iterate over each adjacency list, keeping track of which
                // vertex in each adjacency list needs to be explored next
                Iterator<Integer>[] adjacentIterators = (Iterator<Integer>[]) new Iterator[graph.vertices()];

                for (int vertexId = 0; vertexId < adjacentIterators.length; vertexId++) {
                    if (graph.getAdjacencyList()[vertexId] != null) {
                        adjacentIterators[vertexId] = graph.getAdjacencyList()[vertexId].iterator();
                    }
                }

                while (!stack.isEmpty()) {
                    int currentVertex = stack.peek();

                    if (adjacentIterators[currentVertex].hasNext()) {
                        int neighbor = adjacentIterators[currentVertex].next();

                        if (!visited[neighbor]) {
                            stack.push(neighbor);
                            visited[neighbor] = true;
                            edgeTo[neighbor] = currentVertex;
                        }
                    } else {
                        stack.pop();
                    }
                }
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

        private void degreesOfSeparationDFS(String filePath, String separator, String sourcePerformer) {
            SymbolGraph symbolGraph = new SymbolGraph(filePath, separator);

            Graph graph = symbolGraph.graph();

            if (!symbolGraph.contains(sourcePerformer)) {
                StdOut.println(sourcePerformer + " not in database.");
                return;
            }

            int sourceVertex = symbolGraph.index(sourcePerformer);
            DepthFirstPathsIterative depthFirstPathsIterative = new DepthFirstPathsIterative(graph, sourceVertex);

            while (!StdIn.isEmpty()) {
                String sink = StdIn.readLine();

                if (symbolGraph.contains(sink)) {
                    int destinationVertex = symbolGraph.index(sink);

                    if (depthFirstPathsIterative.hasPathTo(destinationVertex)) {
                        for(int vertexInPath : depthFirstPathsIterative.pathTo(destinationVertex)) {
                            StdOut.println("    " + symbolGraph.name(vertexInPath));
                        }
                    } else {
                        StdOut.println("Not connected");
                    }
                } else {
                    StdOut.println("Not in database.");
                }
            }
        }
    }

    //Arguments example: movies.txt / "Bacon, Kevin"
    public static void main(String[] args) {
        String movieFilePath = Constants.FILES_PATH + args[0];
        new Exercise26().new DegreesOfSeparationDFS().degreesOfSeparationDFS(movieFilePath, args[1], args[2]);
    }

}
