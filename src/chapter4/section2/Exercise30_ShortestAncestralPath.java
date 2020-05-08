package chapter4.section2;

import chapter1.section3.Queue;
import chapter1.section3.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/10/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for suggesting a more efficient algorithm:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/145
public class Exercise30_ShortestAncestralPath {

    private class ShortestAncestralPath {
        private int commonAncestor;
        private String shortestPathFromVertex1ToAncestor;
        private String shortestPathFromVertex2ToAncestor;

        ShortestAncestralPath(int commonAncestor, String shortestPathFromVertex1ToAncestor,
                              String shortestPathFromVertex2ToAncestor) {
            this.commonAncestor = commonAncestor;
            this.shortestPathFromVertex1ToAncestor = shortestPathFromVertex1ToAncestor;
            this.shortestPathFromVertex2ToAncestor = shortestPathFromVertex2ToAncestor;
        }
    }

    private class BreadthFirstSearchToGetIntersection {
        private Digraph digraph;
        private int[] edgeTo;
        private Queue<Integer> pendingVertices;

        BreadthFirstSearchToGetIntersection(Digraph digraph, int source) {
            this.digraph = digraph;
            edgeTo = new int[digraph.vertices()];

            for (int i = 0; i < edgeTo.length; i++) {
                edgeTo[i] = -1;
            }
            edgeTo[source] = source;
            pendingVertices = new Queue<>();
            pendingVertices.enqueue(source);
        }

        int runStep(BreadthFirstSearchToGetIntersection otherBFS) {
            int verticesToProcess = pendingVertices.size();

            while (verticesToProcess > 0) {
                int vertex = pendingVertices.dequeue();
                for (int neighbor : digraph.adjacent(vertex)) {
                    if (edgeTo[neighbor] != -1) {
                        continue;
                    }
                    edgeTo[neighbor] = vertex;

                    if (otherBFS.edgeTo[neighbor] != -1) {
                        return neighbor;
                    }
                    pendingVertices.enqueue(neighbor);
                }
                verticesToProcess--;
            }
            return -1;
        }

        boolean isCompleted() {
            return pendingVertices.isEmpty();
        }

        Stack<Integer> pathTo(int vertex) {
            Stack<Integer> path = new Stack<>();

            while (edgeTo[vertex] != vertex) {
                path.push(vertex);
                vertex = edgeTo[vertex];
            }
            path.push(vertex);
            return path;
        }
    }

    private String pathToString(Stack<Integer> path) {
        StringBuilder pathString = new StringBuilder();

        while (path.size() > 1) {
            pathString.append(path.pop()).append("->").append(path.peek());

            if (path.size() != 1) {
                pathString.append(" ");
            }
        }
        return pathString.toString();
    }

    // O(V + E)
    public ShortestAncestralPath getShortestAncestralPath(Digraph digraph, int vertex1, int vertex2) {
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("Digraph is not a DAG");
        }

        // 1- Reverse graph
        digraph = digraph.reverse();

        // 2- Run a bidirectional breadth-first search to find the ancestor
        BreadthFirstSearchToGetIntersection bfs1 = new BreadthFirstSearchToGetIntersection(digraph, vertex1);
        BreadthFirstSearchToGetIntersection bfs2 = new BreadthFirstSearchToGetIntersection(digraph, vertex2);

        int ancestor = -1;
        while (ancestor == - 1 && (!bfs1.isCompleted() || !bfs2.isCompleted())) {
            ancestor = bfs1.runStep(bfs2);
            if (ancestor == -1) {
                ancestor = bfs2.runStep(bfs1);
            }
        }

        if (ancestor == -1) {
            return new ShortestAncestralPath(-1, null, null);
        }

        // 3- Get the paths from each vertex to the ancestor
        Stack<Integer> shortestPathFromVertex1ToAncestor = bfs1.pathTo(ancestor);
        Stack<Integer> shortestPathFromVertex2ToAncestor = bfs2.pathTo(ancestor);

