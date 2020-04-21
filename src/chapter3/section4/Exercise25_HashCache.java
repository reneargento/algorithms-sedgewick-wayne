package chapter3.section4;

import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 22/07/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for suggesting an improvement on the hashCode() computation.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/126
public class Exercise25_HashCache {

    public class Transaction {
        private final String who;
        private final Date when;
        private final double amount;

        private int hash;

        Transaction(String who, Date when, double amount) {
            this.who = who;
            this.when = when;
            this.amount = amount;

            hash = -1;
        }

        public int hashCode() {
            int hash;

            if (this.hash != -1) {
                hash = this.hash;

                StdOut.println("Cache hit");
            } else {
                hash = 17;
                hash = 31 * hash + who.hashCode();
                hash = 31 * hash + when.hashCode();
                hash = 31 * hash + ((Double) amount).hashCode();

                StdOut.println("Cache miss");

                this.hash = hash;
            }

            return hash;
        }

    }

    public static void main(String[] args) {
        Exercise25_HashCache hashCache = new Exercise25_HashCache();

        Transaction transaction = hashCache.new Transaction("Person 1", new Date(1, 10, 5), 1000);
        StdOut.println(transaction.hashCode() + " Expected: Cache miss");
        StdOut.println(transaction.hashCode() + " Expected: Cache hit");
        StdOut.println(transaction.hashCode() + " Expected: Cache hit");
    }

}
