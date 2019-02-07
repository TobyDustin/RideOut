package org.io.rideout.resource;

import org.io.rideout.model.Staff;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;

@Path("staff")
public class StaffResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Staff> getAllStaff() {
        ArrayList<Staff> result = new ArrayList<>();
        result.add(getDummyStaff());
        return result;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Staff getStaff(@PathParam("id") String id) {
        if (id.equals("12345")) {
            return getDummyStaff();
        }

        throw new NotFoundException();
    }

    private Staff getDummyStaff() {
        return new Staff(
                "12345",
                "jsmith",
                "John",
                "Smith",
                new Date(100),
                "07491012345",
                false
        );
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Staff addStaff() {
        throw new NotImplementedException();
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Staff updateStaff(@PathParam("id") String id) {
        throw new NotImplementedException();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Staff deleteStaff(@PathParam("id") String id) {
        throw new NotImplementedException();
    }
}
