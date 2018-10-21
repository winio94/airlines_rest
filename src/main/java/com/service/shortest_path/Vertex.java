package com.service.shortest_path;

import static java.lang.Double.POSITIVE_INFINITY;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//klasa reprezentująca wierzchołek w grafie
public class Vertex implements Comparable<Vertex> {
    private final String name;                      //nazwa wierzchołka
    private List<Edge> neighbours;                  //sąsiedzi wierzchołka
    private List<FlightVertex> path;                //ścieżka do wierzchołka
    private double minDistance = POSITIVE_INFINITY; //minimalny koszt drogi

    //konstruktor tworzący nowy wierzchołek
    public Vertex(String name) {
        this.name = name;
        neighbours = new ArrayList<>();
        path = new LinkedList<>();
    }

    //metoda porównująca dwa wierzchołki na podstawie minimalnego kosztu
    @Override
    public int compareTo(Vertex other) {
        return Double.compare(minDistance, other.getMinDistance());
    }

    //metoda służąca do odróżnienia dwóch wierzchołków znajdujących się w tej samej kolekcji
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return name.equals(vertex.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public List<Edge> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Edge> neighbours) {
        this.neighbours = neighbours;
    }

    //...





    public List<FlightVertex> getPath() {
        return path;
    }

    public void setPath(List<FlightVertex> path) {
        this.path = path;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    @Override
    public String toString() {
        return name;
    }
}
