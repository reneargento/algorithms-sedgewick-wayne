package chapter6.btrees;

import chapter1.section3.Queue;
import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 02/08/18.
 */
public class Exercise17 {

    private class BTreeSETDrawing<Key extends Comparable<Key>> {

        private class PageToDraw {
            PageInterface<Key> page;
            int treeLevel;
            int parentKeyIndex;

            PageToDraw(PageInterface<Key> page, int treeLevel, int parentKeyIndex) {
                this.page = page;
                this.treeLevel = treeLevel;
                this.parentKeyIndex = parentKeyIndex;
            }
        }

        private class Cell {
            private double x;
            private double y;

            Cell(double x, double y) {
                this.x = x;
                this.y = y;
            }
        }

        private static final double KEY_CELL_DIMENSION = 0.06;
        private static final double KEY_HALF_CELL_DIMENSION = KEY_CELL_DIMENSION / 2;
        private static final double HORIZONTAL_SPACE_TO_MOVE_PER_KEY = KEY_HALF_CELL_DIMENSION;

        private static final double CENTER_X_POSITION = 1.25;
        private static final double VERTICAL_SPACE_TO_ADJUST_FOR_INTERNAL_PAGES_DIVIDER = 0.005;
        private static final double VERTICAL_SPACE_BETWEEN_TREE_LEVELS = 0.15;
        private static final int NO_PARENT = -1;

        private void drawBTree(BTreeSETDrawable<Key> bTreeSETDrawable) {
            if (bTreeSETDrawable == null) {
                return;
            }

            StdDraw.clear();
            int maxNumberOfNodes = bTreeSETDrawable.getRoot().getMaxNumberOfNodes();

            List<PageToDraw> pagesToDraw = getPagesToDraw(bTreeSETDrawable, maxNumberOfNodes);
            drawAllPages(pagesToDraw, maxNumberOfNodes);

            StdDraw.pause(1000);
            StdDraw.show();
        }

        private List<PageToDraw> getPagesToDraw(BTreeSETDrawable<Key> bTreeSET, int maxNumberOfNodes) {
            List<PageToDraw> pagesToDraw = new ArrayList<>();
            int pageId = 0;
            int keyId;

            Queue<PageToDraw> pagesQueue = new Queue<>();
            pagesQueue.enqueue(new PageToDraw(bTreeSET.getRoot(), 0, NO_PARENT));

            while (!pagesQueue.isEmpty()) {
                PageToDraw currentPage = pagesQueue.dequeue();
                PageInterface<Key> page = currentPage.page;
                keyId = 0;

                pagesToDraw.add(currentPage);

                if (page.isExternal()) {
                    continue;
                }

                for (Key key : page.keys()) {
                    int parentCellId = getCellId(pageId, keyId, maxNumberOfNodes);
                    pagesQueue.enqueue(new PageToDraw(page.getPage(key), currentPage.treeLevel + 1, parentCellId));

                    keyId++;
                }
                pageId++;
            }

            return pagesToDraw;
        }

        private void drawAllPages(List<PageToDraw> pagesToDraw, int maxNumberOfNodes) {
            SeparateChainingHashTable<Integer, Cell> keyIdToCellPositionMap = new SeparateChainingHashTable<>();
            int pageId = 0;

            double rectangleWidth = maxNumberOfNodes * KEY_CELL_DIMENSION;
            // Margin adjustment to draw the cells correctly for any valid number of nodes per page
            int horizontalMarginMultiplier = ((maxNumberOfNodes - 4) / 2) + 1;

            double currentYPosition = 0.7;
            int currentTreeLevel = 0;
            int pagesInNextLevel = 1;

            while (pageId < pagesToDraw.size()) {

                int nextTreeLevel = pagesToDraw.get(pageId).treeLevel;

                if (nextTreeLevel != currentTreeLevel) {
                    currentYPosition -= VERTICAL_SPACE_BETWEEN_TREE_LEVELS;
                    currentTreeLevel = nextTreeLevel;
                    continue;
                }

                int pagesInCurrentLevel = pagesInNextLevel;
                pagesInNextLevel = 0;
                double currentXPosition = CENTER_X_POSITION -
                        (pagesInCurrentLevel * maxNumberOfNodes * HORIZONTAL_SPACE_TO_MOVE_PER_KEY)
                        + KEY_HALF_CELL_DIMENSION;

                for (int page = 0; page < pagesInCurrentLevel; page++) {
                    List<Key> keys = new ArrayList<>();
                    PageToDraw currentPageToDraw = pagesToDraw.get(pageId);

                    for (Key key : currentPageToDraw.page.keys()) {
                        keys.add(key);
                    }

                    int pageSize = keys.size();
                    pagesInNextLevel += pageSize;

                    if (page > 0) {
                        currentXPosition += rectangleWidth / 2;
                    }

                    StdDraw.rectangle(currentXPosition, currentYPosition, rectangleWidth / 2,
                            KEY_HALF_CELL_DIMENSION);

                    double separatorXPosition = currentXPosition - horizontalMarginMultiplier * KEY_CELL_DIMENSION;
                    double separatorYUpperPosition = currentYPosition + KEY_HALF_CELL_DIMENSION;

                    for (int keyId = 0; keyId < maxNumberOfNodes; keyId++) {

                        drawPage(currentPageToDraw, keyIdToCellPositionMap, currentYPosition, separatorXPosition,
                                separatorYUpperPosition, keys, pageId, keyId, pageSize, maxNumberOfNodes);

                        separatorXPosition += KEY_CELL_DIMENSION;
                    }

                    currentXPosition = separatorXPosition;
                    pageId++;
                }
            }
        }

