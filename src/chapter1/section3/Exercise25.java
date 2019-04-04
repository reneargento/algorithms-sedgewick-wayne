package chapter1.section3;

import java.util.Iterator;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise25<Item> implements Iterable<Item> {

	private class Node {
		Item item;
		Node next;
	}
	
	private int size;
	private Node first;
	
	public Node createNode(Item item) {
		Node node = new Node();
		node.item = item;
		return node;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public int size() {
		return size;
	}
	
	public void add(Item item) {
		if (isEmpty()) {
			first = new Node();
			first.item = item;
		} else {	
			Node current;
			for(current = first; current.next != null; current = current.next);
			
			Node newNode = new Node();
			newNode.item = item;
			current.next = newNode;
		}
		size++;
	}
	
	public void deleteLastNode() {
		if (!isEmpty()) {
			if (size == 1) {
				first = null;
			} else {
				Node current = first;
				for (int i = 0; i < size - 2; i++) {
					current = current.next;
				}
				current.next = null;
			}
			
			size--;
		}
	}
	
	public void delete(int k) {
		if (k > size || isEmpty()) {
			return;
		}
		
		if (k == 1) {
			first = first.next;
		} else {
			Node current;
			int count = 1;
			
			for(current = first; current != null; current = current.next) {
				if (count == k - 1 && current.next != null) {
					current.next = current.next.next;
					break;
				}
				count++;
			}
		}
		size--;
	}
	
	public boolean find(String key) {
		if (isEmpty()) {
			return false;
		}
		
		boolean contains = false;
		Node current;
		for (current = first; current != null; current = current.next) {
			if (current.item.equals(key)) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	public void removeAfter(Node node) {
		if (isEmpty() || node == null) {
			return;
		}
		
		Node current;
		
		for(current = first; current != null; current = current.next) {
			if (current.item.equals(node.item)) {
				if (current.next != null) {
					current.next = current.next.next;
                    size--;
				}
				break;
			}
		}
	}
	
	public void insertAfter(Node firstNode, Node secondNode) {
		if (isEmpty() || firstNode == null || secondNode == null) {
			return;
		}
		
		Node current;
		
		for (current = first; current != null; current = current.next) {
			if (current.item.equals(firstNode.item)) {
				secondNode.next = current.next;
				current.next = secondNode;
				size++;
			}
		}
	}
	
	@Override
	public Iterator<Item> iterator() {
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<Item> {
		Node current = first;
		
		@Override
		public boolean hasNext() {
			return current != null; 
		}
		
		@Override
		public Item next() {
			Item item = current.item;
			current = current.next;
			
			return item;
		}
	}

	public static void main(String[] args) {
		Exercise25<Integer> linkedList = new Exercise25<>();
		linkedList.add(0);
		linkedList.add(1);
		linkedList.add(2);
		linkedList.add(3);
		linkedList.add(4);
		
		StdOut.println("Before inserting node 99 (after node 2)");

		StringJoiner listBeforeInsert = new StringJoiner(" ");
		for (int number : linkedList) {
            listBeforeInsert.add(String.valueOf(number));
		}

		StdOut.println(listBeforeInsert.toString());
		StdOut.println("Expected: 0 1 2 3 4");
		
		Exercise25<Integer>.Node nodeOfReference = linkedList.createNode(2);
		Exercise25<Integer>.Node nodeToBeInserted = linkedList.createNode(99);
		linkedList.insertAfter(nodeOfReference, nodeToBeInserted);
		
		StdOut.println("\nAfter inserting node 99 (after node 2)");

        StringJoiner listAfterInsert = new StringJoiner(" ");
        for (int number : linkedList) {
            listAfterInsert.add(String.valueOf(number));
        }

        StdOut.println(listAfterInsert.toString());
        StdOut.println("Expected: 0 1 2 99 3 4");
	}
	
}
