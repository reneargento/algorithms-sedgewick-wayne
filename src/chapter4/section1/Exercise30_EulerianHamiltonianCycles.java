package chapter4.section1;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Rene Argento on 30/09/17.
 */
@SuppressWarnings("unchecked")
//Based on http://algs4.cs.princeton.edu/41graph/EulerianCycle.java
public class Exercise30_EulerianHamiltonianCycles {

    private class Edge {
        int vertex1;
        int vertex2;
        boolean isUsed;

        Edge(int vertex1, int vertex2) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            isUsed = false;
        }

        public int otherVertex(int vertex) {
            if (vertex == vertex1) {
                return vertex2;
            } else {
                return vertex1;
            }
        }
    }

    public class EulerCycle {

        public Stack<Integer> getEulerCycle(Graph graph) {

            // A graph with no edges is considered to have an Eulerian cycle
            if (graph.edges() == 0) {
                return new Stack<>();
            }

            // Necessary condition: all vertices have even degree
            // (this test is needed or it might find an Eulerian path instead of an Eulerian cycle)
            // An Eulerian path have exactly 2 vertices with even degrees
            for(int vertex = 0; vertex < graph.vertices(); vertex++) {
                if (graph.degree(vertex) % 2 != 0) {
                    return null;
                }
            }

            // Create local view of adjacency lists, to iterate one vertex at a time
            Queue<Edge>[] adjacent = (Queue<Edge>[]) new Queue[graph.vertices()];
            for(int vertex = 0; vertex < graph.vertices(); vertex++) {
                adjacent[vertex] = new Queue<>();
            }

            for(int vertex = 0; vertex < graph.vertices(); vertex++) {
                int selfLoops = 0;

                for(int neighbor : graph.adjacent(vertex)) {
                    //Careful with self-loops
                    if (vertex == neighbor) {
                        if (selfLoops % 2 == 0) {
                            Edge edge = new Edge(vertex, neighbor);
                            adjacent[vertex].enqueue(edge);
                            adjacent[neighbor].enqueue(edge);
                        }

                        selfLoops++;
                    } else {
                        if (vertex < neighbor) {
                            Edge edge = new Edge(vertex, neighbor);
                            adjacent[vertex].enqueue(edge);
                            adjacent[neighbor].enqueue(edge);
                        }
                    }
                }
            }

            //Start the cycle with a non-isolated vertex
            int nonIsolatedVertex = nonIsolatedVertex(graph);
            Stack<Integer> dfsStack = new Stack<>();
            dfsStack.push(nonIsolatedVertex);

            Stack<Integer> eulerCycle = new Stack<>();

            while (!dfsStack.isEmpty()) {
                int vertex = dfsStack.pop();

                while (!adjacent[vertex].isEmpty()) {
                    Edge edge = adjacent[vertex].dequeue();
                    if (edge.isUsed) {
                        continue;
                    }
                    edge.isUsed = true;

                    dfsStack.push(vertex);
                    vertex = edge.otherVertex(vertex);
                }

                // Push vertex with no more leaving edges to the Euler cycle
                eulerCycle.push(vertex);
            }

            // For each edge visited, we visited a vertex. Add 1 because the first and last vertices are the same.
            if (eulerCycle.size() == graph.edges + 1) {
                return eulerCycle;
            } else {
                return null;
            }
        }

        private int nonIsolatedVertex(Graph graph) {
            int nonIsolatedVertex = -1;

            for(int vertex = 0; vertex < graph.vertices(); vertex++) {
                if (graph.degree(vertex) > 0) {
                    nonIsolatedVertex = vertex;
                    break;
                }
            }

            return nonIsolatedVertex;
        }
    }

    public static void main(String[] args) {
        Exercise30_EulerianHamiltonianCycles exercise30 = new Exercise30_EulerianHamiltonianCycles();
        EulerCycle eulerCycle = exercise30.new EulerCycle();

        Graph graphWithEulerPath1 = new Graph(4);
        graphWithEulerPath1.addEdge(0, 1);
        graphWithEulerPath1.addEdge(1, 2);
        graphWithEulerPath1.addEdge(2, 3);
        graphWithEulerPath1.addEdge(3, 0);
        graphWithEulerPath1.addEdge(3, 2);

        Stack<Integer> eulerCycle1 = eulerCycle.getEulerCycle(graphWithEulerPath1);

        if (eulerCycle1 != null) {
            exercise30.printCycle(eulerCycle1);
        } else {
            StdOut.println("There is no Eulerian cycle");
        }
        StdOut.println("Expected: There is no Eulerian cycle\n");

        Graph graphWithEulerCycle1 = new Graph(4);
        graphWithEulerCycle1.addEdge(0, 1);
        graphWithEulerCycle1.addEdge(1, 2);
        graphWithEulerCycle1.addEdge(2, 3);
        graphWithEulerCycle1.addEdge(3, 0);

        Stack<Integer> eulerCycle2 = eulerCycle.getEulerCycle(graphWithEulerCycle1);

        if (eulerCycle2 != null) {
            exercise30.printCycle(eulerCycle2);
        } else {
            StdOut.println("There is no Eulerian cycle");
        }
        StdOut.println("Expected: 0-3 3-2 2-1 1-0\n");

        //Note that vertex 12 is an isolated vertex
        Graph graphWithEulerCycle2 = new Graph(13);
        graphWithEulerCycle2.addEdge(0, 9);
        graphWithEulerCycle2.addEdge(0, 3);
        graphWithEulerCycle2.addEdge(10, 9);
        graphWithEulerCycle2.addEdge(10, 3);
        graphWithEulerCycle2.addEdge(3, 4);
        graphWithEulerCycle2.addEdge(3, 6);
        graphWithEulerCycle2.addEdge(3, 2);
        graphWithEulerCycle2.addEdge(3, 9);
        graphWithEulerCycle2.addEdge(9, 6);
        graphWithEulerCycle2.addEdge(9, 8);
        graphWithEulerCycle2.addEdge(9, 11);
        graphWithEulerCycle2.addEdge(4, 2);
        graphWithEulerCycle2.addEdge(6, 2);
        graphWithEulerCycle2.addEdge(6, 8);
        graphWithEulerCycle2.addEdge(11, 8);
        graphWithEulerCycle2.addEdge(2, 5);
        graphWithEulerCycle2.addEdge(2, 1);
        graphWithEulerCycle2.addEdge(2, 8);
        graphWithEulerCycle2.addEdge(8, 5);
        graphWithEulerCycle2.addEdge(8, 7);
        graphWithEulerCycle2.addEdge(1, 7);

        Stack<Integer> eulerCycle3 = eulerCycle.getEulerCycle(graphWithEulerCycle2);

        if (eulerCycle3 != null) {
            exercise30.printCycle(eulerCycle3);
        } else {
            StdOut.println("There is no Eulerian cycle");
        }
        StdOut.println("Expected: 0-3 3-2 2-1 1-7 7-8 8-2 2-5 5-8 8-6 6-2 2-4 4-3 3-9 9-6 6-3 3-10 10-9 9-8 8-11 11-9 9-0\n");

        Graph graphWithEulerPath2 = new Graph(4);
        graphWithEulerPath2.addEdge(0, 1);
        graphWithEulerPath2.addEdge(1, 2);
        graphWithEulerPath2.addEdge(2, 3);
        graphWithEulerPath2.addEdge(3, 0);
        graphWithEulerPath2.addEdge(3, 1);

        Stack<Integer> eulerCycle4 = eulerCycle.getEulerCycle(graphWithEulerPath2);

        if (eulerCycle4 != null) {
            exercise30.printCycle(eulerCycle4);
        } else {
            StdOut.println("There is no Eulerian cycle");
        }
        StdOut.println("Expected: There is no Eulerian cycle");
    }

    private void printCycle(Stack<Integer> eulerCycle) {
        StdOut.println("Euler cycle:");

        while (!eulerCycle.isEmpty()) {
            int vertex = eulerCycle.pop();

            if (!eulerCycle.isEmpty()) {
                StdOut.print(vertex + "-" + eulerCycle.peek());

                if (eulerCycle.size() > 1) {
                    StdOut.print(" ");
                }
            }
        }
        StdOut.println();
    }

}
