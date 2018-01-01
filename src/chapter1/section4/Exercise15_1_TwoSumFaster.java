package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 9/30/16.
 */
public class Exercise15_1_TwoSumFaster {

    public static void main(String[] args) {
        int[] array = {-10, -10, -5, 0, 5, 10, 10, 15, 20};
        int[] arrayTest1 = {-3, -2, 2, 3, 5, 99};
        int[] arrayTest2 = {-10, -10, -10, 10};

        StdOut.println("TwoSumFaster: " + twoSumFaster(array) + " Expected: 5");
        StdOut.println("TwoSumFaster: " + twoSumFaster(arrayTest1) + " Expected: 2");
        StdOut.println("TwoSumFaster: " + twoSumFaster(arrayTest2) + " Expected: 3");
    }

    //Considering that the array is already sorted
    private static int twoSumFaster(int[] array){
        int start = 0;
        int end = array.length - 1;

        int tempIndex;

        int count = 0;

        if((array[start] > 0 && array[end] > 0)
                || (array[start] < 0 && array[end] < 0)) {
            return 0;
        }

        while(start < end) {
            if(array[start] + array[end] > 0){
                end--;
            } else if (array[start] + array[end] < 0) {
                start++;
            } else{
                count++;

                //Compare all following elements with array[end]
                tempIndex = start + 1;
                while(tempIndex < end && array[tempIndex] + array[end] == 0) {
                    count++;
                    tempIndex++;
                }

                //Compare all previous elements with array[start]
                tempIndex = end - 1;
                while(tempIndex > start && array[start] + array[tempIndex] == 0) {
                    count++;
                    tempIndex--;
                }

                start++;
                end--;
            }
        }

        return count;
    }

}
