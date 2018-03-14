package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Rene Argento on 24/10/17.
 */
public class Exercise31_StrongComponent {

    //O(sc * (V + E)) where sc is the number of strong components
    public List<Set<Integer>> getAllStrongComponents(Digraph digraph) {
        List<Set<Integer>> strongComponents = new ArrayList<>();
        Set<Integer> verticesAlreadyInAnotherSCC = new HashSet<>();

        // For every vertex that is not yet in a strong component, compute its strong component
        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            if (verticesAlreadyInAnotherSCC.contains(vertex)) {
                continue;
            }

            Set<Integer> strongComponent = getStrongComponent(digraph, vertex, verticesAlreadyInAnotherSCC);
            strongComponents.add(strongComponent);

            verticesAlreadyInAnotherSCC.addAll(strongComponent);
        }

        return strongComponents;
    }

    //O(V + E)
    public Set<Integer> getStrongComponent(Digraph digraph, int sourceVertex, Set<Integer> verticesAlreadyInAnotherSCC) {
        Set<Integer> strongComponent = new HashSet<>();
        strongComponent.add(sourceVertex);

        // 1- Find vertices reachable from the source vertex
        Set<Integer> verticesReachableFromSource = new HashSet<>();
        dfsFromSource(digraph, verticesReachableFromSource, sourceVertex);

        //Optimization: Used to avoid visiting the same vertex more than once and to achieve O(V + E) performance
        Set<Integer> verticesThatCannotReachSource = new HashSet<>();

        // 2- Find vertices that can reach the source vertex
        for(int vertexReachableFromSource : verticesReachableFromSource) {
            if (vertexReachableFromSource == sourceVertex) {
                continue;
            }

            Set<Integer> visited = new HashSet<>();
            boolean canReachStrongComponent = dfsToReachStrongComponent(digraph, visited, vertexReachableFromSource,
                    strongComponent, verticesAlreadyInAnotherSCC, verticesThatCannotReachSource);

            // 3- Vertices in the intersection of the two sets are in the strong component
            if (canReachStrongComponent) {
                strongComponent.add(vertexReachableFromSource);
            } else {
                verticesThatCannotReachSource.addAll(visited);
            }
        }

        return strongComponent;
    }

    private void dfsFromSource(Digraph digraph, Set<Integer> verticesReached, int vertex) {
        verticesReached.add(vertex);

        for(int neighbor : digraph.adjacent(vertex)) {
            if (!verticesReached.contains(neighbor)) {
                dfsFromSource(digraph, verticesReached, neighbor);
            }
        }
    }

    private boolean dfsToReachStrongComponent(Digraph digraph, Set<Integer> visited, int vertex,
                                           Set<Integer> currentStrongComponent, Set<Integer> verticesAlreadyInAnotherSCC,
                                              Set<Integer> verticesThatCannotReachSource) {
        if (currentStrongComponent.contains(vertex)) {
            return true;
        }

        visited.add(vertex);

        for(int neighbor : digraph.adjacent(vertex)) {
            if (!visited.contains(neighbor)
                    && !verticesAlreadyInAnotherSCC.contains(neighbor)
                    && !verticesThatCannotReachSource.contains(neighbor)) {
                boolean reachable = dfsToReachStrongComponent(digraph, visited, neighbor,
                        currentStrongComponent, verticesAlreadyInAnotherSCC, verticesThatCannotReachSource);
                if (reachable) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        Exercise31_StrongComponent strongComponent = new Exercise31_StrongComponent();

        StdOut.print("Strong component 1: ");

        Digraph digraph1 = new Digraph(5);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(2, 0);
        digraph1.addEdge(1, 3);
        digraph1.addEdge(3, 1);
        digraph1.addEdge(1, 4);
        digraph1.addEdge(4, 3);

        Set<Integer> strongComponent1 = strongComponent.getStrongComponent(digraph1, 2, new HashSet<>());
        for(int vertexInStrongComponent : strongComponent1) {
            StdOut.print(vertexInStrongComponent + " ");
        }
        StdOut.println("\nExpected: 0 1 2 3 4");

        StdOut.print("Strong component 2: ");

        Digraph digraph2 = new Digraph(5);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(1, 2);
        digraph2.addEdge(2, 0);
        digraph2.addEdge(1, 3);
        digraph2.addEdge(3, 4);
        digraph2.addEdge(4, 3);

        Set<Integer> strongComponent2 = strongComponent.getStrongComponent(digraph2, 2, new HashSet<>());
        for(int vertexInStrongComponent : strongComponent2) {
            StdOut.print(vertexInStrongComponent + " ");
        }
        StdOut.println("\nExpected: 0 1 2");

        StdOut.print("Strong component 3: ");

        Digraph digraphWorstCase = new Digraph(5);
        digraphWorstCase.addEdge(0, 1);
        digraphWorstCase.addEdge(1, 2);
        digraphWorstCase.addEdge(2, 3);
        digraphWorstCase.addEdge(3, 4);

        Set<Integer> strongComponent3 = strongComponent.getStrongComponent(digraphWorstCase, 0, new HashSet<>());
        for(int vertexInStrongComponent : strongComponent3) {
            StdOut.print(vertexInStrongComponent + " ");
        }
        StdOut.println("\nExpected: 0");


        StdOut.println("\nAll strong components 1:");

        List<Set<Integer>> strongComponents1 = strongComponent.getAllStrongComponents(digraph1);
        for(Set<Integer> currentStrongComponent : strongComponents1) {
            for(int vertexInStrongComponent : currentStrongComponent) {
                StdOut.print(vertexInStrongComponent + " ");
            }
            StdOut.println();
        }
        StdOut.println("Expected: 0 1 2 3 4");


        StdOut.println("All strong components 2:");

        List<Set<Integer>> strongComponents2 = strongComponent.getAllStrongComponents(digraph2);
        for(Set<Integer> currentStrongComponent : strongComponents2) {
            for(int vertexInStrongComponent : currentStrongComponent) {
                StdOut.print(vertexInStrongComponent + " ");
            }
            StdOut.println();
        }
        StdOut.println("Expected: \n0 1 2 \n3 4");


        StdOut.println("All strong components 3:");

        List<Set<Integer>> strongComponents3 = strongComponent.getAllStrongComponents(digraphWorstCase);
        for(Set<Integer> currentStrongComponent : strongComponents3) {
            for(int vertexInStrongComponent : currentStrongComponent) {
                StdOut.print(vertexInStrongComponent + " ");
            }
            StdOut.println();
        }
        StdOut.println("Expected: \n0 \n1 \n2 \n3 \n4");
    }

}
