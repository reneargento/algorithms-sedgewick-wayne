package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 12/04/17.
 */
public class Exercise20_IdleTime {

    private class Job implements Comparable<Job> {

        private int startTime;
        private int endTime;

        Job(int startTime, int endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public int compareTo(Job that) {
            if (this.startTime < that.startTime) {
                return -1;
            } else if (this.startTime > that.startTime) {
                return 1;
            } else {
                if (this.endTime < that.endTime) {
                    return -1;
                } else if (this.endTime > that.endTime) {
                    return 1;
                }
            }

            return 0;
        }
    }

    public static void main(String[] args) {

        Exercise20_IdleTime idleTime = new Exercise20_IdleTime();

        Job job1 = idleTime.new Job(5, 6);
        Job job2 = idleTime.new Job(12, 14);
        Job job3 = idleTime.new Job(1, 3);
        Job job4 = idleTime.new Job(9, 12);
        Job job5 = idleTime.new Job(15, 16);

        Job[] jobs = {job1, job2, job3, job4, job5};

        int[] intervals = idleTime.getAsynchronousJobsIntervals(jobs);

        StdOut.println("Largest idle interval: " + intervals[0] + " to " + intervals[1]);
        StdOut.println("Largest non-idle interval: " + intervals[2] + " to " + intervals[3]);

        StdOut.println("\nExpected");
        StdOut.println("Largest idle interval: 6 to 9");
        StdOut.println("Largest non-idle interval: 9 to 14");

        Job job6 = idleTime.new Job(1, 10);
        Job job7 = idleTime.new Job(6, 10);
        Job job8 = idleTime.new Job(2, 4);
        Job job9 = idleTime.new Job(5, 12);
        Job job10 = idleTime.new Job(15, 17);
        Job job11 = idleTime.new Job(16, 20);

        Job[] jobs2 = {job6, job7, job8, job9, job10, job11};

        int[] intervals2 = idleTime.getAsynchronousJobsIntervals(jobs2);

        StdOut.println("\nLargest idle interval: " + intervals2[0] + " to " + intervals2[1]);
        StdOut.println("Largest non-idle interval: " + intervals2[2] + " to " + intervals2[3]);

        StdOut.println("\nExpected");
        StdOut.println("Largest idle interval: 12 to 15");
        StdOut.println("Largest non-idle interval: 1 to 12");
    }

    private int[] getAsynchronousJobsIntervals(Job[] jobs) {

        if (jobs == null || jobs.length == 0) {
            return new int[]{0, 0, 0, 0};
        }

        int currentIntervalStartIndex = 0;

        int largestIdleTimeStartIndex = 0;
        int largestIdleTimeEndIndex = 0;
        int largestBusyTimeStartIndex = 0;
        int largestBusyTimeEndIndex = 0;

        Arrays.sort(jobs);

        int currentMaxEndTime = jobs[0].endTime;
        int currentMaxEndTimeIndex = 0;

        int maxIdleTime = jobs[0].startTime;

        for(int i = 0; i < jobs.length; i++) {

            if (i != 0 && jobs[i].startTime > currentMaxEndTime) {

                //A new interval is beginning
                if (jobs[i].startTime - currentMaxEndTime > maxIdleTime) {
                    largestIdleTimeEndIndex = i;
                    largestIdleTimeStartIndex = currentMaxEndTimeIndex;

                    maxIdleTime = jobs[largestIdleTimeEndIndex].startTime - jobs[largestIdleTimeStartIndex].endTime;
                }

                currentIntervalStartIndex = i;
            }

            if (jobs[i].endTime - jobs[currentIntervalStartIndex].startTime >
                    jobs[largestBusyTimeEndIndex].endTime - jobs[largestBusyTimeStartIndex].startTime) {
                largestBusyTimeStartIndex = currentIntervalStartIndex;
                largestBusyTimeEndIndex = i;
            }

            if (jobs[i].endTime > currentMaxEndTime) {
                currentMaxEndTime = jobs[i].endTime;
                currentMaxEndTimeIndex = i;
            }
        }

        int largestIdleTimeStart = jobs[largestIdleTimeStartIndex].endTime;
        int largestIdleTimeEnd = jobs[largestIdleTimeEndIndex].startTime;

        //Edge case - when the largest idle interval is from time 0 to the beginning of the first job
        if (largestIdleTimeStart > largestIdleTimeEnd) {
            largestIdleTimeStart = 0;
        }

        int largestBusyTimeStart = jobs[largestBusyTimeStartIndex].startTime;
        int largestBusyTimeEnd = jobs[largestBusyTimeEndIndex].endTime;

        return new int[]{largestIdleTimeStart, largestIdleTimeEnd, largestBusyTimeStart, largestBusyTimeEnd};
    }

    //Only used for synchronous jobs
    private int[] getSynchronousJobsIntervals(Job[] jobs) {

        if (jobs == null || jobs.length == 0) {
            return new int[]{0, 0, 0, 0};
        }

        int currentIntervalStartIndex = 0;

        int largestIdleTimeStartIndex = 0;
        int largestIdleTimeEndIndex = 0;
        int largestBusyTimeStartIndex = 0;
        int largestBusyTimeEndIndex = 0;

        Arrays.sort(jobs);

        int maxIdleTime = jobs[0].startTime;

        for(int i = 0; i < jobs.length; i++) {

            if (i != 0 && jobs[i].startTime > jobs[i - 1].endTime) {

                //A new interval is beginning
                if (jobs[i].startTime - jobs[i - 1].endTime > maxIdleTime) {
                    largestIdleTimeEndIndex = i;
                    largestIdleTimeStartIndex = i-1;

                    maxIdleTime = jobs[largestIdleTimeEndIndex].startTime - jobs[largestIdleTimeStartIndex].endTime;
                }

                currentIntervalStartIndex = i;
            }

            if (jobs[i].endTime - jobs[currentIntervalStartIndex].startTime >
                    jobs[largestBusyTimeEndIndex].endTime - jobs[largestBusyTimeStartIndex].startTime) {
                largestBusyTimeStartIndex = currentIntervalStartIndex;
                largestBusyTimeEndIndex = i;
            }
        }

        int largestIdleTimeStart = jobs[largestIdleTimeStartIndex].endTime;
        int largestIdleTimeEnd = jobs[largestIdleTimeEndIndex].startTime;

        //Edge case - when the largest idle interval is from time 0 to the beginning of the first job
        if (largestIdleTimeStart > largestIdleTimeEnd) {
            largestIdleTimeStart = 0;
        }

        int largestBusyTimeStart = jobs[largestBusyTimeStartIndex].startTime;
        int largestBusyTimeEnd = jobs[largestBusyTimeEndIndex].endTime;

        return new int[]{largestIdleTimeStart, largestIdleTimeEnd, largestBusyTimeStart, largestBusyTimeEnd};
    }

}
