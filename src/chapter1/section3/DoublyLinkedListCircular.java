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
        if(size == 0) {
            return null;
        }

        return first;
    }

    public DoubleNode last() {
        if(size == 0) {
            return null;
        }

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

        if (oldFirst != null) {
            first.previous = oldFirst.previous;
            oldFirst.previous = first;
        }

        //If the list was empty before adding the new item:
        if (last == null) {
            last = first;
        } else {
            last.next = first;
        }

        size++;
    }

    public void insertNodeAtTheEnd(DoubleNode doubleNode) {
        DoubleNode oldLast = last;

        last = doubleNode;
        last.previous = oldLast;

        if (oldLast != null) {
            last.next = oldLast.next;
            oldLast.next = last;
        }

        //If the list was empty before adding the new item:
        if (first == null) {
            first = last;
        } else {
            first.previous = last;
        }

        size++;
    }

    public void insertAtTheBeginning(Item item) {
        DoubleNode oldFirst = first;

        first = new DoubleNode();
        first.item = item;
        first.next = oldFirst;

        if (oldFirst != null) {
            first.previous = oldFirst.previous;
            oldFirst.previous = first;
        }

        //If the list was empty before adding the new item:
        if (last == null) {
            last = first;
        } else {
            last.next = first;
        }

        size++;
    }

    public void insertAtTheEnd(Item item) {
        DoubleNode oldLast = last;

        last = new DoubleNode();
        last.item = item;
        last.previous = oldLast;

        if (oldLast != null) {
            last.next = oldLast.next;
            oldLast.next = last;
        }

        //If the list was empty before adding the new item:
        if (first == null) {
            first = last;
        } else {
            first.previous = last;
        }

        size++;
    }

    public void insertBeforeNode(Item beforeItem, Item item) {
        if (isEmpty()) {
            return;
        }

        DoubleNode currentNode;

        for(currentNode = first; currentNode != null; currentNode = currentNode.next) {
            if (currentNode.item.equals(beforeItem)) {
                break;
            }
        }

        if (currentNode != null) {
            DoubleNode newNode = new DoubleNode();
            newNode.item = item;

            DoubleNode previousNode = currentNode.previous;
            currentNode.previous = newNode;
            newNode.next = currentNode;
            newNode.previous = previousNode;

            if (newNode.previous == null) {
                //This is the first node
                first = newNode;
            } else {
                newNode.previous.next = newNode;
            }

            size++;
        }
    }

    public void insertAfterNode(Item afterNode, Item item) {
        if (isEmpty()) {
            return;
        }

        DoubleNode currentNode;

        for(currentNode = first; currentNode != null; currentNode = currentNode.next) {
            if (currentNode.item.equals(afterNode)) {
                break;
            }
        }

        if (currentNode != null) {
            DoubleNode newNode = new DoubleNode();
            newNode.item = item;

            DoubleNode nextNode = currentNode.next;
            currentNode.next = newNode;
            newNode.previous = currentNode;
            newNode.next = nextNode;

            if (newNode.next == null) {
                //This is the last node
                last = newNode;
            } else {
                newNode.next.previous = newNode;
            }

            size++;
        }
    }

    public void insertLinkedListAtTheEnd(DoublyLinkedListCircular<Item> doublyLinkedListCircular) {
        DoubleNode oldLast = last;

        last = doublyLinkedListCircular.last();
        doublyLinkedListCircular.first().previous = oldLast;

        if (oldLast != null) {
            doublyLinkedListCircular.last().next = oldLast.next;
            oldLast.next = doublyLinkedListCircular.first();
        }

        //If the list was empty before adding the new item:
        if (first == null) {
            first = doublyLinkedListCircular.first();
        } else {
            first.previous = doublyLinkedListCircular.last();
        }

        size += doublyLinkedListCircular.size;
    }

    public Item removeFromTheBeginning() {
        if (isEmpty()){
            return null;
        }

        Item item = first.item;

        if (first.next != null) {
            first.next.previous = first.previous;
        } else { // There is only 1 element in the list
            last = null;
        }

        if (first.previous != null) {
            if(size > 2) {
                first.previous.next = first.next;
            } else {
                first.previous.next = null;
            }
        }

        first = first.next;

        size--;

        return item;
    }

    public Item removeFromTheEnd(){
        if (isEmpty()) {
            return null;
        }

        Item item = last.item;

        if (last.previous != null) {
            if(size > 2) {
                last.previous.next = last.next;
            } else {
                last.previous.next = null;
            }
        } else { // There is only 1 element in the list
            first = null;
        }

        if (last.next != null) {
            if(size > 2) {
                last.next.previous = last.previous;
            } else {
                last.next.previous = null;
            }
        }

        last = last.previous;

        size--;

        return item;
    }

    public void removeItem(Item item) {
        if(isEmpty()) {
            return;
        }

        DoubleNode currentNode = first;

        while (currentNode != null) {
            if(currentNode.item.equals(item)) {
                removeItemWithNode(currentNode);
                break;
            }

            currentNode = currentNode.next;
        }
    }

    public void removeItemWithNode(DoubleNode doubleNode) {
        if(doubleNode == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }

        if(isEmpty()) {
            return;
        }

        DoubleNode previousNode = doubleNode.previous;
        DoubleNode nextNode = doubleNode.next;

        if (previousNode != null) {
            previousNode.next = nextNode;
        }
        if (nextNode != null) {
            nextNode.previous = previousNode;
        }

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

        while (currentNode != null) {
            if (nodeIndex == index) {
                break;
            }

            if (startFromTheBeginning) {
                index++;
            } else{
                index--;
            }

            currentNode = startFromTheBeginning ? currentNode.next : currentNode.previous;
        }

        @SuppressWarnings("ConstantConditions") //if we got here, the node exists
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
