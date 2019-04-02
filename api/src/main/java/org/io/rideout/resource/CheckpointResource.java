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
import org.io.rideout.database.CheckpointDao;
import org.io.rideout.exception.UnauthorizedException;
import org.io.rideout.model.Checkpoint;
import org.io.rideout.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;

@Secured
public class CheckpointResource {

    private ObjectId rideoutId;
    private CheckpointDao checkpointDao;

    public CheckpointResource(ObjectId rideoutId) {
        this.rideoutId = rideoutId;
        this.checkpointDao = CheckpointDao.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get all checkpoints",
            tags = {"rideout", "checkpoint"},
            description = "Returns all checkpoints for given rideout",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of checkpoints", content = @Content(
                            array = @ArraySchema(schema = @Schema(
                                    implementation = Checkpoint.class
                            ))
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public ArrayList<Checkpoint> getAll() {
        ArrayList<Checkpoint> result = checkpointDao.getAll(rideoutId);

        if (result == null) throw new NotFoundException();
        return result;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)@Operation(
            summary = "Get checkpoint",
            tags = {"rideout", "checkpoint"},
            description = "Returns all checkpoints for given rideout",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Selected checkpoint", content = @Content(
                            schema = @Schema(implementation = Checkpoint.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public Checkpoint getById(@Parameter(description = "Checkpoint ID") @PathParam("id") ObjectId id) {
        Checkpoint result = checkpointDao.getById(rideoutId, id);

        if (result == null) throw new NotFoundException();
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Create checkpoint",
            tags = {"rideout", "checkpoint"},
            description = "Inserts new checkpoint into the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Created checkpoint", content = @Content(
                            schema = @Schema(implementation = Checkpoint.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Invalid user role"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            },
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Checkpoint.class)))
    )
    public Checkpoint insert(Checkpoint checkpoint, @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) throw new UnauthorizedException();

        checkpoint.setId(new ObjectId());
        BeanValidation.validate(checkpoint);

        Checkpoint result = checkpointDao.insert(rideoutId, checkpoint);
        if (result == null) throw new NotFoundException();
        return result;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Update checkpoint",
            tags = {"rideout", "checkpoint"},
            description = "Updates checkpoint in the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated checkpoint", content = @Content(
                            schema = @Schema(implementation = Checkpoint.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Invalid user role"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            },
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Checkpoint.class)))
    )
    public Checkpoint update(Checkpoint checkpoint, @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) throw new UnauthorizedException();
        BeanValidation.validate(checkpoint);

        Checkpoint result = checkpointDao.update(rideoutId, checkpoint);
        if (result == null) throw new NotFoundException();
        return result;
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Delete checkpoint",
            tags = {"rideout", "checkpoint"},
            description = "Deletes checkpoint from a rideout",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deleted checkpoint", content = @Content(
                            schema = @Schema(implementation = Checkpoint.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Invalid user role"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public Checkpoint delete(@Parameter(description = "Checkpoint ID") @PathParam("id") ObjectId id,
                             @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(User.STAFF)) throw new UnauthorizedException();

        Checkpoint result = checkpointDao.delete(rideoutId, id);
        if (result == null) throw new NotFoundException();
        return result;
    }
}