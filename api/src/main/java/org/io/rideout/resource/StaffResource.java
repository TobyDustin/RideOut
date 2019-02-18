package org.io.rideout.resource;

import org.bson.types.ObjectId;
import org.io.rideout.model.Staff;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;

@Path("staff")
public class StaffResource {

    public static ObjectId UID_12345 = new ObjectId("5c6a97fe3bd3d419a78de2c4");
    public static ObjectId UID_54321 = new ObjectId("5c6a97fe3bd3d419a78de2c5");

    // GET all staff
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Staff> getAllStaff() {
        ArrayList<Staff> result = new ArrayList<>();
        result.add(getDummyStaff());
        return result;
    }

    // GET staff by id
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Staff getStaff(@PathParam("id") ObjectId id) {
        if (id.equals(UID_12345)) {
            return getDummyStaff();
        }

        throw new NotFoundException();
    }

    // UPDATE staff
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Staff updateStaff(@PathParam("id") ObjectId id, Staff staff) {
        if (id.equals(UID_54321)) {
            return staff;
        }

        throw new NotFoundException();
    }

    // CREATE staff
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Staff addStaff(Staff staff) {
        staff.setId(UID_12345);
        return staff;
    }

    // DELETE staff
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Staff deleteStaff(@PathParam("id") ObjectId id) {
        if (id.equals(UID_12345)) {
            return getDummyStaff();
        }

        throw new NotFoundException();
    }

    // ======= DUMMY DATA ========

    private Staff getDummyStaff() {
        return new Staff(
                UID_12345,
                "jsmith",
                "john123",
                "John",
                "Smith",
                new Date(100),
                "07491012345",
                false
        );
    }
}
