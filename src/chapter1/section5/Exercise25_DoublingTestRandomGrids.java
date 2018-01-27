package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 21/12/16.
 */
public class Exercise25_DoublingTestRandomGrids {

    private class Experiment {

        int numberOfSites;
        int numberOfConnectionsProcessed;
        double ratioOfRunningTimeToPrevious;

        public Experiment(int numberOfSites, int numberOfConnectionsProcessed, double ratioOfRunningTimeToPrevious) {
            this.numberOfSites = numberOfSites;
            this.numberOfConnectionsProcessed = numberOfConnectionsProcessed;
            this.ratioOfRunningTimeToPrevious = ratioOfRunningTimeToPrevious;
        }
    }

    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);

        Exercise25_DoublingTestRandomGrids doublingTestRandomGrids = new Exercise25_DoublingTestRandomGrids();

        for(int i = 1; i <= 3; i++) {

            switch (i) {
                case 1: StdOut.println("Quick Find"); break;
                case 2: StdOut.println("Quick Union"); break;
                case 3: StdOut.println("Weighted Quick Union");  break;
            }

            doublingTestRandomGrids.doExperiments(numberOfExperiments, i);
            StdOut.println();
        }
    }

    private void doExperiments(int numberOfExperiments, int unionFindType) {

        List<Experiment> experiments = new ArrayList<>();

        int numberOfSites = 512;

        //Previous time
        UF initialUnionFind = generateUnionFind(numberOfSites / 2, unionFindType);

        Stopwatch initialTimer = new Stopwatch();

        randomGridsConnection(numberOfSites / 2, initialUnionFind);

        double previousRunningTime = initialTimer.elapsedTime();

        for(int i = 0; i < numberOfExperiments; i++) {

            UF unionFind = generateUnionFind(numberOfSites, unionFindType);

            Stopwatch timer = new Stopwatch();

            int pairsGenerated = randomGridsConnection(numberOfSites, unionFind);

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

    private static int randomGridsConnection(int numberOfSites, UF unionFind) {
        Exercise18_RandomGridGenerator randomGridGenerator = new Exercise18_RandomGridGenerator();
        Exercise18_RandomGridGenerator.Connection[] connections = randomGridGenerator.generate(numberOfSites);

        int connectionsGenerated = 0;
        int connectionIndex = 0;

        while(unionFind.count() != 1) {
            int randomSite1 = connections[connectionIndex].p;
            int randomSite2 = connections[connectionIndex].q;

            connectionsGenerated++;

            if (!unionFind.connected(randomSite1, randomSite2)) {
                unionFind.union(randomSite1, randomSite2);
            }

            connectionIndex++;
        }

        return connectionsGenerated;
    }

    private void printResults(List<Experiment> experiments) {
        StdOut.printf("%12s %17s %23s %23s\n", "Experiment |", "Number of Sites |",
                "AVG Pairs Generated |", "Ratio of Running Time |");

        int experimentId = 1;

        for(Experiment experiment : experiments) {

            StdOut.printf("%7d %13d %21d %23.1f\n", experimentId, experiment.numberOfSites,
                    experiment.numberOfConnectionsProcessed, experiment.ratioOfRunningTimeToPrevious);

            experimentId++;
        }
    }

}
