package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 9/30/16.
 */
public class Exercise15_2_ThreeSumFaster {

    public static void main(String[] args) {
        int[] array = {-10, -10, -5, 0, 5, 10, 10, 15, 20};
        int[] arrayTest1 = {-3, -2, 2, 3, 5, 99};
        int[] arrayTest2 = {-10, -10, -10, 10};

        StdOut.println("ThreeSumFaster: " + threeSumFaster(array) + " Expected: 8");
        StdOut.println("ThreeSumFaster: " + threeSumFaster(arrayTest1) + " Expected: 1");
        StdOut.println("ThreeSumFaster: " + threeSumFaster(arrayTest2) + " Expected: 0");
    }

    //Considering that the array is already sorted
    private static int threeSumFaster(int[] array){
        int start = 0;
        int end = array.length - 1;

        int tempIndex;

        int count = 0;

        if((array[start] > 0 && array[end] > 0)
                || (array[start] < 0 && array[end] < 0)) {
            return 0;
        }

        for(int i = 0; i < array.length; i++) {
            start = i + 1;
            end = array.length - 1;

            while(start < end) {
                if (array[i] + array[start] + array[end] > 0){
                    end--;
                } else if (array[i] + array[start] + array[end] < 0) {
                    start++;
                } else{
                    StdOut.println(""+ array[i] + " " + array[start] + " " + array[end]);
                    count++;

                    //Compare all following elements with array[end]
                    tempIndex = start + 1;
                    while(tempIndex < end && array[i] + array[tempIndex] + array[end] == 0) {
                        count++;
                        tempIndex++;

                        StdOut.println(""+ array[i] + " " + array[start] + " " + array[end]);
                    }

                    //Compare all previous elements with array[start]
                    tempIndex = end - 1;
                    while(tempIndex > start && array[i] + array[start] + array[tempIndex] == 0) {
                        count++;
                        tempIndex--;

                        StdOut.println(""+ array[i] + " " + array[start] + " " + array[end]);
                    }

                    start++;
                    end--;
                }
            }
        }

        return count;
    }

}