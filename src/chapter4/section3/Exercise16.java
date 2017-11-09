package chapter4.section3;

import chapter1.section3.Queue;
import chapter1.section3.Stack;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by rene on 08/11/17.
 */
public class Exercise16 {

    private boolean visited[];
    private Edge[] edgeTo;
    private Stack<Edge> cycle; // edges on  a cycle (if one exists)
    private boolean[] onStack; // vertices on recursive call stack
    private HashSet<Edge> visitedEdges;
    private boolean cycleFound;

    public double[] getWeightRangeToBeInMST(Queue<Edge> minimumSpanningTree, Edge newEdge) {
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(minimumSpanningTree.size() + 1);

        for(Edge edgeInMST : minimumSpanningTree) {
            edgeWeightedGraph.addEdge(edgeInMST);
        }

        // MST now has a cycle
        edgeWeightedGraph.addEdge(newEdge);

        visited = new boolean[edgeWeightedGraph.vertices()];
        edgeTo = new Edge[edgeWeightedGraph.vertices()];
        onStack = new boolean[edgeWeightedGraph.vertices()];
        visitedEdges = new HashSet<>();
        cycleFound = false;
        cycle = null;

        for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
            if(cycleFound) {
                break;
            }

            if(!visited[vertex]) {
                dfs(edgeWeightedGraph, vertex);
            }
        }

        Edge[] edgesInCycle = new Edge[cycle.size()];
        int edgesInCycleIndex = 0;

        for(Edge edgeInCycle : cycle) {
            edgesInCycle[edgesInCycleIndex++] = edgeInCycle;
        }

        Arrays.sort(edgesInCycle);

        double minWeightInRange = Double.NEGATIVE_INFINITY;
        double maxWeightInRange;

        if(newEdge != edgesInCycle[edgesInCycle.length - 1]) {
            maxWeightInRange = edgesInCycle[edgesInCycle.length - 1].weight() - 0.000001;
        } else {
            maxWeightInRange = edgesInCycle[edgesInCycle.length - 2].weight() - 0.000001;
        }

        return new double[]{minWeightInRange, maxWeightInRange};
    }

    private void dfs(EdgeWeightedGraph edgeWeightedGraph, int vertex) {
        onStack[vertex] = true;
        visited[vertex] = true;

        for(Edge neighbor : edgeWeightedGraph.adjacent(vertex)) {
            if(visitedEdges.contains(neighbor)) {
                continue;
            }

            visitedEdges.add(neighbor);
            int neighborVertex = neighbor.other(vertex);

            if(hasCycle()) {
                return;
            } else if(!visited[neighborVertex]) {
                edgeTo[neighborVertex] = neighbor;
                dfs(edgeWeightedGraph, neighborVertex);
            } else if(onStack[neighborVertex]) {
                cycleFound = true;
                cycle = new Stack<>();

                for(int currentVertex = vertex; currentVertex != neighborVertex;
                    currentVertex = edgeTo[currentVertex].other(currentVertex)) {
                    cycle.push(edgeTo[currentVertex]);
                }

                cycle.push(neighbor);
            }
        }

        onStack[vertex] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public static void main(String[] args) {
        Exercise16 exercise16 = new Exercise16();

        Queue<Edge> minimumSpanningTree1 = new Queue<>();
        minimumSpanningTree1.enqueue(new Edge(0, 1, 1));
        minimumSpanningTree1.enqueue(new Edge(1, 2, 4));
        minimumSpanningTree1.enqueue(new Edge(2, 3, 3));
        minimumSpanningTree1.enqueue(new Edge(3, 4, 5));

        Edge edge1 = new Edge(0, 4, 5);

        double[] weightRange1 = exercise16.getWeightRangeToBeInMST(minimumSpanningTree1, edge1);
        StdOut.println("Weight range 1: " + weightRange1[0] + " to " + weightRange1[1] + " Expected: -Infinity to 4.999999");

        Queue<Edge> minimumSpanningTree2 = new Queue<>();
        minimumSpanningTree2.enqueue(new Edge(0, 1, 1));
        minimumSpanningTree2.enqueue(new Edge(1, 2, 4));
        minimumSpanningTree2.enqueue(new Edge(2, 3, 3));
        minimumSpanningTree2.enqueue(new Edge(3, 4, 5));

        Edge edge2 = new Edge(1, 3, 5);

        double[] weightRange2 = exercise16.getWeightRangeToBeInMST(minimumSpanningTree2, edge2);
        StdOut.println("Weight range 2: " + weightRange2[0] + " to " + weightRange2[1] + " Expected: -Infinity to 3.999999");

        Queue<Edge> minimumSpanningTree3 = new Queue<>();
        minimumSpanningTree3.enqueue(new Edge(0, 1, 1));
        minimumSpanningTree3.enqueue(new Edge(1, 2, 4));
        minimumSpanningTree3.enqueue(new Edge(2, 3, 3));
        minimumSpanningTree3.enqueue(new Edge(3, 4, 5));

        Edge edge3 = new Edge(1, 3, 2);

        double[] weightRange3 = exercise16.getWeightRangeToBeInMST(minimumSpanningTree3, edge3);
        StdOut.println("Weight range 3: " + weightRange3[0] + " to " + weightRange3[1] + " Expected: -Infinity to 3.999999");
    }

}
