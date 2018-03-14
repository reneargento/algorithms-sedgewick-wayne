package chapter4.section4;

import edu.princeton.cs.algs4.StdOut;
import util.Constants;
import util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 03/12/17.
 */
public class Exercise20 {

    private static final int CURRENCIES = 5;

    /**
     * 4.4.20_rates.txt file content
     5
     USD 1.00000 0.84279 0.74426 64.5471 1.31644
     EUR 1.18653 1.00000 0.88309 76.5871 1.56199
     GBP 1.34361 1.13239 1.00000 86.7263 1.76878
     INR 0.01549 0.01306 0.01153 1.00000 0.02039
     AUD 0.75963 0.64021 0.56536 49.0318 1.00000
     */

    public void printAllArbitrageOpportunities() {
        String filePath = Constants.FILES_PATH + Constants.REAL_WORLD_RATES_FILE;
        String[] allValues = FileUtil.getAllStringsFromFile(filePath);

        if (allValues == null) {
            throw new IllegalArgumentException("File not found");
        }

        int valueId = 0;
        int vertices = Integer.parseInt(allValues[valueId++]);
        String[] name = new String[vertices];

        double[][] adjacencyMatrix = new double[vertices][vertices];

        for (int vertex = 0; vertex < vertices; vertex++) {
            name[vertex] = allValues[valueId++];

            for(int neighbor = 0; neighbor < vertices; neighbor++) {
                double rate = Double.parseDouble(allValues[valueId++]);
                adjacencyMatrix[vertex][neighbor] = -Math.log(rate);
            }
        }

        Exercise19 exercise19 = new Exercise19();

        // Generate all possible cycles
        List<String> allCyclesOfLength2 = exercise19.generateCycleCombinations(2, CURRENCIES - 1);
        List<String> allCyclesOfLength3 = exercise19.generateCycleCombinations(3, CURRENCIES - 1);
        List<String> allCyclesOfLength4 = exercise19.generateCycleCombinations(4, CURRENCIES - 1);
        List<String> allCyclesOfLength5 = exercise19.generateCycleCombinations(5, CURRENCIES - 1);

        List<String> allCycles = new ArrayList<>();
        allCycles.addAll(allCyclesOfLength2);
        allCycles.addAll(allCyclesOfLength3);
        allCycles.addAll(allCyclesOfLength4);
        allCycles.addAll(allCyclesOfLength5);

        for(String cycle : allCycles) {
            char[] currentCycle = cycle.toCharArray();
            double sum = 0;

            for(char vertex = 0; vertex < currentCycle.length - 1; vertex++) {
                int vertexFrom = Character.getNumericValue((currentCycle[vertex]));
                int vertexTo = Character.getNumericValue((currentCycle[vertex + 1]));

                sum += adjacencyMatrix[vertexFrom][vertexTo];
            }

            if (sum < 0) {
                for(int i = 0; i < cycle.length() - 1; i++) {
                    int currencyId = Character.getNumericValue(cycle.charAt(i));
                    int nextCurrencyId = Character.getNumericValue(cycle.charAt(i + 1));

                    double rateInNegativeLog = adjacencyMatrix[currencyId][nextCurrencyId];
                    double rate = Math.exp(-rateInNegativeLog);

                    StdOut.printf("%s %.5f -> %s\n", name[currencyId], rate, name[nextCurrencyId]);
                }

                StdOut.println();
            }
        }
    }

    public static void main(String[] args) {
        new Exercise20().printAllArbitrageOpportunities();
    }

}
