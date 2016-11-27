package Chapter1.Section3.LinkedList;

import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;

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
				for (int i=0; i < size-2; i++) {
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
		
		StdOut.println("Before removing");
		for (int number : linkedList) {
			StdOut.println(number);
		}
		
		linkedList.deleteLastNode();
		
		StdOut.println("After removing");
		for (int number : linkedList) {
			StdOut.println(number);
		}
	}

}
