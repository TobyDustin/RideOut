package org.io.rideout.resource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.io.rideout.PasswordManager;
import org.io.rideout.database.UserDao;
import org.io.rideout.model.Token;
import org.io.rideout.model.User;
import org.io.rideout.model.UserCredentials;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@OpenAPIDefinition(
        info = @Info(
                description = "A RideOut API documentation",
                version = "0.2",
                title = "RideOut"
        ),
        servers = {
                @Server(
                        url = "https://dev.rideout.edjeffreys.com/api",
                        description = "Development"
                ),
                @Server(
                        url = "https://rideout.edjeffreys.com/api",
                        description = "Production"
                )
        }
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Path("authenticate")
public class AuthenticateResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Authenticate user",
            tags = {"authentication"},
            description = "Authenticates given credentials and returns bearer token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token", content = @Content(
                            schema = @Schema(implementation = Token.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "User credentials are invalid")
            },
            requestBody = @RequestBody(description = "User credentials", content = @Content(
                    schema = @Schema(implementation = UserCredentials.class)
            ))
    )
    public Response authenticate(UserCredentials credentials) {
        User user = UserDao.getInstance().getByUsername(credentials.getUsername());
        if (authenticate(credentials.getPassword(), user)) {
            String token = issueToken(user);
            return Response.ok(new Token(token)).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private boolean authenticate(String password, User user) {
        if (user == null) return false;
        return PasswordManager.verify(password, user.getPassword());
    }

    private String issueToken(User user) {
        Algorithm algorithm = Algorithm.HMAC512("rideout-secrete-21334243215");
        return JWT.create()
                .withIssuer("rideout")
                .withSubject(user.getId().toHexString())
                .withClaim("username", user.getUsername())
                .withClaim("role", user.getRole())
                .sign(algorithm);
    }
}


