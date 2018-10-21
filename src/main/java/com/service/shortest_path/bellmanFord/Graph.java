package com.service.shortest_path.bellmanFord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.domain.Flight;
import com.service.shortest_path.DistanceCalculator;
import com.service.shortest_path.Edge;
import com.service.shortest_path.Vertex;

public class Graph<T extends Vertex> {

    private static Map<Vertex, List<Edge>> adjacencyVertexMap = null;
    private final DistanceCalculator distanceCalculator;

    public Graph() {
        adjacencyVertexMap = new HashMap<>();
        distanceCalculator = new DistanceCalculator();
    }

    /**
     * @param f
     */
    //dodawanie krawędzi do listy sąsiedztwa
    public void addEdge(Flight f) {
        Vertex u = new Vertex(f.getFrom().getCity());
        Vertex v = new Vertex(f.getTo().getCity());
        List<Edge> adjacencyList = adjacencyVertexMap.get(u);
        if (adjacencyList == null)
            adjacencyList = new ArrayList<Edge>();
        adjacencyList.add(new Edge(f, u, v, getWeight(f)));
        adjacencyVertexMap.put(u, adjacencyList);
    }

    public long getWeight(Flight f) {
        return distanceCalculator.distance(f.getFrom().getLatitude(), f.getTo().getLatitude(), f.getFrom().getLongitude(), f.getTo().getLongitude());
    }

    //zwracania krawędzi biegnących od danego wierzchołka
    public List<Edge> getOutEdges(Vertex u) {
        return adjacencyVertexMap.get(u);
    }

    //drukowanie najkrótszych ścieżek
    public void printGraph() {
        adjacencyVertexMap.forEach((v, edges) -> {
            for (Edge edge : edges)
                System.out.println(edge.getFlight());
        });
    }

    public Set<Vertex> getVertices() {
        return adjacencyVertexMap.keySet();
    }
}
