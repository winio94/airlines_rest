package com.service.shortest_path;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

import com.domain.Airport;

public class Dijkstra {

    /**
     * metoda wyswietlająca najkrótsze możliwe ścieżki do wierzchołka @destination
     */
    public void printCalculatedGraph(Graph g, Airport destination) {
        for (Vertex v : g.getVertices().values()) {
            List<FlightVertex> path = v.getPath();
            //wyświetl najkrótszą ścieżkę jedynie dla wierzchołków które
            //są powiązane z lotniskiem @destination
            if (destination.getCity().equals(v.getName())) {
                double minDistance = v.getMinDistance();
                if (Double.isFinite(minDistance)) {
                    System.out.println("Vertex - " + v + " , Dist - " + minDistance);
                    //wyświetl loty prowadzące do lotniska @destination
                    for (FlightVertex pathvert : path) {
                        System.out.println(pathvert.getFlight());
                    }
                    System.out.println();
                }
            }
        }
    }

    /**
     * metoda obliczająca najkrótsze ścieżki od wierzchołka @source do wszystkich pozostałych
     */
    public void calculate(Vertex source) {

        //inicjalizacja danych
        source.setMinDistance(0);
        PriorityQueue<FlightVertex> queue = new PriorityQueue<>();
        queue.add(new FlightVertex(null, source));

        while (!queue.isEmpty()) {

            //Struktura danych reprezentująca wierzchołek, posiadająca dodatkowe informacje o locie
            FlightVertex u = queue.poll();

            //dla wszystkich sąsiadów wierzchołka sprawszamy czy nie istnieje krótsza ścieżka
            for (Edge neighbour : u.neighbours) {
                Double newDist = u.minDistance + neighbour.getWeight();

                Vertex neighbourTarget = neighbour.getTarget();
                //jeśli istnieje krótsza droga do sąsiada to zmieniamy wartość minDistance
                if (neighbourTarget.getMinDistance() > newDist) {
                    // Usuwamy lot z kolejki
                    queue.remove(new FlightVertex(null, neighbourTarget));
                    neighbourTarget.setMinDistance(newDist);

                    // Dodajemy nowy wierzchołek do już odwiedzonej ścieżki
                    neighbourTarget.setPath(new LinkedList<>(u.path));

                    FlightVertex newFlightOnPath = new FlightVertex(neighbour.getFlight(), u.getVertex());
                    List<FlightVertex> neighbourPath = neighbourTarget.getPath();

                    if (Objects.nonNull(neighbourPath) && neighbourPath.isEmpty()) {
                        neighbourTarget.getPath().add(newFlightOnPath);
                    } else {
                        //sprawdzamy czy nowo znaleziony wierzchołek jest powiązany z lotem o późniejszym czasie startu
                        //od czasu lądowania ostatniego lotu na już odwiedzonej ścieżce
                        FlightVertex lastOnPath = neighbourPath.get(neighbourPath.size() - 1);
                        if (lastOnPath.getFlight().getArrivalDate().isBefore(newFlightOnPath.getFlight().getDepartureDate())) {
                            neighbourTarget.getPath().add(newFlightOnPath);
                        }
                    }
                    //Dodajemy lot o krótszej drodze
                    queue.add(new FlightVertex(neighbour.getFlight(), neighbourTarget));
                }
            }
        }
    }

    public void printCalculatedGraph(Graph g) {
//        for (Vertex v : g.getVertices().values()) {
//            List<FlightVertex> path = v.path;
////            if (destination.equals(v.name) || path.contains(destination)) {
//            double minDistance = v.minDistance;
//            if (Double.isFinite(minDistance)) {
//                System.out.println("Vertex - " + v + " , Dist - " + minDistance + " , Path:");
//                for (FlightVertex pathvert : path) {
//                    System.out.print(pathvert.getFlight() + " ");
//                }
//                System.out.println("" + v);
////                }
//            }
//        }
    }
}

