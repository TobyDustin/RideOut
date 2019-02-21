package org.io.rideout.resource;

import org.bson.types.ObjectId;
import org.io.rideout.model.Booking;
import org.io.rideout.model.RideOut;
import org.io.rideout.model.StayOut;
import org.io.rideout.model.TourOut;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;

@Path("rideout")
public class RideOutResource {

    public static ObjectId ID_12345 = new ObjectId("5c6a958ac724414cb18a412c");
    public static ObjectId ID_23456 = new ObjectId("5c6a958ac724414cb18a412d");
    public static ObjectId ID_34567 = new ObjectId("5c6a958ac724414cb18a4130");
    public static ObjectId BID_1234 = new ObjectId("5c6a958ac724414cb18a412e");
    public static ObjectId BID_4321 = new ObjectId("5c6a958ac724414cb18a412f");
    public static ObjectId BID_9876 = new ObjectId("5c6a958ac724414cb18a4131");

    // GET all ride outs
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<RideOut> getAllRideOuts() {
        ArrayList<RideOut> result = new ArrayList<>();

        RideOut dummyRideOut = getDummyRideOut();
        StayOut dummyStayOut = getDummyStayOut();

        result.add(dummyRideOut);
        result.add(dummyStayOut);
        return result;
    }

    // GET ride out by id
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut getRideOut(@PathParam("id") ObjectId id) {
        if (id.equals(ID_12345)) {

            return getDummyRideOut();
        }

        throw new NotFoundException();
    }

    // GET ride outs with type ride
    @GET
    @Path("ride")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<RideOut> getRideOuts() {
        ArrayList<RideOut> rideOuts = new ArrayList<>();
        rideOuts.add(getDummyRideOut());
        return rideOuts;
    }

    // GET ride outs with type stay
    @GET
    @Path("stay")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<StayOut> getStayOuts() {
        ArrayList<StayOut> stayOuts = new ArrayList<>();
        stayOuts.add(getDummyStayOut());
        return stayOuts;
    }

    // GET ride outs with type tour
    @GET
    @Path("tour")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<TourOut> getTourOuts() {
        ArrayList<TourOut> tourOuts = new ArrayList<>();
        tourOuts.add(getDummyTourOut());
        return tourOuts;
    }

    // UPDATE rideout
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public RideOut updateRideOut(@PathParam("id") ObjectId id, RideOut rideOut) {
        if (id.equals(ID_12345)) {
            return rideOut;
        }
        throw new NotFoundException();
    }

    // POST ride out
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public RideOut addRideOut(RideOut rideOut) {
        rideOut.setId(ID_12345);
        return rideOut;

    }

    // DELETE ride out
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut deleteRideOut(@PathParam("id") ObjectId id) {
        if (id.equals(ID_12345)) {
            return getDummyRideOut();
        }
        throw new NotFoundException();
    }

    // ADD rider to ride out
    @PUT
    @Path("{rideOutId}/rider/{riderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut addRider(@PathParam("rideOutId") ObjectId rideOutId, @PathParam("riderId") ObjectId riderId) {
        if (rideOutId.equals(ID_12345)) {
            RideOut rideOut = getDummyRideOut();

            if (!riderId.equals(UserResource.UID_12345)) throw new NotFoundException("Rider not found");
            rideOut.getRiders().add(UserResource.getDummyRider());

            return rideOut;
        }

        throw new NotFoundException("RideOut not found");
    }

    // REMOVE rider from ride out
    @DELETE
    @Path("{rideOutId}/rider/{riderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut removeRider(@PathParam("rideOutId") ObjectId rideOutId, @PathParam("riderId") ObjectId riderId) {
        if (rideOutId.equals(ID_12345)) {
            if (!riderId.equals(UserResource.UID_12345)) throw new NotFoundException("Rider not found");

            return getDummyRideOut();
        }

        throw new NotFoundException("RideOut not found");
    }

    // ======== DUMMY DATA ========

    private RideOut getDummyRideOut() {
        return new RideOut(
                ID_12345,
                "Ride around the candovers",
                new Date(100),
                new Date(100),
                15,
                "54321",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date(100)

        );
    }
    
    private StayOut getDummyStayOut() {
        StayOut dummy = new StayOut(
                ID_23456,
                "Stay around the candovers",
                new Date(200),
                new Date(200),
                10,
                "1234",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date(200)
        );
        Booking accommodation = new Booking(BID_1234, "Marriot Hotel", "ABCDE");
        Booking restaurant = new Booking(BID_4321, "KFC", "");
        dummy.addAccommodation(accommodation);
        dummy.addRestaurant(restaurant);
        return dummy;
    }

    private TourOut getDummyTourOut() {
        TourOut dummy = new TourOut(
                ID_34567,
                "Tour around the candovers",
                new Date(300),
                new Date(300),
                5,
                "2345",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date(300)
        );
        Booking accommodation = new Booking(BID_1234, "Marriot Hotel", "ABCDE");
        Booking restaurant = new Booking(BID_4321, "KFC", "");
        Booking travel = new Booking(BID_9876, "Condor Ferries", "QWERTY");
        dummy.addAccommodation(accommodation);
        dummy.addRestaurant(restaurant);
        dummy.addTravelBooking(travel);
        return dummy;
    }
}
