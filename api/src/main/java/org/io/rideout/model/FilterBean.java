package org.io.rideout.model;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

public class FilterBean {

    @QueryParam("available")
    public boolean showOnlyVacant = false;

    @QueryParam("attending")
    public boolean showOnlyUsers = false;

    @QueryParam("type")
    public List<String> types = new ArrayList<>();

    @Context
    public SecurityContext securityContext;
}
