package chapter4.section4;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.*;

/**
 * Created by Rene Argento on 01/01/18.
 */
public class Exercise59_ParallelJobSchedulingWithDeadlinesModel {

    private class ParallelJobSchedulingWithDeadlinesProblem {

        private double[] jobDurations;
        private SeparateChainingHashTable<Integer, List<Integer>> jobPrecedences;

        // Relative deadlines in the format
        // JOB_ID1 MAXIMUM_TIME_ELAPSED_AFTER_JOBID2_STARTED_IN_WHICH_JOBID1_MUST_START JOB_ID2
        // Example: 2 12.0 4
        // Job 2 must start at most 12 seconds after job 4's start time
        private String[] relativeDeadlines;

        ParallelJobSchedulingWithDeadlinesProblem(double[] jobDurations,
                                                  SeparateChainingHashTable<Integer, List<Integer>> jobPrecedences,
                                                  String[] relativeDeadlines) {
            this.jobDurations = jobDurations;
            this.jobPrecedences = jobPrecedences;
            this.relativeDeadlines = relativeDeadlines;
        }

        public double[] getJobDurations() {
            return jobDurations;
        }

        public SeparateChainingHashTable<Integer, List<Integer>> getJobPrecedences() {
            return jobPrecedences;
        }

        public String[] getRelativeDeadlines() {
            return relativeDeadlines;
        }
    }

    public ParallelJobSchedulingWithDeadlinesProblem generateParallelJobSchedulingWithDeadlinesProblem(int numberOfJobs,
                                                                                                       double maxDuration,
                                                                                                       int maxPrecedences,
                                                                                                       int deadlines) {
        double[] jobDurations = new double[numberOfJobs];
        SeparateChainingHashTable<Integer, List<Integer>> jobPrecedences = new SeparateChainingHashTable<>();
        String[] relativeDeadlines = new String[deadlines];

        // Used to avoid repeating successors for the same job
        SeparateChainingHashTable<Integer, HashSet<Integer>> precedences = new SeparateChainingHashTable<>();

        // Any relative deadline duration higher than maxDuration * numberOfJobs would have no effect.
        // This is because if the sum of all jobs duration is D then if any job has a relative deadline duration to
        // any other job of D + 1 it would be automatically met because D is the maximum distance from any job
        // to any other job.
        double maxPossibleRelativeDeadlineDuration = maxDuration * numberOfJobs;

        // However, after empirical experiments, it was possible to notice that having such a high maximum-possible-
        // relative-deadline-duration led to several high deadline times, frequently not requiring any change in
        // the solution. To have tighter deadlines, we divide maxPossibleRelativeDeadlineDuration by 3.
        maxPossibleRelativeDeadlineDuration /= 3;

        for(int job = 0; job < numberOfJobs; job++) {
            // Duration
            double randomDuration = StdRandom.uniform(0, maxDuration + 1);
            jobDurations[job] = randomDuration;

            // Precedences
            jobPrecedences.put(job, new ArrayList<>());
            precedences.put(job, new HashSet<>());
            int numberOfSuccessors = StdRandom.uniform(maxPrecedences + 1);

            // Only add precedence to jobs with higher IDs to avoid cycles and increase chances of problem feasibility
            if (job + numberOfSuccessors >= numberOfJobs) {
                numberOfSuccessors = numberOfJobs - 1 - job;
            }

            for(int precedence = 0; precedence < numberOfSuccessors; precedence++) {
                int jobSuccessorId = StdRandom.uniform(job + 1, numberOfJobs);

                if (precedences.get(job).contains(jobSuccessorId)) {
                    // A precedence to this job already exists, try another job
                    precedence--;
                    continue;
                }

                jobPrecedences.get(job).add(jobSuccessorId);
                precedences.get(job).add(jobSuccessorId);
            }

            // Sort successor IDs
            Collections.sort(jobPrecedences.get(job));
        }

        // Only add relative deadlines to jobs with lower IDs to avoid negative cycles and increase chances of
        // problem feasibility
        for(int deadline = 0; deadline < deadlines; deadline++) {
            int jobThatMustBeginNotLongAfterTheOther = StdRandom.uniform(numberOfJobs);

            if (jobThatMustBeginNotLongAfterTheOther == 0) {
                // Try again
                deadline--;
                continue;
            }

            int otherJob = StdRandom.uniform(jobThatMustBeginNotLongAfterTheOther);
            double maxTimeThatCanPass = StdRandom.uniform(0, maxPossibleRelativeDeadlineDuration);
            relativeDeadlines[deadline] = String.format("%-5d", jobThatMustBeginNotLongAfterTheOther) +
                    String.format("%8.2f", maxTimeThatCanPass) + String.format("%7d", otherJob);
        }

        return new ParallelJobSchedulingWithDeadlinesProblem(jobDurations, jobPrecedences, relativeDeadlines);
    }

