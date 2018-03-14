package chapter3.section3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 28/06/17.
 */
/**
 * The exercise does not specify how to compute the color of nodes with 0 or 1 children.
 * In order to get all the operations working correctly, the color verification occurs as following:
 *  If a node has two children: if the left child is larger than the right child, it is red. Otherwise, it is black.
 *  If a node has one or zero children: if it has no sibling, it is red. Otherwise, it is black.
 */
public class Exercise29_OptimalStorage {

    private class RedBlackBSTOptimalStorage<Key extends Comparable<Key>, Value> {

        private class Node {
            Key key;
            Value value;
            Node left, right;

            int size;

            Node(Key key, Value value, int size) {
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
            if (node == null) {
                return 0;
            }

            return node.size;
        }

        public boolean isEmpty() {
            return size(root) == 0;
        }

        private void swapChildLinks(Node node) {
            if (node == null) {
                return;
            }

            Node aux = node.left;
            node.left = node.right;
            node.right = aux;
        }

        //This operation now takes O(lg N) time instead of O(1)
        private boolean isRed(Node node) {
            if (node == null) {
                return false;
            }

            //The root is always black if it is the only node in the tree
            if (node == root && size(root) == 1) {
                return false;
            }
            //If a node has no sibling, it is red
            //Find the node to check if it has a sibling
            Node current = root;
            while (current != null) {
                if (current.left == node && current.right == null) {
                    return true;
                }
                if (current.right == node && current.left == null) {
                    return true;
                }
                //The node has a sibling, so do the default check
                if (current.left == node || current.right == node) {
                    break;
                }

                //Continue the search for the node
                int compare = node.key.compareTo(current.key);
                if (current.left == null || current.right == null) {
                    if (current.left == null) {
                        current = current.right;
                    } else {
                        current = current.left;
                    }
                } else {
                    boolean isRed = current.left.key.compareTo(current.right.key) > 0;

                    if (!isRed) {
                        if (compare < 0) {
                            current = current.left;
                        } else if (compare > 0) {
                            current = current.right;
                        } else {
                            break;
                        }
                    } else {
                        if (compare < 0) {
                            current = current.right;
                        } else if (compare > 0) {
                            current = current.left;
                        } else {
                            break;
                        }
                    }
                }
            }

            if (node.left == null || node.right == null) {
                return false;
            }

            return node.left.key.compareTo(node.right.key) > 0;
        }

        private Node rotateLeft(Node node) {
            if (node == null) {
                return null;
            }

            if (!isRed(node)) {
                if (node.right == null) {
                    return node;
                }
            } else {
                if (node.left == null) {
                    return node;
                }
            }

            if (isRed(node)) {
                swapChildLinks(node);
            }

            boolean isNewRootRed = isRed(node.right);
            if (isNewRootRed) {
                swapChildLinks(node.right);
            }

            Node newRoot = node.right;

            node.right = newRoot.left;
            newRoot.left = node;

            if (isRed(node)) {
                swapChildLinks(newRoot);
            }

            //After a rotation, paint the node red
            if (!isRed(node)) {
                swapChildLinks(node);
            }

            newRoot.size = node.size;
            node.size = size(node.left) + 1 + size(node.right);

            return newRoot;
        }

        private Node rotateRight(Node node) {
            if (node == null) {
                return null;
            }

            if (!isRed(node)) {
                if (node.left == null) {
                    return node;
                }
            } else {
                if (node.right == null) {
                    return node;
                }
            }

            if (isRed(node)) {
                swapChildLinks(node);
            }

            boolean isNewRootRed = isRed(node.left);
            if (isNewRootRed) {
                swapChildLinks(node.left);
            }

            Node newRoot = node.left;

            node.left = newRoot.right;
            newRoot.right = node;

            //After a rotation, paint the node red
            if (!isRed(node)) {
                swapChildLinks(node);
            }
            //Since in this configuration nodes without children are by default black,
            // the new root becomes red and its children become black
            if (!isRed(newRoot) && newRoot.left != null && newRoot.right != null) {
                swapChildLinks(newRoot);

                swapChildLinks(newRoot.left);
                swapChildLinks(newRoot.right);
            }

            newRoot.size = node.size;
            node.size = size(node.left) + 1 + size(node.right);

            return newRoot;
        }

