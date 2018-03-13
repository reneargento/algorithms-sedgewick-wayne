package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 23/03/17.
 */
@SuppressWarnings("unchecked")
public class Exercise24_PriorityQueueExplicitLinks<Key extends Comparable> {

    private class PQNode {
        PQNode parent;
        PQNode leftChild;
        PQNode rightChild;

        Key value;
    }

    private class PriorityQueueExplicitLinks {

        private PQNode priorityQueue;
        private int size = 0; // The first node will not be used to simplify index computation

        PriorityQueueExplicitLinks() {
            priorityQueue = new PQNode(); // 0 position is not used
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        //O(lg N)
        public void insert(Key key) {
            size++;

            PQNode newNode = new PQNode();
            newNode.value = key;

            int parentIndex = size / 2; //The index of the parent of the new node
            int[] pathToParentNode = generatePathToNode(parentIndex);
            PQNode parentNode = getNode(pathToParentNode);

            if (parentNode.leftChild == null) {
                parentNode.leftChild = newNode;
            } else {
                parentNode.rightChild = newNode;
            }
            newNode.parent = parentNode;

            swim(newNode);
        }

        //O(lg N)
        public Key deleteMax() {
            if (size == 0) {
                throw new RuntimeException("Priority queue underflow");
            }

            Key max = priorityQueue.leftChild.value;
            int parentNodeIndex = size / 2; //The index of the parent of the last node

            //If we are deleting the root
            if (parentNodeIndex == 0) {
                priorityQueue.leftChild = null;
                return max;
            }

            int[] pathToLastNodeParent = generatePathToNode(parentNodeIndex);
            PQNode lastNodeParent = getNode(pathToLastNodeParent);

            Key lastItemValue;

            if (lastNodeParent.rightChild != null) {
                lastItemValue = lastNodeParent.rightChild.value;
                lastNodeParent.rightChild.parent = null;
                lastNodeParent.rightChild = null;
            } else {
                lastItemValue = lastNodeParent.leftChild.value;
                lastNodeParent.leftChild.parent = null;
                lastNodeParent.leftChild = null;
            }

            priorityQueue.leftChild.value = lastItemValue;
            sink(priorityQueue.leftChild);

            size--;

            return max;
        }

        //O(lg N)
        private void swim(PQNode node) {

            while (node.parent.value != null && ArrayUtil.less(node.parent.value, node.value)) {
                // Swap node values
                Key temp = node.value;
                node.value = node.parent.value;
                node.parent.value = temp;

                node = node.parent;
            }
        }

        //O(lg N)
        private void sink(PQNode node) {
            boolean isTheLeftChildTheHighestItem;
            Key highestItemValue;

            //Repeat while the current node exists and has at least 1 child
            while (node != null && node.leftChild != null) {

                //Check which child is bigger
                if (node.rightChild != null) {
                    if (ArrayUtil.less(node.leftChild.value, node.rightChild.value)) {
                        isTheLeftChildTheHighestItem = false;
                        highestItemValue = node.rightChild.value;
                    } else {
                        isTheLeftChildTheHighestItem = true;
                        highestItemValue = node.leftChild.value;
                    }
                } else {
                    isTheLeftChildTheHighestItem = true;
                    highestItemValue = node.leftChild.value;
                }

                //Compare highest value child and parent
                if (ArrayUtil.less(node.value, highestItemValue)) {
                    Key temp = node.value;

                    if (isTheLeftChildTheHighestItem) {
                        node.value = node.leftChild.value;
                        node.leftChild.value = temp;

                        node = node.leftChild;
                    } else {
                        node.value = node.rightChild.value;
                        node.rightChild.value = temp;

                        node = node.rightChild;
                    }
                } else {
                    break;
                }
            }
        }

        //O(lg N)
        private int[] generatePathToNode(int nodeIndex) {
            int pathSize = (int) Math.ceil(Math.log10(nodeIndex) / Math.log10(2)) + 1;

            if (pathSize <= 0) {
                return new int[0];
            }

            int[] pathToNode = new int[pathSize];

            for(int i = pathSize - 1; i >= 0; i--) {
                pathToNode[i] = nodeIndex;

                nodeIndex /= 2;
            }

            return pathToNode;
        }

        //O(lg N)
        private PQNode getNode(int[] pathToNode) {
            int currentIndex = 1;
            PQNode currentNode = priorityQueue.leftChild;

            for(int i = 0; i < pathToNode.length && currentNode != null; i++) {
                if (pathToNode[i] == currentIndex * 2) {
                    currentNode = currentNode.leftChild;
                    currentIndex = currentIndex * 2;
                } else if (pathToNode[i] == currentIndex * 2 + 1) {
                    currentNode = currentNode.rightChild;
                    currentIndex = currentIndex * 2 + 1;
                }
            }

            if (currentNode == null) {
                return priorityQueue;
            }

            return currentNode;
        }
    }

    public static void main(String[] args) {
        Exercise24_PriorityQueueExplicitLinks<Integer>.PriorityQueueExplicitLinks priorityQueueExplicitLinks =
                new Exercise24_PriorityQueueExplicitLinks().new PriorityQueueExplicitLinks();

        StdOut.println("isEmpty: " + priorityQueueExplicitLinks.isEmpty() + " Expected: true");

        priorityQueueExplicitLinks.insert(10);
        priorityQueueExplicitLinks.insert(2);
        priorityQueueExplicitLinks.insert(7);
        priorityQueueExplicitLinks.insert(20);

        StdOut.println("Size: " + priorityQueueExplicitLinks.size() + " Expected: 4");
        StdOut.println("isEmpty: " + priorityQueueExplicitLinks.isEmpty() + " Expected: false");

        StdOut.println("Item removed: " + priorityQueueExplicitLinks.deleteMax());
        StdOut.println("Item removed: " + priorityQueueExplicitLinks.deleteMax());
        StdOut.println("Item removed: " + priorityQueueExplicitLinks.deleteMax());
        StdOut.println("Item removed: " + priorityQueueExplicitLinks.deleteMax());
    }

}
