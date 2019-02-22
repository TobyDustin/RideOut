package org.io.rideout.resource;

import org.bson.types.ObjectId;
import org.io.rideout.database.RideOutDao;
import org.io.rideout.database.UserDao;
import org.io.rideout.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;

@Path("rideout")
public class RideOutResource {

    private RideOutDao rideoutDao = RideOutDao.getInstance();
    private UserDao userDao = UserDao.getInstance();

    // GET all ride outs
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<RideOut> getAllRideOuts() {
        return rideoutDao.getAll();
    }

    // GET ride out by id
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut getRideOut(@PathParam("id") ObjectId id) {
        RideOut result = rideoutDao.getById(id);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // GET ride outs with type ride
    @GET
    @Path("ride")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<RideOut> getRideOuts() {
        return rideoutDao.getAllByType("ride");
    }

    // GET ride outs with type stay
    @GET
    @Path("stay")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<StayOut> getStayOuts() {
        ArrayList<StayOut> result = new ArrayList<>();

        for (RideOut ride : rideoutDao.getAllByType("stay")) {
            if (ride instanceof StayOut) result.add((StayOut) ride);
        }

        return result;
    }

    // GET ride outs with type tour
    @GET
    @Path("tour")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<TourOut> getTourOuts() {
        ArrayList<TourOut> result = new ArrayList<>();

        for (RideOut ride : rideoutDao.getAllByType("tour")) {
            if (ride instanceof TourOut) result.add((TourOut) ride);
        }

        return result;
    }

    // UPDATE rideout
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public RideOut updateRideOut(@PathParam("id") ObjectId id, RideOut rideOut) {
        RideOut result = rideoutDao.update(id, rideOut);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // POST ride out
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public RideOut addRideOut(RideOut rideOut) {
        return rideoutDao.insert(rideOut);
    }

    // DELETE ride out
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut deleteRideOut(@PathParam("id") ObjectId id) {
        RideOut result = rideoutDao.delete(id);

        if (result != null) return result;
        throw new NotFoundException();
    }

    // ADD rider to ride out
    @PUT
    @Path("{rideOutId}/rider/{riderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut addRider(@PathParam("rideOutId") ObjectId rideOutId, @PathParam("riderId") ObjectId riderId) {
        User rider = userDao.getById(riderId);
        if (rider == null) throw new NotFoundException("Rider not found");

        RideOut result = rideoutDao.addRider(rideOutId, rider);

        if (result != null) return result;
        throw new NotFoundException("Rideout not found");
    }

    // REMOVE rider from ride out
    @DELETE
    @Path("{rideOutId}/rider/{riderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RideOut removeRider(@PathParam("rideOutId") ObjectId rideOutId, @PathParam("riderId") ObjectId riderId) {
        User rider = userDao.getById(riderId);
        if (rider == null) throw new NotFoundException("Rider not found");

        RideOut result = rideoutDao.removeRider(rideOutId, rider);

        if (result != null) return result;
        throw new NotFoundException("Rideout not found");
    }
}
