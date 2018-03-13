package chapter5.section1;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 15/01/18.
 */
public class Exercise24_RightmostCharacterAccessed {

    public class MostSignificantDigitRightmostCharacter {

        private int alphabetSize = 256; // Extended ASCII characters; radix
        private final int CUTOFF_FOR_SMALL_SUBARRAYS = 15;
        private String[] auxArray;

        private int rightmostCharacterAccessed = 0;

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

                // If digit exists, check if it is the rightmost character accessed
                if (digitIndex > 1 && digit > rightmostCharacterAccessed) {
                    rightmostCharacterAccessed = digit;
                }

                count[digitIndex]++;
            }

            // Transform counts to indices
            for(int r = 0; r < alphabetSize + 1; r++) {
                count[r + 1] += count[r];
            }

            // Distribute
            for(int i = low; i <= high; i++) {
                int digitIndex = charAt(array[i], digit) + 1;
                int indexInAuxArray = count[digitIndex]++;
                auxArray[indexInAuxArray] = array[i];
            }

            // Copy back
            for(int i = low; i <= high; i++) {
                array[i] = auxArray[i - low];
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

        public int getRightmostCharacterAccessed() {
            return rightmostCharacterAccessed;
        }

        // Insertion sort for Strings whose first digit characters are equal
        public class InsertionSort {

            public void sort(String[] array, int low, int high, int digit) {
                // Sort from array[low] to array[high], starting at the digitTh character
                for(int i = low; i <= high; i++) {
                    for(int j = i; j > low && less(array[j], array[j - 1], digit); j--) {
                        ArrayUtil.exchange(array, j, j - 1);
                    }
                }
            }

            private boolean less(String string1, String string2, int digit) {
                for(int i = digit; i < Math.min(string1.length(), string2.length()); i++) {
                    if (i > rightmostCharacterAccessed) {
                        rightmostCharacterAccessed = i;
                    }

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

    public class ThreeWayStringQuickSortRightmostCharacter {

        private int rightmostCharacterAccessed = 0;

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

            // If digit exists, check if it is the rightmost character accessed
            if (pivot != -1 && digit > rightmostCharacterAccessed) {
                rightmostCharacterAccessed = digit;
            }

            int index = low + 1;

            while (index <= greaterThan) {
                int currentChar = charAt(array[index], digit);

                if (currentChar < pivot) {
                    ArrayUtil.exchange(array, lowerThan++, index++);
                } else if (currentChar > pivot) {
                    ArrayUtil.exchange(array, index, greaterThan--);
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

        public int getRightmostCharacterAccessed() {
            return rightmostCharacterAccessed;
        }
    }

    private static final int MSD_SORT_ID = 0;
    private static final int THREE_WAY_STRING_QUICKSORT_ID = 1;

    private void generateStringsAndDoExperiments(int numberOfStrings, int numberOfCharacters, char[] randomItemsGivenValues) {
        StdOut.printf("%26s %27s %15s\n", "Random string type | ", "Sort type | ", "Rightmost character accessed");

        String[] sortAlgorithms = {"Most-Significant-Digit", "3-way string quicksort"};

        int rightmostCharacterAccessedMSD;
        int rightmostCharacterAccessed3WayStringQuicksort;

        // Key generator 1: Random decimal keys
        String randomStringType = "Decimal keys";

        String[] randomStringsMSD = Exercise18_RandomDecimalKeys.randomDecimalKeys(numberOfStrings, numberOfCharacters);

        String[] randomStrings3WayStringQuicksort = new String[randomStringsMSD.length];
        System.arraycopy(randomStringsMSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsMSD.length);

        rightmostCharacterAccessedMSD = doExperiment(randomStringsMSD, MSD_SORT_ID);
        rightmostCharacterAccessed3WayStringQuicksort = doExperiment(randomStrings3WayStringQuicksort,
                THREE_WAY_STRING_QUICKSORT_ID);

        printResults(randomStringType, sortAlgorithms[MSD_SORT_ID], rightmostCharacterAccessedMSD);
        printResults(randomStringType, sortAlgorithms[THREE_WAY_STRING_QUICKSORT_ID], rightmostCharacterAccessed3WayStringQuicksort);

        // Key generator 2: Random CA license plates
        randomStringType = "CA license plates";

        randomStringsMSD = Exercise19_RandomCALicensePlates.randomPlatesCA(numberOfStrings);

        randomStrings3WayStringQuicksort = new String[randomStringsMSD.length];
        System.arraycopy(randomStringsMSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsMSD.length);

        rightmostCharacterAccessedMSD = doExperiment(randomStringsMSD, MSD_SORT_ID);
        rightmostCharacterAccessed3WayStringQuicksort = doExperiment(randomStrings3WayStringQuicksort,
                THREE_WAY_STRING_QUICKSORT_ID);

        printResults(randomStringType, sortAlgorithms[MSD_SORT_ID], rightmostCharacterAccessedMSD);
        printResults(randomStringType, sortAlgorithms[THREE_WAY_STRING_QUICKSORT_ID], rightmostCharacterAccessed3WayStringQuicksort);

        // Key generator 3: Random fixed-length words
        randomStringType = "Fixed length words";

        randomStringsMSD =  Exercise20_RandomFixedLengthWords.randomFixedLengthWords(numberOfStrings, numberOfCharacters);

        randomStrings3WayStringQuicksort = new String[randomStringsMSD.length];
        System.arraycopy(randomStringsMSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsMSD.length);

        rightmostCharacterAccessedMSD = doExperiment(randomStringsMSD, MSD_SORT_ID);
        rightmostCharacterAccessed3WayStringQuicksort = doExperiment(randomStrings3WayStringQuicksort,
                THREE_WAY_STRING_QUICKSORT_ID);

        printResults(randomStringType, sortAlgorithms[MSD_SORT_ID], rightmostCharacterAccessedMSD);
        printResults(randomStringType, sortAlgorithms[THREE_WAY_STRING_QUICKSORT_ID], rightmostCharacterAccessed3WayStringQuicksort);

        // Key generator 4: Random variable length items
        randomStringType = "Variable length items";

        randomStringsMSD =  Exercise21_RandomItems.randomItems(numberOfStrings, randomItemsGivenValues);

        randomStrings3WayStringQuicksort = new String[randomStringsMSD.length];
        System.arraycopy(randomStringsMSD, 0, randomStrings3WayStringQuicksort, 0, randomStringsMSD.length);

        rightmostCharacterAccessedMSD = doExperiment(randomStringsMSD, MSD_SORT_ID);
        rightmostCharacterAccessed3WayStringQuicksort = doExperiment(randomStrings3WayStringQuicksort,
                THREE_WAY_STRING_QUICKSORT_ID);

        printResults(randomStringType, sortAlgorithms[MSD_SORT_ID], rightmostCharacterAccessedMSD);
        printResults(randomStringType, sortAlgorithms[THREE_WAY_STRING_QUICKSORT_ID], rightmostCharacterAccessed3WayStringQuicksort);
    }

    private int doExperiment(String[] randomStrings, int sortAlgorithmType) {
        int rightmostCharacterAccessed = 0;

        if (sortAlgorithmType == MSD_SORT_ID) {
            MostSignificantDigitRightmostCharacter mostSignificantDigitRightmostCharacter =
                    new MostSignificantDigitRightmostCharacter();

            mostSignificantDigitRightmostCharacter.msdSort(randomStrings);
            rightmostCharacterAccessed = mostSignificantDigitRightmostCharacter.getRightmostCharacterAccessed();
        } else if (sortAlgorithmType == THREE_WAY_STRING_QUICKSORT_ID) {
            ThreeWayStringQuickSortRightmostCharacter threeWayStringQuickSortRightmostCharacter =
                    new ThreeWayStringQuickSortRightmostCharacter();

            threeWayStringQuickSortRightmostCharacter.threeWayStringQuickSort(randomStrings);
            rightmostCharacterAccessed = threeWayStringQuickSortRightmostCharacter.getRightmostCharacterAccessed();
        }

        return rightmostCharacterAccessed;
    }

    private void printResults(String randomStringType, String sortAlgorithm, int rightmostCharacterAccessed) {
        StdOut.printf("%23s %27s %31d\n", randomStringType, sortAlgorithm, rightmostCharacterAccessed);
    }

    // Parameters example: 1000000 10 A B
    public static void main(String[] args) {
        int numberOfStrings = Integer.parseInt(args[0]);
        int numberOfCharacters = Integer.parseInt(args[1]);
        char randomItemGivenValue1 = args[2].charAt(0);
        char randomItemGivenValue2 = args[3].charAt(0);

        char[] randomItemsGivenValues = {randomItemGivenValue1, randomItemGivenValue2};
        new Exercise24_RightmostCharacterAccessed().generateStringsAndDoExperiments(numberOfStrings, numberOfCharacters,
                randomItemsGivenValues);
    }

}
