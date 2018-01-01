package chapter2.section1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Transaction;

import java.util.Arrays;

/**
 * Created by Rene Argento on 30/01/17.
 */
public class Exercise22_TransactionSortTest {

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

        Arrays.sort(transactions);

        for (Transaction transaction : transactions) {
            StdOut.println(transaction);
        }
    }

}
