package chapter5.section1;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 15/01/18.
 */
public class Exercise23_ArrayAccesses {

    public class LeastSignificantDigitArrayAccesses {

        // Number of times that the original array was accessed
        private long numberOfArrayAccesses = 0;

        public void lsdSort(String[] array, int stringsLength) {

            int alphabetSize = 256; // Extended ASCII characters

            String[] auxArray = new String[array.length];

            for(int digit = stringsLength - 1; digit >= 0; digit--) {
                // Sort by key-indexed counting on digitTh char

                // Compute frequency counts
                int count[] = new int[alphabetSize + 1];
                for(int i = 0; i < array.length; i++) {
                    int digitIndex = array[i].charAt(digit);
                    numberOfArrayAccesses++;

                    count[digitIndex + 1]++;
                }

                // Transform counts to indices
                for(int r = 0; r < alphabetSize; r++) {
                    count[r + 1] += count[r];
                }

                // Distribute
                for(int i = 0; i < array.length; i++) {
                    int digitIndex = array[i].charAt(digit);
                    numberOfArrayAccesses++;

                    int indexInAuxArray = count[digitIndex]++;
                    auxArray[indexInAuxArray] = array[i];
                    numberOfArrayAccesses++;
                }

                // Copy back
                for(int i = 0; i < array.length; i++) {
                    array[i] = auxArray[i];
                    numberOfArrayAccesses++;
                }
            }
        }

        public long getNumberOfArrayAccesses() {
            return numberOfArrayAccesses;
        }
    }

    public class MostSignificantDigitArrayAccesses {

        private int alphabetSize = 256; // Extended ASCII characters; radix
        private final int CUTOFF_FOR_SMALL_SUBARRAYS = 15;
        private String[] auxArray;

        // Number of times that the original array was accessed
        private long numberOfArrayAccesses = 0;

        public void msdSort(String[] array) {
            auxArray = new String[array.length];
            sort(array, 0, array.length - 1, 0);
        }

        private void sort(String[] array, int low, int high, int digit) {
            // Sort from array[low] to array[high], starting at the digitTh character
            if (low + CUTOFF_FOR_SMALL_SUBARRAYS >= high) {
                InsertionSort insertionSort = new InsertionSort();
                insertionSort.sort(array, low, high, digit);
                return;
            }

            // Compute frequency counts
            int[] count = new int[alphabetSize + 2];
            for(int i = low; i <= high; i++) {
                int digitIndex = charAt(array[i], digit) + 2;
                numberOfArrayAccesses++;

                count[digitIndex]++;
            }

            // Transform counts to indices
            for(int r = 0; r < alphabetSize + 1; r++) {
                count[r + 1] += count[r];
            }

            // Distribute
            for(int i = low; i <= high; i++) {
                int digitIndex = charAt(array[i], digit) + 1;
                numberOfArrayAccesses++;

                int indexInAuxArray = count[digitIndex]++;
                auxArray[indexInAuxArray] = array[i];
                numberOfArrayAccesses++;
            }

            // Copy back
            for(int i = low; i <= high; i++) {
                array[i] = auxArray[i - low];
                numberOfArrayAccesses++;
            }

            // Recursively sort for each character value
            for(int r = 0; r < alphabetSize; r++) {
                sort(array, low + count[r], low + count[r + 1] - 1,digit + 1);
            }
        }

        private int charAt(String string, int digit) {
            if (digit < string.length()) {
                return string.charAt(digit);
            } else {
                return -1;
            }
        }

        public long getNumberOfArrayAccesses() {
            return numberOfArrayAccesses;
        }

        // Insertion sort for strings whose first digit characters are equal
        public class InsertionSort {

            public void sort(String[] array, int low, int high, int digit) {
                // Sort from array[low] to array[high], starting at the digitTh character
                for(int i = low; i <= high; i++) {
                    for(int j = i; j > low; j--) {

                        numberOfArrayAccesses += 2;
                        if (!less(array[j], array[j - 1], digit)) {
                            break;
                        }

                        ArrayUtil.exchange(array, j, j - 1);
                        numberOfArrayAccesses += 4;
                    }
                }
            }

            private boolean less(String string1, String string2, int digit) {
                for(int i = digit; i < Math.min(string1.length(), string2.length()); i++) {
                    if (string1.charAt(i) < string2.charAt(i)) {
                        return true;
                    } else if (string1.charAt(i) > string2.charAt(i)) {
                        return false;
                    }
                }

                return string1.length() < string2.length();
            }
        }
    }

    public class ThreeWayStringQuickSortArrayAccesses {

        // Number of times that the original array was accessed
        private long numberOfArrayAccesses = 0;

        public void threeWayStringQuickSort(String[] array) {
            threeWayStringQuickSort(array, 0, array.length - 1, 0);
        }

