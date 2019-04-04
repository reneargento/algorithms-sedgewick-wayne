package chapter1.section3;

import java.util.Iterator;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise24<Item> implements Iterable<Item> {

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
		Exercise24<Integer> linkedList = new Exercise24<>();
		linkedList.add(0);
		linkedList.add(1);
		linkedList.add(2);
		linkedList.add(3);
		linkedList.add(4);
		
		StdOut.println("Before removing node after node 0");

        StringJoiner listBeforeRemove = new StringJoiner(" ");
        for (int number : linkedList) {
            listBeforeRemove.add(String.valueOf(number));
        }

        StdOut.println(listBeforeRemove.toString());
        StdOut.println("Expected: 0 1 2 3 4");
		
		Exercise24<Integer>.Node nodeToBeDeleted = linkedList.createNode(0);
		linkedList.removeAfter(nodeToBeDeleted);
		
		StdOut.println("\nAfter removing node after node 0");

        StringJoiner listAfterRemove = new StringJoiner(" ");
        for (int number : linkedList) {
            listAfterRemove.add(String.valueOf(number));
        }

        StdOut.println(listAfterRemove.toString());
        StdOut.println("Expected: 0 2 3 4");
	}
	
}
