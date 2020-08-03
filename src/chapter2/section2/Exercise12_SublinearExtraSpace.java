package chapter2.section2;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 12/02/17.
 */
//Based on http://softwareengineering.stackexchange.com/questions/179431/sublinear-extra-space-mergesort
@SuppressWarnings("unchecked")
public class Exercise12_SublinearExtraSpace {

    public static void main(String[] args) {

        int arraySize = Integer.parseInt(args[0]);
        int blockSize = Integer.parseInt(args[1]);

        if (arraySize % blockSize != 0) {
            throw new RuntimeException("Array size needs to be a multiple of block size");
        }

        Comparable[] array = generateRandomArray(arraySize);

        selectionSortBlocks(array, blockSize);

        Comparable[] aux = new Comparable[blockSize];

        //O (N^2)
        for (int i = arraySize / blockSize - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                int low = j * blockSize;
                int middle = (j + 1) * blockSize - 1;
                int high = middle + blockSize;
                merge(array, aux, low, middle, high);
            }
        }
    }

    private static Comparable[] generateRandomArray(int arrayLength) {
        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = StdRandom.uniform();
        }

        return array;
    }

    private static void selectionSortBlocks(Comparable[] array, int blockSize) {
        for(int i = 0; i < array.length; i += blockSize) {
            selectionSort(array, i, i + blockSize - 1);
        }
    }

    private static void selectionSort(Comparable[] array, int low, int high) {
        for(int i = low; i <= high; i++) {
            int minIndex = i;

            for(int j = i + 1; j <= high; j++) {
                if (array[j].compareTo(array[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            Comparable temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }
    }

    private static void merge(Comparable[] array, Comparable[] aux, int low, int middle, int high) {
        int auxIndex = 0;

        for(int i = low; i <= middle; i++) {
            aux[auxIndex] = array[i];
            auxIndex++;
        }

        int indexLeft = 0;
        int indexRight = middle + 1;
        int arrayIndex = low;

        while (indexLeft < aux.length && indexRight <= high) {
            if (aux[indexLeft].compareTo(array[indexRight]) <= 0) {
                array[arrayIndex] = aux[indexLeft];
                indexLeft++;
            } else {
                array[arrayIndex] = array[indexRight];
                indexRight++;
            }

            arrayIndex++;
        }

        while (indexLeft < aux.length) {
            array[arrayIndex] = aux[indexLeft];

            indexLeft++;
            arrayIndex++;
        }
    }
}
