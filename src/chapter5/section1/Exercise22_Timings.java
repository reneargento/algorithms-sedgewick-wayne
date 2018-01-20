package chapter5.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 15/01/18.
 */
public class Exercise22_Timings {

    private static final int LSD_SORT_ID = 0;
    private static final int MSD_SORT_ID = 1;
    private static final int THREE_WAY_STRING_QUICKSORT_ID = 2;

    private void generateStringsAndDoExperiments(int experiments, int numberOfStrings, int numberOfCharacters,
                                                 char[] randomItemsGivenValues) {
        StdOut.printf("%26s %27s %11s\n", "Random string type | ", "Sort type | ", "Average time spent");

        String[] sortAlgorithms = {"Least-Significant-Digit", "Most-Significant-Digit", "3-way string quicksort"};

        double totalTimeSpentLSD = 0;
        double totalTimeSpentMSD = 0;
        double totalTimeSpent3WayStringQuicksort = 0;

        // Key generator 1: Random decimal keys
        String randomStringType = "Decimal keys";

        for(int experiment = 0; experiment < experiments; experiment++) {
            String[] randomStringsLSD = Exercise18_RandomDecimalKeys.randomDecimalKeys(numberOfStrings, numberOfCharacters);

            String[] randomStringsMSD = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStringsMSD, 0, randomStringsLSD.length);

            String[] randomStrings3WayStringQuicksort = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsLSD.length);

