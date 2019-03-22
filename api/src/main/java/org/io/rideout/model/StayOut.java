package org.io.rideout.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

@BsonDiscriminator
@JsonTypeName("Stay")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "rideoutType")
@JsonSubTypes({ @JsonSubTypes.Type(value = TourOut.class) })
public class StayOut extends RideOut {

    private ArrayList<Booking> accommodationList = new ArrayList<>();
    private ArrayList<Booking> restaurantList = new ArrayList<>();

    public StayOut() {
        super();
    }

    public StayOut(ObjectId id, String name, Date dateStart, Date dateEnd, int maxRiders, String leadRider, String route, Date minCancellationDate) {
        super(id, name, dateStart, dateEnd, maxRiders, leadRider, route, minCancellationDate);
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
