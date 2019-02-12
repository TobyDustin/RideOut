package org.io.rideout.resource;

import org.io.rideout.model.RideOut;
import org.io.rideout.model.Rider;
import org.io.rideout.model.Staff;
import org.io.rideout.model.Vehicle;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;

@Path("rideout")
public class RideOutResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<RideOut> getAllRiders() {
        ArrayList<RideOut> result = new ArrayList<>();
        RideOut dummy = getDummyRideOut();
        result.add(dummy);
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

}
