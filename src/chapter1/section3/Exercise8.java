package chapter1.section3;

import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise8 implements Iterable<String> {

	private String[] items = new String[1];
	private int n = 0;
	
	public boolean isEmpty() {
		return n == 0;
	}
	
	public int size() {
		return n;
	}
	
	private void resize(int max) {
		String[] temp = new String[max];
		
		for(int i = 0; i < n; i++) {
			temp[i] = items[i];
		}
		
		items = temp;
	}
	
	public void push(String item) {
		if (n == items.length) {
			resize(items.length * 2);
		}
		items[n++] = item;
	}
	
	public String pop() {
		String item = null;
		
		if (!isEmpty()) {
			item = items[--n];
		}
		
		if (!isEmpty()) {
			items[n] = null; //to avoid loitering
		}
		
		if (n > 0 && n == items.length / 4) {
			resize(items.length / 2);
		}
		
		return item;
	}
	
	public Iterator<String> iterator() {
		return new ReverseArrayIterator();
	}
	
	private class ReverseArrayIterator implements Iterator<String>{
		
		int index = n;
		
		public boolean hasNext() {
			return index > 0;
		}
		
		public String next() {
			return items[--index];
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static void main(String[] args) {
		Exercise8 resizingArrayStackOfStrings = new Exercise8();
		
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
			
			if (!item.equals("-")) {
				resizingArrayStackOfStrings.push(item);
			} else if (!resizingArrayStackOfStrings.isEmpty()) {
				StdOut.print(resizingArrayStackOfStrings.pop() + " ");
			}
		}
		StdOut.println("(" + resizingArrayStackOfStrings.size() + " left on stack)");
	}
	
}
