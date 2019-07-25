package chapter1.section3;

import java.util.Iterator;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
// Thanks to Oreshnik (https://github.com/Oreshnik) for reporting that some methods were not updating
// the list size.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/41
// Thanks to sdxxxa (https://github.com/sdxxxa) for reporting that there was a bug in the remove() method.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/60
public class Exercise26<Item> implements Iterable<Item> {

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
	
	public void remove(Item key) {
		if (isEmpty() || key == null) {
			return;
		}
		
		while(first != null && first.item.equals(key)) {
			first = first.next;
			size--;
		}
		
		Node current;
		
		for(current = first; current != null; current = current.next) {
			Node next = current.next;

			while (next != null && next.item.equals(key)) {
				next = next.next;
				size--;
			}

			current.next = next;
		}
	}
	
	public boolean find(Item key) {
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
		Exercise26<String> linkedList = new Exercise26<>();
		linkedList.add("Mark");
		linkedList.add("Bill");
		linkedList.add("Elon");
		linkedList.add("Rene");
		linkedList.add("Mark");
		linkedList.add("Mark");
		linkedList.add("Mark");
		linkedList.add("Elon");
		
		StdOut.println("Before removing Mark");

		StringJoiner listBeforeRemove = new StringJoiner(" ");
		for (String name : linkedList) {
            listBeforeRemove.add(name);
		}

        StdOut.println(listBeforeRemove.toString());
        StdOut.println("Expected: Mark Bill Elon Rene Mark Mark Mark Elon");
		
		String itemToBeRemoved = "Mark";
		linkedList.remove(itemToBeRemoved);
		
		StdOut.println("\nAfter removing Mark");

        StringJoiner listAfterRemove = new StringJoiner(" ");
        for (String name : linkedList) {
            listAfterRemove.add(name);
        }

        StdOut.println(listAfterRemove.toString());
        StdOut.println("Expected: Bill Elon Rene Elon");
	}
	
}
