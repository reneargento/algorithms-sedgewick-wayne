package chapter6.networkflow;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 29/09/18.
 */
// The data source is a file containing data about the 500 busiest commercial airports in the United States in 2002.

// Each row in the file has the format:
// Airport1 Airport2 NumberOfSeats
// Where Airport1 and Airport2 describe the airport IDs and NumberOfSeats describes the total number of seats
// available in the flights between Airport1 and Airport2 in 2002.
// The NumberOfSeats represents the capacity of the number of people that could be transported between the two given
// airports in 2002 in the network.

// A max flow algorithm in this network could compute the maximum number of people that could move from a source
// airport to a sink airport in 2002.

// The data file can be downloaded here: http://opsahl.co.uk/tnet/datasets/USairport500.txt
// More information about the data: https://toreopsahl.com/datasets/#usairports
public class Exercise40 {

    public FlowNetwork getRealWorldFlowNetwork() {
        String filePath = Constants.FILES_PATH + Constants.US_FLIGHTS_2002_FILE;
        String separator = " ";
        int numberOfAirports = 500;

        FlowNetwork flowNetwork = new FlowNetwork(numberOfAirports);

        In in = new In(filePath);

        while (in.hasNextLine()) {
            String[] information = in.readLine().split(separator);

            // Subtract 1 because vertices in the file are 1-index based
            int airportId1 = Integer.parseInt(information[0]) - 1;
            int airportId2 = Integer.parseInt(information[1]) - 1;
            int capacity = Integer.parseInt(information[2]);

            flowNetwork.addEdge(new FlowEdge(airportId1, airportId2, capacity));
        }

        return flowNetwork;
    }

    public static void main(String[] args) {
        FlowNetwork flowNetwork = new Exercise40().getRealWorldFlowNetwork();

        StdOut.println("Flow Network");
        StdOut.println("Vertices: " + flowNetwork.vertices());
        StdOut.println("Edges: " + flowNetwork.edgesCount());

        StdOut.println();
        StdOut.println(flowNetwork);

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, 499);
        StdOut.println("Max number of people that could be transported from airport 1 to airport 500: "
                + (int) fordFulkerson.maxFlowValue());
    }

}
