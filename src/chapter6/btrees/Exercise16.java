package chapter6.btrees;

import chapter1.section3.Queue;
import chapter3.section1.BinarySearchSymbolTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 25/07/18.
 */
public class Exercise16 {

    private class BinarySearchSTPage<Key extends Comparable<Key>, Value> implements PageSTInterface<Key, Value> {

        private class PageValue {
            private PageSTInterface<Key, Value> childPage;
            private Value value;

            PageValue(PageSTInterface<Key, Value> childPage, Value value) {
                this.childPage = childPage;
                this.value = value;
            }
        }

        private PageSTInterface<Key, Value> parentPage;

        private BinarySearchSymbolTable<Key, PageValue> binarySearchSymbolTable;
        private boolean isOpen;
        private boolean isExternal;
        private int size;
        private boolean containsSentinel;

        // Reference to pages in memory on the system
        private HashSet<PageSTInterface> pagesInMemory;
        private int maxNumberOfNodes;

        BinarySearchSTPage(boolean bottom, int maxNumberOfNodes, HashSet<PageSTInterface> pagesInMemory) {
            if (maxNumberOfNodes % 2 != 0 || maxNumberOfNodes == 2) {
                throw new IllegalArgumentException("Max number of nodes must be divisible by 2 and higher than 2");
            }

            binarySearchSymbolTable = new BinarySearchSymbolTable<>();
            this.pagesInMemory = pagesInMemory;
            this.maxNumberOfNodes = maxNumberOfNodes;
            isExternal = bottom;
            open();
        }

        @Override
        public int pageSize() {
            return binarySearchSymbolTable.size();
        }

        @Override
        public void setKeysSize(int size) {
            this.size = size;
        }

        @Override
        public int keysSize(boolean countSentinel) {
            int totalSize = size;

            if (containsSentinel() && !countSentinel) {
                totalSize--;
            }

            return totalSize;
        }

        @Override
        public void incrementKeysSize() {
            size++;
        }

        @Override
        public void decrementKeysSize() {
            size--;
        }

        @Override
        public boolean isEmpty() {
            return keysSize(false) == 0;
        }

        @Override
        public void open() {
            pagesInMemory.add(this);
            isOpen = true;
        }

        @Override
        public void close(boolean verbose) {
            if (verbose) {
                StringJoiner pageContent = new StringJoiner(" ");

                for (Key key : keys()) {
                    pageContent.add(String.valueOf(key));
                }

                StdOut.println("Page content: " + pageContent.toString());
            }

            pagesInMemory.delete(this);
            isOpen = false;
        }

        @Override
        public boolean isExternal() {
            return isExternal;
        }

        @Override
        public boolean contains(Key key) {
            return binarySearchSymbolTable.contains(key);
        }

        @Override
        public Value get(Key key) {
            if (!isExternal()) {
                throw new IllegalArgumentException("Cannot get keys directly on an internal page");
            }

            PageValue pageValue = binarySearchSymbolTable.get(key);

            if (pageValue == null) {
                return null;
            }

            return pageValue.value;
        }

        @Override
        public PageSTInterface<Key, Value> getParentPage() {
            return parentPage;
        }

        @Override
        public void setParentPage(PageSTInterface<Key, Value> parentPage) {
            this.parentPage = parentPage;
        }

        @Override
        public PageSTInterface<Key, Value> getChildPage(Key key) {
            if (isExternal()) {
                throw new IllegalArgumentException("External pages have no children");
            }

            if (!binarySearchSymbolTable.contains(key)) {
                return null;
            }

            return binarySearchSymbolTable.get(key).childPage;
        }

        @Override
        public void add(Key key, Value value) {
            if (!isExternal()) {
                throw new IllegalArgumentException("Cannot add key directly to an internal page");
            }

            PageValue pageValue = new PageValue(null, value);
            binarySearchSymbolTable.put(key, pageValue);
        }

        @Override
        public void addPage(PageSTInterface<Key, Value> page) {
            Key minKey = ((BinarySearchSTPage<Key, Value>) page).binarySearchSymbolTable.min();

            // If we are adding another page to this page, this means that this is an internal page
            PageValue pageValue = new PageValue(page, null);
            binarySearchSymbolTable.put(minKey, pageValue);
            page.setParentPage(this);
        }

        @Override
        public void addAllKeysFromPage(PageSTInterface<Key, Value> page) {
            BinarySearchSTPage<Key, Value> binarySearchSTPage = (BinarySearchSTPage<Key, Value>) page;

            for (Key key : page.keys()) {
                PageValue pageValue = binarySearchSTPage.binarySearchSymbolTable.get(key);
                binarySearchSymbolTable.put(key, pageValue);

                PageSTInterface<Key, Value> childPage = pageValue.childPage;
                if (childPage != null) {
                    size += childPage.keysSize(true);
                    childPage.setParentPage(this);
                } else {
                    size++;
                }
            }
        }

        @Override
        public boolean containsSentinel() {
            return containsSentinel;
        }

        @Override
        public void setContainsSentinel(boolean containsSentinel) {
            this.containsSentinel = containsSentinel;
        }

        @Override
        public Key min() {
            if (isEmpty()) {
                return null;
            }

            Key minKey;

            if (isExternal() && containsSentinel()) {
                minKey = binarySearchSymbolTable.select(1);
            } else {
                minKey = binarySearchSymbolTable.min();
            }

            return minKey;
        }

        @Override
        public Key max() {
            if (isEmpty()) {
                return null;
            }

            return binarySearchSymbolTable.max();
        }

