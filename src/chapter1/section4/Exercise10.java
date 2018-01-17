package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 9/28/16.
 */
public class Exercise10 {

    public static void main(String[] args){
        int[] testArray = {3, 4, 4, 5, 6, 10, 15, 20, 20, 20, 20, 21};
        int elementToSearch1 = 4;
        int elementToSearch2 = 20;
        int elementToSearch3 = -5;

        StdOut.println("Binary search: " + binarySearch(testArray, elementToSearch1, 0, testArray.length) +
                " Expected: 1");
        StdOut.println("Binary search: " + binarySearch(testArray, elementToSearch2, 0, testArray.length) +
                " Expected: 7");
        StdOut.println("Binary search: " + binarySearch(testArray, elementToSearch3, 0, testArray.length) +
                " Expected: -1");
    }

    private static int binarySearch(int[] array, int element, int low, int high) {

        if (low >= high) {
            return -1;
        }

        int middle = low + (high - low) / 2;

        if (array[middle] < element) {
            return binarySearch(array, element, middle+1, high);
        } else if (array[middle] > element) {
            return binarySearch(array, element, low, middle);
        } else {

            int possibleSmallestIndex = binarySearch(array, element, 0, middle - 1);
            if(possibleSmallestIndex == -1){
                return middle;
            } else{
                return binarySearch(array, element, 0, middle - 1);
            }

            //O(n)
            //int smallestIndex = middle;
//            while(smallestIndex - 1 >= 0 && array[smallestIndex - 1] == array[smallestIndex]) {
//                smallestIndex--;
//            }
            //return smallestIndex;
        }
    }
}
