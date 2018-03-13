package chapter2.section1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.GraphPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 05/02/17.
 */
public class Exercise32_PlotRunningTimes {

    public static void main(String[] args) {
        String sortTypeString = args[0];

        SortTypes sortType;

        switch (sortTypeString) {
            case "Selection": sortType = SortTypes.SELECTION;
                break;
            case "Insertion": sortType = SortTypes.INSERTION;
                break;
            default: sortType = SortTypes.SHELL;
                break;
        }

        int experiments = Integer.parseInt(args[1]);
        int initialLength = Integer.parseInt(args[2]);
        int arrayLengthIncrements = Integer.parseInt(args[3]);

        plotRunningTimes(sortType, initialLength, experiments, arrayLengthIncrements);
    }

    private static void plotRunningTimes(SortTypes sortType, int initialLength, int experiments, int arrayLengthIncrements) {

        List<Double> runningTimes = new ArrayList<>();
        int arrayLength = initialLength;

        for(int experiment = 0; experiment < experiments; experiment++) {

            Comparable[] array = new Comparable[arrayLength];

            for(int i = 0; i < arrayLength; i++) {
                array[i] = StdRandom.uniform();
            }

            double time = time(array, sortType);
            runningTimes.add(time);

            arrayLength *= arrayLengthIncrements;
        }

        GraphPanel graphPanel = new GraphPanel("Running Times", "Running Times",
                "Experiments", "Running Times", runningTimes, 0);
        graphPanel.createAndShowGui();
    }

    public static double time(Comparable[] array, SortTypes sortType) {
        Stopwatch timer = new Stopwatch();

        switch (sortType) {
            case SELECTION: SelectionSort.selectionSort(array);
                break;
            case INSERTION: InsertionSort.insertionSort(array);
                break;
            case SHELL: ShellSort.shellSort(array);
                break;
        }

        return timer.elapsedTime();
    }

}