        private void threeWayStringQuickSort(String[] array, int low, int high, int digit) {
            if (low >= high) {
                return;
            }

            int lowerThan = low;
            int greaterThan = high;

            int pivot = charAt(array[low], digit);
            numberOfArrayAccesses++;

            int index = low + 1;

            while (index <= greaterThan) {
                int currentChar = charAt(array[index], digit);
                numberOfArrayAccesses++;

                if (currentChar < pivot) {
                    ArrayUtil.exchange(array, lowerThan++, index++);
                    numberOfArrayAccesses += 4;
                } else if (currentChar > pivot) {
                    ArrayUtil.exchange(array, index, greaterThan--);
                    numberOfArrayAccesses += 4;
                } else {
                    index++;
                }
            }

            // Now array[low..lowerThan - 1] < pivot = array[lowerThan..greaterThan] < array[greaterThan + 1..high]
            threeWayStringQuickSort(array, low, lowerThan - 1, digit);
            if (pivot >= 0) {
                threeWayStringQuickSort(array, lowerThan, greaterThan, digit + 1);
            }
            threeWayStringQuickSort(array, greaterThan + 1, high, digit);
        }

        private int charAt(String string, int digit) {
            if (digit < string.length()) {
                return string.charAt(digit);
            } else {
                return -1;
            }
        }

        public long getNumberOfArrayAccesses() {
            return numberOfArrayAccesses;
        }
    }

    private static final int LSD_SORT_ID = 0;
    private static final int MSD_SORT_ID = 1;
    private static final int THREE_WAY_STRING_QUICKSORT_ID = 2;

    private void generateStringsAndDoExperiments(int experiments, int numberOfStrings, int numberOfCharacters,
                                                 char[] randomItemsGivenValues) {
        StdOut.printf("%26s %27s %15s\n", "Random string type | ", "Sort type | ", "Number of array accesses");

        String[] sortAlgorithms = {"Least-Significant-Digit", "Most-Significant-Digit", "3-way string quicksort"};

        long totalArrayAccessesLSD = 0;
        long totalArrayAccessesMSD = 0;
        long totalArrayAccesses3WayStringQuicksort = 0;

        // Key generator 1: Random decimal keys
        String randomStringType = "Decimal keys";

        for(int experiment = 0; experiment < experiments; experiment++) {
            String[] randomStringsLSD = Exercise18_RandomDecimalKeys.randomDecimalKeys(numberOfStrings, numberOfCharacters);

            String[] randomStringsMSD = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStringsMSD, 0, randomStringsLSD.length);

            String[] randomStrings3WayStringQuicksort = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsLSD.length);

