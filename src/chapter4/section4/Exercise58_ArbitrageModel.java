package chapter4.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 31/12/17.
 */
public class Exercise58_ArbitrageModel {

    private class ArbitrageProblem {
        private double[][] conversionRates;
        private String[] currencyNames;

        ArbitrageProblem(double[][] conversionRates, String[] currencyNames) {
            this.conversionRates = conversionRates;
            this.currencyNames = currencyNames;
        }

        public double[][] getConversionRates() {
            return conversionRates;
        }

        public String[] getCurrencyNames() {
            return currencyNames;
        }
    }

    // Guarantee 4 precision points in the arithmetic operations
    private static final int INTEGER_VALUE_TO_MULTIPLY = 10000;

    public ArbitrageProblem generateArbitrageProblem(int numberOfCurrencies, int currencyNamesLength,
                                                     double minConversionRate, double maxConversionRate) {

        double[][] conversionRates = new double[numberOfCurrencies][numberOfCurrencies];
        String[] currencyNames = new String[numberOfCurrencies];

        for(int currency1 = 0; currency1 < numberOfCurrencies; currency1++) {
            currencyNames[currency1] = generateRandomCurrencyName(currencyNamesLength);

            conversionRates[currency1][currency1] = 1;

            for(int currency2 = currency1 + 1; currency2 < numberOfCurrencies; currency2++) {
                double randomConversionRate = StdRandom.uniform(minConversionRate, maxConversionRate);
                conversionRates[currency1][currency2] = randomConversionRate;

                double inverseConversionRate = 1 / randomConversionRate;
                conversionRates[currency2][currency1] = inverseConversionRate;
            }
        }

        return new ArbitrageProblem(conversionRates, currencyNames);
    }

    private String generateRandomCurrencyName(int currencyNameLength) {
        StringBuilder currencyName = new StringBuilder();

        for(int letter = 0; letter < currencyNameLength; letter++) {
            char randomChar = (char) StdRandom.uniform(Constants.ASC_II_UPPERCASE_LETTERS_INITIAL_INDEX,
                    Constants.ASC_II_UPPERCASE_LETTERS_FINAL_INDEX + 1);
            currencyName.append(randomChar);
        }

        return currencyName.toString();
    }

    private void printArbitrageProblem(ArbitrageProblem arbitrageProblem) {
        if (arbitrageProblem == null) {
            return;
        }

        StringBuilder arbitrageTable = new StringBuilder();
        String[] currencyNames = arbitrageProblem.getCurrencyNames();
        int currencyNamesLength = currencyNames[0].length();

        int leftColumnSpaceFormat = currencyNamesLength;
        int currencyNameSpaceFormat = currencyNamesLength + 6;
        int conversionRateSpaceFormat = 10;

        // Initial cell is blank
        arbitrageTable.append(String.format("%" + leftColumnSpaceFormat + "s", ""));

        // First line
        for (String currencyName : arbitrageProblem.getCurrencyNames()) {
            arbitrageTable.append(String.format("%" + currencyNameSpaceFormat + "s", currencyName));
        }

        arbitrageTable.append("\n");

        double[][] conversionRates = arbitrageProblem.getConversionRates();

        for (int currency1 = 0; currency1 < conversionRates.length; currency1++) {
            arbitrageTable.append(String.format("%-" + leftColumnSpaceFormat + "s", currencyNames[currency1]));

            for (int currency2 = 0; currency2 < conversionRates.length; currency2++) {
                arbitrageTable.append(String.format("%" + conversionRateSpaceFormat + ".4f",
                        conversionRates[currency1][currency2]));
            }

            arbitrageTable.append("\n");
        }

        StdOut.println(arbitrageTable.toString());
    }

    public List<DirectedEdge> findAnArbitrageOpportunity(ArbitrageProblem arbitrageProblem) {
        double[][] conversionRates = arbitrageProblem.getConversionRates();

        EdgeWeightedDigraph edgeWeightedDigraph = getDigraphFromConversionRatesTable(conversionRates);

        BellmanFordSP bellmanFordSP = new BellmanFordSP(edgeWeightedDigraph, 0);

        if (!bellmanFordSP.hasNegativeCycle()) {
            return null;
        }

        List<DirectedEdge> arbitrageOpportunity = new ArrayList<>();

        for(DirectedEdge edge : bellmanFordSP.negativeCycle()) {
            arbitrageOpportunity.add(edge);
        }

        return arbitrageOpportunity;
    }

