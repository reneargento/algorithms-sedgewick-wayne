package chapter4.section1;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 17/09/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for simplifying the bfsToGetShortestCycle() method.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/137
public class Exercise18 {

    public class GraphProperties {

        private int[] eccentricities;
        private int diameter;
        private int radius;
        private int center;

        private int girth = Integer.MAX_VALUE;

        GraphProperties(Graph graph, boolean useIterativeDFS) {
            eccentricities = new int[graph.vertices()];

            ConnectedComponents connectedComponents;

            if (useIterativeDFS) {
                connectedComponents = new ConnectedComponentsIterativeDFS(graph);
            } else {
                connectedComponents = new ConnectedComponentsRecursiveDFS(graph);
            }

            if (connectedComponents.count() != 1) {
                throw new RuntimeException("Graph must be connected");
            }

            getProperties(graph);
            computeGirth(graph);
        }

        //O(V * (V + E))
        private void getProperties(Graph graph) {
            diameter = 0;
            radius = Integer.MAX_VALUE;
            center = 0;

            for(int vertex = 0; vertex < graph.vertices(); vertex++) {
                BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, vertex);

                for(int otherVertex = 0; otherVertex < graph.vertices(); otherVertex++) {
                    if (otherVertex == vertex) {
                        continue;
                    }

                    eccentricities[vertex] = Math.max(eccentricities[vertex], breadthFirstPaths.distTo(otherVertex));
                }

                if (eccentricities[vertex] > diameter) {
                    diameter = eccentricities[vertex];
                }
                if (eccentricities[vertex] < radius) {
                    radius = eccentricities[vertex];
                    center = vertex;
                }
            }
        }

        //O(V * (V + E))
        private void computeGirth(Graph graph) {
            for(int vertex = 0; vertex < graph.vertices(); vertex++) {
                int shortestCycle = bfsToGetShortestCycle(graph, vertex);
                girth = Math.min(girth, shortestCycle);
            }
        }

        private int bfsToGetShortestCycle(Graph graph, int sourceVertex) {
            int shortestCycle = Integer.MAX_VALUE;
            int[] distTo = new int[graph.vertices()];
            int[] edgeTo = new int[graph.vertices()];

            Queue<Integer> queue = new Queue<>();
            boolean[] visited = new boolean[graph.vertices()];

            visited[sourceVertex] = true;
            edgeTo[sourceVertex] = Integer.MAX_VALUE;
            queue.enqueue(sourceVertex);

            while (!queue.isEmpty()) {
                int currentVertex = queue.dequeue();

                for(int neighbor : graph.adjacent(currentVertex)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        distTo[neighbor] = distTo[currentVertex] + 1;
                        edgeTo[neighbor] = currentVertex;
                        queue.enqueue(neighbor);
                    } else if (neighbor != edgeTo[currentVertex]) {
                        // Cycle found
                        int cycleLength = distTo[currentVertex] + distTo[neighbor] + 1;
                        shortestCycle = Math.min(shortestCycle, cycleLength);
                    }
                }
            }

            return shortestCycle;
        }

        public int diameter() {
            return diameter;
        }

        public int radius() {
            return radius;
        }

        public int center() {
            return center;
        }

        public int girth() {
            return girth;
        }

        public int eccentricity(int vertexId) {
            return eccentricities[vertexId];
        }
    }

    public static void main(String[] args) {
        Exercise18 exercise18 = new Exercise18();

        // Graph with girth = 3
        Graph graph1 = new Graph(6);
        graph1.addEdge(2 ,3);
        graph1.addEdge(0 ,1);
        graph1.addEdge(3 ,1);
        graph1.addEdge(5 ,3);
        graph1.addEdge(2 ,0);
        graph1.addEdge(1 ,2);
        graph1.addEdge(4 ,2);
        graph1.addEdge(4 ,5);
        graph1.addEdge(4 ,0);

        // Graph with girth = 2
        Graph graph2 = new Graph(4);
        graph2.addEdge(0, 1);
        graph2.addEdge(1, 0); //Parallel edge
        graph2.addEdge(1, 2);
        graph2.addEdge(2, 3);

        // Graph with girth = 4
        Graph graph3 = new Graph(4);
        graph3.addEdge(0, 1);
        graph3.addEdge(1, 2);
        graph3.addEdge(2, 3);
        graph3.addEdge(3, 0);

        // Graph with girth = Integer.MAX_VALUE
        Graph graph4 = new Graph(4);
        graph4.addEdge(0, 1);
        graph4.addEdge(1, 3);
        graph4.addEdge(2, 3);

        GraphProperties graphProperties1 = exercise18.new GraphProperties(graph1, false);
        StdOut.println("Girth 1: " + graphProperties1.girth() + " Expected: 3");

        GraphProperties graphProperties2 = exercise18.new GraphProperties(graph2, false);
        StdOut.println("Girth 2: " + graphProperties2.girth() + " Expected: 2");

        GraphProperties graphProperties3 = exercise18.new GraphProperties(graph3, false);
        StdOut.println("Girth 3: " + graphProperties3.girth() + " Expected: 4");

        GraphProperties graphProperties4 = exercise18.new GraphProperties(graph4, false);
        StdOut.println("Girth 4: " + graphProperties4.girth() + " Expected: 2147483647");
    }

}
