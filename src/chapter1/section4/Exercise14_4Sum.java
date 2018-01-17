package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 9/29/16.
 */
public class Exercise14_4Sum {

    public static void main(String[] args){

        int[] array = {1, 2, 3, 4, -4, -5, -6, 2, 4, -1};
        StdOut.println("4 sum: " + fourSum(array));
        StdOut.println("Expected: 4");
    }

    //O(n^3 lg n)
    private static int fourSum(int[] array){

        Arrays.sort(array);

        int count = 0;

        for(int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++){
                for(int k = j + 1; k < array.length; k++){

                    int searchElement = -1 * (array[i] + array[j] + array[k]);
                    int elementIndex = binarySearch(array, searchElement, 0, array.length);
                    if(elementIndex > k) {
                        StdOut.println("" + array[i] +  " " + array[j] +  " " + array[k] +  " " + array[elementIndex]);
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private static int binarySearch(int[] array, int key, int low, int high){

        if (low >= high) {
            return -1;
        }

        int middle = low + (high - low) / 2;

        if (array[middle] > key) {
            return binarySearch(array, key, low, middle-1);
        } else if (array[middle] < key) {
            return binarySearch(array, key, middle + 1, high);
        } else {
            return middle;
        }
    }
}
