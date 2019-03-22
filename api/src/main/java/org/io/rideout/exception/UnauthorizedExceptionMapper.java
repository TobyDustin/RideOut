package org.io.rideout.exception;

import org.io.rideout.authentication.AuthenticationFilter;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static org.io.rideout.authentication.AuthenticationFilter.AUTHENTICATION_SCHEMA;
import static org.io.rideout.authentication.AuthenticationFilter.REALM;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
    @Override
    public Response toResponse(UnauthorizedException e) {
        return AuthenticationFilter.getUnauthorizedResponse();
    }
}
