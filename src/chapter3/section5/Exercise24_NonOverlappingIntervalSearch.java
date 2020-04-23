package chapter3.section5;

import chapter3.section3.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 17/08/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for suggesting a simplification for this exercise:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/133
public class Exercise24_NonOverlappingIntervalSearch {

    private class Interval {
        int start;
        int end;
        int index;

        Interval(int index, int start, int end) {
            this.index = index;
            this.start = start;
            this.end = end;
        }
    }

    private class NonOverlappingIntervalFinder {
        private RedBlackBST<Integer, Interval> redBlackBST;

        NonOverlappingIntervalFinder(List<Interval> intervals) {
            redBlackBST = new RedBlackBST<>();

            for(Interval interval : intervals) {
                redBlackBST.put(interval.start, interval);
            }
        }

        private int findInterval(int query) {
            Integer key = redBlackBST.floor(query);
            Interval possibleInterval = redBlackBST.get(key);

            if (possibleInterval != null && possibleInterval.start <= query && query <= possibleInterval.end) {
                return possibleInterval.index;
            } else {
                return -1;
            }
        }
    }

    public static void main(String[] args) {
        Exercise24_NonOverlappingIntervalSearch nonOverlappingIntervalSearch = new Exercise24_NonOverlappingIntervalSearch();
        Interval interval1 = nonOverlappingIntervalSearch.new Interval(1, 1643, 2033);
        Interval interval2 = nonOverlappingIntervalSearch.new Interval(2, 5532, 7643);
        Interval interval3 = nonOverlappingIntervalSearch.new Interval(3, 8999, 10332);
        Interval interval4 = nonOverlappingIntervalSearch.new Interval(4, 5666653, 5669321);

        List<Interval> intervals = new ArrayList<>();
        intervals.add(interval1);
        intervals.add(interval2);
        intervals.add(interval3);
        intervals.add(interval4);

        NonOverlappingIntervalFinder nonOverlappingIntervalFinder =
                nonOverlappingIntervalSearch.new NonOverlappingIntervalFinder(intervals);

        StdOut.println("Find 9122: " + nonOverlappingIntervalFinder.findInterval(9122) + " Expected: 3");
        StdOut.println("Find 8122: " + nonOverlappingIntervalFinder.findInterval(8122) + " Expected: -1");
        StdOut.println("Find -100: " + nonOverlappingIntervalFinder.findInterval(-100) + " Expected: -1");
        StdOut.println("Find 5531: " + nonOverlappingIntervalFinder.findInterval(5531) + " Expected: -1");
        StdOut.println("Find 5532: " + nonOverlappingIntervalFinder.findInterval(5532) + " Expected: 2");
        StdOut.println("Find 5669321: " + nonOverlappingIntervalFinder.findInterval(5669321) + " Expected: 4");
        StdOut.println("Find 5669322: " + nonOverlappingIntervalFinder.findInterval(5669322) + " Expected: -1");
    }

}
