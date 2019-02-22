package org.io.rideout.resource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.io.rideout.model.Token;
import org.io.rideout.model.UserCredentials;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("authenticate")
public class AuthenticateResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(UserCredentials credentials) {
        if (authenticate(credentials.getUsername(), credentials.getPassword())) {
            String token = issueToken("12345", "jsmith");
            return Response.ok(new Token(token)).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private boolean authenticate(String username, String password) {
        return username.equals("jsmith") && password.equals("john123");
    }

    private String issueToken(String id, String username) {
        Algorithm algorithm = Algorithm.HMAC512("rideout-secrete-21334243215");
        return JWT.create()
                .withIssuer("rideout")
                .withSubject(id)
                .withClaim("username", username)
                .sign(algorithm);
    }
}
