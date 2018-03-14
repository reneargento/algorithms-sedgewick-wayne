package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 04/06/17.
 */
public class Exercise33_SelectRankCheck {

    private boolean checkRankAndSelect(BinarySearchTree<Integer, String> binarySearchTree) {
        int size = binarySearchTree.size();

        //Check rank
        for(int i = 0; i < size; i++) {
            if (i != binarySearchTree.rank(binarySearchTree.select(i))) {
                return false;
            }
        }

        //Check select
        for(Integer key : binarySearchTree.keys()) {
            if (key.compareTo(binarySearchTree.select(binarySearchTree.rank(key))) != 0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer, String> binarySearchTree = new BinarySearchTree<>();
        binarySearchTree.put(10, "Value 10");
        binarySearchTree.put(4, "Value 4");
        binarySearchTree.put(6, "Value 6");
        binarySearchTree.put(1, "Value 1");
        binarySearchTree.put(15, "Value 15");
        binarySearchTree.put(12, "Value 12");
        binarySearchTree.put(20, "Value 20");
        binarySearchTree.put(25, "Value 25");

        Exercise33_SelectRankCheck selectRankCheck = new Exercise33_SelectRankCheck();
        StdOut.println(selectRankCheck.checkRankAndSelect(binarySearchTree) + " Expected: true");
    }

}