        private void flipColors(Node node) {
            if (node == null || node.left == null || node.right == null) {
                return;
            }

            //The root must have opposite color of its two children
            if ((isRed(node) && !isRed(node.left) && !isRed(node.right))
                    || (!isRed(node) && isRed(node.left) && isRed(node.right))) {
                swapChildLinks(node);
                swapChildLinks(node.left);
                swapChildLinks(node.right);
            }
        }

        public void put(Key key, Value value) {
            if (key == null) {
                return;
            }

            if (value == null) {
                delete(key);
                return;
            }

            root = put(root, key, value);
            if (isRed(root)) {
                swapChildLinks(root);
            }
        }

        private Node put(Node node, Key key, Value value) {
            if (node == null) {
                return new Node(key, value, 1);
            }

            int compare = key.compareTo(node.key);

            if (compare < 0) {
                if (!isRed(node)) {
                    node.left = put(node.left, key, value);
                } else {
                    node.right = put(node.right, key, value);
                }
            } else if (compare > 0) {
                if (!isRed(node)) {
                    node.right = put(node.right, key, value);
                } else {
                    node.left = put(node.left, key, value);
                }
            } else {
                node.value = value;
            }

            if (!isRed(node)) {
                if (isRed(node.right) && !isRed(node.left)) {
                    node = rotateLeft(node);
                }
            } else {
                if (isRed(node.left) && !isRed(node.right)) {
                    node = rotateLeft(node);
                }
            }

            if (!isRed(node)) {
                if (!isRed(node.left)) {
                    if (isRed(node.left) && isRed(node.left.left)) {
                        node = rotateRight(node);
                    }
                } else {
                    if (isRed(node.left) && isRed(node.left.right)) {
                        node = rotateRight(node);
                    }
                }
            } else {
                if (!isRed(node.right)) {
                    if (isRed(node.right) && isRed(node.right.left)) {
                        node = rotateRight(node);
                    }
                } else {
                    if (isRed(node.right) && isRed(node.right.right)) {
                        node = rotateRight(node);
                    }
                }
            }

            if (isRed(node.left) && isRed(node.right)) {
                flipColors(node);
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        public Value get(Key key) {
            if (key == null) {
                return null;
            }

            return get(root, key);
        }

        private Value get(Node node, Key key) {
            if (node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);
            if (compare < 0) {
                if (!isRed(node)) {
                    return get(node.left, key);
                } else {
                    return get(node.right, key);
                }
            } else if (compare > 0) {
                if (!isRed(node)) {
                    return get(node.right, key);
                } else {
                    return get(node.left, key);
                }
            } else {
                return node.value;
            }
        }

        public boolean contains(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to contains() cannot be null");
            }
            return get(key) != null;
        }

        public Key min() {
            if (root == null) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            return min(root).key;
        }

        private Node min(Node node) {
            if (node.left == null) {
                return node;
            }

            if (!isRed(node)) {
                return min(node.left);
            } else {
                return min(node.right);
            }
        }

        public Key max() {
            if (root == null) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            return max(root).key;
        }

        private Node max(Node node) {
            if (node.right == null) {
                return node;
            }

            if (!isRed(node)) {
                return max(node.right);
            } else {
                return max(node.left);
            }
        }

        //Returns the highest key in the symbol table smaller than or equal to key.
        public Key floor(Key key) {
            Node node = floor(root, key);
            if (node == null) {
                return null;
            }

            return node.key;
        }

        private Node floor(Node node, Key key) {
            if (node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);

            if (compare == 0) {
                return node;
            } else if (compare < 0) {
                if (!isRed(node)) {
                    return floor(node.left, key);
                } else {
                    return floor(node.right, key);
                }
            } else {
                Node rightNode;

                if (!isRed(node)) {
                    rightNode = floor(node.right, key);
                } else {
                    rightNode = floor(node.left, key);
                }

                if (rightNode != null) {
                    return rightNode;
                } else {
                    return node;
                }
            }
        }

        //Returns the smallest key in the symbol table greater than or equal to key.
        public Key ceiling(Key key) {
            Node node = ceiling(root, key);
            if (node == null) {
                return null;
            }

            return node.key;
        }

        private Node ceiling(Node node, Key key) {
            if (node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);

            if (compare == 0) {
                return node;
            } else if (compare > 0) {
                if (!isRed(node)) {
                    return ceiling(node.right, key);
                } else {
                    return ceiling(node.left, key);
                }
            } else {
                Node leftNode;

                if (!isRed(node)) {
                    leftNode = ceiling(node.left, key);
                } else {
                    leftNode = ceiling(node.right, key);;
                }

                if (leftNode != null) {
                    return leftNode;
                } else {
                    return node;
                }
            }
        }

        public Key select(int index) {
            if (index >= size()) {
                throw new IllegalArgumentException("Index is higher than tree size");
            }

            return select(root, index).key;
        }

        private Node select(Node node, int index) {
            int leftSubtreeSize;

            if (!isRed(node)) {
                leftSubtreeSize = size(node.left);
            } else {
                leftSubtreeSize = size(node.right);
            }

            if (leftSubtreeSize == index) {
                return node;
            } else if (leftSubtreeSize > index) {
                if (!isRed(node)) {
                    return select(node.left, index);
                } else {
                    return select(node.right, index);
                }
            } else {
                if (!isRed(node)) {
                    return select(node.right, index - leftSubtreeSize - 1);
                } else {
                    return select(node.left, index - leftSubtreeSize - 1);
                }
            }
        }

        public int rank(Key key) {
            return rank(root, key);
        }

        private int rank(Node node, Key key) {
            if (node == null) {
                return 0;
            }

            //Returns the number of keys less than node.key in the subtree rooted at node
            int compare = key.compareTo(node.key);
            if (compare < 0) {
                if (!isRed(node)) {
                    return rank(node.left, key);
                } else {
                    return rank(node.right, key);
                }
            } else if (compare > 0) {
                if (!isRed(node)) {
                    return size(node.left) + 1 + rank(node.right, key);
                } else {
                    return size(node.right) + 1 + rank(node.left, key);
                }
            } else {
                if (!isRed(node)) {
                    return size(node.left);
                } else {
                    return size(node.right);
                }
            }
        }

        public void deleteMin() {
            if (isEmpty()) {
                return;
            }

            if (!isRed(root.left) && !isRed(root.right)) {
                if (!isRed(root)) {
                    swapChildLinks(root);
                }
            }

            root = deleteMin(root);

            if (!isEmpty() && isRed(root)) {
                swapChildLinks(root);
            }
        }

        private Node deleteMin(Node node) {
            if (node.left == null) {
                return null;
            }

            if (!isRed(node)) {
                if (!isRed(node.left)) {
                    if (!isRed(node.left) && node.left != null && !isRed(node.left.left)) {
                        node = moveRedLeft(node);
                    }
                } else {
                    if (!isRed(node.left) && node.left != null && !isRed(node.left.right)) {
                        node = moveRedLeft(node);
                    }
                }
            } else {
                if (!isRed(node.right)) {
                    if (!isRed(node.right) && node.right != null && !isRed(node.right.left)) {
                        node = moveRedLeft(node);
                    }
                } else {
                    if (!isRed(node.right) && node.right != null && !isRed(node.right.right)) {
                        node = moveRedLeft(node);
                    }
                }
            }

            if (!isRed(node)) {
                node.left = deleteMin(node.left);
            } else {
                node.right = deleteMin(node.right);
            }

            return balance(node);
        }

        public void deleteMax() {
            if (isEmpty()) {
                return;
            }

            if (!isRed(root.left) && !isRed(root.right)) {
                if (!isRed(root)) {
                    swapChildLinks(root);
                }
            }

            root = deleteMax(root);

            if (!isEmpty() && isRed(root)) {
                swapChildLinks(root);
            }
        }

        private Node deleteMax(Node node) {
            if (!isRed(node)) {
                if (isRed(node.left)) {
                    node = rotateRight(node);
                }
            } else {
                if (isRed(node.right)) {
                    node = rotateRight(node);
                }
            }

            if (node.right == null) {
                return null;
            }

            if (!isRed(node)) {
                if (!isRed(node.right)) {
                    if (!isRed(node.right) && node.right != null && !isRed(node.right.left)) {
                        node = moveRedRight(node);
                    }
                } else {
                    if (!isRed(node.right) && node.right != null && !isRed(node.right.right)) {
                        node = moveRedRight(node);
                    }
                }
            } else {
                if (!isRed(node.right)) {
                    if (!isRed(node.left) && node.left != null && !isRed(node.left.left)) {
                        node = moveRedRight(node);
                    }
                } else {
                    if (!isRed(node.left) && node.left != null && !isRed(node.left.right)) {
                        node = moveRedRight(node);
                    }
                }
            }

            if (!isRed(node)) {
                node.right = deleteMax(node.right);
            } else {
                node.left = deleteMax(node.left);
            }

            return balance(node);
        }

        public void delete(Key key) {
            if (isEmpty()) {
                return;
            }

            if (!contains(key)) {
                return;
            }

            if (!isRed(root.left) && !isRed(root.right)) {
                if (!isRed(root)) {
                    swapChildLinks(root);
                }
            }

            root = delete(root, key);

            if (!isEmpty() && isRed(root)) {
                swapChildLinks(root);
            }
        }

        private Node delete(Node node, Key key) {
            if (node == null) {
                return null;
            }

            if (key.compareTo(node.key) < 0) {
                if (!isRed(node)) {
                    if (!isRed(node.left)) {
                        if (!isRed(node.left) && node.left != null && !isRed(node.left.left)) {
                            node = moveRedLeft(node);
                        }
                    } else {
                        if (!isRed(node.left) && node.left != null && !isRed(node.left.right)) {
                            node = moveRedLeft(node);
                        }
                    }
                } else {
                    if (!isRed(node.right)) {
                        if (!isRed(node.right) && node.right != null && !isRed(node.right.left)) {
                            node = moveRedLeft(node);
                        }
                    } else {
                        if (!isRed(node.right) && node.right != null && !isRed(node.right.right)) {
                            node = moveRedLeft(node);
                        }
                    }
                }

                if (!isRed(node)) {
                    node.left = delete(node.left, key);
                } else {
                    node.right = delete(node.right, key);
                }
            } else {
                if (!isRed(node)) {
                    if (isRed(node.left)) {
                        node = rotateRight(node);
                    }
                } else {
                    if (isRed(node.right)) {
                        node = rotateRight(node);
                    }
                }

                if (key.compareTo(node.key) == 0 && node.right == null) {
                    return null;
                }

                if (!isRed(node)) {
                    if (!isRed(node.right)) {
                        if (!isRed(node.right) && node.right != null && !isRed(node.right.left)) {
                            node = moveRedRight(node);
                        }
                    } else {
                        if (!isRed(node.right) && node.right != null && !isRed(node.right.right)) {
                            node = moveRedRight(node);
                        }
                    }
                } else {
                    if (!isRed(node.right)) {
                        if (!isRed(node.left) && node.left != null && !isRed(node.left.left)) {
                            node = moveRedRight(node);
                        }
                    } else {
                        if (!isRed(node.left) && node.left != null && !isRed(node.left.right)) {
                            node = moveRedRight(node);
                        }
                    }
                }

                if (key.compareTo(node.key) == 0) {
                    Node aux;

                    if (!isRed(node)) {
                        aux = min(node.right);
                    } else {
                        aux = min(node.left);
                    }

                    node.key = aux.key;
                    node.value = aux.value;

                    if (!isRed(node)) {
                        node.right = deleteMin(node.right);
                    } else {
                        node.left = deleteMin(node.left);
                    }
                } else {
                    if (!isRed(node)) {
                        node.right = delete(node.right, key);
                    } else {
                        node.left = delete(node.left, key);
                    }
                }
            }

            return balance(node);
        }

        private Node moveRedLeft(Node node) {
            //Assuming that node is red and both node.left and node.left.left are black,
            // make node.left or one of its children red
            flipColors(node);

            if (!isRed(node)) {
                if (!isRed(node.right)) {
                    if (node.right != null && isRed(node.right.left)) {
                        node.right = rotateRight(node.right);
                        node = rotateLeft(node);
                        flipColors(node);
                    }
                } else {
                    if (node.right != null && isRed(node.right.right)) {
                        node.right = rotateRight(node.right);
                        node = rotateLeft(node);
                        flipColors(node);
                    }
                }
            } else {
                if (!isRed(node.right)) {
                    if (node.left != null && isRed(node.left.left)) {
                        node.left = rotateRight(node.left);
                        node = rotateLeft(node);
                        flipColors(node);
                    }
                } else {
                    if (node.left != null && isRed(node.left.right)) {
                        node.left = rotateRight(node.left);
                        node = rotateLeft(node);
                        flipColors(node);
                    }
                }
            }

            return node;
        }

        private Node moveRedRight(Node node) {
            //Assuming that node is red and both node.right and node.right.left are black,
            // make node.right or one of its children red
            flipColors(node);

            if (!isRed(node)) {
                if (!isRed(node.left)) {
                    if (node.left != null && isRed(node.left.left)) {
                        node = rotateRight(node);
                        flipColors(node);
                    }
                } else {
                    if (node.left != null && isRed(node.left.right)) {
                        node = rotateRight(node);
                        flipColors(node);
                    }
                }
            } else {
                if (!isRed(node.left)) {
                    if (node.right != null && isRed(node.right.left)) {
                        node = rotateRight(node);
                        flipColors(node);
                    }
                } else {
                    if (node.right != null && isRed(node.right.right)) {
                        node = rotateRight(node);
                        flipColors(node);
                    }
                }
            }

            return node;
        }

        private Node balance(Node node) {
            if (node == null) {
                return null;
            }

            if (!isRed(node)) {
                if (isRed(node.right)) {
                    node = rotateLeft(node);
                }
            } else {
                if (isRed(node.left)) {
                    node = rotateLeft(node);
                }
            }

            if (!isRed(node)) {
                if (!isRed(node.left)) {
                    if (isRed(node.left) && isRed(node.left.left)) {
                        node = rotateRight(node);
                    }
                } else {
                    if (isRed(node.left) && isRed(node.left.right)) {
                        node = rotateRight(node);
                    }
                }
            } else {
                if (!isRed(node.right)) {
                    if (isRed(node.right) && isRed(node.right.left)) {
                        node = rotateRight(node);
                    }
                } else {
                    if (isRed(node.right) && isRed(node.right.right)) {
                        node = rotateRight(node);
                    }
                }
            }

            if (isRed(node.left) && isRed(node.right)) {
                flipColors(node);
            }

            node.size = size(node.left) + 1 + size(node.right);

            return node;
        }

        public Iterable<Key> keys() {
            return keys(min(), max());
        }

        public Iterable<Key> keys(Key low, Key high) {
            if (low == null)  {
                throw new IllegalArgumentException("First argument to keys() cannot be null");
            }
            if (high == null) {
                throw new IllegalArgumentException("Second argument to keys() cannot be null");
            }

            Queue<Key> queue = new Queue<>();
            keys(root, queue, low, high);
            return queue;
        }

        private void keys(Node node, Queue<Key> queue, Key low, Key high) {
            if (node == null) {
                return;
            }

            int compareLow = low.compareTo(node.key);
            int compareHigh = high.compareTo(node.key);

            if (compareLow < 0) {
                if (!isRed(node)) {
                    keys(node.left, queue, low, high);
                } else {
                    keys(node.right, queue, low, high);
                }
            }

            if (compareLow <= 0 && compareHigh >= 0) {
                queue.enqueue(node.key);
            }

            if (compareHigh > 0) {
                if (!isRed(node)) {
                    keys(node.right, queue, low, high);
                } else {
                    keys(node.left, queue, low, high);
                }
            }
        }

        public int size(Key low, Key high) {
            if (low == null)  {
                throw new IllegalArgumentException("First argument to size() cannot be null");
            }
            if (high == null) {
                throw new IllegalArgumentException("Second argument to size() cannot be null");
            }

            if (low.compareTo(high) > 0) {
                return 0;
            }

            if (contains(high)) {
                return rank(high) - rank(low) + 1;
            } else {
                return rank(high) - rank(low);
            }
        }

        public boolean isSubtreeCountConsistent() {
            return isSubtreeCountConsistent(root);
        }

        private boolean isSubtreeCountConsistent(Node node) {
            if (node == null) {
                return true;
            }

            int totalSubtreeCount = 0;
            if (node.left != null) {
                totalSubtreeCount += node.left.size;
            }
            if (node.right != null) {
                totalSubtreeCount += node.right.size;
            }

            if (node.size != totalSubtreeCount + 1) {
                return false;
            }

            return isSubtreeCountConsistent(node.left) && isSubtreeCountConsistent(node.right);
        }

        public boolean isValid23Tree() {
            return isValid23Tree(root);
        }

        private boolean isValid23Tree(Node node) {
            if (node == null) {
                return true;
            }

            if (isRed(node.left) && isRed(node.right)) {
                return false;
            }

            if (!isRed(node)) {
                if (!isRed(node.left) && isRed(node.right)) {
                    return false;
                }
            } else {
                if (!isRed(node.right) && isRed(node.left)) {
                    return false;
                }
            }

            if (!isRed(node)) {
                if (!isRed(node.left)) {
                    if (isRed(node.left) && isRed(node.left.left)) {
                        return false;
                    }
                } else {
                    if (isRed(node.left) && isRed(node.left.right)) {
                        return false;
                    }
                }
            } else {
                if (!isRed(node.left)) {
                    if (isRed(node.right) && isRed(node.right.left)) {
                        return false;
                    }
                } else {
                    if (isRed(node.right) && isRed(node.right.right)) {
                        return false;
                    }
                }
            }

            return isValid23Tree(node.left) && isValid23Tree(node.right);
        }

        public boolean isBalanced() {
            int blackNodes = 0; // number of black links on path from root to min

            Node currentNode = root;
            while (currentNode != null) {
                if (!isRed(currentNode)) {
                    blackNodes++;
                }

                currentNode = currentNode.left;
            }

            return isBalanced(root, blackNodes);
        }

        private boolean isBalanced(Node node, int blackNodes) {
            if (node == null) {
                return blackNodes == 0;
            }

            if (!isRed(node)) {
                blackNodes--;
            }

            return isBalanced(node.left, blackNodes) && isBalanced(node.right, blackNodes);
        }

    }

