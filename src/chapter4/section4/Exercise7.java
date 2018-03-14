package chapter4.section4;

import chapter2.section4.PriorityQueueResize;
import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rene Argento on 30/11/17.
 */
@SuppressWarnings("unchecked")
public class Exercise7 {

    public class Path implements Comparable<Path> {

        private Path previousPath;
        private DirectedEdge directedEdge;
        private int lastVertexInPath;
        private double weight;
        private HashSet<Integer> verticesInPath;

        Path(int vertex) {
            lastVertexInPath = vertex;

            verticesInPath = new HashSet<>();
            verticesInPath.add(vertex);
        }

        Path(Path previousPath, DirectedEdge directedEdge) {
            this(directedEdge.to());
            this.previousPath = previousPath;

            verticesInPath.addAll(previousPath.verticesInPath);

            this.directedEdge = directedEdge;
            weight += previousPath.weight() + directedEdge.weight();
        }

        public double weight() {
            return weight;
        }

        public Iterable<DirectedEdge> getPath() {
            LinkedList<DirectedEdge> path = new LinkedList<>();

            Path iterator = previousPath;

            while (iterator != null && iterator.directedEdge != null) {
                path.addFirst(iterator.directedEdge);

                iterator = iterator.previousPath;
            }
            path.add(directedEdge);

            return path;
        }
        
        @Override
        public int compareTo(Path other) {
            if (this.weight < other.weight) {
                return -1;
            } else if (this.weight > other.weight) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public class KShortestPaths {

        public List<Path> getKShortestPaths(EdgeWeightedDigraph edgeWeightedDigraph, int source, int target, int kPaths) {

            List<Path> paths = new ArrayList<>();
            SeparateChainingHashTable<Integer, Integer> countMap = new SeparateChainingHashTable<>();
            countMap.put(target, 0);

            PriorityQueueResize<Path> priorityQueue = new PriorityQueueResize(PriorityQueueResize.Orientation.MIN);
            priorityQueue.insert(new Path(source));

            while (!priorityQueue.isEmpty() && countMap.get(target) < kPaths) {
                Path currentPath = priorityQueue.deleteTop();
                int lastVertexInPath = currentPath.lastVertexInPath;

                int pathsToCurrentVertex = 0;

                if (countMap.get(lastVertexInPath) != null) {
                    pathsToCurrentVertex = countMap.get(lastVertexInPath);
                }

                pathsToCurrentVertex++;
                countMap.put(lastVertexInPath, pathsToCurrentVertex);

                if (lastVertexInPath == target) {
                    paths.add(currentPath);
                }

                if (pathsToCurrentVertex <= kPaths) {
                    for(DirectedEdge edge : edgeWeightedDigraph.adjacent(lastVertexInPath)) {
                        // Do not repeat vertices - we are interested in paths and not walks
                        if (!currentPath.verticesInPath.contains(edge.to())) {
                            Path newPath = new Path(currentPath, edge);
                            priorityQueue.insert(newPath);
                        }
                    }
                }
            }

            return paths;
        }
    }

    public class DijkstraSecondSP {

        public Path getSecondShortestPath(EdgeWeightedDigraph edgeWeightedDigraph, int source, int target) {
            KShortestPaths kShortestPaths = new KShortestPaths();

            List<Path> twoShortestPaths = kShortestPaths.getKShortestPaths(edgeWeightedDigraph, source, target, 2);

            if (twoShortestPaths.size() == 2) {
                return twoShortestPaths.get(1);
            } else {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        DijkstraSecondSP dijkstraSecondSP = new Exercise7().new DijkstraSecondSP();

        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(8);
        edgeWeightedDigraph.addEdge(new DirectedEdge(4, 5, 0.35));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 4, 0.35));
        edgeWeightedDigraph.addEdge(new DirectedEdge(4, 7, 0.37));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 7, 0.28));
        edgeWeightedDigraph.addEdge(new DirectedEdge(7, 5, 0.28));
        edgeWeightedDigraph.addEdge(new DirectedEdge(5, 1, 0.32));
        edgeWeightedDigraph.addEdge(new DirectedEdge(0, 4, 0.38));
        edgeWeightedDigraph.addEdge(new DirectedEdge(2, 0, 0.26));
        edgeWeightedDigraph.addEdge(new DirectedEdge(7, 3, 0.39));
        edgeWeightedDigraph.addEdge(new DirectedEdge(1, 3, 0.29));
        edgeWeightedDigraph.addEdge(new DirectedEdge(2, 7, 0.34));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 2, 0.40));
        edgeWeightedDigraph.addEdge(new DirectedEdge(3, 6, 0.52));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 0, 0.58));
        edgeWeightedDigraph.addEdge(new DirectedEdge(6, 4, 0.93));

        StdOut.println("Second shortest path from 2 to 4");
        Path secondShortestPath1 = dijkstraSecondSP.getSecondShortestPath(edgeWeightedDigraph, 2, 4);

        if (secondShortestPath1 == null) {
            StdOut.println("There is only one shortest path from 2 to 4");
        } else {
            for(DirectedEdge directedEdge : secondShortestPath1.getPath()) {
                StdOut.print(directedEdge + " ");
            }
        }
        StdOut.println("\nExpected: 2->7 0.34 7->5 0.28 5->4 0.35");

        StdOut.println("\nSecond shortest path from 6 to 5");
        Path secondShortestPath2 = dijkstraSecondSP.getSecondShortestPath(edgeWeightedDigraph, 6, 5);

        if (secondShortestPath2 == null) {
            StdOut.println("There is only one shortest path from 6 to 5");
        } else {
            for(DirectedEdge directedEdge : secondShortestPath2.getPath()) {
                StdOut.print(directedEdge + " ");
            }
        }
        StdOut.println("\nExpected: 6->4 0.93 4->5 0.35");

        StdOut.println("\nSecond shortest path from 6 to 2");
        Path secondShortestPath3 = dijkstraSecondSP.getSecondShortestPath(edgeWeightedDigraph, 6, 2);

        if (secondShortestPath3 == null) {
            StdOut.println("There is only one shortest path from 6 to 2");
        } else {
            for(DirectedEdge directedEdge : secondShortestPath3.getPath()) {
                StdOut.print(directedEdge + " ");
            }
        }
        StdOut.println("Expected: There is only one shortest path from 6 to 2");
    }

}
