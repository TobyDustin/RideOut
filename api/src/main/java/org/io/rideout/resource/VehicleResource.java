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
import org.io.rideout.BeanValidation;
import org.io.rideout.authentication.Secured;
import org.io.rideout.database.UserDao;
import org.io.rideout.database.VehicleDao;
import org.io.rideout.model.Vehicle;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

public class VehicleResource {

    private UserDao userDao = UserDao.getInstance();
    private VehicleDao vehicleDao = VehicleDao.getInstance();
    private ObjectId userId;

    VehicleResource(ObjectId userId) {
        this.userId = userId;
    }

    // GET user vehicles
    @GET
    @Secured
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
    public ArrayList<Vehicle> getUserVehicles() {
        ArrayList<Vehicle> result = vehicleDao.getAll(userId);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // GET user vehicle by ID
    @GET
    @Secured
    @Path("{vid}")
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
    public Vehicle getUserVehicle(@Parameter(description = "Vehicle ID", schema = @Schema(type = "string")) @PathParam("vid") ObjectId vid) {
        Vehicle result = vehicleDao.getById(userId, vid);

        if (result != null) {
            return result;
        }

        throw new NotFoundException();
    }

    // UPDATE user vehicle
    @PUT
    @Secured
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
    public Vehicle updateVehicle(Vehicle vehicle) {
        BeanValidation.validate(vehicle);
        Vehicle result = vehicleDao.update(userId, vehicle);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // CREATE user vehicle
    @POST
    @Secured
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
    public Vehicle addVehicle(Vehicle vehicle) {
        vehicle.setId(new ObjectId());
        BeanValidation.validate(vehicle);
        Vehicle result = vehicleDao.insert(userId, vehicle);

        if (result != null) return result;
        throw new InternalServerErrorException();
    }

    //DELETE user vehicle
    @DELETE
    @Secured
    @Path("{vid}")
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
    public Vehicle removeVehicle(@Parameter(description = "Vehicle ID", schema = @Schema(type = "string")) @PathParam("vid") ObjectId vid) {
        Vehicle result = vehicleDao.delete(userId, vid);

        if (result != null) return result;
        throw new NotFoundException();
    }
}
