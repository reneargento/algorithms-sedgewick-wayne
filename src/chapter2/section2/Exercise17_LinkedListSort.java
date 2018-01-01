package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by Rene Argento on 15/02/17.
 */
//Based on http://stackoverflow.com/questions/7685/merge-sort-a-linked-list
@SuppressWarnings("unchecked")
public class Exercise17_LinkedListSort<Item> implements Iterable<Item> {

    private class Node {
        Item item;
        Node next;

        Node() {
        }

        Node(Item item) {
            this.item = item;
        }
    }

    private int size;
    private Node first;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void add(Item item) {
        Node newNode = new Node(item);
        newNode.next = first;

        first = newNode;

        size++;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            Item item = current.item;
            current = current.next;

            return item;
        }
    }

    public static void main(String[] args) {
        Exercise17_LinkedListSort<Comparable> linkedList = createList();

        Exercise17_LinkedListSort.Node newSourceNode = mergesort(linkedList.first);
        linkedList.first = newSourceNode;

        while(newSourceNode != null) {
            StdOut.print(newSourceNode.item + " ");

            newSourceNode = newSourceNode.next;
        }
    }

    private static Exercise17_LinkedListSort<Comparable> createList() {
        Exercise17_LinkedListSort<Comparable> linkedList = new Exercise17_LinkedListSort<>();

        linkedList.add(-9);
        linkedList.add(20);
        linkedList.add(1);
        linkedList.add(5);
        linkedList.add(55);
        linkedList.add(-10);
        linkedList.add(0);

        return linkedList;
    }

    //Dave Gamble's answer
    //Runtime: O(n log n)
    //Space: CONSTANT
    private static Exercise17_LinkedListSort.Node mergesort(Exercise17_LinkedListSort<Comparable>.Node sourceNode) {

        if(sourceNode == null || sourceNode.next == null) {
            return sourceNode;
        }

        int listSize = 1;
        int numberOfMerges;
        int leftSize;
        int rightSize;

        Exercise17_LinkedListSort<Comparable>.Node tail;
        Exercise17_LinkedListSort<Comparable>.Node left;
        Exercise17_LinkedListSort<Comparable>.Node right;
        Exercise17_LinkedListSort<Comparable>.Node next;

        do{ // For each power of 2 <= list length
            numberOfMerges = 0;
            left = sourceNode;
            tail = null;
            sourceNode = null;

            while(left != null) { // Do this list length / listSize times:
                numberOfMerges++;
                right = left;
                leftSize = 0;
                rightSize = listSize;

                // Cut list into two halves (but don't overrun)
                while(right != null && leftSize < listSize) {
                    leftSize++;
                    right = right.next;
                }

                // Run through the lists appending onto what we have so far.
                while(leftSize > 0 || (rightSize > 0 && right != null)) {
                    // Left empty, take right OR Right empty, take left, OR compare.
                    if(leftSize == 0) {
                        next = right;
                        right = right.next;
                        rightSize--;
                    } else if (rightSize == 0 || right == null) {
                        next = left;
                        left = left.next;
                        leftSize--;
                    } else if(left.item.compareTo(right.item) <= 0) {
                        next = left;
                        left = left.next;
                        leftSize--;
                    } else {
                        next = right;
                        right = right.next;
                        rightSize--;
                    }

                    // Update pointers to keep track of where we are:
                    if(tail != null) {
                        tail.next = next;
                    } else {
                        sourceNode = next;
                    }

                    tail = next;
                }
                // Right is now AFTER the list we just sorted, so start the next sort there.
                left = right;
            }
            // Terminate the list, double the list-sort size.
            if(tail != null) {
                tail.next = null;
            }

            listSize *= 2;
        } while (numberOfMerges > 1); // If we only did one merge, then we just sorted the whole list.

        return sourceNode;
    }

    //Recursive approach:
    // Runs faster, but requires log n space due to recursive stack
    //jayadev's answer
    //Runtime: O(n log n)
    //Space: log n
    private static Exercise17_LinkedListSort.Node mergesortRecursive(Exercise17_LinkedListSort<Comparable>.Node sourceNode) {

        if(sourceNode == null || sourceNode.next == null) {
            return sourceNode;
        }

        Exercise17_LinkedListSort.Node middle = getMiddle(sourceNode);

        //Split the list in 2
        Exercise17_LinkedListSort.Node secondHalf = middle.next;
        middle.next = null;

        return merge(mergesortRecursive(sourceNode), mergesortRecursive(secondHalf));
    }

    //Finding the middle element of the list for splitting
    private static Exercise17_LinkedListSort.Node getMiddle(Exercise17_LinkedListSort<Comparable>.Node sourceNode) {

        if(sourceNode == null) {
            return null;
        }

        Exercise17_LinkedListSort.Node slow = sourceNode;
        Exercise17_LinkedListSort.Node fast = sourceNode;

        while(fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    private static Exercise17_LinkedListSort.Node merge(Exercise17_LinkedListSort<Comparable>.Node firstHalf,
                                                        Exercise17_LinkedListSort<Comparable>.Node secondHalf) {
        Exercise17_LinkedListSort<Comparable>.Node dummyHead = new Exercise17_LinkedListSort<Comparable>().new Node();
        Exercise17_LinkedListSort<Comparable>.Node current = dummyHead;

        while(firstHalf != null && secondHalf != null) {
            if(firstHalf.item.compareTo(secondHalf.item) <= 0) {
                current.next = firstHalf;
                firstHalf = firstHalf.next;
            } else {
                current.next = secondHalf;
                secondHalf = secondHalf.next;
            }

            current = current.next;
        }

        current.next = firstHalf == null ? secondHalf : firstHalf;

        return dummyHead.next;
    }
}
