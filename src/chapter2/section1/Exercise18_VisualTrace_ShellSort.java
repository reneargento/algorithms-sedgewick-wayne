package chapter2.section1;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;

/**
 * Created by Rene Argento on 30/01/17.
 */
@SuppressWarnings("unchecked")
public class Exercise18_VisualTrace_ShellSort {

    public static void main(String[] args) {
        int arraySize = 20;
        Comparable[] array = new Comparable[arraySize];

        for(int i = 0; i < array.length; i++) {
            double value = StdRandom.uniform();
            array[i] = value;
        }

        int numberOfIncrements = getNumberOfIncrements(arraySize);

        // Set canvas size
        StdDraw.setCanvasSize(30 * (arraySize + 3), 30 * arraySize);
        StdDraw.setXscale(-0.5, arraySize / 3 + 1);
        StdDraw.setYscale(-0.5, numberOfIncrements + 0.8 + 1); //+1 to draw the input array
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 13));

        draw(array, numberOfIncrements + 1, "Input");

        shellSort(array);
    }

    private static void shellSort(Comparable[] array) {
        int incrementSequence = 1;

        int numberOfIncrementSequences = 0;

        while(incrementSequence * 3 + 1 < array.length) {
            incrementSequence *= 3;
            incrementSequence++;

            numberOfIncrementSequences++;
        }

        int rowToDraw = numberOfIncrementSequences;

        while (incrementSequence > 0) {

            for(int i = incrementSequence; i < array.length; i++) {
                int j;

                for(j = i; j >= incrementSequence && array[j].compareTo(array[j - incrementSequence]) < 0; j -= incrementSequence) {
                    Comparable temp = array[j];
                    array[j] = array[j - incrementSequence];
                    array[j - incrementSequence] = temp;
                }
            }

            draw(array, rowToDraw, incrementSequence + "-sorted");
            rowToDraw--;
            incrementSequence /= 3;
        }
    }

    private static int getNumberOfIncrements(int arraySize) {
        int incrementSequence = 1;

        int numberOfIncrementSequences = 0;

        while(incrementSequence * 3 + 1 < arraySize) {
            incrementSequence *= 3;
            incrementSequence++;

            numberOfIncrementSequences++;
        }

        return numberOfIncrementSequences;
    }

    private static void draw(Comparable[] array, int rowToDraw, String textLabel) {

        StdDraw.setPenColor(StdDraw.BLACK);

        StdDraw.text(0, rowToDraw + 0.7, textLabel);

        for (int i = 0; i < array.length; i++) {

            double barHalfWidth = 0.08;
            double barHalfHeight = Double.parseDouble(String.valueOf(array[i])) / 2;

            StdDraw.filledRectangle(((double) i) / 3, barHalfHeight + rowToDraw - 0.2, barHalfWidth, barHalfHeight);
        }
    }
}
