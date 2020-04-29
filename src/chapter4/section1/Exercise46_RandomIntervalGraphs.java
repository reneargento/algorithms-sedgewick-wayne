package chapter4.section1;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.List;

/**
 * Created by Rene Argento on 12/10/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for suggesting an improvement on the interval generation:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/141
@SuppressWarnings("unchecked")
public class Exercise46_RandomIntervalGraphs {

    public class IntervalGraph implements GraphInterface {
        private IntervalBinarySearchTree.Interval[] keys;
        private Graph graph;

        private SeparateChainingHashTable<IntervalBinarySearchTree.Interval, Integer> intervalToIdHashMap;

        public IntervalGraph(int vertices) {
            graph = new Graph(vertices);

            intervalToIdHashMap = new SeparateChainingHashTable<>();
            keys = new IntervalBinarySearchTree.Interval[vertices];
        }

        public int vertices() {
            return graph.vertices();
        }

        public int edges() {
            return graph.edges();
        }

        public void addEdge(IntervalBinarySearchTree.Interval interval1, IntervalBinarySearchTree.Interval interval2) {
            if (!intervalToIdHashMap.contains(interval1)) {
                int newVertexId = intervalToIdHashMap.size();

                intervalToIdHashMap.put(interval1, newVertexId);
                keys[newVertexId] = interval1;
            }
            if (!intervalToIdHashMap.contains(interval2)) {
                int newVertexId = intervalToIdHashMap.size();

                intervalToIdHashMap.put(interval2, newVertexId);
                keys[newVertexId] = interval2;
            }

            int vertexId1 = intervalToIdHashMap.get(interval1);
            int vertexId2 = intervalToIdHashMap.get(interval2);

            graph.addEdge(vertexId1, vertexId2);
        }

        public boolean contains(IntervalBinarySearchTree.Interval interval) {
            return intervalToIdHashMap.contains(interval);
        }

        public int index(IntervalBinarySearchTree.Interval interval) {
            return intervalToIdHashMap.get(interval);
        }

        public IntervalBinarySearchTree.Interval getIntervalById(int vertexId) {
            return keys[vertexId];
        }

        public Graph graph() {
            return graph;
        }

        @Override
        public Iterable<Integer> adjacent(int vertex) {
            return graph.adjacent(vertex);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex = 0; vertex < vertices(); vertex++) {
                IntervalBinarySearchTree.Interval intervalVertex = keys[vertex];
                stringBuilder.append("[").append(intervalVertex.min).append(" - ").append(intervalVertex.max).append("]: ");

                for(int neighbor : graph.adjacent(vertex)) {
                    IntervalBinarySearchTree.Interval neighborVertex = keys[neighbor];
                    stringBuilder.append("[").append(neighborVertex.min).append(" - ").append(neighborVertex.max).append("] ");
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public IntervalGraph generateIntervalGraph(int vertices, double length) {
        // Generate random endpoints
        IntervalBinarySearchTree.Interval[] randomIntervals = new IntervalBinarySearchTree.Interval[vertices];
        int randomIntervalsIndex = 0;

        IntervalBinarySearchTree<Integer> intervalBinarySearchTree = new IntervalBinarySearchTree();

        for(int randomVertex = 0; randomVertex < vertices; randomVertex++) {
            double randomIntervalStart = StdRandom.uniform(0, 1 - length);
            double randomIntervalEnd = randomIntervalStart + length;
            IntervalBinarySearchTree.Interval intervalVertex =
                    intervalBinarySearchTree.new Interval(randomIntervalStart, randomIntervalEnd);
            randomIntervals[randomIntervalsIndex++] = intervalVertex;
        }

        // Generate interval graph
        IntervalGraph intervalGraph = new IntervalGraph(vertices);

        for(int vertexId = 0; vertexId < randomIntervals.length; vertexId++) {
            IntervalBinarySearchTree.Interval randomInterval = randomIntervals[vertexId];

            List<IntervalBinarySearchTree.Interval> intervalsThatIntersect =
                    intervalBinarySearchTree.getAllIntersections(randomInterval);

            for(IntervalBinarySearchTree.Interval intervalIntersection : intervalsThatIntersect) {
                intervalGraph.addEdge(randomInterval, intervalIntersection);
            }

            intervalBinarySearchTree.put(randomInterval, vertexId);
        }

        return intervalGraph;
    }

    public static void main(String[] args) {
        // Arguments example: 10 0.3
        int vertices = Integer.parseInt(args[0]);
        double length = Double.parseDouble(args[1]);

        if (length < 0 || length >= 1) {
            throw new IllegalArgumentException("Intervals length must be higher than 0 and less than 1");
        }

        IntervalGraph intervalGraph = new Exercise46_RandomIntervalGraphs().generateIntervalGraph(vertices, length);
        StdOut.println(intervalGraph);
    }

}
