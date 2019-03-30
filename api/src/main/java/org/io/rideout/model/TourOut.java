package org.io.rideout.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

@BsonDiscriminator
@JsonTypeName("Tour")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "rideoutType")
public class TourOut extends StayOut {

    private ArrayList<Booking> travelBookings = new ArrayList<>();

    public TourOut() {
        super();
    }

    public TourOut(ObjectId id, String name, Date dateStart, Date dateEnd, int maxRiders, SimpleUser leadRider, String route, Date minCancellationDate) {
        super(id, name, dateStart, dateEnd, maxRiders, leadRider, route, minCancellationDate);
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
