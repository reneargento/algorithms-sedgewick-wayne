package chapter4.section4;

import chapter2.section4.PriorityQueueResize;
import chapter3.section4.SeparateChainingHashTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rene Argento on 01/12/17.
 */
// Based on https://en.wikipedia.org/wiki/K_shortest_path_routing#Algorithm
@SuppressWarnings("unchecked")
public class KShortestPaths {

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
