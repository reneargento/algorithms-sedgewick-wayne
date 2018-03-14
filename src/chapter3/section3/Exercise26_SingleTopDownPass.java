package chapter3.section3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Created by Rene Argento on 25/06/17.
 */
public class Exercise26_SingleTopDownPass {

    private class RedBlackIterative234BST<Key extends Comparable<Key>, Value> {

        private static final boolean RED = true;
        private static final boolean BLACK = false;

        private class Node {
            Key key;
            Value value;
            Node left, right;

            boolean color;
            int size;

            int numberOfBlackNodesInPath; //Only used to check if the tree is balanced on isBalanced()

            Node(Key key, Value value, int size, boolean color) {
                this.key = key;
                this.value = value;

                this.size = size;
                this.color = color;
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

            //The root must have opposite color of its two children
            if ((isRed(node) && !isRed(node.left) && !isRed(node.right))
                    || (!isRed(node) && isRed(node.left) && isRed(node.right))) {
                node.color = !node.color;
                node.left.color = !node.left.color;
                node.right.color = !node.right.color;
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

            boolean isANewNode = !contains(key);

            Node godparentNode = null;
            Node grandparentNode = null; //Used to update references from grandparentNode after balancing its children
                                         //Only used to update bottom node on a rotate right operation
            Node parentNode = null; //Used to update references from parent after balancing its children

            Node currentNode = root;

            if (root == null) {
                root = new Node(key, value, 1, RED);
            } else {

                while (currentNode != null) {
                    if (isANewNode) {
                        currentNode.size = currentNode.size + 1;
                    }

                    //Flip colors on the way down
                    if (isRed(currentNode.left) && isRed(currentNode.right)) {
                        flipColors(currentNode);
                    }

                    //Check if parent needs to be balanced after color flip
                    if (grandparentNode != null && isRed(grandparentNode.left) && isRed(grandparentNode.left.left)) {
                        grandparentNode = rotateRight(grandparentNode);
                        updateParentReference(godparentNode, grandparentNode);
                    }

                    //Balance on the way down
                    if (isRed(currentNode.right) && !isRed(currentNode.left)) {
                        currentNode = rotateLeft(currentNode);
                        updateParentReference(parentNode, currentNode);
                    }

                    if (isRed(currentNode.left) && isRed(currentNode.left.left)) {
                        currentNode = rotateRight(currentNode);
                        updateParentReference(parentNode, currentNode);
                    }

                    int compare = key.compareTo(currentNode.key);

                    if (compare < 0) {

                        if (currentNode.left == null) {
                            currentNode.left = new Node(key, value, 1, RED);
                            break;
                        }

                        godparentNode = grandparentNode;
                        grandparentNode = parentNode;
                        parentNode = currentNode;

                        currentNode = currentNode.left;
                    } else if (compare > 0) {

                        if (currentNode.right == null) {
                            currentNode.right = new Node(key, value, 1, RED);
                            break;
                        }

                        godparentNode = grandparentNode;
                        grandparentNode = parentNode;
                        parentNode = currentNode;

                        currentNode = currentNode.right;
                    } else {
                        currentNode.value = value;
                        break;
                    }
                }
            }

            //Extra color flipping and balancing on the last node
            //currentNode is the new node's parent
            //parentNode is the new node's grandparent
            if (currentNode != null) {
                if (isRed(currentNode.left) && isRed(currentNode.right)) {
                    flipColors(currentNode);
                }

                if (isRed(currentNode.right) && !isRed(currentNode.left)) {
                    currentNode = rotateLeft(currentNode);
                    updateParentReference(parentNode, currentNode);
                }

                if (parentNode != null && isRed(parentNode.left) && isRed(parentNode.left.left)) {
                    parentNode = rotateRight(parentNode);
                    updateParentReference(grandparentNode, parentNode);
                }
            }

            root.color = BLACK;
        }

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

            Node current = root;

            while (current != null) {
                if (current.left == null) {
                    return current.key;
                } else {
                    current = current.left;
                }
            }

            return null;
        }

