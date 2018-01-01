package chapter1.section3;

import edu.princeton.cs.algs4.Transaction;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise17 {

	public static Transaction[] readAllTransactions(String fileName) {
		In in = new In(fileName);
		Queue<Transaction> queue = new Queue<>();

		while(!in.isEmpty()) {
			queue.enqueue(new Transaction(in.readLine()));
		}

		int queueSize = queue.size();
		Transaction[] transactions = new Transaction[queueSize];
		
		for (int i = 0; i < queueSize; i++) {
			transactions[i] = queue.dequeue();
		}

		return transactions;
	}
	
	public static void main(String[] args) {
		String transactionFilePath = args[0];
		Transaction[] transactions = readAllTransactions(transactionFilePath);
		
		for (Transaction transaction : transactions) {
			StdOut.println(transaction);
		}
	}
	
}