        @Override
        public Key floor(Key key) {
            return binarySearchSymbolTable.floor(key);
        }

        @Override
        public Key ceiling(Key key) {
            return binarySearchSymbolTable.ceiling(key);
        }

        @Override
        public Key select(int index) {
            if (!isExternal()) {
                throw new IllegalArgumentException("Cannot select directly on an internal page");
            }

            int indexToSearch = index;

            if (containsSentinel()) {
                indexToSearch++;
            }

            return binarySearchSymbolTable.select(indexToSearch);
        }

        @Override
        public int rank(Key key) {
            if (!isExternal()) {
                throw new IllegalArgumentException("Cannot compute rank directly on an internal page");
            }

            int rank = binarySearchSymbolTable.rank(key);

            if (containsSentinel()) {
                rank--;
            }

            return rank;
        }

        @Override
        public void delete(Key key) {
            if (!isExternal()) {
                throw new IllegalArgumentException("Cannot delete directly on an internal page");
            }

            // BTreeST guarantees that the key being deleted is in the page
            size--;
            binarySearchSymbolTable.delete(key);
        }

        @Override
        public void deletePageWithKey(Key key) {
            if (isExternal()) {
                throw new IllegalArgumentException("Delete page operation must be called on an internal page");
            }

            if (!binarySearchSymbolTable.contains(key)) {
                return;
            }

            PageSTInterface<Key, Value> pageToDelete = binarySearchSymbolTable.get(key).childPage;
            size -= pageToDelete.keysSize(true);

            binarySearchSymbolTable.delete(key);
        }

        // Used as part of BTreeST general delete method, which handles page size updates
        @Override
        public void deleteMin() {
            binarySearchSymbolTable.deleteMin();
        }

        // Used as part of BTreeST general delete method, which handles page size updates
        @Override
        public void deleteMax() {
            binarySearchSymbolTable.deleteMax();
        }

        @Override
        public PageSTInterface<Key, Value> next(Key key) {
            if (isExternal()) {
                throw new IllegalArgumentException("Next cannot be called on an external page");
            }

            Key nextKey = binarySearchSymbolTable.floor(key);

            if (nextKey == null) {
                return null;
            }

            PageValue pageValue = binarySearchSymbolTable.get(nextKey);
            return pageValue.childPage;
        }

        @Override
        public boolean isFull() {
            return binarySearchSymbolTable.size() == maxNumberOfNodes;
        }

        @Override
        public PageSTInterface<Key, Value> split() {
            List<Key> keysToMove = new ArrayList<>();
            int middleRank = binarySearchSymbolTable.size() / 2;

            for (int index = middleRank; index < binarySearchSymbolTable.size(); index++) {
                Key keyToMove = binarySearchSymbolTable.select(index);
                keysToMove.add(keyToMove);
            }

            PageSTInterface<Key, Value> newPage = new BinarySearchSTPage<>(isExternal, maxNumberOfNodes, pagesInMemory);
            int keysToMoveSize = 0;

            for (Key key : keysToMove) {
                PageValue pageValue = binarySearchSymbolTable.get(key);
                PageSTInterface<Key, Value> childPage = pageValue.childPage;
                Value value = pageValue.value;

                if (!isExternal()) {
                    keysToMoveSize += childPage.keysSize(true);
                    newPage.addPage(childPage);
                } else {
                    keysToMoveSize++;
                    newPage.add(key, value);
                }

                binarySearchSymbolTable.delete(key);
            }

            // No need to set containsSentinel because right nodes will never have it
            newPage.setKeysSize(keysToMoveSize);
            size -= keysToMoveSize;

            return newPage;
        }

        @Override
        public Iterable<Key> keys() {
            return binarySearchSymbolTable.keys();
        }

        @Override
        public int maxNumberOfNodes() {
            return maxNumberOfNodes;
        }
    }

    private class BTreeST<Key extends Comparable<Key>, Value> {

        private PageSTInterface<Key, Value> root;
        private static final int DEFAULT_MAX_NUMBER_OF_NODES_PER_PAGE = 4;
        private static final boolean DEFAULT_VERBOSE = false;

        private int size;
        private Key sentinel;

        private int maxNumberOfNodesPerPage;
        private HashSet<PageSTInterface> pagesInMemory;
        private boolean verbose;

        public BTreeST(Key sentinel) {
            this(sentinel, DEFAULT_VERBOSE, DEFAULT_MAX_NUMBER_OF_NODES_PER_PAGE);
        }

        public BTreeST(Key sentinel, boolean verbose, int maxNumberOfNodesPerPage) {
            if (maxNumberOfNodesPerPage % 2 != 0 || maxNumberOfNodesPerPage == 2) {
                throw new IllegalArgumentException("Max number of nodes must be divisible by 2 and higher than 2");
            }

            pagesInMemory = new HashSet<>();
            root = new BinarySearchSTPage<>(true, maxNumberOfNodesPerPage, pagesInMemory);

            this.verbose = verbose;
            this.maxNumberOfNodesPerPage = maxNumberOfNodesPerPage;

            add(sentinel, null);
            this.sentinel = sentinel;
            root.setContainsSentinel(true);
        }

        public int size() {
            // Decrease 1 because one of the keys is the sentinel
            return size - 1;
        }

        boolean isEmpty() {
            // Decrease 1 because one of the keys is the sentinel
            return size - 1 == 0;
        }

        public boolean contains(Key key) {
            return contains(root, key);
        }