    public static void main(String[] args) {
        //Expected 2-3 tree
        //
        //                 (B)1
        //         (B)-1          (B)5
        //    (B)-2   (B)0    (B)3    (B)99
        // (B)-5            (B)2    (B)9
        //

        Exercise29_OptimalStorage optimalStorage = new Exercise29_OptimalStorage();
        RedBlackBSTOptimalStorage<Integer, Integer> redBlackBSTOptimalStorage = optimalStorage.new RedBlackBSTOptimalStorage<>();
        redBlackBSTOptimalStorage.put(5, 5);
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        redBlackBSTOptimalStorage.put(1, 1);
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        redBlackBSTOptimalStorage.put(9, 9);
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        redBlackBSTOptimalStorage.put(2, 2);
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        redBlackBSTOptimalStorage.put(0, 0);
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        redBlackBSTOptimalStorage.put(99, 99);
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        redBlackBSTOptimalStorage.put(-1, -1);
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        redBlackBSTOptimalStorage.put(-2, -2);
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        redBlackBSTOptimalStorage.put(3, 3);
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        redBlackBSTOptimalStorage.put(-5, -5);
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");

        StdOut.println("Size consistent: " + redBlackBSTOptimalStorage.isSubtreeCountConsistent() + " Expected: true\n");

        StdOut.println("Keys() test");

        for(Integer key : redBlackBSTOptimalStorage.keys()) {
            StdOut.println("Key " + key + ": " + redBlackBSTOptimalStorage.get(key));
        }
        StdOut.println("Expected: -5 -2 -1 0 1 2 3 5 9 99\n");

        //Test min()
        StdOut.println("Min key: " + redBlackBSTOptimalStorage.min() + " Expected: -5");

        //Test max()
        StdOut.println("Max key: " + redBlackBSTOptimalStorage.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + redBlackBSTOptimalStorage.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + redBlackBSTOptimalStorage.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + redBlackBSTOptimalStorage.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + redBlackBSTOptimalStorage.ceiling(15) + " Expected: 99");

        //Test select()
        StdOut.println("Select key of rank 4: " + redBlackBSTOptimalStorage.select(4) + " Expected: 1");

        //Test rank()
        StdOut.println("Rank of key 9: " + redBlackBSTOptimalStorage.rank(9) + " Expected: 8");
        StdOut.println("Rank of key 10: " + redBlackBSTOptimalStorage.rank(10) + " Expected: 9");

        //Test delete()
        StdOut.println("\nDelete key 2");
        redBlackBSTOptimalStorage.delete(2);

        for(Integer key : redBlackBSTOptimalStorage.keys()) {
            StdOut.println("Key " + key + ": " + redBlackBSTOptimalStorage.get(key));
        }
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        StdOut.println("Size consistent: " + redBlackBSTOptimalStorage.isSubtreeCountConsistent() + " Expected: true");

        //Test deleteMin()
        StdOut.println("\nDelete min (key -5)");
        redBlackBSTOptimalStorage.deleteMin();

        for(Integer key : redBlackBSTOptimalStorage.keys()) {
            StdOut.println("Key " + key + ": " + redBlackBSTOptimalStorage.get(key));
        }
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        StdOut.println("Size consistent: " + redBlackBSTOptimalStorage.isSubtreeCountConsistent() + " Expected: true");

        //Test deleteMax()
        StdOut.println("\nDelete max (key 99)");
        redBlackBSTOptimalStorage.deleteMax();

        for(Integer key : redBlackBSTOptimalStorage.keys()) {
            StdOut.println("Key " + key + ": " + redBlackBSTOptimalStorage.get(key));
        }
        StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
        StdOut.println("Size consistent: " + redBlackBSTOptimalStorage.isSubtreeCountConsistent() + " Expected: true");

        //Test keys() with range
        StdOut.println("\nKeys in range [2, 10]");
        for(Integer key : redBlackBSTOptimalStorage.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + redBlackBSTOptimalStorage.get(key));
        }

        StdOut.println("\nKeys in range [-4, -1]");
        for(Integer key : redBlackBSTOptimalStorage.keys(-4, -1)) {
            StdOut.println("Key " + key + ": " + redBlackBSTOptimalStorage.get(key));
        }

        //Delete all
        StdOut.println("\nDelete all");
        while (redBlackBSTOptimalStorage.size() > 0) {
            for(Integer key : redBlackBSTOptimalStorage.keys()) {
                StdOut.println("Key " + key + ": " + redBlackBSTOptimalStorage.get(key));
            }

            //redBlackBSTOptimalStorage.delete(redBlackBSTOptimalStorage.select(0));
            redBlackBSTOptimalStorage.delete(redBlackBSTOptimalStorage.select(redBlackBSTOptimalStorage.size() - 1));
            StdOut.println("Is valid 2-3 tree: " + redBlackBSTOptimalStorage.isValid23Tree() + " Expected: true");
            StdOut.println("Is balanced: " + redBlackBSTOptimalStorage.isBalanced() + " Expected: true");
            StdOut.println("Size consistent: " + redBlackBSTOptimalStorage.isSubtreeCountConsistent() + " Expected: true");

            StdOut.println();
        }
    }

}
