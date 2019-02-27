package org.io.rideout.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.bson.types.ObjectId;
import org.io.rideout.PasswordManager;
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
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get all Users",
            tags = {"user"},
            description = "Returns all Users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of Users", content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized")
            },
            security = @SecurityRequirement(
                    name = "JWT"
            )
    )
    public ArrayList<User> getAllUsers() {
        return userDao.getAll();
    }

    // GET user by ID
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get User",
            tags = {"user"},
            description = "Returns User with given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User", content = @Content(
                            schema = @Schema(implementation = User.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            },
            security = @SecurityRequirement(
                    name = "JWT"
            )
    )
    public User getUser(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("id") ObjectId id) {
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
    @Operation(
            summary = "Get all Vehicles",
            tags = {"user", "vehicle"},
            description = "Returns all Vehicles for given user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of Vehicles", content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = Vehicle.class))
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized")
            },
            security = @SecurityRequirement(
                    name = "JWT"
            )
    )
    public ArrayList<Vehicle> getUserVehicles(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("id") ObjectId id) {
        ArrayList<Vehicle> result = vehicleDao.getAll(id);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // GET user vehicle by ID
    @GET
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get Vehicle",
            tags = {"user", "vehicle"},
            description = "Returns User with given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehicle", content = @Content(
                            schema = @Schema(implementation = Vehicle.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found")
            },
            security = @SecurityRequirement(
                    name = "JWT"
            )
    )
    public Vehicle getUserVehicle(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("uid") ObjectId uid,
                                  @Parameter(description = "Vehicle ID", schema = @Schema(type = "string")) @PathParam("vid") ObjectId vid) {
        Vehicle result = vehicleDao.getById(uid, vid);

        if (result != null) {
            return result;
        }

        throw new NotFoundException();
    }

    // UPDATE user
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Update User",
            tags = {"user"},
            description = "Updates user with given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User", content = @Content(
                            schema = @Schema(implementation = User.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            },
            requestBody = @RequestBody(description = "Updated User", content = @Content(
                    schema = @Schema(implementation = User.class)
            )),
            security = @SecurityRequirement(
                    name = "JWT"
            )
    )
    public User updateUser(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("id") ObjectId id, User user) {
        User result = userDao.update(id, user);

        if (result == null) throw new NotFoundException();
        return result;
    }

    // UPDATE user vehicle
    @PUT
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Update Vehicle",
            tags = {"user", "vehicle"},
            description = "Updates Vehicle with given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehicle", content = @Content(
                            schema = @Schema(implementation = Vehicle.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found")
            },
            requestBody = @RequestBody(description = "Updated Vehicle", content = @Content(
                    schema = @Schema(implementation = Vehicle.class)
            )),
            security = @SecurityRequirement(
                    name = "JWT"
            )
    )
    public Vehicle updateVehicle(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("uid") ObjectId uid,
                                 @Parameter(description = "Vehicle ID", schema = @Schema(type = "string")) @PathParam("vid") ObjectId vid, Vehicle vehicle) {
        Vehicle result = vehicleDao.update(uid, vid, vehicle);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // CREATE user
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Add User",
            tags = {"user"},
            description = "Adds new User",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User", content = @Content(
                            schema = @Schema(implementation = User.class)
                    ))
            },
            requestBody = @RequestBody(description = "New User", content = @Content(
                    schema = @Schema(implementation = User.class)
            ))
    )
    public User addUser(User user) {
        user.setPassword(PasswordManager.hashPassword(user.getPassword()));
        return userDao.insert(user);
    }

    // CREATE user vehicle
    @POST
    @Path("{uid}/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Add Vehicle",
            tags = {"user", "vehicle"},
            description = "Adds new Vehicle to a User",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehicle", content = @Content(
                            schema = @Schema(implementation = Vehicle.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found")
            },
            requestBody = @RequestBody(description = "New Vehicle", content = @Content(
                    schema = @Schema(implementation = Vehicle.class)
            )),
            security = @SecurityRequirement(
                    name = "JWT"
            )
    )
    public Vehicle addVehicle(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("uid") ObjectId uid, Vehicle vehicle) {
        Vehicle result = vehicleDao.insert(uid, vehicle);

        if (result != null) return result;
        throw new InternalServerErrorException();
    }

    // DELETE user
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Delete User",
            tags = {"user"},
            description = "Deletes a User",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User", content = @Content(
                            schema = @Schema(implementation = User.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            },
            security = @SecurityRequirement(
                    name = "JWT"
            )
    )
    public User removeUser(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("id") ObjectId id) {
        User result = userDao.delete(id);

        if (result == null) throw new NotFoundException();
        return result;
    }

    //DELETE user vehicle
    @DELETE
    @Path("{uid}/vehicle/{vid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Delete Vehicle",
            tags = {"user", "vehicle"},
            description = "Deletes a Vehicle",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehicle", content = @Content(
                            schema = @Schema(implementation = Vehicle.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found")
            },
            security = @SecurityRequirement(
                    name = "JWT"
            )
    )
    public Vehicle removeVehicle(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("uid") ObjectId uid,
                                 @Parameter(description = "Vehicle ID", schema = @Schema(type = "string")) @PathParam("vid") ObjectId vid) {
        Vehicle result = vehicleDao.delete(uid, vid);

        if (result != null) return result;
        throw new NotFoundException();
    }
}
