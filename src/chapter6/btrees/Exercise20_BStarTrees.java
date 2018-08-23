package chapter6.btrees;

import chapter3.section1.BinarySearchSymbolTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 12/08/18.
 */
public class Exercise20_BStarTrees {

    public class BStarTreePage<Key extends Comparable<Key>> {

        private class PageValue {
            private BStarTreePage<Key> childPage;

            PageValue(BStarTreePage<Key> childPage) {
                this.childPage = childPage;
            }
        }

        private BinarySearchSymbolTable<Key, PageValue> binarySearchSymbolTable;
        private boolean isOpen;
        private boolean isExternal;

        // Reference to pages in memory on the system
        private HashSet<BStarTreePage> pagesInMemory;
        private int maxNumberOfNodesInInternalPages;
        private int maxNumberOfNodesInRoot;
        private int minNumberOfNodes;

        BStarTreePage(boolean bottom, int maxNumberOfNodesInInternalPages, HashSet<BStarTreePage> pagesInMemory) {
            if (maxNumberOfNodesInInternalPages % 3 != 0 || maxNumberOfNodesInInternalPages == 3) {
                throw new IllegalArgumentException("Max number of nodes must be divisible by 3 and higher than 3");
            }

            binarySearchSymbolTable = new BinarySearchSymbolTable<>();
            this.pagesInMemory = pagesInMemory;
            this.maxNumberOfNodesInInternalPages = maxNumberOfNodesInInternalPages;
            maxNumberOfNodesInRoot = (4 * maxNumberOfNodesInInternalPages) / 3;
            minNumberOfNodes = 2 * maxNumberOfNodesInInternalPages / 3;

            isExternal = bottom;
            open();
        }

        public int pageSize() {
            return binarySearchSymbolTable.size();
        }

        private void open() {
            pagesInMemory.add(this);
            isOpen = true;
        }

        public void close(boolean verbose) {
            StringJoiner pageContent = new StringJoiner(" ");

            for (Key key : keys()) {
                pageContent.add(String.valueOf(key));
            }

            if (verbose) {
                StdOut.println("Page content: " + pageContent.toString());
            }

            pagesInMemory.delete(this);
            isOpen = false;
        }

        public BStarTreePage<Key> getPage(Key key) {
            PageValue pageValue = binarySearchSymbolTable.get(key);

            if (pageValue != null) {
                return pageValue.childPage;
            } else {
                return null;
            }
        }

        public void add(Key key) {
            if (!isExternal()) {
                throw new IllegalArgumentException("Cannot add key directly to an internal page");
            }

            // External pages do not point to any page
            binarySearchSymbolTable.put(key, new PageValue(null));
        }

        public void add(BStarTreePage<Key> page) {
            Key minKey = page.binarySearchSymbolTable.min();
            binarySearchSymbolTable.put(minKey, new PageValue(page));
        }

        public boolean isExternal() {
            return isExternal;
        }

        public boolean contains(Key key) {
            if (!isExternal()) {
                throw new IllegalArgumentException("Cannot check contains directly on an internal page");
            }

            return binarySearchSymbolTable.contains(key);
        }

        public BStarTreePage<Key> next(Key key) {
            if (isExternal()) {
                throw new IllegalArgumentException("Next cannot be called on an external page");
            }

            Key nextKey = binarySearchSymbolTable.floor(key);

            if (nextKey == null) {
                return null;
            }

            return binarySearchSymbolTable.get(nextKey).childPage;
        }

        public boolean isFull() {
            return binarySearchSymbolTable.size() == maxNumberOfNodesInInternalPages;
        }

        // Assumes this page is the root
        public boolean isRootFull() {
            return binarySearchSymbolTable.size() == maxNumberOfNodesInRoot;
        }

        // Assumes this page is the root
        public BStarTreePage<Key> splitRoot() {
            List<Key> keysToMove = new ArrayList<>();
            int middleRank = binarySearchSymbolTable.size() / 2;

            for (int index = middleRank; index < binarySearchSymbolTable.size(); index++) {
                Key keyToMove = binarySearchSymbolTable.select(index);
                keysToMove.add(keyToMove);
            }

            BStarTreePage<Key> newPage = new BStarTreePage<>(isExternal, maxNumberOfNodesInInternalPages, pagesInMemory);

            for (Key key : keysToMove) {
                BStarTreePage<Key> pageLink = binarySearchSymbolTable.get(key).childPage;
                binarySearchSymbolTable.delete(key);

                if (!isExternal()) {
                    newPage.add(pageLink);
                } else {
                    newPage.add(key);
                }
            }

            return newPage;
        }

        public void split() {
            int numberOfChildren = binarySearchSymbolTable.size();

            int maxPossibleNumberOfItems = numberOfChildren * (maxNumberOfNodesInInternalPages - 1);
            int numberOfItems = 0;

            List<Key> pageKeys = new ArrayList<>();

            List<Key> keysToReallocate = new ArrayList<>();
            List<PageValue> valuesToReallocate = new ArrayList<>();

            // O(M^2)
            for(Key key : keys()) {
                pageKeys.add(key);

                BStarTreePage<Key> childPage = binarySearchSymbolTable.get(key).childPage;
                numberOfItems += childPage.pageSize();

                for (Key childKey : childPage.keys()) {
                    PageValue grandchildPage = childPage.binarySearchSymbolTable.get(childKey);

                    keysToReallocate.add(childKey);
                    valuesToReallocate.add(grandchildPage);
                }
            }

            if (numberOfItems > maxPossibleNumberOfItems) {
                // Should create a new node
                numberOfChildren++;
            }

            deleteAllKeys(pageKeys);

            boolean areChildrenExternalPages = valuesToReallocate.get(0).childPage == null;

            int numberOfItemsPerNode = numberOfItems / numberOfChildren;

            // Nodes should have at least 2M/3 entries (except the rightmost child node, which can sometimes have less)
            numberOfItemsPerNode = Math.max(numberOfItemsPerNode, minNumberOfNodes);

            reallocateItems(keysToReallocate, valuesToReallocate, numberOfItemsPerNode, areChildrenExternalPages);
        }

