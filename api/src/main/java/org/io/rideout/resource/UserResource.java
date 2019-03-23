package org.io.rideout.resource;

import org.bson.types.ObjectId;
import org.io.rideout.BeanValidation;
import org.io.rideout.PasswordManager;
import org.io.rideout.authentication.Secured;
import org.io.rideout.database.UserDao;
import org.io.rideout.database.VehicleDao;
import org.io.rideout.model.User;
import org.io.rideout.model.Vehicle;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("user")
public class UserResource {

    private UserDao userDao = UserDao.getInstance();
    private VehicleDao vehicleDao = VehicleDao.getInstance();

    // GET all users
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<User> getAllUsers() {
        return userDao.getAll();
    }

    // GET user by ID
    @GET
    @Secured
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") ObjectId id) {
        User result = userDao.getById(id);

        if (result != null) {
            return result;
        }

        throw new NotFoundException();
    }

    // GET user vehicles
    @GET
    @Secured
    @Path("{id}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Vehicle> getUserVehicles(@PathParam("id") ObjectId id) {
        ArrayList<Vehicle> result = vehicleDao.getAll(id);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // GET user vehicle by ID
    @GET
    @Secured
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Vehicle getUserVehicle(@PathParam("uid") ObjectId uid, @PathParam("vid") ObjectId vid) {
        Vehicle result = vehicleDao.getById(uid, vid);

        if (result != null) {
            return result;
        }

        throw new NotFoundException();
    }

    // UPDATE user
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User updateUser(User user) {
        BeanValidation.validate(user);
        User result = userDao.update(user);

        if (result == null) throw new NotFoundException();
        return result;
    }

    // UPDATE user vehicle
    @PUT
    @Secured
    @Path("{uid}/vehicle/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vehicle updateVehicle(@PathParam("uid") ObjectId uid, Vehicle vehicle) {
        BeanValidation.validate(vehicle);
        Vehicle result = vehicleDao.update(uid, vehicle);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // CREATE user
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User addUser(User user) {
        user.setId(new ObjectId());
        user.setPassword(PasswordManager.hashPassword(user.getPassword()));
        BeanValidation.validate(user);

        return userDao.insert(user);
    }

    // CREATE user vehicle
    @POST
    @Secured
    @Path("{uid}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vehicle addVehicle(@PathParam("uid") ObjectId uid, Vehicle vehicle) {
        vehicle.setId(new ObjectId());
        BeanValidation.validate(vehicle);

        Vehicle result = vehicleDao.insert(uid, vehicle);

        if (result != null) return result;
        throw new InternalServerErrorException();
    }

    // DELETE user
    @DELETE
    @Secured
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User removeUser(@PathParam("id") ObjectId id) {
        User result = userDao.delete(id);

        if (result == null) throw new NotFoundException();
        return result;
    }

    //DELETE user vehicle
    @DELETE
    @Secured
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Vehicle removeVehicle(@PathParam("uid") ObjectId uid, @PathParam("vid") ObjectId vid) {
        Vehicle result = vehicleDao.delete(uid, vid);

        if (result != null) return result;
        throw new NotFoundException();
    }
}
