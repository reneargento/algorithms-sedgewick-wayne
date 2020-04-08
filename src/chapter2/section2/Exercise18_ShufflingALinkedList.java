package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 18/02/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for reporting a bug in which elements were not
// shuffled uniformly at random.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/108
// Based on https://stackoverflow.com/questions/12167630/algorithm-for-shuffling-a-linked-list-in-n-log-n-time
@SuppressWarnings("unchecked")
public class Exercise18_ShufflingALinkedList<Item> {

    private class Node {
        Item item;
        Exercise18_ShufflingALinkedList.Node next;

        Node() {
        }

        Node(Item item) {
            this.item = item;
        }
    }

    private int size;
    private Exercise18_ShufflingALinkedList.Node first;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void add(Item item) {
        Exercise18_ShufflingALinkedList.Node newNode = new Exercise18_ShufflingALinkedList.Node(item);
        newNode.next = first;

        first = newNode;
        size++;
    }

    public Iterator<Item> iterator() {
        return new Exercise18_ShufflingALinkedList.ListIterator();
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
        Map<String, Integer> countMap = new HashMap<>();

        for (int i = 0; i < 100000; i++) {
            Exercise18_ShufflingALinkedList<Comparable> list = createList();
            Exercise18_ShufflingALinkedList<Comparable>.Node newHead = shuffle(list.first);

            StringJoiner shuffledList = new StringJoiner(" ");

            while(newHead != null) {
                shuffledList.add(String.valueOf(newHead.item));
                newHead = newHead.next;
            }
            String value = shuffledList.toString();
            countMap.put(value, countMap.getOrDefault(value, 0) + 1);
        }

        StdOut.println("List: 0 0 1");
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            StdOut.println("Permutation = " + entry.getKey() + ", Count = " + entry.getValue());
        }
    }

    private static Exercise18_ShufflingALinkedList<Comparable> createList() {
        Exercise18_ShufflingALinkedList<Comparable> linkedList = new Exercise18_ShufflingALinkedList<>();
        linkedList.add(0);
        linkedList.add(0);
        linkedList.add(1);
        return linkedList;
    }

    private static Exercise18_ShufflingALinkedList.Node shuffle(Exercise18_ShufflingALinkedList.Node firstHalf) {
        if (firstHalf == null || firstHalf.next == null) {
            return firstHalf;
        }

        Exercise18_ShufflingALinkedList.Node middle = getMiddle(firstHalf);
        Exercise18_ShufflingALinkedList.Node secondHalf = middle.next;
        middle.next = null;

        return shuffleItems(shuffle(firstHalf), shuffle(secondHalf));
    }

    private static Exercise18_ShufflingALinkedList.Node getMiddle(Exercise18_ShufflingALinkedList<Comparable>.Node source) {
        if (source == null) {
            return null;
        }

        Exercise18_ShufflingALinkedList.Node slow = source;
        Exercise18_ShufflingALinkedList.Node fast = source;

        while(fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private static Exercise18_ShufflingALinkedList.Node shuffleItems(Exercise18_ShufflingALinkedList.Node firstHalf,
                                                                     Exercise18_ShufflingALinkedList.Node secondHalf) {
        Exercise18_ShufflingALinkedList.Node dummyHead = new Exercise18_ShufflingALinkedList().new Node();
        Exercise18_ShufflingALinkedList.Node current = dummyHead;

        int[] listSizes = getListSizes(firstHalf, secondHalf);
        int firstHalfLength = listSizes[0];
        int secondHalfLength = listSizes[1];

        while(firstHalf != null && secondHalf != null) {
            double random = StdRandom.uniform();
            // Select elements according to the Gilbert–Shannon–Reeds model
            double selectFromFirstHalfProbability = firstHalfLength / (double) (firstHalfLength + secondHalfLength);

            if (random < selectFromFirstHalfProbability) {
                current.next = firstHalf;
                firstHalf = firstHalf.next;
                firstHalfLength--;
            } else {
                current.next = secondHalf;
                secondHalf = secondHalf.next;
                secondHalfLength--;
            }
            current = current.next;
        }
        current.next = firstHalf == null ? secondHalf : firstHalf;

        return dummyHead.next;
    }

    private static int[] getListSizes(Exercise18_ShufflingALinkedList.Node firstHalf,
                                      Exercise18_ShufflingALinkedList.Node secondHalf) {
        int firstHalfLength = 0;
        int secondHalfLength = 0;

        while (firstHalf != null && secondHalf != null) {
            firstHalf = firstHalf.next;
            secondHalf = secondHalf.next;
            firstHalfLength++;
            secondHalfLength++;
        }

        while (firstHalf != null) {
            firstHalfLength++;
            firstHalf = firstHalf.next;
        }
        while (secondHalf != null) {
            secondHalfLength++;
            secondHalf = secondHalf.next;
        }
        return new int[]{firstHalfLength, secondHalfLength};
    }

}
