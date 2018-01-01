package chapter4.section2;

import chapter1.section3.Queue;
import chapter1.section3.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/10/17.
 */
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

    //O(V + E)
    public ShortestAncestralPath getShortestAncestralPath(Digraph digraph, int vertex1, int vertex2) {
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if(directedCycle.hasCycle()) {
            throw new IllegalArgumentException("Digraph is not a DAG");
        }

        // 1- Reverse graph
        Digraph reverseDigraph = digraph.reverse();

        int[] distancesFromVertex1 = new int[reverseDigraph.vertices()];
        int[] distancesFromVertex2 = new int[reverseDigraph.vertices()];

        for(int vertex = 0; vertex < reverseDigraph.vertices(); vertex++) {
            distancesFromVertex1[vertex] = Integer.MAX_VALUE;
            distancesFromVertex2[vertex] = Integer.MAX_VALUE;
        }

        // 2- Do a BFS from vertex1 to find all its ancestors and compute the distance from vertex1 to them
        bfs(reverseDigraph, vertex1, distancesFromVertex1);

        // 3- Do a BFS from vertex2 to find all its ancestors and compute the distance from vertex2 to them
        bfs(reverseDigraph, vertex2, distancesFromVertex2);

        // 4- Find the common ancestor with a shortest ancestral path
        int commonAncestorWithShortestPath = -1;
        int shortestDistance = Integer.MAX_VALUE;

        for(int vertex = 0; vertex < reverseDigraph.vertices(); vertex++) {
            if(distancesFromVertex1[vertex] != Integer.MAX_VALUE
                    && distancesFromVertex2[vertex] != Integer.MAX_VALUE
                    && distancesFromVertex1[vertex] + distancesFromVertex2[vertex] < shortestDistance) {
                shortestDistance = distancesFromVertex1[vertex] + distancesFromVertex2[vertex];
                commonAncestorWithShortestPath = vertex;
            }
        }

        if(commonAncestorWithShortestPath == -1) {
            return new ShortestAncestralPath(-1, null, null);
        }

        // 5- Do a BFS from vertex1 to the common ancestor to get the shortest path
        String shortestPathFromVertex1ToAncestor = bfsToGetPath(reverseDigraph, vertex1, commonAncestorWithShortestPath);

        // 6- Do a BFS from vertex2 to the common ancestor to get the shortest path
        String shortestPathFromVertex2ToAncestor = bfsToGetPath(reverseDigraph, vertex2, commonAncestorWithShortestPath);

        return new ShortestAncestralPath(commonAncestorWithShortestPath, shortestPathFromVertex1ToAncestor,
                shortestPathFromVertex2ToAncestor);
    }

    private void bfs(Digraph digraph, int source, int[] distances) {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(source);
        distances[source] = 0;

        while (!queue.isEmpty()) {
            int currentVertex = queue.dequeue();

            for(int neighbor : digraph.adjacent(currentVertex)) {
                distances[neighbor] = distances[currentVertex] + 1;
                queue.enqueue(neighbor);
            }
        }
    }

    private String bfsToGetPath(Digraph digraph, int source, int target) {
        int[] edgeTo = new int[digraph.vertices()];
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(source);

        while (!queue.isEmpty()) {
            int currentVertex = queue.dequeue();

            for(int neighbor : digraph.adjacent(currentVertex)) {
                edgeTo[neighbor] = currentVertex;
                queue.enqueue(neighbor);

                if(neighbor == target) {
                    Stack<Integer> inversePath = new Stack<>();

                    for(int vertex = target; vertex != source; vertex = edgeTo[vertex]) {
                        inversePath.push(vertex);
                    }
                    inversePath.push(source);

                    StringBuilder path = new StringBuilder();

                    while (!inversePath.isEmpty()) {
                        int vertexInPath = inversePath.pop();

                        if(!inversePath.isEmpty()) {
                            int nextVertexInPath = inversePath.peek();
                            path.append(vertexInPath).append("->").append(nextVertexInPath);
                        }

                        if(inversePath.size() > 1) {
                            path.append(" ");
                        }
                    }

                    return path.toString();
                }
            }
        }

        //If we got here, there is not path from source vertex to target vertex
        return null;
    }

    public static void main(String[] args) {
        Exercise30_ShortestAncestralPath shortestAncestralPath = new Exercise30_ShortestAncestralPath();

        Digraph digraph1 = new Digraph(5);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(0, 3);
        digraph1.addEdge(3, 4);

        ShortestAncestralPath shortestAncestralPath1 = shortestAncestralPath.getShortestAncestralPath(digraph1, 2, 4);
        if(shortestAncestralPath1.commonAncestor == -1) {
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
        if(shortestAncestralPath2.commonAncestor == -1) {
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
        if(shortestAncestralPath3.commonAncestor == -1) {
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
        if(shortestAncestralPath4.commonAncestor == -1) {
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
        if(shortestAncestralPath5.commonAncestor == -1) {
            StdOut.println("Common ancestor in digraph 5: Common ancestor not found");
        } else {
            StdOut.println("Common ancestor in digraph 5: " + shortestAncestralPath5.commonAncestor);
            StdOut.println("Path from vertex 2 to ancestor: " + shortestAncestralPath5.shortestPathFromVertex1ToAncestor);
            StdOut.println("Path from vertex 3 to ancestor: " + shortestAncestralPath5.shortestPathFromVertex2ToAncestor);
        }
        StdOut.println("Expected: Common ancestor not found");
    }

}
