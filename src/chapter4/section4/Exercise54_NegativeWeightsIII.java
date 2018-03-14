package chapter4.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 29/12/17.
 */
// An algorithm for counting the number of cycles in a graph has an exponential runtime in the worst case,
// so the range of values possible for the experiments is:
    // Vertices: Between 10 and 100
    // Edges: Between 1V and 1.7V, or 10 and 170
public class Exercise54_NegativeWeightsIII {

    private List<EdgeWeightedDigraphInterface> generateDigraphsWithNotManyNegativeCycles(int digraphsOfEachConfiguration) {
        int[] verticesCount = {10, 20, 50, 100};
        // E = V * edgesMultiplier[i]
        double[] edgesMultiplier = {1, 1.5, 1.7};

        int largePercentageOfNegativeWeights = 60;
        int acceptableNumberOfNegativeCycles = 10;

        List<EdgeWeightedDigraphInterface> digraphsWithNotManyNegativeCycles = new ArrayList<>();

        Exercise53_NegativeWeightsII.RandomEdgeWeightedDigraphsGenerator randomEdgeWeightedDigraphsGenerator =
                new Exercise53_NegativeWeightsII().new RandomEdgeWeightedDigraphsGenerator();

        for(int vertexCountIndex = 0; vertexCountIndex < verticesCount.length; vertexCountIndex++) {
            for(int edgesMultiplierIndex = 0; edgesMultiplierIndex < edgesMultiplier.length; edgesMultiplierIndex++) {
                int vertices = verticesCount[vertexCountIndex];
                int edges = (int) (vertices * edgesMultiplier[edgesMultiplierIndex]);

                for(int digraph = 0; digraph < digraphsOfEachConfiguration; digraph++) {

                    // Generate one random edge weighted digraph at a time
                    List<EdgeWeightedDigraphInterface> randomEdgeWeightedDigraphs =
                            randomEdgeWeightedDigraphsGenerator.randomEdgeWeightedDigraphsGenerator(1,
                                    vertices, edges, largePercentageOfNegativeWeights);

                    EdgeWeightedDigraphInterface randomEdgeWeightedDigraph = randomEdgeWeightedDigraphs.get(0);

                    // Count negative cycles
                    int numberOfNegativeCycles = countNegativeCycles(randomEdgeWeightedDigraph);
                    if (numberOfNegativeCycles <= acceptableNumberOfNegativeCycles) {
                        digraphsWithNotManyNegativeCycles.add(randomEdgeWeightedDigraph);
                    } else {
                        // Too many negative cycles in the digraph. Try again.
                        digraph--;
                    }
                }
            }
        }

        return digraphsWithNotManyNegativeCycles;
    }

    // There is no known efficient algorithm for counting all negative cycles, since such algorithm would also
    // solve the Hamiltonian Cycle problem.
    private int countNegativeCycles(EdgeWeightedDigraphInterface edgeWeightedDigraph) {
        int negativeCycles = 0;

        JohnsonAllCycles johnsonAllCycles = new JohnsonAllCycles();
        johnsonAllCycles.findAllCycles(edgeWeightedDigraph);

        List<List<DirectedEdge>> allCyclesInDigraph = johnsonAllCycles.getAllCyclesByEdges();

        for(List<DirectedEdge> cycle : allCyclesInDigraph) {
            double totalWeight = 0;

            for(DirectedEdge edge : cycle) {
                totalWeight += edge.weight();
            }

            if (totalWeight < 0) {
                negativeCycles++;
            }
        }

        return negativeCycles;
    }

    // Parameter example: 10
    public static void main(String[] args) {
        int digraphsOfEachConfiguration = Integer.parseInt(args[0]);

        List<EdgeWeightedDigraphInterface> digraphsWithNotManyNegativeCycles =
                new Exercise54_NegativeWeightsIII().generateDigraphsWithNotManyNegativeCycles(digraphsOfEachConfiguration);

        EdgeWeightedDigraphInterface firstDigraphWithNotManyNegativeCycles = digraphsWithNotManyNegativeCycles.get(0);

        StdOut.println("Random edge-weighted-digraph with not many negative cycles:");
        StdOut.println(firstDigraphWithNotManyNegativeCycles);
    }
}
