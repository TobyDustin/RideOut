package org.io.rideout.resource;

import org.io.rideout.model.RideOut;
import org.io.rideout.model.Rider;
import org.io.rideout.model.Vehicle;

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
                new Date(),
                new Date(),
                15,
                "54321",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date()

        );

        return dummy;
    }




}
