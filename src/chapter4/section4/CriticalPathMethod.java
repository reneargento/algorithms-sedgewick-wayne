package chapter4.section4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 27/11/17.
 */
public class CriticalPathMethod {

    public static void main(String[] args) {
        int jobs = StdIn.readInt();
        StdIn.readLine();

        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(2 * jobs + 2);

        int source = 2 * jobs;
        int target = 2 * jobs + 1;

        for(int job = 0; job < jobs; job++) {
            String[] jobInformation = StdIn.readLine().split("\\s+");
            double duration = Double.parseDouble(jobInformation[0]);

            edgeWeightedDigraph.addEdge(new DirectedEdge(job, job + jobs, duration));
            edgeWeightedDigraph.addEdge(new DirectedEdge(source, job, 0));
            edgeWeightedDigraph.addEdge(new DirectedEdge(job + jobs, target, 0));

            for(int successors = 1; successors < jobInformation.length; successors++) {
                int successor = Integer.parseInt(jobInformation[successors]);
                edgeWeightedDigraph.addEdge(new DirectedEdge(job + jobs, successor, 0));
            }
        }

        AcyclicLP acyclicLP = new AcyclicLP(edgeWeightedDigraph, source);

        StdOut.println("Start times:");
        for(int job = 0; job < jobs; job++) {
            StdOut.printf("%4d: %5.1f\n", job, acyclicLP.distTo(job));
        }
        StdOut.printf("Finish time: %5.1f\n", acyclicLP.distTo(target));
    }

}