        private boolean contains(PageSTInterface<Key, Value> page, Key key) {
            if (page.isExternal()) {
                return page.contains(key);
            }

            page.open();

            boolean contains = contains(page.next(key), key);
            page.close(verbose);
            return contains;
        }

        public Value get(Key key) {
            if (key == null) {
                return null;
            }

            return get(root, key);
        }

        private Value get(PageSTInterface<Key, Value> page, Key key) {
            if (page.isExternal()) {
                return page.get(key);
            }

            page.open();

            Value value = get(page.next(key), key);
            page.close(verbose);
            return value;
        }

        public void add(Key key, Value value) {
            boolean isNewKey = !contains(key);

            if (isNewKey) {
                size++;
            }

            add(root, key, value, isNewKey);

            if (root.isFull()) {
                PageSTInterface<Key, Value> leftHalf = root;
                PageSTInterface<Key, Value> rightHalf = root.split();

                root = new BinarySearchSTPage<>(false, maxNumberOfNodesPerPage, pagesInMemory);
                root.addPage(leftHalf);
                root.addPage(rightHalf);

                leftHalf.setParentPage(root);
                rightHalf.setParentPage(root);

                root.setKeysSize(leftHalf.keysSize(true) + rightHalf.keysSize(true));
                root.setContainsSentinel(true);
            }
        }

        private void add(PageSTInterface<Key, Value> page, Key key, Value value, boolean isNewKey) {
            if (isNewKey) {
                page.incrementKeysSize();
            }

            if (page.isExternal()) {
                page.add(key, value);
                return;
            }

            PageSTInterface<Key, Value> next = page.next(key);
            add(next, key, value, isNewKey);

            if (next.isFull()) {
                page.addPage(next.split());
                next.setParentPage(page);
            }
            next.close(verbose);
        }

        public Key min() {
            if (isEmpty()) {
                return null;
            }

            return min(root);
        }

        private Key min(PageSTInterface<Key, Value> page) {
            page.open();

            if (page.isExternal()) {
                return page.min();
            }

            Key minKey = min(page.next(page.min()));
            page.close(verbose);
            return minKey;
        }

        public Key max() {
            if (isEmpty()) {
                return null;
            }

            return max(root);
        }

        private Key max(PageSTInterface<Key, Value> page) {
            page.open();

            if (page.isExternal()) {
                return page.max();
            }

            Key maxKey = max(page.next(page.max()));
            page.close(verbose);
            return maxKey;
        }

        // Returns the highest key in the symbol table smaller than or equal to key.
        public Key floor(Key key) {
            PageSTInterface<Key, Value> pageThatMayContainKey = getPageThatMayContainKey(key);
            pageThatMayContainKey.open();

            Key floorKey = pageThatMayContainKey.floor(key);
            pageThatMayContainKey.close(verbose);

            if (floorKey == sentinel) {
                floorKey = null;
            }

            return floorKey;
        }

        // Returns the smallest key in the symbol table greater than or equal to key.
        public Key ceiling(Key key) {
            PageSTInterface<Key, Value> pageThatMayContainKey = getPageThatMayContainKey(key);
            pageThatMayContainKey.open();

            Key ceilingKey = pageThatMayContainKey.ceiling(key);
            pageThatMayContainKey.close(verbose);

            if (ceilingKey == null) {
                int rank = rank(key);

                if (rank < size()) {
                    ceilingKey = select(rank);
                }
            }

            if (ceilingKey == sentinel) {
                ceilingKey = min();
            }

            return ceilingKey;
        }

        public Key select(int index) {
            if (index < 0 || index >= size()) {
                throw new IllegalArgumentException("Index cannot be negative and must be lower than BTree size");
            }

            return select(root, index);
        }

        private Key select(PageSTInterface<Key, Value> page, int index) {
            if (page.isExternal()) {
                return page.select(index);
            }

            int leftSubtreeSize = 0;

            for (Key key : page.keys()) {
                PageSTInterface<Key, Value> childPage = page.getChildPage(key);
                childPage.open();

                int childPageSize = childPage.keysSize(false);

                if (leftSubtreeSize + childPageSize > index) {
                    Key keySelected = select(childPage, index - leftSubtreeSize);
                    childPage.close(verbose);
                    return keySelected;
                }

                leftSubtreeSize += childPageSize;
                childPage.close(verbose);
            }

            // If the index searched is not higher than or equal to the BTree size (as previously validated),
            // we will never reach here
            throw new IllegalStateException("Select search reached an invalid state");
        }

        public int rank(Key key) {
            return rank(root, key);
        }

        private int rank(PageSTInterface<Key, Value> page, Key searchKey) {
            if (page.isExternal()) {
                return page.rank(searchKey);
            }

            PageSTInterface<Key, Value> nextPage = page.next(searchKey);
            int leftSubtreeSize = 0;

            // Returns the number of keys less than key in the subtree rooted at page
            for (Key key : page.keys()) {
                PageSTInterface<Key, Value> childPage = page.getChildPage(key);
                childPage.open();

                int childPageSize = childPage.keysSize(false);

                if (childPage == nextPage) {
                    int rank = leftSubtreeSize + rank(childPage, searchKey);
                    childPage.close(verbose);
                    return rank;
                }

                leftSubtreeSize += childPageSize;
                childPage.close(verbose);
            }

            // We should never reach here
            throw new IllegalStateException("Rank search reached an invalid state");
        }

