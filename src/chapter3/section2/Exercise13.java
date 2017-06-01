package chapter3.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by rene on 30/05/17.
 */
public class Exercise13 {

    private class BinarySearchTree<Key extends Comparable<Key>, Value>{

        private class Node {
            private Key key;
            private Value value;

            private Node left;
            private Node right;

            private int size; //# of nodes in subtree rooted here

            public Node(Key key, Value value, int size) {
                this.key = key;
                this.value = value;
                this.size = size;
            }
        }

        private Node root;

        public int size() {
            return size(root);
        }

        private int size(Node node) {
            if(node == null) {
                return 0;
            }

            return node.size;
        }

        public Value get(Key key) {

            Node current = root;

            while (current != null) {
                int compare = key.compareTo(current.key);

                if(compare < 0) {
                    current = current.left;
                } else if(compare > 0) {
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

                if(compare < 0) {
                    current = current.left;
                } else if (compare > 0) {
                    current = current.right;
                } else {
                    current.value = value;
                    keyExists = true;
                    break;
                }
            }

            if(keyExists) {
                return;
            }

            //Second pass
            //If we reached here, the key does not exist yet

            if(root == null) {
                root = new Node(key, value, 1);
                return;
            }

            current = root;

            while (current != null) {

                int compare = key.compareTo(current.key);
                current.size = current.size + 1;

                if(compare < 0) {

                    if(current.left != null) {
                        current = current.left;
                    } else {
                        current.left = new Node(key, value, 1);
                        break;
                    }
                } else if (compare > 0) {

                    if(current.right != null) {
                        current = current.right;
                    } else {
                        current.right = new Node(key, value, 1);
                        break;
                    }
                }
            }
        }

        public Key min() {
            if(root == null) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            return min(root).key;
        }

        private Node min(Node node) {
            if(node.left == null) {
                return node;
            }

            return min(node.left);
        }

        public Key max() {
            if(root == null) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            return max(root).key;
        }

        private Node max(Node node) {
            if(node.right == null) {
                return node;
            }

            return max(node.right);
        }

        public Key floor(Key key) {
            Node node = floor(root, key);
            if(node == null) {
                return null;
            }

            return node.key;
        }

        private Node floor(Node node, Key key) {
            if(node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);

            if(compare == 0) {
                return node;
            } else if(compare < 0) {
                return floor(node.left, key);
            } else {
                Node rightNode = floor(node.right, key);
                if(rightNode != null) {
                    return rightNode;
                } else {
                    return node;
                }
            }
        }

        public Key ceiling(Key key) {
            Node node = ceiling(root, key);
            if(node == null) {
                return null;
            }

            return node.key;
        }

        private Node ceiling(Node node, Key key) {
            if(node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);

            if(compare == 0) {
                return node;
            } else if(compare > 0) {
                return ceiling(node.right, key);
            } else {
                Node leftNode = ceiling(node.left, key);
                if(leftNode != null) {
                    return leftNode;
                } else {
                    return node;
                }
            }
        }

        public Key select(int index) {
            if(index >= size()) {
                throw new IllegalArgumentException("Index is higher than tree size");
            }

            return select(root, index).key;
        }

        private Node select(Node node, int index) {
            int leftSubtreeSize = size(node.left);

            if(leftSubtreeSize == index) {
                return node;
            } else if (leftSubtreeSize > index) {
                return select(node.left, index);
            } else {
                return select(node.right, index - leftSubtreeSize - 1);
            }
        }

        public int rank(Key key) {
            return rank(root, key);
        }

        private int rank(Node node, Key key) {
            if(node == null) {
                return 0;
            }

            //Returns the number of keys less than node.key in the subtree rooted at node
            int compare = key.compareTo(node.key);
            if(compare < 0) {
                return rank(node.left, key);
            } else if(compare > 0) {
                return size(node.left) + 1 + rank(node.right, key);
            } else {
                return size(node.left);
            }
        }

        public void deleteMin() {
            root = deleteMin(root);
        }

        private Node deleteMin(Node node) {
            if(node == null) {
                return null;
            }

            if(node.left == null) {
                return node.right;
            }

            node.left = deleteMin(node.left);
            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        public void deleteMax() {
            root = deleteMax(root);
        }

        private Node deleteMax(Node node) {
            if(node == null) {
                return null;
            }

            if(node.right == null) {
                return node.left;
            }

            node.right = deleteMax(node.right);
            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        public void delete(Key key) {
            root = delete(root, key);
        }

        private Node delete(Node node, Key key) {
            if(node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);
            if(compare < 0) {
                node.left = delete(node.left, key);
            } else if(compare > 0) {
                node.right = delete(node.right, key);
            } else {
                if(node.left == null) {
                    return node.right;
                } else if (node.right == null) {
                    return node.left;
                } else {
                    Node aux = node;
                    node = min(aux.right);
                    node.right = deleteMin(aux.right);
                    node.left = aux.left;
                }
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        public Iterable<Key> keys() {
            return keys(min(), max());
        }

        public Iterable<Key> keys(Key low, Key high) {
            Queue<Key> queue = new Queue<>();
            keys(root, queue, low, high);
            return queue;
        }

        private void keys(Node node, Queue<Key> queue, Key low, Key high) {
            if(node == null) {
                return;
            }

            int compareLow = low.compareTo(node.key);
            int compareHigh = high.compareTo(node.key);

            if(compareLow < 0) {
                keys(node.left, queue, low, high);
            }

            if(compareLow <= 0 && compareHigh >= 0) {
                queue.enqueue(node.key);
            }

            if(compareHigh > 0) {
                keys(node.right, queue, low, high);
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