        //Used for delete
        public Node min(Node node) {
            if (node == null) {
                return null;
            }

            Node current = node;

            while (current != null) {
                if (current.left == null) {
                    return current;
                } else {
                    current = current.left;
                }
            }

            return null;
        }

        public Key max() {
            if (root == null) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            Node current = root;

            while (current != null) {
                if (current.right == null) {
                    return current.key;
                } else {
                    current = current.right;
                }
            }

            return null;
        }

        //Returns the highest key in the symbol table smaller than or equal to key.
        public Key floor(Key key) {

            Node current = root;
            Key currentFloor = null;

            while (current != null) {
                int compare = key.compareTo(current.key);

                if (compare < 0) {
                    current = current.left;
                } else if (compare > 0) {
                    currentFloor = current.key;
                    current = current.right;
                } else {
                    currentFloor = current.key;
                    break;
                }
            }

            return currentFloor;
        }

        //Returns the smallest key in the symbol table greater than or equal to key.
        public Key ceiling(Key key) {

            Node current = root;
            Key currentCeiling = null;

            while (current != null) {
                int compare = key.compareTo(current.key);

                if (compare < 0) {
                    currentCeiling = current.key;
                    current = current.left;
                } else if (compare > 0) {
                    current = current.right;
                } else {
                    currentCeiling = current.key;
                    break;
                }
            }

            return currentCeiling;
        }

        public Key select(int index) {
            if (index >= size()) {
                throw new IllegalArgumentException("Index is higher than tree size");
            }

            Node current = root;

            while (current != null) {
                int leftSubtreeSize = size(current.left);

                if (leftSubtreeSize == index) {
                    return current.key;
                } else if (leftSubtreeSize > index) {
                    current = current.left;
                } else {
                    index -= (leftSubtreeSize + 1);
                    current = current.right;
                }

            }

            return null;
        }

        public int rank(Key key) {
            Node current = root;

            int rank = 0;

            while (current != null) {
                int compare = key.compareTo(current.key);

                if (compare < 0) {
                    current = current.left;
                } else if (compare > 0) {
                    rank += size(current.left) + 1;
                    current = current.right;
                } else {
                    rank += size(current.left);
                    return rank;
                }
            }

            return rank;
        }

        public void deleteMin() {
            root = deleteMin(root, false);
        }

        //Needs a Node parameter because it is used in delete()
        private Node deleteMin(Node subtreeRoot, boolean isDelete) {
            if (isEmpty()) {
                return null;
            }

            if (subtreeRoot.left == null) {
                return subtreeRoot.right;
            }

            if (!isRed(subtreeRoot.left) && !isRed(subtreeRoot.right)) {
                subtreeRoot.color = RED;
            }

            Node grandparent = null;
            Node parent = null;
            Node currentNode = subtreeRoot;

            //Used to avoid decrementing the size of the deleted node's right child after deletion and promotion
            //It can't use updateSizeOfNodesInPath(min()) because deleteMin() is used in delete() operation
            boolean nodeDeleted = false;

            while (currentNode != null) {

                if (!nodeDeleted) {
                    currentNode.size = currentNode.size - 1;
                }

                if (!isRed(currentNode.left) && currentNode.left != null && !isRed(currentNode.left.left)) {

                    //Check if subtree root will be updated
                    boolean updateSubtreeRoot = false;
                    if (currentNode == subtreeRoot) {
                        updateSubtreeRoot = true;
                    }

                    currentNode = moveRedLeft(currentNode);
                    updateParentReference(parent, currentNode);

                    if (updateSubtreeRoot) {
                        subtreeRoot = currentNode;
                    }
                }

                //Delete node
                if (currentNode.left != null && currentNode.left.left == null) {
                    currentNode.left = currentNode.left.right;
                    nodeDeleted = true;
                }

                //Balance on the way down
                Node oldParent = parent;

                if (parent != null) {
                    parent = balance(parent);

                    if (grandparent == null && !isDelete) {
                        root = parent;
                    } else if (grandparent != null) {
                        grandparent.left = parent;
                    }

                    if (parent.left == subtreeRoot || parent.right == subtreeRoot) {
                        subtreeRoot = parent;
                    }
                }

                grandparent = parent;
                if (oldParent != parent) {
                    parent = oldParent;
                } else {
                    parent = currentNode;
                }

                currentNode = currentNode.left;
            }

            //Balance on the bottom
            //Balance parent
            if (parent != grandparent) {
                parent = balance(parent);

                if (grandparent == null && !isDelete) {
                    root = parent;
                } else if (grandparent != null) {
                    grandparent.left = parent;
                }
                if (parent.left == subtreeRoot || parent.right == subtreeRoot) {
                    subtreeRoot = parent;
                }
            }

            //Balance bottom node (if different from parent)
            if (parent.left != null) {
                parent.left = balance(parent.left);

                if (parent.left.left == subtreeRoot || parent.left.right == subtreeRoot) {
                    subtreeRoot = parent;
                }
            }

            if (!isEmpty()) {
                root.color = BLACK;
            }

            return subtreeRoot;
        }

