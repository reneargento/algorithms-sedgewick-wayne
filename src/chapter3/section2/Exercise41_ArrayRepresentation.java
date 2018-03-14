package chapter3.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 11/06/17.
 */
@SuppressWarnings("unchecked")
public class Exercise41_ArrayRepresentation {

    private class BinarySearchTreeArrayRepresentation<Key extends Comparable<Key>, Value>{

        private Key[] keys;
        private Value[] values;
        private int[] size;
        private int[] leftLinks;
        private int[] rightLinks;

        BinarySearchTreeArrayRepresentation(int size) {
            keys = (Key[]) new Comparable[size];
            values = (Value[]) new Object[size];
            this.size = new int[size];
            leftLinks = new int[size];
            rightLinks = new int[size];

            for(int i = 0; i < size; i++) {
                leftLinks[i] = -1;
                rightLinks[i] = -1;
            }
        }

        public int size() {
            return size(0);
        }

        private int size(int index) {
            if (index == -1) {
                return 0;
            }

            return size[index];
        }

        public Value get(Key key) {
            return get(0, key);
        }

        private Value get(int index, Key key) {
            if (index == -1 || keys[index] == null) {
                return null;
            }

            int compare = key.compareTo(keys[index]);
            if (compare < 0) {
                return get(leftLinks[index], key);
            } else if (compare > 0) {
                return get(rightLinks[index], key);
            } else {
                return values[index];
            }
        }

        public void put(Key key, Value value) {
            if (size() == keys.length) {
                StdOut.println("Tree is full");
                return;
            }

            put(0, key, value);
        }

        private int put(int index, Key key, Value value) {
            if (index == -1 || keys[index] == null) {
                int nextElementIndex = size();

                keys[nextElementIndex] = key;
                values[nextElementIndex] = value;
                size[nextElementIndex] = 1;

                return nextElementIndex;
            }

            int compare = key.compareTo(keys[index]);

            if (compare < 0) {
                leftLinks[index] = put(leftLinks[index], key, value);
            } else if (compare > 0) {
                rightLinks[index] = put(rightLinks[index], key, value);
            } else {
                values[index] = value;
            }

            size[index] = size(leftLinks[index]) + 1 + size(rightLinks[index]);
            return index;
        }

        public Key min() {
            if (size() == 0) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            int minIndex = min(0);
            return keys[minIndex];
        }

        private int min(int index) {
            if (leftLinks[index] == -1) {
                return index;
            }

            return min(leftLinks[index]);
        }

        public Key max() {
            if (size() == 0) {
                throw new NoSuchElementException("Empty binary search tree");
            }

            int maxIndex = max(0);
            return keys[maxIndex];
        }

        private int max(int index) {
            if (rightLinks[index] == -1) {
                return index;
            }

            return max(rightLinks[index]);
        }

        public Key floor(Key key) {
            int index = floor(0, key);
            if (index == -1) {
                return null;
            }

            return keys[index];
        }

        private int floor(int index, Key key) {
            if (index == -1 || keys[index] == null) {
                return -1;
            }

            int compare = key.compareTo(keys[index]);

            if (compare == 0) {
                return index;
            } else if (compare < 0) {
                return floor(leftLinks[index], key);
            } else {
                int rightKeyIndex = floor(rightLinks[index], key);
                if (rightKeyIndex != -1) {
                    return rightKeyIndex;
                } else {
                    return index;
                }
            }
        }

        public Key ceiling(Key key) {
            int index = ceiling(0, key);
            if (index == -1) {
                return null;
            }

            return keys[index];
        }

        private int ceiling(int index, Key key) {
            if (index == -1 || keys[index] == null) {
                return -1;
            }

            int compare = key.compareTo(keys[index]);

            if (compare == 0) {
                return index;
            } else if (compare > 0) {
                return ceiling(rightLinks[index], key);
            } else {
                int leftKeyIndex = ceiling(leftLinks[index], key);
                if (leftKeyIndex != -1) {
                    return leftKeyIndex;
                } else {
                    return index;
                }
            }
        }

