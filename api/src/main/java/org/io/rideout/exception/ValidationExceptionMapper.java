package org.io.rideout.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<AppValidationException> {
    @Override
    public Response toResponse(AppValidationException e) {
        return Response.status(400).entity(e.getErrors()).build();
    }
}
