package chapter1.section3;

import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise2<Item> {
	
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

	public static void main (String...args) {
		Exercise2<String> stack = new Exercise2<>();
		
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
			
			if (!item.equals("-")) {
				stack.push(item);
			} else if (!stack.isEmpty()) {
				StdOut.print(stack.pop() + " ");
			}
		}
		StdOut.println("(" + stack.size() + " left on stack)");
	}
	
}