        public void deleteMax() {
            if (isEmpty()) {
                return;
            }

            updateSizeOfNodesInPath(max());

            if (root.right == null) {
                root = root.left;
                return;
            }

            if (!isRed(root.left) && !isRed(root.right)) {
                root.color = RED;
            }

            Node grandparent = null;
            Node parent = null;
            Node currentNode = root;

            while (currentNode != null) {

                if (!isRed(currentNode.right) && isRed(currentNode.left)) {
                    currentNode = rotateRight(currentNode);
                    updateParentReference(parent, currentNode);
                }

                //Delete node
                if (currentNode.right == null) {
                    if (parent == null) {
                        root = currentNode.left;
                    } else {
                        parent.right = currentNode.left;
                    }
                    currentNode = parent;
                }

                if (!isRed(currentNode.right) && currentNode.right != null && !isRed(currentNode.right.left)) {
                    currentNode = moveRedRight(currentNode);
                    updateParentReference(parent, currentNode);
                }

                //Balance on the way down
                if (parent != null) {
                    parent = balance(parent);

                    if (grandparent == null) {
                        root = parent;
                    } else  {
                        grandparent.right = parent;
                    }
                }

                grandparent = parent;
                parent = currentNode;
                currentNode = currentNode.right;
            }

            //Balance on the bottom
            //Balance parent
            if (parent != grandparent) {
                parent = balance(parent);

                if (grandparent == null) {
                    root = parent;
                } else {
                    grandparent.right = parent;
                }
            }

            //Balance bottom node (if different from parent)
            if (parent.right != null) {
                parent.right = balance(parent.right);
            }

            if (!isEmpty()) {
                root.color = BLACK;
            }
        }

