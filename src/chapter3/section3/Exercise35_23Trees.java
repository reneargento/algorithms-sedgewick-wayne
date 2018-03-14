package chapter3.section3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 02/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise35_23Trees {

    private static class TwoThreeST<Key extends Comparable<Key>, Value> {

        private class Node {
            Node left, right;
            int size;

            Node(int size) {
                this.size = size;
            }
        }

        private class TwoNode extends Node {
            Key key;
            Value value;

            TwoNode(Key key, Value value, int size) {
                super(size);
                this.key = key;
                this.value = value;
            }
        }

        private class ThreeNode extends Node {
            Node middle;

            Key leftKey;
            Value leftValue;

            Key rightKey;
            Value rightValue;

            ThreeNode(Key leftKey, Value leftValue, Key rightKey, Value rightValue, int size) {
                super(size);

                this.leftKey = leftKey;
                this.leftValue = leftValue;
                this.rightKey = rightKey;
                this.rightValue = rightValue;
            }
        }

        private class FourNode extends Node {
            Node middle1;
            Node middle2;

            Key leftKey;
            Value leftValue;

            Key middleKey;
            Value middleValue;

            Key rightKey;
            Value rightValue;

            FourNode(Key leftKey, Value leftValue, Key middleKey, Value middleValue, Key rightKey, Value rightValue, int size) {
                super(size);

                this.leftKey = leftKey;
                this.leftValue = leftValue;
                this.middleKey = middleKey;
                this.middleValue = middleValue;
                this.rightKey = rightKey;
                this.rightValue = rightValue;
            }
        }

        private Node root;

        private enum NodePosition {
            LEFT, MIDDLE1, MIDDLE2, RIGHT
        }

        private int getNodePositionValue(Node parent, NodePosition nodePosition) {
            int value = 0;

            if (is2Node(parent)) {
                switch (nodePosition) {
                    case LEFT: value = 0; break;
                    case RIGHT: value = 1; break;
                }
            } else if (is3Node(parent)) {
                switch (nodePosition) {
                    case LEFT: value = 0; break;
                    case MIDDLE1: value = 1; break;
                    case RIGHT: value = 2; break;
                }
            } else {
                switch (nodePosition) {
                    case LEFT: value = 0; break;
                    case MIDDLE1: value = 1; break;
                    case MIDDLE2: value = 2; break;
                    case RIGHT: value = 3; break;
                }
            }

            return value;
        }

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

        private boolean is2Node(Node node) {
            if (node == null) {
                return false;
            }

            return node instanceof TwoThreeST.TwoNode;
        }

        private boolean is3Node(Node node) {
            if (node == null) {
                return false;
            }

            return node instanceof TwoThreeST.ThreeNode;
        }

        private boolean is4Node(Node node) {
            if (node == null) {
                return false;
            }

            return node instanceof TwoThreeST.FourNode;
        }

        private TwoNode split4Node(FourNode fourNode) {
            int leftNodeSize = size(fourNode.left) + 1 + size(fourNode.middle1);
            int rightNodeSize = size(fourNode.middle2) + 1 + size(fourNode.right);
            int middleNodeSize = leftNodeSize + 1 + rightNodeSize;

            TwoNode leftNode = new TwoNode(fourNode.leftKey, fourNode.leftValue, leftNodeSize);
            TwoNode middleNode = new TwoNode(fourNode.middleKey, fourNode.middleValue, middleNodeSize);
            TwoNode rightNode = new TwoNode(fourNode.rightKey, fourNode.rightValue, rightNodeSize);

            leftNode.left = fourNode.left;
            leftNode.right = fourNode.middle1;

            rightNode.left = fourNode.middle2;
            rightNode.right = fourNode.right;

            middleNode.left = leftNode;
            middleNode.right = rightNode;

            return middleNode;
        }

        private TwoNode split3NodeAndMakeRightNodeParent(ThreeNode threeNode) {
            int leftNodeSize = size(threeNode.left) + 1 + size(threeNode.middle);
            int rightNodeSize = leftNodeSize + 1 + size(threeNode.right);

            TwoNode leftNode = new TwoNode(threeNode.leftKey, threeNode.leftValue, leftNodeSize);
            TwoNode rightNode = new TwoNode(threeNode.rightKey, threeNode.rightValue, rightNodeSize);

            leftNode.left = threeNode.left;
            leftNode.right = threeNode.middle;

            rightNode.left = leftNode;
            rightNode.right = threeNode.right;

            return rightNode;
        }

        private TwoNode split3NodeAndMakeLeftNodeParent(ThreeNode threeNode) {
            int rightNodeSize = size(threeNode.right) + 1 + size(threeNode.middle);
            int leftNodeSize = size(threeNode.left) + 1 + rightNodeSize;

            TwoNode leftNode = new TwoNode(threeNode.leftKey, threeNode.leftValue, leftNodeSize);
            TwoNode rightNode = new TwoNode(threeNode.rightKey, threeNode.rightValue, rightNodeSize);

            leftNode.left = threeNode.left;
            leftNode.right = rightNode;

            rightNode.left = threeNode.middle;
            rightNode.right = threeNode.right;

            return leftNode;
        }

        private ThreeNode generate3Node(TwoNode parentNode, TwoNode newNode) {
            boolean isLeftNode = parentNode.key.compareTo(newNode.key) > 0;

            ThreeNode newThreeNode;
            int newSize = parentNode.size;
            if (isLeftNode) {
                newThreeNode = new ThreeNode(newNode.key, newNode.value, parentNode.key,
                        parentNode.value, newSize);
                newThreeNode.left = newNode.left;
                newThreeNode.middle = newNode.right;
                newThreeNode.right = parentNode.right;
            } else {
                newThreeNode = new ThreeNode(parentNode.key, parentNode.value, newNode.key,
                        newNode.value, newSize);
                newThreeNode.left = parentNode.left;
                newThreeNode.middle = newNode.left;
                newThreeNode.right = newNode.right;
            }

            return newThreeNode;
        }

        private FourNode generate4Node(ThreeNode parentNode, TwoNode newNode) {
            boolean isLeftNode = parentNode.leftKey.compareTo(newNode.key) > 0;
            boolean isRightNode = parentNode.rightKey.compareTo(newNode.key) < 0;

            //Create temporary 4-node
            FourNode newFourNode;
            int newSize = parentNode.size;
            if (isLeftNode) {
                newFourNode = new FourNode(newNode.key, newNode.value, parentNode.leftKey, parentNode.leftValue,
                        parentNode.rightKey, parentNode.rightValue, newSize);
                newFourNode.left = newNode.left;
                newFourNode.middle1 = newNode.right;
                newFourNode.middle2 = parentNode.middle;
                newFourNode.right = parentNode.right;
            } else if (isRightNode) {
                newFourNode = new FourNode(parentNode.leftKey, parentNode.leftValue,
                        parentNode.rightKey, parentNode.rightValue, newNode.key, newNode.value, newSize);
                newFourNode.left = parentNode.left;
                newFourNode.middle1 = parentNode.middle;
                newFourNode.middle2 = newNode.left;
                newFourNode.right = newNode.right;
            } else {
                newFourNode = new FourNode(parentNode.leftKey, parentNode.leftValue, newNode.key, newNode.value,
                        parentNode.rightKey, parentNode.rightValue, newSize);
                newFourNode.left = parentNode.left;
                newFourNode.middle1 = newNode.left;
                newFourNode.middle2 = newNode.right;
                newFourNode.right = parentNode.right;
            }

            return newFourNode;
        }

        /**
         * Source node - Child node to move threeNode from
         * Destination node - Child node to move threeNode to
         */
        private Node moveKey(Node parent, NodePosition sourceNodePosition, NodePosition destinationNodePosition) {
            int source = getNodePositionValue(parent, sourceNodePosition);
            int destination = getNodePositionValue(parent, destinationNodePosition);
            boolean isMoveLeft = source > destination;

            if (isMoveLeft) {
                while (destination < source) {
                    //Default values for 2-node
                    Node temporarySource = parent.right;
                    Node temporaryDestination = parent.left;

                    if (is3Node(parent)) {
                        ThreeNode threeNodeParent = (ThreeNode) parent;

                        switch (source) {
                            case 2:
                                temporarySource = threeNodeParent.right;
                                temporaryDestination = threeNodeParent.middle;

                                sourceNodePosition = NodePosition.RIGHT;
                                destinationNodePosition = NodePosition.MIDDLE1;
                                break;
                            case 1:
                                temporarySource = threeNodeParent.middle;
                                temporaryDestination = threeNodeParent.left;

                                sourceNodePosition = NodePosition.MIDDLE1;
                                destinationNodePosition = NodePosition.LEFT;
                                break;
                        }
                    } else if (is4Node(parent)) {
                        FourNode fourNode = (FourNode) parent;

                        switch (source) {
                            case 3:
                                temporarySource = fourNode.right;
                                temporaryDestination = fourNode.middle2;

                                sourceNodePosition = NodePosition.RIGHT;
                                destinationNodePosition = NodePosition.MIDDLE2;
                                break;
                            case 2:
                                temporarySource = fourNode.middle2;
                                temporaryDestination = fourNode.middle1;

                                sourceNodePosition = NodePosition.MIDDLE2;
                                destinationNodePosition = NodePosition.MIDDLE1;
                                break;
                            case 1:
                                temporarySource = fourNode.middle1;
                                temporaryDestination = fourNode.left;

                                sourceNodePosition = NodePosition.MIDDLE1;
                                destinationNodePosition = NodePosition.LEFT;
                                break;
                        }
                    }

                    //Invert temporaryDestination and temporarySource parameters when going left
                    parent = moveKeyBetweenImmediateSiblings(parent, temporaryDestination, temporarySource,
                            sourceNodePosition, destinationNodePosition);
                    source--;
                }
            } else {
                while (source < destination) {
                    //Default values for 2-node
                    Node temporarySource = parent.left;
                    Node temporaryDestination = parent.right;

                    if (is3Node(parent)) {
                        ThreeNode threeNodeParent = (ThreeNode) parent;

                        switch (source) {
                            case 0:
                                temporarySource = threeNodeParent.left;
                                temporaryDestination = threeNodeParent.middle;

                                sourceNodePosition = NodePosition.LEFT;
                                destinationNodePosition = NodePosition.MIDDLE1;
                                break;
                            case 1:
                                temporarySource = threeNodeParent.middle;
                                temporaryDestination = threeNodeParent.right;

                                sourceNodePosition = NodePosition.MIDDLE1;
                                destinationNodePosition = NodePosition.RIGHT;
                                break;
                        }
                    } else if (is4Node(parent)) {
                        FourNode fourNode = (FourNode) parent;

                        switch (source) {
                            case 0:
                                temporarySource = fourNode.left;
                                temporaryDestination = fourNode.middle1;

                                sourceNodePosition = NodePosition.LEFT;
                                destinationNodePosition = NodePosition.MIDDLE1;
                                break;
                            case 1:
                                temporarySource = fourNode.middle1;
                                temporaryDestination = fourNode.middle2;

                                sourceNodePosition = NodePosition.MIDDLE1;
                                destinationNodePosition = NodePosition.MIDDLE2;
                                break;
                            case 2:
                                temporarySource = fourNode.middle2;
                                temporaryDestination = fourNode.right;

                                sourceNodePosition = NodePosition.MIDDLE2;
                                destinationNodePosition = NodePosition.RIGHT;
                                break;
                        }
                    }

                    parent = moveKeyBetweenImmediateSiblings(parent, temporarySource, temporaryDestination,
                            sourceNodePosition, destinationNodePosition);
                    source++;
                }
            }

            return parent;
        }

        private Node moveKeyBetweenImmediateSiblings(Node parent, Node leftChild, Node rightChild,
                                                     NodePosition sourceNodePosition, NodePosition destinationNodePosition) {
            int source = getNodePositionValue(parent, sourceNodePosition);
            int destination = getNodePositionValue(parent, destinationNodePosition);

            boolean isMoveLeft = source > destination;

            if (isMoveLeft) {
                if (is2Node(rightChild)) {
                    //No point in moving a 2-node
                    return parent;
                } else if (is3Node(rightChild)) {
                    ThreeNode rightChildThreeNode = (ThreeNode) rightChild;
                    Node newLeftChild;

                    if (is2Node(parent)) {
                        TwoNode twoNodeParent = (TwoNode) parent;

                        if (leftChild != null) {
                            TwoNode twoNodeLeftChild = (TwoNode) leftChild;
                            newLeftChild = new ThreeNode(twoNodeLeftChild.key, twoNodeLeftChild.value,
                                    twoNodeParent.key, twoNodeParent.value, leftChild.size + 1 + size(rightChild.left));

                            newLeftChild.left = leftChild.left;
                            ((ThreeNode) newLeftChild).middle = leftChild.right;
                        } else {
                            newLeftChild = new TwoNode(twoNodeParent.key, twoNodeParent.value, 1);
                        }
                        newLeftChild.right = rightChildThreeNode.left;

                        twoNodeParent.key = rightChildThreeNode.leftKey;
                        twoNodeParent.value = rightChildThreeNode.leftValue;

                        TwoNode newRightChild = new TwoNode(rightChildThreeNode.rightKey, rightChildThreeNode.rightValue,
                                rightChildThreeNode.size - 1 - size(rightChild.left));
                        newRightChild.left = rightChildThreeNode.middle;
                        newRightChild.right = rightChildThreeNode.right;

                        twoNodeParent.left = newLeftChild;
                        twoNodeParent.right = newRightChild;
                    } else if (is3Node(parent)) {
                        ThreeNode threeNodeParent = (ThreeNode) parent;
                        TwoNode newRightChild;

                        switch (source) {
                            case 1:
                                if (leftChild != null) {
                                    TwoNode twoNodeLeftChild = (TwoNode) leftChild;
                                    newLeftChild = new ThreeNode(twoNodeLeftChild.key, twoNodeLeftChild.value,
                                            threeNodeParent.leftKey, threeNodeParent.leftValue, leftChild.size + 1
                                            + size(rightChild.left));

                                    newLeftChild.left = leftChild.left;
                                    ((ThreeNode) newLeftChild).middle = leftChild.right;
                                } else {
                                    newLeftChild = new TwoNode(threeNodeParent.leftKey, threeNodeParent.leftValue, 1);
                                }
                                newLeftChild.right = rightChildThreeNode.left;

                                threeNodeParent.leftKey = rightChildThreeNode.leftKey;
                                threeNodeParent.leftValue = rightChildThreeNode.leftValue;

                                newRightChild = new TwoNode(rightChildThreeNode.rightKey, rightChildThreeNode.rightValue,
                                        rightChildThreeNode.size - 1 - size(rightChild.left));
                                newRightChild.left = rightChildThreeNode.middle;
                                newRightChild.right = rightChildThreeNode.right;

                                threeNodeParent.left = newLeftChild;
                                threeNodeParent.middle = newRightChild;

                                break;
                            case 2:
                                if (leftChild != null) {
                                    TwoNode twoNodeLeftChild = (TwoNode) leftChild;
                                    newLeftChild = new ThreeNode(twoNodeLeftChild.key, twoNodeLeftChild.value,
                                            threeNodeParent.rightKey, threeNodeParent.rightValue, leftChild.size + 1
                                            + size(rightChild.left));

                                    newLeftChild.left = leftChild.left;
                                    ((ThreeNode) newLeftChild).middle = leftChild.right;
                                } else {
                                    newLeftChild = new TwoNode(threeNodeParent.rightKey, threeNodeParent.rightValue, 1);
                                }
                                newLeftChild.right = rightChildThreeNode.left;

                                threeNodeParent.rightKey = rightChildThreeNode.leftKey;
                                threeNodeParent.rightValue = rightChildThreeNode.leftValue;

                                newRightChild = new TwoNode(rightChildThreeNode.rightKey, rightChildThreeNode.rightValue,
                                        rightChildThreeNode.size - 1 - size(rightChild.left));
                                newRightChild.left = rightChildThreeNode.middle;
                                newRightChild.right = rightChildThreeNode.right;

                                threeNodeParent.middle = newLeftChild;
                                threeNodeParent.right = newRightChild;

                                break;
                        }
                    } else if (is4Node(parent)) {
                        FourNode fourNodeParent = (FourNode) parent;
                        TwoNode newRightChild;

                        switch (source) {
                            case 1:
                                if (leftChild != null) {
                                    TwoNode twoNodeLeftChild = (TwoNode) leftChild;
                                    newLeftChild = new ThreeNode(twoNodeLeftChild.key, twoNodeLeftChild.value,
                                            fourNodeParent.leftKey, fourNodeParent.leftValue, leftChild.size + 1
                                            + size(rightChild.left));

                                    newLeftChild.left = leftChild.left;
                                    ((ThreeNode) newLeftChild).middle = leftChild.right;
                                } else {
                                    newLeftChild = new TwoNode(fourNodeParent.leftKey, fourNodeParent.leftValue, 1);
                                }
                                newLeftChild.right = rightChildThreeNode.left;

                                fourNodeParent.leftKey = rightChildThreeNode.leftKey;
                                fourNodeParent.leftValue = rightChildThreeNode.leftValue;

                                newRightChild = new TwoNode(rightChildThreeNode.rightKey, rightChildThreeNode.rightValue,
                                        rightChildThreeNode.size - 1 - size(rightChild.left));
                                newRightChild.left = rightChildThreeNode.middle;
                                newRightChild.right = rightChildThreeNode.right;

                                fourNodeParent.left = newLeftChild;
                                fourNodeParent.middle1 = newRightChild;

                                break;
                            case 2:
                                if (leftChild != null) {
                                    TwoNode twoNodeLeftChild = (TwoNode) leftChild;
                                    newLeftChild = new ThreeNode(twoNodeLeftChild.key, twoNodeLeftChild.value,
                                            fourNodeParent.middleKey, fourNodeParent.middleValue, leftChild.size + 1
                                            + size(rightChild.left));

                                    newLeftChild.left = leftChild.left;
                                    ((ThreeNode) newLeftChild).middle = leftChild.right;
                                } else {
                                    newLeftChild = new TwoNode(fourNodeParent.middleKey, fourNodeParent.middleValue, 1);
                                }
                                newLeftChild.right = rightChildThreeNode.left;

                                fourNodeParent.middleKey = rightChildThreeNode.leftKey;
                                fourNodeParent.middleValue = rightChildThreeNode.leftValue;

                                newRightChild = new TwoNode(rightChildThreeNode.rightKey, rightChildThreeNode.rightValue,
                                        rightChildThreeNode.size - 1 - size(rightChild.left));
                                newRightChild.left = rightChildThreeNode.middle;
                                newRightChild.right = rightChildThreeNode.right;

                                fourNodeParent.middle1 = newLeftChild;
                                fourNodeParent.middle2 = newRightChild;

                                break;
                            case 3:
                                if (leftChild != null) {
                                    TwoNode twoNodeLeftChild = (TwoNode) leftChild;
                                    newLeftChild = new ThreeNode(twoNodeLeftChild.key, twoNodeLeftChild.value,
                                            fourNodeParent.rightKey, fourNodeParent.rightValue, leftChild.size + 1
                                            + size(rightChild.left));

                                    newLeftChild.left = leftChild.left;
                                    ((ThreeNode) newLeftChild).middle = leftChild.right;
                                } else {
                                    newLeftChild = new TwoNode(fourNodeParent.rightKey, fourNodeParent.rightValue, 1);
                                }
                                newLeftChild.right = rightChildThreeNode.left;

                                fourNodeParent.rightKey = rightChildThreeNode.leftKey;
                                fourNodeParent.rightValue = rightChildThreeNode.leftValue;

                                newRightChild = new TwoNode(rightChildThreeNode.rightKey, rightChildThreeNode.rightValue,
                                        rightChildThreeNode.size - 1 - size(rightChild.left));
                                newRightChild.left = rightChildThreeNode.middle;
                                newRightChild.right = rightChildThreeNode.right;

                                fourNodeParent.middle2 = newLeftChild;
                                fourNodeParent.right = newRightChild;

                                break;
                        }
                    }
                }
            } else {
                if (is2Node(leftChild)) {
                    //No point in moving a 2-node
                    return parent;
                } else if (is3Node(leftChild)) {
                    ThreeNode leftChildThreeNode = (ThreeNode) leftChild;
                    Node newRightChild;

                    if (is2Node(parent)) {
                        TwoNode twoNodeParent = (TwoNode) parent;

                        if (rightChild != null) {
                            TwoNode twoNodeRightChild = (TwoNode) rightChild;
                            newRightChild = new ThreeNode(twoNodeParent.key, twoNodeParent.value,
                                    twoNodeRightChild.key, twoNodeRightChild.value,rightChild.size + 1
                                    + size(leftChild.right));

                            newRightChild.right = rightChild.right;
                            ((ThreeNode) newRightChild).middle = rightChild.left;
                        } else {
                            newRightChild = new TwoNode(twoNodeParent.key, twoNodeParent.value, 1);
                        }
                        newRightChild.left = leftChildThreeNode.right;

                        twoNodeParent.key = leftChildThreeNode.rightKey;
                        twoNodeParent.value = leftChildThreeNode.rightValue;

                        TwoNode newLeftChild = new TwoNode(leftChildThreeNode.leftKey, leftChildThreeNode.leftValue,
                                leftChildThreeNode.size - 1 - size(leftChild.right));
                        newLeftChild.left = leftChildThreeNode.left;
                        newLeftChild.right = leftChildThreeNode.middle;

                        twoNodeParent.left = newLeftChild;
                        twoNodeParent.right = newRightChild;
                    } else if (is3Node(parent)) {
                        ThreeNode threeNodeParent = (ThreeNode) parent;
                        TwoNode newLeftChild;

                        switch (source) {
                            case 0:
                                if (rightChild != null) {
                                    TwoNode twoNodeRightChild = (TwoNode) rightChild;
                                    newRightChild = new ThreeNode(threeNodeParent.leftKey, threeNodeParent.leftValue,
                                            twoNodeRightChild.key, twoNodeRightChild.value,rightChild.size + 1
                                            + size(leftChild.right));

                                    newRightChild.right = rightChild.right;
                                    ((ThreeNode) newRightChild).middle = rightChild.left;
                                } else {
                                    newRightChild = new TwoNode(threeNodeParent.leftKey, threeNodeParent.leftValue, 1);
                                }
                                newRightChild.left = leftChildThreeNode.right;

                                threeNodeParent.leftKey = leftChildThreeNode.rightKey;
                                threeNodeParent.leftValue = leftChildThreeNode.rightValue;

                                newLeftChild = new TwoNode(leftChildThreeNode.leftKey, leftChildThreeNode.leftValue,
                                        leftChildThreeNode.size - 1 - size(leftChild.right));
                                newLeftChild.left = leftChildThreeNode.left;
                                newLeftChild.right = leftChildThreeNode.middle;

                                threeNodeParent.left = newLeftChild;
                                threeNodeParent.middle = newRightChild;

                                break;
                            case 1:
                                if (rightChild != null) {
                                    TwoNode twoNodeRightChild = (TwoNode) rightChild;
                                    newRightChild = new ThreeNode(threeNodeParent.rightKey, threeNodeParent.rightValue,
                                            twoNodeRightChild.key, twoNodeRightChild.value,rightChild.size + 1
                                            + size(leftChild.right));

                                    newRightChild.right = rightChild.right;
                                    ((ThreeNode) newRightChild).middle = rightChild.left;
                                } else {
                                    newRightChild = new TwoNode(threeNodeParent.rightKey, threeNodeParent.rightValue, 1);
                                }
                                newRightChild.left = leftChildThreeNode.right;

                                threeNodeParent.rightKey = leftChildThreeNode.rightKey;
                                threeNodeParent.rightValue = leftChildThreeNode.rightValue;

                                newLeftChild = new TwoNode(leftChildThreeNode.leftKey, leftChildThreeNode.leftValue,
                                        leftChildThreeNode.size - 1 - size(leftChild.right));
                                newLeftChild.left = leftChildThreeNode.left;
                                newLeftChild.right = leftChildThreeNode.middle;

                                threeNodeParent.middle = newLeftChild;
                                threeNodeParent.right = newRightChild;

                                break;
                        }
                    } else if (is4Node(parent)) {
                        FourNode fourNodeParent = (FourNode) parent;
                        TwoNode newLeftChild;

                        switch (source) {
                            case 0:
                                if (rightChild != null) {
                                    TwoNode twoNodeRightChild = (TwoNode) rightChild;
                                    newRightChild = new ThreeNode(fourNodeParent.leftKey, fourNodeParent.leftValue,
                                            twoNodeRightChild.key, twoNodeRightChild.value, rightChild.size + 1
                                            + size(leftChild.right));

                                    newRightChild.right = rightChild.right;
                                    ((ThreeNode) newRightChild).middle = rightChild.left;
                                } else {
                                    newRightChild = new TwoNode(fourNodeParent.leftKey, fourNodeParent.leftValue, 1);
                                }
                                newRightChild.left = leftChildThreeNode.right;

                                fourNodeParent.leftKey = leftChildThreeNode.rightKey;
                                fourNodeParent.leftValue = leftChildThreeNode.rightValue;

                                newLeftChild = new TwoNode(leftChildThreeNode.leftKey, leftChildThreeNode.leftValue,
                                        leftChildThreeNode.size - 1 - size(leftChild.right));
                                newLeftChild.left = leftChildThreeNode.left;
                                newLeftChild.right = leftChildThreeNode.middle;

                                fourNodeParent.left = newLeftChild;
                                fourNodeParent.middle1 = newRightChild;

                                break;
                            case 2:
                                if (rightChild != null) {
                                    TwoNode twoNodeRightChild = (TwoNode) rightChild;
                                    newRightChild = new ThreeNode(fourNodeParent.middleKey, fourNodeParent.middleValue,
                                            twoNodeRightChild.key, twoNodeRightChild.value,rightChild.size + 1
                                            + size(leftChild.right));

                                    newRightChild.right = rightChild.right;
                                    ((ThreeNode) newRightChild).middle = rightChild.left;
                                } else {
                                    newRightChild = new TwoNode(fourNodeParent.middleKey, fourNodeParent.middleValue, 1);
                                }
                                newRightChild.left = leftChildThreeNode.right;

                                fourNodeParent.middleKey = leftChildThreeNode.rightKey;
                                fourNodeParent.middleValue = leftChildThreeNode.rightValue;

                                newLeftChild = new TwoNode(leftChildThreeNode.leftKey, leftChildThreeNode.leftValue,
                                        leftChildThreeNode.size - 1 - size(leftChild.right));
                                newLeftChild.left = leftChildThreeNode.left;
                                newLeftChild.right = leftChildThreeNode.middle;

                                fourNodeParent.middle1 = newLeftChild;
                                fourNodeParent.middle2 = newRightChild;

                                break;
                            case 3:
                                if (rightChild != null) {
                                    TwoNode twoNodeRightChild = (TwoNode) rightChild;
                                    newRightChild = new ThreeNode(fourNodeParent.rightKey, fourNodeParent.rightValue,
                                            twoNodeRightChild.key, twoNodeRightChild.value, rightChild.size + 1
                                            + size(leftChild.right));

                                    newRightChild.right = rightChild.right;
                                    ((ThreeNode) newRightChild).middle = rightChild.left;
                                } else {
                                    newRightChild = new TwoNode(fourNodeParent.rightKey, fourNodeParent.rightValue, 1);
                                }
                                newRightChild.left = leftChildThreeNode.right;

                                fourNodeParent.rightKey = leftChildThreeNode.rightKey;
                                fourNodeParent.rightValue = leftChildThreeNode.rightValue;

                                newLeftChild = new TwoNode(leftChildThreeNode.leftKey, leftChildThreeNode.leftValue,
                                        leftChildThreeNode.size - 1 - size(leftChild.right));
                                newLeftChild.left = leftChildThreeNode.left;
                                newLeftChild.right = leftChildThreeNode.middle;

                                fourNodeParent.middle2 = newLeftChild;
                                fourNodeParent.right = newRightChild;

                                break;
                        }
                    }
                }
            }

            return parent;
        }

        private Node mergeParentKeyInto4NodeChild(Node parent, Node leftChild, Node rightChild, NodePosition destinationNode) {
            Node newParent = null;

            if (!is2Node(leftChild) || !is2Node(rightChild)) {
                return parent;
            }

            TwoNode childLeftTwoNode = (TwoNode) leftChild;
            TwoNode childRightTwoNode = (TwoNode) rightChild;

            int newChildSize = size(leftChild) + 1 + size(rightChild);

            FourNode newChild = null;

            if (is2Node(parent)) {
                TwoNode parentTwoNode = (TwoNode) parent;

                newChild = new FourNode(childLeftTwoNode.key, childLeftTwoNode.value,
                        parentTwoNode.key, parentTwoNode.value, childRightTwoNode.key, childRightTwoNode.value,
                        newChildSize);

                newParent = newChild;
            } else if (is3Node(parent)) {
                ThreeNode parentThreeNode = (ThreeNode) parent;

                //Go left
                if (destinationNode == NodePosition.LEFT) {
                    newChild = new FourNode(childLeftTwoNode.key, childLeftTwoNode.value,
                            parentThreeNode.leftKey, parentThreeNode.leftValue, childRightTwoNode.key, childRightTwoNode.value,
                            newChildSize);

                    newParent = new TwoNode(parentThreeNode.rightKey, parentThreeNode.rightValue, parentThreeNode.size);
                    newParent.left = newChild;
                    newParent.right = parent.right;
                } else if (destinationNode == NodePosition.RIGHT) {
                    //Go right
                    newChild = new FourNode(childLeftTwoNode.key, childLeftTwoNode.value,
                            parentThreeNode.rightKey, parentThreeNode.rightValue, childRightTwoNode.key, childRightTwoNode.value,
                            newChildSize);

                    newParent = new TwoNode(parentThreeNode.leftKey, parentThreeNode.leftValue, parentThreeNode.size);
                    newParent.left = parent.left;
                    newParent.right = newChild;
                }
            } else {
                FourNode parentFourNode = (FourNode) parent;

                //Go left
                if (destinationNode == NodePosition.LEFT) {
                    newChild = new FourNode(childLeftTwoNode.key, childLeftTwoNode.value,
                            parentFourNode.leftKey, parentFourNode.leftValue, childRightTwoNode.key, childRightTwoNode.value,
                            newChildSize);

                    newParent = new ThreeNode(parentFourNode.middleKey, parentFourNode.middleValue,
                            parentFourNode.rightKey, parentFourNode.rightValue, parentFourNode.size);
                    newParent.left = newChild;
                    ((ThreeNode) newParent).middle = parentFourNode.middle2;
                    newParent.right = parent.right;
                } else if (destinationNode == NodePosition.MIDDLE1) {
                    //Go to the middle
                    newChild = new FourNode(childLeftTwoNode.key, childLeftTwoNode.value,
                            parentFourNode.middleKey, parentFourNode.middleValue, childRightTwoNode.key, childRightTwoNode.value,
                            newChildSize);

                    newParent = new ThreeNode(parentFourNode.leftKey, parentFourNode.leftValue,
                            parentFourNode.rightKey, parentFourNode.rightValue, parentFourNode.size);
                    newParent.left = parent.left;
                    ((ThreeNode) newParent).middle = newChild;
                    newParent.right = parent.right;
                } else if (destinationNode == NodePosition.RIGHT) {
                    //Go right
                    newChild = new FourNode(childLeftTwoNode.key, childLeftTwoNode.value,
                            parentFourNode.rightKey, parentFourNode.rightValue, childRightTwoNode.key, childRightTwoNode.value,
                            newChildSize);

                    newParent = new ThreeNode(parentFourNode.leftKey, parentFourNode.leftValue,
                            parentFourNode.middleKey, parentFourNode.middleValue, parentFourNode.size);
                    newParent.left = parent.left;
                    ((ThreeNode) newParent).middle = parentFourNode.middle1;
                    newParent.right = newChild;
                }
            }

            newChild.left = leftChild.left;
            newChild.middle1 = leftChild.right;
            newChild.middle2 = rightChild.left;
            newChild.right = rightChild.right;

            return newParent;
        }

        public void put(Key key, Value value) {
            if (key == null) {
                return;
            }

            if (value == null) {
                delete(key);
                return;
            }

            root = put(root, null, key, value);
            if (is4Node(root)) {
                root = split4Node((FourNode) root);
            }
        }

        private Node put(Node node, Node parent, Key key, Value value) {
            if (node == null) {
                TwoNode newNode = new TwoNode(key, value, 1);

                if (parent == null) {
                    return newNode;
                } else if (is2Node(parent)) {
                    TwoNode parentNode = (TwoNode) parent;
                    return generate3Node(parentNode, newNode);
                } else {
                    //Parent is a 3-node
                    ThreeNode parentNode = (ThreeNode) parent;

                    //Create temporary 4-node
                    return generate4Node(parentNode, newNode);
                }
            }

            if (is2Node(node)) {
                TwoNode twoNode = (TwoNode) node;

                int compare = key.compareTo(twoNode.key);

                if (compare < 0) {
                    Node newNode = put(node.left, node, key, value);

                    if (is2Node(newNode)) {
                        node.left = newNode;
                    } else {
                        ThreeNode newThreeNode = (ThreeNode) newNode;

                        if (containsKey(newThreeNode, twoNode.key)) {
                            node = newNode;
                        } else {
                            node.left = newNode;
                        }
                    }
                } else if (compare > 0) {
                    Node newNode = put(node.right, node, key, value);

                    if (is2Node(newNode)) {
                        node.right = newNode;
                    } else {
                        ThreeNode newThreeNode = (ThreeNode) newNode;

                        if (containsKey(newThreeNode, twoNode.key)) {
                            node = newNode;
                        } else {
                            node.right = newNode;
                        }
                    }
                } else {
                    twoNode.value = value;
                }
            } else {
                //Parent is a 3-node
                ThreeNode threeNode = (ThreeNode) node;

                int compareLeft = key.compareTo(threeNode.leftKey);
                int compareRight = key.compareTo(threeNode.rightKey);

                if (compareLeft < 0) {
                    Node newNode = put(threeNode.left, threeNode, key, value);

                    if (!is4Node(newNode)) {
                        node.left = newNode;
                    } else {
                        FourNode fourNode = (FourNode) newNode;
                        node = splitNodeAndBuildTree(parent, fourNode);
                    }
                } else if (compareLeft > 0 && compareRight < 0) {
                    Node newNode = put(threeNode.middle, node, key, value);

                    if (!is4Node(newNode)) {
                        threeNode.middle = newNode;
                    } else {
                        FourNode fourNode = (FourNode) newNode;
                        node = splitNodeAndBuildTree(parent, fourNode);
                    }
                } else if (compareRight > 0) {
                    Node newNode = put(node.right, node, key, value);

                    if (!is4Node(newNode)) {
                        node.right = newNode;
                    } else {
                        FourNode fourNode = (FourNode) newNode;
                        node = splitNodeAndBuildTree(parent, fourNode);
                    }
                } else {
                    if (threeNode.leftKey.compareTo(key) == 0) {
                        threeNode.leftValue = value;
                    } else if (threeNode.rightKey.compareTo(key) == 0) {
                        threeNode.rightValue = value;
                    }
                }
            }

            if (!is3Node(node)) {
                node.size = size(node.left) + 1 + size(node.right);
            } else {
                ThreeNode threeNode = (ThreeNode) node;
                threeNode.size = size(threeNode.left) + 2 + size(threeNode.middle) + size(threeNode.right);
            }

            return node;
        }

        private Node splitNodeAndBuildTree(Node parent, FourNode fourNode) {
            Node returnNode;

            TwoNode splitNode = split4Node(fourNode);
            if (parent == null) {
                returnNode = splitNode;
            } else if (!is3Node(parent)) {
                TwoNode parentNode = (TwoNode) parent;
                returnNode = generate3Node(parentNode, splitNode);
            } else {
                ThreeNode parentNode = (ThreeNode) parent;
                returnNode = generate4Node(parentNode, splitNode);
            }

            return returnNode;
        }

        private boolean containsKey(ThreeNode threeNode, Key key) {
            if ((threeNode.leftKey != null && threeNode.leftKey.compareTo(key) == 0)
                    || (threeNode.rightKey != null && threeNode.rightKey.compareTo(key) == 0)) {
                return true;
            } else {
                return false;
            }
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

            if (is2Node(node)) {
                TwoNode twoNode = (TwoNode) node;

                int compare = key.compareTo(twoNode.key);
                if (compare < 0) {
                    return get(twoNode.left, key);
                } else if (compare > 0) {
                    return get(twoNode.right, key);
                } else {
                    return twoNode.value;
                }
            } else {
                ThreeNode threeNode = (ThreeNode) node;

                int compareLeft = key.compareTo(threeNode.leftKey);
                int compareRight = key.compareTo(threeNode.rightKey);

                if (compareLeft < 0) {
                    return get(threeNode.left, key);
                } else if (compareLeft > 0 && compareRight < 0) {
                    return get(threeNode.middle, key);
                } else if (compareRight > 0) {
                    return get(threeNode.right, key);
                } else {
                    if (compareLeft == 0) {
                        return threeNode.leftValue;
                    } else {
                        return threeNode.rightValue;
                    }
                }
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

            Node minNode = min(root);

            if (is2Node(minNode)) {
                return ((TwoNode) minNode).key;
            } else {
                return ((ThreeNode) minNode).leftKey;
            }
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

            Node maxNode = max(root);

            if (is2Node(maxNode)) {
                return ((TwoNode) maxNode).key;
            } else {
                return ((ThreeNode) maxNode).rightKey;
            }
        }

        private Node max(Node node) {
            if (node.right == null) {
                return node;
            }

            return max(node.right);
        }

        //Returns the highest key in the symbol table smaller than or equal to key.
        public Key floor(Key key) {
            Node node = floor(root, key);
            if (node == null) {
                return null;
            }

            if (is2Node(node)) {
                return ((TwoNode) node).key;
            } else {
                ThreeNode threeNode = (ThreeNode) node;

                int compareLeft = key.compareTo(threeNode.leftKey);
                int compareRight = key.compareTo(threeNode.rightKey);

                //compareLeft < 0 is not possible
                if (compareLeft > 0 && compareRight < 0) {
                    return threeNode.leftKey;
                } else if (compareRight > 0) {
                    return threeNode.rightKey;
                } else {
                    if (threeNode.leftKey.compareTo(key) == 0) {
                        return threeNode.leftKey;
                    } else {
                        return threeNode.rightKey;
                    }
                }
            }
        }

        private Node floor(Node node, Key key) {
            if (node == null) {
                return null;
            }

            if (is2Node(node)) {
                TwoNode twoNode = (TwoNode) node;

                int compare = key.compareTo(twoNode.key);
                if (compare == 0) {
                    return node;
                } else if (compare < 0) {
                    return floor(twoNode.left, key);
                } else {
                    Node rightNode = floor(twoNode.right, key);
                    if (rightNode != null) {
                        return rightNode;
                    } else {
                        return node;
                    }
                }
            } else {
                ThreeNode threeNode = (ThreeNode) node;

                int compareLeft = key.compareTo(threeNode.leftKey);
                int compareRight = key.compareTo(threeNode.rightKey);

                if (compareLeft < 0) {
                    return floor(threeNode.left, key);
                } else if (compareLeft > 0 && compareRight < 0) {
                    Node middleNode = floor(threeNode.middle, key);
                    if (middleNode != null) {
                        return middleNode;
                    } else {
                        return node;
                    }
                } else if (compareRight > 0) {
                    Node rightNode = floor(threeNode.right, key);
                    if (rightNode != null) {
                        return rightNode;
                    } else {
                        return node;
                    }
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

            if (is2Node(node)) {
                return ((TwoNode) node).key;
            } else {
                ThreeNode threeNode = (ThreeNode) node;

                int compareLeft = key.compareTo(threeNode.leftKey);
                int compareRight = key.compareTo(threeNode.rightKey);

                //compareRight > 0 is not possible
                if (compareLeft < 0) {
                    return threeNode.leftKey;
                } else if (compareLeft > 0 && compareRight < 0) {
                    return threeNode.rightKey;
                } else {
                    if (threeNode.leftKey.compareTo(key) == 0) {
                        return threeNode.leftKey;
                    } else {
                        return threeNode.rightKey;
                    }
                }
            }
        }

        private Node ceiling(Node node, Key key) {
            if (node == null) {
                return null;
            }

            if (is2Node(node)) {
                TwoNode twoNode = (TwoNode) node;

                int compare = key.compareTo(twoNode.key);
                if (compare == 0) {
                    return node;
                } else if (compare > 0) {
                    return ceiling(twoNode.right, key);
                } else {
                    Node leftNode = ceiling(twoNode.left, key);
                    if (leftNode != null) {
                        return leftNode;
                    } else {
                        return node;
                    }
                }
            } else {
                ThreeNode threeNode = (ThreeNode) node;

                int compareLeft = key.compareTo(threeNode.leftKey);
                int compareRight = key.compareTo(threeNode.rightKey);

                if (compareRight > 0) {
                    return ceiling(threeNode.right, key);
                } else if (compareLeft > 0 && compareRight < 0) {
                    Node middleNode = ceiling(threeNode.middle, key);
                    if (middleNode != null) {
                        return middleNode;
                    } else {
                        return node;
                    }
                } else if (compareLeft < 0) {
                    Node leftNode = ceiling(threeNode.left, key);
                    if (leftNode != null) {
                        return leftNode;
                    } else {
                        return node;
                    }
                } else {
                    return node;
                }
            }
        }

        public Key select(int index) {
            if (index >= size()) {
                throw new IllegalArgumentException("Index is higher than tree size");
            }

            Node selectedNode = select(root, index);
            return ((TwoNode) selectedNode).key;
        }

        private Node select(Node node, int index) {
            int leftSubtreeSize = size(node.left);

            if (is2Node(node)) {
                if (leftSubtreeSize == index) {
                    return node;
                } else if (leftSubtreeSize > index) {
                    return select(node.left, index);
                } else {
                    return select(node.right, index - leftSubtreeSize - 1);
                }
            } else {
                ThreeNode threeNode = (ThreeNode) node;
                int middleSubtreeSize = size(threeNode.middle);

                if (leftSubtreeSize == index) {
                    return new TwoNode(threeNode.leftKey, threeNode.leftValue, threeNode.size);
                } else if (leftSubtreeSize + 1 + middleSubtreeSize == index) {
                    return new TwoNode(threeNode.rightKey, threeNode.rightValue, threeNode.size);
                } else if (leftSubtreeSize > index) {
                    return select(node.left, index);
                } else if (leftSubtreeSize + 1 + middleSubtreeSize > index) {
                    return select(threeNode.middle, index - leftSubtreeSize - 1);
                } else {
                    return select(node.right, index - leftSubtreeSize - 1 - middleSubtreeSize - 1);
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
            if (is2Node(node)) {
                TwoNode twoNode = (TwoNode) node;

                int compare = key.compareTo(twoNode.key);
                if (compare < 0) {
                    return rank(node.left, key);
                } else if (compare > 0) {
                    return size(node.left) + 1 + rank(node.right, key);
                } else {
                    return size(node.left);
                }
            } else {
                ThreeNode threeNode = (ThreeNode) node;

                int compareLeft = key.compareTo(threeNode.leftKey);
                int compareRight = key.compareTo(threeNode.rightKey);

                if (compareLeft < 0) {
                    return rank(threeNode.left, key);
                } else if (compareLeft > 0 && compareRight < 0) {
                    return size(threeNode.left) + 1 + rank(threeNode.middle, key);
                } else if (compareRight > 0) {
                    return size(threeNode.left) + 1 + size(threeNode.middle) + 1 + rank(threeNode.right, key);
                } else {
                    if (compareLeft == 0) {
                        return size(node.left);
                    } else {
                        return size(node.left) + 1 + size(threeNode.middle);
                    }
                }
            }
        }

        public void deleteMin() {
            if (isEmpty()) {
                return;
            }

            if (is2Node(root)) {
                if (root.right != null && is2Node(root.left) && is2Node(root.right)) {
                    root = mergeParentKeyInto4NodeChild(root, root.left, root.right, NodePosition.LEFT);
                } else if (is2Node(root.left) &&
                        (is3Node(root.right) || is4Node(root.right))) {
                    root = moveKey(root, NodePosition.RIGHT, NodePosition.LEFT);
                }
            }

            root = deleteMin(root);

            if (!isEmpty() && is4Node(root)) {
                root = split4Node((FourNode) root);
            }
        }

        private Node deleteMin(Node node) {
            //Current node is a 2-node
            if (is2Node(node)) {
                if (is2Node(node.left)) {
                    if (!is2Node(node.right)) {
                        node = moveKey(node, NodePosition.RIGHT, NodePosition.LEFT);
                    } else {
                        node = mergeParentKeyInto4NodeChild(node, node.left, node.right, NodePosition.LEFT);
                    }
                }
            } else if (is3Node(node)) {
                //Current node is a 3-node
                ThreeNode threeNode = (ThreeNode) node;

                if (is2Node(node.left)) {
                    if (!is2Node(threeNode.middle)) {
                        node = moveKey(node, NodePosition.MIDDLE1, NodePosition.LEFT);
                    } else if (!is2Node(threeNode.right)) {
                        node = moveKey(node, NodePosition.RIGHT, NodePosition.LEFT);
                    } else {
                        node = mergeParentKeyInto4NodeChild(threeNode, threeNode.left, threeNode.middle, NodePosition.LEFT);
                    }
                }
            } else {
                //Current node is a 4-node
                FourNode fourNode = (FourNode) node;

                if (is2Node(node.left)) {
                    if (!is2Node(fourNode.middle1)) {
                        node = moveKey(node, NodePosition.MIDDLE1, NodePosition.LEFT);
                    } else if (!is2Node(fourNode.middle2)) {
                        node = moveKey(node, NodePosition.MIDDLE2, NodePosition.LEFT);
                    } else if (!is2Node(fourNode.middle2)) {
                        node = moveKey(node, NodePosition.RIGHT, NodePosition.LEFT);
                    } else {
                        node = mergeParentKeyInto4NodeChild(fourNode, fourNode.left, fourNode.middle1, NodePosition.LEFT);
                    }
                }
            }

            if (node.left == null) {
                return removeMinKeyFromNode(node);
            } else {
                node.left = deleteMin(node.left);
            }

            return balance(node);
        }

        public void deleteMax() {
            if (isEmpty()) {
                return;
            }

            if (is2Node(root)) {
                if (root.left != null && is2Node(root.left) && is2Node(root.right)) {
                    root = mergeParentKeyInto4NodeChild(root, root.left, root.right, NodePosition.RIGHT);
                } else if (is2Node(root.right) &&
                        (is3Node(root.left) || is4Node(root.left))) {
                    root = moveKey(root, NodePosition.LEFT, NodePosition.RIGHT);
                }
            }

            root = deleteMax(root);

            if (!isEmpty() && is4Node(root)) {
                root = split4Node((FourNode) root);
            }
        }

        private Node deleteMax(Node node) {
            //Current node is a 2-node
            if (is2Node(node)) {
                if (is2Node(node.right)) {
                    if (!is2Node(node.left)) {
                        node = moveKey(node, NodePosition.LEFT, NodePosition.RIGHT);
                    } else {
                        node = mergeParentKeyInto4NodeChild(node, node.left, node.right, NodePosition.RIGHT);
                    }
                }
            } else if (is3Node(node)) {
                //Current node is a 3-node
                ThreeNode threeNode = (ThreeNode) node;

                if (is2Node(node.right)) {
                    if (!is2Node(threeNode.middle)) {
                        node = moveKey(threeNode, NodePosition.MIDDLE1, NodePosition.RIGHT);
                    } else if (!is2Node(threeNode.left)) {
                        node = moveKey(threeNode, NodePosition.LEFT, NodePosition.RIGHT);
                    } else {
                        node = mergeParentKeyInto4NodeChild(threeNode, threeNode.middle, threeNode.right, NodePosition.RIGHT);
                    }
                }
            } else {
                //Current node is a 4-node
                FourNode fourNode = (FourNode) node;

                if (is2Node(node.right)) {
                    if (!is2Node(fourNode.middle2)) {
                        node = moveKey(fourNode, NodePosition.MIDDLE2, NodePosition.RIGHT);
                    } else if (!is2Node(fourNode.middle1)) {
                        node = moveKey(fourNode, NodePosition.MIDDLE1, NodePosition.RIGHT);
                    } else if (!is2Node(fourNode.middle1)) {
                        node = moveKey(fourNode, NodePosition.LEFT, NodePosition.RIGHT);
                    } else {
                        node = mergeParentKeyInto4NodeChild(fourNode, fourNode.middle2, fourNode.right, NodePosition.RIGHT);
                    }
                }
            }

            if (node.right == null) {
                return removeMaxKeyFromNode(node);
            } else {
                node.right = deleteMax(node.right);
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

            if (is2Node(root)) {
                TwoNode rootNode = (TwoNode) root;

                //If root is the key to delete and we can't make a 4-node
                if (rootNode.key.compareTo(key) == 0 && is2Node(root)
                        && (rootNode.left == null || rootNode.right == null)) {
                    if (rootNode.right == null) {
                        root = rootNode.left;
                    } else {
                        Node aux = min(rootNode.right);

                        if (is2Node(aux)) {
                            TwoNode auxNode = (TwoNode) aux;
                            rootNode.key = auxNode.key;
                            rootNode.value = auxNode.value;
                            rootNode.right = deleteMin(rootNode.right);
                        } else if (is3Node(aux)) {
                            ThreeNode auxNode = (ThreeNode) aux;
                            rootNode.key = auxNode.leftKey;
                            rootNode.value = auxNode.leftValue;
                            rootNode.right = deleteMin(rootNode.right);
                        }

                        rootNode.size--;
                    }

                    return;
                }

                if (root.left != null && is2Node(root.left) && is2Node(root.right)) {
                    TwoNode leftNode = (TwoNode) root.left;
                    TwoNode middleNode = (TwoNode) root;
                    TwoNode rightNode = (TwoNode) root.right;

                    ThreeNode threeNode = generate3Node(middleNode, leftNode);
                    root = generate4Node(threeNode, rightNode);
                } else if (is3Node(rootNode.left)) {
                    root = moveThreeNodeLeftToCenter(root);
                } else if (is3Node(rootNode.right)) {
                    root = moveThreeNodeRightToCenter(root);
                }
            }

            root = delete(root, key);

            if (!isEmpty() && is4Node(root)) {
                root = split4Node((FourNode) root);
            }
        }

        private Node delete(Node node, Key key) {
            if (node == null) {
                return null;
            }

            //Move left
            if ((is2Node(node) && key.compareTo(((TwoNode) node).key) < 0)
                    || (is3Node(node) && key.compareTo(((ThreeNode) node).leftKey) < 0)
                    || (is4Node(node) && key.compareTo(((FourNode) node).leftKey) < 0)) {

                //Current node is a 2-node
                if (is2Node(node)) {
                    if (is2Node(node.left)) {
                        if (!is2Node(node.right)) {
                            node = moveKey(node, NodePosition.RIGHT, NodePosition.LEFT);
                        } else {
                            node = mergeParentKeyInto4NodeChild(node, node.left, node.right, NodePosition.LEFT);
                        }
                    }
                } else if (is3Node(node)) {
                    //Current node is a 3-node
                    ThreeNode threeNode = (ThreeNode) node;

                    if (is2Node(node.left)) {
                        if (threeNode.middle != null && !is2Node(threeNode.middle)) {
                            node = moveKey(node, NodePosition.MIDDLE1, NodePosition.LEFT);
                        } else if (!is2Node(threeNode.right)) {
                            node = moveKey(node, NodePosition.RIGHT, NodePosition.LEFT);
                        } else {
                            node = mergeParentKeyInto4NodeChild(threeNode, threeNode.left, threeNode.middle, NodePosition.LEFT);
                        }
                    }
                } else {
                    //Current node is a 4-node
                    FourNode fourNode = (FourNode) node;

                    if (is2Node(node.left)) {
                        if (fourNode.middle1 != null && !is2Node(fourNode.middle1)) {
                            node = moveKey(node, NodePosition.MIDDLE1, NodePosition.LEFT);
                        } else if (fourNode.middle2 != null && !is2Node(fourNode.middle2)) {
                            node = moveKey(node, NodePosition.MIDDLE2, NodePosition.LEFT);
                        } else if (!is2Node(fourNode.right)) {
                            node = moveKey(node, NodePosition.RIGHT, NodePosition.LEFT);
                        } else {
                            node = mergeParentKeyInto4NodeChild(fourNode, fourNode.left, fourNode.middle1, NodePosition.LEFT);
                        }
                    }
                }

                node.left = delete(node.left, key);
            } else if ((is3Node(node)
                    && ((ThreeNode) node).leftKey.compareTo(key) < 0
                    && ((ThreeNode) node).rightKey.compareTo(key) > 0)) {
                //Move middle
                ThreeNode threeNode = (ThreeNode) node;

                if (is3Node(threeNode.middle) || is4Node(threeNode.middle)) {
                    threeNode.middle = delete(threeNode.middle, key);
                } else {
                    //The middle node is a 2-node
                    if (threeNode.left != null && !is2Node(threeNode.left)) {
                        node = moveKey(threeNode, NodePosition.LEFT, NodePosition.MIDDLE1);
                        threeNode.middle = delete(threeNode.middle, key);
                    } else if (!is2Node(threeNode.right)) {
                        node = moveKey(threeNode, NodePosition.RIGHT, NodePosition.MIDDLE1);
                        threeNode.middle = delete(threeNode.middle, key);
                    } else {
                        node = mergeParentKeyInto4NodeChild(threeNode, threeNode.left, threeNode.middle, NodePosition.LEFT);
                        node.left = delete(node.left, key);
                    }
                }
            } else if ((is4Node(node)
                    && ((FourNode) node).leftKey.compareTo(key) < 0
                    && ((FourNode) node).middleKey.compareTo(key) > 0)) {
                //Move to middle 1
                FourNode fourNode = (FourNode) node;

                if (is3Node(fourNode.middle1) || is4Node(fourNode.middle1)) {
                    fourNode.middle1 = delete(fourNode.middle1, key);
                } else {
                    //The middle1 node is a 2-node
                    if (fourNode.left != null && !is2Node(fourNode.left)) {
                        node = moveKey(fourNode, NodePosition.LEFT, NodePosition.MIDDLE1);
                        fourNode.middle1 = delete(fourNode.middle1, key);
                    } else if (fourNode.middle2 != null && !is2Node(fourNode.middle2)) {
                        node = moveKey(fourNode, NodePosition.MIDDLE2, NodePosition.MIDDLE1);
                        fourNode.middle1 = delete(fourNode.middle1, key);
                    } else if (!is2Node(fourNode.right)) {
                        node = moveKey(fourNode, NodePosition.RIGHT, NodePosition.MIDDLE1);
                        fourNode.middle1 = delete(fourNode.middle1, key);
                    } else {
                        node = mergeParentKeyInto4NodeChild(fourNode, fourNode.left, fourNode.middle1, NodePosition.LEFT);
                        node.left = delete(node.left, key);
                    }
                }
            } else if ((is4Node(node)
                    && ((FourNode) node).middleKey.compareTo(key) < 0
                    && ((FourNode) node).rightKey.compareTo(key) > 0)) {
                //Move to middle 2
                FourNode fourNode = (FourNode) node;

                if (is3Node(fourNode.middle2) || is4Node(fourNode.middle2)) {
                    fourNode.middle2 = delete(fourNode.middle2, key);
                } else {
                    //The middle2 node is a 2-node
                    if (fourNode.middle1 != null && !is2Node(fourNode.middle1)) {
                        node = moveKey(fourNode, NodePosition.MIDDLE1, NodePosition.MIDDLE2);
                        fourNode.middle2 = delete(fourNode.middle2, key);
                    } else if (fourNode.right != null && !is2Node(fourNode.right)) {
                        node = moveKey(fourNode, NodePosition.RIGHT, NodePosition.MIDDLE2);
                        fourNode.middle2 = delete(fourNode.middle2, key);
                    } else if (!is2Node(fourNode.left)) {
                        node = moveKey(fourNode, NodePosition.LEFT, NodePosition.MIDDLE2);
                        fourNode.middle2 = delete(fourNode.middle2, key);
                    } else {
                        node = mergeParentKeyInto4NodeChild(fourNode, fourNode.middle1, fourNode.middle2, NodePosition.MIDDLE1);

                        //Now we have a 3-node as parent
                        ThreeNode newThreeNodeParent = (ThreeNode) node;
                        newThreeNodeParent.middle = delete(newThreeNodeParent.middle, key);
                    }
                }
            } else {
                //Move right or delete
                if ((is2Node(node)
                        && key.compareTo(((TwoNode) node).key) == 0 && node.right == null)) {
                    return null;
                } else if (is3Node(node)
                        && (key.compareTo(((ThreeNode) node).rightKey) == 0)) {
                    return removeMaxKeyFromNode(node);
                } else if (is4Node(node)
                        && (key.compareTo(((FourNode) node).rightKey) == 0)) {
                    return removeMaxKeyFromNode(node);
                }

                //Current node is a 2-node
                if (is2Node(node)) {
                    if (is2Node(node.right)) {
                        if (!is2Node(node.left)) {
                            node = moveKey(node, NodePosition.LEFT, NodePosition.RIGHT);
                        } else {
                            node = mergeParentKeyInto4NodeChild(node, node.left, node.right, NodePosition.RIGHT);
                        }
                    }
                } else if (is3Node(node)) {
                    //Current node is a 3-node
                    ThreeNode threeNode = (ThreeNode) node;

                    if (is2Node(node.right)) {
                        if (threeNode.middle != null && !is2Node(threeNode.middle)) {
                            node = moveKey(threeNode, NodePosition.MIDDLE1, NodePosition.RIGHT);
                        } else if (!is2Node(threeNode.left)) {
                            node = moveKey(threeNode, NodePosition.LEFT, NodePosition.RIGHT);
                        } else {
                            node = mergeParentKeyInto4NodeChild(threeNode, threeNode.middle, threeNode.right, NodePosition.RIGHT);
                        }
                    }
                } else {
                    //Current node is a 4-node
                    FourNode fourNode = (FourNode) node;

                    if (is2Node(node.right)) {
                        if (fourNode.middle2 != null && !is2Node(fourNode.middle2)) {
                            node = moveKey(fourNode, NodePosition.MIDDLE2, NodePosition.RIGHT);
                        } else if (fourNode.middle1 != null && !is2Node(fourNode.middle1)) {
                            node = moveKey(fourNode, NodePosition.MIDDLE1, NodePosition.RIGHT);
                        } else if (!is2Node(fourNode.left)) {
                            node = moveKey(fourNode, NodePosition.LEFT, NodePosition.RIGHT);
                        } else {
                            node = mergeParentKeyInto4NodeChild(fourNode, fourNode.middle2, fourNode.right, NodePosition.RIGHT);
                        }
                    }
                }

                //Check to see if key was found or if it is higher than current key
                if (is2Node(node)) {
                    TwoNode twoNode = (TwoNode) node;

                    if (key.compareTo(twoNode.key) == 0) {
                        Node aux = min(twoNode.right);

                        if (is2Node(aux)) {
                            TwoNode auxNode = (TwoNode) aux;
                            twoNode.key = auxNode.key;
                            twoNode.value = auxNode.value;
                            twoNode.right = deleteMin(twoNode.right);
                        } else if (is3Node(aux)) {
                            ThreeNode auxNode = (ThreeNode) aux;
                            twoNode.key = auxNode.leftKey;
                            twoNode.value = auxNode.leftValue;
                            twoNode.right = deleteMin(twoNode.right);
                        }
                    } else {
                        node.right = delete(node.right, key);
                    }
                } else if (is3Node(node)) {
                    ThreeNode threeNode = (ThreeNode) node;

                    if (key.compareTo(threeNode.leftKey) == 0) {
                        if (threeNode.middle != null) {
                            Node aux = min(threeNode.middle);

                            if (is2Node(aux)) {
                                TwoNode auxNode = (TwoNode) aux;
                                threeNode.leftKey = auxNode.key;
                                threeNode.leftValue = auxNode.value;
                                threeNode.middle = deleteMin(threeNode.middle);
                            } else if (is3Node(aux)) {
                                ThreeNode auxNode = (ThreeNode) aux;
                                threeNode.leftKey = auxNode.leftKey;
                                threeNode.leftValue = auxNode.leftValue;
                                threeNode.middle = deleteMin(threeNode.middle);
                            }
                        } else {
                            //Delete left key reference
                            node = new TwoNode(threeNode.rightKey, threeNode.rightValue, threeNode.size - 1);
                            //We know that there is nothing in the middle, so just set left and right references
                            node.left = threeNode.left;
                            node.right = threeNode.right;
                        }
                    } else if (key.compareTo(threeNode.rightKey) == 0) {
                        Node aux = min(threeNode.right);

                        if (is2Node(aux)) {
                            TwoNode auxNode = (TwoNode) aux;
                            threeNode.rightKey = auxNode.key;
                            threeNode.rightValue = auxNode.value;
                            threeNode.right = deleteMin(threeNode.right);
                        } else if (is3Node(aux)) {
                            ThreeNode auxNode = (ThreeNode) aux;
                            threeNode.rightKey = auxNode.leftKey;
                            threeNode.rightValue = auxNode.leftValue;
                            threeNode.right = deleteMin(threeNode.right);
                        }
                    } else {
                        node.right = delete(node.right, key);
                    }
                } else if (is4Node(node)) {
                    FourNode fourNode = (FourNode) node;

                    if (key.compareTo(fourNode.leftKey) == 0) {
                        if (fourNode.middle1 != null) {
                            Node aux = min(fourNode.middle1);

                            if (is2Node(aux)) {
                                TwoNode auxNode = (TwoNode) aux;
                                fourNode.leftKey = auxNode.key;
                                fourNode.leftValue = auxNode.value;
                                fourNode.middle1 = deleteMin(fourNode.middle1);
                            } else if (is3Node(aux)) {
                                ThreeNode auxNode = (ThreeNode) aux;
                                fourNode.leftKey = auxNode.leftKey;
                                fourNode.leftValue = auxNode.leftValue;
                                fourNode.middle1 = deleteMin(fourNode.middle1);
                            }
                        } else {
                            //Delete left key reference
                            node = new ThreeNode(fourNode.middleKey, fourNode.middleValue,
                                    fourNode.rightKey, fourNode.rightValue, fourNode.size - 1);
                            //We know that there is nothing in the middle1 reference, so just set left, middle2 and right references
                            node.left = fourNode.left;
                            ((ThreeNode) node).middle = fourNode.middle2;
                            node.right = fourNode.right;
                        }
                    } else if (key.compareTo(fourNode.middleKey) == 0) {
                        if (fourNode.middle2 != null) {
                            Node aux = min(fourNode.middle2);

                            if (is2Node(aux)) {
                                TwoNode auxNode = (TwoNode) aux;
                                fourNode.middleKey = auxNode.key;
                                fourNode.middleValue = auxNode.value;
                                fourNode.middle2 = deleteMin(fourNode.middle2);
                            } else if (is3Node(aux)) {
                                ThreeNode auxNode = (ThreeNode) aux;
                                fourNode.middleKey = auxNode.leftKey;
                                fourNode.middleValue = auxNode.leftValue;
                                fourNode.middle2 = deleteMin(fourNode.middle2);
                            }
                        } else {
                            //Delete middle1 key reference
                            node = new ThreeNode(fourNode.leftKey, fourNode.leftValue,
                                    fourNode.rightKey, fourNode.rightValue, fourNode.size - 1);
                            //We know that there is nothing in the middle2 reference, so just set left, middle1 and right references
                            node.left = fourNode.left;
                            ((ThreeNode) node).middle = fourNode.middle1;
                            node.right = fourNode.right;
                        }
                    } else if (key.compareTo(fourNode.rightKey) == 0) {
                        Node aux = min(fourNode.right);

                        if (is2Node(aux)) {
                            TwoNode auxNode = (TwoNode) aux;
                            fourNode.rightKey = auxNode.key;
                            fourNode.rightValue = auxNode.value;
                            fourNode.right = deleteMin(fourNode.right);
                        } else if (is3Node(aux)) {
                            ThreeNode auxNode = (ThreeNode) aux;
                            fourNode.rightKey = auxNode.leftKey;
                            fourNode.rightValue = auxNode.leftValue;
                            fourNode.right = deleteMin(fourNode.right);
                        }
                    } else {
                        node.right = delete(node.right, key);
                    }
                }

                node.right = delete(node.right, key);
            }

            return balance(node);
        }

        private Node removeMinKeyFromNode(Node node) {
            if (is2Node(node)) {
                return null;
            }

            Node bottomNode = null;

            if (is4Node(node)) {
                FourNode fourNode = (FourNode) node;

                ThreeNode finalThreeNode = new ThreeNode(fourNode.middleKey, fourNode.middleValue, fourNode.rightKey,
                        fourNode.rightValue, node.size - 1);
                finalThreeNode.left = fourNode.middle1;
                finalThreeNode.middle = fourNode.middle2;
                finalThreeNode.right = fourNode.right;

                bottomNode = finalThreeNode;
            } else if (is3Node(node)) {
                ThreeNode threeNode = (ThreeNode) node;

                TwoNode finalTwoNode = new TwoNode(threeNode.rightKey, threeNode.rightValue, node.size - 1);
                finalTwoNode.left = threeNode.middle;
                finalTwoNode.right = threeNode.right;

                bottomNode = finalTwoNode;
            }

            return bottomNode;
        }

        private Node removeMaxKeyFromNode(Node node) {
            if (is2Node(node)) {
                return null;
            }

            Node bottomNode = null;

            if (is4Node(node)) {
                FourNode fourNode = (FourNode) node;

                ThreeNode finalThreeNode = new ThreeNode(fourNode.leftKey, fourNode.leftValue, fourNode.middleKey,
                        fourNode.middleValue, node.size - 1);
                finalThreeNode.left = fourNode.left;
                finalThreeNode.middle = fourNode.middle1;
                finalThreeNode.right = fourNode.middle2;

                bottomNode = finalThreeNode;
            } else if (is3Node(node)) {
                ThreeNode threeNode = (ThreeNode) node;

                TwoNode finalTwoNode = new TwoNode(threeNode.leftKey, threeNode.leftValue, node.size - 1);
                finalTwoNode.left = threeNode.left;
                finalTwoNode.right = threeNode.middle;

                bottomNode = finalTwoNode;
            }

            return bottomNode;
        }

        private Node moveThreeNodeRightToCenter(Node node) {
            if (!is2Node(node) || is2Node(node.right)) {
                return node;
            }

            TwoNode twoNodeRoot = (TwoNode) node;
            TwoNode splitTwoNodeRight;

            if (is4Node(node.right)) {
                FourNode fourNode = (FourNode) node.right;
                splitTwoNodeRight = split4Node(fourNode);
            } else {
                ThreeNode threeNode = (ThreeNode) node.right;
                splitTwoNodeRight = split3NodeAndMakeRightNodeParent(threeNode);
            }

            //Generate 3-node at root
            TwoNode leftNodeFromSplit = (TwoNode) splitTwoNodeRight.left;

            //Move node from right subtree to root
            int splitTwoNodeRightSubtreeLeftSize = size(splitTwoNodeRight.left); //This will become its middle node size
            splitTwoNodeRight.left = splitTwoNodeRight.left.right;
            splitTwoNodeRight.size = size(splitTwoNodeRight) - splitTwoNodeRightSubtreeLeftSize + size(splitTwoNodeRight.left);

            ThreeNode threeNodeRoot = new ThreeNode(twoNodeRoot.key, twoNodeRoot.value, leftNodeFromSplit.key,
                    leftNodeFromSplit.value, twoNodeRoot.size);
            threeNodeRoot.left = twoNodeRoot.left;
            threeNodeRoot.middle = leftNodeFromSplit.left;
            threeNodeRoot.right = splitTwoNodeRight;

            return threeNodeRoot;
        }

        private Node moveThreeNodeLeftToCenter(Node node) {
            if (!is2Node(node) || is2Node(node.left)) {
                return node;
            }

            TwoNode twoNodeRoot = (TwoNode) node;
            TwoNode splitTwoNodeLeft;

            if (is4Node(node.left)) {
                FourNode fourNode = (FourNode) node.left;
                splitTwoNodeLeft = split4Node(fourNode);
            } else {
                ThreeNode threeNode = (ThreeNode) node.left;
                splitTwoNodeLeft = split3NodeAndMakeLeftNodeParent(threeNode);
            }

            //Generate 3-node at root
            TwoNode rightNodeFromSplit = (TwoNode) splitTwoNodeLeft.right;

            //Move node from left subtree to root
            int splitTwoNodeLeftSubtreeRightSize = size(splitTwoNodeLeft.right); //This will become its middle node size
            splitTwoNodeLeft.right = splitTwoNodeLeft.right.left;
            splitTwoNodeLeft.size = size(splitTwoNodeLeft) - splitTwoNodeLeftSubtreeRightSize + size(splitTwoNodeLeft.right);

            ThreeNode threeNodeRoot = new ThreeNode(rightNodeFromSplit.key, rightNodeFromSplit.value,
                    twoNodeRoot.key, twoNodeRoot.value, twoNodeRoot.size);
            threeNodeRoot.left = splitTwoNodeLeft;
            threeNodeRoot.middle = rightNodeFromSplit.right;
            threeNodeRoot.right = twoNodeRoot.right;

            return threeNodeRoot;
        }

        private Node balance(Node node) {
            if (node == null) {
                return null;
            }

            if (is4Node(node)) {
                node = split4Node((FourNode) node);
            }

            if (is2Node(node)) {
                node.size = size(node.left) + 1 + size(node.right);
            } else if (is3Node(node)) {
                node.size = size(node.left) + size(((ThreeNode) node).middle) + 2 + size(node.right);
            }

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

            if (is2Node(node)) {
                TwoNode twoNode = (TwoNode) node;

                int compareLow = low.compareTo(twoNode.key);
                int compareHigh = high.compareTo(twoNode.key);

                if (compareLow < 0) {
                    keys(node.left, queue, low, high);
                }

                if (compareLow <= 0 && compareHigh >= 0) {
                    queue.enqueue(twoNode.key);
                }

                if (compareHigh > 0) {
                    keys(node.right, queue, low, high);
                }
            } else {
                ThreeNode threeNode = (ThreeNode) node;

                int compareLeftLow = low.compareTo(threeNode.leftKey);
                int compareLeftHigh = high.compareTo(threeNode.leftKey);
                int compareRightLow = low.compareTo(threeNode.rightKey);
                int compareRightHigh = high.compareTo(threeNode.rightKey);

                if (compareLeftLow < 0) {
                    keys(threeNode.left, queue, low, high);
                }

                if (compareLeftLow <= 0 && compareLeftHigh >= 0) {
                    queue.enqueue(threeNode.leftKey);
                }

                if (compareLeftHigh > 0 && compareRightLow < 0) {
                    keys(threeNode.middle, queue, low, high);
                }

                if (compareRightLow <= 0 && compareRightHigh >= 0) {
                    queue.enqueue(threeNode.rightKey);
                }

                if (compareRightHigh > 0) {
                    keys(threeNode.right, queue, low, high);
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

        private boolean isBST() {
            return isBST(root, null, null);
        }

        private boolean isBST(Node node, Comparable low, Comparable high) {
            if (node == null) {
                return true;
            }

            if (is2Node(node)) {
                TwoNode twoNode = (TwoNode) node;

                if (low != null && low.compareTo(twoNode.key) >= 0) {
                    return false;
                }
                if (high != null && high.compareTo(twoNode.key) <= 0) {
                    return false;
                }

                return isBST(node.left, low, twoNode.key) && isBST(node.right, twoNode.key, high);
            } else {
                ThreeNode threeNode = (ThreeNode) node;

                if (low != null &&
                        (low.compareTo(threeNode.leftKey) >= 0 || low.compareTo(threeNode.rightKey) >= 0)) {
                    return false;
                }
                if (high != null &&
                        (high.compareTo(threeNode.leftKey) <= 0 || high.compareTo(threeNode.rightKey) <= 0)) {
                    return false;
                }

                return isBST(node.left, low, threeNode.leftKey)
                        && isBST(threeNode.middle, threeNode.leftKey, threeNode.rightKey)
                        && isBST(node.right, threeNode.rightKey, high);
            }
        }

        private boolean isSubtreeCountConsistent() {
            return isSubtreeCountConsistent(root);
        }

        private boolean isSubtreeCountConsistent(Node node) {
            if (node == null) {
                return true;
            }

            if (is2Node(node)) {
                if (size(node) != size(node.left) + size(node.right) + 1) {
                    return false;
                }

                return isSubtreeCountConsistent(node.left) && isSubtreeCountConsistent(node.right);
            } else {
                ThreeNode threeNode = (ThreeNode) node;

                if (size(node) != size(threeNode.left) + size(threeNode.middle) + 2 + size(threeNode.right)) {
                    return false;
                }

                return isSubtreeCountConsistent(threeNode.left)
                        && isSubtreeCountConsistent(threeNode.middle)
                        && isSubtreeCountConsistent(threeNode.right);
            }
        }

    }

    public static void main(String[] args) {
        //Expected 2-3 tree
        //                1
        //         -1           5 9
        //    -5 -2   0   2   3  7   99
        //

        TwoThreeST<Integer, Integer> twoThreeST = new Exercise35_23Trees.TwoThreeST<>();
        twoThreeST.put(5, 5);
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");
        twoThreeST.put(1, 1);
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");
        twoThreeST.put(9, 9);
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");
        twoThreeST.put(2, 2);
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");
        twoThreeST.put(0, 0);
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");
        twoThreeST.put(99, 99);
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");
        twoThreeST.put(-1, -1);
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");
        twoThreeST.put(-2, -2);
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");
        twoThreeST.put(3, 3);
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");
        twoThreeST.put(-5, -5);
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");
        twoThreeST.put(7, 7);
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true\n");

        StdOut.println("Keys() test");

        for(Integer key : twoThreeST.keys()) {
            StdOut.println("Key " + key + ": " + twoThreeST.get(key));
        }
        StdOut.println("Expected: -5 -2 -1 0 1 2 3 5 7 9 99\n");

        //Test min()
        StdOut.println("Min key: " + twoThreeST.min() + " Expected: -5");

        //Test max()
        StdOut.println("Max key: " + twoThreeST.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + twoThreeST.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + twoThreeST.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + twoThreeST.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + twoThreeST.ceiling(15) + " Expected: 99");

        //Test select()
        StdOut.println("Select key of rank 4: " + twoThreeST.select(4) + " Expected: 1");
        StdOut.println("Select key of rank 7: " + twoThreeST.select(7) + " Expected: 5");
        StdOut.println("Select key of rank 8: " + twoThreeST.select(8) + " Expected: 7");

        //Test rank()
        StdOut.println("Rank of key -5: " + twoThreeST.rank(-5) + " Expected: 0");
        StdOut.println("Rank of key -4: " + twoThreeST.rank(-4) + " Expected: 1");
        StdOut.println("Rank of key 7: " + twoThreeST.rank(7) + " Expected: 8");
        StdOut.println("Rank of key 9: " + twoThreeST.rank(9) + " Expected: 9");
        StdOut.println("Rank of key 10: " + twoThreeST.rank(10) + " Expected: 10");
        StdOut.println("Rank of key 100: " + twoThreeST.rank(100) + " Expected: 11");

        //Test delete()
        StdOut.println("\nDelete key 2");
        twoThreeST.delete(2);

        for(Integer key : twoThreeST.keys()) {
            StdOut.println("Key " + key + ": " + twoThreeST.get(key));
        }
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");

        //Test deleteMin()
        StdOut.println("\nDelete min (key -5)");
        twoThreeST.deleteMin();

        for(Integer key : twoThreeST.keys()) {
            StdOut.println("Key " + key + ": " + twoThreeST.get(key));
        }
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");

        //Test deleteMax()
        StdOut.println("\nDelete max (key 99)");
        twoThreeST.deleteMax();

        for(Integer key : twoThreeST.keys()) {
            StdOut.println("Key " + key + ": " + twoThreeST.get(key));
        }
        StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");

        //Test keys() with range
        StdOut.println("\nKeys in range [2, 10]");
        for(Integer key : twoThreeST.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + twoThreeST.get(key));
        }

        StdOut.println("\nKeys in range [-4, -1]");
        for(Integer key : twoThreeST.keys(-4, -1)) {
            StdOut.println("Key " + key + ": " + twoThreeST.get(key));
        }

        //Delete all
        StdOut.println("\nDelete all");
        while (twoThreeST.size() > 0) {
            for(Integer key : twoThreeST.keys()) {
                StdOut.println("Key " + key + ": " + twoThreeST.get(key));
            }

            //twoThreeST.delete(twoThreeST.select(0));
            twoThreeST.delete(twoThreeST.select(twoThreeST.size() - 1));
            StdOut.println("Is BST: " + twoThreeST.isBST() + " Expected: true");
            StdOut.println("Size consistent: " + twoThreeST.isSubtreeCountConsistent() + " Expected: true");

            StdOut.println();
        }
    }

}