            totalTimeSpentLSD += doExperiment(randomStringsLSD, LSD_SORT_ID);
            totalTimeSpentMSD += doExperiment(randomStringsMSD, MSD_SORT_ID);
            totalTimeSpent3WayStringQuicksort += doExperiment(randomStrings3WayStringQuicksort,
                    THREE_WAY_STRING_QUICKSORT_ID);
        }

        computeAndPrintResults(randomStringType, sortAlgorithms, experiments, totalTimeSpentLSD, totalTimeSpentMSD,
                totalTimeSpent3WayStringQuicksort);

        totalTimeSpentLSD = 0;
        totalTimeSpentMSD = 0;
        totalTimeSpent3WayStringQuicksort = 0;

        // Key generator 2: Random CA license plates
        randomStringType = "CA license plates";

        for(int experiment = 0; experiment < experiments; experiment++) {
            String[] randomStringsLSD = Exercise19_RandomCALicensePlates.randomPlatesCA(numberOfStrings);

            String[] randomStringsMSD = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStringsMSD, 0, randomStringsLSD.length);

            String[] randomStrings3WayStringQuicksort = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsLSD.length);

            totalTimeSpentLSD += doExperiment(randomStringsLSD, LSD_SORT_ID);
            totalTimeSpentMSD += doExperiment(randomStringsMSD, MSD_SORT_ID);
            totalTimeSpent3WayStringQuicksort += doExperiment(randomStrings3WayStringQuicksort,
                    THREE_WAY_STRING_QUICKSORT_ID);
        }

        computeAndPrintResults(randomStringType, sortAlgorithms, experiments, totalTimeSpentLSD, totalTimeSpentMSD,
                totalTimeSpent3WayStringQuicksort);

        totalTimeSpentLSD = 0;
        totalTimeSpentMSD = 0;
        totalTimeSpent3WayStringQuicksort = 0;

        // Key generator 3: Random fixed-length words
        randomStringType = "Fixed length words";

        for(int experiment = 0; experiment < experiments; experiment++) {
            String[] randomStringsLSD =  Exercise20_RandomFixedLengthWords.randomFixedLengthWords(numberOfStrings,
                    numberOfCharacters);

            String[] randomStringsMSD = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStringsMSD, 0, randomStringsLSD.length);

            String[] randomStrings3WayStringQuicksort = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsLSD.length);

            totalTimeSpentLSD += doExperiment(randomStringsLSD, LSD_SORT_ID);
            totalTimeSpentMSD += doExperiment(randomStringsMSD, MSD_SORT_ID);
            totalTimeSpent3WayStringQuicksort += doExperiment(randomStrings3WayStringQuicksort,
                    THREE_WAY_STRING_QUICKSORT_ID);
        }

        computeAndPrintResults(randomStringType, sortAlgorithms, experiments, totalTimeSpentLSD, totalTimeSpentMSD,
                totalTimeSpent3WayStringQuicksort);

        totalTimeSpentLSD = 0;
        totalTimeSpentMSD = 0;
        totalTimeSpent3WayStringQuicksort = 0;

        // Key generator 4: Random variable length items
        randomStringType = "Variable length items";

        for(int experiment = 0; experiment < experiments; experiment++) {
            // LSD algorithm cannot be used here
            String[] randomStringsMSD =  Exercise21_RandomItems.randomItems(numberOfStrings, randomItemsGivenValues);

            String[] randomStrings3WayStringQuicksort = new String[randomStringsMSD.length];
            System.arraycopy(randomStringsMSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsMSD.length);

            totalTimeSpentMSD += doExperiment(randomStringsMSD, MSD_SORT_ID);
            totalTimeSpent3WayStringQuicksort += doExperiment(randomStrings3WayStringQuicksort,
                    THREE_WAY_STRING_QUICKSORT_ID);
        }

        computeAndPrintResults(randomStringType, sortAlgorithms, experiments, totalTimeSpentLSD, totalTimeSpentMSD,
                totalTimeSpent3WayStringQuicksort);
    }

    private double doExperiment(String[] randomStrings, int sortAlgorithmType) {
        int stringsLength = randomStrings[0].length();
        Stopwatch stopwatch = new Stopwatch();

        if (sortAlgorithmType == LSD_SORT_ID) {
            LeastSignificantDigit.lsdSort(randomStrings, stringsLength);
        } else if (sortAlgorithmType == MSD_SORT_ID) {
            MostSignificantDigit.msdSort(randomStrings);
        } else if (sortAlgorithmType == THREE_WAY_STRING_QUICKSORT_ID) {
            ThreeWayStringQuickSort.threeWayStringQuickSort(randomStrings);
        }

        return stopwatch.elapsedTime();
    }

    private void computeAndPrintResults(String randomStringType, String[] sortAlgorithms, int experiments,
                                        double totalTimeSpentLSD, double totalTimeSpentMSD,
                                        double totalTimeSpent3WayStringQuicksort) {
        double averageTimeSpentLSD = totalTimeSpentLSD / experiments;
        double averageTimeSpentMSD = totalTimeSpentMSD / experiments;
        double averageTimeSpent3WayStringQuicksort = totalTimeSpent3WayStringQuicksort / experiments;

        if (totalTimeSpentLSD != 0) {
            printResults(randomStringType, sortAlgorithms[LSD_SORT_ID], averageTimeSpentLSD);
        }
        printResults(randomStringType, sortAlgorithms[MSD_SORT_ID], averageTimeSpentMSD);
        printResults(randomStringType, sortAlgorithms[THREE_WAY_STRING_QUICKSORT_ID], averageTimeSpent3WayStringQuicksort);
    }

    private void printResults(String randomStringType, String sortAlgorithm, double averageTimeSpent) {
        StdOut.printf("%23s %27s %21.2f\n", randomStringType, sortAlgorithm, averageTimeSpent);
    }

    // Parameters example: 10 1000000 10 A B
    public static void main(String[] args) {
        int experiments = Integer.parseInt(args[0]);
        int numberOfStrings = Integer.parseInt(args[1]);
        int numberOfCharacters = Integer.parseInt(args[2]);
        char randomItemGivenValue1 = args[3].charAt(0);
        char randomItemGivenValue2 = args[4].charAt(0);

        char[] randomItemsGivenValues = {randomItemGivenValue1, randomItemGivenValue2};
        new Exercise22_Timings().generateStringsAndDoExperiments(experiments, numberOfStrings, numberOfCharacters,
                randomItemsGivenValues);
    }

}
