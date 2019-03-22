package org.io.rideout.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

public class FilterBean {

    @QueryParam("available")
    @Parameter(description = "Only returns RideOuts that are not full")
    public boolean showOnlyVacant = false;

    @QueryParam("attending")
    @Parameter(description = "Only returns RideOuts which user is attending")
    public boolean showOnlyUsers = false;

    @QueryParam("type")
    @Parameter(description = "Returns RideOuts of specified type")
    public List<String> types = new ArrayList<>();

    @Context
    public SecurityContext securityContext;
}
