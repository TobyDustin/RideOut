package org.io.rideout.resource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

@Path("authenticate")
public class AuthenticateResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(UserCredentials credentials) {
        User user = UserDao.getInstance().getByUsername(credentials.getUsername());
        if (authenticate(credentials.getPassword(), user)) {
            String token = issueToken(user.getId().toHexString(), user.getUsername());
            return Response.ok(new Token(token)).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private boolean authenticate(String password, User user) {
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
