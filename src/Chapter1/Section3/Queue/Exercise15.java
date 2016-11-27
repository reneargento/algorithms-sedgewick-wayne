package Chapter1.Section3.Queue;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

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

	public static void main (String[] args) {
		Queue<String> queue = new Queue<>();
		queue.enqueue("A");
		queue.enqueue("B");
		queue.enqueue("C");
		queue.enqueue("D");
		queue.enqueue("E");
		queue.enqueue("F");
		
		int k = 3;
		
		printItems(queue, k);
	}
	
}
