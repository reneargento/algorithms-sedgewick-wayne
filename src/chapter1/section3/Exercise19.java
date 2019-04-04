package chapter1.section3;

import java.util.Iterator;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise19<Item> implements Iterable<Item>{
	
	private class Node {
		Item item;
		Node next;
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
		Exercise19<Integer> linkedList = new Exercise19<>();
		linkedList.add(0);
		linkedList.add(1);
		linkedList.add(2);
		linkedList.add(3);
		
		StdOut.println("Before removing last node");

        StringJoiner listBeforeRemove = new StringJoiner(" ");
		for (int number : linkedList) {
            listBeforeRemove.add(String.valueOf(number));
		}

        StdOut.println(listBeforeRemove.toString());
        StdOut.println("Expected: 0 1 2 3");
		
		linkedList.deleteLastNode();
		
		StdOut.println("\nAfter removing last node");

        StringJoiner listAfterRemove = new StringJoiner(" ");
		for (int number : linkedList) {
            listAfterRemove.add(String.valueOf(number));
		}

        StdOut.println(listAfterRemove.toString());
        StdOut.println("Expected: 0 1 2");
	}

}
