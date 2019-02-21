package org.io.rideout.resource;

import org.bson.types.ObjectId;
import org.io.rideout.model.RiderInformation;
import org.io.rideout.model.User;
import org.io.rideout.model.Vehicle;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;

@Path("user")
public class UserResource {

    public static ObjectId UID_12345 = new ObjectId("5c6a97fe3bd3d419a78de2c4");
    public static ObjectId UID_23456 = new ObjectId("5c6a97fe3bd3d419a78de2c5");
    public static ObjectId UID_54321 = new ObjectId("5c6a9bd73b16145a50f1c4cc");
    public static ObjectId VID_9876 = new ObjectId("5c6a96ba2ebe572fd56ce470");
    public static ObjectId VID_1234 = new ObjectId("5c6a96ba2ebe572fd56ce471");

    // GET all users
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<User> getAllUsers() {
        ArrayList<User> result = new ArrayList<>();
        result.add(getDummyRider());
        result.add(getDummyStaff());
        return result;
    }

    // GET user by ID
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") ObjectId id) {
        if (id.equals(UID_12345)) {
            return getDummyRider();
        } else if (id.equals(UID_23456)) {
            return getDummyStaff();
        }

        throw new NotFoundException();
    }

    // GET user vehicles
    @GET
    @Path("{id}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Vehicle> getUserVehicles(@PathParam("id") ObjectId id) {
        if (id.equals(UID_12345)) {
            return getDummyRider().getRiderInformation().getVehicles();
        } else if (id.equals(UID_23456)) {
            return new ArrayList<>();
        }

        throw new NotFoundException();
    }

    // GET user vehicle by ID
    @GET
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Vehicle getUserVehicle(@PathParam("uid") ObjectId uid, @PathParam("vid") ObjectId vid) {
        if (uid.equals(UID_12345)) {
            RiderInformation riderInformation = getDummyRider().getRiderInformation();

            if (vid.equals(VID_9876)) {
                return riderInformation.getVehicles().get(0);
            } else if (vid.equals(VID_1234)) {
                return riderInformation.getVehicles().get(1);
            }
        }

        throw new NotFoundException();
    }

    // UPDATE user
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User updateUser(@PathParam("id") ObjectId id, User user) {
        if (id.equals(UID_54321)) {
            return user;
        }

        throw new NotFoundException();
    }

    // UPDATE user vehicle
    @PUT
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vehicle updateVehicle(@PathParam("uid") ObjectId uid, @PathParam("vid") ObjectId vid, Vehicle vehicle) {
        if (uid.equals(UID_12345)) {
            if (vid.equals(VID_9876) || vid.equals(VID_1234)) {
                return vehicle;
            }
        }

        throw new NotFoundException();
    }

    // CREATE user
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User addUser(User user) {
        switch (user.getRole()) {
            case User.RIDER: user.setId(UID_12345); break;
            case User.STAFF: user.setId(UID_23456); break;
        }

        return user;
    }

    // CREATE user vehicle
    @POST
    @Path("{uid}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vehicle addVehicle(@PathParam("uid") ObjectId uid, Vehicle vehicle) {
        return getDummyRider().getRiderInformation().getVehicles().get(0);
    }

    // DELETE user
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User removeUser(@PathParam("id") ObjectId id) {
        if (id.equals(UID_12345)) {
            return getDummyRider();
        } else if (id.equals(UID_23456)) {
            return getDummyStaff();
        }

        throw new NotFoundException();
    }

    //DELETE user vehicle
    @DELETE
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Vehicle removeVehicle(@PathParam("uid") ObjectId uid, @PathParam("vid") ObjectId vid) {
        if (uid.equals(UID_12345)) {
            RiderInformation riderInformation = getDummyRider().getRiderInformation();

            if (vid.equals(VID_9876)) {
                return riderInformation.getVehicles().get(0);
            } else if (vid.equals(VID_1234)) {
                return riderInformation.getVehicles().get(1);
            }
        }

        throw new NotFoundException();
    }

//    ======== DUMMY DATA =========

    static User getDummyRider() {
        User dummy = new User(
                UID_12345,
                "jsmith",
                "john123",
                User.RIDER,
                "John",
                "Smith",
                new Date(100),
                "07491012345",
                new RiderInformation(
                "999",
                true,
                "A"
                )
        );
        dummy.getRiderInformation().addVehicle(new Vehicle(VID_9876, "Honda", "Monkey", 125, "REG123"));
        dummy.getRiderInformation().addVehicle(new Vehicle(VID_1234, "Suzuki", "GSXR", 1000, "REG987"));
        return dummy;
    }

    static User getDummyStaff() {
        return new User(
                UID_23456,
                "jsmith",
                "john123",
                User.STAFF,
                "John",
                "Smith",
                new Date(100),
                "07491012345",
                null
        );
    }
}
