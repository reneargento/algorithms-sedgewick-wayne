package chapter2.section1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.GraphPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 05/02/17.
 */
public class Exercise33_Distribution {

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
        int arrayLength = Integer.parseInt(args[2]);

        plotRunningTimes(sortType, arrayLength, experiments);
    }

    private static void plotRunningTimes(SortTypes sortType, int arrayLength, int experiments) {

        List<Double> runningTimes = new ArrayList<>();

        int experiment = 0;

        while(true) {

            Comparable[] array = new Comparable[arrayLength];

            for(int i = 0; i < arrayLength; i++) {
                array[i] = StdRandom.uniform();
            }

            double time = time(array, sortType);
            runningTimes.add(time);

            experiment++;

            if (experiment == experiments) {
                break;
            }
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