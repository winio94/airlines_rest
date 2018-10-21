package com.service.shortest_path;

import java.util.List;

import com.domain.Flight;

public class FlightVertex implements Comparable<FlightVertex>{
    private final Flight flight;
    private final Vertex vertex;
    public final List<Edge> neighbours;
    public final double minDistance;
    public final List<FlightVertex> path;

    public FlightVertex(Flight flight, Vertex vertex) {
        this.flight = flight;
        this.vertex = vertex;
        this.neighbours = vertex.getNeighbours();
        this.minDistance = vertex.getMinDistance();
        this.path = vertex.getPath();
    }

    public Flight getFlight() {
        return flight;
    }

    public Vertex getVertex() {
        return vertex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlightVertex that = (FlightVertex) o;

        return getVertex().equals(that.getVertex());
    }

    @Override
    public int hashCode() {
        return getVertex().hashCode();
    }

    @Override
    public int compareTo(FlightVertex o) {
        return this.vertex.compareTo(o.getVertex());
    }
}
