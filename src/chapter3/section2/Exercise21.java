package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 02/06/17.
 */
public class Exercise21 {

    private class BinarySearchTree<Key extends Comparable<Key>, Value> extends chapter3.section2.BinarySearchTree<Key, Value> {

        public Key randomKey() {
            if(size() == 0) {
                return null;
            }

            Node current = root;

            while (current != null) {
                int randomMove = StdRandom.uniform(3);
                //0 - Move left
                //1 - Move right
                //2 - Choose the current key
                if(randomMove == 0) {
                    if(current.left != null) {
                        current = current.left;
                    } else {
                        return current.key;
                    }
                } else if(randomMove == 1) {
                    if(current.right != null) {
                        current = current.right;
                    } else {
                        return current.key;
                    }
                } else {
                    return current.key;
                }
            }

            return null;
        }
    }

    public static void main(String[] args) {
        Exercise21 exercise21 = new Exercise21();
        BinarySearchTree<Integer, String> binarySearchTree = exercise21.new BinarySearchTree<>();

        //Test put()
        binarySearchTree.put(5, "Value 5");
        binarySearchTree.put(1, "Value 1");
        binarySearchTree.put(9, "Value 9");
        binarySearchTree.put(2, "Value 2");
        binarySearchTree.put(0, "Value 0");
        binarySearchTree.put(99, "Value 99");

        StdOut.println();

        //Test size()
        StdOut.println("Size: " + binarySearchTree.size() + " Expected: 6");

        //Test get() and keys()
        for(Integer key : binarySearchTree.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
        }

        StdOut.println("Random keys:");
        StdOut.println(binarySearchTree.randomKey());
        StdOut.println(binarySearchTree.randomKey());
        StdOut.println(binarySearchTree.randomKey());
        StdOut.println(binarySearchTree.randomKey());
        StdOut.println(binarySearchTree.randomKey());
        StdOut.println(binarySearchTree.randomKey());
    }

}
