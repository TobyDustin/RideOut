package org.io.rideout.resource;

import org.io.rideout.model.RiderInformation;
import org.io.rideout.model.User;
import org.io.rideout.model.Vehicle;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;

@Path("user")
public class UserResource {

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
    public User getUser(@PathParam("id") String id) {
        switch (id) {
            case "12345": return getDummyRider();
            case "23456": return getDummyStaff();
        }

        throw new NotFoundException();
    }

    // GET user vehicles
    @GET
    @Path("{id}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Vehicle> getUserVehicles(@PathParam("id") String id) {
        switch (id) {
            case "12345": return getDummyRider().getRiderInformation().getVehicles();
            case "23456": return new ArrayList<>();
        }

        throw new NotFoundException();
    }

    // GET user vehicle by ID
    @GET
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Vehicle getUserVehicle(@PathParam("uid") String uid, @PathParam("vid") String vid) {
        if (uid.equals("12345")) {
            RiderInformation riderInformation = getDummyRider().getRiderInformation();
            switch (vid) {
                case "9876":
                    return riderInformation.getVehicles().get(0);
                case "1234":
                    return riderInformation.getVehicles().get(1);
                default:
                    throw new NotFoundException();
            }
        }

        throw new NotFoundException();
    }

    // UPDATE user
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User updateUser(@PathParam("id") String id, User user) {
        if (id.equals("54321")) {
            return user;
        }

        throw new NotFoundException();
    }

    // UPDATE user vehicle
    @PUT
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vehicle updateVehicle(@PathParam("uid") String uid, @PathParam("vid") String vid, Vehicle vehicle) {
        if (uid.equals("12345")) {
            switch (vid) {
                case "9876":
                case "1234": return vehicle;
                default: throw new NotFoundException();
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
            case User.RIDER: user.setId("12345"); break;
            case User.STAFF: user.setId("23456"); break;
        }

        return user;
    }

    // CREATE user vehicle
    @POST
    @Path("{uid}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vehicle addVehicle(@PathParam("uid") String uid, Vehicle vehicle) {
        return getDummyRider().getRiderInformation().getVehicles().get(0);
    }

    // DELETE user
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User removeUser(@PathParam("id") String id) {
        switch (id) {
            case "12345": return getDummyRider();
            case "23456": return getDummyStaff();
            default: throw new NotFoundException();
        }
    }

    //DELETE user vehicle
    @DELETE
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Vehicle removeVehicle(@PathParam("uid") String uid, @PathParam("vid") String vid) {
        if (uid.equals("12345")) {
            User rider = getDummyRider();
            RiderInformation riderInformation = rider.getRiderInformation();
            switch (vid) {
                case "9876":
                    return riderInformation.getVehicles().get(0);
                case "1234":
                    return riderInformation.getVehicles().get(1);
                default:
                    throw new NotFoundException();
            }
        }

        throw new NotFoundException();
    }

//    ======== DUMMY DATA =========

    static User getDummyRider() {
        User dummy = new User(
                "12345",
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
        dummy.getRiderInformation().addVehicle(new Vehicle("9876", "Honda", "Monkey", 125, "REG123"));
        dummy.getRiderInformation().addVehicle(new Vehicle("1234", "Suzuki", "GSXR", 1000, "REG987"));
        return dummy;
    }

    static User getDummyStaff() {
        return new User(
                "23456",
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
