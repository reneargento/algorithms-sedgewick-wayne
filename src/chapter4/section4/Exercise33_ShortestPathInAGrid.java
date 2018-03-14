package chapter4.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 10/12/17.
 */
public class Exercise33_ShortestPathInAGrid {

    public Iterable<DirectedEdge> shortestPathInGridAll4Directions(double[][] matrix) {
        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(matrix.length * matrix[0].length);

        for(int row = 0; row < matrix.length; row++) {
            for(int column = 0; column < matrix[0].length; column++) {

                int currentCellIndex = getCellIndex(matrix, row, column);

                int[] neighborRows = {-1, 0, 0, 1};
                int[] neighborColumns = {0, -1, 1, 0};

                for(int i = 0; i < neighborRows.length; i++) {
                    int neighborRow = row + neighborRows[i];
                    int neighborColumn = column + neighborColumns[i];

                    if (isValidCell(matrix, neighborRow, neighborColumn)) {
                        int neighborCellIndex = getCellIndex(matrix, neighborRow, neighborColumn);
                        edgeWeightedDigraph.addEdge(new DirectedEdge(currentCellIndex, neighborCellIndex,
                                matrix[neighborRow][neighborColumn]));
                    }
                }
            }
        }

        int targetCell = matrix.length * matrix.length - 1;

        DijkstraSP dijkstraSP = new DijkstraSP(edgeWeightedDigraph, 0);
        return dijkstraSP.pathTo(targetCell);
    }

    public Iterable<DirectedEdge> shortestPathInGridOnlyRightOrDown(double[][] matrix) {
        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(matrix.length * matrix[0].length);

        for(int row = 0; row < matrix.length; row++) {
            for(int column = 0; column < matrix[0].length; column++) {

                int currentCellIndex = getCellIndex(matrix, row, column);

                int[] neighborRows = {0, 1};
                int[] neighborColumns = {1, 0};

                for(int i = 0; i < neighborRows.length; i++) {
                    int neighborRow = row + neighborRows[i];
                    int neighborColumn = column + neighborColumns[i];

                    if (isValidCell(matrix, neighborRow, neighborColumn)) {
                        int neighborCellIndex = getCellIndex(matrix, neighborRow, neighborColumn);
                        edgeWeightedDigraph.addEdge(new DirectedEdge(currentCellIndex, neighborCellIndex,
                                matrix[neighborRow][neighborColumn]));
                    }
                }
            }
        }

        int targetCell = matrix.length * matrix.length - 1;

        DijkstraSP dijkstraSP = new DijkstraSP(edgeWeightedDigraph, 0);
        return dijkstraSP.pathTo(targetCell);
    }

    private boolean isValidCell(double[][] matrix, int row, int column) {
        return row >= 0 && row < matrix.length && column >= 0 && column < matrix[0].length;
    }

    private int getCellIndex(double[][] matrix, int row, int column) {
        return matrix.length * row + column;
    }

    public static void main(String[] args) {
        Exercise33_ShortestPathInAGrid shortestPathInAGrid = new Exercise33_ShortestPathInAGrid();

        StdOut.println("Moving either up, down, left or right:");

        double[][] matrix1 = {
                {0, 1},
                {3, 1}
        };

        StdOut.print("Path:     ");

        Iterable<DirectedEdge> shortestPath1 = shortestPathInAGrid.shortestPathInGridAll4Directions(matrix1);
        for(DirectedEdge edge : shortestPath1) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected: 0->1 1->3");

        double[][] matrix2 = {
                {0, 2, 1},
                {1, 3, 2},
                {4, 2, 5}
        };

        StdOut.print("\nPath:     ");

        Iterable<DirectedEdge> shortestPath2 = shortestPathInAGrid.shortestPathInGridAll4Directions(matrix2);
        for(DirectedEdge edge : shortestPath2) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected: 0->1 1->2 2->5 5->8");

        double[][] matrix3 = {
                {0,   4, 10, 10, 10},
                {1,   8,  1,  1,  1},
                {1,   8,  1, 10,  1},
                {1,   1,  1, 10,  1},
                {10, 10, 10, 10,  2}
        };

        StdOut.print("\nPath:     ");

        Iterable<DirectedEdge> shortestPath3 = shortestPathInAGrid.shortestPathInGridAll4Directions(matrix3);
        for(DirectedEdge edge : shortestPath3) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected: 0->5 5->10 10->15 15->16 16->17 17->12 12->7 7->8 8->9 9->14 14->19 19->24");

        StdOut.println("\nMoving only right and down:");

        StdOut.print("Path:     ");

        Iterable<DirectedEdge> shortestPath4 = shortestPathInAGrid.shortestPathInGridOnlyRightOrDown(matrix3);
        for(DirectedEdge edge : shortestPath4) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }
        StdOut.println("\nExpected: 0->5 5->6 6->7 7->8 8->9 9->14 14->19 19->24");
    }

}