            totalArrayAccessesLSD += doExperiment(randomStringsLSD, LSD_SORT_ID);
            totalArrayAccessesMSD += doExperiment(randomStringsMSD, MSD_SORT_ID);
            totalArrayAccesses3WayStringQuicksort += doExperiment(randomStrings3WayStringQuicksort,
                    THREE_WAY_STRING_QUICKSORT_ID);
        }

        computeAndPrintResults(randomStringType, sortAlgorithms, experiments, totalArrayAccessesLSD, totalArrayAccessesMSD,
                totalArrayAccesses3WayStringQuicksort);

        totalArrayAccessesLSD = 0;
        totalArrayAccessesMSD = 0;
        totalArrayAccesses3WayStringQuicksort = 0;

        // Key generator 2: Random CA license plates
        randomStringType = "CA license plates";

        for(int experiment = 0; experiment < experiments; experiment++) {
            String[] randomStringsLSD = Exercise19_RandomCALicensePlates.randomPlatesCA(numberOfStrings);

            String[] randomStringsMSD = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStringsMSD, 0, randomStringsLSD.length);

            String[] randomStrings3WayStringQuicksort = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsLSD.length);

            totalArrayAccessesLSD += doExperiment(randomStringsLSD, LSD_SORT_ID);
            totalArrayAccessesMSD += doExperiment(randomStringsMSD, MSD_SORT_ID);
            totalArrayAccesses3WayStringQuicksort += doExperiment(randomStrings3WayStringQuicksort,
                    THREE_WAY_STRING_QUICKSORT_ID);
        }

        computeAndPrintResults(randomStringType, sortAlgorithms, experiments, totalArrayAccessesLSD, totalArrayAccessesMSD,
                totalArrayAccesses3WayStringQuicksort);

        totalArrayAccessesLSD = 0;
        totalArrayAccessesMSD = 0;
        totalArrayAccesses3WayStringQuicksort = 0;

        // Key generator 3: Random fixed-length words
        randomStringType = "Fixed length words";

        for(int experiment = 0; experiment < experiments; experiment++) {
            String[] randomStringsLSD =  Exercise20_RandomFixedLengthWords.randomFixedLengthWords(numberOfStrings,
                    numberOfCharacters);

            String[] randomStringsMSD = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStringsMSD, 0, randomStringsLSD.length);

            String[] randomStrings3WayStringQuicksort = new String[randomStringsLSD.length];
            System.arraycopy(randomStringsLSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsLSD.length);

            totalArrayAccessesLSD += doExperiment(randomStringsLSD, LSD_SORT_ID);
            totalArrayAccessesMSD += doExperiment(randomStringsMSD, MSD_SORT_ID);
            totalArrayAccesses3WayStringQuicksort += doExperiment(randomStrings3WayStringQuicksort,
                    THREE_WAY_STRING_QUICKSORT_ID);
        }

        computeAndPrintResults(randomStringType, sortAlgorithms, experiments, totalArrayAccessesLSD, totalArrayAccessesMSD,
                totalArrayAccesses3WayStringQuicksort);

        totalArrayAccessesLSD = 0;
        totalArrayAccessesMSD = 0;
        totalArrayAccesses3WayStringQuicksort = 0;

        // Key generator 4: Random variable length items
        randomStringType = "Variable length items";

        for(int experiment = 0; experiment < experiments; experiment++) {
            // LSD algorithm cannot be used here
            String[] randomStringsMSD =  Exercise21_RandomItems.randomItems(numberOfStrings, randomItemsGivenValues);

            String[] randomStrings3WayStringQuicksort = new String[randomStringsMSD.length];
            System.arraycopy(randomStringsMSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsMSD.length);

            totalArrayAccessesMSD += doExperiment(randomStringsMSD, MSD_SORT_ID);
            totalArrayAccesses3WayStringQuicksort += doExperiment(randomStrings3WayStringQuicksort,
                    THREE_WAY_STRING_QUICKSORT_ID);
        }

        computeAndPrintResults(randomStringType, sortAlgorithms, experiments, totalArrayAccessesLSD, totalArrayAccessesMSD,
                totalArrayAccesses3WayStringQuicksort);
    }

    private long doExperiment(String[] randomStrings, int sortAlgorithmType) {
        int stringsLength = randomStrings[0].length();
        long numberOfArrayAccesses = 0;

        if (sortAlgorithmType == LSD_SORT_ID) {
            LeastSignificantDigitArrayAccesses leastSignificantDigitArrayAccesses =
                    new LeastSignificantDigitArrayAccesses();

            leastSignificantDigitArrayAccesses.lsdSort(randomStrings, stringsLength);
            numberOfArrayAccesses = leastSignificantDigitArrayAccesses.getNumberOfArrayAccesses();
        } else if (sortAlgorithmType == MSD_SORT_ID) {
            MostSignificantDigitArrayAccesses mostSignificantDigitArrayAccesses =
                    new MostSignificantDigitArrayAccesses();

            mostSignificantDigitArrayAccesses.msdSort(randomStrings);
            numberOfArrayAccesses = mostSignificantDigitArrayAccesses.getNumberOfArrayAccesses();
        } else if (sortAlgorithmType == THREE_WAY_STRING_QUICKSORT_ID) {
            ThreeWayStringQuickSortArrayAccesses threeWayStringQuickSortArrayAccesses =
                    new ThreeWayStringQuickSortArrayAccesses();

            threeWayStringQuickSortArrayAccesses.threeWayStringQuickSort(randomStrings);
            numberOfArrayAccesses = threeWayStringQuickSortArrayAccesses.getNumberOfArrayAccesses();
        }

        return numberOfArrayAccesses;
    }

    private void computeAndPrintResults(String randomStringType, String[] sortAlgorithms, int experiments,
                                        long totalArrayAccessesLSD, long totalArrayAccessesMSD,
                                        long totalArrayAccesses3WayStringQuicksort) {
        long averageArrayAccessesLSD = totalArrayAccessesLSD / experiments;
        long averageArrayAccessesMSD = totalArrayAccessesMSD / experiments;
        long averageArrayAccesses3WayStringQuicksort = totalArrayAccesses3WayStringQuicksort / experiments;

        if (totalArrayAccessesLSD != 0) {
            printResults(randomStringType, sortAlgorithms[LSD_SORT_ID], averageArrayAccessesLSD);
        }
        printResults(randomStringType, sortAlgorithms[MSD_SORT_ID], averageArrayAccessesMSD);
        printResults(randomStringType, sortAlgorithms[THREE_WAY_STRING_QUICKSORT_ID],
                averageArrayAccesses3WayStringQuicksort);
    }

    private void printResults(String randomStringType, String sortAlgorithm, long averageArrayAccesses) {
        StdOut.printf("%23s %27s %27d\n", randomStringType, sortAlgorithm, averageArrayAccesses);
    }

    // Parameters example: 10 1000000 10 A B
    public static void main(String[] args) {
        int experiments = Integer.parseInt(args[0]);
        int numberOfStrings = Integer.parseInt(args[1]);
        int numberOfCharacters = Integer.parseInt(args[2]);
        char randomItemGivenValue1 = args[3].charAt(0);
        char randomItemGivenValue2 = args[4].charAt(0);

        char[] randomItemsGivenValues = {randomItemGivenValue1, randomItemGivenValue2};
        new Exercise23_ArrayAccesses().generateStringsAndDoExperiments(experiments, numberOfStrings, numberOfCharacters,
                randomItemsGivenValues);
    }

}
