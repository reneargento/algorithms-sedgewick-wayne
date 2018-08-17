package chapter6;

import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 15/08/18.
 */
public class Exercise21 {

    public class BTreeSETWithExternalPageCounter<Key extends Comparable<Key>> {

        private HashSet<PageInterface> pagesInMemory = new HashSet<>();

        private PageInterface<Key> root;
        private int maxNumberOfNodes;
        private int numberOfExternalNodes;

        public BTreeSETWithExternalPageCounter(Key sentinel, int maxNumberOfNodes) {
            this.maxNumberOfNodes = maxNumberOfNodes;
            root = new Page<>(true, maxNumberOfNodes, pagesInMemory);
            numberOfExternalNodes = 1;
            root.setVerbose(false);
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
                if (root.isExternal()) {
                    numberOfExternalNodes++;
                }

                PageInterface<Key> leftHalf = root;
                PageInterface<Key> rightHalf = root.split();

                root = new Page<>(false, maxNumberOfNodes, pagesInMemory);
                root.add(leftHalf);
                root.add(rightHalf);
                root.setVerbose(false);
                rightHalf.setVerbose(false);
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
                newPage.setVerbose(false);
                page.add(newPage);

                if (newPage.isExternal()) {
                    numberOfExternalNodes++;
                }
            }
            next.close();
        }

        public int getNumberOfExternalNodes() {
            return numberOfExternalNodes;
        }
    }

    private void doExperiment(int numberOfExperimentsPerConfiguration) {

        int[] orderValues = {4, 16, 64, 256};
        int[] numberOfItemValues = {100000, 1000000, 10000000};

        StdOut.printf("%11s %17s %27s\n", "Order M | ", "Number of items | ", "AVG Number of External Pages");

        for(int order : orderValues) {
            for(int numberOfItems : numberOfItemValues) {
                int totalNumberOfExternalNodes = 0;

                for (int experiment = 0; experiment < numberOfExperimentsPerConfiguration; experiment++) {
                    BTreeSETWithExternalPageCounter<Integer> bTreeSet =
                            new BTreeSETWithExternalPageCounter<>(0, order);

                    for (int i = 0; i < numberOfItems; i++) {
                        int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
                        bTreeSet.add(randomKey);
                    }

                    totalNumberOfExternalNodes += bTreeSet.getNumberOfExternalNodes();
                }

                int averageNumberOfExternalNodes = (int) Math.round(totalNumberOfExternalNodes /
                        (double) numberOfExperimentsPerConfiguration);
                printResults(order, numberOfItems, averageNumberOfExternalNodes);
            }
        }
    }

    private void printResults(int order, int numberOfItems, int averageNumberOfExternalNodes) {
        StdOut.printf("%8s %18d %31d\n", order, numberOfItems, averageNumberOfExternalNodes);
    }

    // Parameters example: 5
    public static void main(String[] args) {
        int numberOfExperimentsPerConfiguration = Integer.parseInt(args[0]);
        new Exercise21().doExperiment(numberOfExperimentsPerConfiguration);
    }

}