    private void printParallelJobSchedulingWithDeadlinesProblem(ParallelJobSchedulingWithDeadlinesProblem problem) {
        if (problem == null) {
            return;
        }

        double[] jobDurations = problem.getJobDurations();
        SeparateChainingHashTable<Integer, List<Integer>> jobPrecedences = problem.getJobPrecedences();
        String[] relativeDeadlines = problem.getRelativeDeadlines();

        StringBuilder problemDescription = new StringBuilder();

        problemDescription.append(String.format("%5s", "Job"))
                .append(String.format("%10s", "Duration"))
                .append(String.format("%22s", "Must complete before"));
        problemDescription.append("\n");

        for(int job = 0; job < jobDurations.length; job++) {
            problemDescription.append(String.format("%5d", job))
                    .append(String.format("%10.2f", jobDurations[job]));

            StringJoiner precedences = new StringJoiner(" ");
            for(int precedence : jobPrecedences.get(job)) {
                precedences.add(String.valueOf(precedence));
            }
            problemDescription.append(String.format("%22s", precedences.toString()));

            problemDescription.append("\n");
        }

        problemDescription.append("\n");
        problemDescription.append("Deadlines\n");

        for(String relativeDeadline : relativeDeadlines) {
            problemDescription.append(relativeDeadline).append("\n");
        }

        StdOut.println(problemDescription.toString());
    }

    private void solveProblemIfFeasible(ParallelJobSchedulingWithDeadlinesProblem problem) {
        if (problem == null) {
            return;
        }

        double[] jobDurations = problem.getJobDurations();
        SeparateChainingHashTable<Integer, List<Integer>> jobPrecedences = problem.getJobPrecedences();
        String[] relativeDeadlines = problem.getRelativeDeadlines();

        int numberOfJobs = jobDurations.length;

        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(2 * numberOfJobs + 2);

        int source = 2 * numberOfJobs;
        int target = 2 * numberOfJobs + 1;

        // Add direct successor relations
        for(int job = 0; job < numberOfJobs; job++) {
            edgeWeightedDigraph.addEdge(new DirectedEdge(job, job + numberOfJobs, jobDurations[job]));
            edgeWeightedDigraph.addEdge(new DirectedEdge(source, job, 0));
            edgeWeightedDigraph.addEdge(new DirectedEdge(job + numberOfJobs, target, 0));

            for(int successor : jobPrecedences.get(job)) {
                edgeWeightedDigraph.addEdge(new DirectedEdge(job + numberOfJobs, successor, 0));
            }
        }

        // Add relative deadline relations
        for(String relativeDeadline : relativeDeadlines) {
            String[] deadlineInformation = relativeDeadline.split(" +");

            int dependentJob = Integer.parseInt(deadlineInformation[0]);
            double duration = Double.parseDouble(deadlineInformation[1]);
            int job = Integer.parseInt(deadlineInformation[2]);

            edgeWeightedDigraph.addEdge(new DirectedEdge(dependentJob, job, -duration));
        }

        // Solve problem
        // 1- Negate all edge weights
        EdgeWeightedDigraph edgeWeightedDigraphNegatedEdges = new EdgeWeightedDigraph(edgeWeightedDigraph.vertices());

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                edgeWeightedDigraphNegatedEdges.addEdge(new DirectedEdge(edge.from(), edge.to(), -edge.weight()));
            }
        }

        // 2- Get longest paths using Bellman-Ford
        BellmanFordSP bellmanFordSP = new BellmanFordSP(edgeWeightedDigraphNegatedEdges, source);
        printJobSchedules(bellmanFordSP, numberOfJobs, target);
    }

    private void printJobSchedules(BellmanFordSP bellmanFordSP, int numberOfJobs, int target) {
        StdOut.println("Start times:");

        for(int job = 0; job < numberOfJobs; job++) {
            double distance = bellmanFordSP.distTo(job);

            if (distance != 0) {
                distance = distance * -1;
            }

            StdOut.printf("%4d: %6.2f\n", job, distance);
        }

        double targetDistance = bellmanFordSP.distTo(target);

        if (targetDistance != 0) {
            targetDistance = targetDistance * -1;
        }

        StdOut.printf("Finish time: %5.2f\n", targetDistance);
    }

    // Parameters example: 10 50 4 3
    public static void main(String[] args) {
        Exercise59_ParallelJobSchedulingWithDeadlinesModel parallelJobSchedulingWithDeadlinesModel =
                new Exercise59_ParallelJobSchedulingWithDeadlinesModel();

        int numberOfJobs = Integer.parseInt(args[0]);
        double maxDuration = Double.parseDouble(args[1]);
        int maxPrecedences = Integer.parseInt(args[2]);
        int numberOfProblemsToGenerate = Integer.parseInt(args[3]);

        // Add relative deadlines to at most a third of jobs to increase chances of problem feasibility
        int deadlines = (int) (numberOfJobs * 0.3);

        for(int problem = 1; problem <= numberOfProblemsToGenerate; problem++) {
            StdOut.println("Parallel job scheduling with deadlines problem " + problem + ":\n");

            ParallelJobSchedulingWithDeadlinesProblem parallelJobSchedulingWithDeadlinesProblem =
                    parallelJobSchedulingWithDeadlinesModel.
                            generateParallelJobSchedulingWithDeadlinesProblem(numberOfJobs, maxDuration, maxPrecedences,
                            deadlines);

            parallelJobSchedulingWithDeadlinesModel
                    .printParallelJobSchedulingWithDeadlinesProblem(parallelJobSchedulingWithDeadlinesProblem);

            // Considering the problem to be feasible
            StdOut.println("Job schedules " + problem + ":");
            parallelJobSchedulingWithDeadlinesModel.solveProblemIfFeasible(parallelJobSchedulingWithDeadlinesProblem);

            if (problem != numberOfProblemsToGenerate) {
                StdOut.println();
            }
        }
    }

}
