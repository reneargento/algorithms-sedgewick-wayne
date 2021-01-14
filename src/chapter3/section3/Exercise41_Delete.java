package chapter3.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 22/06/17.
 */
public class Exercise41_Delete {

    private class RedBlackBSTDelete<Key extends Comparable<Key>, Value> extends RedBlackBST<Key, Value> {

        public void delete(Key key) {
            if (isEmpty()) {
                return;
            }

            if (!contains(key)) {
                return;
            }

            if (!isRed(root.left) && !isRed(root.right)) {
                root.color = RED;
            }

            root = delete(root, key);

            if (!isEmpty()) {
                root.color = BLACK;
            }
        }

        private Node delete(Node node, Key key) {
            if (node == null) {
                return null;
            }

            if (key.compareTo(node.key) < 0) {
                if (!isRed(node.left) && node.left != null && !isRed(node.left.left)) {
                    node = moveRedLeft(node);
                }

                node.left = delete(node.left, key);
            } else {
                if (isRed(node.left)) {
                    node = rotateRight(node);
                }

                if (key.compareTo(node.key) == 0 && node.right == null) {
                    return null;
                }

                if (!isRed(node.right) && node.right != null && !isRed(node.right.left)) {
                    node = moveRedRight(node);
                }

                if (key.compareTo(node.key) == 0) {
                    Node aux = min(node.right);
                    node.key = aux.key;
                    node.value = aux.value;
                    node.right = deleteMin(node.right);
                } else {
                    node.right = delete(node.right, key);
                }
            }

            return balance(node);
        }

        protected Node moveRedLeft(Node node) {
            flipColors(node);

            if (node.right != null && isRed(node.right.left)) {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
                flipColors(node);
            }

            return node;
        }

        protected Node moveRedRight(Node node) {
            flipColors(node);

            if (node.left != null && isRed(node.left.left)) {
                node = rotateRight(node);
                flipColors(node);
            }

            return node;
        }

        protected Node balance(Node node) {
            if (node == null) {
                return null;
            }

            if (isRed(node.right) && !isRed(node.left)) {
                node = rotateLeft(node);
            }

            if (isRed(node.left) && node.left != null && isRed(node.left.left)) {
                node = rotateRight(node);
            }

            if (isRed(node.left) && isRed(node.right)) {
                flipColors(node);
            }

            node.size = size(node.left) + 1 + size(node.right);

            return node;
        }

        protected void flipColors(Node node) {
            if (node != null) {
                node.color = !node.color;

                if (node.left != null) {
                    node.left.color = !node.left.color;
                }

                if (node.right != null) {
                    node.right.color = !node.right.color;
                }
            }
        }

    }

    public static void main(String[] args) {
        Exercise41_Delete delete = new Exercise41_Delete();
        RedBlackBSTDelete<Integer, Integer> redBlackBST = delete.new RedBlackBSTDelete<>();

        redBlackBST.put(10, 10);
        redBlackBST.put(4, 4);
        redBlackBST.put(6, 6);
        redBlackBST.put(1, 1);
        redBlackBST.put(2, 2);
        redBlackBST.put(15, 15);
        redBlackBST.put(12, 12);

        StdOut.println("Keys");
        delete.printKeys(redBlackBST);

        StdOut.println("Delete 1");
        redBlackBST.delete(1);
        delete.printKeys(redBlackBST);

        StdOut.println("Delete 15");
        redBlackBST.delete(15);
        delete.printKeys(redBlackBST);

        StdOut.println("Delete 10");
        redBlackBST.delete(10);
        delete.printKeys(redBlackBST);

        StdOut.println("Delete 6");
        redBlackBST.delete(6);
        delete.printKeys(redBlackBST);

        while (redBlackBST.size() > 0) {
            redBlackBST.delete(redBlackBST.select(0));
        }

        StdOut.println("Final size after deleting all keys: " + redBlackBST.size());
    }

    private void printKeys(RedBlackBSTDelete<Integer, Integer> redBlackBST) {
        for(Integer key : redBlackBST.keys()) {
            StdOut.println(key);
        }
        StdOut.println();
    }
}
