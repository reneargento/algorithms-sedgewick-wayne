package chapter3.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 12/06/17.
 */
public class Exercise42_HibbardDeletionDegradation {

    private class BinarySearchTreeInternalPathLength<Key extends Comparable<Key>, Value> extends BinarySearchTree<Key, Value> {

        private class Node {
            private Key key;
            private Value value;

            private Node left;
            private Node right;

            private int size; //# of nodes in subtree rooted here
            private int depth; //used only to compute the internal path length

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
            if (node == null) {
                return 0;
            }

            return node.size;
        }

        public void put(Key key, Value value) {
            root = put(root, key, value);
        }

        private Node put(Node node, Key key, Value value) {
            if (node == null) {
                return new Node(key, value, 1);
            }

            int compare = key.compareTo(node.key);

            if (compare < 0) {
                node.left = put(node.left, key, value);
            } else if (compare > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        public void deleteWithRandomNodePromotion(Key key) {
            root = deleteWithRandomNodePromotion(root, key);
        }

        private Node deleteWithRandomNodePromotion(Node node, Key key) {
            if (node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);
            if (compare < 0) {
                node.left = deleteWithRandomNodePromotion(node.left, key);
            } else if (compare > 0) {
                node.right = deleteWithRandomNodePromotion(node.right, key);
            } else {
                if (node.left == null) {
                    return node.right;
                } else if (node.right == null) {
                    return node.left;
                } else {
                    //Randomly chooses predecessor or successor to promote
                    int promotePredecessorNode = StdRandom.uniform(2);

                    Node aux = node;

                    if (promotePredecessorNode == 0) {
                        node = max(aux.left);
                        node.left = deleteMax(aux.left);
                        node.right = aux.right;
                    } else {
                        node = min(aux.right);
                        node.right = deleteMin(aux.right);
                        node.left = aux.left;
                    }
                }
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
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

            return min(node.left);
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

            return max(node.right);
        }

        //Used in delete()
        private Node deleteMin(Node node) {
            if (node == null) {
                return null;
            }

            if (node.left == null) {
                return node.right;
            }

            node.left = deleteMin(node.left);
            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        //Used in delete()
        private Node deleteMax(Node node) {
            if (node == null) {
                return null;
            }

            if (node.right == null) {
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
            if (node == null) {
                return null;
            }

            int compare = key.compareTo(node.key);
            if (compare < 0) {
                node.left = delete(node.left, key);
            } else if (compare > 0) {
                node.right = delete(node.right, key);
            } else {
                if (node.left == null) {
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

        public Key select(int index) {
            if (index >= size()) {
                throw new IllegalArgumentException("Index is higher than tree size");
            }

            return select(root, index).key;
        }

        private Node select(Node node, int index) {
            int leftSubtreeSize = size(node.left);

            if (leftSubtreeSize == index) {
                return node;
            } else if (leftSubtreeSize > index) {
                return select(node.left, index);
            } else {
                return select(node.right, index - leftSubtreeSize - 1);
            }
        }

        public int internalPathLength() {
            if (root == null) {
                return 0;
            }

            int internalPathLength = 0;

            Queue<Node> queue = new Queue<>();
            root.depth = 0;
            queue.enqueue(root);

            while (!queue.isEmpty()) {
                Node current = queue.dequeue();
                internalPathLength += current.depth;

                if (current.left != null) {
                    current.left.depth = current.depth + 1;
                    queue.enqueue(current.left);
                }
                if (current.right != null) {
                    current.right.depth = current.depth + 1;
                    queue.enqueue(current.right);
                }
            }

            return internalPathLength;
        }

        public int averagePathLength() {
            if (size() == 0) {
                return 0;
            }

            return (internalPathLength() / size()) + 1;
        }
    }

    private void doExperiment(int size) {

        int[] sizes = {100, 1000, 10000};

        StdOut.printf("%10s %20s %30s %30s\n", "Tree Size | ", "Square Root of Size | ","AVG Path Length W/ Standard Delete | ",
                "AVG Path Length W/ Delete Random Promotion");

        for(int i = 0; i < sizes.length; i++) {
            double[] averagePathLengths = computeAVGPathLengthOfRandomKeys(sizes[i]);
            printResults(sizes[i], Math.sqrt(sizes[i]), averagePathLengths[0], averagePathLengths[1]);
        }
    }

    private double[] computeAVGPathLengthOfRandomKeys(int size) {
        BinarySearchTreeInternalPathLength<Integer, Integer> binarySearchTreeStandardDelete =
                new BinarySearchTreeInternalPathLength<>();
        BinarySearchTreeInternalPathLength<Integer, Integer> binarySearchTreeRandomPromotionDelete =
                new BinarySearchTreeInternalPathLength<>();

        for(int i = 0; i < size; i++) {
            int random = StdRandom.uniform(Integer.MAX_VALUE);
            binarySearchTreeStandardDelete.put(random, random);
            binarySearchTreeRandomPromotionDelete.put(random, random);
        }

        for(int i = 0; i < size * size; i++) {
            //Delete random key
            int randomIndexKeyToDelete = StdRandom.uniform(binarySearchTreeStandardDelete.size());
            binarySearchTreeStandardDelete.delete(binarySearchTreeStandardDelete.select(randomIndexKeyToDelete));
            binarySearchTreeRandomPromotionDelete.
                    deleteWithRandomNodePromotion(binarySearchTreeRandomPromotionDelete.select(randomIndexKeyToDelete));

            //Insert random key
            int random = StdRandom.uniform(Integer.MAX_VALUE);
            binarySearchTreeStandardDelete.put(random, random);
            binarySearchTreeRandomPromotionDelete.put(random, random);
        }

        double[] averagePathLengths = new double[2];
        averagePathLengths[0] = binarySearchTreeStandardDelete.averagePathLength();
        averagePathLengths[1] = binarySearchTreeRandomPromotionDelete.averagePathLength();

        return averagePathLengths;
    }

    private void printResults(int size, double squareRootOfSize, double averagePathLengthStandardDelete,
                              double averagePathLengthRandomPromotionDelete) {
        StdOut.printf("%9d %22.2f %37.2f %45.2f\n", size, squareRootOfSize, averagePathLengthStandardDelete,
                averagePathLengthRandomPromotionDelete);
    }

    public static void main(String[] args) {
        Exercise42_HibbardDeletionDegradation hibbardDeletionDegradation = new Exercise42_HibbardDeletionDegradation();
        hibbardDeletionDegradation.testInternalPathLength();

        int size = Integer.parseInt(args[0]);
        hibbardDeletionDegradation.doExperiment(size);
    }

    private void testInternalPathLength() {
        StdOut.println("Internal path length tests:");

        BinarySearchTreeInternalPathLength<Integer, String> binarySearchTreeInternalPathLength =
                new BinarySearchTreeInternalPathLength<>();

        binarySearchTreeInternalPathLength.put(5, "Value 5");
        StdOut.println("Internal path length: " + binarySearchTreeInternalPathLength.internalPathLength() + " Expected: 0");

        binarySearchTreeInternalPathLength.put(1, "Value 1");
        StdOut.println("Internal path length: " + binarySearchTreeInternalPathLength.internalPathLength() + " Expected: 1");

        binarySearchTreeInternalPathLength.put(9, "Value 9");
        StdOut.println("Internal path length: " + binarySearchTreeInternalPathLength.internalPathLength() + " Expected: 2");

        binarySearchTreeInternalPathLength.put(8, "Value 8");
        StdOut.println("Internal path length: " + binarySearchTreeInternalPathLength.internalPathLength() + " Expected: 4");

        binarySearchTreeInternalPathLength.put(2, "Value 2");
        StdOut.println("Internal path length: " + binarySearchTreeInternalPathLength.internalPathLength() + " Expected: 6");

        binarySearchTreeInternalPathLength.put(0, "Value 0");
        StdOut.println("Internal path length: " + binarySearchTreeInternalPathLength.internalPathLength() + " Expected: 8");

        binarySearchTreeInternalPathLength.put(99, "Value 99");
        StdOut.println("Internal path length: " + binarySearchTreeInternalPathLength.internalPathLength() + " Expected: 10\n");
    }

}