        private void drawPage(PageToDraw pageToDraw, SeparateChainingHashTable<Integer, Cell> keyIdToCellPositionMap,
                              double currentYPosition, double separatorXPosition, double separatorYUpperPosition,
                              List<Key> keys, int pageId, int keyId, int pageSize, int maxNumberOfNodes) {
            double keyInitialXPosition = separatorXPosition - KEY_CELL_DIMENSION;
            double keyFinalXPosition = keyInitialXPosition + KEY_CELL_DIMENSION;
            boolean isPageInternal = !pageToDraw.page.isExternal();

            drawSeparators(separatorXPosition, currentYPosition, separatorYUpperPosition);

            if (isPageInternal) {
                drawInternalHorizontalLine(keyInitialXPosition, currentYPosition, keyFinalXPosition);
            }

            double keyXPosition;

            if (keyId < pageSize) {
                keyXPosition = separatorXPosition - KEY_HALF_CELL_DIMENSION;

                drawKey(keys, keyId, keyXPosition, separatorYUpperPosition, isPageInternal);

                // Store key position to connect to child pages later
                int cellId = getCellId(pageId, keyId, maxNumberOfNodes);
                Cell cellPosition = new Cell(keyXPosition, currentYPosition - KEY_HALF_CELL_DIMENSION);
                keyIdToCellPositionMap.put(cellId, cellPosition);

                if (keyId == 0 && pageToDraw.parentKeyIndex != NO_PARENT) {
                    drawLineConnectingToParent(keyIdToCellPositionMap, pageToDraw, keyXPosition,
                            separatorYUpperPosition);
                }
            }

            // If it is the last key in the page, draw the last key indicator
            if (keyId == maxNumberOfNodes - 1) {
                drawLastKeyIndicator(keyFinalXPosition, currentYPosition, separatorYUpperPosition, isPageInternal);
            }
        }

        private void drawSeparators(double separatorXPosition, double currentYPosition, double separatorYUpperPosition) {
            StdDraw.line(separatorXPosition,
                    currentYPosition - KEY_HALF_CELL_DIMENSION,
                    separatorXPosition,
                    separatorYUpperPosition);
        }

        private void drawInternalHorizontalLine(double initialXPosition, double currentYPosition, double finalXPosition) {
            StdDraw.setPenColor(Color.GRAY);

            StdDraw.line(initialXPosition,
                    currentYPosition - VERTICAL_SPACE_TO_ADJUST_FOR_INTERNAL_PAGES_DIVIDER,
                    finalXPosition,
                    currentYPosition - VERTICAL_SPACE_TO_ADJUST_FOR_INTERNAL_PAGES_DIVIDER);

            StdDraw.setPenColor(Color.BLACK);
        }

        private void drawKey(List<Key> keys, int keyId, double keyXPosition, double cellYUpperPosition,
                             boolean isPageInternal) {
            double yPosition = cellYUpperPosition - KEY_HALF_CELL_DIMENSION;

            if (isPageInternal) {
                yPosition += VERTICAL_SPACE_TO_ADJUST_FOR_INTERNAL_PAGES_DIVIDER;
                StdDraw.setPenColor(Color.RED);
            }

            StdDraw.text(keyXPosition,
                    yPosition,
                    String.valueOf(keys.get(keyId)));

            StdDraw.setPenColor(Color.BLACK);
        }

