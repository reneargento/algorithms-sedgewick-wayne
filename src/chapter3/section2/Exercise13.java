package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 30/05/17.
 */
public class Exercise13 {

    private class BinarySearchTree<Key extends Comparable<Key>, Value> extends chapter3.section2.BinarySearchTree<Key, Value>{

        public Value get(Key key) {

            Node current = root;

            while (current != null) {
                int compare = key.compareTo(current.key);

                if (compare < 0) {
                    current = current.left;
                } else if (compare > 0) {
                    current = current.right;
                } else {
                    return current.value;
                }
            }

            return null;
        }

        public void put(Key key, Value value) {
            //First pass to check if key already exists - needed to know whether to update the node's sizes on the second pass
            boolean keyExists = false;

            Node current = root;

            while (current != null) {
                int compare = key.compareTo(current.key);

                if (compare < 0) {
                    current = current.left;
                } else if (compare > 0) {
                    current = current.right;
                } else {
                    current.value = value;
                    keyExists = true;
                    break;
                }
            }

            if (keyExists) {
                return;
            }

            //Second pass
            //If we reached here, the key does not exist yet

            if (root == null) {
                root = new Node(key, value, 1);
                return;
            }

            current = root;

            while (current != null) {

                int compare = key.compareTo(current.key);
                current.size = current.size + 1;

                if (compare < 0) {

                    if (current.left != null) {
                        current = current.left;
                    } else {
                        current.left = new Node(key, value, 1);
                        break;
                    }
                } else if (compare > 0) {

                    if (current.right != null) {
                        current = current.right;
                    } else {
                        current.right = new Node(key, value, 1);
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Exercise13 exercise13 = new Exercise13();
        BinarySearchTree<Integer, String> binarySearchTree = exercise13.new BinarySearchTree<>();

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

        //Test delete()
        StdOut.println("\nDelete key 2");
        binarySearchTree.delete(2);
        for(Integer key : binarySearchTree.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
        }

        StdOut.println();

        //Test size()
        StdOut.println("Size: " + binarySearchTree.size() + " Expected: 5");

        //Test min()
        StdOut.println("Min key: " + binarySearchTree.min() + " Expected: 0");

        //Test max()
        StdOut.println("Max key: " + binarySearchTree.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + binarySearchTree.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + binarySearchTree.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + binarySearchTree.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + binarySearchTree.ceiling(15) + " Expected: 99");

        //Test select()
        StdOut.println("Select key of rank 4: " + binarySearchTree.select(4) + " Expected: 99");

        //Test rank()
        StdOut.println("Rank of key 9: " + binarySearchTree.rank(9) + " Expected: 3");
        StdOut.println("Rank of key 10: " + binarySearchTree.rank(10) + " Expected: 4");

        //Test deleteMin()
        StdOut.println("\nDelete min (key 0)");

        binarySearchTree.deleteMin();
        for(Integer key : binarySearchTree.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
        }

        //Test deleteMax()
        StdOut.println("\nDelete max (key 99)");

        binarySearchTree.deleteMax();
        for(Integer key : binarySearchTree.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
        }

        //Test keys() with range
        StdOut.println();
        StdOut.println("Keys in range [2, 10]");
        for(Integer key : binarySearchTree.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + binarySearchTree.get(key));
        }

        StdOut.println("Size: " + binarySearchTree.size() + " Expected: 3");
    }

}
