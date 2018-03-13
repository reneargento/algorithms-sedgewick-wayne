package chapter1.section2;

import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise13 {
	
	private final String who;
	private final Date when;
	private final double amount;
	
	public Exercise13(String who, Date when, double amount) {
		this.who = who;
		this.when = when;
		this.amount = amount;
	}
	
	public String who() {
		return who;
	}
	
	public Date when() {
		return when;
	}
	
	public double amount() {
		return amount;
	}
	
	public String toString() {
		return who() + " spent " + amount() + " on " + when();
	}
	
	public static void main (String[] args) {
		Date date = new Date(8, 3, 2016);
		
		Exercise13 transaction = new Exercise13("Rene", date, 500);
		StdOut.println(transaction);
		StdOut.println("Expected: Rene spent 500.0 on 8/3/2016");
	}
	
}
