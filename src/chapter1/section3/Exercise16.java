package chapter1.section3;

import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise16 {

	private static Date[] readAllDates(String fileName) {
		In in = new In(fileName);
		Queue<Date> queue = new Queue<>();

		while(!in.isEmpty()) {
			queue.enqueue(new Date(in.readString()));
		}

		int n = queue.size();
		Date[] dates = new Date[n];
		
		for (int i = 0; i < n; i++) {
			dates[i] = queue.dequeue();
		}

		return dates;
	}
	
	public static void main(String[] args) {
		String dateFilePath = args[0];
		Date[] dates = readAllDates(dateFilePath);
		
		for (Date date : dates) {
			StdOut.println(date);
		}
	}

}
