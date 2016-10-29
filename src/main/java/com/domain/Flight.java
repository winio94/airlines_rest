package com.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Michał on 2016-10-02.
 */
@Entity
public class Flight {

    @Id
    @GeneratedValue
    private Long id;

    private String flightNumber;
    private Date departureDate;
    private Date arrivalDate;

    @Enumerated(EnumType.STRING)
    private FLightClass fLightClass;

    @OneToOne
    private Airport from;

    @OneToOne
    private Airport to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public FLightClass getfLightClass() {
        return fLightClass;
    }

    public void setfLightClass(FLightClass fLightClass) {
        this.fLightClass = fLightClass;
    }

    public Airport getFrom() {
        return from;
    }

    public void setFrom(Airport from) {
        this.from = from;
    }

    public Airport getTo() {
        return to;
    }

    public void setTo(Airport to) {
        this.to = to;
    }
}
