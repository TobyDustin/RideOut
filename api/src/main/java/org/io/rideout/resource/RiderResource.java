package org.io.rideout.resource;

import org.bson.types.ObjectId;
import org.io.rideout.model.Rider;
import org.io.rideout.model.Vehicle;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;

@Path("rider")
public class RiderResource {

    public static ObjectId UID_12345 = new ObjectId("5c6a96ba2ebe572fd56ce46f");
    public static ObjectId UID_54321 = new ObjectId("5c6a9bd73b16145a50f1c4cc");
    public static ObjectId VID_9876 = new ObjectId("5c6a96ba2ebe572fd56ce470");
    public static ObjectId VID_1234 = new ObjectId("5c6a96ba2ebe572fd56ce471");

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
    public Rider getRider(@PathParam("id") ObjectId id) {
        if (id.equals(UID_12345)) {
            return getDummyRider();
        }

        throw new NotFoundException();
    }

    // GET rider vehicles
    @GET
    @Path("{id}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Vehicle> getRiderVehicles(@PathParam("id") ObjectId id) {
        if (id.equals(UID_12345)) {
            Rider rider = getDummyRider();
            return rider.getVehicles();
        }

        throw new NotFoundException();
    }

    // GET rider vehicle by ID
    @GET
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Vehicle getRiderVehicle(@PathParam("uid") ObjectId uid, @PathParam("vid") ObjectId vid) {
        if (uid.equals(UID_12345)) {
            Rider rider = getDummyRider();

            if (vid.equals(VID_9876)) {
                return rider.getVehicles().get(0);
            } else if (vid.equals(VID_1234)) {
                return rider.getVehicles().get(1);
            } else {
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
    public Rider updateRider(@PathParam("id") ObjectId id, Rider rider) {
        if (id.equals(UID_54321)) {
            return rider;
        }

        throw new NotFoundException();
    }

    // UPDATE rider vehicle
    @PUT
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vehicle updateVehicle(@PathParam("uid") ObjectId uid, @PathParam("vid") ObjectId vid, Vehicle vehicle) {
        if (uid.equals(UID_12345)) {
            if (vid.equals(VID_9876) || vid.equals(VID_1234)) {
                return  vehicle;
            } else {
                throw new NotImplementedException();
            }
        }

        throw new NotFoundException();
    }

    // CREATE rider
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Rider addRider(Rider rider) {
        rider.setId(UID_12345);
        return rider;
    }

    // CREATE rider vehicle
    @POST
    @Path("{uid}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vehicle addVehicle(@PathParam("uid") ObjectId uid, Vehicle vehicle) {
        return getDummyRider().getVehicles().get(0);
    }

    // DELETE rider
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Rider removeRider(@PathParam("id") ObjectId id) {
        if (id.equals(UID_12345)) {
            return getDummyRider();
        }

        throw new NotFoundException();
    }

    //DELETE rider vehicle
    @DELETE
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Vehicle removeVehicle(@PathParam("uid") ObjectId uid, @PathParam("vid") ObjectId vid) {
        if (uid.equals(UID_12345)) {
            Rider rider = getDummyRider();

            if (vid.equals(VID_9876)) {
                return rider.getVehicles().get(0);
            } else if (vid.equals(VID_1234)) {
                return rider.getVehicles().get(1);
            }
        }

        throw new NotFoundException();
    }

//    ======== DUMMY DATA =========

    static Rider getDummyRider() {
        Rider dummy = new Rider(
                UID_12345,
                "jsmith",
                "john123",
                "John",
                "Smith",
                new Date(100),
                "07491012345",
                "999",
                true,
                false,
                "A"
        );
        dummy.addVehicle(new Vehicle(VID_9876, "Honda", "Monkey", 125, "REG123"));
        dummy.addVehicle(new Vehicle(VID_1234, "Suzuki", "GSXR", 1000, "REG987"));
        return dummy;
    }

}
