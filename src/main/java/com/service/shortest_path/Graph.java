package com.service.shortest_path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domain.Airport;
import com.domain.Flight;

public class Graph {
    public Map<Integer, Vertex> vertices;
    private final DistanceCalculator distanceCalculator = new DistanceCalculator();

    public Graph(List<Airport> airports) {
        int airportsSize = airports.size();
        vertices = new HashMap<>(airportsSize);
        airports.forEach(airport ->
                //dodawanie wpisu do mapy, w której kluczem jest
                vertices.put(airport.getAirportId(), new Vertex(airport.getCity()))
        );
    }

    public void addEdge(Flight f) {
        //lotnisko, z którego lot się rozpoczyna
        Airport from = f.getFrom();
        //lotnisko, na którym lot się kończy
        Airport to = f.getTo();
        int src = from.getAirportId();
        int dest = to.getAirportId();
        //wierzchołek grafu reprezentujący lotnisko początkowe
        Vertex start = vertices.get(src);
        //koszt przelotu
        long weight = getWeight(f);
        Edge newEdge = new Edge(f, vertices.get(src), vertices.get(dest), weight);
        //dodawanie nowej krawędzi do listy sąsiedztwa wierzchołka
        start.getNeighbours().add(newEdge);
    }

    public Edge create(Flight f) {
        //lotnisko, z którego lot się rozpoczyna
        Airport from = f.getFrom();
        //lotnisko, na którym lot się kończy
        Airport to = f.getTo();
        int src = from.getAirportId();
        int dest = to.getAirportId();
        //wierzchołek grafu reprezentujący lotnisko początkowe
        Vertex start = vertices.get(src);
        //koszt przelotu
        long weight = getWeight(f);
        return new Edge(f, vertices.get(src), vertices.get(dest), weight);
        //dodawanie nowej krawędzi do listy sąsiedztwa wierzchołka
    }

    public long getWeight(Flight f) {
        //szerokość geograficzna lotniska początkowego
        double sourceLatitude = f.getFrom().getLatitude();
        //szerokość geograficzna lotniska końcowego
        double destinationLatitude = f.getTo().getLatitude();
        double sourceLongitude = f.getFrom().getLongitude();
        double destinationLongitude = f.getTo().getLongitude();
        //obliczanie dystansu na podstawie współrzędnych
        return distanceCalculator.distance(sourceLatitude, destinationLatitude, sourceLongitude, destinationLongitude);
    }

    public Map<Integer, Vertex> getVertices() {
        return vertices;
    }

    public Vertex getVertex(int vert) {
        return vertices.get(vert);
    }

    public void addEdge(int src, int dest, double weight) {
        Vertex start = vertices.get(src);
        Edge new_edge = new Edge(null, vertices.get(src), vertices.get(dest), weight);
        start.getNeighbours().add(new_edge);
    }
}
