package org.io.rideout.resource;

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
    public RideOut getRideOut(@PathParam("id") String id) {
        if (id.equals("12345")) {

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
    public RideOut updateRideOut(@PathParam("id") String id, RideOut rideOut) {
        if (id.equals("12345")) {
            return rideOut;
        }
        throw new NotFoundException();
    }

    // POST ride out
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public RideOut addRideOut(RideOut rideOut) {
        rideOut.setId("12345");
        return rideOut;

    }

    // DELETE ride out
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut deleteRideOut(@PathParam("id") String id) {
        if (id.equals("12345")) {
            return getDummyRideOut();
        }
        throw new NotFoundException();
    }

    // ADD rider to ride out
    @PUT
    @Path("{rideOutId}/rider/{riderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut addRider(@PathParam("rideOutId") String rideOutId, @PathParam("riderId") String riderId) {
        if (rideOutId.equals("12345")) {
            RideOut rideOut = getDummyRideOut();

            if (!riderId.equals("12345")) throw new NotFoundException("Rider not found");
            rideOut.getRiders().add(RiderResource.getDummyRider());

            return rideOut;
        }

        throw new NotFoundException("RideOut not found");
    }

    // REMOVE rider from ride out
    @DELETE
    @Path("{rideOutId}/rider/{riderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut removeRider(@PathParam("rideOutId") String rideOutId, @PathParam("riderId") String riderId) {
        if (rideOutId.equals("12345")) {
            if (!riderId.equals("12345")) throw new NotFoundException("Rider not found");

            return getDummyRideOut();
        }

        throw new NotFoundException("RideOut not found");
    }

    // ======== DUMMY DATA ========

    private RideOut getDummyRideOut() {
        RideOut dummy = new RideOut(
                "12345",
                "Ride around the candovers",
                new Date(100),
                new Date(100),
                15,
                "54321",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date(100)

        );
        return dummy;
    }
    
    private StayOut getDummyStayOut() {
        StayOut dummy = new StayOut(
                "23456",
                "Stay around the candovers",
                new Date(200),
                new Date(200),
                10,
                "1234",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date(200)
        );
        Booking accommodation = new Booking("1234", "Marriot Hotel", "ABCDE");
        Booking restaurant = new Booking("4321", "KFC", "");
        dummy.addAccommodation(accommodation);
        dummy.addRestaurant(restaurant);
        return dummy;
    }

    private TourOut getDummyTourOut() {
        TourOut dummy = new TourOut(
                "34567",
                "Tour around the candovers",
                new Date(300),
                new Date(300),
                5,
                "2345",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date(300)
        );
        Booking accommodation = new Booking("1234", "Marriot Hotel", "ABCDE");
        Booking restaurant = new Booking("4321", "KFC", "");
        Booking travel = new Booking("9876", "Condor Ferries", "QWERTY");
        dummy.addAccommodation(accommodation);
        dummy.addRestaurant(restaurant);
        dummy.addTravelBooking(travel);
        return dummy;
    }
}
