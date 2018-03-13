package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 07/10/17.
 */
@SuppressWarnings("unchecked")
public class Exercise39_ImageProcessing {

    //Flood fill to count the number of components in an image
    private int floodFill(int[][] image) {
        Graph graph = new Graph(image.length * image[0].length);

        int[] neighborRows = {-1, 1, 0 ,0};
        int[] neighborColumns = {0, 0, -1 ,1};

        for(int row = 0; row < image.length; row++) {
            for(int column = 0; column < image[0].length; column++) {
                for(int i = 0; i < 4; i++) {
                    int neighborRow = row + neighborRows[i];
                    int neighborColumn = column + neighborColumns[i];

                    if (isValidCell(image, neighborRow, neighborColumn)) {
                        if (image[row][column] == image[neighborRow][neighborColumn]) {
                            int vertexId1 = getCellIndex(row, column, image[0].length);
                            int vertexId2 = getCellIndex(neighborRow, neighborColumn, image[0].length);

                            //Used to avoid connecting vertices more than once
                            if (vertexId1 < vertexId2) {
                                graph.addEdge(vertexId1, vertexId2);
                            }
                        }
                    }
                }
            }
        }

        int components = 0;
        boolean[] visited = new boolean[graph.vertices()];

        for(int vertex = 0; vertex < graph.vertices(); vertex++) {
            if (!visited[vertex]) {
                components++;
                depthFirstSearch(graph, vertex, visited);
            }
        }

        return components;
    }

    private void depthFirstSearch(Graph graph, int currentVertex, boolean[] visited) {
        visited[currentVertex] = true;

        for(int neighbor : graph.adjacent(currentVertex)) {
            if (!visited[neighbor]) {
                depthFirstSearch(graph, neighbor, visited);
            }
        }
    }

    private boolean isValidCell(int[][] matrix, int row, int column) {
        return row >= 0 && row < matrix.length && column >= 0 && column < matrix[0].length;
    }

    private int getCellIndex(int row, int column, int columns) {
        return row * columns + column;
    }

    public static void main(String[] args) {
        Exercise39_ImageProcessing imageProcessing = new Exercise39_ImageProcessing();

        //Image (numbers are colors)
        int[][] image = {
                {1, 1, 1, 3},
                {1, 1, 2, 2},
                {4, 4, 5, 5}
        };

        int numberOfComponents = imageProcessing.floodFill(image);
        StdOut.println("Number of components: " + numberOfComponents + " Expected: 5");
    }

}
