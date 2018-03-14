package chapter3.section2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 28/05/17.
 */
public class Exercise10 {

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

        BinarySearchTree<Integer, String> binarySearchTree = new BinarySearchTree<>();

        while(!StdIn.isEmpty()) {
            Integer key = StdIn.readInt();
            binarySearchTree.put(key, "Value " + key);
        }

        if (testType == -1 || testType == 0) {
            StdOut.println("Keys() test");

            for(Integer key : binarySearchTree.keys()) {
                StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
            }

            StdOut.println("Expected: 0 1 2 5 9 99\n");
        }

        if (testType == -1 || testType == 1) {
            //Test min()
            StdOut.println("Min key: " + binarySearchTree.min() + " Expected: 0");
        }

        if (testType == -1 || testType == 2) {
            //Test max()
            StdOut.println("Max key: " + binarySearchTree.max() + " Expected: 99");
        }

        if (testType == -1 || testType == 3) {
            //Test floor()
            StdOut.println("Floor of 5: " + binarySearchTree.floor(5) + " Expected: 5");
            StdOut.println("Floor of 15: " + binarySearchTree.floor(15) + " Expected: 9");
        }

        if (testType == -1 || testType == 4) {
            //Test ceiling()
            StdOut.println("Ceiling of 5: " + binarySearchTree.ceiling(5) + " Expected: 5");
            StdOut.println("Ceiling of 15: " + binarySearchTree.ceiling(15) + " Expected: 99");
        }

        if (testType == -1 || testType == 5) {
            //Test select()
            StdOut.println("Select key of rank 4: " + binarySearchTree.select(4) + " Expected: 9");
        }

        if (testType == -1 || testType == 6) {
            //Test rank()
            StdOut.println("Rank of key 9: " + binarySearchTree.rank(9) + " Expected: 4");
            StdOut.println("Rank of key 10: " + binarySearchTree.rank(10) + " Expected: 5");
        }

        if (testType == -1 || testType == 7) {
            //Test delete()
            StdOut.println("\nDelete key 2");
            binarySearchTree.delete(2);

            for(Integer key : binarySearchTree.keys()) {
                StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
            }
        }

        if (testType == -1 || testType == 8) {
            //Test deleteMin()
            StdOut.println("\nDelete min (key 0)");
            binarySearchTree.deleteMin();

            for(Integer key : binarySearchTree.keys()) {
                StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
            }
        }

        if (testType == -1 || testType == 9) {
            //Test deleteMax()
            StdOut.println("\nDelete max (key 99)");
            binarySearchTree.deleteMax();

            for(Integer key : binarySearchTree.keys()) {
                StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
            }
        }

        if (testType == -1 || testType == 10) {
            //Test keys() with range
            StdOut.println("\nKeys in range [2, 10]");
            for(Integer key : binarySearchTree.keys(2, 10)) {
                StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
            }
        }
    }

}
