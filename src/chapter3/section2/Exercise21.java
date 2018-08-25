package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 02/06/17.
 */
// Thanks to Daniel Bedrenko (https://github.com/dbedrenko) for correcting and suggesting a better implementation
// for this exercise: https://github.com/reneargento/algorithms-sedgewick-wayne/issues/19
// Based on https://github.com/ChangeMyUsername/algorithms-sedgewick-python/blob/master/chapter_3/module_3_2.py
public class Exercise21 {

    private class BinarySearchTree<Key extends Comparable<Key>, Value> extends chapter3.section2.BinarySearchTree<Key, Value> {

        public Key randomKey() {
            if (isEmpty()) {
                return null;
            }

            int randomIndex = StdRandom.uniform(size());
            return select(randomIndex);
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

        //Test size()
        StdOut.println("Size: " + binarySearchTree.size() + " Expected: 6\n");

        //Test get() and keys()
        for(Integer key : binarySearchTree.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
        }

        StdOut.println("\nRandom keys:");
        StdOut.println(binarySearchTree.randomKey());
        StdOut.println(binarySearchTree.randomKey());
        StdOut.println(binarySearchTree.randomKey());
        StdOut.println(binarySearchTree.randomKey());
        StdOut.println(binarySearchTree.randomKey());
        StdOut.println(binarySearchTree.randomKey());
    }

}
