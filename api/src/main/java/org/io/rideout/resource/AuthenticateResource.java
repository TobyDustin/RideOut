package org.io.rideout.resource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.io.rideout.PasswordManager;
import org.io.rideout.database.UserDao;
import org.io.rideout.model.User;
import org.io.rideout.model.UserCredentials;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("authenticate")
public class AuthenticateResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticate(UserCredentials credentials) {
        if (authenticate(credentials.getUsername(), credentials.getPassword())) {
            String token = issueToken("12345", "jsmith");
            return Response.ok(token).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private boolean authenticate(String username, String password) {
        User user = UserDao.getInstance().getByUsername(username);

        if (user == null) return false;
        return PasswordManager.verify(password, user.getPassword());
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
