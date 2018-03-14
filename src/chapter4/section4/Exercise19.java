package chapter4.section4;

import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;
import util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 03/12/17.
 */
public class Exercise19 {

    private static final int CURRENCIES = 5;

    /**
     * rates.txt file content
     5
     USD 1      0.741  0.657  1.061  1.005
     EUR 1.349  1      0.888  1.433  1.366
     GBP 1.521  1.126  1      1.614  1.538
     CHF 0.942  0.698  0.619  1      0.953
     CAD 0.995  0.732  0.650  1.049  1
     */

    public String getLowestWeightCycle() {
        String filePath = Constants.FILES_PATH + Constants.RATES_FILE;
        String[] allValues = FileUtil.getAllStringsFromFile(filePath);

        if (allValues == null) {
            throw new IllegalArgumentException("File not found");
        }

        int valueId = 0;
        int vertices = Integer.parseInt(allValues[valueId++]);
        String[] name = new String[vertices];

        double[][] adjacencyMatrix = new double[vertices][vertices];

        for(int vertex = 0; vertex < vertices; vertex++) {
            name[vertex] = allValues[valueId++];

            for(int neighbor = 0; neighbor < vertices; neighbor++) {
                double rate = Double.parseDouble(allValues[valueId++]);
                adjacencyMatrix[vertex][neighbor] = -Math.log(rate);
            }
        }

        // There is no known efficient algorithm for finding the lowest cycle in a graph.
        // Such problem is NP-hard and can be reduced from the Hamiltonian Cycle problem:
        // Set each edge weight to −1. There is a cycle of weight at most −n iff the graph has a Hamiltonian Cycle.

        // Generate all possible cycles
        List<String> allCyclesOfLength2 = generateCycleCombinations(2, CURRENCIES - 1);
        List<String> allCyclesOfLength3 = generateCycleCombinations(3, CURRENCIES - 1);
        List<String> allCyclesOfLength4 = generateCycleCombinations(4, CURRENCIES - 1);
        List<String> allCyclesOfLength5 = generateCycleCombinations(5, CURRENCIES - 1);

        List<String> allCycles = new ArrayList<>();
        allCycles.addAll(allCyclesOfLength2);
        allCycles.addAll(allCyclesOfLength3);
        allCycles.addAll(allCyclesOfLength4);
        allCycles.addAll(allCyclesOfLength5);

        double bestArbitrageOpportunity = Double.POSITIVE_INFINITY;
        String currenciesForBestArbitrage = "";

        for(String cycle : allCycles) {
            char[] currentCycle = cycle.toCharArray();
            double sum = 0;

            for(char vertex = 0; vertex < currentCycle.length - 1; vertex++) {
                int vertexFrom = Character.getNumericValue((currentCycle[vertex]));
                int vertexTo = Character.getNumericValue((currentCycle[vertex + 1]));

                sum += adjacencyMatrix[vertexFrom][vertexTo];
            }

            if (sum < bestArbitrageOpportunity) {
                bestArbitrageOpportunity = sum;
                currenciesForBestArbitrage = cycle;
            }
        }

        StringBuilder bestArbitrage = new StringBuilder();

        for(int i = 0; i < currenciesForBestArbitrage.length() - 1; i++) {
            int currencyId = Character.getNumericValue(currenciesForBestArbitrage.charAt(i));
            int nextCurrencyId = Character.getNumericValue(currenciesForBestArbitrage.charAt(i + 1));

            double rateInNegativeLog = adjacencyMatrix[currencyId][nextCurrencyId];
            double rate = Math.exp(-rateInNegativeLog);

            bestArbitrage.append(name[currencyId]).append(" ").append(rate).append(" -> ").append(name[nextCurrencyId])
                    .append("\n");
        }

        return bestArbitrage.toString();
    }

    public List<String> generateCycleCombinations(int size, int maxValue) {
        List<String> combinations = new ArrayList<>();
        String currentCycle = "";

        generateCycles(combinations, currentCycle, 0, size, new HashSet<>(), maxValue);
        return combinations;
    }

    private void generateCycles(List<String> combinations, String currentCycle, int position, int size,
                                HashSet<Integer> verticesInCycle, int maxValue) {
        StringBuilder cycleCopy = new StringBuilder(currentCycle);

        if (position == size) {
            // Check if the cycle is composed only of unique vertices
            if (verticesInCycle.size() == size) {
                char initialVertex = cycleCopy.charAt(0);
                cycleCopy.append(initialVertex); // Complete cycle
                combinations.add(cycleCopy.toString());
            }
        } else {
            for(int vertex = 0; vertex <= maxValue; vertex++) {
                HashSet<Integer> copyHashSet = new HashSet<>();
                for(int vertexInCycle : verticesInCycle.keys()) {
                    copyHashSet.add(vertexInCycle);
                }

                copyHashSet.add(vertex);

                cycleCopy.replace(position, position + 1, String.valueOf(vertex));
                generateCycles(combinations, cycleCopy.toString(), position + 1, size, copyHashSet, maxValue);
            }
        }
    }

    public static void main(String[] args) {
        StdOut.println(new Exercise19().getLowestWeightCycle());
    }

}
