package chapter2.section5;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 10/04/17.
 */
public class Exercise12_Scheduling {

    private class Job implements Comparable<Job> {

        private String name;
        private int processingTime;

        Job(String name, int processingTime) {
            this.name = name;
            this.processingTime = processingTime;
        }

        @Override
        //Shortest processing time first rule
        public int compareTo(Job that) {
            if (this.processingTime < that.processingTime) {
                return -1;
            } else if (this.processingTime > that.processingTime) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return name + "   " + processingTime;
        }
    }

    public static void main(String[] args) {
        new Exercise12_Scheduling().spt();
    }

    private void spt() {
        /**
         * Test case
         *
         * JobA 100
         * JobB 1
         * JobC 999
         * JobD 50
         * JobE 0
         * JobF 999999999
         */
        String[] input = StdIn.readAllLines();
        Job[] jobs = new Job[input.length];
        int jobsIndex = 0;

        for(String jobString : input) {
            String[] jobDescription = jobString.split(" ");
            String name = jobDescription[0];
            int processingTime = Integer.parseInt(jobDescription[1]);

            jobs[jobsIndex++] = new Job(name, processingTime);
        }

        Arrays.sort(jobs);

        StdOut.println("Schedule:");
        for(Job job : jobs) {
            StdOut.println(job);
        }

        StdOut.println();
        StdOut.println("Expected:");
        StdOut.println("JobE   0\n" +
                "JobB   1\n" +
                "JobD   50\n" +
                "JobA   100\n" +
                "JobC   999\n" +
                "JobF   999999999");
    }

}
