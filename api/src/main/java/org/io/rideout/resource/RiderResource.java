package org.io.rideout.resource;

import org.io.rideout.model.Rider;
import org.io.rideout.model.Vehicle;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;

@Path("rider")
public class RiderResource {

    // GET all riders
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Rider> getAllRiders() {
        ArrayList<Rider> result = new ArrayList<>();
        Rider dummy = getDummyRider();
        result.add(dummy);
        return result;
    }

    // GET rider by ID
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Rider getRider(@PathParam("id") String id) {
        if (id.equals("12345")) {
            return getDummyRider();
        }

        throw new NotFoundException();
    }

    // GET rider vehicles
    @GET
    @Path("{id}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Vehicle> getRiderVehicles(@PathParam("id") String id) {
        if (id.equals("12345")) {
            Rider rider = getDummyRider();
            return rider.getVehicles();
        }

        throw new NotFoundException();
    }

    // GET rider vehicle by ID
    @GET
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Vehicle getRiderVehicle(@PathParam("uid") String uid, @PathParam("vid") String vid) {
        if (uid.equals("12345")) {
            Rider rider = getDummyRider();
            switch (vid) {
                case "9876":
                    return rider.getVehicles().get(0);
                case "1234":
                    return rider.getVehicles().get(1);
                default:
                    throw new NotFoundException();
            }
        }

        throw new NotFoundException();
    }

    // UPDATE rider
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Rider updateRider(@PathParam("id") String id, Rider rider) {
        if (id.equals("54321")) {
            return rider;
        }

        throw new NotFoundException();
    }

    // UPDATE rider vehicle
    @PUT
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vehicle updateVehicle(@PathParam("uid") String uid, @PathParam("vid") String vid, Vehicle vehicle) {
        if (uid.equals("12345")) {
            Rider rider = getDummyRider();
            switch (vid) {
                case "9876":
                    return vehicle;
                case "1234":
                    return vehicle;
                default:
                    throw new NotFoundException();
            }
        }

        throw new NotFoundException();
    }

    // CREATE rider
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Rider addRider(Rider rider) {
        rider.setId("12345");
        return rider;
    }

    // DELETE rider
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Rider removeRider(@PathParam("id") String id) {
        if (id.equals("12345")) {
            return getDummyRider();
        }

        throw new NotFoundException();
    }

//    ======== DUMMY DATA =========

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
        dummy.addVehicle(new Vehicle("1234", "Suzuki", "GSXR", 1000, "REG987"));
        return dummy;
    }

}