        public Key select(int index) {
            if (index >= size()) {
                throw new IllegalArgumentException("Index is higher than tree size");
            }

            int keyIndex = select(0, index);
            return keys[keyIndex];
        }

        private int select(int keyIndex, int index) {
            int leftSubtreeSize = size[leftLinks[keyIndex]];

            if (leftSubtreeSize == index) {
                return keyIndex;
            } else if (leftSubtreeSize > index) {
                return select(leftLinks[keyIndex], index);
            } else {
                return select(rightLinks[keyIndex], index - leftSubtreeSize - 1);
            }
        }

        public int rank(Key key) {
            return rank(0, key);
        }

        private int rank(int index, Key key) {
            if (index == -1 || keys[index] == null) {
                return 0;
            }

            //Returns the number of keys less than keys[index] in the subtree rooted at node
            int compare = key.compareTo(keys[index]);
            if (compare < 0) {
                return rank(leftLinks[index], key);
            } else if (compare > 0) {
                return size(leftLinks[index]) + 1 + rank(rightLinks[index], key);
            } else {
                return size[leftLinks[index]];
            }
        }

        private void eraseKeyData(int index) {
            keys[index] = null;
            values[index] = null;
            size[index] = 0;
            leftLinks[index] = -1;
            rightLinks[index] = -1;
        }

        private void copyDataFromOtherKey(int indexToCopyTo, int indexToCopyFrom) {
            keys[indexToCopyTo] = keys[indexToCopyFrom];
            values[indexToCopyTo] = values[indexToCopyFrom];
            size[indexToCopyTo] = size[indexToCopyFrom];
            leftLinks[indexToCopyTo] = leftLinks[indexToCopyFrom];
            rightLinks[indexToCopyTo] = rightLinks[indexToCopyFrom];
        }

        public void deleteMin() {
            int rootIndex = deleteMin(0, true);

            if (rootIndex == -1) {
                eraseKeyData(0);
                return;
            }

            //Update root
            if (rootIndex != 0) {
                copyDataFromOtherKey(0, rootIndex);
                eraseKeyData(rootIndex);
            }
        }

        //setKeyNull parameter is used because we do not want to set the key value as null when using deleteMin() inside delete()
        private int deleteMin(int index, boolean setKeyNull) {
            if (index == -1 || keys[index] == null) {
                return -1;
            }

            if (leftLinks[index] == -1) {
                int rightKeyLink = rightLinks[index];
                if (setKeyNull) {
                    eraseKeyData(index);
                }

                return rightKeyLink;
            }

            int leftIndex = deleteMin(leftLinks[index], setKeyNull);
            leftLinks[index] = leftIndex;

            size[index] = size(leftLinks[index]) + 1 + size(rightLinks[index]);
            return index;
        }

        public void deleteMax() {
            int rootIndex = deleteMax(0);

            if (rootIndex == -1) {
                eraseKeyData(0);
                return;
            }

            //Update root
            if (rootIndex != 0) {
                copyDataFromOtherKey(0, rootIndex);
                eraseKeyData(rootIndex);
            }
        }

        private int deleteMax(int index) {
            if (index == -1 || keys[index] == null) {
                return -1;
            }

            if (rightLinks[index] == -1) {
                int leftKeyLink = leftLinks[index];
                eraseKeyData(index);

                return leftKeyLink;
            }

            int rightIndex = deleteMax(rightLinks[index]);
            rightLinks[index] = rightIndex;

            size[index] = size(leftLinks[index]) + 1 + size(rightLinks[index]);
            return index;
        }

        public void delete(Key key) {
            int rootIndex = delete(0, key);

            if (rootIndex == -1) {
                eraseKeyData(0);
                return;
            }

            //Update root
            if (rootIndex != 0) {
                copyDataFromOtherKey(0, rootIndex);
                eraseKeyData(rootIndex);
            }
        }

