package chapter1.section1;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento
 */
public class Exercise23 {

    // Arguments example:
    // 1.1.23.txt
    // +
    public static void main(String[] args) {
        String fileName = Constants.FILES_PATH + args[0];
        int[] whitelist = (new In(fileName)).readAllInts();
        char operation = args[1].charAt(0);

        if (operation != '-' && operation != '+') {
            throw new IllegalArgumentException("Operation needs to be - or +");
        }

        boolean searchForWhiteList = operation == '-';
        Arrays.sort(whitelist);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            int index = binarySearch(key, whitelist);

            if (index != -1 && searchForWhiteList) {
                StdOut.println("Number in whitelist: " + key);
            } else if (index == -1 && !searchForWhiteList) {
                StdOut.println("Number not in whitelist: " + key);
            }
        }
    }
    
    private static int binarySearch(int key, int[] numbers) {
        return rank(key, numbers, 0, numbers.length - 1);
    }

    private static int rank(int key, int[] arr, int lo, int hi) {
        if (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (key < arr[mid]) {
                return rank(key, arr, lo, mid-1);
            } else if (key > arr[mid]) {
                return rank(key, arr, mid+1, hi);
            } else {
                return mid;
            }
        } else {
            return -1;
        }
    }
}
