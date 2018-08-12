package chapter6;

import chapter3.section5.HashSet;

/**
 * Created by Rene Argento on 23/07/18.
 */
public class BTreeSET<Key extends Comparable<Key>> {

    // By convention MAX_NUMBER_OF_NODES is always an even number >= 4
    private static final int MAX_NUMBER_OF_NODES = 4;
    private HashSet<PageInterface> pagesInMemory = new HashSet<>();

    private PageInterface<Key> root = new Page<>(true, MAX_NUMBER_OF_NODES, pagesInMemory);

    public BTreeSET(Key sentinel) {
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

            root = new Page<>(false, MAX_NUMBER_OF_NODES, pagesInMemory);
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
