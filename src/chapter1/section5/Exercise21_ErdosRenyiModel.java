package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 09/12/16.
 */
public class Exercise21_ErdosRenyiModel {

    private class Experiment {

        int numberOfSites;
        int pairsGeneratedToGetToOneComponent;

        public Experiment(int numberOfSites, int pairsGeneratedToGetToOneComponent) {
            this.numberOfSites = numberOfSites;
            this.pairsGeneratedToGetToOneComponent = pairsGeneratedToGetToOneComponent;
        }
    }

    private final int NUMBER_OF_EXPERIMENTS = 10;

    public static void main(String[] args) {
        new Exercise21_ErdosRenyiModel().makeExperiments();
    }

    private void makeExperiments() {
        List<Experiment> experiments = new ArrayList<>();

        int numberOfSites = 16;

        for(int i = 0; i < NUMBER_OF_EXPERIMENTS; i++) {
            int pairsGenerated = Exercise17_RandomConnections.erdosRenyi(numberOfSites, false);

            Experiment experiment = new Experiment(numberOfSites, pairsGenerated);
            experiments.add(experiment);

            numberOfSites *= 2;
        }

        printResults(experiments);
    }

    //For accuracy, the closer to 1, the better
    private void printResults(List<Experiment> experiments) {
        StdOut.printf("%12s %17s %19s %10s %10s\n", "Experiment |", "Number of Sites |",
                "Pairs Generated |", "Expected |", "Accuracy |");

        int experimentId = 1;

        for(Experiment experiment : experiments) {
            double lnN = Math.log(experiment.numberOfSites);
            int expectedResult = (int) (0.5 * experiment.numberOfSites * lnN);// ~1/2N ln N

            StdOut.printf("%7d %13d %19d %14d", experimentId, experiment.numberOfSites,
                    experiment.pairsGeneratedToGetToOneComponent, expectedResult);

            double accuracy = (double) experiment.pairsGeneratedToGetToOneComponent / (double) expectedResult;
            StdOut.printf("%12.1f\n", accuracy);

            experimentId++;
        }
    }

}
