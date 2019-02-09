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
    @Consumes(MediaType.APPLICATION_JSON)
    public Staff addStaff(Staff staff) {
        staff.setId("12345");
        return staff;
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Staff updateStaff(@PathParam("id") String id, Staff staff) {
        if (id.equals("54321")) {
            return staff;
        }

        throw new NotFoundException();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Staff deleteStaff(@PathParam("id") String id) {
        if (id.equals("12345")) {
            return getDummyStaff();
        }

        throw new NotFoundException();
    }
}
