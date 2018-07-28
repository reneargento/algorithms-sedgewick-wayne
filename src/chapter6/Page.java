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
@SuppressWarnings("unchecked")
public class Page<Key extends Comparable<Key>> implements PageInterface<Key> {

    private BinarySearchSymbolTable<Key, PageInterface> binarySearchSymbolTable;
    private boolean isOpen;
    private boolean isExternal;

    // Reference to pages in memory on the system
    private HashSet<PageInterface> pagesInMemory;
    private int maxNumberOfNodes;

    Page(boolean bottom, int maxNumberOfNodes, HashSet<PageInterface> pagesInMemory) {
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
    public void add(Key key) {
        if (!isExternal()) {
            throw new IllegalArgumentException("Cannot add key directly to an internal page");
        }

        // Since binary search symbol tables do not allow null values, we make the external nodes point to themselves
        binarySearchSymbolTable.put(key, this);
    }

    @Override
    public void add(PageInterface page) {
        Key minKey = (Key) ((Page) page).binarySearchSymbolTable.min();
        binarySearchSymbolTable.put(minKey, page);
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
    public PageInterface next(Key key) {
        if (isExternal()) {
            throw new IllegalArgumentException("Next cannot be called on an external page");
        }

        Key nextKey = binarySearchSymbolTable.floor(key);
        return binarySearchSymbolTable.get(nextKey);
    }

    @Override
    public boolean isFull() {
        return binarySearchSymbolTable.size() == maxNumberOfNodes;
    }

    @Override
    public PageInterface split() {
        List<Key> keysToMove = new ArrayList<>();
        int middleRank = binarySearchSymbolTable.size() / 2;

        for (int index = middleRank; index < binarySearchSymbolTable.size(); index++) {
            Key keyToMove = binarySearchSymbolTable.select(index);
            keysToMove.add(keyToMove);
        }

        PageInterface<Key> newPage = new Page<>(isExternal, maxNumberOfNodes, pagesInMemory);

        for (Key key : keysToMove) {
            PageInterface pageLink = binarySearchSymbolTable.get(key);
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
}
