package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise1 { //FixedCapacityStackOfStrings
	
	private String[] a;
	private int n;

	public Exercise1(int cap) {
		a = new String[cap];
	}
	
	public boolean isEmpty() {
		return n == 0;
	}
	
	public int size() {
		return n;
	}
	
	public void push(String item) {
		a[n++] = item;
	}
	
	public String pop() {
		return a[--n];
	}
	
	public boolean isFull() {
		return n == a.length;
	}
	
	public static void main (String...args) {
		Exercise1 fixedCapacityStackOfStrings = new Exercise1(2);
		StdOut.println("Is Full 1: " + fixedCapacityStackOfStrings.isFull() + " Expected: false");
		
		fixedCapacityStackOfStrings.push("a");
		fixedCapacityStackOfStrings.push("b");
		StdOut.println("Is Full 2: " + fixedCapacityStackOfStrings.isFull() + " Expected: true");
	}
}
