package chapter4.section1;

import chapter1.section3.Queue;

/**
 * Created by Rene Argento on 18/09/17.
 */
public class GraphProperties {

    private int[] eccentricities;
    private int diameter;
    private int radius;
    private int center;

    private Graph graph;

    //Used for lazy properties computation
    private boolean propertiesComputed;
    private boolean girthComputed;

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

        this.graph = graph;
    }

    //O(V * (V + E))
    public void computeProperties() {
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

        propertiesComputed = true;
    }

    //Used when we have domain information and only need to check a few vertices to compute the properties
    //O(V * (V + E))
    public void computeProperties(int[] vertices) {
        diameter = 0;
        radius = Integer.MAX_VALUE;
        center = 0;

        for(int vertex = 0; vertex < vertices.length; vertex++) {
            BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, vertices[vertex]);

            for(int otherVertex = 0; otherVertex < graph.vertices(); otherVertex++) {
                if (otherVertex == vertices[vertex]) {
                    continue;
                }

                eccentricities[vertices[vertex]] = Math.max(eccentricities[vertices[vertex]], breadthFirstPaths.distTo(otherVertex));
            }

            if (eccentricities[vertices[vertex]] > diameter) {
                diameter = eccentricities[vertices[vertex]];
            }
            if (eccentricities[vertices[vertex]] < radius) {
                radius = eccentricities[vertices[vertex]];
                center = vertices[vertex];
            }
        }

        propertiesComputed = true;
    }

    //O(V * (V + E))
    public void computeGirth() {
        for(int vertex = 0; vertex < graph.vertices(); vertex++) {
            int shortestCycle = bfsToGetShortestCycle(graph, vertex);
            girth = Math.min(girth, shortestCycle);
        }

        girthComputed = true;
    }

    //Used when we have domain information and have an idea of the shortest cycle length
    // and need to validate this information
    //O(VC * (VC + E)), where VC is the number of vertices to check
    public int computeGirthLessOrEqualTo(int girthToSearchFor) {
        for(int vertex = 0; vertex < graph.vertices(); vertex++) {
            int shortestCycle = bfsToGetShortestCycle(graph, vertex);
            girth = Math.min(girth, shortestCycle);

            if (girth <= girthToSearchFor) {
                break;
            }
        }

        girthComputed = true;
        return girth;

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
        if (!propertiesComputed) {
            computeProperties();
        }

        return diameter;
    }

    public int radius() {
        if (!propertiesComputed) {
            computeProperties();
        }

        return radius;
    }

    public int center() {
        if (!propertiesComputed) {
            computeProperties();
        }

        return center;
    }

    public int eccentricity(int vertexId) {
        if (!propertiesComputed) {
            computeProperties();
        }

        return eccentricities[vertexId];
    }

    public int girth() {
        if (!girthComputed) {
            computeGirth();
        }

        return girth;
    }

}