        private void deleteAllKeys(List<Key> pageKeys) {
            for( Key key : pageKeys) {
                binarySearchSymbolTable.delete(key);
            }
        }

        private void reallocateItems(List<Key> keysToDistribute, List<PageValue> valuesToDistribute,
                                     int numberOfItemsPerNode, boolean isExternalPage) {
            int currentNumberOfKeysInNewNode = 0;
            BStarTreePage<Key> newChildPage = new BStarTreePage<>(isExternalPage, maxNumberOfNodesInInternalPages,
                    pagesInMemory);

            for (int i = 0; i < keysToDistribute.size(); i++) {
                Key key = keysToDistribute.get(i);

                if (isExternalPage) {
                    newChildPage.add(key);
                } else {
                    PageValue pageValue = valuesToDistribute.get(i);
                    newChildPage.add(pageValue.childPage);
                }

                currentNumberOfKeysInNewNode++;

                if (currentNumberOfKeysInNewNode == numberOfItemsPerNode
                        || i == keysToDistribute.size() - 1) {
                    add(newChildPage);

                    if (i != keysToDistribute.size() - 1) {
                        newChildPage = new BStarTreePage<>(isExternalPage, maxNumberOfNodesInInternalPages, pagesInMemory);
                        currentNumberOfKeysInNewNode = 0;
                    }
                }
            }
        }

        public Iterable<Key> keys() {
            return binarySearchSymbolTable.keys();
        }
    }

    public class BStarTree<Key extends Comparable<Key>> {

        private static final int DEFAULT_MAX_NUMBER_OF_NODES_PER_INTERNAL_PAGE = 6;
        private static final boolean DEFAULT_VERBOSE = false;

        private int maxNumberOfNodesInInternalPages;
        private HashSet<BStarTreePage> pagesInMemory;
        private boolean verbose;

        private BStarTreePage<Key> root;

        public BStarTree(Key sentinel) {
            this(sentinel, DEFAULT_MAX_NUMBER_OF_NODES_PER_INTERNAL_PAGE, DEFAULT_VERBOSE);
        }

        public BStarTree(Key sentinel, int maxNumberOfNodesInInternalPages, boolean verbose) {
            if (maxNumberOfNodesInInternalPages % 3 != 0 || maxNumberOfNodesInInternalPages == 3) {
                throw new IllegalArgumentException("Max number of nodes must be divisible by 3 and higher than 3");
            }

            pagesInMemory = new HashSet<>();
            root = new BStarTreePage<>(true, maxNumberOfNodesInInternalPages, pagesInMemory);

            this.verbose = verbose;
            this.maxNumberOfNodesInInternalPages = maxNumberOfNodesInInternalPages;

            add(sentinel);
        }

        public boolean contains(Key key) {
            return contains(root, key);
        }

        private boolean contains(BStarTreePage<Key> page, Key key) {
            if (page.isExternal()) {
                return page.contains(key);
            }

            return contains(page.next(key), key);
        }

        public void add(Key key) {
            add(root, key);

            if (root.isRootFull()) {
                BStarTreePage<Key> leftHalf = root;
                BStarTreePage<Key> rightHalf = root.splitRoot();

                root = new BStarTreePage<>(false, maxNumberOfNodesInInternalPages, pagesInMemory);
                root.add(leftHalf);
                root.add(rightHalf);
            }
        }

        public void add(BStarTreePage<Key> page, Key key) {
            if (page.isExternal()) {
                page.add(key);
                return;
            }

            BStarTreePage<Key> next = page.next(key);
            add(next, key);

            if (next.isFull()) {
                page.split();
            }
            next.close(verbose);
        }
    }

    public static void main(String[] args) {
        BStarTree<String> bStarTree = new Exercise20_BStarTrees().new BStarTree<>("*");

        bStarTree.add("Rene");
        bStarTree.add("Sedgewick");
        bStarTree.add("Wayne");
        bStarTree.add("B-tree");
        bStarTree.add("Binary tree");
        bStarTree.add("Red-black tree");
        // Split root node
        bStarTree.add("AVL tree");
        bStarTree.add("Segment tree");
        // Split non-root node with no need to create a new child
        bStarTree.add("Z-Function");
        // Split non-root node creating a new child
        bStarTree.add("Van Emde Boas tree");

        for (int i = 0; i < 20; i++) {
            char characterToAppend = (char) ('A' + i);
            bStarTree.add("Key " + characterToAppend);
        }

        StdOut.println("Contains Binary tree: " + bStarTree.contains("Binary tree") + " Expected: true");
        StdOut.println("Contains Red-black tree: " + bStarTree.contains("Red-black tree") + " Expected: true");
        StdOut.println("Contains B-tree: " + bStarTree.contains("B-tree") + " Expected: true");
        StdOut.println("Contains Rene: " + bStarTree.contains("Rene") + " Expected: true");
        StdOut.println("Contains Key M: " + bStarTree.contains("Key M") + " Expected: true");
        StdOut.println("Contains Binary trees: " + bStarTree.contains("Binary trees") + " Expected: false");
        StdOut.println("Contains Binary tre: " + bStarTree.contains("Binary tre") + " Expected: false");
        StdOut.println("Contains Binary : " + bStarTree.contains("Binary ") + " Expected: false");
    }

}
