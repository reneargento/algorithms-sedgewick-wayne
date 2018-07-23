package chapter6;

/**
 * Created by Rene Argento on 23/07/18.
 */
@SuppressWarnings("unchecked")
public class BTreeSET<Key extends Comparable<Key>> {

    private PageInterface<Key> root = new Page<>(true);

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
            PageInterface leftHalf = root;
            PageInterface rightHalf = root.split();
            root = new Page(false);
            root.add(leftHalf);
            root.add(rightHalf);
        }
    }

    public void add(PageInterface page, Key key) {
        if (page.isExternal()) {
            page.add(key);
            return;
        }

        PageInterface next = page.next(key);
        add(next, key);

        if (next.isFull()) {
            page.add(next.split());
        }
        next.close();
    }

}
