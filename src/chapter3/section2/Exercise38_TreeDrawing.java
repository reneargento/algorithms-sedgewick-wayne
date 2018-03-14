package chapter3.section2;

import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by Rene Argento on 10/06/17.
 */
@SuppressWarnings("unchecked")
public class Exercise38_TreeDrawing {

    public class BinarySearchTreeDrawable<Key extends Comparable<Key>, Value> extends BinarySearchTree<Key, Value> {
        private class Node  {
            private Key key;
            private Value value;
            private Node left, right;
            private int size;              // number of nodes in tree rooted here
            private double xCoordinate, yCoordinate;

            Node(Key key, Value value) {
                this.key = key;
                this.value = value;
                this.size = 1;
            }
        }

        private Node root;
        private int treeLevel;

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
                return new Node(key, value);
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
                StdDraw.line(node.xCoordinate, node.yCoordinate, node.left.xCoordinate, node.left.yCoordinate);
            }
            if (node.right != null) {
                StdDraw.line(node.xCoordinate, node.yCoordinate, node.right.xCoordinate, node.right.yCoordinate);
            }

            drawLines(node.right);
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
        Exercise38_TreeDrawing treeDrawing = new Exercise38_TreeDrawing();

        BinarySearchTreeDrawable<Integer, String> binarySearchTreeDrawable = treeDrawing.new BinarySearchTreeDrawable();

        /**
         *       10
         *   4       15
         * 1   6   12  20
         *  2            25
         */

        binarySearchTreeDrawable.put(10, "Value 10");
        binarySearchTreeDrawable.put(4, "Value 4");
        binarySearchTreeDrawable.put(6, "Value 6");
        binarySearchTreeDrawable.put(1, "Value 1");
        binarySearchTreeDrawable.put(2, "Value 2");
        binarySearchTreeDrawable.put(15, "Value 15");
        binarySearchTreeDrawable.put(12, "Value 12");
        binarySearchTreeDrawable.put(20, "Value 20");
        binarySearchTreeDrawable.put(25, "Value 25");

        StdDraw.clear(StdDraw.WHITE);
        binarySearchTreeDrawable.draw();
    }

}