        private void drawLineConnectingToParent(SeparateChainingHashTable<Integer, Cell> keyIdToCellPositionMap,
                                                PageToDraw pageToDraw, double keyXPosition,
                                                double cellYUpperPosition) {
            Cell parentCellPosition = keyIdToCellPositionMap.get(pageToDraw.parentKeyIndex);

            StdDraw.line(keyXPosition,
                    cellYUpperPosition,
                    parentCellPosition.x,
                    parentCellPosition.y);
        }

        private void drawLastKeyIndicator(double keyFinalXPosition, double currentYPosition,
                                          double cellYUpperPosition, boolean isPageInternal) {
            double finalYPosition = cellYUpperPosition - KEY_CELL_DIMENSION;

            if (isPageInternal) {
                finalYPosition = currentYPosition - VERTICAL_SPACE_TO_ADJUST_FOR_INTERNAL_PAGES_DIVIDER;
            }

            StdDraw.setPenColor(Color.GRAY);

            StdDraw.line(keyFinalXPosition,
                    cellYUpperPosition,
                    keyFinalXPosition - KEY_CELL_DIMENSION,
                    finalYPosition);

            StdDraw.setPenColor(Color.BLACK);
        }

        private int getCellId(int pageId, int keyId, int maxNumberOfNodes) {
            return (pageId * maxNumberOfNodes) + keyId;
        }
    }

    private class BTreeSETDrawable<Key extends Comparable<Key>> {

        private PageInterface<Key> root;
        private static final int DEFAULT_MAX_NUMBER_OF_NODES_PER_PAGE = 6;
        private static final boolean DEFAULT_VERBOSE = false;

        private int maxNumberOfNodesPerPage;
        private HashSet<PageInterface> pagesInMemory;
        private boolean verbose;

        BTreeSETDrawing<Key> bTreeSETDrawing;

        public BTreeSETDrawable(Key sentinel) {
            this(sentinel, DEFAULT_MAX_NUMBER_OF_NODES_PER_PAGE, DEFAULT_VERBOSE);
        }

        public BTreeSETDrawable(Key sentinel, int maxNumberOfNodesPerPage, boolean verbose) {
            if (maxNumberOfNodesPerPage % 2 != 0 || maxNumberOfNodesPerPage == 2) {
                throw new IllegalArgumentException("Max number of nodes must be divisible by 2 and higher than 2");
            }

            pagesInMemory = new HashSet<>();
            bTreeSETDrawing = new BTreeSETDrawing<>();

            root = new Page<>(true, maxNumberOfNodesPerPage, pagesInMemory);
            add(sentinel);

            this.verbose = verbose;
            root.setVerbose(verbose);
            this.maxNumberOfNodesPerPage = maxNumberOfNodesPerPage;
        }

        public PageInterface<Key> getRoot() {
            return root;
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
                bTreeSETDrawing.drawBTree(this);

                PageInterface<Key> leftHalf = root;
                PageInterface<Key> rightHalf = root.split();

                root = new Page<>(false, maxNumberOfNodesPerPage, pagesInMemory);
                root.add(leftHalf);
                root.add(rightHalf);

                root.setVerbose(verbose);
                rightHalf.setVerbose(verbose);
            }
            bTreeSETDrawing.drawBTree(this);
        }

        public void add(PageInterface<Key> page, Key key) {
            if (page.isExternal()) {
                page.add(key);
                return;
            }

            PageInterface<Key> next = page.next(key);
            add(next, key);

            if (next.isFull()) {
                bTreeSETDrawing.drawBTree(this);

                PageInterface<Key> newPage = next.split();
                newPage.setVerbose(verbose);
                page.add(newPage);
            }
            next.close();
        }
    }

    public static void main(String[] args) {
        Exercise17 exercise17 = new Exercise17();

        StdDraw.setXscale(0, 2.5);
        Font font = new Font("Arial", Font.PLAIN, 12);
        StdDraw.setFont(font);
        StdDraw.enableDoubleBuffering();

        BTreeSETDrawable<String> bTreeSETDrawable = exercise17.new BTreeSETDrawable<>("*");

        String[] keysToAdd = {
                "H", "K", "Q", "U", "B", "C", "E", "F", "I", "J", "M", "N", "O", "P", "R", "T", "W", "X", "Z"
        };

        for (String key : keysToAdd) {
            bTreeSETDrawable.add(key);
        }
    }

}
