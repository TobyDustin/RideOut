package org.io.rideout.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.bson.types.ObjectId;
import org.io.rideout.database.UserDao;
import org.io.rideout.model.User;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import static javax.ws.rs.Priorities.AUTHENTICATION;

@Secured
@Provider
@Priority(AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    public static final String REALM = "rideout";
    public static final String AUTHENTICATION_SCHEMA = "Bearer";

    @Context
    UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!isTokenBasedAuthentication(authHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        String token = authHeader.substring(AUTHENTICATION_SCHEMA.length()).trim();

        String userId = validateToken(token);

        if (userId == null) {
            abortWithUnauthorized(requestContext);
        } else {
            User authUser = UserDao.getInstance().getById(new ObjectId(userId));
            if (authUser == null) abortWithUnauthorized(requestContext);

            boolean secure = uriInfo.getAbsolutePath().toString().startsWith("https");
            requestContext.setSecurityContext(new UserSecurityContext(authUser, AUTHENTICATION_SCHEMA, secure));
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEMA.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(getUnauthorizedResponse());
    }

    public static Response getUnauthorizedResponse() {
        return Response.status(Response.Status.UNAUTHORIZED)
            .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEMA + " realm=\"" + REALM + "\"")
            .build();
    }

    public static String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512("rideout-secrete-21334243215");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("rideout")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
