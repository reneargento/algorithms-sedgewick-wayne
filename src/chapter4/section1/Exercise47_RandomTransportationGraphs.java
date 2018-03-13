package chapter4.section1;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.In;
import util.Constants;

/**
 * Created by Rene Argento on 12/10/17.
 */
public class Exercise47_RandomTransportationGraphs {

    public class TransportationGraph {

        private SeparateChainingHashTable<String, Integer> vertexNameToIdMap;
        private String[] keys;
        private Exercise37_EuclideanGraphs.EuclideanGraph graph;

        public TransportationGraph(String stream,
                                   SeparateChainingHashTable<String, Exercise37_EuclideanGraphs.EuclideanGraph.Vertex>
                                           metroStationToCoordinateMap) {
            String separator = "-";
            vertexNameToIdMap = new SeparateChainingHashTable<>();

            //First pass
            In in = new In(stream);

            while (in.hasNextLine()) {
                String[] vertices = in.readLine().split(separator);

                for(int i = 0; i < vertices.length; i++) {
                    if (!vertexNameToIdMap.contains(vertices[i])) {
                        vertexNameToIdMap.put(vertices[i], vertexNameToIdMap.size());
                    }
                }
            }

            keys = new String[vertexNameToIdMap.size()];

            for(String vertexName : vertexNameToIdMap.keys()) {
                keys[vertexNameToIdMap.get(vertexName)] = vertexName;
            }

            graph = new Exercise37_EuclideanGraphs().new EuclideanGraph(vertexNameToIdMap.size());
            //Seconds pass
            in = new In(stream);

            while (in.hasNextLine()) {
                String[] vertices = in.readLine().split(separator);

                for(int vertex = 0; vertex < vertices.length - 1; vertex++) {
                    //Since we need an EuclideanGraph, we get the coordinates first
//                    int vertex1 = vertexNameToIdMap.get(vertices[vertex]);
//                    int vertex2 = vertexNameToIdMap.get(vertices[vertex + 1]);

                    Exercise37_EuclideanGraphs.EuclideanGraph.Vertex coordinateVertex1 =
                            metroStationToCoordinateMap.get(vertices[vertex]);
                    Exercise37_EuclideanGraphs.EuclideanGraph.Vertex coordinateVertex2 =
                            metroStationToCoordinateMap.get(vertices[vertex + 1]);

                    graph.addVertex(coordinateVertex1);
                    graph.addVertex(coordinateVertex2);

                    graph.addEdge(coordinateVertex1.id, coordinateVertex2.id);
                }
            }
        }

        public boolean contains(String vertexName) {
            return vertexNameToIdMap.contains(vertexName);
        }

        public int index(String vertexName) {
            return vertexNameToIdMap.get(vertexName);
        }

        public String name(int vertexId) {
            return keys[vertexId];
        }

        public Exercise37_EuclideanGraphs.EuclideanGraph graph() {
            return graph;
        }
    }

    public Exercise37_EuclideanGraphs.EuclideanGraph randomTransportation() {
        // Based on https://parisbytrain.com/wp-content/uploads/2014/01/paris-metro-mini-map-2014.pdf

        /**
         * File contents
         *
         Jasmin-Ranelagh-Boulainvilliers
         Lourmel-Boucicaut-Félix Faure-Commerce
         Pte de Vanves-Plaisance-Pernety-Gaité
         Corvisart-Place d’Italie-Nationale-Chevaleret-Quai de la Gare-Bercy
         Tolbiac-Place d’Italie-Campo Formio-Saint Marcel
         */

        String filePath = Constants.FILES_PATH + Constants.PARIS_METRO_FILE;

        // Used only to generate the vertices
        Exercise37_EuclideanGraphs.EuclideanGraph euclideanGraph = new Exercise37_EuclideanGraphs().new EuclideanGraph(0);

        SeparateChainingHashTable<String, Exercise37_EuclideanGraphs.EuclideanGraph.Vertex> metroStationToCoordinateMap =
                new SeparateChainingHashTable<>();
        metroStationToCoordinateMap.put("Jasmin", euclideanGraph.new Vertex(0, "Jasmin",10.4, 20.4));
        metroStationToCoordinateMap.put("Ranelagh", euclideanGraph.new Vertex(1,"Ranelagh", 10.4, 25.7));
        metroStationToCoordinateMap.put("Boulainvilliers", euclideanGraph.new Vertex(2, "Boulainvilliers",10.4, 32.11));

        metroStationToCoordinateMap.put("Lourmel", euclideanGraph.new Vertex(3, "Lourmel",68.9, 60.4));
        metroStationToCoordinateMap.put("Boucicaut", euclideanGraph.new Vertex(4, "Boucicaut",78.1, 70.2));
        metroStationToCoordinateMap.put("Félix Faure", euclideanGraph.new Vertex(5, "Félix Faure",85.9, 82.4));
        metroStationToCoordinateMap.put("Commerce", euclideanGraph.new Vertex(6, "Commerce",85.9, 91.4));

        metroStationToCoordinateMap.put("Pte de Vanves", euclideanGraph.new Vertex(7, "Pte de Vanves",44.9, 23.4));
        metroStationToCoordinateMap.put("Plaisance", euclideanGraph.new Vertex(8, "Plaisance",70.1, 19.1));
        metroStationToCoordinateMap.put("Pernety", euclideanGraph.new Vertex(9, "Pernety",89.2, 15.4));
        metroStationToCoordinateMap.put("Gaité", euclideanGraph.new Vertex(10, "Gaité",105.9, 13.9));

        metroStationToCoordinateMap.put("Corvisart", euclideanGraph.new Vertex(11, "Corvisart",-10.9, 66.9));
        metroStationToCoordinateMap.put("Place d’Italie", euclideanGraph.new Vertex(12, "Place d’Italie",10.3, 78.9));
        metroStationToCoordinateMap.put("Nationale", euclideanGraph.new Vertex(13, "Nationale",40.9, 80.2));
        metroStationToCoordinateMap.put("Chevaleret", euclideanGraph.new Vertex(14, "Chevaleret",46.2, 90.9));
        metroStationToCoordinateMap.put("Quai de la Gare", euclideanGraph.new Vertex(15, "Quai de la Gare",52.9, 101.0));
        metroStationToCoordinateMap.put("Bercy", euclideanGraph.new Vertex(16, "Bercy",62.9, 111.2));

        metroStationToCoordinateMap.put("Tolbiac", euclideanGraph.new Vertex(17, "Tolbiac",10.3, 60.9));
        metroStationToCoordinateMap.put("Campo Formio", euclideanGraph.new Vertex(18, "Campo Formio",10.9, 93.2));
        metroStationToCoordinateMap.put("Saint Marcel", euclideanGraph.new Vertex(19, "Saint Marcel",18.9, 103.2));

        TransportationGraph transportationGraph = new TransportationGraph(filePath, metroStationToCoordinateMap);
        return transportationGraph.graph;
    }

    public static void main(String[] args) {
        Exercise37_EuclideanGraphs.EuclideanGraph euclideanGraph =
                new Exercise47_RandomTransportationGraphs().randomTransportation();
        euclideanGraph.show(-20, 120, 0, 120, 0.5);
    }

}
