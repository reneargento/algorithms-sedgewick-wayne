package chapter1.section3;

import java.util.Iterator;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise30<Item> implements Iterable<Item> {

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
				if (count == k-1 && current.next != null) {
					current.next = current.next.next;
					break;
				}
				count++;
			}
		}
		size--;
	}
	
	// First implementation
	public Node reverse() {
		if (isEmpty()) {
			return null;
		}
		
		if (size == 1) {
			return first;
		}
		
		Node old = first;
		Node current = first.next;
		Node newNode = first.next.next;
		
		first.next = null;
		first = current;
		current.next = old;
		
		while(newNode != null) {
			old = current;
			current = newNode;

            newNode = newNode.next;
			
			current.next = old;
			first = current;
		}
		
		return first;
	}
	
	//Improved implementation 
	public Node reverse2() {
		Node reverse = null;
		
		while(first != null) {
			Node second = first.next;
			first.next = reverse;
			reverse = first;
			first = second;
		}
		
		first = reverse;
		return reverse;
	}
	
	//Recursive solution
	public void reverse3() {
		first = reverse3Impl(first);
	}
		
	private Node reverse3Impl(Node first) {
		if (first == null) {
			return null;
		}
			
		if (first.next == null) {
			return first;
		}
		
		Node second = first.next;
		Node rest = reverse3Impl(second);
		second.next = first;
		first.next = null;
		
		return rest;
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
		Exercise30<Integer> linkedList = new Exercise30<>();
		linkedList.add(1);
		linkedList.add(2);
		linkedList.add(3);
		linkedList.add(4);
		
		linkedList.reverse3();

        StringJoiner listItems = new StringJoiner(" ");
        for (int item : linkedList) {
            listItems.add(String.valueOf(item));
        }

        StdOut.println("Reverse list items: " + listItems.toString());
        StdOut.println("Expected: 4 3 2 1");
	}
	
}
