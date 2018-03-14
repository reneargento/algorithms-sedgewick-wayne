package chapter4.section4;

import chapter1.section3.Queue;
import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

/**
 * Created by Rene Argento on 02/12/17.
 */
public class Exercise18 {

    private class Path implements Comparable<Path> {

        private Path previousPath;
        private DirectedEdge directedEdge;
        private double weight;
        private int lastVertexInPath;

        Path(int vertex) {
            this.lastVertexInPath = vertex;
        }

        Path(Path previousPath, DirectedEdge directedEdge) {
            this(directedEdge.to());
            this.previousPath = previousPath;
            this.directedEdge = directedEdge;

            weight += previousPath.weight() + directedEdge.weight();
        }

        public double weight() {
            return weight;
        }

        public Iterable<DirectedEdge> getPath() {
            LinkedList<DirectedEdge> path = new LinkedList<>();

            Path currentPreviousPath = previousPath;

            while (currentPreviousPath != null && currentPreviousPath.directedEdge != null) {
                path.addFirst(currentPreviousPath.directedEdge);

                currentPreviousPath = currentPreviousPath.previousPath;
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

    public class CriticalPathMethod {

        public void printAllCriticalPaths(String[] precedenceConstraints) {
            int jobs = precedenceConstraints.length;

            EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(2 * jobs + 2);

            int source = 2 * jobs;
            int target = 2 * jobs + 1;

            for(int job = 0; job < jobs; job++) {
                String[] jobInformation = precedenceConstraints[job].split("\\s+");
                double duration = Double.parseDouble(jobInformation[0]);

                edgeWeightedDigraph.addEdge(new DirectedEdge(job, job + jobs, duration));
                edgeWeightedDigraph.addEdge(new DirectedEdge(source, job, 0));
                edgeWeightedDigraph.addEdge(new DirectedEdge(job + jobs, target, 0));

                for(int successors = 1; successors < jobInformation.length; successors++) {
                    int successor = Integer.parseInt(jobInformation[successors]);
                    edgeWeightedDigraph.addEdge(new DirectedEdge(job + jobs, successor, 0));
                }
            }

            // Check if there is any cycle
            EdgeWeightedDirectedCycle edgeWeightedDirectedCycle = new EdgeWeightedDirectedCycle(edgeWeightedDigraph);
            if (edgeWeightedDirectedCycle.hasCycle()) {
                throw new IllegalArgumentException("The jobs graph cannot contain cycles.");
            }

            PriorityQueueResize<Path> allPaths = getAllPaths(edgeWeightedDigraph, target);
            printAllCriticalPaths(allPaths, target);
        }

        //O((E + V) * lg P), where P is the number of paths in the graph
        private PriorityQueueResize<Path> getAllPaths(EdgeWeightedDigraph edgeWeightedDigraph, int target) {
            Topological topological = new Topological(edgeWeightedDigraph);
            boolean isFirstVertexInTopologicalOrder = true;

            PriorityQueueResize<Path> allPaths = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MAX);
            Queue<Path> queue = new Queue<>();

            for(int vertex : topological.order()) {
                if (isFirstVertexInTopologicalOrder) {
                    queue.enqueue(new Path(vertex));
                    isFirstVertexInTopologicalOrder = false;
                } else {
                    Queue<Path> newPaths = new Queue<>();

                    while (!queue.isEmpty()) {
                        Path currentPath = queue.dequeue();

                        for(DirectedEdge edge : edgeWeightedDigraph.adjacent(currentPath.lastVertexInPath)) {
                            Path newPath = new Path(currentPath, edge);
                            newPaths.enqueue(newPath);

                            if (edge.to() == target) {
                                allPaths.insert(newPath);
                            }
                        }
                    }

                    for(Path newPath : newPaths) {
                        queue.enqueue(newPath);
                    }
                }
            }

            return allPaths;
        }

        private void printAllCriticalPaths(PriorityQueueResize<Path> allPaths, int target) {
            if (allPaths == null || allPaths.isEmpty()) {
                return;
            }

            double longestPathWeight = allPaths.peek().weight();

            while (!allPaths.isEmpty() && allPaths.peek().weight() == longestPathWeight) {
                Path criticalPath = allPaths.deleteTop();

                boolean addArrowInPath = false;

                for(DirectedEdge edge : criticalPath.getPath()) {
                    // Avoid printing extra vertices
                    if (edge.from() < edge.to() && edge.to() != target) {
                        if (addArrowInPath) {
                            StdOut.print("->");
                        } else {
                            addArrowInPath = true;
                        }

                        StdOut.print(edge.from());
                    }
                }

                StdOut.println();
            }
        }
    }

    public static void main(String[] args) {

        // Graph with two critical paths: 0->1->2->3 and 0->4->5
        // Both with total duration of 35
        String[] precedenceConstraints = new String[6];
        precedenceConstraints[0] = "10 1 4";
        precedenceConstraints[1] = "15 2";
        precedenceConstraints[2] = "5 3";
        precedenceConstraints[3] = "5";
        precedenceConstraints[4] = "20 5";
        precedenceConstraints[5] = "5";

        CriticalPathMethod criticalPathMethod = new Exercise18().new CriticalPathMethod();
        criticalPathMethod.printAllCriticalPaths(precedenceConstraints);

        StdOut.println("\nExpected: \n0->4->5\n0->1->2->3");
    }

}
