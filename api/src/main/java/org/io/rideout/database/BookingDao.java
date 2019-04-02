package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.io.rideout.model.Booking;
import org.io.rideout.model.RideOut;
import org.io.rideout.model.StayOut;
import org.io.rideout.model.TourOut;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

public class BookingDao {
    private static BookingDao ourInstance = new BookingDao();
    public static BookingDao getInstance() {
        return ourInstance;
    }

    private static RideOutDao rideoutDao;

    private BookingDao() {
        rideoutDao = RideOutDao.getInstance();
    }

    public ArrayList<Booking> getAll(ObjectId rideoutId) {
        ArrayList<Booking> result = new ArrayList<>();
        RideOut rideout = rideoutDao.getById(rideoutId);

        if (rideout == null) return null;

        if (rideout instanceof TourOut) {
            TourOut tour = (TourOut) rideout;

            result.addAll(tour.getRestaurantList());
            result.addAll(tour.getAccommodationList());
            result.addAll(tour.getTravelBookings());
        } else if (rideout instanceof StayOut) {
            StayOut stay = (StayOut) rideout;

            result.addAll(stay.getRestaurantList());
            result.addAll(stay.getAccommodationList());
        } else {
            result.addAll(rideout.getRestaurantList());
        }

        return result;
    }

    public Booking getById(ObjectId rideoutId, ObjectId bookingId) {
        ArrayList<Booking> bookings = getAll(rideoutId);
        if (bookings == null) return null;

        for (Booking b : bookings) {
            if (b.getId().equals(bookingId)) {
                return b;
            }
        }

        return null;
    }

    public ArrayList<Booking> getByType(ObjectId rideoutId, String type) {
        ArrayList<Booking> result = new ArrayList<>();
        RideOut rideout = rideoutDao.getById(rideoutId);

        switch (type) {
            case Booking.RESTAURANT:
                result.addAll(rideout.getRestaurantList());
                break;

            case Booking.ACCOMMODATION:
                if (rideout instanceof StayOut) {
                    StayOut stay = (StayOut) rideout;
                    result.addAll(stay.getAccommodationList());
                }
                break;

            case Booking.TRAVEL:
                if (rideout instanceof TourOut) {
                    TourOut tour = (TourOut) rideout;
                    result.addAll(tour.getTravelBookings());
                }
                break;
        }

        return result;
    }

    public Booking insert(ObjectId rideoutId, Booking booking) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(eq("_id", rideoutId), addToSet(getArrayName(booking.getType()), booking));
        return result.getModifiedCount() == 1 ? getById(rideoutId, booking.getId()) : null;
    }

    public Booking update(ObjectId rideoutId, Booking booking) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(and(
                eq("_id", rideoutId), eq(getArrayName(booking.getType()) + "._id", booking.getId())
        ), getUpdateBson(booking));
        return result.getModifiedCount() == 1 ? getById(rideoutId, booking.getId()) : null;
    }

    public Booking delete(ObjectId rideoutId, ObjectId bookingId) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);
        Booking booking = getById(rideoutId, bookingId);
        if (booking == null) return null;

        String arrayName = getArrayName(booking.getType());
        UpdateResult result = collection.updateOne(eq("_id", rideoutId), pull(arrayName, eq("_id", bookingId)));
        return result.getModifiedCount() == 1 ? booking : null;
    }

    private Bson getUpdateBson(Booking booking) {
        String prefix = getArrayName(booking.getType()) + ".$.";

        return combine(
                set(prefix + "_id", booking.getId()),
                set(prefix + "type", booking.getType()),
                set(prefix + "name", booking.getName()),
                set(prefix + "reference", booking.getReference())
        );
    }

    private String getArrayName(String type) {
        switch (type) {
            case Booking.TRAVEL: return "travelBookings";
            case Booking.ACCOMMODATION: return "accommodationList";
            case Booking.RESTAURANT:
            default: return "restaurantList";
        }
    }
}