    public List<DirectedEdge> findTheBestArbitrageOpportunity(ArbitrageProblem arbitrageProblem) {

        double[][] conversionRates = arbitrageProblem.getConversionRates();

        EdgeWeightedDigraph edgeWeightedDigraph = getDigraphFromConversionRatesTable(conversionRates);

        JohnsonAllCycles johnsonAllCycles = new JohnsonAllCycles();
        johnsonAllCycles.findAllCycles(edgeWeightedDigraph);

        List<List<DirectedEdge>> allCyclesInDigraph = johnsonAllCycles.getAllCyclesByEdges();

        List<DirectedEdge> bestArbitrageOpportunity = new ArrayList<>();
        double smallestWeightInAnyCycle = 0;

        for(List<DirectedEdge> cycle : allCyclesInDigraph) {
            double totalWeight = 0;

            for(DirectedEdge edge : cycle) {
                totalWeight += edge.weight();
            }

            if (totalWeight < smallestWeightInAnyCycle) {
                smallestWeightInAnyCycle = totalWeight;
                bestArbitrageOpportunity = cycle;
            }
        }

        if (smallestWeightInAnyCycle == 0) {
            return null;
        }
        return bestArbitrageOpportunity;
    }

    private EdgeWeightedDigraph getDigraphFromConversionRatesTable(double[][] conversionRates) {
        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(conversionRates.length);

        for(int currency1 = 0; currency1 < conversionRates.length; currency1++) {

            for(int currency2 = 0; currency2 < conversionRates.length; currency2++) {
                double conversionRate = conversionRates[currency1][currency2];

                double negativeLn = -Math.log(conversionRate);
                // Convert to int to avoid floating point arithmetic issues.
                // Not doing this conversion can lead to negative cycles such as Currency A -> Currency B -> Currency A
                // due to floating point imprecision.
                int integerConversionRate = (int) Math.round(negativeLn * INTEGER_VALUE_TO_MULTIPLY);

                DirectedEdge edge = new DirectedEdge(currency1, currency2, integerConversionRate);
                edgeWeightedDigraph.addEdge(edge);
            }
        }

        return edgeWeightedDigraph;
    }

    // Parameters example: 10 4 0.001 4 1000 3
    public static void main(String[] args) {
        Exercise58_ArbitrageModel arbitrageModel = new Exercise58_ArbitrageModel();

        int numberOfCurrencies = Integer.parseInt(args[0]);
        int currencyNamesLength = Integer.parseInt(args[1]);
        double minConversionRate = Double.parseDouble(args[2]);
        double maxConversionRate = Double.parseDouble(args[3]);

        double initialInvestment = Double.parseDouble(args[4]);
        int numberOfProblemsToGenerate = Integer.parseInt(args[5]);

        for(int problem = 1; problem <= numberOfProblemsToGenerate; problem++) {
            StdOut.println("Arbitrage problem " + problem + ":\n");

            ArbitrageProblem arbitrageProblem = arbitrageModel.generateArbitrageProblem(numberOfCurrencies,
                    currencyNamesLength, minConversionRate, maxConversionRate);

            arbitrageModel.printArbitrageProblem(arbitrageProblem);

            List<DirectedEdge> arbitrageOpportunity = arbitrageModel.findAnArbitrageOpportunity(arbitrageProblem);
            StdOut.println("Arbitrage opportunity " + problem + ":");
            arbitrageModel.printArbitrageOpportunity(arbitrageProblem, initialInvestment, arbitrageOpportunity);

            List<DirectedEdge> bestArbitrageOpportunity = arbitrageModel.findTheBestArbitrageOpportunity(arbitrageProblem);
            StdOut.println("\nBest arbitrage opportunity " + problem + ":");
            arbitrageModel.printArbitrageOpportunity(arbitrageProblem, initialInvestment, bestArbitrageOpportunity);
            StdOut.println();
        }
    }

    private void printArbitrageOpportunity(ArbitrageProblem arbitrageProblem, double initialInvestment,
                                           List<DirectedEdge> arbitrageOpportunity) {
        if (arbitrageOpportunity != null) {
            String[] currencyNames = arbitrageProblem.getCurrencyNames();
            double[][] conversionRates = arbitrageProblem.getConversionRates();
            StringJoiner stringJoiner = new StringJoiner(" ");

            double totalMoney = initialInvestment;

            for(DirectedEdge edge : arbitrageOpportunity) {
                String currencies = currencyNames[edge.from()] + "->" + currencyNames[edge.to()];
                stringJoiner.add(currencies);

                // Instead of converting the edge weight back to the original value, get the value directly from
                // the conversion rates table to avoid losses in floating point precision
                totalMoney *= roundValuePrecisionDigits(conversionRates[edge.from()][edge.to()]);
            }

            StdOut.println(stringJoiner);
            StdOut.printf("Initial investment of %.2f becomes %.2f\n", initialInvestment, totalMoney);
        } else {
            StdOut.println("No arbitrage opportunity");
        }
    }

    private double roundValuePrecisionDigits(double value) {
        return (double) Math.round(value * INTEGER_VALUE_TO_MULTIPLY) / INTEGER_VALUE_TO_MULTIPLY;
    }

}
