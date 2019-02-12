package org.io.rideout.resource;

import org.io.rideout.model.Rider;
import org.io.rideout.model.Vehicle;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;

@Path("rider")
public class RiderResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Rider> getAllRiders() {
        ArrayList<Rider> result = new ArrayList<>();
        Rider dummy = getDummyRider();
        result.add(dummy);
        return result;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Rider getRider(@PathParam("id") String id) {
        if (id.equals("12345")) {
            return getDummyRider();
        }

        throw new NotFoundException();
    }

    static Rider getDummyRider() {
        Rider dummy = new Rider(
                "12345",
                "jsmith",
                "John",
                "Smith",
                new Date(100),
                "07491012345",
                "999",
                true,
                false,
                "A"
        );
        dummy.addVehicle(new Vehicle("9876", "Honda", "Monkey", 125, "REG123"));
        return dummy;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Rider addRider(Rider rider) {
        rider.setId("12345");
        return rider;
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Rider updateRider(@PathParam("id") String id, Rider rider) {
        if (id.equals("54321")) {
            return rider;
        }

        throw new NotFoundException();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Rider removeRider(@PathParam("id") String id) {
        if (id.equals("12345")) {
            return getDummyRider();
        }

        throw new NotFoundException();
    }
}
