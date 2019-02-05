package org.io.rideout;

import org.io.rideout.model.Rider;
import org.io.rideout.model.Vehicle;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("/rider/{id}")
public class RiderResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Rider getRider(@PathParam("id") String id) {
        Rider dummy = new Rider(
                "12342",
                "jsmith",
                "John",
                "Smith",
                new Date(),
                "07491012345",
                "999",
                true,
                false,
                "A"
        );
        dummy.addVehicle(new Vehicle("9876", "Honda", "Monkey", 125, "REG123"));
        return dummy;
    }
}
