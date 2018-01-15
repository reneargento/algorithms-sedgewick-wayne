package chapter1.section3;

import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise7<Item> {

	private Node first;
	private int n;
	
	private class Node {
		Item item;
		Node next;
	}
	
	public boolean isEmpty() {
		return first == null;
	}
	
	public int size() {
		return n;
	}
	
	public void push(Item item) {
		Node oldFirst = first;
		
		first = new Node();
		first.item = item;
		first.next = oldFirst;
		n++;
	}
	
	public Item pop() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		}
		Item item = first.item;
		first = first.next;
		n--;
		
		return item;
	}
	
	public Item peek() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		}
		return first.item;
	}
	
	public static void main (String[] args) {
		Exercise7<String> stack = new Exercise7<>();
		
		stack.push("String 1");
		stack.push("String 2");
		stack.push("String 4");
		stack.push("String 8");
		
		StdOut.println("Peek: " + stack.peek());
		StdOut.println("Expected: String 8\n");
		
		StdOut.println("Pop: " + stack.pop());
        StdOut.println("Expected: String 8\n");
		StdOut.println("Pop: " + stack.pop());
        StdOut.println("Expected: String 4");
	}
}
