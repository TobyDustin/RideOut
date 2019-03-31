package org.io.rideout.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.bson.types.ObjectId;
import org.io.rideout.BeanValidation;
import org.io.rideout.authentication.Secured;
import org.io.rideout.database.BookingDao;
import org.io.rideout.exception.UnauthorizedException;
import org.io.rideout.exception.ValidationException;
import org.io.rideout.model.Booking;
import org.io.rideout.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Collections;

@Secured
public class BookingResource {

    private ObjectId rideoutId;
    private BookingDao bookingDao;

    public BookingResource(ObjectId rideoutId) {
        this.rideoutId = rideoutId;
        this.bookingDao = BookingDao.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get all bookings",
            tags = {"rideout", "booking"},
            description = "Returns all bookings for given rideout",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of bookings", content = @Content(
                            array = @ArraySchema(schema = @Schema(
                                    implementation = Booking.class
                            ))
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public ArrayList<Booking> getAll(@Parameter(description = "Booking type") @QueryParam("type") @DefaultValue("") String type) {
        ArrayList<Booking> result;

        if (type.isEmpty()) {
            result = bookingDao.getAll(rideoutId);
        } else {
            if (!type.matches(Booking.RESTAURANT + "|" + Booking.ACCOMMODATION + "|" + Booking.TRAVEL))
                throw new ValidationException(new ArrayList<>(Collections.singletonList(type + " is not valid booking type")));

            result = bookingDao.getByType(rideoutId, type);
        }

        if (result == null) throw new NotFoundException();
        return result;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)@Operation(
            summary = "Get booking",
            tags = {"rideout", "booking"},
            description = "Returns all bookings for given rideout",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Selected booking", content = @Content(
                            schema = @Schema(implementation = Booking.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public Booking getById(@Parameter(description = "Booking ID") @PathParam("id") ObjectId id) {
        Booking result = bookingDao.getById(rideoutId, id);

        if (result == null) throw new NotFoundException();
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Create booking",
            tags = {"rideout", "booking"},
            description = "Inserts new booking into the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Created booking", content = @Content(
                            schema = @Schema(implementation = Booking.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Invalid user role"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            },
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Booking.class)))
    )
    public Booking insert(Booking booking, @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) throw new UnauthorizedException();

        booking.setId(new ObjectId());
        BeanValidation.validate(booking);

        Booking result = bookingDao.insert(rideoutId, booking);
        if (result == null) throw new NotFoundException();
        return result;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Update booking",
            tags = {"rideout", "booking"},
            description = "Updates booking in the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated booking", content = @Content(
                            schema = @Schema(implementation = Booking.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Invalid user role"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            },
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Booking.class)))
    )
    public Booking update(Booking booking, @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) throw new UnauthorizedException();
        BeanValidation.validate(booking);

        Booking result = bookingDao.update(rideoutId, booking);
        if (result == null) throw new NotFoundException();
        return result;
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Delete booking",
            tags = {"rideout", "booking"},
            description = "Deletes booking from a rideout",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deleted booking", content = @Content(
                            schema = @Schema(implementation = Booking.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Invalid user role"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public Booking delete(@Parameter(description = "Booking ID") @PathParam("id") ObjectId id,
                             @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) throw new UnauthorizedException();

        Booking result = bookingDao.delete(rideoutId, id);
        if (result == null) throw new NotFoundException();
        return result;
    }
}
