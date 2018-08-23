package chapter6.btrees;

import chapter3.section5.HashSet;

/**
 * Created by Rene Argento on 23/07/18.
 */
public class BTreeSET<Key extends Comparable<Key>> {

    private PageInterface<Key> root;
    private static final int DEFAULT_MAX_NUMBER_OF_NODES_PER_PAGE = 4;
    private static final boolean DEFAULT_VERBOSE = false;

    private int maxNumberOfNodesPerPage;
    private HashSet<PageInterface> pagesInMemory;
    private boolean verbose;

    public BTreeSET(Key sentinel) {
        this(sentinel, DEFAULT_MAX_NUMBER_OF_NODES_PER_PAGE, DEFAULT_VERBOSE);
    }

    public BTreeSET(Key sentinel, int maxNumberOfNodesPerPage, boolean verbose) {
        if (maxNumberOfNodesPerPage % 2 != 0 || maxNumberOfNodesPerPage == 2) {
            throw new IllegalArgumentException("Max number of nodes must be divisible by 2 and higher than 2");
        }

        pagesInMemory = new HashSet<>();
        root = new Page<>(true, maxNumberOfNodesPerPage, pagesInMemory);

        this.verbose = verbose;
        root.setVerbose(verbose);
        this.maxNumberOfNodesPerPage = maxNumberOfNodesPerPage;

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

            root = new Page<>(false, maxNumberOfNodesPerPage, pagesInMemory);
            root.add(leftHalf);
            root.add(rightHalf);

            root.setVerbose(verbose);
            rightHalf.setVerbose(verbose);
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
            PageInterface<Key> newPage = next.split();
            newPage.setVerbose(verbose);
            page.add(newPage);
        }
        next.close();
    }

}
