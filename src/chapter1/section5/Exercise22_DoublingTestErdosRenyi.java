package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 09/12/16.
 */
public class Exercise22_DoublingTestErdosRenyi {

    private class Experiment {

        int numberOfSites;
        int numberOfPairsGenerated;
        double ratioOfRunningTimeToPrevious;

        public Experiment(int numberOfSites, int numberOfPairsGenerated, double ratioOfRunningTimeToPrevious) {
            this.numberOfSites = numberOfSites;
            this.numberOfPairsGenerated = numberOfPairsGenerated;
            this.ratioOfRunningTimeToPrevious = ratioOfRunningTimeToPrevious;
        }
    }

    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);

        Exercise22_DoublingTestErdosRenyi doublingTestErdosRenyi = new Exercise22_DoublingTestErdosRenyi();

        for(int i = 1; i <= 3; i++) {

            switch (i) {
                case 1: StdOut.println("Quick Find"); break;
                case 2: StdOut.println("Quick Union"); break;
                case 3: StdOut.println("Weighted Quick Union");  break;
            }

            doublingTestErdosRenyi.doExperiments(numberOfExperiments, i);
            StdOut.println();
        }
    }

    private void doExperiments(int numberOfExperiments, int unionFindType) {

        List<Experiment> experiments = new ArrayList<>();

        int numberOfSites = 512;

        //Previous time
        UF initialUnionFind = generateUnionFind(numberOfSites / 2, unionFindType);

        Stopwatch initialTimer = new Stopwatch();

        erdosRenyi(numberOfSites / 2, initialUnionFind);

        double previousRunningTime = initialTimer.elapsedTime();

        for(int i = 0; i < numberOfExperiments; i++) {

            UF unionFind = generateUnionFind(numberOfSites, unionFindType);

            Stopwatch timer = new Stopwatch();

            int pairsGenerated = erdosRenyi(numberOfSites, unionFind);

            double runningTime = timer.elapsedTime();

            Experiment experiment = new Experiment(numberOfSites, pairsGenerated, runningTime / previousRunningTime);
            experiments.add(experiment);

            previousRunningTime = runningTime;
            numberOfSites *= 2;
        }

        printResults(experiments);
    }

    private UF generateUnionFind(int numberOfSites, int unionFindType) {

        UF unionFind;

        switch (unionFindType) {
            case 1: unionFind = new QuickFind(numberOfSites); break;
            case 2: unionFind = new QuickUnion(numberOfSites); break;
            case 3: unionFind = new WeightedQuickUnion(numberOfSites); break;
            default: unionFind = null;
        }

        return unionFind;
    }

    private static int erdosRenyi(int numberOfSites, UF unionFind) {
        int connectionsGenerated = 0;

        while(unionFind.count() != 1) {

            int randomSite1 = StdRandom.uniform(numberOfSites);
            int randomSite2 = StdRandom.uniform(numberOfSites);

            connectionsGenerated++;

            if (!unionFind.connected(randomSite1, randomSite2)) {
                unionFind.union(randomSite1, randomSite2);
            }
        }

        return connectionsGenerated;
    }

    private void printResults(List<Experiment> experiments) {
        StdOut.printf("%12s %17s %23s %23s\n", "Experiment |", "Number of Sites |",
                "AVG Pairs Generated |", "Ratio of Running Time |");

        int experimentId = 1;

        for(Experiment experiment : experiments) {

            StdOut.printf("%7d %13d %21d %23.1f\n", experimentId, experiment.numberOfSites,
                    experiment.numberOfPairsGenerated, experiment.ratioOfRunningTimeToPrevious);

            experimentId++;
        }
    }

}
