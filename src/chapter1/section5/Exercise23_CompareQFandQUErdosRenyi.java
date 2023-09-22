package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 20/12/16.
 */
// Thanks to lhjl1065 (https://github.com/lhjl1065) for reporting a bug with one of the object types.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/298
public class Exercise23_CompareQFandQUErdosRenyi {

    private static class Experiment {
        int numberOfSites;
        double ratioBetweenUnionFindModels;

        public Experiment(int numberOfSites, double ratioBetweenUnionFindModels) {
            this.numberOfSites = numberOfSites;
            this.ratioBetweenUnionFindModels = ratioBetweenUnionFindModels;
        }
    }

    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);

        Exercise23_CompareQFandQUErdosRenyi compareTestErdosRenyi = new Exercise23_CompareQFandQUErdosRenyi();
        compareTestErdosRenyi.doExperiments(numberOfExperiments);
    }

    private void doExperiments(int numberOfExperiments) {
        List<Exercise23_CompareQFandQUErdosRenyi.Experiment> experiments = new ArrayList<>();
        int numberOfSites = 512;

        for (int i = 0; i < numberOfExperiments; i++) {
            List<Exercise18_RandomGridGenerator.Connection> connectionsGenerated =
                    erdosRenyiGeneratingConnections(numberOfSites);
            // QuickUnion
            UF quickUnion = new QuickUnion(numberOfSites);

            Stopwatch timer = new Stopwatch();
            erdosRenyiUsingConnections(quickUnion, connectionsGenerated);
            double runningTimeQuickUnion = timer.elapsedTime();

            // QuickFind
            UF quickFind = new QuickFind(numberOfSites);

            timer = new Stopwatch();
            erdosRenyiUsingConnections(quickFind, connectionsGenerated);
            double runningTimeQuickFind = timer.elapsedTime();

            double ratio = runningTimeQuickFind / runningTimeQuickUnion;
            Experiment experiment = new Experiment(numberOfSites, ratio);
            experiments.add(experiment);

            numberOfSites *= 2;
        }
        printResults(experiments);
    }

    private List<Exercise18_RandomGridGenerator.Connection> erdosRenyiGeneratingConnections(int numberOfSites) {
        UF quickUnion = new QuickUnion(numberOfSites);
        List<Exercise18_RandomGridGenerator.Connection> connectionsGenerated = new ArrayList<>();

        while (quickUnion.count() != 1) {
            int randomSite1 = StdRandom.uniform(numberOfSites);
            int randomSite2 = StdRandom.uniform(numberOfSites);

            Exercise18_RandomGridGenerator.Connection connection = new Exercise18_RandomGridGenerator().new Connection(
                    randomSite1, randomSite2);
            connectionsGenerated.add(connection);

            if (!quickUnion.connected(randomSite1, randomSite2)) {
                quickUnion.union(randomSite1, randomSite2);
            }
        }
        return connectionsGenerated;
    }

    private void erdosRenyiUsingConnections(UF unionFind, List<Exercise18_RandomGridGenerator.Connection> generatedConnections) {
        int connectionIndex = 0;

        while (unionFind.count() != 1) {
            Exercise18_RandomGridGenerator.Connection connection = generatedConnections.get(connectionIndex);

            if (!unionFind.connected(connection.p, connection.q)) {
                unionFind.union(connection.p, connection.q);
            }
            connectionIndex++;
        }
    }

    private void printResults(List<Exercise23_CompareQFandQUErdosRenyi.Experiment> experiments) {
        StdOut.printf("%12s %17s %23s\n", "Experiment |", "Number of Sites |",
               "Ratio of Running Time |");
        int experimentId = 1;

        for (Experiment experiment : experiments) {
            StdOut.printf("%10d %17d %23.1f\n", experimentId, experiment.numberOfSites, experiment.ratioBetweenUnionFindModels);
            experimentId++;
        }
    }
}
