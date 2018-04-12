package chapter1.section3;

import java.util.Iterator;

/**
 * Created by Rene Argento on 14/11/17.
 */
public class DoublyLinkedListCircular<Item> implements Iterable<Item> {

    public class DoubleNode {
        public Item item;
        DoubleNode previous;
        DoubleNode next;
    }

    private int size;
    private DoubleNode first;
    private DoubleNode last;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public DoubleNode first() {
        return first;
    }

    public DoubleNode last() {
        return last;
    }

    public Item get(int index) {
        if (isEmpty()) {
            return null;
        }

        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Index must be between 0 and " + (size() - 1));
        }

        DoubleNode current = first;
        int count = 0;

        while (count != index) {
            current = current.next;
            count++;
        }

        return current.item;
    }

    public void insertNodeAtTheBeginning(DoubleNode doubleNode) {
        DoubleNode oldFirst = first;

        first = doubleNode;
        first.next = oldFirst;

        if (!isEmpty()) {
            first.previous = oldFirst.previous;
            oldFirst.previous = first;
        } else {
            last = first;
            first.previous = last;
        }

        last.next = first;
        size++;
    }

    public void insertNodeAtTheEnd(DoubleNode doubleNode) {
        DoubleNode oldLast = last;

        last = doubleNode;
        last.previous = oldLast;

        if (!isEmpty()) {
            last.next = oldLast.next;
            oldLast.next = last;
        } else {
            first = last;
            last.next = first;
        }

        first.previous = last;
        size++;
    }

    public void insertAtTheBeginning(Item item) {
        DoubleNode oldFirst = first;

        first = new DoubleNode();
        first.item = item;
        first.next = oldFirst;

        if (!isEmpty()) {
            first.previous = oldFirst.previous;
            oldFirst.previous = first;
        } else {
            last = first;
            first.previous = last;
        }

        last.next = first;
        size++;
    }

    public void insertAtTheEnd(Item item) {
        DoubleNode oldLast = last;

        last = new DoubleNode();
        last.item = item;
        last.previous = oldLast;

        if (!isEmpty()) {
            last.next = oldLast.next;
            oldLast.next = last;
        } else {
            first = last;
            last.next = first;
        }

        first.previous = last;
        size++;
    }

    public void insertBeforeNode(Item beforeItem, Item item) {
        if (isEmpty()) {
            return;
        }

        DoubleNode currentNode;

        for(currentNode = first; currentNode != last; currentNode = currentNode.next) {
            if (currentNode.item.equals(beforeItem)) {
                break;
            }
        }

        if (currentNode == last && last.item != beforeItem) {
            return;
        }

        DoubleNode newNode = new DoubleNode();
        newNode.item = item;

        DoubleNode previousNode = currentNode.previous;
        currentNode.previous = newNode;
        newNode.next = currentNode;
        newNode.previous = previousNode;

        if (newNode.previous == last) {
            // This is the first node
            first = newNode;
        }

        newNode.previous.next = newNode;
        size++;
    }

    public void insertAfterNode(Item afterNode, Item item) {
        if (isEmpty()) {
            return;
        }

        DoubleNode currentNode;

        for(currentNode = first; currentNode != last; currentNode = currentNode.next) {
            if (currentNode.item.equals(afterNode)) {
                break;
            }
        }

        if (currentNode == last && last.item != afterNode) {
            return;
        }

        DoubleNode newNode = new DoubleNode();
        newNode.item = item;

        DoubleNode nextNode = currentNode.next;
        currentNode.next = newNode;
        newNode.previous = currentNode;
        newNode.next = nextNode;

        if (newNode.next == first) {
            // This is the last node
            last = newNode;
        }

        newNode.next.previous = newNode;
        size++;
    }

    public void insertLinkedListAtTheEnd(DoublyLinkedListCircular<Item> doublyLinkedListCircular) {

        if (!doublyLinkedListCircular.isEmpty()) {
            doublyLinkedListCircular.first().previous = last;
            doublyLinkedListCircular.last().next = first;
        }

        if (!isEmpty()) {
            last.next = doublyLinkedListCircular.first();
            first.previous = doublyLinkedListCircular.last();
        } else {
            first = doublyLinkedListCircular.first();
        }

        last = doublyLinkedListCircular.last();
        size += doublyLinkedListCircular.size;
    }

    public Item removeFromTheBeginning() {
        if (isEmpty()) {
            return null;
        }

        Item item = first.item;

        if (size() > 1) {
            first.next.previous = first.previous;
            first.previous.next = first.next;
            first = first.next;
        } else { // There is only 1 element in the list
            first = null;
            last = null;
        }

        size--;
        return item;
    }

    public Item removeFromTheEnd() {
        if (isEmpty()) {
            return null;
        }

        Item item = last.item;

        if (size() > 1) {
            last.previous.next = last.next;
            last.next.previous = last.previous;
            last = last.previous;
        } else { // There is only 1 element in the list
            first = null;
            last = null;
        }

        size--;
        return item;
    }

    public void removeItem(Item item) {
        if (isEmpty()) {
            return;
        }

        DoubleNode currentNode = first;

        while (currentNode != last) {
            if (currentNode.item.equals(item)) {
                removeItemWithNode(currentNode);
                return;
            }

            currentNode = currentNode.next;
        }

        if (currentNode.item == item) {
            removeFromTheEnd();
        }
    }

    public void removeItemWithNode(DoubleNode doubleNode) {
        if (doubleNode == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }

        // Base case 1: empty list
        if (isEmpty()) {
            return;
        }

        // Base case 2: size 1 list
        if (doubleNode == first && first == last) {
            first = null;
            last = null;
            return;
        }

        DoubleNode previousNode = doubleNode.previous;
        DoubleNode nextNode = doubleNode.next;

        previousNode.next = nextNode;
        nextNode.previous = previousNode;

        if (doubleNode == first) {
            first = nextNode;
        }
        if (doubleNode == last) {
            last = previousNode;
        }

        size--;
    }

    public Item removeItemWithIndex(int nodeIndex) {
        if (isEmpty()) {
            return null;
        }

        if (nodeIndex < 0 || nodeIndex >= size) {
            throw new IllegalArgumentException("Index must be between 0 and " + (size() - 1));
        }

        boolean startFromTheBeginning = nodeIndex <= size() / 2;

        int index = startFromTheBeginning ? 0 : size() - 1;

        DoubleNode currentNode = startFromTheBeginning? first : last;

        while (true) {
            if (nodeIndex == index) {
                break;
            }

            if (startFromTheBeginning) {
                index++;
            } else {
                index--;
            }

            currentNode = startFromTheBeginning ? currentNode.next : currentNode.previous;
        }

        @SuppressWarnings("ConstantConditions") // If we got here, the node exists
        Item item = currentNode.item;
        removeItemWithNode(currentNode);

        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DoublyLinkedListCircularIterator();
    }

    private class DoublyLinkedListCircularIterator implements Iterator<Item> {

        int index = 0;
        DoubleNode currentNode = first;

        @Override
        public boolean hasNext() {
            return index < size();
        }

        @Override
        public Item next() {
            Item item = currentNode.item;
            currentNode = currentNode.next;

            index++;
            return item;
        }
    }

}
