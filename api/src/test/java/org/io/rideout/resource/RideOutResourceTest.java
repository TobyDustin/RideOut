package org.io.rideout.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.io.rideout.HttpTestServer;
import org.io.rideout.model.RideOut;
import org.io.rideout.model.StayOut;
import org.io.rideout.model.TourOut;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class RideOutResourceTest {


    private static org.glassfish.grizzly.http.server.HttpServer server;
    private static WebTarget target;

    @BeforeClass
    public static void setUp() {
        server = HttpTestServer.startServer();
        Client c = ClientBuilder.newClient();

        target = c.target(HttpTestServer.BASE_URI);
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
    }

    @Test
    public void testGetAllRideOut() throws IOException {
        Response response = target.path("rideout").request().get();
        ArrayList result = response.readEntity(ArrayList.class);
        assertEquals(200, response.getStatus());
        assertEquals(2, result.size());
        ObjectMapper mapper = new ObjectMapper();
        RideOut rideOut = mapper.convertValue(result.get(0), RideOut.class);
        StayOut stayOut = mapper.convertValue(result.get(1), StayOut.class);
        testRideOut(rideOut);
        testStayOut(stayOut);
    }

    @Test
    public void testGetRideOutNotFound(){
        Response response = target.path("rideout/" + new ObjectId(new Date(54321)).toHexString()).request().get();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testGetRideOutSuccess() {
        Response response = target.path("rideout/" + RideOutResource.ID_12345.toHexString()).request().get();
        RideOut rideOut = response.readEntity(RideOut.class);
        assertEquals(200, response.getStatus());
        testRideOut(rideOut);
    }

    @Test
    public void testAddUserSuccess() {
        String id = RideOutResource.ID_12345.toHexString();
        String rid = UserResource.UID_12345.toHexString();
        Response response = target.path("rideout/" + id + "/rider/" + rid).request().put(Entity.text(""));
        RideOut rideOut = response.readEntity(RideOut.class);
        assertEquals(200, response.getStatus());
        testRideOut(rideOut);
        UserResourceTest.testRider(rideOut.getRiders().get(0));
    }

    @Test
    public void testAddUserRideOutNotFound() {
        String id = new ObjectId(new Date(12121)).toHexString();
        String rid = RideOutResource.ID_12345.toHexString();

        Response response = target.path("rideout/" + id + "/rider/" + rid).request().put(Entity.text(""));
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testAddUserNotFound() {
        String id = RideOutResource.ID_12345.toHexString();
        String rid = new ObjectId(new Date(54321)).toHexString();

        Response response = target.path("rideout/" + id + "/rider/" + rid).request().put(Entity.text(""));
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testRemoveUserSuccess() {
        String id = RideOutResource.ID_12345.toHexString();
        Response response = target.path("rideout/" + id + "/rider/" + id).request().delete();
    }

    @Test
    public void testGetRideOuts() {
        Response response = target.path("rideout/ride").request().get();
        ArrayList<RideOut> rideOuts = response.readEntity(new GenericType<ArrayList<RideOut>>() {});
        testRideOut(rideOuts.get(0));
    }

    @Test
    public void testGetStayOuts() {
        Response response = target.path("rideout/stay").request().get();
        ArrayList<StayOut> stayOuts = response.readEntity(new GenericType<ArrayList<StayOut>>() {});
        testStayOut(stayOuts.get(0));
    }

    @Test
    public void testGetTourOuts() {
        Response response = target.path("rideout/tour").request().get();
        ArrayList<TourOut> tourOuts = response.readEntity(new GenericType<ArrayList<TourOut>>() {});
        testTourOut(tourOuts.get(0));
    }

    @Test
    public void testPutRideOut() {
        String id = RideOutResource.ID_12345.toHexString();
        String body = "{\"id\":\"" + id + "\",\"rideoutType\":\"Ride\",\"name\":\"Ride around the candovers\",\"dateStart\":\"100\",\"dateEnd\":\"100\",\"maxRiders\":\"15\",\"leadRider\":\"54321\",\"route\":\"https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx\",\"minCancellationDate\":\"100\"}";
        Response response = target.path("rideout/" + id).request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testRideOut(response.readEntity(RideOut.class));
    }

    @Test
    public void testRemoveUserRideOutNotFound() {
        String id = new ObjectId(new Date(12121)).toHexString();
        String rid = RideOutResource.ID_12345.toHexString();

        Response response = target.path("rideout/" + id + "/rider/" + rid).request().delete();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testRemoveUserNotFound() {
        String id = RideOutResource.ID_12345.toHexString();
        String rid = new ObjectId(new Date(54321)).toHexString();

        Response response = target.path("rideout/" + id + "/rider/" + rid).request().delete();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testPutRideOutSuccess() {
        String id = RideOutResource.ID_12345.toHexString();

        String body = "{\"id\":\"" + id + "\",\"rideoutType\":\"Ride\",\"name\":\"Ride around the candovers\",\"dateStart\":\"100\",\"dateEnd\":\"100\",\"maxRiders\":\"15\",\"leadRider\":\"54321\",\"route\":\"https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx\",\"minCancellationDate\":\"100\"}";
        Response response = target.path("rideout/" + id).request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testRideOut(response.readEntity(RideOut.class));
    }

    @Test
    public void testPutRiderNotFound() {
        String id = new ObjectId(new Date(54321)).toHexString();
        String fid = new ObjectId(new Date(5555)).toHexString();

        String body = "{\"id\":\""+id+"\",\"rideoutType\":\"Ride\",\"name\":\"Ride around the candovers\",\"dateStart\":\"100\",\"dateEnd\":\"100\",\"maxRiders\":\"15\",\"leadRider\":\"54321\",\"route\":\"https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx\",\"minCancellationDate\":\"100\"}";
        Response response = target.path("rideout/" + fid).request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testRemoveRideOut() {
        String id = RideOutResource.ID_12345.toHexString();

        Response response = target.path("rideout/" + id).request().delete();
        RideOut rideOut = response.readEntity(RideOut.class);

        testRideOut(rideOut);
    }

    private void testRideOut(RideOut rideOut) {
        assertNotNull(rideOut);
        assertEquals(RideOutResource.ID_12345, rideOut.getId());
        assertEquals("Ride around the candovers", rideOut.getName());
        assertEquals(new Date(100),  rideOut.getDateStart());
        assertEquals(new Date(100), rideOut.getDateEnd());
        assertEquals(15, rideOut.getMaxRiders());
        assertEquals("54321", rideOut.getLeadRider());
        assertEquals("https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx", rideOut.getRoute());
        assertEquals(new Date(100), rideOut.getMinCancellationDate());
        assertFalse(rideOut.isPublished());
    }

    private void testStayOut(StayOut stayOut) {
        assertNotNull(stayOut);
        assertEquals(RideOutResource.ID_23456, stayOut.getId());
        assertEquals("Stay around the candovers", stayOut.getName());
        assertEquals(new Date(200),  stayOut.getDateStart());
        assertEquals(new Date(200), stayOut.getDateEnd());
        assertEquals(10, stayOut.getMaxRiders());
        assertEquals("1234", stayOut.getLeadRider());
        assertEquals("https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx", stayOut.getRoute());
        assertEquals(new Date(200), stayOut.getMinCancellationDate());
        assertFalse(stayOut.isPublished());
        assertEquals(RideOutResource.BID_1234, stayOut.getAccommodationList().get(0).getId());
        assertEquals("Marriot Hotel", stayOut.getAccommodationList().get(0).getName());
        assertEquals("ABCDE", stayOut.getAccommodationList().get(0).getReference());
        assertEquals(RideOutResource.BID_4321, stayOut.getRestaurantList().get(0).getId());
        assertEquals("KFC", stayOut.getRestaurantList().get(0).getName());
        assertEquals("", stayOut.getRestaurantList().get(0).getReference());
    }

    private void testTourOut(TourOut tourOut) {
        assertNotNull(tourOut);
        assertEquals(RideOutResource.ID_34567, tourOut.getId());
        assertEquals("Tour around the candovers", tourOut.getName());
        assertEquals(new Date(300),  tourOut.getDateStart());
        assertEquals(new Date(300), tourOut.getDateEnd());
        assertEquals(5, tourOut.getMaxRiders());
        assertEquals("2345", tourOut.getLeadRider());
        assertEquals("https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx", tourOut.getRoute());
        assertEquals(new Date(300), tourOut.getMinCancellationDate());
        assertFalse(tourOut.isPublished());
        assertEquals(RideOutResource.BID_1234, tourOut.getAccommodationList().get(0).getId());
        assertEquals("Marriot Hotel", tourOut.getAccommodationList().get(0).getName());
        assertEquals("ABCDE", tourOut.getAccommodationList().get(0).getReference());
        assertEquals(RideOutResource.BID_4321, tourOut.getRestaurantList().get(0).getId());
        assertEquals("KFC", tourOut.getRestaurantList().get(0).getName());
        assertEquals("", tourOut.getRestaurantList().get(0).getReference());
        assertEquals(RideOutResource.BID_9876, tourOut.getTravelBookings().get(0).getId());
        assertEquals("Condor Ferries", tourOut.getTravelBookings().get(0).getName());
        assertEquals("QWERTY", tourOut.getTravelBookings().get(0).getReference());
    }



}
