package chapter3.section3;

import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by Rene Argento on 18/06/17.
 */
public class Exercise31_TreeDrawing {

    public class RedBlackBSTDrawable<Key extends Comparable<Key>, Value> extends RedBlackBST<Key, Value> {

        private class Node {
            Key key;
            Value value;
            Node left, right;

            boolean color;
            int size;

            private double xCoordinate, yCoordinate;

            Node(Key key, Value value, int size, boolean color) {
                this.key = key;
                this.value = value;

                this.size = size;
                this.color = color;
            }
        }

        private Node root;
        private int treeLevel;

        public int size() {
            return size(root);
        }

        protected int size(Node node) {
            if (node == null) {
                return 0;
            }

            return node.size;
        }

        private boolean isRed(Node node) {
            if (node == null) {
                return false;
            }

            return node.color == RED;
        }

        private Node rotateLeft(Node node) {
            if (node == null || node.right == null) {
                return node;
            }

            Node newRoot = node.right;

            node.right = newRoot.left;
            newRoot.left = node;

            newRoot.color = node.color;
            node.color = RED;

            newRoot.size = node.size;
            node.size = size(node.left) + 1 + size(node.right);

            return newRoot;
        }

        private Node rotateRight(Node node) {
            if (node == null || node.left == null) {
                return node;
            }

            Node newRoot = node.left;

            node.left = newRoot.right;
            newRoot.right = node;

            newRoot.color = node.color;
            node.color = RED;

            newRoot.size = node.size;
            node.size = size(node.left) + 1 + size(node.right);

            return newRoot;
        }

        private void flipColors(Node node) {
            if (node == null || node.left == null || node.right == null) {
                return;
            }

            node.color = RED;
            node.left.color = BLACK;
            node.right.color = BLACK;
        }

        public void put(Key key, Value value) {
            if (key == null) {
                return;
            }

            root = put(root, key, value);
            root.color = BLACK;
        }

        private Node put(Node node, Key key, Value value) {
            if (node == null) {
                return new Node(key, value, 1, RED);
            }

            int compare = key.compareTo(node.key);

            if (compare < 0) {
                node.left = put(node.left, key, value);
            } else if (compare > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
            }

            if (isRed(node.right) && !isRed(node.left)) {
                node = rotateLeft(node);
            }
            if (isRed(node.left) && isRed(node.left.left)) {
                node = rotateRight(node);
            }
            if (isRed(node.left) && isRed(node.right)) {
                flipColors(node);
            }

            node.size = size(node.left) + 1 + size(node.right);
            return node;
        }

        public void draw() {
            treeLevel = 0;
            setCoordinates(root, 0.9);

            StdDraw.setPenColor(StdDraw.BLACK);
            drawLines(root);
            drawNodes(root);
        }

        private void setCoordinates(Node node, double distance) {
            if (node == null) {
                return;
            }

            setCoordinates(node.left, distance - 0.05);
            node.xCoordinate = (0.5 + treeLevel++) / size();
            node.yCoordinate = distance - 0.05;
            setCoordinates(node.right, distance - 0.05);
        }

        private void drawLines(Node node) {
            if (node == null) {
                return;
            }

            drawLines(node.left);

            if (node.left != null) {
                if (node.left.color == RED) {
                    setPenToWriteRedLine();
                }

                StdDraw.line(node.xCoordinate, node.yCoordinate, node.left.xCoordinate, node.left.yCoordinate);
                resetPen();
            }
            if (node.right != null) {
                if (node.right.color == RED) {
                    setPenToWriteRedLine();
                }

                StdDraw.line(node.xCoordinate, node.yCoordinate, node.right.xCoordinate, node.right.yCoordinate);
                resetPen();
            }

            drawLines(node.right);
        }

        private void setPenToWriteRedLine() {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.007);
        }

        private void resetPen() {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.0025);
        }

        private void drawNodes(Node node) {
            if (node == null) {
                return;
            }

            double nodeRadius = 0.032;

            drawNodes(node.left);

            StdDraw.setPenColor(StdDraw.WHITE);
            //Clear the node circle area
            StdDraw.filledCircle(node.xCoordinate, node.yCoordinate, nodeRadius);

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.circle(node.xCoordinate, node.yCoordinate, nodeRadius);
            StdDraw.text(node.xCoordinate, node.yCoordinate, String.valueOf(node.key));

            drawNodes(node.right);
        }
    }

    public static void main(String[] args) {
        StdDraw.setPenRadius(0.0025);
        Exercise31_TreeDrawing treeDrawing = new Exercise31_TreeDrawing();

        RedBlackBSTDrawable<Integer, String> redBlackBSTDrawable = treeDrawing.new RedBlackBSTDrawable();

        /**
         *            (B)6
         *    (B)2            (B)20
         * (B)1 (B)4     (R)12     (B)25
         *            (B)10 (B)15
         */

        redBlackBSTDrawable.put(10, "Value 10");
        redBlackBSTDrawable.put(4, "Value 4");
        redBlackBSTDrawable.put(6, "Value 6");
        redBlackBSTDrawable.put(1, "Value 1");
        redBlackBSTDrawable.put(2, "Value 2");
        redBlackBSTDrawable.put(15, "Value 15");
        redBlackBSTDrawable.put(12, "Value 12");
        redBlackBSTDrawable.put(20, "Value 20");
        redBlackBSTDrawable.put(25, "Value 25");

        StdDraw.clear(StdDraw.WHITE);
        redBlackBSTDrawable.draw();
    }

}
