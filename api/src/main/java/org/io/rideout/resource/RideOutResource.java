package org.io.rideout.resource;

import org.io.rideout.model.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;

@Path("rideout")
public class RideOutResource {
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

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut getRideOut(@PathParam("id") String id) {
        if (id.equals("12345")) {
            return getDummyRideOut();
        }

        throw new NotFoundException();
    }

    @GET
    @Path("ride")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<RideOut> getRideOuts() {
        ArrayList<RideOut> rideOuts = new ArrayList<>();
        rideOuts.add(getDummyRideOut());
        return rideOuts;
    }

    @GET
    @Path("stay")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<StayOut> getStayOuts() {
        ArrayList<StayOut> stayOuts = new ArrayList<>();
        stayOuts.add(getDummyStayOut());
        return stayOuts;
    }

    @GET
    @Path("tour")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<TourOut> getTourOuts() {
        ArrayList<TourOut> tourOuts = new ArrayList<>();
        tourOuts.add(getDummyTourOut());
        return tourOuts;
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut addRideOut() {
        throw new NotImplementedException();
    }


    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut updateRideOut(@PathParam("id") String id) {
        throw new NotImplementedException();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut deleteRideOut(@PathParam("id") String id) {
        throw new NotImplementedException();
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
