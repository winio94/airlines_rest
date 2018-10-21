package com.service.shortest_path;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.domain.Airport;
import com.domain.Route;

public class DataReader {

    private static final DistanceCalculator distanceCalculator = new DistanceCalculator();
    public static void main(String[] args) throws URISyntaxException, IOException, ClassNotFoundException {
        saveAirportAndRoutes();
        Graph graph = buildGraph();
//        calculatePathTo(679, graph);
    }

    public static void calculatePathTo(int destinationAirportId, Graph graph) {
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.calculate(graph.getVertex(destinationAirportId));
        dijkstra.printCalculatedGraph(graph);
    }

    public static void saveAirportAndRoutes() throws IOException, URISyntaxException {
        List<Airport> airports = readAirports();
        Map<Integer, Airport> airportsMap = airports.stream().collect(Collectors.toMap(Airport::getAirportId, Function.identity()));
        List<Route> routes = readRoutes(airportsMap);
        List<Route> filteredRoutes = routes.stream()
                                           .filter(route -> {
                                               int sourceAirportId = route.getSourceAirportId();
                                               int destinationAirportId = route.getDestinationAirportId();
                                               return airportsMap.containsKey(sourceAirportId) && airportsMap.containsKey(destinationAirportId);
                                           }).collect(Collectors.toList());

        saveList(airports, "all_airports.ser");
        saveList(filteredRoutes,"all_Routes.ser");
    }

    public static Graph buildGraph() throws IOException, ClassNotFoundException {
        List<Airport> airports = readObjects(DataReader.class.getClassLoader().getResource("all_airports.ser").getPath());
        List<Route> routes = readObjects(DataReader.class.getClassLoader().getResource("all_Routes.ser").getPath());

        Graph graph = new Graph(airports);
        routes.forEach(route -> {
            graph.addEdge(route.getSourceAirportId(), route.getDestinationAirportId(), route.getDistance());
        });
        return graph;
    }

    public static <T> List<T> readObjects(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream streamIn = new FileInputStream(fileName);
        try (ObjectInputStream objectinputstream = new ObjectInputStream(streamIn)) {
            return (List<T>) objectinputstream.readObject();
        }
    }

    private static List<Route> readRoutes(Map<Integer, Airport> airportsMap) throws IOException, URISyntaxException {
        Stream<String> routes = Files.lines(Paths.get(DataReader.class.getClassLoader().getResource("routes.txt").toURI()));
        return routes.map(s -> s.split(","))
                     .map(array -> {
                         int airlineId = Integer.parseInt(array[0]);
                         int sourceAirportId = Integer.parseInt(array[1]);
                         int destinationAirportId = Integer.parseInt(array[2]);
                         long distance = calculateDistanceBetween(sourceAirportId, destinationAirportId, airportsMap);
                         return new Route(airlineId, sourceAirportId, destinationAirportId, distance);
                     }).collect(Collectors.toList());
    }

    private static long calculateDistanceBetween(int sourceAirportId, int destinationAirportId, Map<Integer, Airport> airportsMap) {
        Airport src = airportsMap.get(sourceAirportId);
        Airport dest = airportsMap.get(destinationAirportId);
        if(Objects.isNull(src) || Objects.isNull(dest)) {
            return -1L;
        }
        return distanceCalculator.distance(src.getLatitude(), dest.getLatitude(), src.getLongitude(), dest.getLongitude());
    }

    private static List<Airport> readAirports() throws IOException, URISyntaxException {
        Stream<String> polishAirports = Files.lines(Paths.get(DataReader.class.getClassLoader().getResource("zeszyt5.txt").toURI()));
        return polishAirports.map(s -> s.split(","))
                             .map(array -> {
                                 int airportId = Integer.parseInt(array[0]);
                                 String name = array[1];
                                 String city = array[2];
                                 String country = array[3];
                                 double longitude = Double.parseDouble(array[4]);
                                 double latitude = Double.parseDouble(array[5]);
                                 return new Airport(airportId, name, city, country, latitude, longitude);
                             }).collect(Collectors.toList());
    }

    private static <T> void saveList(List<T> objects, String fileName) throws IOException, URISyntaxException {
        FileOutputStream fout = new FileOutputStream(fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(fout)) {
            oos.writeObject(objects);
        }
    }
}