        private int delete(int index, Key key) {
            if (index == -1 || keys[index] == null) {
                return -1;
            }

            int compare = key.compareTo(keys[index]);
            if (compare < 0) {
                int leftIndex = delete(leftLinks[index], key);
                leftLinks[index] = leftIndex;
            } else if (compare > 0) {
                int rightIndex = delete(rightLinks[index], key);
                rightLinks[index] = rightIndex;
            } else {
                keys[index] = null;
                values[index] = null;
                size[index] = 0;

                if (leftLinks[index] == -1) {
                    int rightLinkIndex = rightLinks[index];
                    rightLinks[index] = -1;

                    return rightLinkIndex;
                } else if (rightLinks[index] == -1) {
                    int leftLinkIndex = leftLinks[index];
                    leftLinks[index] = -1;

                    return leftLinkIndex;
                } else {
                    int promotedIndex = min(rightLinks[index]);
                    rightLinks[promotedIndex] = deleteMin(rightLinks[index], false);
                    leftLinks[promotedIndex] = leftLinks[index];

                    rightLinks[index] = -1;
                    leftLinks[index] = -1;

                    index = promotedIndex;
                }
            }

            size[index] = size(leftLinks[index]) + 1 + size(rightLinks[index]);
            return index;
        }

        public Iterable<Key> keys() {
            return keys(min(), max());
        }

        public Iterable<Key> keys(Key low, Key high) {
            Queue<Key> queue = new Queue<>();
            keys(0, queue, low, high);
            return queue;
        }

        private void keys(int index, Queue<Key> queue, Key low, Key high) {
            if (index == -1 || keys[index] == null) {
                return;
            }

            int compareLow = low.compareTo(keys[index]);
            int compareHigh = high.compareTo(keys[index]);

            if (compareLow < 0) {
                keys(leftLinks[index], queue, low, high);
            }

            if (compareLow <= 0 && compareHigh >= 0) {
                queue.enqueue(keys[index]);
            }

            if (compareHigh > 0) {
                keys(rightLinks[index], queue, low, high);
            }
        }
    }

    public static void main(String[] args) {
        Exercise41_ArrayRepresentation arrayRepresentation = new Exercise41_ArrayRepresentation();
        arrayRepresentation.testArrayRepresentationTree();
        arrayRepresentation.doExperiment();
    }

    private void testArrayRepresentationTree() {
        BinarySearchTreeArrayRepresentation<Integer, String> binarySearchTreeArrayRepresentation =
                new BinarySearchTreeArrayRepresentation<>(10);

        //Test put()
        binarySearchTreeArrayRepresentation.put(5, "Value 5");
        binarySearchTreeArrayRepresentation.put(1, "Value 1");
        binarySearchTreeArrayRepresentation.put(9, "Value 9");
        binarySearchTreeArrayRepresentation.put(8, "Value 8");
        binarySearchTreeArrayRepresentation.put(2, "Value 2");
        binarySearchTreeArrayRepresentation.put(0, "Value 0");
        binarySearchTreeArrayRepresentation.put(99, "Value 99");

        StdOut.println();

        //Test size()
        StdOut.println("Size: " + binarySearchTreeArrayRepresentation.size() + " Expected: 7");

        //Test get() and keys()
        for(Integer key : binarySearchTreeArrayRepresentation.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTreeArrayRepresentation.get(key));
        }