        public void delete(Key key) {
            if (isEmpty()) {
                return;
            }

            if (!contains(key)) {
                return;
            }

            updateSizeOfNodesInPath(key);

            if (!isRed(root.left) && !isRed(root.right)) {
                root.color = RED;
            }

            Node grandparent = null;
            Node parent = null;
            Node currentNode = root;

            while (currentNode != null) {

                if (key.compareTo(currentNode.key) < 0) {

                    if (!isRed(currentNode.left) && currentNode.left != null && !isRed(currentNode.left.left)) {
                        currentNode = moveRedLeft(currentNode);
                        updateParentReference(parent, currentNode);
                    }

                    //Balance on the way down
                    Node oldParent = parent;

                    if (parent != null) {
                        parent = balance(parent);
                        updateParentReference(grandparent, parent);
                    }

                    grandparent = oldParent;
                    parent = currentNode;
                    currentNode = currentNode.left;
                } else {
                    if (!isRed(currentNode.right) && isRed(currentNode.left)) {
                        currentNode = rotateRight(currentNode);
                        updateParentReference(parent, currentNode);
                    }

                    //Delete node
                    if (key.compareTo(currentNode.key) == 0 && currentNode.right == null) {
                        if (parent == null) {
                            root = currentNode.left;
                        } else {
                            boolean isCurrentNodeLeftChild = currentNode.key.compareTo(parent.key) < 0;

                            if (isCurrentNodeLeftChild) {
                                parent.left = currentNode.left;
                            } else {
                                parent.right = currentNode.left;
                            }
                        }

                        break;
                    }

                    if (!isRed(currentNode.right) && currentNode.right != null && !isRed(currentNode.right.left)) {
                        currentNode = moveRedRight(currentNode);
                        updateParentReference(parent, currentNode);
                    }

                    //Delete node
                    if (key.compareTo(currentNode.key) == 0) {
                        Node aux = min(currentNode.right);
                        currentNode.key = aux.key;
                        currentNode.value = aux.value;
                        currentNode.right = deleteMin(currentNode.right, true);

                        break;
                    } else {
                        //Balance parent
                        if (parent != null) {
                            parent = balance(parent);
                            updateParentReference(grandparent, parent);
                        }

                        grandparent = parent;
                        parent = currentNode;
                        currentNode = currentNode.right;
                    }
                }
            }

            //Balance on the bottom
            if (parent != null) {
                parent = balance(parent);
                updateParentReference(grandparent, parent);
            }

            if (!isEmpty()) {
                root.color = BLACK;
            }
        }

        private void updateSizeOfNodesInPath(Key key) {
            Node current = root;

            while (current != null) {
                current.size = current.size - 1;

                int compare = key.compareTo(current.key);

                if (compare < 0) {
                    current = current.left;
                } else if (compare > 0) {
                    current = current.right;
                } else {
                    break;
                }
            }
        }

        private void updateParentReference(Node parent, Node child) {
            if (parent == null) {
                root = child;
            } else {
                boolean isCurrentNodeLeftChild = child.key.compareTo(parent.key) < 0;

                if (isCurrentNodeLeftChild) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
            }
        }

        private Node moveRedLeft(Node node) {
            //Assuming that node is red and both node.left and node.left.left are black,
            // make node.left or one of its children red
            flipColors(node);

            if (node.right != null && isRed(node.right.left)) {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
                flipColors(node);
            }

            return node;
        }

        private Node moveRedRight(Node node) {
            //Assuming that node is red and both node.right and node.right.left are black,
            // make node.right or one of its children red
            flipColors(node);

            if (node.left != null && isRed(node.left.left)) {
                node = rotateRight(node);
                flipColors(node);
            }

            return node;
        }

