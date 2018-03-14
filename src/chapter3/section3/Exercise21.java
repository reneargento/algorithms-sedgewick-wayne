package chapter3.section3;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 19/06/17.
 */
public class Exercise21 {

    public static void main(String[] args) {

        /** Test type
         * 0- Keys()
         * 1- Min()
         * 2- Max()
         * 3- Floor()
         * 4- Ceiling()
         * 5- Select()
         * 6- Rank()
         * 7- Delete()
         * 8- DeleteMin()
         * 9- DeleteMax()
         * 10- Keys() with range
         *
         * Or no value, for all tests
         */

        int testType = -1;
        if (args.length == 1) {
            testType = Integer.parseInt(args[0]);
        }

        //Test values:
        //5 1 9 2 0 99

        //Expected tree
        //       (B)5
        //  (R)1      (B)99
        //(B)0 (B)2  (R)9

        RedBlackBST<Integer, String> redBlackBST = new RedBlackBST<>();

        while(!StdIn.isEmpty()) {
            Integer key = StdIn.readInt();
            redBlackBST.put(key, "Value " + key);
        }

        if (testType == -1 || testType == 0) {
            StdOut.println("Keys() test");

            for(Integer key : redBlackBST.keys()) {
                StdOut.println("Key " + key + ": " + redBlackBST.get(key));
            }

            StdOut.println("Expected: 0 1 2 5 9 99\n");
        }

        if (testType == -1 || testType == 1) {
            //Test min()
            StdOut.println("Min key: " + redBlackBST.min() + " Expected: 0");
        }

        if (testType == -1 || testType == 2) {
            //Test max()
            StdOut.println("Max key: " + redBlackBST.max() + " Expected: 99");
        }

        if (testType == -1 || testType == 3) {
            //Test floor()
            StdOut.println("Floor of 5: " + redBlackBST.floor(5) + " Expected: 5");
            StdOut.println("Floor of 15: " + redBlackBST.floor(15) + " Expected: 9");
        }

        if (testType == -1 || testType == 4) {
            //Test ceiling()
            StdOut.println("Ceiling of 5: " + redBlackBST.ceiling(5) + " Expected: 5");
            StdOut.println("Ceiling of 15: " + redBlackBST.ceiling(15) + " Expected: 99");
        }

        if (testType == -1 || testType == 5) {
            //Test select()
            StdOut.println("Select key of rank 4: " + redBlackBST.select(4) + " Expected: 9");
        }

        if (testType == -1 || testType == 6) {
            //Test rank()
            StdOut.println("Rank of key 9: " + redBlackBST.rank(9) + " Expected: 4");
            StdOut.println("Rank of key 10: " + redBlackBST.rank(10) + " Expected: 5");
        }

        if (testType == -1 || testType == 7) {
            //Test delete()
            StdOut.println("\nDelete key 2");
            redBlackBST.delete(2);

            for(Integer key : redBlackBST.keys()) {
                StdOut.println("Key " + key + ": " + redBlackBST.get(key));
            }
        }

        if (testType == -1 || testType == 8) {
            //Test deleteMin()
            StdOut.println("\nDelete min (key 0)");
            redBlackBST.deleteMin();

            for(Integer key : redBlackBST.keys()) {
                StdOut.println("Key " + key + ": " + redBlackBST.get(key));
            }
        }

        if (testType == -1 || testType == 9) {
            //Test deleteMax()
            StdOut.println("\nDelete max (key 99)");
            redBlackBST.deleteMax();

            for(Integer key : redBlackBST.keys()) {
                StdOut.println("Key " + key + ": " + redBlackBST.get(key));
            }
        }

        if (testType == -1 || testType == 10) {
            //Test keys() with range
            StdOut.println("\nKeys in range [2, 10]");
            for(Integer key : redBlackBST.keys(2, 10)) {
                StdOut.println("Key " + key + ": " + redBlackBST.get(key));
            }
        }
    }

}