        public void delete(Key key) {
            if (key == null) {
                return;
            }

            if (!contains(key)) {
                return;
            }

            size--;
            delete(root, key, 0);

            // If root has only one child, update the root
            if (root.pageSize() == 1 && !root.isExternal()) {
                root = root.getChildPage(root.min());
                root.setParentPage(null);
            }
        }

        private void delete(PageSTInterface<Key, Value> page, Key key, int treeLevel) {
            if (page.isExternal()) {
                page.delete(key);
            } else {
                page.decrementKeysSize();

                PageSTInterface<Key, Value> next = page.next(key);
                next.open();
                delete(next, key, treeLevel + 1);

                Key minKeyInNextPage = next.min();
                boolean wasMergeRequired = false;

                if (next.pageSize() < page.maxNumberOfNodes() / 2) {
                    wasMergeRequired = stabilizePages(next, page, treeLevel + 1, key);
                }

                // Update internal page references if necessary
                // If a merge was not required, next page still exists
                if (!wasMergeRequired) {
                    Key keyToDeleteReference = key;

                    if (!page.contains(key) && next.min() != minKeyInNextPage) {
                        keyToDeleteReference = minKeyInNextPage;
                    }
                    updateParentPageReference(page, next, keyToDeleteReference);
                }

                // Update size - movements and merges may have happened
                int newKeysSize = 0;
                for (Key keyInInternalPage : page.keys()) {
                    PageSTInterface<Key, Value> childPage = page.getChildPage(keyInInternalPage);
                    newKeysSize += childPage.keysSize(true);
                }

                page.setKeysSize(newKeysSize);
                next.close(verbose);
            }
        }

        public void deleteMin() {
            if (isEmpty()) {
                return;
            }

            Key minKey = min();
            delete(minKey);
        }

        public void deleteMax() {
            if (isEmpty()) {
                return;
            }

            Key maxKey = max();
            delete(maxKey);
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

            Queue<Key> keys = new Queue<>();
            keys(root, keys, low, high);
            return keys;
        }

