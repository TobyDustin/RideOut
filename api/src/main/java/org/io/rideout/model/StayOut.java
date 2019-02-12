package org.io.rideout.model;

import java.util.ArrayList;
import java.util.Date;

public class StayOut extends RideOut {

    private ArrayList<Booking> accommodation;
    private ArrayList<Booking> restaurant;

    public StayOut() {
        super();

        this.accommodation = new ArrayList<>();
        this.restaurant = new ArrayList<>();
    }

    public StayOut(String id,String name, Date dateStart,Date dateEnd,int maxRiders,String leadRider, String route, Date minCancellationDate, ArrayList<Booking> accommodation, ArrayList<Booking> restaurant) {
        super(id, name, dateStart, dateEnd, maxRiders, leadRider, route, minCancellationDate);
        this.accommodation = accommodation;
        this.restaurant = restaurant;
    }

    public ArrayList<Booking> getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(ArrayList<Booking> accommodation) {
        this.accommodation = accommodation;
    }

    public ArrayList<Booking> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(ArrayList<Booking> restaurant) {
        this.restaurant = restaurant;
    }
}
