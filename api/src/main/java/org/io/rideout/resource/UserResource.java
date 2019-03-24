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
import org.io.rideout.PasswordManager;
import org.io.rideout.authentication.AuthenticationFilter;
import org.io.rideout.authentication.Secured;
import org.io.rideout.database.UserDao;
import org.io.rideout.exception.UnauthorizedException;
import org.io.rideout.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.Collections;
import java.util.List;

import static org.io.rideout.authentication.AuthenticationFilter.AUTHENTICATION_SCHEMA;

@Path("user")
public class UserResource {

    private UserDao userDao = UserDao.getInstance();

    // GET all users
    @GET
    @Secured
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
    public List<User> getAllUsers(@Context SecurityContext securityContext) {
        if (securityContext.isUserInRole(User.STAFF)) {
            return userDao.getAll();
        } else if (securityContext.isUserInRole(User.RIDER)) {
            User result = userDao.getById(new ObjectId(securityContext.getUserPrincipal().getName()));
            if (result == null) throw new NotFoundException();
            return Collections.singletonList(result);
        }

        throw new UnauthorizedException();
    }

    // GET user by ID
    @GET
    @Secured
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
    public User getUser(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("id") ObjectId id,
                        @Context SecurityContext securityContext) {

        if (securityContext.isUserInRole(User.RIDER) && !id.toHexString().equals(securityContext.getUserPrincipal().getName())) {
            throw new UnauthorizedException();
        }

        User result = userDao.getById(id);

        if (result != null) {
            return result;
        }

        throw new NotFoundException();
    }

    @Path("{uid}/vehicle")
    public VehicleResource getVehicleResource(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("uid") ObjectId uid) {
        return new VehicleResource(uid);
    }

    @Path("{uid}/riderinfo")
    public RiderInformationResource getRiderInformationResource(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("uid") ObjectId uid) {
        return new RiderInformationResource(uid);
    }

    // UPDATE user
    @PUT
    @Secured
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
    public User updateUser(User user, @Context SecurityContext securityContext) {
        if (securityContext.isUserInRole(User.RIDER) &&
                !user.getId().toHexString().equals(securityContext.getUserPrincipal().getName())) {
            throw new UnauthorizedException();
        }

        BeanValidation.validate(user);
        User result = userDao.update(user);

        if (result == null) throw new NotFoundException();
        return result;
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
    public User addUser(User user, @HeaderParam("Authorization") @DefaultValue("") String authHeader) {
        user.setId(new ObjectId());
        user.setPassword(PasswordManager.hashPassword(user.getPassword()));
        BeanValidation.validate(user);

        if (user.getRole().equals(User.RIDER)) {
            return userDao.insert(user);
        } else if (user.getRole().equals(User.STAFF)) {
            if (!authHeader.isEmpty()) {
                String token = authHeader.substring(AUTHENTICATION_SCHEMA.length()).trim();
                String id = AuthenticationFilter.validateToken(token);

                if (id != null) {
                    User auth = userDao.getById(new ObjectId(id));

                    if (auth != null && auth.getRole().equals(User.STAFF)) {
                        return userDao.insert(user);
                    }
                }
            }

            throw new UnauthorizedException();
        }

        throw new BadRequestException();
    }

    // DELETE user
    @DELETE
    @Secured
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
    public User removeUser(@Parameter(description = "User ID", schema = @Schema(type = "string")) @PathParam("id") ObjectId id,
                           @Context SecurityContext securityContext) {

        if (securityContext.isUserInRole(User.RIDER) && !id.toHexString().equals(securityContext.getUserPrincipal().getName())) {
            throw new UnauthorizedException();
        }

        User result = userDao.delete(id);

        if (result == null) throw new NotFoundException();
        return result;
    }
}
