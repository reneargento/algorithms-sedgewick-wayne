package chapter2.section1;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by rene on 29/01/17.
 */
@SuppressWarnings("unchecked")
public class Exercise17_Animation {

    private enum SortType {
        SELECTION_SORT, INSERTION_SORT, SHELL_SORT;
    }

    public static void main(String[] args) {
        int arraySize = 20;
        Comparable[] array = new Comparable[arraySize];

        for(int i=0; i < array.length; i++) {
            double value = StdRandom.uniform();
            array[i] = value;
        }

        // Set canvas size
        StdDraw.setCanvasSize(30 * (arraySize + 3), 90);
        StdDraw.setXscale(-0.5, arraySize/3 + 1);
        StdDraw.setYscale(0, 2);

        //selectionSort(array);
        //insertionSort(array);
        shellSort(array);
    }

    private static void selectionSort(Comparable[] array) {
        for(int i=0; i < array.length; i++) {
            int minIndex = i;

            for(int j=i+1; j < array.length; j++) {
                if(array[j].compareTo(array[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            draw(array, i, minIndex, -1, SortType.SELECTION_SORT);

            Comparable temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }
    }

    private static void insertionSort(Comparable[] array) {

        for(int i=0; i < array.length; i++) {
            int j;
            for(j = i; j > 0 && array[j-1].compareTo(array[j]) > 0; j--) {
                Comparable temp = array[j];
                array[j] = array[j-1];
                array[j-1] = temp;
            }
            draw(array, i, j, -1, SortType.INSERTION_SORT);
        }
    }

    private static void shellSort(Comparable[] array) {
        int incrementSequence = 1;

        while(incrementSequence * 3 + 1 < array.length) {
            incrementSequence *= 3;
            incrementSequence++;
        }

        while (incrementSequence > 0) {

            for(int i=incrementSequence; i < array.length; i++) {
                int j;

                for(j=i; j >= incrementSequence && array[j].compareTo(array[j - incrementSequence]) < 0; j -= incrementSequence) {
                    Comparable temp = array[j];
                    array[j] = array[j - incrementSequence];
                    array[j - incrementSequence] = temp;
                }
                draw(array, i, j, incrementSequence, SortType.SHELL_SORT);
            }

            incrementSequence /= 3;
        }
    }

    private static void draw(Comparable[] array, int ith, int jth, int shellSortIncrement, SortType sortType) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StdDraw.clear();

        for (int i = 0; i < array.length; i++) {

            switch (sortType) {
                case SELECTION_SORT:
                    if (i == jth) {
                        StdDraw.setPenColor(StdDraw.BOOK_RED);
                    } else if (i < ith) {
                        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                    } else {
                        StdDraw.setPenColor(StdDraw.BLACK);
                    }
                    break;
                case INSERTION_SORT:
                    if (i == jth) {
                        StdDraw.setPenColor(StdDraw.BOOK_RED);
                    } else if (i > ith || i < jth) {
                        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                    } else {
                        StdDraw.setPenColor(StdDraw.BLACK);
                    }
                    break;
                case SHELL_SORT:
                    if (i == jth) {
                        StdDraw.setPenColor(StdDraw.BOOK_RED);
                    } else if (i > ith || i < jth) {
                        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                    } else if ((i % shellSortIncrement) == (jth % shellSortIncrement)) {
                        StdDraw.setPenColor(StdDraw.BLACK);
                    } else {
                        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                    }
                    break;
            }

            double barHalfWidth = 0.08;
            double barHalfHeight = Double.parseDouble(String.valueOf(array[i])) / 2;

            StdDraw.filledRectangle(((double)i)/3, barHalfHeight + 0.7, barHalfWidth, barHalfHeight);
        }
    }

}
