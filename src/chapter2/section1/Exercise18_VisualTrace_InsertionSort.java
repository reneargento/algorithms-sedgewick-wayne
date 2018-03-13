package chapter2.section1;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 30/01/17.
 */
@SuppressWarnings("unchecked")
public class Exercise18_VisualTrace_InsertionSort {

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
        StdDraw.setYscale(0, arraySize + 2);

        insertionSort(array);
    }

    private static void insertionSort(Comparable[] array) {

        for(int i = 0; i < array.length; i++) {
            int j;
            for(j = i; j > 0 && array[j].compareTo(array[j - 1]) < 0; j--) {
                Comparable temp = array[j];
                array[j] = array[j - 1];
                array[j - 1] = temp;
            }
            draw(array, array.length - i, i, j);
        }
    }

    private static void draw(Comparable[] array, int row, int ith, int jth) {

        for (int i = 0; i < array.length; i++) {
            if (i == jth) {
                StdDraw.setPenColor(StdDraw.BOOK_RED);
            } else if (i > ith || i < jth) {
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
