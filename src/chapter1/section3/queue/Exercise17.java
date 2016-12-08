package chapter1.section3.queue;

import edu.princeton.cs.algs4.Transaction;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Exercise17 {

	public static Transaction[] readAllTransactions(String fileName) {
		In in = new In(fileName);
		Queue<Transaction> queue = new Queue<>();
		while(!in.isEmpty()) {
			queue.enqueue(new Transaction(in.readLine()));
		}
		int n = queue.size();
		Transaction[] transactions = new Transaction[n];
		
		for (int i = 0; i< n; i++) {
			transactions[i] = queue.dequeue();
		}
		return transactions;
	}
	
	public static void main(String[] args) {
		String transactionFilePath = "/Users/rene/Desktop/Algorithms/Books/Algorithms, 4th ed. - Exercises/Data/transactionsFile.txt";
		Transaction[] transactions = readAllTransactions(transactionFilePath);
		
		for (Transaction transaction : transactions) {
			StdOut.println(transaction);
		}
	}
	
}
