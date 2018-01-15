package chapter1.section3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise15 {
	
	private static void printItems(Queue<String> queue, int k) {
		int count = 0;
		
		for (String item : queue) {
			count++;
			
			if (count >= k) {
				StdOut.println(item);
			}
		}
	}

	// Parameters example: 3 "A B C D E F"
	public static void main (String[] args) {
		int k = Integer.parseInt(args[0]);

		String input = args[1];
		String[] stringsInput = input.split(" ");

		Queue<String> queue = new Queue<>();

		for(String string : stringsInput) {
			queue.enqueue(string);
		}
		
		printItems(queue, k);
	}
	
}
