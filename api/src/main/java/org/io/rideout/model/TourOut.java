package org.io.rideout.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.Date;

@JsonTypeName("Tour")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "rideoutType")
public class TourOut extends StayOut {

    private ArrayList<Booking> travelBookings;

    public TourOut() {
        super();
    }

    public TourOut(String id, String name, Date dateStart, Date dateEnd, int maxRiders, String leadRider, String route, Date minCancellationDate) {
        super(id, name, dateStart, dateEnd, maxRiders, leadRider, route, minCancellationDate);
        this.travelBookings = new ArrayList<>();
    }

    public ArrayList<Booking> getTravelBookings() {
        return travelBookings;
    }

    public void setTravelBookings(ArrayList<Booking> travelBookings) {
        this.travelBookings = travelBookings;
    }

    public void addTravelBooking(Booking booking) {
        this.travelBookings.add(booking);
    }
}
