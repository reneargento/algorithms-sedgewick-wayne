package chapter2.section1;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;

/**
 * Created by Rene Argento on 29/01/17.
 */
@SuppressWarnings("unchecked")
public class ShellSortDraw {

    private static int line = 0;

    public static void main(String[] args) {
        int arraySize = 20;
        Comparable[] array = new Comparable[arraySize];

        for(int i = 0; i < array.length; i++) {
            double value = StdRandom.uniform();
            array[i] = value;
        }

        // number of rows needed
        int rows = 0;
        int h = 1;
        while (h < arraySize / 3) {
            rows += (arraySize - h + 1);
            h = 3 * h + 1;
        }
        rows += (arraySize - h + 1);

        // Set canvas size
        StdDraw.setCanvasSize(30 * (arraySize + 3), 30 * (arraySize + 3));
        StdDraw.setXscale(-4, arraySize + 1);
        StdDraw.setYscale(rows, -4);
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // draw the header
        header(array);

        // sort the array
        shellSort(array);
    }


    public static void shellSort(Comparable[] array) {
        int incrementSequence = 1;

        while(incrementSequence * 3 + 1 < array.length) {
            incrementSequence *= 3;
            incrementSequence++;
        }

        while (incrementSequence > 0) {

            for(int i = incrementSequence; i < array.length; i++) {
                int j;

                for(j = i; j >= incrementSequence && array[j].compareTo(array[j - incrementSequence]) < 0; j -= incrementSequence) {
                    Comparable temp = array[j];
                    array[j] = array[j - incrementSequence];
                    array[j - incrementSequence] = temp;
                }
                draw(array, incrementSequence, i, j);
                line++;
            }

            incrementSequence /= 3;
            footer(array);
            line++;
        }
    }

    // Display header
    private static void header(Comparable[] array) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(array.length / 2.0, -3, "array[ ]");

        for (int i = 0; i < array.length; i++) {
            StdDraw.text(i, -2, String.valueOf(i));
        }

        StdDraw.text(-2.50, -2, "i");
        StdDraw.text(-1.25, -2, "j");
        StdDraw.setPenColor(StdDraw.BOOK_RED);
        StdDraw.line(-4, -1.65, array.length - 0.5, -1.65);
        StdDraw.setPenColor(StdDraw.BLACK);

        for (int i = 0; i < array.length; i++) {
            StdDraw.text(i, -1, String.format("%.1f", Double.parseDouble(String.valueOf(array[i]))));
        }
    }

    // Display footer
    private static void footer(Comparable[] array) {
        StdDraw.setPenColor(StdDraw.BLACK);

        for (int i = 0; i < array.length; i++) {
            StdDraw.text(i, line, String.format("%.1f", Double.parseDouble(String.valueOf(array[i]))));
        }
    }

    private static void draw(Comparable[] array, int incrementSequence, int ith, int jth) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(-3.75, line, String.valueOf(incrementSequence));
        StdDraw.text(-2.50, line, String.valueOf(ith));
        StdDraw.text(-1.25, line, String.valueOf(jth));

        for (int i = 0; i < array.length; i++) {
            if (i == jth) {
                StdDraw.setPenColor(StdDraw.BOOK_RED);
            } else if (i > ith || i < jth) {
                StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            } else if ((i % incrementSequence) == (jth % incrementSequence)) {
                StdDraw.setPenColor(StdDraw.BLACK);
            } else {
                StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            }
            StdDraw.text(i, line, String.format("%.1f", Double.parseDouble(String.valueOf(array[i]))));
        }
    }

}