        private void keys(PageSTInterface<Key, Value> page, Queue<Key> keys, Key low, Key high) {
            page.open();

            if (page.isExternal()) {
                for (Key key : page.keys()) {
                    if (key != sentinel
                            && low.compareTo(key) <= 0 && key.compareTo(high) <= 0) {
                        keys.enqueue(key);
                    }
                }

                return;
            }

            for (Key key : page.keys()) {
                PageSTInterface<Key, Value> nextPage = page.getChildPage(key);
                nextPage.open();

                Key nextPageMinKey = nextPage.min();
                Key nextPageMaxKey = nextPage.max();

                if (nextPageMinKey.compareTo(high) <= 0 && nextPageMaxKey.compareTo(low) >= 0) {
                    keys(nextPage, keys, low, high);
                    nextPage.close(verbose);
                }
            }
            page.close(verbose);
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

        public Iterable<Value> get(Key low, Key high) {
            if (low == null)  {
                throw new IllegalArgumentException("First argument to get() cannot be null");
            }
            if (high == null) {
                throw new IllegalArgumentException("Second argument to get() cannot be null");
            }

            Queue<Value> values = new Queue<>();
            get(root, values, low, high);
            return values;
        }

        private void get(PageSTInterface<Key, Value> page, Queue<Value> values, Key low, Key high) {
            page.open();

            if (page.isExternal()) {
                for (Key key : page.keys()) {
                    if (key != sentinel
                            && low.compareTo(key) <= 0 && key.compareTo(high) <= 0) {
                        Value value = page.get(key);
                        values.enqueue(value);
                    }
                }

                return;
            }

            for (Key key : page.keys()) {
                PageSTInterface<Key, Value> nextPage = page.getChildPage(key);
                nextPage.open();

                Key nextPageMinKey = nextPage.min();
                Key nextPageMaxKey = nextPage.max();

                if (nextPageMinKey.compareTo(high) <= 0 && nextPageMaxKey.compareTo(low) >= 0) {
                    get(nextPage, values, low, high);
                    nextPage.close(verbose);
                }
            }
            page.close(verbose);
        }

        private boolean stabilizePages(PageSTInterface<Key, Value> page, PageSTInterface<Key, Value> parentPage,
                                    int treeLevel, Key keyBeingDeleted) {
            boolean pagesStable = false;
            boolean hasLeftSibling = false;
            boolean hasRightSibling = false;
            boolean wasMergeRequired = false;

            PageSTInterface<Key, Value> leftPageParent = null;
            PageSTInterface<Key, Value> leftPage = null;
            PageSTInterface<Key, Value> rightPageParent = null;
            PageSTInterface<Key, Value> rightPage = null;
            Key rightPageMinKey;

            // Case 1: Move key from left page
            Key minKey = page.min();

            if (minKey != null) {
                int minKeyRank = rank(minKey);

                if (minKeyRank > 0) {
                    hasLeftSibling = true;

                    Key leftPageMaxKey = select(minKeyRank - 1);
                    leftPageParent = getPageWithKeyInTreeLevel(leftPageMaxKey, treeLevel - 1);
                    leftPage = leftPageParent.next(leftPageMaxKey);

                    if (leftPage.pageSize() > page.maxNumberOfNodes() / 2) {
                        if (leftPage.isExternal()) {
                            Value leftPageMaxKeyValue = leftPage.get(leftPageMaxKey);
                            leftPage.deleteMax();
                            leftPage.decrementKeysSize();

                            updateParentsSizesAndReference(leftPage, false, null,
                                    -1);

                            page.add(leftPageMaxKey, leftPageMaxKeyValue);
                            page.incrementKeysSize();

                            // Page parents sizes and references will be updated while returning from the recursive delete
                            // method
                        } else {
                            PageSTInterface<Key, Value> leftPageChild = leftPage.next(leftPageMaxKey);
                            leftPage.deleteMax();
                            int leftPageChildKeysSize = leftPageChild.keysSize(true);
                            leftPage.setKeysSize(leftPage.keysSize(true) - leftPageChildKeysSize);

                            updateParentsSizesAndReference(leftPage, false, null,
                                    -leftPageChildKeysSize);

                            page.addPage(leftPageChild);
                            page.setKeysSize(page.keysSize(true) + leftPageChildKeysSize);

                            // Page parents sizes and references will be updated while returning from the recursive delete
                            // method
                        }

                        pagesStable = true;
                    }
                }
            }

            // Case 2: Move key from right page
            if (!pagesStable) {
                Key maxKey = max(page);

                int maxKeyRank;

                if (maxKey != null) {
                    maxKeyRank = rank(maxKey);
                } else {
                    // Edge case in which we only have a page with one key: the sentinel
                    maxKeyRank = -1;
                }

                if (maxKeyRank < size() - 1) {
                    hasRightSibling = true;

                    rightPageMinKey = select(maxKeyRank + 1);
                    rightPageParent = getPageWithKeyInTreeLevel(rightPageMinKey, treeLevel - 1);
                    rightPage = rightPageParent.next(rightPageMinKey);

                    if (rightPage.pageSize() > page.maxNumberOfNodes() / 2) {
                        if (rightPage.isExternal()) {
                            Value rightPageMinKeyValue = rightPage.get(rightPageMinKey);
                            rightPage.deleteMin();
                            rightPage.decrementKeysSize();

                            updateParentsSizesAndReference(rightPage, true, rightPageMinKey,
                                    -1);

                            page.add(rightPageMinKey, rightPageMinKeyValue);
                            page.incrementKeysSize();

                            // Page parents sizes will be updated while returning from the recursive delete method
                        } else {
                            PageSTInterface<Key, Value> rightPageChild = rightPage.next(rightPageMinKey);
                            rightPage.deleteMin();
                            int rightPageChildKeysSize = rightPageChild.keysSize(true);
                            rightPage.setKeysSize(rightPage.keysSize(true) - rightPageChildKeysSize);

                            updateParentsSizesAndReference(rightPage, true, rightPageMinKey,
                                    -rightPageChildKeysSize);

                            page.addPage(rightPageChild);
                            page.setKeysSize(page.keysSize(true) + rightPageChildKeysSize);

                            // Page parents sizes will be updated while returning from the recursive delete method
                        }

                        pagesStable = true;
                    }
                }
            }

            // Case 3: Merge to left page
            if (!pagesStable && hasLeftSibling) {
                merge(page, parentPage, leftPage, false, keyBeingDeleted);
                wasMergeRequired = true;
                pagesStable = true;
            }

            // Case 4: Merge to right page
            if (!pagesStable && hasRightSibling) {
                merge(page, parentPage, rightPage, true, keyBeingDeleted);
                wasMergeRequired = true;
            }

            // Close pages
            if (leftPageParent != null) {
                leftPageParent.close(verbose);
            }
            if (leftPage != null) {
                leftPage.close(verbose);
            }
            if (rightPageParent != null) {
                rightPageParent.close(verbose);
            }
            if (rightPage != null) {
                rightPage.close(verbose);
            }

            return wasMergeRequired;
        }

        private void merge(PageSTInterface<Key, Value> page, PageSTInterface<Key, Value> parentPage,
                           PageSTInterface<Key, Value> siblingPage, boolean updateReference, Key keyToDelete) {
            // With the guarantee that both the current page and the sibling page contain less than
            // MAX_NUMBER_OF_NODES / 2 keys, no split will happen during additions

            // Page parents sizes and references will be updated while returning from the recursive delete
            // method
            int sizeToUpdateInSiblingParentPages = 0;
            Key minKeyInSiblingParentPage = null;

            if (updateReference) {
                minKeyInSiblingParentPage = siblingPage.min();
            }

            if (page.isExternal()) {
                for (Key key : page.keys()) {
                    Value value = page.get(key);
                    siblingPage.add(key, value);
                    siblingPage.incrementKeysSize();
                    sizeToUpdateInSiblingParentPages++;
                }
            } else {
                siblingPage.addAllKeysFromPage(page);
                sizeToUpdateInSiblingParentPages = page.keysSize(true);
            }

            if (!parentPage.contains(keyToDelete)) {
                if (!page.containsSentinel()) {
                    keyToDelete = page.min();
                } else {
                    keyToDelete = sentinel;
                }
            }
            parentPage.deletePageWithKey(keyToDelete);

            updateParentsSizesAndReference(siblingPage, updateReference, minKeyInSiblingParentPage,
                    sizeToUpdateInSiblingParentPages);

            if (page.containsSentinel()) {
                siblingPage.setContainsSentinel(true);
            }
        }

        private void updateParentPageReference(PageSTInterface<Key, Value> parentPage, PageSTInterface<Key, Value> page,
                                               Key currentMinKey) {
            parentPage.deletePageWithKey(currentMinKey);
            parentPage.addPage(page);
            parentPage.setKeysSize(parentPage.keysSize(true) + page.keysSize(true));
        }

        private void updateParentsSizesAndReference(PageSTInterface<Key, Value> page, boolean updateReference,
                                                    Key keyOfPageToDelete, int sizeToUpdate) {
            PageSTInterface<Key, Value> currentPage = page;
            PageSTInterface<Key, Value> currentParent = page.getParentPage();

            while (currentParent != null) {
                int currentParentKeysSize = currentParent.keysSize(true);
                int updatedKeysSize = currentParentKeysSize + sizeToUpdate;

                if (updateReference && currentParent.contains(keyOfPageToDelete)) {
                    currentParent.deletePageWithKey(keyOfPageToDelete);
                    currentParent.addPage(currentPage);
                }

                currentParent.setKeysSize(updatedKeysSize);

                currentPage = currentParent;
                currentParent = currentParent.getParentPage();
            }
        }

        private PageSTInterface<Key, Value> getParentPageOfPageWithKey(Key key) {
            if (root.isExternal()) {
                return null;
            }

            return getParentPageOfPageWithKey(root, key);
        }

        private PageSTInterface<Key, Value> getParentPageOfPageWithKey(PageSTInterface<Key, Value> page, Key key) {
            PageSTInterface<Key, Value> nextPage = page.next(key);
            nextPage.open();

            if (nextPage.isExternal()) {
                nextPage.close(verbose);
                return page;
            } else {
                page.close(verbose);
            }

            return getParentPageOfPageWithKey(nextPage, key);
        }

        private PageSTInterface<Key, Value> getPageThatMayContainKey(Key key) {
            PageSTInterface<Key, Value> parent = getParentPageOfPageWithKey(key);

            if (parent == null) {
                return root;
            }

            return parent.next(key);
        }

        private PageSTInterface<Key, Value> getPageWithKeyInTreeLevel(Key key, int treeLevel) {
            if (treeLevel < 0) {
                throw new IllegalArgumentException("Tree level cannot be negative");
            }

            return getPageWithKeyInTreeLevel(root, key, treeLevel, 0);
        }

        private PageSTInterface<Key, Value> getPageWithKeyInTreeLevel(PageSTInterface<Key, Value> page, Key key,
                                                                      int treeLevel, int currentTreeLevel) {
            page.open();

            if (currentTreeLevel == treeLevel) {
                return page;
            }

            if (page.isExternal()) {
                throw new IllegalArgumentException("Tree only has " + currentTreeLevel + "levels.");
            }

            PageSTInterface<Key, Value> nextPage = page.next(key);
            page.close(verbose);

            PageSTInterface<Key, Value> targetPage =
                    getPageWithKeyInTreeLevel(nextPage, key, treeLevel, currentTreeLevel + 1);
            nextPage.close(verbose);
            return targetPage;
        }
    }

