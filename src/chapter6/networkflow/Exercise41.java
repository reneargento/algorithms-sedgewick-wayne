package chapter6.networkflow;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 30/09/18.
 */
public class Exercise41 {

    private final int MAX_VALUE = (int) Math.pow(2, 20);

    interface RandomCapacity {
        int getRandomCapacity();
    }

    class RandomUniformCapacity implements RandomCapacity {
        public int getRandomCapacity() {
            return StdRandom.uniform(MAX_VALUE);
        }
    }

    class RandomGaussianCapacity implements RandomCapacity {
        int median = MAX_VALUE / 2;
        double sigma = median / 6.0;

        public int getRandomCapacity() {
            return (int) StdRandom.gaussian(median, sigma);
        }
    }

    class VertexPair {
        int vertex1;
        int vertex2;

        VertexPair(int vertex1, int vertex2) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
        }
    }

    public class RandomNetworkGenerator {

        public FlowNetwork generateRandomUniformCapacityNetwork(int vertices, int edges) {
            validateInput(vertices, edges);

            FlowNetwork flowNetwork = new FlowNetwork(vertices);

            if (vertices == 1) {
                return flowNetwork;
            }

            RandomUniformCapacity randomUniformCapacity = new RandomUniformCapacity();

            for (int i = 0; i < edges; i++) {
                VertexPair vertexPair = getRandomVerticesPair(vertices);
                int randomCapacity = randomUniformCapacity.getRandomCapacity();
                flowNetwork.addEdge(new FlowEdge(vertexPair.vertex1, vertexPair.vertex2, randomCapacity));
            }

            return flowNetwork;
        }

        public FlowNetwork generateRandomGaussianCapacityNetwork(int vertices, int edges) {
            validateInput(vertices, edges);

            FlowNetwork flowNetwork = new FlowNetwork(vertices);

            if (vertices == 1) {
                return flowNetwork;
            }

            RandomGaussianCapacity randomGaussianCapacity = new RandomGaussianCapacity();

            for (int i = 0; i < edges; i++) {
                VertexPair vertexPair = getRandomVerticesPair(vertices);
                int randomCapacity = randomGaussianCapacity.getRandomCapacity();
                flowNetwork.addEdge(new FlowEdge(vertexPair.vertex1, vertexPair.vertex2, randomCapacity));
            }

            return flowNetwork;
        }

        private void validateInput(int vertices, int edges) {
            if (vertices < 0) {
                throw new IllegalArgumentException("Network flow cannot have a negative number of vertices");
            }
            if (edges < 0) {
                throw new IllegalArgumentException("Network flow cannot have a negative number of edges");
            }
        }

        public VertexPair getRandomVerticesPair(int vertices) {
            int vertex1 = StdRandom.uniform(vertices);
            int vertex2 = StdRandom.uniform(vertices);

            while (vertex1 == vertex2) {
                vertex1 = StdRandom.uniform(vertices);
                vertex2 = StdRandom.uniform(vertices);
            }

            return new VertexPair(vertex1, vertex2);
        }
    }

    // Sparse graph configurations
    private int[] vertexValues = {20, 100, 500, 800};
    private int[] edgeValues = {20, 100, 500, 800};

    public FlowNetwork[] getRandomUniformCapacityNetworks() {
        FlowNetwork[] flowNetworks = new FlowNetwork[4];
        RandomNetworkGenerator randomNetworkGenerator = new RandomNetworkGenerator();

        for (int i = 0; i < flowNetworks.length; i++) {
            flowNetworks[i] =
                    randomNetworkGenerator.generateRandomUniformCapacityNetwork(vertexValues[i], edgeValues[i]);
        }

        return flowNetworks;
    }

    public FlowNetwork[] getRandomGaussianCapacityNetworks() {
        FlowNetwork[] flowNetworks = new FlowNetwork[4];
        RandomNetworkGenerator randomNetworkGenerator = new RandomNetworkGenerator();

        for (int i = 0; i < flowNetworks.length; i++) {
            flowNetworks[i] =
                    randomNetworkGenerator.generateRandomGaussianCapacityNetwork(vertexValues[i], edgeValues[i]);
        }

        return flowNetworks;
    }

    public static void main(String[] args) {
        Exercise41 exercise41 = new Exercise41();

        FlowNetwork[] randomUniformCapacityNetworks = exercise41.getRandomUniformCapacityNetworks();
        FlowNetwork[] randomGaussianCapacityNetworks = exercise41.getRandomGaussianCapacityNetworks();

        StdOut.println("*** Random uniform capacity networks generated ***");
        exercise41.printFlowNetworks(randomUniformCapacityNetworks);

        StdOut.println("\n\n*** Random gaussian capacity networks generated ***");
        exercise41.printFlowNetworks(randomGaussianCapacityNetworks);
    }

    private void printFlowNetworks(FlowNetwork[] flowNetworks) {
        StdOut.println("Number of flow networks: " + flowNetworks.length + "\n");

        for (int i = 0; i < flowNetworks.length; i++) {
            if (i > 0) {
                StdOut.println();
            }

            StdOut.println("Configuration " + (i + 1));
            StdOut.println("Vertices: " + flowNetworks[i].vertices());
            StdOut.println("Edges: " + flowNetworks[i].edgesCount());
        }
    }

}
