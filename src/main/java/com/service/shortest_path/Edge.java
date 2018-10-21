package com.service.shortest_path;

import com.domain.Flight;

public class Edge {
    private Flight flight;
    private final Vertex src;
    private final Vertex target;
    private final double weight;

    public Edge(Flight flight, Vertex src, Vertex target, double weight) {
        this.flight = flight;
        this.src = src;
        this.target = target;
        this.weight = weight;
    }

    public Vertex getSrc() {
        return src;
    }

    public Vertex getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
