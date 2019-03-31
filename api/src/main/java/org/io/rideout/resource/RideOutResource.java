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
import org.io.rideout.database.RideOutDao;
import org.io.rideout.database.UserDao;
import org.io.rideout.database.VehicleDao;
import org.io.rideout.exception.UnauthorizedException;
import org.io.rideout.exception.ValidationException;
import org.io.rideout.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("ALL")
@Path("rideout")
@Secured
@SecurityRequirement(
        name = "JWT"
)
public class RideOutResource {

    private RideOutDao rideoutDao = RideOutDao.getInstance();
    private UserDao userDao = UserDao.getInstance();
    private VehicleDao vehicleDao = VehicleDao.getInstance();

    @Path("{id}/checkpoint")
    public CheckpointResource getCheckpointResource(@Parameter(description = "Rideout ID") @PathParam("id") ObjectId id) {
        return new CheckpointResource(id);
    }

    @Path("{id}/booking")
    public BookingResource getBookingResource(@Parameter(description = "Rideout ID") @PathParam("id") ObjectId id) {
        return new BookingResource(id);
    }

    // GET all ride outs
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get all rideouts",
            tags = {"rideout"},
            description = "Returns all RideOuts (all types)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of RideOuts", content = @Content(
                            array = @ArraySchema(schema = @Schema(
                                    implementation = RideOut.class,
                                    subTypes = {StayOut.class, TourOut.class}
                            ))
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized")
            }
    )
    public ArrayList<RideOut> getAllRideOuts(@BeanParam FilterBean filters) {
        BeanValidation.validate(filters);
        return rideoutDao.getAll(filters);
    }

    // GET ride out by id
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get RideOut by ID",
            tags = {"rideout"},
            description = "Returns a RideOut with given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "RideOut", content = @Content(
                            schema = @Schema(
                                    type = "object",
                                    oneOf = {
                                            RideOut.class,
                                            StayOut.class,
                                            TourOut.class
                                    }
                            )
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "RideOut not found")
            }
    )
    public RideOut getRideOut(
            @Parameter(description = "ID of the RideOut", schema = @Schema(type = "string")) @PathParam("id") ObjectId id
    ) {
        RideOut result = rideoutDao.getById(id);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // GET ride outs with type ride
    @GET
    @Path("ride")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get all RideOuts",
            tags = {"rideout"},
            description = "Returns all RideOuts (type = ride)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of RideOuts", content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = RideOut.class))
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized")
            }
    )
    public ArrayList<RideOut> getRideOuts(@BeanParam FilterBean filters) {
        filters.types = Collections.singletonList(RideOut.RIDE);
        BeanValidation.validate(filters);
        return rideoutDao.getAll(filters);
    }

    // GET ride outs with type stay
    @GET
    @Path("stay")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get all StayOut",
            tags = {"rideout"},
            description = "Returns all StayOuts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of StayOuts", content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = StayOut.class))
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized")
            }
    )
    public ArrayList<StayOut> getStayOuts(@BeanParam FilterBean filters) {
        filters.types = Collections.singletonList(RideOut.STAY);
        BeanValidation.validate(filters);
        ArrayList<StayOut> result = new ArrayList<>();

        for (RideOut ride : rideoutDao.getAll(filters)) {
            if (ride instanceof StayOut) result.add((StayOut) ride);
        }

        return result;
    }

    // GET ride outs with type tour
    @GET
    @Path("tour")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get all TourOuts",
            tags = {"rideout"},
            description = "Returns all TourOuts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of TourOuts", content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = TourOut.class))
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized")
            }
    )
    public ArrayList<TourOut> getTourOuts(@BeanParam FilterBean filters) {
        filters.types = Collections.singletonList(RideOut.TOUR);
        BeanValidation.validate(filters);
        ArrayList<TourOut> result = new ArrayList<>();

        for (RideOut ride : rideoutDao.getAll(filters)) {
            if (ride instanceof TourOut) result.add((TourOut) ride);
        }

        return result;
    }

    // Search rideouts by name
    @GET
    @Path("s/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Search RideOuts by name",
            tags = {"rideout"},
            description = "Returns all RideOuts with name matching given string",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of RideOuts", content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = RideOut.class,
                                    subTypes = {StayOut.class, TourOut.class}))
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized")
            }
    )
    public ArrayList<RideOut> search(@Parameter(description = "Search query") @PathParam("name") String name, @BeanParam FilterBean filters) {
        BeanValidation.validate(filters);
        return rideoutDao.search(name, filters);
    }

    // UPDATE rideout
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Operation(
            summary = "Update a RideOut",
            tags = {"rideout"},
            description = "Updates a given RideOut",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated RideOut", content = @Content(
                            schema = @Schema(
                                    type = "object",
                                    oneOf = {
                                            RideOut.class,
                                            StayOut.class,
                                            TourOut.class
                                    }
                            )
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "RideOut not found")
            },
            requestBody = @RequestBody(description = "RideOut model with changes to be saved", content = @Content(
                    schema = @Schema(
                            type = "object",
                            oneOf = {
                                    RideOut.class,
                                    StayOut.class,
                                    TourOut.class
                            }
                    )
            ))
    )
    public RideOut updateRideOut(RideOut rideOut, @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) throw new UnauthorizedException();

        BeanValidation.validate(rideOut);
        RideOut result = rideoutDao.update(rideOut);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // POST ride out
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Operation(
            summary = "Insert RideOut",
            tags = {"rideout"},
            description = "Adds new RideOut",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Created RideOut", content = @Content(
                            schema = @Schema(
                                    type = "object",
                                    oneOf = {
                                            RideOut.class,
                                            StayOut.class,
                                            TourOut.class
                                    }
                            )
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized")
            },
            requestBody = @RequestBody(description = "RideOut to add", content = @Content(
                    schema = @Schema(
                            type = "object",
                            oneOf = {
                                    RideOut.class,
                                    StayOut.class,
                                    TourOut.class
                            }
                    )
            ))
    )
    public RideOut addRideOut(RideOut rideOut, @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) throw new UnauthorizedException();

        rideOut.setId(new ObjectId());
        BeanValidation.validate(rideOut);

        return rideoutDao.insert(rideOut);
    }

    // DELETE ride out
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Delete RideOut",
            tags = {"rideout"},
            description = "Deletes given RideOut",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deleted RideOut", content = @Content(
                            schema = @Schema(
                                    type = "object",
                                    oneOf = {
                                            RideOut.class,
                                            StayOut.class,
                                            TourOut.class
                                    }
                            )
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "RideOut not found")
            }
    )
    public RideOut deleteRideOut(@Parameter(description = "ID to be deleted", schema = @Schema(type = "string")) @PathParam("id") ObjectId id,
                                 @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) throw new UnauthorizedException();

        RideOut result = rideoutDao.delete(id);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // ADD rider to ride out
    @PUT
    @Path("{rideOutId}/rider")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Add rider to RideOut",
            tags = {"user", "rideout"},
            description = "Adds given Rider to a RideOut",
            responses = {
                    @ApiResponse(responseCode = "200", description = "RideOut", content = @Content(
                            schema = @Schema(
                                    type = "object",
                                    oneOf = {
                                            RideOut.class,
                                            StayOut.class,
                                            TourOut.class
                                    }
                            )
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Rider or RideOut not found")
            },
            requestBody = @RequestBody(description = "User and Vehicle IDs", content = @Content(
                    schema = @Schema(
                            implementation = UserVehiclePair.class
                    )
            ))
    )
    public RideOut addRider(@Parameter(description = "RideOut ID", schema = @Schema(type = "string")) @PathParam("rideOutId") ObjectId rideOutId,
                            UserVehiclePair pair,
                            @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) {
            if (securityContext.isUserInRole(User.RIDER) &&
                    !pair.getUserId().equals(new ObjectId(securityContext.getUserPrincipal().getName()))) {
                throw new UnauthorizedException();
            }
        }

        User rider = userDao.getById(pair.getUserId());
        Vehicle vehicle = vehicleDao.getById(pair.getUserId(), pair.getVehicleId());

        if (rider == null) throw new NotFoundException("Rider not found");
        if (vehicle == null) throw new NotFoundException("Vehicle not found");
        if (rider.getRiderInformation() == null)
            throw new ValidationException(new ArrayList<>(Collections.singletonList("Rider information must not be null")));
        if (!rider.getRiderInformation().getVehicles().contains(vehicle))
            throw new ValidationException(new ArrayList<>(Collections.singletonList("Rider must own the selected vehicle")));
        if (!vehicle.isChecked())
            throw new ValidationException(new ArrayList<>(Collections.singletonList("Vehicle must be checked by staff")));

        RideOut result = rideoutDao.addRider(rideOutId, rider.simplify(), vehicle);

        if (result != null) return result;
        throw new NotFoundException("Rideout not found");
    }

    // REMOVE rider from ride out
    @DELETE
    @Path("{rideOutId}/rider/{riderId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Remove rider from RideOut",
            tags = {"user", "rideout"},
            description = "Removes given Rider from a RideOut",
            responses = {
                    @ApiResponse(responseCode = "200", description = "RideOut", content = @Content(
                            schema = @Schema(
                                    type = "object",
                                    oneOf = {
                                            RideOut.class,
                                            StayOut.class,
                                            TourOut.class
                                    }
                            )
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Rider or RideOut not found")
            }
    )
    public RideOut removeRider(@Parameter(description = "RideOut ID", schema = @Schema(type = "string")) @PathParam("rideOutId") ObjectId rideOutId,
                               @Parameter(description = "Rider ID", schema = @Schema(type = "string")) @PathParam("riderId") ObjectId riderId,
                               @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) {
            if (securityContext.isUserInRole(User.RIDER) &&
                    !riderId.equals(new ObjectId(securityContext.getUserPrincipal().getName()))) {
                throw new UnauthorizedException();
            }
        }

        User rider = userDao.getById(riderId);
        if (rider == null) throw new NotFoundException("Rider not found");

        RideOut result = rideoutDao.removeRider(rideOutId, rider.simplify());

        if (result != null) return result;
        throw new NotFoundException("Rideout not found");
    }

    @POST
    @Path("{rideOutId}/lead")@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Updates lead rider",
            tags = {"user", "rideout"},
            description = "Updates lead rider for give Rideout",
            responses = {
                    @ApiResponse(responseCode = "200", description = "RideOut", content = @Content(
                            schema = @Schema(
                                    type = "object",
                                    oneOf = {
                                            RideOut.class,
                                            StayOut.class,
                                            TourOut.class
                                    }
                            )
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Rider or RideOut not found")
            },
            requestBody = @RequestBody(description = "User and Vehicle IDs", content = @Content(
                    schema = @Schema(
                            implementation = UserVehiclePair.class
                    )
            ))
    )
    public RideOut updateLeadRider(@Parameter(description = "RideOut ID", schema = @Schema(type = "string")) @PathParam("rideOutId") ObjectId rideOutId,
                                   UserVehiclePair pair,
                                   @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) {
            throw new UnauthorizedException();
        }

        User rider = userDao.getById(pair.getUserId());
        Vehicle vehicle = vehicleDao.getById(pair.getUserId(), pair.getVehicleId());

        if (rider == null) throw new NotFoundException("Rider not found");
        if (vehicle == null) throw new NotFoundException("Vehicle not found");
        if (!rider.getRole().equals(User.STAFF))
            throw new ValidationException(new ArrayList<>(Collections.singletonList("Lead rider must be staff")));
        if (rider.getRiderInformation() == null)
            throw new ValidationException(new ArrayList<>(Collections.singletonList("Rider information must not be null")));
        if (!rider.getRiderInformation().getVehicles().contains(vehicle))
            throw new ValidationException(new ArrayList<>(Collections.singletonList("Rider must own the selected vehicle")));
        if (!vehicle.isChecked())
            throw new ValidationException(new ArrayList<>(Collections.singletonList("Vehicle must be checked by staff")));

        RideOut result = rideoutDao.updateLeadRider(rideOutId, rider.simplify(), vehicle);

        if (result != null) return result;
        throw new NotFoundException("Rideout not found");
    }
}
