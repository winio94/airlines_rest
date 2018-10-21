package com.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Route implements Serializable {

    private static final long serialVersionUID = 1113799434508676095L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int airlineId;
    private int sourceAirportId;
    private int destinationAirportId;
    private long distance;

    public Route() {
    }

    public Route(int airlineId, int sourceAirportId, int destinationAirportId, long distance) {
        this.airlineId = airlineId;
        this.sourceAirportId = sourceAirportId;
        this.destinationAirportId = destinationAirportId;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAirlineId() {
        return airlineId;
    }

    public int getSourceAirportId() {
        return sourceAirportId;
    }

    public int getDestinationAirportId() {
        return destinationAirportId;
    }

    public long getDistance() {
        return distance;
    }
}