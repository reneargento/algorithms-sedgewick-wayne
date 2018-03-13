package chapter2.section5;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Rene Argento on 10/04/17.
 */
//The resulting solution is guaranteed to be within 33% of the best possible (actually 4/3 - 1/(3N)).
public class Exercise13_LoadBalancing {

    private class Job implements Comparable<Job> {

        private String name;
        private int processingTime;

        Job(String name, int processingTime) {
            this.name = name;
            this.processingTime = processingTime;
        }

        @Override
        //Longest processing time first rule
        public int compareTo(Job that) {
            if (this.processingTime > that.processingTime) {
                return -1;
            } else if (this.processingTime < that.processingTime) {
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

    private class Processor implements Comparable<Processor> {
        private String name;
        private List<Job> jobsAssigned;
        private int sumOfJobsAssignedProcessingTime;

        Processor(String name) {
            this.name = name;
            this.sumOfJobsAssignedProcessingTime = 0;
            jobsAssigned = new ArrayList<>();
        }

        void assignJob(Job job) {
            jobsAssigned.add(job);
        }

        @Override
        public int compareTo(Processor that) {
            if (this.sumOfJobsAssignedProcessingTime < that.sumOfJobsAssignedProcessingTime) {
                return -1;
            } else if (this.sumOfJobsAssignedProcessingTime > that.sumOfJobsAssignedProcessingTime) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        int numberOfProcessors = Integer.parseInt(args[0]); //testcase: numberOfProcessors = 3

        new Exercise13_LoadBalancing().lpt(numberOfProcessors);
    }

    private void lpt(int numberOfProcessors) {

        /**
         * Test case
         *
         * JobA 100
         * JobB 1
         * JobC 999
         * JobD 1000
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

        PriorityQueue<Processor> heap = new PriorityQueue<>();
        for(int i = 0; i < numberOfProcessors; i++) {
            Processor processor = new Processor("Processor " + i);
            heap.add(processor);
        }

        loadBalanceAndPrintSchedule(jobs, heap);

        StdOut.println();
        StdOut.println("Expected:");
        StdOut.println("JobF assigned to Processor 0\n" +
                "JobD assigned to Processor 2\n" +
                "JobC assigned to Processor 1\n" +
                "JobA assigned to Processor 1\n" +
                "JobB assigned to Processor 2\n" +
                "JobE assigned to Processor 2");
    }

    private void loadBalanceAndPrintSchedule(Job[] jobs, PriorityQueue<Processor> heap) {

        for(int i = 0; i < jobs.length; i++) {
            Processor nextProcessorAvailable = heap.remove();
            //Assign job to the next available processor
            nextProcessorAvailable.assignJob(jobs[i]);
            nextProcessorAvailable.sumOfJobsAssignedProcessingTime += jobs[i].processingTime;

            StdOut.println(jobs[i].name + " assigned to " + nextProcessorAvailable.name);

            heap.add(nextProcessorAvailable);
        }

    }

}