        private Node balance(Node node) {
            if (node == null) {
                return null;
            }

            if (isRed(node.right)) {
                node = rotateLeft(node);
            }

            if (isRed(node.left) && isRed(node.left.left)) {
                node = rotateRight(node);
            }

            if (isRed(node.left) && isRed(node.right)) {
                flipColors(node);
            }

            //Not used in the iterative version - the size is computed while moving down during deletion
            //node.size = size(node.left) + 1 + size(node.right);

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

        public Iterable<Key> keys(Node node,  Queue<Key> queue, Key low, Key high) {
            Stack<Node> stack = new Stack<>();

            while (node != null || !stack.isEmpty()) {
                if (node != null) {
                    if (low.compareTo(node.key) <= 0 && node.key.compareTo(high) <= 0) {
                        stack.push(node);
                    }

                    if (low.compareTo(node.key) <= 0) {
                        node = node.left;
                    } else {
                        node = node.right;
                    }
                } else {
                    node = stack.pop();
                    queue.enqueue(node.key);

                    if (high.compareTo(node.key) >= 0) {
                        node = node.right;
                    }
                }
            }

            return queue;
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
            if (root == null) {
                return true;
            }

            Queue<Node> queue = new Queue<>();
            queue.enqueue(root);

            while (queue.size() > 0) {
                Node current = queue.dequeue();

                if (current.left != null) {
                    if (current.key.compareTo(current.left.key) < 0) {
                        return false;
                    }
                    queue.enqueue(current.left);
                }
                if (current.right != null) {
                    if (current.key.compareTo(current.right.key) > 0) {
                        return false;
                    }
                    queue.enqueue(current.right);
                }
            }

            return true;
        }

        private boolean isSubtreeCountConsistent() {
            if (root == null) {
                return true;
            }

            int totalSubtreeCount;

            Queue<Node> queue = new Queue<>();
            queue.enqueue(root);

            while (queue.size() > 0) {
                Node current = queue.dequeue();

                totalSubtreeCount = 0;

                if (current.left != null) {
                    queue.enqueue(current.left);
                    totalSubtreeCount += current.left.size;
                }
                if (current.right != null) {
                    queue.enqueue(current.right);
                    totalSubtreeCount += current.right.size;
                }

                if (current.size != totalSubtreeCount + 1) {
                    return false;
                }
            }
            return true;
        }

        private boolean isValid234Tree() {
            if (root == null) {
                return true;
            }

            Queue<Node> queue = new Queue<>();
            queue.enqueue(root);

            while (queue.size() > 0) {
                Node current = queue.dequeue();

                if (!isRed(current.left) && isRed(current.right)) {
                    return false;
                }
                if (isRed(current.left) && isRed(current.left.left)) {
                    return false;
                }
                if (isRed(current.left) && isRed(current.left.right)) {
                    return false;
                }
                if (isRed(current.right) && isRed(current.right.right)) {
                    return false;
                }
                if (isRed(current.right) && isRed(current.right.left)) {
                    return false;
                }

                if (current.left != null) {
                    queue.enqueue(current.left);
                }
                if (current.right != null) {
                    queue.enqueue(current.right);
                }
            }

            return true;
        }

        public boolean isBalanced() {
            if (isEmpty()) {
                return true;
            }

            int blackNodes = 0; // number of black links on path from root to min

            Node currentNode = root;
            while (currentNode != null) {
                if (!isRed(currentNode)) {
                    blackNodes++;
                }

                currentNode = currentNode.left;
            }

            return isBalanced(blackNodes);
        }

        private boolean isBalanced(int blackNodes) {
            Queue<Node> queue = new Queue<>();
            queue.enqueue(root);
            root.numberOfBlackNodesInPath = 0;

            while (!queue.isEmpty()) {
                Node current = queue.dequeue();

                if (!isRed(current)) {
                    current.numberOfBlackNodesInPath++;
                }

                if (current.left != null) {
                    current.left.numberOfBlackNodesInPath = current.numberOfBlackNodesInPath;
                    queue.enqueue(current.left);
                } else {
                    if (current.numberOfBlackNodesInPath != blackNodes) {
                        return false;
                    }
                }

                if (current.right != null) {
                    current.right.numberOfBlackNodesInPath = current.numberOfBlackNodesInPath;
                    queue.enqueue(current.right);
                } else {
                    if (current.numberOfBlackNodesInPath != blackNodes) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public static void main(String[] args) {
        //Expected 2-3-4 tree
        //
        //                  (B)1
        //         (R)-1             (R)5
        //    (B)-2   (B)0       (B)3    (B)99
        // (R)-5               (R)2    (R)9
        //

        Exercise26_SingleTopDownPass singleTopDownPass = new Exercise26_SingleTopDownPass();
        RedBlackIterative234BST<Integer, Integer> redBlackIterative234BST = singleTopDownPass.new RedBlackIterative234BST<>();
        redBlackIterative234BST.put(5, 5);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        redBlackIterative234BST.put(1, 1);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        redBlackIterative234BST.put(9, 9);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        redBlackIterative234BST.put(2, 2);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        redBlackIterative234BST.put(0, 0);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        redBlackIterative234BST.put(99, 99);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        redBlackIterative234BST.put(-1, -1);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        redBlackIterative234BST.put(-2, -2);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        redBlackIterative234BST.put(3, 3);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        redBlackIterative234BST.put(-5, -5);
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true\n");

        StdOut.println("Size consistent: " + redBlackIterative234BST.isSubtreeCountConsistent() + " Expected: true\n");

        StdOut.println("Keys() test");

        for(Integer key : redBlackIterative234BST.keys()) {
            StdOut.println("Key " + key + ": " + redBlackIterative234BST.get(key));
        }
        StdOut.println("Expected: -5 -2 -1 0 1 2 3 5 9 99\n");

        //Test min()
        StdOut.println("Min key: " + redBlackIterative234BST.min() + " Expected: -5");

        //Test max()
        StdOut.println("Max key: " + redBlackIterative234BST.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + redBlackIterative234BST.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + redBlackIterative234BST.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + redBlackIterative234BST.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + redBlackIterative234BST.ceiling(15) + " Expected: 99");

        //Test select()
        StdOut.println("Select key of rank 4: " + redBlackIterative234BST.select(4) + " Expected: 1");

        //Test rank()
        StdOut.println("Rank of key 9: " + redBlackIterative234BST.rank(9) + " Expected: 8");
        StdOut.println("Rank of key 10: " + redBlackIterative234BST.rank(10) + " Expected: 9");

        //Test delete()
        StdOut.println("\nDelete key 2");
        redBlackIterative234BST.delete(2);

        for(Integer key : redBlackIterative234BST.keys()) {
            StdOut.println("Key " + key + ": " + redBlackIterative234BST.get(key));
        }
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + redBlackIterative234BST.isSubtreeCountConsistent() + " Expected: true");

        //Test deleteMin()
        StdOut.println("\nDelete min (key -5)");
        redBlackIterative234BST.deleteMin();

        for(Integer key : redBlackIterative234BST.keys()) {
            StdOut.println("Key " + key + ": " + redBlackIterative234BST.get(key));
        }
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + redBlackIterative234BST.isSubtreeCountConsistent() + " Expected: true");

        //Test deleteMax()
        StdOut.println("\nDelete max (key 99)");
        redBlackIterative234BST.deleteMax();

        for(Integer key : redBlackIterative234BST.keys()) {
            StdOut.println("Key " + key + ": " + redBlackIterative234BST.get(key));
        }
        StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
        StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
        StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
        StdOut.println("Size consistent: " + redBlackIterative234BST.isSubtreeCountConsistent() + " Expected: true");

        //Test keys() with range
        StdOut.println("\nKeys in range [2, 10]");
        for(Integer key : redBlackIterative234BST.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + redBlackIterative234BST.get(key));
        }

        StdOut.println("\nKeys in range [-4, -1]");
        for(Integer key : redBlackIterative234BST.keys(-4, -1)) {
            StdOut.println("Key " + key + ": " + redBlackIterative234BST.get(key));
        }

        //Delete all
        StdOut.println("\nDelete all");
        while (redBlackIterative234BST.size() > 0) {
            for(Integer key : redBlackIterative234BST.keys()) {
                StdOut.println("Key " + key + ": " + redBlackIterative234BST.get(key));
            }

            //redBlackIterative234BST.delete(redBlackIterative234BST.select(0));
            redBlackIterative234BST.delete(redBlackIterative234BST.select(redBlackIterative234BST.size() - 1));
            StdOut.println("Is valid 2-3-4 tree: " + redBlackIterative234BST.isValid234Tree() + " Expected: true");
            StdOut.println("Is balanced: " + redBlackIterative234BST.isBalanced() + " Expected: true");
            StdOut.println("Is BST: " + redBlackIterative234BST.isBST() + " Expected: true");
            StdOut.println("Size consistent: " + redBlackIterative234BST.isSubtreeCountConsistent() + " Expected: true");

            StdOut.println();
        }
    }
}
