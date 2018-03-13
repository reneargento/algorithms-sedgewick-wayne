package chapter2.section1;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 29/01/17.
 */
@SuppressWarnings("unchecked")
public class Exercise18_VisualTrace_SelectionSort {

    public static void main(String[] args) {
        int arraySize = 20;
        Comparable[] array = new Comparable[arraySize];

        for(int i = 0; i < array.length; i++) {
            double value = StdRandom.uniform();
            array[i] = value;
        }

        // Set canvas size
        StdDraw.setCanvasSize(30 * (arraySize + 3), 30 * arraySize);
        StdDraw.setXscale(-0.5, arraySize / 3 + 1);
        StdDraw.setYscale(0.5, arraySize + 1.7);

        selectionSort(array);
    }

    private static void selectionSort(Comparable[] array) {
        for(int i = 0; i < array.length; i++) {
            int minIndex = i;

            for(int j = i + 1; j < array.length; j++) {
                if (array[j].compareTo(array[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            draw(array, array.length - i, i, minIndex);

            Comparable temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }
    }

    private static void draw(Comparable[] array, int row, int ith, int min) {

        for (int i = 0; i < array.length; i++) {
            if (i == min) {
                StdDraw.setPenColor(StdDraw.BOOK_RED);
            } else if (i < ith) {
                StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            } else {
                StdDraw.setPenColor(StdDraw.BLACK);
            }

            double barHalfWidth = 0.08;
            double barHalfHeight = Double.parseDouble(String.valueOf(array[i])) / 2;

            StdDraw.filledRectangle(((double) i) / 3, barHalfHeight + row, barHalfWidth, barHalfHeight);
        }
    }

}
