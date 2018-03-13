package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 28/01/17.
 */
public class Exercise11 {

    public static void main(String[] args) {
        int[] array = {2, 20, -1, -30, 30, 5, 6, 8, -99, -3, 0, 4, 4, 4};
        shellsort(array);

        for(int value : array) {
            StdOut.print(value + " ");
        }
        StdOut.println("\nExpected: -99 -30 -3 -1 0 2 4 4 4 5 6 8 20 30 ");
    }

    private static void shellsort(int[] array) {

        if (array == null || array.length == 0) {
            return;
        }

        int maxIncrement = 1;
        int numberOfIncrements = 1;

        while((maxIncrement * 3) + 1 < array.length) {
            maxIncrement = maxIncrement * 3;
            maxIncrement++;

            numberOfIncrements++;
        }

        int[] incrementSequence = new int[numberOfIncrements];

        int index = 0;
        while(maxIncrement > 0) {
            incrementSequence[index] = maxIncrement;

            maxIncrement--;
            maxIncrement = maxIncrement / 3;
            index++;
        }

        //Shellsort
        for(int i = 0; i < incrementSequence.length; i++) {

            int increment = incrementSequence[i];

            //h-sort the array
            for(int j = increment; j < array.length; j++) {
                int currentIndex = j;

                while(currentIndex >= increment && array[currentIndex] < array[currentIndex - increment]) {
                    int temp = array[currentIndex];
                    array[currentIndex] = array[currentIndex - increment];
                    array[currentIndex - increment] = temp;

                    currentIndex = currentIndex - increment;
                }
            }
        }
    }

}
