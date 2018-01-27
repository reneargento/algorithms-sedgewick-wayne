package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 20/12/16.
 */
public class Exercise23_CompareQFandQUErdosRenyi {

    private class Experiment {

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

        for(int i = 0; i < numberOfExperiments; i++) {

            //QuickUnion
            UF quickUnion = new QuickUnion(numberOfSites);

            Stopwatch timer = new Stopwatch();
            List<Exercise18_RandomGridGenerator.Connection> connectionsGenerated =
                    erdosRenyiGeneratingConnections(numberOfSites, quickUnion);
            double runningTimeQuickUnion = timer.elapsedTime();

            //QuickFind
            UF quickFind = new QuickUnion(numberOfSites);

            timer = new Stopwatch();
            erdosRenyiUsingConnections(quickFind, connectionsGenerated);
            double runningTimeQuickFind = timer.elapsedTime();

            Experiment experiment = new Experiment(numberOfSites, runningTimeQuickFind / runningTimeQuickUnion);
            experiments.add(experiment);

            numberOfSites *= 2;
        }

        printResults(experiments);
    }

    private List<Exercise18_RandomGridGenerator.Connection> erdosRenyiGeneratingConnections(int numberOfSites, UF unionFind) {

        List<Exercise18_RandomGridGenerator.Connection> connectionsGenerated = new ArrayList<>();

        while(unionFind.count() != 1) {

            int randomSite1 = StdRandom.uniform(numberOfSites);
            int randomSite2 = StdRandom.uniform(numberOfSites);

            Exercise18_RandomGridGenerator.Connection connection = new Exercise18_RandomGridGenerator().new Connection(
                    randomSite1, randomSite2);
            connectionsGenerated.add(connection);

            if (!unionFind.connected(randomSite1, randomSite2)) {
                unionFind.union(randomSite1, randomSite2);
            }
        }

        return connectionsGenerated;
    }

    private void erdosRenyiUsingConnections(UF unionFind, List<Exercise18_RandomGridGenerator.Connection> generatedConnections) {

        int connectionIndex = 0;

        while(unionFind.count() != 1) {

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

        for(Experiment experiment : experiments) {

            StdOut.printf("%7d %13d %23.1f\n", experimentId, experiment.numberOfSites, experiment.ratioBetweenUnionFindModels);

            experimentId++;
        }
    }

}
