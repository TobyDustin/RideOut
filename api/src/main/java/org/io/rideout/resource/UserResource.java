package org.io.rideout.resource;

import org.bson.types.ObjectId;
import org.glassfish.jersey.jaxb.internal.XmlJaxbElementProvider;
import org.io.rideout.PasswordManager;
import org.io.rideout.database.UserDao;
import org.io.rideout.database.VehicleDao;
import org.io.rideout.exception.AppValidationException;
import org.io.rideout.model.User;
import org.io.rideout.model.Vehicle;

import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Set;

@Path("user")
public class UserResource {

    private UserDao userDao = UserDao.getInstance();
    private VehicleDao vehicleDao = VehicleDao.getInstance();
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // GET all users
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<User> getAllUsers() {
        return userDao.getAll();
    }

    // GET user by ID
    @GET
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
    @Path("{id}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Vehicle> getUserVehicles(@PathParam("id") ObjectId id) {
        ArrayList<Vehicle> result = vehicleDao.getAll(id);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // GET user vehicle by ID
    @GET
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User updateUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            throw new AppValidationException(extractViolations(violations));
        }

        User result = userDao.update(user);

        if (result == null) throw new NotFoundException();
        return result;
    }

    // UPDATE user vehicle
    @PUT
    @Path("{uid}/vehicle/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vehicle updateVehicle(@PathParam("uid") ObjectId uid, Vehicle vehicle) {
        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        if (!violations.isEmpty()) {
            throw new AppValidationException(extractViolations(violations));
        }

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
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            throw new AppValidationException(extractViolations(violations));
        }

        user.setPassword(PasswordManager.hashPassword(user.getPassword()));
        return userDao.insert(user);
    }

    // CREATE user vehicle
    @POST
    @Path("{uid}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vehicle addVehicle(@PathParam("uid") ObjectId uid, Vehicle vehicle) {
        vehicle.setId(new ObjectId());
        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        if (!violations.isEmpty()) {
            throw new AppValidationException(extractViolations(violations));
        }

        Vehicle result = vehicleDao.insert(uid, vehicle);

        if (result != null) return result;
        throw new InternalServerErrorException();
    }

    // DELETE user
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User removeUser(@PathParam("id") ObjectId id) {
        User result = userDao.delete(id);

        if (result == null) throw new NotFoundException();
        return result;
    }

    //DELETE user vehicle
    @DELETE
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Vehicle removeVehicle(@PathParam("uid") ObjectId uid, @PathParam("vid") ObjectId vid) {
        Vehicle result = vehicleDao.delete(uid, vid);

        if (result != null) return result;
        throw new NotFoundException();
    }

    private <T> ArrayList<String> extractViolations(Set<ConstraintViolation<T>> violations) {
        ArrayList<String> errors = new ArrayList<>();

        for (ConstraintViolation<T> violation : violations) {
            errors.add(violation.getPropertyPath() + " " + violation.getMessage());
        }

        return errors;
    }
}
