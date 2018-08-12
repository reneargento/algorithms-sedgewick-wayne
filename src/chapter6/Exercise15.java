package chapter6;

import chapter3.section1.BinarySearchSymbolTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 23/07/18.
 */
public class Exercise15 {

    private class BinarySearchSTPage<Key extends Comparable<Key>> implements PageInterface<Key> {

        private class PageValue {
            private PageInterface<Key> childPage;

            PageValue(PageInterface<Key> childPage) {
                this.childPage = childPage;
            }
        }

        private BinarySearchSymbolTable<Key, PageValue> binarySearchSymbolTable;
        private boolean isOpen;
        private boolean isExternal;

        // Reference to pages in memory on the system
        private HashSet<PageInterface> pagesInMemory;
        private int maxNumberOfNodes;

        BinarySearchSTPage(boolean bottom, int maxNumberOfNodes, HashSet<PageInterface> pagesInMemory) {
            if (maxNumberOfNodes % 2 != 0 || maxNumberOfNodes == 2) {
                throw new IllegalArgumentException("Max number of nodes must be divisible by 2 and higher than 2");
            }

            binarySearchSymbolTable = new BinarySearchSymbolTable<>();
            this.pagesInMemory = pagesInMemory;
            this.maxNumberOfNodes = maxNumberOfNodes;
            isExternal = bottom;
            open();
        }

        private void open() {
            pagesInMemory.add(this);
            isOpen = true;
        }

        @Override
        public void close() {
            StringJoiner pageContent = new StringJoiner(" ");

            for (Key key : keys()) {
                pageContent.add(String.valueOf(key));
            }

            StdOut.println("Page content: " + pageContent.toString());
            pagesInMemory.delete(this);
            isOpen = false;
        }

        @Override
        public PageInterface<Key> getPage(Key key) {
            PageValue pageValue = binarySearchSymbolTable.get(key);

            if (pageValue != null) {
                return pageValue.childPage;
            } else {
                return null;
            }
        }

        @Override
        public void add(Key key) {
            if (!isExternal()) {
                throw new IllegalArgumentException("Cannot add key directly to an internal page");
            }

            // External pages do not point to any page
            binarySearchSymbolTable.put(key, new PageValue(null));
        }

        @Override
        public void add(PageInterface<Key> page) {
            Key minKey = ((BinarySearchSTPage<Key>) page).binarySearchSymbolTable.min();
            binarySearchSymbolTable.put(minKey, new PageValue(page));
        }

        @Override
        public boolean isExternal() {
            return isExternal;
        }

        @Override
        public boolean contains(Key key) {
            if (!isExternal()) {
                throw new IllegalArgumentException("Cannot check contains directly on an internal page");
            }

            return binarySearchSymbolTable.contains(key);
        }

        @Override
        public PageInterface<Key> next(Key key) {
            if (isExternal()) {
                throw new IllegalArgumentException("Next cannot be called on an external page");
            }

            Key nextKey = binarySearchSymbolTable.floor(key);
            return binarySearchSymbolTable.get(nextKey).childPage;
        }

        @Override
        public boolean isFull() {
            return binarySearchSymbolTable.size() == maxNumberOfNodes;
        }

        @Override
        public PageInterface<Key> split() {
            List<Key> keysToMove = new ArrayList<>();
            int middleRank = binarySearchSymbolTable.size() / 2;

            for (int index = middleRank; index < binarySearchSymbolTable.size(); index++) {
                Key keyToMove = binarySearchSymbolTable.select(index);
                keysToMove.add(keyToMove);
            }

            PageInterface<Key> newPage = new BinarySearchSTPage<>(isExternal, maxNumberOfNodes, pagesInMemory);

            for (Key key : keysToMove) {
                PageInterface<Key> pageLink = binarySearchSymbolTable.get(key).childPage;
                binarySearchSymbolTable.delete(key);

                if (!isExternal()) {
                    newPage.add(pageLink);
                } else {
                    newPage.add(key);
                }
            }

            return newPage;
        }

        @Override
        public Iterable<Key> keys() {
            return binarySearchSymbolTable.keys();
        }

        @Override
        public int getMaxNumberOfNodes() {
            return maxNumberOfNodes;
        }
    }

    private class BTreeSETWithBinarySearchSTPage<Key extends Comparable<Key>> {

        private HashSet<PageInterface> pagesInMemory = new HashSet<>();
        private PageInterface<Key> root = new BinarySearchSTPage<>(true, MAX_NUMBER_OF_NODES, pagesInMemory);

        // By convention MAX_NUMBER_OF_NODES is always an even number >= 4
        private static final int MAX_NUMBER_OF_NODES = 4;

        public BTreeSETWithBinarySearchSTPage(Key sentinel) {
            add(sentinel);
        }

        public boolean contains(Key key) {
            return contains(root, key);
        }

        private boolean contains(PageInterface<Key> page, Key key) {
            if (page.isExternal()) {
                return page.contains(key);
            }

            return contains(page.next(key), key);
        }

        public void add(Key key) {
            add(root, key);

            if (root.isFull()) {
                PageInterface<Key> leftHalf = root;
                PageInterface<Key> rightHalf = root.split();

                root = new BinarySearchSTPage<>(false, MAX_NUMBER_OF_NODES, pagesInMemory);
                root.add(leftHalf);
                root.add(rightHalf);
            }
        }

        public void add(PageInterface<Key> page, Key key) {
            if (page.isExternal()) {
                page.add(key);
                return;
            }

            PageInterface<Key> next = page.next(key);
            add(next, key);

            if (next.isFull()) {
                page.add(next.split());
            }
            next.close();
        }
    }

    public static void main(String[] args) {
        BTreeSETWithBinarySearchSTPage<String> bTreeSET = new Exercise15().new BTreeSETWithBinarySearchSTPage<>("*");

        bTreeSET.add("Rene");
        bTreeSET.add("Sedgewick");
        bTreeSET.add("Wayne");
        bTreeSET.add("B-tree");
        bTreeSET.add("Binary tree");
        bTreeSET.add("Red-black tree");
        bTreeSET.add("AVL tree");
        bTreeSET.add("Segment tree");
        bTreeSET.add("Fenwick tree");

        StdOut.println("\nTests");

        StdOut.println("Contains Binary tree: " + bTreeSET.contains("Binary tree") + " Expected: true");
        StdOut.println("Contains Red-black tree: " + bTreeSET.contains("Red-black tree") + " Expected: true");
        StdOut.println("Contains B-tree: " + bTreeSET.contains("B-tree") + " Expected: true");
        StdOut.println("Contains Rene: " + bTreeSET.contains("Rene") + " Expected: true");
        StdOut.println("Contains Binary trees: " + bTreeSET.contains("Binary trees") + " Expected: false");
        StdOut.println("Contains Binary tre: " + bTreeSET.contains("Binary tre") + " Expected: false");
        StdOut.println("Contains Binary : " + bTreeSET.contains("Binary ") + " Expected: false");
    }

}