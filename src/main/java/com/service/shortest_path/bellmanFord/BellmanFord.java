
package com.service.shortest_path.bellmanFord;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.domain.Flight;
import com.service.shortest_path.Edge;
import com.service.shortest_path.Vertex;

public class BellmanFord {

    public static Map<Vertex, Double> d;
    private static Graph<Vertex> graph = null;
    private final static double INFINITY = Double.MAX_VALUE;

    //tworzenie grafu na podstawie lotów
    public void constructGraph(List<Flight> flights) {
        flights.forEach(f -> graph.addEdge(f));
    }

    private void initializeSingleSource(Vertex src) {
        //Inicjalizacja dystansu do wierzchołka źródłowego jako 0
        //oraz wszystkich pozostałych wierzchołków jako INFINITY
        d.put(src, 0.0);
        Set<Vertex> vertices = graph.getVertices();

        Iterator<Vertex> iterator = vertices.iterator();
        iterator.next();
        while (iterator.hasNext()) {
            Vertex next = iterator.next();
            d.put(next, INFINITY);
        }
    }

    //relaksacja krawędzi
    public void doRelax(Vertex u, Vertex v, double w) {
        if (d.get(v) > d.get(u) + w)
            d.put(v, d.get(u) + w);
    }

    //algorytm Bellmana-Forda znajdujący najkrótsze ścieżki od wybranego wierzchołka
    //startowego do wszystkich pozostałych wierzchołków
    public boolean findShortestPath(Vertex src) {
        initializeSingleSource(src);

        //wykonujemy n -1 obiegów pętli, w której dokonujemy relaksacji krawędzi
        Set<Vertex> vertices = graph.getVertices();
        for (int i = 1; i < vertices.size(); i++) {
            for (Vertex v : vertices) {
                List<Edge> adjList = graph.getOutEdges(v);
                if (adjList != null)
                    for (Edge e : adjList) {
                        doRelax(e.getSrc(), e.getTarget(), e.getWeight());
                    }
            }
        }

        //sprawdzenie czy w grafie nie występują cykle ujemne
        for (Vertex vertex : vertices) {
            List<Edge> adjList = graph.getOutEdges(vertex);
            if (adjList != null)
                for (Edge e : adjList) {
                    Vertex u = e.getSrc();
                    Vertex v = e.getTarget();
                    if (d.get(u) > d.get(v) + e.getWeight())
                    return false;
                }
        }

        return true;
    }

}
