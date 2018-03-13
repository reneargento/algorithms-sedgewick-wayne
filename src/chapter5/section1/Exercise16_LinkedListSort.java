package chapter5.section1;

import chapter1.section3.DoublyLinkedList;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 13/01/18.
 */
// The linked list must be a doubly linked list due to the traversing back of "greaterThan" node in the main loop.
public class Exercise16_LinkedListSort {

    public DoublyLinkedList<String>.DoubleNode sortLinkedList(DoublyLinkedList<String> doublyLinkedList) {

        DoublyLinkedList<String>.DoubleNode lowNode = doublyLinkedList.getFirstNode();
        DoublyLinkedList<String>.DoubleNode highNode = doublyLinkedList.getLastNode();

        sortLinkedList(lowNode, highNode, 0, doublyLinkedList.size() - 1, 0);
        return doublyLinkedList.getFirstNode();
    }

    private void sortLinkedList(DoublyLinkedList<String>.DoubleNode lowNode, DoublyLinkedList<String>.DoubleNode highNode,
                                int lowIndex, int highIndex, int digit) {
        if (lowIndex >= highIndex) {
            return;
        }

        DoublyLinkedList<String>.DoubleNode pivotNode = getPivotNode(lowNode, highNode, lowIndex, highIndex);

        exchange(lowNode, pivotNode);

        int pivot = charAt(lowNode.item, digit);

        DoublyLinkedList<String>.DoubleNode lowerThan = lowNode;
        DoublyLinkedList<String>.DoubleNode greaterThan = highNode;

        int lowerThanIndex = lowIndex;
        int greaterThanIndex = highIndex;

        DoublyLinkedList<String>.DoubleNode currentNode = lowNode.next;
        int index = lowIndex + 1;

        while (index <= greaterThanIndex) {
            int currentChar = charAt(currentNode.item, digit);

            if (currentChar < pivot) {
                exchange(lowerThan, currentNode);
                lowerThan = lowerThan.next;
                currentNode = currentNode.next;

                index++;
                lowerThanIndex++;
            } else if (currentChar > pivot) {
                exchange(currentNode, greaterThan);
                greaterThan = greaterThan.previous;
                greaterThanIndex--;
            } else {
                currentNode = currentNode.next;
                index++;
            }
        }

        // Now linkedList[lowIndex..lowerThanIndex - 1] < pivot = linkedList[lowerThanIndex..greaterThanIndex]
        // < linkedList[greaterThanIndex + 1..highIndex]
        sortLinkedList(lowNode, lowerThan.previous, lowIndex, lowerThanIndex - 1, digit);
        if (digit != -1) {
            sortLinkedList(lowerThan, greaterThan, lowerThanIndex, greaterThanIndex, digit + 1);
        }
        sortLinkedList(greaterThan.next, highNode, greaterThanIndex + 1, highIndex, digit);
    }

    private DoublyLinkedList<String>.DoubleNode getPivotNode(DoublyLinkedList<String>.DoubleNode lowNode,
                                                             DoublyLinkedList<String>.DoubleNode highNode, int lowIndex,
                                                             int highIndex) {
        int pivotIndex = StdRandom.uniform(lowIndex, highIndex + 1);

        DoublyLinkedList<String>.DoubleNode currentNode;
        int middleIndex = lowIndex + (highIndex - lowIndex) / 2;

        if (pivotIndex <= middleIndex) {
            currentNode = lowNode;
            int count = lowIndex;

            while (count != pivotIndex) {
                currentNode = currentNode.next;
                count++;
            }
        } else {
            currentNode = highNode;
            int count = highIndex;

            while (count != pivotIndex) {
                currentNode = currentNode.previous;
                count--;
            }
        }

        return currentNode;
    }

    private void exchange(DoublyLinkedList<String>.DoubleNode node1, DoublyLinkedList<String>.DoubleNode node2) {
        String temp = node1.item;
        node1.item = node2.item;
        node2.item = temp;
    }

    private int charAt(String string, int digit) {
        if (digit < string.length()) {
            return string.charAt(digit);
        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        Exercise16_LinkedListSort linkedListSort = new Exercise16_LinkedListSort();

        DoublyLinkedList<String> linkedList = new DoublyLinkedList<>();

        linkedList.insertAtTheEnd("Rene");
        linkedList.insertAtTheEnd("Argento");
        linkedList.insertAtTheEnd("Arg");
        linkedList.insertAtTheEnd("Alg");
        linkedList.insertAtTheEnd("Algorithms");
        linkedList.insertAtTheEnd("LSD");
        linkedList.insertAtTheEnd("MSD");
        linkedList.insertAtTheEnd("3WayStringQuickSort");
        linkedList.insertAtTheEnd("Dijkstra");
        linkedList.insertAtTheEnd("Floyd");
        linkedList.insertAtTheEnd("Warshall");
        linkedList.insertAtTheEnd("Johnson");
        linkedList.insertAtTheEnd("Sedgewick");
        linkedList.insertAtTheEnd("Wayne");
        linkedList.insertAtTheEnd("Bellman");
        linkedList.insertAtTheEnd("Ford");
        linkedList.insertAtTheEnd("BFS");
        linkedList.insertAtTheEnd("DFS");

        DoublyLinkedList<String>.DoubleNode nodeInSortedList = linkedListSort.sortLinkedList(linkedList);

        StringJoiner sortedList = new StringJoiner(" ");

        while (nodeInSortedList != null) {
            sortedList.add(nodeInSortedList.item);
            nodeInSortedList = nodeInSortedList.next;
        }

        StdOut.print("Sorted list:\n" + sortedList);
        StdOut.println("\nExpected: \n3WayStringQuickSort Alg Algorithms Arg Argento BFS Bellman DFS Dijkstra Floyd Ford " +
                "Johnson LSD MSD Rene Sedgewick Warshall Wayne");
    }

}
