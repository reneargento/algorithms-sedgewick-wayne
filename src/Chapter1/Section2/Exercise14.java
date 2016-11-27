package Chapter1.Section2;

import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.algs4.StdOut;

public class Exercise14 {

	private final String who;
	private final Date when;
	private final double amount;
	
	public Exercise14(String who, Date when, double amount){
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
		return who() + " spent " + amount + " on " + when();
	}
	
	public boolean equals(Object x) {
		if (this == x) {
			return true;
		}
		if (x == null) {
			return false;
		}
		if (this.getClass() != x.getClass()) {
			return false;
		}
		
		Exercise14 that = (Exercise14) x;
		if (this.who != that.who) {
			return false;
		}
		if (this.when != that.when) {
			return false;
		}
		if (this.amount != that.amount) {
			return false;
		}
		
		return true;
	}
	
	public static void main (String[] args) {
		Date date = new Date(8, 3, 2016);
		
		Exercise14 transaction1 = new Exercise14("Rene", date, 500);
		Exercise14 transaction2 = new Exercise14("Rene", date, 500);
		Exercise14 transaction3 = new Exercise14("Argento", date, 600);
		Exercise14 transaction4 = transaction3;
		
		StdOut.println("Equals 1: " + transaction1.equals(transaction2));
		StdOut.println("Equals 2: " + transaction2.equals(transaction1));
		StdOut.println("Equals 3: " + transaction1.equals(transaction3));
		StdOut.println("Equals 4: " + transaction3.equals(transaction4));
	}
	
}