    public static void main(String[] args) {
        Exercise16 exercise16 = new Exercise16();
        BTreeST<String, Integer> bTreeST = exercise16.new BTreeST<>("*");

        // Test add()
        bTreeST.add("Rene", 0);
        bTreeST.add("Sedgewick", 1);
        bTreeST.add("Wayne", 2);
        bTreeST.add("B-tree", 3);
        bTreeST.add("Binary tree", 4);
        bTreeST.add("Red-black tree", 5);
        bTreeST.add("AVL tree", 6);
        bTreeST.add("Segment tree", 7);
        bTreeST.add("Fenwick tree", 8);

        // Test size()
        StdOut.println("*** Size tests ***");
        StdOut.println("BTreeST size: " + bTreeST.size() + " Expected: 9");

        // Test size() with range
        StdOut.println("\n*** Size with range tests ***");

        int sizeWithRange1 = bTreeST.size("Red-black tree", "Segment tree");
        StdOut.println("BTreeST size between Red-black tree and Segment tree: " + sizeWithRange1);
        StdOut.println("Expected: 4");

        int sizeWithRange2 = bTreeST.size("A*", "B-tree");
        StdOut.println("BTreeST size between A* and B-tree: " + sizeWithRange2);
        StdOut.println("Expected: 2");

        int sizeWithRange3 = bTreeST.size("Rene", "Z-Function");
        StdOut.println("BTreeST size between Rene and Z-Function: " + sizeWithRange3);
        StdOut.println("Expected: 4");

        // Test keys()
        StdOut.println("\n*** Keys tests ***");
        StringJoiner allKeys = new StringJoiner(" - ");
        for (String key : bTreeST.keys()) {
            allKeys.add(key);
        }
        StdOut.println("          " + allKeys.toString());
        StdOut.println("Expected: AVL tree - B-tree - Binary tree - Fenwick tree - Red-black tree - Rene - " +
                "Sedgewick - Segment tree - Wayne");

        // Test keys() with range
        StdOut.println("\n*** Keys with range tests ***");

        StdOut.println("Keys between Red-black tree and Segment tree:");
        StringJoiner keysWithRange1 = new StringJoiner(" - ");
        for (String key : bTreeST.keys("Red-black tree", "Segment tree")) {
            keysWithRange1.add(key);
        }
        StdOut.println("          " + keysWithRange1.toString());
        StdOut.println("Expected: Red-black tree - Rene - Sedgewick - Segment tree");

        StdOut.println("\nKeys between A* and B-tree");
        StringJoiner keysWithRange2 = new StringJoiner(" - ");
        for (String key : bTreeST.keys("A*", "B-tree")) {
            keysWithRange2.add(key);
        }
        StdOut.println("          " + keysWithRange2.toString());
        StdOut.println("Expected: AVL tree - B-tree");

        StdOut.println("\nKeys between Rene and Z-Function");
        StringJoiner keysWithRange3 = new StringJoiner(" - ");
        for (String key : bTreeST.keys("Rene", "Z-Function")) {
            keysWithRange3.add(key);
        }
        StdOut.println("          " + keysWithRange3.toString());
        StdOut.println("Expected: Rene - Sedgewick - Segment tree - Wayne");

        // Test contains()
        StdOut.println("\n*** Contains tests ***");
        StdOut.println("Contains Binary tree: " + bTreeST.contains("Binary tree") + " Expected: true");
        StdOut.println("Contains Red-black tree: " + bTreeST.contains("Red-black tree") + " Expected: true");
        StdOut.println("Contains B-tree: " + bTreeST.contains("B-tree") + " Expected: true");
        StdOut.println("Contains Rene: " + bTreeST.contains("Rene") + " Expected: true");
        StdOut.println("Contains Binary trees: " + bTreeST.contains("Binary trees") + " Expected: false");
        StdOut.println("Contains Binary tre: " + bTreeST.contains("Binary tre") + " Expected: false");
        StdOut.println("Contains Binary : " + bTreeST.contains("Binary ") + " Expected: false");

        // Test get()
        StdOut.println("\n*** Get tests ***");
        StdOut.println("Get Rene: " + bTreeST.get("Rene") + " Expected: 0");
        StdOut.println("Get Fenwick tree: " + bTreeST.get("Fenwick tree") + " Expected: 8");
        StdOut.println("Get Wayne: " + bTreeST.get("Wayne") + " Expected: 2");
        StdOut.println("Get AVL tree: " + bTreeST.get("AVL tree") + " Expected: 6");
        StdOut.println("Get Splay tree: " + bTreeST.get("Splay tree") + " Expected: null");
        StdOut.println("Get AAA: " + bTreeST.get("AAA") + " Expected: null");
        StdOut.println("Get ZZZ: " + bTreeST.get("ZZZ") + " Expected: null");

        StdOut.println("\n*** Get with range tests ***");

        StdOut.println("Get values for keys between Red-black tree and Segment tree:");
        StringJoiner getWithRange1 = new StringJoiner(" - ");
        for (int value : bTreeST.get("Red-black tree", "Segment tree")) {
            getWithRange1.add(String.valueOf(value));
        }
        StdOut.println("          " + getWithRange1.toString());
        StdOut.println("Expected: 5 - 0 - 1 - 7");

        StdOut.println("\nGet values for keys between A* and B-tree");
        StringJoiner getWithRange2 = new StringJoiner(" - ");
        for (int value : bTreeST.get("A*", "B-tree")) {
            getWithRange2.add(String.valueOf(value));
        }
        StdOut.println("          " + getWithRange2.toString());
        StdOut.println("Expected: 6 - 3");

        StdOut.println("\nGet values for keys between Rene and Z-Function");
        StringJoiner getWithRange3 = new StringJoiner(" - ");
        for (int value : bTreeST.get("Rene", "Z-Function")) {
            getWithRange3.add(String.valueOf(value));
        }
        StdOut.println("          " + getWithRange3.toString());
        StdOut.println("Expected: 0 - 1 - 7 - 2");

        // Test min()
        StdOut.println("\n*** Min and max tests ***");
        StdOut.println("Min: " + bTreeST.min() + " Expected: AVL tree");
        StdOut.println("Max: " + bTreeST.max() + " Expected: Wayne");

        // Test floor()
        StdOut.println("\n*** Floor tests ***");
        StdOut.println("Floor AVL tree: " + bTreeST.floor("AVL tree") + " Expected: AVL tree");
        StdOut.println("Floor B-tree: " + bTreeST.floor("B-tree") + " Expected: B-tree");
        StdOut.println("Floor Red-black tree: " + bTreeST.floor("Red-black tree") + " Expected: Red-black tree");
        StdOut.println("Floor Wayne: " + bTreeST.floor("Wayne") + " Expected: Wayne");
        StdOut.println("Floor AAA: " + bTreeST.floor("AAA") + " Expected: null");
        StdOut.println("Floor Binary zebra: " + bTreeST.floor("Binary zebra") + " Expected: Binary tree");
        StdOut.println("Floor Ball: " + bTreeST.floor("Ball") + " Expected: B-tree");
        StdOut.println("Floor ZZZ: " + bTreeST.floor("ZZZ") + " Expected: Wayne");

        // Test ceiling()
        StdOut.println("\n*** Ceiling tests ***");
        StdOut.println("Ceiling AVL tree: " + bTreeST.ceiling("AVL tree") + " Expected: AVL tree");
        StdOut.println("Ceiling B-tree: " + bTreeST.ceiling("B-tree") + " Expected: B-tree");
        StdOut.println("Ceiling Red-black tree: " + bTreeST.ceiling("Red-black tree") + " Expected: Red-black tree");
        StdOut.println("Ceiling Wayne: " + bTreeST.ceiling("Wayne") + " Expected: Wayne");
        StdOut.println("Ceiling AAA: " + bTreeST.ceiling("AAA") + " Expected: AVL tree");
        StdOut.println("Ceiling Binary zebra: " + bTreeST.ceiling("Binary zebra") + " Expected: Fenwick tree");
        StdOut.println("Ceiling Quicksort: " + bTreeST.ceiling("Quicksort") + " Expected: Red-black tree");
        StdOut.println("Ceiling ZZZ: " + bTreeST.ceiling("ZZZ") + " Expected: null");

        // Test select()
        StdOut.println("\n*** Select tests ***");
        StdOut.println("Select 0: " + bTreeST.select(0) + " Expected: AVL tree");
        StdOut.println("Select 1: " + bTreeST.select(1) + " Expected: B-tree");
        StdOut.println("Select 2: " + bTreeST.select(2) + " Expected: Binary tree");
        StdOut.println("Select 3: " + bTreeST.select(3) + " Expected: Fenwick tree");
        StdOut.println("Select 4: " + bTreeST.select(4) + " Expected: Red-black tree");
        StdOut.println("Select 5: " + bTreeST.select(5) + " Expected: Rene");
        StdOut.println("Select 6: " + bTreeST.select(6) + " Expected: Sedgewick");
        StdOut.println("Select 7: " + bTreeST.select(7) + " Expected: Segment tree");
        StdOut.println("Select 8: " + bTreeST.select(8) + " Expected: Wayne");

        // Test rank()
        StdOut.println("\n*** Rank tests ***");
        StdOut.println("Rank AVL tree: " + bTreeST.rank("AVL tree") + " Expected: 0");
        StdOut.println("Rank B-tree: " + bTreeST.rank("B-tree") + " Expected: 1");
        StdOut.println("Rank Binary tree: " + bTreeST.rank("Binary tree") + " Expected: 2");
        StdOut.println("Rank Fenwick tree: " + bTreeST.rank("Fenwick tree") + " Expected: 3");
        StdOut.println("Rank Red-black tree: " + bTreeST.rank("Red-black tree") + " Expected: 4");
        StdOut.println("Rank Rene: " + bTreeST.rank("Rene") + " Expected: 5");
        StdOut.println("Rank Sedgewick: " + bTreeST.rank("Sedgewick") + " Expected: 6");
        StdOut.println("Rank Segment tree: " + bTreeST.rank("Segment tree") + " Expected: 7");
        StdOut.println("Rank Wayne: " + bTreeST.rank("Wayne") + " Expected: 8");
        StdOut.println("Rank AAA: " + bTreeST.rank("AAA") + " Expected: 0");
        StdOut.println("Rank ZZZ: " + bTreeST.rank("ZZZ") + " Expected: 9");

        // Test delete()
        StdOut.println("\n*** Delete tests ***");

        StdOut.println("Delete B-tree");
        bTreeST.delete("B-tree");
        exercise16.printKeys(bTreeST);
        StdOut.println("Expected: AVL tree - Binary tree - Fenwick tree - Red-black tree - Rene - " +
                "Sedgewick - Segment tree - Wayne");
        StdOut.println("Size: " + bTreeST.size() + " Expected: 8");

        StdOut.println("\nDelete Red-black tree");
        bTreeST.delete("Red-black tree");
        exercise16.printKeys(bTreeST);
        StdOut.println("Expected: AVL tree - Binary tree - Fenwick tree - Rene - Sedgewick - Segment tree - Wayne");
        StdOut.println("Size: " + bTreeST.size() + " Expected: 7");

        StdOut.println("\nDelete Segment tree");
        bTreeST.delete("Segment tree");
        exercise16.printKeys(bTreeST);
        StdOut.println("Expected: AVL tree - Binary tree - Fenwick tree - Rene - Sedgewick - Wayne");
        StdOut.println("Size: " + bTreeST.size() + " Expected: 6");

        StdOut.println("\nDelete Wayne");
        bTreeST.delete("Wayne");
        exercise16.printKeys(bTreeST);
        StdOut.println("Expected: AVL tree - Binary tree - Fenwick tree - Rene - Sedgewick");
        StdOut.println("Size: " + bTreeST.size() + " Expected: 5");

        StdOut.println("\nDelete Binary tree");
        bTreeST.delete("Binary tree");
        exercise16.printKeys(bTreeST);
        StdOut.println("Expected: AVL tree - Fenwick tree - Rene - Sedgewick");
        StdOut.println("Size: " + bTreeST.size() + " Expected: 4");

        // Test deleteMin()
        StdOut.println("\n*** Delete min tests ***");
        StdOut.println("\nDelete min 1");
        bTreeST.deleteMin();
        exercise16.printKeys(bTreeST);
        StdOut.println("Expected: Fenwick tree - Rene - Sedgewick");
        StdOut.println("Size: " + bTreeST.size() + " Expected: 3");

        StdOut.println("\nDelete min 2");
        bTreeST.deleteMin();
        exercise16.printKeys(bTreeST);
        StdOut.println("Expected: Rene - Sedgewick");
        StdOut.println("Size: " + bTreeST.size() + " Expected: 2");

        // Test deleteMax()
        StdOut.println("\n*** Delete max tests ***");
        StdOut.println("\nDelete max 1");
        bTreeST.deleteMax();
        exercise16.printKeys(bTreeST);
        StdOut.println("Expected: Rene");
        StdOut.println("Size: " + bTreeST.size() + " Expected: 1");

        StdOut.println("\nDelete max 2");
        bTreeST.deleteMax();
        if (!bTreeST.isEmpty()) {
            exercise16.printKeys(bTreeST);
        } else {
            StdOut.println(" ");
        }
        StdOut.println("Expected: ");
        StdOut.println("Size: " + bTreeST.size() + " Expected: 0");

        StdOut.println("IsEmpty: " + bTreeST.isEmpty() + " Expected: true");
    }

    private void printKeys(BTreeST<String, Integer> bTreeST) {
        StringJoiner allKeys = new StringJoiner(" - ");
        for (String key : bTreeST.keys()) {
            allKeys.add(key);
        }
        StdOut.println("          " + allKeys.toString());
    }

}
