package org.io.rideout.model;

import java.util.ArrayList;
import java.util.Date;

public class StayOut extends RideOut {

    private ArrayList<Booking> accommodationList;
    private ArrayList<Booking> restaurantList;

    public StayOut() {
        super();

        this.accommodationList = new ArrayList<>();
        this.restaurantList = new ArrayList<>();
    }

    public StayOut(String id,String name, Date dateStart,Date dateEnd,int maxRiders,String leadRider, String route, Date minCancellationDate, ArrayList<Booking> accommodation, ArrayList<Booking> restaurant) {
        super(id, name, dateStart, dateEnd, maxRiders, leadRider, route, minCancellationDate);
        this.accommodationList = accommodation;
        this.restaurantList = restaurant;
    }

    public ArrayList<Booking> getAccommodationList() {
        return accommodationList;
    }

    public void setAccommodationList(ArrayList<Booking> accommodationList) {
        this.accommodationList = accommodationList;
    }

    public ArrayList<Booking> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(ArrayList<Booking> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public void addRestaurant(Booking restaurant) {
        this.restaurantList.add(restaurant);
    }

    public void addAccommodation(Booking accommodation) {
        this.accommodationList.add(accommodation);
    }
}
