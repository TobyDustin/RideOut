package org.io.rideout.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.bson.types.ObjectId;
import org.io.rideout.BeanValidation;
import org.io.rideout.authentication.Secured;
import org.io.rideout.database.UserDao;
import org.io.rideout.model.RiderInformation;
import org.io.rideout.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public class RiderInformationResource {

    private UserDao userDao = UserDao.getInstance();
    private ObjectId userId;

    public RiderInformationResource(ObjectId userId) {
        this.userId = userId;
    }

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Returns Rider information",
            tags = {"user", "rider information"},
            description = "Returns Rider information for given user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rider Information", content = @Content(
                            schema = @Schema(implementation = RiderInformation.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "User or rider information not found")
            },
            security = @SecurityRequirement(
                    name = "JWT"
            )
    )
    public RiderInformation getRiderInformation() {
        User result = userDao.getById(userId);

        if (result == null || result.getRiderInformation() == null) throw new NotFoundException();
        return result.getRiderInformation();
    }

    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Create/Update rider information",
            tags = {"user", "rider information"},
            description = "Creates or updates rider information for given user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rider Information", content = @Content(
                            schema = @Schema(implementation = RiderInformation.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "User or rider information not found")
            },
            security = @SecurityRequirement(
                    name = "JWT"
            ),
            requestBody = @RequestBody(description = "New rider information to be saved", content = @Content(
                    schema = @Schema(implementation = RiderInformation.class)
            ))
    )
    public RiderInformation updateRiderInformation(RiderInformation riderInformation) {
        BeanValidation.validate(riderInformation);
        User temp = userDao.getById(userId);

        if (temp == null) throw new NotFoundException();
        temp.setRiderInformation(riderInformation);

        User result = userDao.update(temp);
        if (result == null || result.getRiderInformation() == null) throw new NotFoundException();
        return result.getRiderInformation();
    }

    @DELETE
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Create/Update rider information",
            tags = {"user", "rider information"},
            description = "Creates or updates rider information for given user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rider Information", content = @Content(
                            schema = @Schema(implementation = RiderInformation.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User unauthorized"),
                    @ApiResponse(responseCode = "404", description = "User or rider information not found")
            },
            security = @SecurityRequirement(
                    name = "JWT"
            )
    )
    public RiderInformation removeRiderInformation() {
        User temp = userDao.getById(userId);

        if (temp == null) throw new NotFoundException();
        temp.setRiderInformation(null);

        User result = userDao.update(temp);
        if (result == null || result.getRiderInformation() == null) throw new NotFoundException();
        return result.getRiderInformation();
    }
}