        //Test delete()
        StdOut.println("\nDelete key 2");
        binarySearchTreeArrayRepresentation.delete(2);
        for(Integer key : binarySearchTreeArrayRepresentation.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTreeArrayRepresentation.get(key));
        }

        StdOut.println();

        //Test size()
        StdOut.println("Size: " + binarySearchTreeArrayRepresentation.size() + " Expected: 6");

        //Test min()
        StdOut.println("Min key: " + binarySearchTreeArrayRepresentation.min() + " Expected: 0");

        //Test max()
        StdOut.println("Max key: " + binarySearchTreeArrayRepresentation.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + binarySearchTreeArrayRepresentation.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + binarySearchTreeArrayRepresentation.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + binarySearchTreeArrayRepresentation.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + binarySearchTreeArrayRepresentation.ceiling(15) + " Expected: 99");

        //Test deleteMin()
        StdOut.println("\nDelete min (key 0)");

        binarySearchTreeArrayRepresentation.deleteMin();
        for(Integer key : binarySearchTreeArrayRepresentation.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTreeArrayRepresentation.get(key));
        }

        //Test delete node with left and right children
        StdOut.println("\nDelete key 9");
        binarySearchTreeArrayRepresentation.delete(9);
        for(Integer key : binarySearchTreeArrayRepresentation.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTreeArrayRepresentation.get(key));
        }

        //Test deleteMax()
        StdOut.println("\nDelete max (key 99)");

        binarySearchTreeArrayRepresentation.deleteMax();
        for(Integer key : binarySearchTreeArrayRepresentation.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchTreeArrayRepresentation.get(key));
        }

        //Test keys() with range
        StdOut.println();
        StdOut.println("Keys in range [2, 10]");
        for(Integer key : binarySearchTreeArrayRepresentation.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + binarySearchTreeArrayRepresentation.get(key));
        }

        StdOut.println("Size: " + binarySearchTreeArrayRepresentation.size() + " Expected: 3");
    }

    private void doExperiment() {
        int maxSize = 1000000;

        int[] valuesToInsert = new int[maxSize];
        int[] valuesToSearch = new int[maxSize];
        int[] valuesToDelete = new int[maxSize];

        for(int i = 0; i < maxSize; i++) {
            int randomValue = StdRandom.uniform(maxSize);
            valuesToInsert[i] = randomValue;
        }

        for(int i = 0; i < maxSize; i++) {
            int randomValue = StdRandom.uniform(maxSize);
            valuesToSearch[i] = randomValue;
        }

        for(int i = 0; i < maxSize; i++) {
            int randomValue = StdRandom.uniform(maxSize);
            valuesToDelete[i] = randomValue;
        }

        //Test BST with array representation
        BinarySearchTreeArrayRepresentation<Integer, Integer> binarySearchTreeArrayRepresentation =
                new BinarySearchTreeArrayRepresentation<>(maxSize);

        Stopwatch timer = new Stopwatch();
        for(int value : valuesToInsert) {
            binarySearchTreeArrayRepresentation.put(value, value);
        }
        double bstArrayInsertTime = timer.elapsedTime();

        timer = new Stopwatch();
        for(int value : valuesToSearch) {
            binarySearchTreeArrayRepresentation.get(value);
        }
        double bstArraySearchTime = timer.elapsedTime();

        timer = new Stopwatch();
        for(int value : valuesToDelete) {
            binarySearchTreeArrayRepresentation.delete(value);
        }
        double bstArrayDeleteTime = timer.elapsedTime();

        //Test BST
        BinarySearchTree<Integer, Integer> binarySearchTree = new BinarySearchTree<>();

        timer = new Stopwatch();
        for(int value : valuesToInsert) {
            binarySearchTree.put(value, value);
        }
        double bstInsertTime = timer.elapsedTime();

        timer = new Stopwatch();
        for(int value : valuesToSearch) {
            binarySearchTree.get(value);
        }
        double bstSearchTime = timer.elapsedTime();

        timer = new Stopwatch();
        for(int value : valuesToDelete) {
            binarySearchTree.delete(value);
        }
        double bstDeleteTime = timer.elapsedTime();

        StdOut.println("\nExperiment results for N = " + maxSize);
        StdOut.println("Array representation BST insert time = " + bstArrayInsertTime);
        StdOut.println("Array representation BST search time = " + bstArraySearchTime);
        StdOut.println("Array representation BST delete time = " + bstArrayDeleteTime);
        StdOut.println("Standard BST insert time = " + bstInsertTime);
        StdOut.println("Standard BST search time = " + bstSearchTime);
        StdOut.println("Standard BST delete time = " + bstDeleteTime);
    }

}
