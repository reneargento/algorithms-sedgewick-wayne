package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 9/29/16.
 */
public class Exercise12 {

    public static void main(String[] args){
        int[] array1 = {-2, 1, 2, 2, 5, 6, 6, 8, 25};
        int[] array2 = {0, 1, 2, 2, 2, 3, 4, 5, 10, 20, 25};

        printElementsThatAppearInBothArrays(array1, array2);
        StdOut.println("\nExpected: 1 2 5 25");
    }

    private static void printElementsThatAppearInBothArrays(int[] array1, int[] array2) {

        int array1Index = 0;
        int array2Index = 0;

        while(array1Index < array1.length && array2Index < array2.length) {

            while(array1Index < array1.length && array1[array1Index] < array2[array2Index]) {
                array1Index++;
            }

            if (array1Index < array1.length && array2Index < array2.length &&
                    array1[array1Index] == array2[array2Index]){
                StdOut.print(array1[array1Index] + " ");

                int equalNumber = array1[array1Index];

                array1Index++;
                array2Index++;

                //Avoid printing the same number more than once
                while(array1Index < array1.length && array1[array1Index] == equalNumber) {
                    array1Index++;
                }
                while(array2Index < array2.length && array2[array2Index] == equalNumber) {
                    array2Index++;
                }
            }

            while(array1Index < array1.length && array2Index < array2.length &&
                    array2[array2Index] < array1[array1Index]) {
                array2Index++;
            }
        }
    }

}