        return new ShortestAncestralPath(ancestor, pathToString(shortestPathFromVertex1ToAncestor),
                pathToString(shortestPathFromVertex2ToAncestor));
    }

    public static void main(String[] args) {
        Exercise30_ShortestAncestralPath shortestAncestralPath = new Exercise30_ShortestAncestralPath();

        Digraph digraph1 = new Digraph(5);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(0, 3);
        digraph1.addEdge(3, 4);

        ShortestAncestralPath shortestAncestralPath1 = shortestAncestralPath.getShortestAncestralPath(digraph1, 2, 4);
        if (shortestAncestralPath1.commonAncestor == -1) {
            StdOut.println("Common ancestor in digraph 1: No common ancestor found");
        } else {
            StdOut.println("Common ancestor in digraph 1: " + shortestAncestralPath1.commonAncestor);
            StdOut.println("Path from vertex 2 to ancestor: " + shortestAncestralPath1.shortestPathFromVertex1ToAncestor);
            StdOut.println("Path from vertex 4 to ancestor: " + shortestAncestralPath1.shortestPathFromVertex2ToAncestor);
        }
        StdOut.println("Expected: 0");
        StdOut.println("2->1 1->0");
        StdOut.println("4->3 3->0\n");


        Digraph digraph2 = new Digraph(5);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(0, 2);
        digraph2.addEdge(2, 3);
        digraph2.addEdge(2, 4);

        ShortestAncestralPath shortestAncestralPath2 = shortestAncestralPath.getShortestAncestralPath(digraph2, 3, 4);
        if (shortestAncestralPath2.commonAncestor == -1) {
            StdOut.println("Common ancestor in digraph 2: Common ancestor not found");
        } else {
            StdOut.println("Common ancestor in digraph 2: " + shortestAncestralPath2.commonAncestor);
            StdOut.println("Path from vertex 3 to ancestor: " + shortestAncestralPath2.shortestPathFromVertex1ToAncestor);
            StdOut.println("Path from vertex 4 to ancestor: " + shortestAncestralPath2.shortestPathFromVertex2ToAncestor);
        }
        StdOut.println("Expected: 2");
        StdOut.println("3->2");
        StdOut.println("4->2\n");


        // Warmup: This is a DAG in which the shortest ancestral path goes to a common ancestor x (vertex 1) that
        // is not an LCA (vertex 6)
        Digraph digraph3 = new Digraph(9);
        digraph3.addEdge(0, 1);
        digraph3.addEdge(1, 2);
        digraph3.addEdge(1, 3);

        digraph3.addEdge(4, 5);
        digraph3.addEdge(5, 6);
        digraph3.addEdge(6, 8);
        digraph3.addEdge(6, 7);
        digraph3.addEdge(7, 2);
        digraph3.addEdge(8, 3);

        ShortestAncestralPath shortestAncestralPath3 = shortestAncestralPath.getShortestAncestralPath(digraph3, 2, 3);
        if (shortestAncestralPath3.commonAncestor == -1) {
            StdOut.println("Common ancestor in digraph 3: Common ancestor not found");
        } else {
            StdOut.println("Common ancestor in digraph 3: " + shortestAncestralPath3.commonAncestor);
            StdOut.println("Path from vertex 2 to ancestor: " + shortestAncestralPath3.shortestPathFromVertex1ToAncestor);
            StdOut.println("Path from vertex 3 to ancestor: " + shortestAncestralPath3.shortestPathFromVertex2ToAncestor);
        }
        StdOut.println("Expected: 1");
        StdOut.println("2->1");
        StdOut.println("3->1\n");


        Digraph digraph4 = new Digraph(9);
        digraph4.addEdge(0, 1);
        digraph4.addEdge(1, 3);
        digraph4.addEdge(1, 4);
        digraph4.addEdge(4, 5);
        digraph4.addEdge(5, 6);
        digraph4.addEdge(6, 2);

        digraph4.addEdge(7, 8);
        digraph4.addEdge(8, 3);
        digraph4.addEdge(7, 2);

        ShortestAncestralPath shortestAncestralPath4 = shortestAncestralPath.getShortestAncestralPath(digraph4, 2, 3);
        if (shortestAncestralPath4.commonAncestor == -1) {
            StdOut.println("Common ancestor in digraph 4: Common ancestor not found");
        } else {
            StdOut.println("Common ancestor in digraph 4: " + shortestAncestralPath4.commonAncestor);
            StdOut.println("Path from vertex 2 to ancestor: " + shortestAncestralPath4.shortestPathFromVertex1ToAncestor);
            StdOut.println("Path from vertex 3 to ancestor: " + shortestAncestralPath4.shortestPathFromVertex2ToAncestor);
        }
        StdOut.println("Expected: 7");
        StdOut.println("2->7");
        StdOut.println("3->8 8->7\n");


        Digraph digraph5 = new Digraph(4);
        digraph5.addEdge(0, 1);
        digraph5.addEdge(1, 2);

        ShortestAncestralPath shortestAncestralPath5 = shortestAncestralPath.getShortestAncestralPath(digraph5, 2, 3);
        if (shortestAncestralPath5.commonAncestor == -1) {
            StdOut.println("Common ancestor in digraph 5: Common ancestor not found");
        } else {
            StdOut.println("Common ancestor in digraph 5: " + shortestAncestralPath5.commonAncestor);
            StdOut.println("Path from vertex 2 to ancestor: " + shortestAncestralPath5.shortestPathFromVertex1ToAncestor);
            StdOut.println("Path from vertex 3 to ancestor: " + shortestAncestralPath5.shortestPathFromVertex2ToAncestor);
        }
        StdOut.println("Expected: Common ancestor not found");
    }

}
