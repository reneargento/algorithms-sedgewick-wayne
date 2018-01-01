package chapter4.section4;

import java.util.Arrays;

/**
 * Adapted by Rene Argento on 21/12/17.
 */

/**
 * The {@code SegmentTree} class is an structure for efficient search of cumulative data.
 * It performs Range Minimum Query and Range Sum Query in O(log(n)) time.
 * It can be easily customizable to support Range Max Query, Range Multiplication Query etc.
 * <p>
 * Also it has been developed with  {@code LazyPropagation} for range updates, which means
 * when you perform update operations over a range, the update process affects the least nodes as possible
 * so that the bigger the range you want to update the less time it consumes to update it.
 * Eventually those changes will be propagated to the children and the whole array will be up to date.
 * <p>
 * Example:
 * <p>
 * SegmentTree st = new SegmentTree(new Integer[]{1,3,4,2,1, -2, 4});
 * st.update(0, 3, 1)
 * In the above case only the node that represents the range [0,3] will be updated (and not their children) so in this case
 * the update task will be less than n*log(n)
 *
 * Memory usage: O(n)
 *
 * @author Ricardo Pacheco
 */
// Based on https://github.com/kevin-wayne/algs4/blob/master/src/main/java/edu/princeton/cs/algs4/SegmentTree.java
public class SegmentTree {

    //The Node class represents a partition range of the array.
    private static class Node {
        double sum;
        double min;

        //Here we store the value that will be propagated lazily
        Double pendingValue = null;
        int from;
        int to;

        int size() {
            return to - from + 1;
        }
    }

    private Node[] heap;
    private double[] array;

    /**
     * Time-Complexity:  O(n*log(n))
     *
     * @param array the Initialization array
     */
    public SegmentTree(double[] array) {
        this.array = Arrays.copyOf(array, array.length);
        //The max size of this array is about 2 * 2 ^ (log2(n) + 1)
        int size = (int) (2 * Math.pow(2.0, Math.floor((Math.log((double) array.length) / Math.log(2.0)) + 1)));
        heap = new Node[size];
        build(1, 0, array.length);
    }

    public int size() {
        return array.length;
    }

    //Initialize the Nodes of the Segment tree
    private void build(int index, int from, int size) {
        heap[index] = new Node();
        heap[index].from = from;
        heap[index].to = from + size - 1;

        if (size == 1) {
            heap[index].sum = array[from];
            heap[index].min = array[from];
        } else {
            //Build childs
            build(2 * index, from, size / 2);
            build(2 * index + 1, from + size / 2, size - size / 2);

            heap[index].sum = heap[2 * index].sum + heap[2 * index + 1].sum;
            //min = min of the children
            heap[index].min = Math.min(heap[2 * index].min, heap[2 * index + 1].min);
        }
    }

    /**
     * Range Sum Query
     *
     * Time-Complexity: O(log(n))
     *
     * @param  from from index
     * @param  to to index
     * @return sum
     */
    public double rangeSumQuery(int from, int to) {
        return rangeSumQuery(1, from, to);
    }

    private double rangeSumQuery(int index, int from, int to) {
        Node node = heap[index];

        //If you did a range update that contained this node, you can infer the Sum without going down the tree
        if (node.pendingValue != null && contains(node.from, node.to, from, to)) {
            return (to - from + 1) * node.pendingValue;
        }

        if (contains(from, to, node.from, node.to)) {
            return heap[index].sum;
        }

        if (intersects(from, to, node.from, node.to)) {
            propagate(index);
            double leftSum = rangeSumQuery(2 * index, from, to);
            double rightSum = rangeSumQuery(2 * index + 1, from, to);

            return leftSum + rightSum;
        }

        return 0;
    }

    /**
     * Range Min Query
     *
     * Time-Complexity: O(log(n))
     *
     * @param  from from index
     * @param  to to index
     * @return min
     */
    public double rangeMinQuery(int from, int to) {
        return rangeMinQuery(1, from, to);
    }

    private double rangeMinQuery(int index, int from, int to) {
        Node node = heap[index];

        //If you did a range update that contained this node, you can infer the Min value without going down the tree
        if (node.pendingValue != null && contains(node.from, node.to, from, to)) {
            return node.pendingValue;
        }

        if (contains(from, to, node.from, node.to)) {
            return heap[index].min;
        }

        if (intersects(from, to, node.from, node.to)) {
            propagate(index);
            double leftMin = rangeMinQuery(2 * index, from, to);
            double rightMin = rangeMinQuery(2 * index + 1, from, to);

            return Math.min(leftMin, rightMin);
        }

        return Double.MAX_VALUE;
    }

    /**
     * Range Update Operation.
     * With this operation you can update either one position or a range of positions with a given number.
     * The update operations will update the less they can to update the whole range (Lazy Propagation).
     * The values will be propagated lazily from top to bottom of the segment tree.
     * This behavior is really useful for updates on portions of the array
     * <p>
     * Time-Complexity: O(log(n))
     *
     * @param from  from index
     * @param to    to index
     * @param value value
     */
    public void update(int from, int to, double value) {
        update(1, from, to, value);
    }

    private void update(int index, int from, int to, double value) {

        //The Node of the heap tree represents a range of the array with bounds: [node.from, node.to]
        Node node = heap[index];

        /**
         * If the updating-range contains the portion of the current Node, we lazily update it.
         * This means that we do NOT update each position of the vector, but update only some temporal
         * values into the Node; such values will be propagated down to the Node's children only when they need to.
         */
        if (contains(from, to, node.from, node.to)) {
            change(node, value);
        }

        if (node.size() == 1) return;

        if (intersects(from, to, node.from, node.to)) {
            /**
             * Before continuing to go down the tree we need to propagate the
             * values that have been temporarily/lazily saved into this Node to its children,
             * so that when we visit them the values are properly updated.
             */
            propagate(index);

            update(2 * index, from, to, value);
            update(2 * index + 1, from, to, value);

            node.sum = heap[2 * index].sum + heap[2 * index + 1].sum;
            node.min = Math.min(heap[2 * index].min, heap[2 * index + 1].min);
        }
    }

    //Propagate temporal values to children
    private void propagate(int index) {
        Node node = heap[index];

        if (node.pendingValue != null) {
            change(heap[2 * index], node.pendingValue);
            change(heap[2 * index + 1], node.pendingValue);
            node.pendingValue = null; //unset the pending propagation value
        }
    }

    //Save the temporal values that will be propagated lazily
    private void change(Node node, double value) {
        node.pendingValue = value;
        node.sum = node.size() * value;
        node.min = value;
        array[node.from] = value;
    }

    //Test if range1 contains range2
    private boolean contains(int from1, int to1, int from2, int to2) {
        return from2 >= from1 && to2 <= to1;
    }

    //Check inclusive intersection, test if range1[from1, to1] intersects range2[from2, to2]
    private boolean intersects(int from1, int to1, int from2, int to2) {
        return from1 <= from2 && to1 >= from2   //  (.[..)..] or (.[...]..)
                || from1 >= from2 && from1 <= to2; // [.(..]..) or [..(..)..
    }
}
