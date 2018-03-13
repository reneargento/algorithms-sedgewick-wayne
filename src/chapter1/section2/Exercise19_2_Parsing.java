package chapter1.section2;

import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise19_2_Parsing {
	
	private final String who;
	private final Date when;
	private final double amount;
	
	public Exercise19_2_Parsing(String who, Date when, double amount) {
		this.who = who;
		this.when = when;
		this.amount = amount;
	}
	
	public Exercise19_2_Parsing(String transaction) {
		String[] values = transaction.split(" ");
		who = values[0];
		when = new Date(values[1]);
		amount = Double.parseDouble(values[2]);
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
	
	public static void main (String[] args) {
		Date date = new Date(8, 5, 2016);
		
		Exercise19_2_Parsing transaction = new Exercise19_2_Parsing("Turing", date, 22.10);
		StdOut.println(transaction);
		StdOut.println("Expected: Turing spent 22.1 on 8/5/2016");
	}
	

}
