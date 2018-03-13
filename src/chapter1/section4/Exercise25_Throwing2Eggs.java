package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/10/16.
 */
public class Exercise25_Throwing2Eggs {

    public static void main(String[] args) {
        int[] array = {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1};

        int[] array2 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1};

        int[] array3 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 1};

        Exercise25_Throwing2Eggs exercise25_throwing2Eggs = new Exercise25_Throwing2Eggs();

        //findFloorIn2SqrtN

        int floor = exercise25_throwing2Eggs.findFloorIn2SqrtN(array);
        StdOut.println("Floor: " + floor + " Expected: 7");

        int floor2 = exercise25_throwing2Eggs.findFloorIn2SqrtN(array2);
        StdOut.println("Floor: " + floor2 + " Expected: 0");

        int floor3 = exercise25_throwing2Eggs.findFloorIn2SqrtN(array3);
        StdOut.println("Floor: " + floor3 + " Expected: 40");

        //findFloorInCSqrtF

        int floor4 = exercise25_throwing2Eggs.findFloorInCSqrtF(array);
        StdOut.println("Floor: " + floor4 + " Expected: 7");

        int floor5 = exercise25_throwing2Eggs.findFloorInCSqrtF(array2);
        StdOut.println("Floor: " + floor5 + " Expected: 0");

        int floor6 = exercise25_throwing2Eggs.findFloorInCSqrtF(array3);
        StdOut.println("Floor: " + floor6 + " Expected: 40");
    }

    private int findFloorIn2SqrtN(int[] array) {
        int low = 0;
        int high = array.length - 1;

        return findFloorIn2SqrtN(array, low, high, 0);
    }

    /*
    Solution to Part 1: To achieve 2 * sqrt(N), drop eggs at floors
    sqrt(N), 2 * sqrt(N), 3 * sqrt(N), ..., sqrt(N) * sqrt(N).
    (For simplicity, we assume here that sqrt(N) is an integer.)
    Let assume that the egg broke at level k * sqrt(N).
    With the second egg you should then perform a linear search
    in the interval (k-1) * sqrt(N) to k * sqrt(N).
    In total you will be able to find the floor F in at most 2 * sqrt(N) trials.
     */
    private int findFloorIn2SqrtN(int[] array, int low, int high, int searchLevel) {
        int key = 1;

        if (low <= high) {
            int sqrt = (int) Math.sqrt(array.length - 1);

            int separator = sqrt * searchLevel;

            if (separator >= array.length) {
                separator = array.length - 1;
            }

            StdOut.println("Debug - current index: " + separator);

            if (key > array[separator]) {
                return findFloorIn2SqrtN(array, separator + 1, high, ++searchLevel);
            } else {
                //We broke 1 out of 2 eggs, now we do a linear search starting from a floor in which we know that the egg
                // does not break

                if (searchLevel != 0) {
                    searchLevel = searchLevel - 1;
                }

                int lastFloorThatDidNotBreak = sqrt * searchLevel;

                for(int i = lastFloorThatDidNotBreak; i <= separator; i++) {
                    StdOut.println("Debug - current index: " + i);

                    if (array[i] == 1) {
                        //2 out of 2 eggs broken, but we now have the floor number
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    private int findFloorInCSqrtF(int[] array) {
        int low = 0;
        int high = array.length - 1;

        return findFloorInCSqrtF(array, low, high, 0, 0);
    }

    //Hint from website: 1 + 2 + 3 + ... k ~ 1/2 k^2.
    private int findFloorInCSqrtF(int[] array, int low, int high, int searchElement, int increment) {
        int key = 1;

        if (low <= high) {

            searchElement = searchElement + increment;

            if (searchElement >= array.length) {
                searchElement = array.length - 1;
            }

            StdOut.println("Debug - current index: " + searchElement);

            if (key > array[searchElement]) {
                return findFloorInCSqrtF(array, searchElement + 1, high, searchElement, ++increment);
            } else {
                //We broke 1 out of 2 eggs, now we do a linear search starting from a floor in which we know that the egg
                // does not break

                searchElement = searchElement - increment;
                int lastFloorThatDidNotBreak = searchElement;

                for(int i = lastFloorThatDidNotBreak; i <= searchElement + increment; i++) {
                    StdOut.println("Debug - current index: " + i);

                    if (array[i] == 1) {
                        //2 out of 2 eggs broken, but we now have the floor number
                        return i;
                    }
                }
            }
        }

        return -1;
    }

}
