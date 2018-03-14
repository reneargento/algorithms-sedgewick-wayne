package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 04/06/17.
 */
@SuppressWarnings("unchecked")
public class Exercise34_Threading {

    private class DoublyThreadedBST<Key extends Comparable<Key>, Value> extends BinarySearchTree<Key, Value> {

        private class Node {

            private Key key;
            private Value value;

            private Node left;
            private Node right;

            private int size; //# of nodes in subtree rooted here

            private Node pred;
            private Node succ;

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

        public Key next(Key key) {
            if (key == null) {
                return null;
            }

            Node current = root;
            while (current != null) {

                int compare = key.compareTo(current.key);

                if (compare < 0) {
                    current = current.left;
                } else if (compare > 0) {
                    current = current.right;
                } else {
                    if (current.succ != null) {
                        return current.succ.key;
                    } else {
                        return null;
                    }
                }
            }

            return null;
        }

        public Key prev(Key key) {
            if (key == null) {
                return null;
            }

            Node current = root;
            while (current != null) {

                int compare = key.compareTo(current.key);

                if (compare < 0) {
                    current = current.left;
                } else if (compare > 0) {
                    current = current.right;
                } else {
                    if (current.pred != null) {
                        return current.pred.key;
                    } else {
                        return null;
                    }
                }
            }

            return null;
        }

        //Used by delete() method
        private Node min(Node node) {
            if (node.left == null) {
                return node;
            }

            return min(node.left);
        }

        @Override
        public void put(Key key, Value value) {
            root = put(root, key, value, null, null);
        }

        private Node put(Node node, Key key, Value value, Node predecessor, Node successor) {
            if (node == null) {
                Node newNode = new Node(key, value, 1);

                if (predecessor != null) {
                    predecessor.succ = newNode;
                    newNode.pred = predecessor;
                }
                if (successor != null) {
                    newNode.succ = successor;
                    successor.pred = newNode;
                }

                return newNode;
            }

            int compare = key.compareTo(node.key);

            if (compare < 0) {
                node.left = put(node.left, key, value, predecessor, node);
            } else if (compare > 0) {
                node.right = put(node.right, key, value, node, successor);
            } else {
                node.value = value;
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        public void deleteMin() {
            root = deleteMin(root, true);
        }

        //updatePredAndSucc parameter is used because we don't want to update pred and succ fields
        // when using deleteMin() inside delete()
        private Node deleteMin(Node node, boolean updatePredAndSucc) {
            if (node == null) {
                return null;
            }

            if (node.left == null) {
                if (updatePredAndSucc && node.succ != null) {
                    node.succ.pred = null;
                }

                return node.right;
            }

            node.left = deleteMin(node.left, updatePredAndSucc);
            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        public void deleteMax() {
            root = deleteMax(root);
        }

        private Node deleteMax(Node node) {
            if (node == null) {
                return null;
            }

            if (node.right == null) {
                if (node.pred != null) {
                    node.pred.succ = null;
                }

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
                if (node.left == null || node.right == null) {

                    if (node.pred != null) {
                        node.pred.succ = node.succ;
                    }
                    if (node.succ != null) {
                        node.succ.pred = node.pred;
                    }

                    if (node.left == null) {
                        return node.right;
                    } else if (node.right == null) { //Always true when we get here, but leaving it here for legibility
                        return node.left;
                    }
                } else {
                    Node aux = node;
                    node = min(aux.right);
                    node.right = deleteMin(aux.right, false);
                    node.left = aux.left;

                    node.pred = aux.pred;
                    if (node.pred != null) {
                        node.pred.succ = node;
                    }
                    //The deleted node's successor's (the new root of this subtree) pred and succ fields were already updated
                }
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

    }

    public static void main(String[] args) {
        Exercise34_Threading threading = new Exercise34_Threading();

        DoublyThreadedBST<Integer, String> binarySearchTree = threading.new DoublyThreadedBST<>();
        binarySearchTree.put(10, "Value 10");
        binarySearchTree.put(4, "Value 4");
        binarySearchTree.put(6, "Value 6");
        binarySearchTree.put(1, "Value 1");
        binarySearchTree.put(2, "Value 2");
        binarySearchTree.put(15, "Value 15");
        binarySearchTree.put(12, "Value 12");
        binarySearchTree.put(20, "Value 20");
        binarySearchTree.put(25, "Value 25");

        //Test put()
        StdOut.println("Predecessor of 1: " + binarySearchTree.prev(1) + " Expected: null");
        StdOut.println("Predecessor of 6: " + binarySearchTree.prev(6) + " Expected: 4");
        StdOut.println("Predecessor of 10: " + binarySearchTree.prev(10) + " Expected: 6");
        StdOut.println("Predecessor of 25: " + binarySearchTree.prev(25) + " Expected: 20");
        StdOut.println("Successor of 1: " + binarySearchTree.next(1) + " Expected: 2");
        StdOut.println("Successor of 6: " + binarySearchTree.next(6) + " Expected: 10");
        StdOut.println("Successor of 12: " + binarySearchTree.next(12) + " Expected: 15");
        StdOut.println("Successor of 25: " + binarySearchTree.next(25) + " Expected: null");

        StdOut.println();

        //Test deleteMin()
        StdOut.println("Predecessor of 2: " + binarySearchTree.prev(2) + " Expected: 1");
        binarySearchTree.deleteMin();
        StdOut.println("Predecessor of 2 after deleteMin(): " + binarySearchTree.prev(2) + " Expected: null");

        StdOut.println();

        //Test deleteMax()
        StdOut.println("Successor of 20: " + binarySearchTree.next(20) + " Expected: 25");
        binarySearchTree.deleteMax();
        StdOut.println("Successor of 20 after deleteMax(): " + binarySearchTree.next(20) + " Expected: null");

        StdOut.println();

        //Test delete()
        StdOut.println("Predecessor of 20: " + binarySearchTree.prev(20) + " Expected: 15");
        StdOut.println("Successor of 12: " + binarySearchTree.next(12) + " Expected: 15");
        binarySearchTree.delete(15);
        StdOut.println("Predecessor of 20 after delete(15): " + binarySearchTree.prev(20) + " Expected: 12");
        StdOut.println("Successor of 12 after delete(15): " + binarySearchTree.next(12) + " Expected: 20");

        StdOut.println();

        StdOut.println("Predecessor of 12: " + binarySearchTree.prev(12) + " Expected: 10");
        StdOut.println("Successor of 6: " + binarySearchTree.next(6) + " Expected: 10");
        binarySearchTree.delete(10);
        StdOut.println("Predecessor of 12 after delete(10): " + binarySearchTree.prev(12) + " Expected: 6");
        StdOut.println("Successor of 6 after delete(10): " + binarySearchTree.next(6) + " Expected: 12");

        StdOut.println();

        StdOut.println("Predecessor of 4: " + binarySearchTree.prev(4) + " Expected: 2");
        binarySearchTree.delete(2);
        StdOut.println("Predecessor of 4 after delete(2): " + binarySearchTree.prev(4) + " Expected: null");

        StdOut.println("Successor of 12: " + binarySearchTree.next(12) + " Expected: 20");
        binarySearchTree.delete(20);
        StdOut.println("Successor of 12 after delete(20): " + binarySearchTree.next(12) + " Expected: null");
    }

}
