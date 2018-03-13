package chapter2.section1;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;

/**
 * Created by Rene Argento on 29/01/17.
 */
@SuppressWarnings("unchecked")
public class SelectionSortDraw {

    public static void main(String[] args) {
        int arraySize = 20;
        Comparable[] array = new Comparable[arraySize];

        for(int i = 0; i < array.length; i++) {
            double value = StdRandom.uniform();
            array[i] = value;
        }

        // Set canvas size
        StdDraw.setCanvasSize(30 * (arraySize + 3), 30 * (arraySize + 3));
        StdDraw.setXscale(-3, arraySize + 1);
        StdDraw.setYscale(arraySize + 1, -3);
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // draw the header
        header(array);

        // sort the array
        selectionSort(array);

        // draw the footer
        footer(array);
    }

    public static void selectionSort(Comparable[] array) {
        for(int i = 0; i < array.length; i++) {
            int minIndex = i;

            for(int j = i + 1; j < array.length; j++) {
                if (array[j].compareTo(array[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            draw(array, i, i, minIndex);

            Comparable temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }
    }

    // Display header
    private static void header(Comparable[] array) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(array.length / 2.0, -2.7, "array[ ]");

        for (int i = 0; i < array.length; i++) {
            StdDraw.text(i, -2, String.valueOf(i));
        }

        StdDraw.text(-2.50, -2, "i");
        StdDraw.text(-1.25, -2, "min");
        StdDraw.setPenColor(StdDraw.BOOK_RED);
        StdDraw.line(-3, -1.65, array.length - 0.5, -1.65);
        StdDraw.setPenColor(StdDraw.BLACK);

        for (int i = 0; i < array.length; i++) {
            StdDraw.text(i, -1, String.format("%.1f", Double.parseDouble(String.valueOf(array[i]))));
        }
    }

    // Display footer
    private static void footer(Comparable[] array) {
        StdDraw.setPenColor(StdDraw.BLACK);

        for (int i = 0; i < array.length; i++) {
            StdDraw.text(i, array.length, String.format("%.1f", Double.parseDouble(String.valueOf(array[i]))));
        }
    }

    private static void draw(Comparable[] array, int row, int ith, int min) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(-2.50, row, String.valueOf(ith));
        StdDraw.text(-1.25, row, String.valueOf(min));

        for (int i = 0; i < array.length; i++) {
            if (i == min) {
                StdDraw.setPenColor(StdDraw.BOOK_RED);
            } else if (i < ith) {
                StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            } else {
                StdDraw.setPenColor(StdDraw.BLACK);
            }
            StdDraw.text(i, row, String.format("%.1f", Double.parseDouble(String.valueOf(array[i]))));
        }
    }

}
