package chapter2.section1;

import edu.princeton.cs.algs4.Date;

/**
 * Created by Rene Argento on 30/01/17.
 */
public class Exercise21_ComparableTransactions implements Comparable<Exercise21_ComparableTransactions>{

    private final String who;
    private final Date when;
    private final double amount;

    public Exercise21_ComparableTransactions(String who, Date when, double amount) {
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

    public int compareTo(Exercise21_ComparableTransactions otherTransaction) {

        if (this.amount > otherTransaction.amount) {
            return 1;
        } else if (this.amount < otherTransaction.amount) {
            return -1;
        } else {
            return 0;
        }
    }

}
