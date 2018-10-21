package com.service.flight;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StopWatch;

import com.domain.Airport;
import com.domain.Flight;
import com.repository.AirportRepository;
import com.service.shortest_path.Dijkstra;
import com.service.shortest_path.DistanceCalculator;
import com.service.shortest_path.Graph;
import com.service.shortest_path.Vertex;
import com.service.shortest_path.bellmanFord.BellmanFord;
import com.service.shortest_path.floyd.FloydWarshall;

/**
 * Created by Michał on 2016-12-31.
 */
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final DistanceCalculator distanceCalculator;
    private final AirportRepository airportReporitory;

    public FlightServiceImpl(FlightRepository flightRepository,
                             DistanceCalculator distanceCalculator,
                             AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.distanceCalculator = distanceCalculator;
        this.airportReporitory = airportRepository;
    }

    @Override
    public Flight create(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public List<Flight> findFlightsByCitiesAndDate(String from, String to, LocalDateTime departureDate) {
//        dijkstra(from, to, departureDate);

        BellmanFord bellmanFord = new BellmanFord();
        FloydWarshall floydWarshall = new FloydWarshall();
        List<Flight> flightsByDate = flightRepository.findFlightByDepartureDateBetween(departureDate, departureDate.plusDays(1));

        bellmanFord.constructGraph(flightsByDate);
        boolean shortestPath = bellmanFord.findShortestPath(new Vertex(from));

//        floydWarshall.constructGraph(flightsByDate);
        floydWarshall.findShortestPath();
        return new ArrayList<>();
    }

    public void dijkstra(String from, String to, LocalDateTime departureDate) {
        Graph graph = buildGraph(departureDate);

        Dijkstra dijkstra = new Dijkstra();
        Airport source = airportReporitory.findByName(from);
        Airport destination = airportReporitory.findByName(to);
        calculateDijkstra(graph, dijkstra, source);

        dijkstra.printCalculatedGraph(graph, destination);
    }

    public void calculateDijkstra(Graph graph, Dijkstra dijkstra, Airport source) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        dijkstra.calculate(graph.getVertex(source.getAirportId()));
        stopWatch.stop();
        System.out.println(String.format("Calculating Dijkstra took %s  milis", stopWatch.getTotalTimeMillis()));
    }

    //budowanie grafu
    private Graph buildGraph(LocalDateTime departureDate) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //znajdź wszystkie loty w zadanym dniu
        List<Flight> flightsByDate = flightRepository.findFlightByDepartureDateBetween(departureDate, departureDate.plusDays(1));
        //mapowanie lotów na listę lotnisk
        List<Airport> airports = flightsByDate.stream()
                                              .map(f -> Arrays.asList(f.getFrom(), f.getTo()))
                                              .flatMap(List::stream)
                                              .collect(Collectors.toList());
        //inizjalizacja wierzchołków
        Graph graph = new Graph(airports);
        //tworzenie krawędzi dla znalezionych lotów
        flightsByDate.forEach(graph::addEdge);
        stopWatch.stop();
        System.out.println(String.format("Building graph for %s flights took %s milis", flightsByDate.size(), stopWatch.getTotalTimeMillis()));
        return graph;
    }


    public long getWeight(Flight f) {
        return distanceCalculator.distance(f.getFrom().getLatitude(), f.getTo().getLatitude(), f.getFrom().getLongitude(), f.getTo().getLongitude());
    }

    public List<Flight> findFlightsByDate(LocalDateTime departureDate) {
        //Znajdź loty
        return flightRepository.findFlightByDepartureDateBetween(departureDate, departureDate.plusDays(1));
//        Predicate<Flight> atTheSameDay = flight -> flight.getDepartureDate().toLocalDate().equals(departureDate.toLocalDate());
//        return flightsByCity.stream().filter(atTheSameDay).collect(Collectors.toList());
    }
}