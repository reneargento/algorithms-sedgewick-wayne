package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 16/09/17.
 */
public class Exercise17 {

    public class GraphProperties {

        private int[] eccentricities;
        private int diameter;
        private int radius;
        private int center;

        private long wienerIndex;

        GraphProperties(Graph graph) {
            eccentricities = new int[graph.vertices()];

            ConnectedComponentsRecursiveDFS connectedComponents = new ConnectedComponentsRecursiveDFS(graph);

            if (connectedComponents.count() != 1) {
                throw new RuntimeException("Graph must be connected");
            }

            getProperties(graph);
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

                    int shortestDistanceFromOtherVertex = breadthFirstPaths.distTo(otherVertex);
                    wienerIndex += shortestDistanceFromOtherVertex;

                    eccentricities[vertex] = Math.max(eccentricities[vertex], shortestDistanceFromOtherVertex);
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

        public int diameter() {
            return diameter;
        }

        public int radius() {
            return radius;
        }

        public int center() {
            return center;
        }

        public long wiener() {
            return wienerIndex;
        }

    }

    public static void main(String[] args) {
        Exercise17 exercise17 = new Exercise17();

        //Graph
        // 0 -- 1 -- 2 -- 3 -- 4 -- 5 -- 6 -- 7 -- 8 -- 9 -- 10

        Graph graph = new Graph(11);
        graph.addEdge(0 ,1);
        graph.addEdge(1 ,2);
        graph.addEdge(2 ,3);
        graph.addEdge(3 ,4);
        graph.addEdge(4 ,5);
        graph.addEdge(5 ,6);
        graph.addEdge(6 ,7);
        graph.addEdge(7 ,8);
        graph.addEdge(8 ,9);
        graph.addEdge(9 ,10);

        //Wiener index
        /**
         *
         Vertex 0: 0 + 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10 = 55
         Vertex 1: 1 + 0 + 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 = 46
         Vertex 2: 2 + 1 + 0 + 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 = 39
         Vertex 3: 3 + 2 + 1 + 0 + 1 + 2 + 3 + 4 + 5 + 6 + 7 = 34
         Vertex 4: 4 + 3 + 2 + 1 + 0 + 1 + 2 + 3 + 4 + 5 + 6 = 31
         = 205

         205 * 2 = 410 //Same sums for vertices from 6 to 10 as well

         Vertex 5: 5 + 4 + 3 + 2 + 1 + 0 + 1 + 2 + 3 + 4 + 5 = 30

         410 + 30 = 440
         */

        GraphProperties graphProperties = exercise17.new GraphProperties(graph);
        StdOut.println("Wiener index: " + graphProperties.wiener() + " Expected: 440");
    }

}
