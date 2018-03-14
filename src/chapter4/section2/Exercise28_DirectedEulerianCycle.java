package chapter4.section2;

import chapter1.section3.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by Rene Argento on 23/10/17.
 */
//Based on https://algs4.cs.princeton.edu/42digraph/DirectedEulerianCycle.java.html
@SuppressWarnings("unchecked")
public class Exercise28_DirectedEulerianCycle {

    private class DirectedEulerianCycle {

        public Stack<Integer> getDirectedEulerianCycle(Digraph digraph) {
            // A graph with no edges is considered to have an Eulerian cycle
            if (digraph.edges() == 0) {
                return new Stack<>();
            }

            // Check if all vertices have indegree equal to their outdegree
            // If any vertex does not, the algorithm may return an Eulerian path instead
            for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
                if (digraph.indegree(vertex) != digraph.outdegree(vertex)) {
                    return null;
                }
            }

            // Create local view of adjacency lists, to iterate one vertex at a time
            Iterator<Integer>[] adjacent = (Iterator<Integer>[]) new Iterator[digraph.vertices()];
            for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
                adjacent[vertex] = digraph.adjacent(vertex).iterator();
            }

            //Start the cycle with a non-isolated vertex
            int nonIsolatedVertex = nonIsolatedVertex(digraph);
            Stack<Integer> dfsStack = new Stack<>();
            dfsStack.push(nonIsolatedVertex);

            Stack<Integer> eulerCycle = new Stack<>();

            while (!dfsStack.isEmpty()) {
                int vertex = dfsStack.pop();

                while (adjacent[vertex].hasNext()) {
                    dfsStack.push(vertex);
                    vertex = adjacent[vertex].next();
                }

                // Push vertex with no more leaving edges to the Euler cycle
                eulerCycle.push(vertex);
            }

            // For each edge visited, we visited a vertex. Add 1 because the first and last vertices are the same.
            if (eulerCycle.size() == digraph.edges() + 1) {
                return eulerCycle;
            } else {
                return null;
            }
        }

        private int nonIsolatedVertex(Digraph digraph) {
            int nonIsolatedVertex = -1;

            for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
                if (digraph.outdegree(vertex) > 0) {
                    nonIsolatedVertex = vertex;
                }
            }

            return nonIsolatedVertex;
        }
    }

    public static void main(String[] args) {
        Exercise28_DirectedEulerianCycle exercise28 = new Exercise28_DirectedEulerianCycle();
        DirectedEulerianCycle directedEulerianCycle = exercise28.new DirectedEulerianCycle();

        Digraph digraphWithDirectedEulerPath1 = new Digraph(4);
        digraphWithDirectedEulerPath1.addEdge(0, 1);
        digraphWithDirectedEulerPath1.addEdge(1, 2);
        digraphWithDirectedEulerPath1.addEdge(2, 3);
        digraphWithDirectedEulerPath1.addEdge(3, 0);
        digraphWithDirectedEulerPath1.addEdge(3, 2);

        Stack<Integer> eulerCycle1 = directedEulerianCycle.getDirectedEulerianCycle(digraphWithDirectedEulerPath1);

        if (eulerCycle1 != null) {
            exercise28.printCycle(eulerCycle1);
        } else {
            StdOut.println("There is no directed Eulerian cycle");
        }
        StdOut.println("Expected: There is no directed Eulerian cycle\n");

        Digraph digraphWithDirectedEulerCycle1 = new Digraph(4);
        digraphWithDirectedEulerCycle1.addEdge(0, 1);
        digraphWithDirectedEulerCycle1.addEdge(1, 2);
        digraphWithDirectedEulerCycle1.addEdge(2, 3);
        digraphWithDirectedEulerCycle1.addEdge(3, 0);

        Stack<Integer> eulerCycle2 = directedEulerianCycle.getDirectedEulerianCycle(digraphWithDirectedEulerCycle1);

        if (eulerCycle2 != null) {
            exercise28.printCycle(eulerCycle2);
        } else {
            StdOut.println("There is no directed Eulerian cycle");
        }
        StdOut.println("Expected: 3->0 0->1 1->2 2->3\n");

        //Note that vertex 5 is an isolated vertex
        Digraph digraphWithDirectedEulerCycle2 = new Digraph(6);
        digraphWithDirectedEulerCycle2.addEdge(0, 1);
        digraphWithDirectedEulerCycle2.addEdge(1, 2);
        digraphWithDirectedEulerCycle2.addEdge(2, 0);
        digraphWithDirectedEulerCycle2.addEdge(1, 3);
        digraphWithDirectedEulerCycle2.addEdge(3, 1);
        digraphWithDirectedEulerCycle2.addEdge(3, 2);
        digraphWithDirectedEulerCycle2.addEdge(2, 4);
        digraphWithDirectedEulerCycle2.addEdge(4, 3);

        Stack<Integer> eulerCycle3 = directedEulerianCycle.getDirectedEulerianCycle(digraphWithDirectedEulerCycle2);

        if (eulerCycle3 != null) {
            exercise28.printCycle(eulerCycle3);
        } else {
            StdOut.println("There is no directed Eulerian cycle");
        }
        StdOut.println("Expected: 4->3 3->2 2->0 0->1 1->3 3->1 1->2 2->4\n");

        Digraph digraphWithDirectedEulerPath2 = new Digraph(4);
        digraphWithDirectedEulerPath2.addEdge(0, 1);
        digraphWithDirectedEulerPath2.addEdge(1, 2);
        digraphWithDirectedEulerPath2.addEdge(2, 3);
        digraphWithDirectedEulerPath2.addEdge(3, 0);
        digraphWithDirectedEulerPath2.addEdge(3, 1);

        Stack<Integer> eulerCycle4 = directedEulerianCycle.getDirectedEulerianCycle(digraphWithDirectedEulerPath2);

        if (eulerCycle4 != null) {
            exercise28.printCycle(eulerCycle4);
        } else {
            StdOut.println("There is no directed Eulerian cycle");
        }
        StdOut.println("Expected: There is no directed Eulerian cycle");
    }

    private void printCycle(Stack<Integer> eulerCycle) {
        StdOut.println("Euler cycle:");

        while (!eulerCycle.isEmpty()) {
            int vertex = eulerCycle.pop();

            if (!eulerCycle.isEmpty()) {
                StdOut.print(vertex + "->" + eulerCycle.peek());

                if (eulerCycle.size() > 1) {
                    StdOut.print(" ");
                }
            }
        }
        StdOut.println();
    }

}
