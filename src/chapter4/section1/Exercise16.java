package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 16/09/17.
 */
public class Exercise16 {

    public class GraphProperties {

        private int[] eccentricities;
        private int diameter;
        private int radius;
        private int center;

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

        public int diameter() {
            return diameter;
        }

        public int radius() {
            return radius;
        }

        public int center() {
            return center;
        }
    }

    public static void main(String[] args) {
        Exercise16 exercise16 = new Exercise16();

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

        GraphProperties graphProperties = exercise16.new GraphProperties(graph);
        StdOut.println("Diameter: " + graphProperties.diameter() + " Expected: 10");
        StdOut.println("Radius: " + graphProperties.radius() + " Expected: 5");
        StdOut.println("Center: " + graphProperties.center() + " Expected: 5");
    }

}
