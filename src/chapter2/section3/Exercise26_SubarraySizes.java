package chapter2.section3;

import chapter2.section1.InsertionSort;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import util.ArrayUtil;
import util.ArrayGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 11/03/17.
 */
public class Exercise26_SubarraySizes {

    public static void main(String[] args) {
        int arraySize = 100000;

        Comparable[] array = ArrayGenerator.generateRandomArray(arraySize);

        doExperiment(array);
    }

    private static void doExperiment(Comparable[] originalArray) {

        int[] cutoffSizes = {10, 20, 50};

        List<List<Integer>> allSubArraySizes = new ArrayList<>();

        for(int cutoffSize : cutoffSizes) {

            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            List<Integer> subArraySizes = new ArrayList<>();
            quickSortWithCutoff(array, cutoffSize, subArraySizes);

            allSubArraySizes.add(subArraySizes);
        }

        for(int i = 0; i < cutoffSizes.length; i++) {
            histogram(allSubArraySizes.get(i), cutoffSizes[i]);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(i != cutoffSizes.length - 1) {
                StdDraw.clear();
            }
        }
    }

    private static void quickSortWithCutoff(Comparable[] array, int cutoffSize, List<Integer> subArraySizes) {
        StdRandom.shuffle(array);
        quickSort(array, 0, array.length - 1, cutoffSize, subArraySizes);
    }

    private static void quickSort(Comparable[] array, int low, int high, int cutoffSize, List<Integer> subArraySizes) {

        if(low >= high) {
            return;
        }

        int subArraySize = high - low + 1;

        if(subArraySize < cutoffSize) {
            subArraySizes.add(subArraySize);

            InsertionSort.insertionSort(array, low, high);
            return;
        }

        int partition = partition(array, low, high);
        quickSort(array, low, partition - 1, cutoffSize, subArraySizes);
        quickSort(array, partition + 1, high, cutoffSize, subArraySizes);
    }

    private static int partition(Comparable[] array, int low, int high) {
        Comparable pivot = array[low];

        int i = low;
        int j = high + 1;

        while(true) {
            while (ArrayUtil.less(array[++i], pivot)) {
                if(i == high) {
                    break;
                }
            }

            while(ArrayUtil.less(pivot, array[--j])) {
                if(j == low) {
                    break;
                }
            }

            if(i >= j) {
                break;
            }

            ArrayUtil.exchange(array, i, j);
        }

        //Place pivot in the right place
        ArrayUtil.exchange(array, low, j);
        return j;
    }

    private static void histogram(List<Integer> subArraySizes, int cutoff) {

        double[] sizesArray = new double[subArraySizes.size()];
        double maxCount = 0;

        for(int i=0; i < subArraySizes.size(); i++) {
            sizesArray[i] = subArraySizes.get(i);

            if(sizesArray[i] > maxCount) {
                maxCount = sizesArray[i];
            }
        }

        StdDraw.setCanvasSize(1024, 512);
        StdDraw.setXscale(0, sizesArray.length);

        //Labels
        if(cutoff != 50) {
            StdDraw.setYscale(-1, maxCount + 2);
            StdDraw.text(200, maxCount + 0.5, String.valueOf(maxCount));
            StdDraw.text(200, -0.5, String.valueOf(2));
            StdDraw.text(sizesArray.length - 500, -0.5, String.valueOf(sizesArray.length));
        } else {
            StdDraw.setYscale(-5, maxCount + 5);
            StdDraw.text(100, maxCount + 0.5, String.valueOf(maxCount));
            StdDraw.text(100, -1.5, String.valueOf(2));
            StdDraw.text(sizesArray.length - 100, -1.5, String.valueOf(sizesArray.length));
        }

        StdDraw.text(sizesArray.length / 2, maxCount + 1, "Cutoff: " + cutoff);

        StdStats.plotBars(sizesArray);
    }

}
