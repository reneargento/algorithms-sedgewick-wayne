package chapter6.btrees;

import chapter1.section3.Queue;
import chapter3.section5.HashSet;

/**
 * Created by Rene Argento on 01/08/18.
 */

public class BTreeST<Key extends Comparable<Key>, Value> {

    private HashSet<PageSTInterface> pagesInMemory = new HashSet<>();
    private PageSTInterface<Key, Value> root = new BinarySearchSTPage<>(true, pagesInMemory);
    private int size;
    private Key sentinel;

    public BTreeST(Key sentinel) {
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
        page.close(false);
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
        page.close(false);
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

            root = new BinarySearchSTPage<>(false, pagesInMemory);
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
        next.close(false);
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
        page.close(false);
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
        page.close(false);
        return maxKey;
    }

    // Returns the highest key in the symbol table smaller than or equal to key.
    public Key floor(Key key) {
        PageSTInterface<Key, Value> pageThatMayContainKey = getPageThatMayContainKey(key);
        pageThatMayContainKey.open();

        Key floorKey = pageThatMayContainKey.floor(key);
        pageThatMayContainKey.close(false);

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
        pageThatMayContainKey.close(false);

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
                childPage.close(false);
                return keySelected;
            }

            leftSubtreeSize += childPageSize;
            childPage.close(false);
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
                childPage.close(false);
                return rank;
            }

            leftSubtreeSize += childPageSize;
            childPage.close(false);
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
            next.close(false);
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
                nextPage.close(false);
            }
        }
        page.close(false);
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
                nextPage.close(false);
            }
        }
        page.close(false);
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
            leftPageParent.close(false);
        }
        if (leftPage != null) {
            leftPage.close(false);
        }
        if (rightPageParent != null) {
            rightPageParent.close(false);
        }
        if (rightPage != null) {
            rightPage.close(false);
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
            nextPage.close(false);
            return page;
        } else {
            page.close(false);
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
        page.close(false);

        PageSTInterface<Key, Value> targetPage =
                getPageWithKeyInTreeLevel(nextPage, key, treeLevel, currentTreeLevel + 1);
        nextPage.close(false);
        return targetPage;
    }
}
