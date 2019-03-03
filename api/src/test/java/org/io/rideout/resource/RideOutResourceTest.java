package org.io.rideout.resource;

import org.bson.types.ObjectId;
import org.io.rideout.HttpTestServer;
import org.io.rideout.database.TestDatabase;
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

import static javax.ws.rs.client.Entity.entity;
import static org.junit.Assert.*;

public class RideOutResourceTest {


    private static org.glassfish.grizzly.http.server.HttpServer server;
    private static WebTarget target;

    @BeforeClass
    public static void setUp() {
        TestDatabase.setUp();

        server = HttpTestServer.startServer();
        Client c = ClientBuilder.newClient();

        target = c.target(HttpTestServer.BASE_URI);
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
        TestDatabase.tearDown();
    }

    @Test
    public void testGetAllRideOut() throws IOException {
        Response response = target.path("rideout").request().get();
        ArrayList result = response.readEntity(ArrayList.class);
        assertEquals(200, response.getStatus());
        assertTrue(result.size() >= 3);
    }

    @Test
    public void testGetRideOutNotFound(){
        String id = new ObjectId().toHexString();
        Response response = target.path("rideout/" + id).request().get();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testGetRideOutSuccess() {
        String id = TestDatabase.GET_RIDEOUT.toHexString();
        Response response = target.path("rideout/" + id).request().get();
        RideOut rideOut = response.readEntity(RideOut.class);
        assertEquals(200, response.getStatus());
        testRideOut(rideOut, TestDatabase.GET_RIDEOUT);
    }

    @Test
    public void testAddUserSuccess() {
        String id = TestDatabase.ADD_RIDER_RIDEOUT.toHexString();
        String rid = TestDatabase.GET_RIDER.toHexString();
        Response response = target.path("rideout/" + id + "/rider/" + rid).request().put(Entity.text(""));
        //System.out.println(response.readEntity(String.class));
        assertEquals(200, response.getStatus());
        RideOut rideOut = response.readEntity(RideOut.class);
        testRideOut(rideOut, TestDatabase.ADD_RIDER_RIDEOUT);
        UserResourceTest.testSimpleUser(rideOut.getRiders().get(0), TestDatabase.GET_RIDER);
    }

    @Test
    public void testAddUserRideOutNotFound() {
        String id = new ObjectId().toHexString();
        String rid = TestDatabase.GET_RIDER.toHexString();

        Response response = target.path("rideout/" + id + "/rider/" + rid).request().put(Entity.text(""));
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testAddUserNotFound() {
        String id = TestDatabase.ADD_RIDER_RIDEOUT.toHexString();
        String rid = new ObjectId().toHexString();

        Response response = target.path("rideout/" + id + "/rider/" + rid).request().put(Entity.text(""));
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testRemoveUserSuccess() {
        String id = TestDatabase.REMOVE_RIDER_RIDEOUT.toHexString();
        String uid = TestDatabase.GET_RIDER.toHexString();
        Response response = target.path("rideout/" + id + "/rider/" + uid).request().delete();

        RideOut rideout = response.readEntity(RideOut.class);
        assertEquals(200, response.getStatus());
        assertNotNull(rideout);
        assertEquals(0, rideout.getRiders().size());
        testRideOut(rideout, TestDatabase.REMOVE_RIDER_RIDEOUT);
    }

    @Test
    public void testGetRideOuts() {
        Response response = target.path("rideout/ride").request().get();
        ArrayList<RideOut> rideOuts = response.readEntity(new GenericType<ArrayList<RideOut>>() {});
        assertEquals(200, response.getStatus());
        assertTrue(rideOuts.size() >= 1);
    }

    @Test
    public void testGetStayOuts() {
        Response response = target.path("rideout/stay").request().get();
        ArrayList<StayOut> stayOuts = response.readEntity(new GenericType<ArrayList<StayOut>>() {});
        assertEquals(200, response.getStatus());
        testStayOut(stayOuts.get(0), TestDatabase.GET_STAYOUT);
    }

    @Test
    public void testGetTourOuts() {
        Response response = target.path("rideout/tour").request().get();
        ArrayList<TourOut> tourOuts = response.readEntity(new GenericType<ArrayList<TourOut>>() {});
        assertEquals(200, response.getStatus());
        testTourOut(tourOuts.get(0), TestDatabase.GET_TOUROUT);
    }

    @Test
    public void testRemoveUserRideOutNotFound() {
        String id = new ObjectId().toHexString();
        String rid = TestDatabase.GET_RIDER.toHexString();

        Response response = target.path("rideout/" + id + "/rider/" + rid).request().delete();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testRemoveUserNotFound() {
        String id = TestDatabase.REMOVE_RIDER_RIDEOUT.toHexString();
        String rid = new ObjectId().toHexString();

        Response response = target.path("rideout/" + id + "/rider/" + rid).request().delete();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testPutRideOutSuccess() {
        String id = TestDatabase.PUT_RIDEOUT.toHexString();

        String body = "{\"id\":\"" + id + "\",\"rideoutType\":\"Ride\",\"name\":\"Ride around the candovers\",\"dateStart\":\"100\",\"dateEnd\":\"100\",\"maxRiders\":\"15\",\"leadRider\":\"54321\",\"route\":\"https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx\",\"minCancellationDate\":\"100\"}";
        Response response = target.path("rideout/" + id).request().put(entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testRideOut(response.readEntity(RideOut.class), TestDatabase.PUT_RIDEOUT);
    }

    @Test
    public void testPutRiderNotFound() {
        String id = TestDatabase.ADD_RIDER_RIDEOUT.toHexString();
        String uid = new ObjectId().toHexString();

        String body = "{\"id\":\""+id+"\",\"rideoutType\":\"Ride\",\"name\":\"Ride around the candovers\",\"dateStart\":\"100\",\"dateEnd\":\"100\",\"maxRiders\":\"15\",\"leadRider\":\"54321\",\"route\":\"https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx\",\"minCancellationDate\":\"100\"}";
        Response response = target.path("rideout/" + uid).request().put(entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testRemoveRideOut() {
        String id = TestDatabase.DELETE_RIDEOUT.toHexString();

        Response response = target.path("rideout/" + id).request().delete();
        RideOut rideOut = response.readEntity(RideOut.class);

        testRideOut(rideOut, TestDatabase.DELETE_RIDEOUT);
    }

    @Test
    public void testPostRideOut() {
        Response response = target.path("rideout").request()
                .post(entity(new RideOut(
                        null,
                        "Ride around the candovers",
                        new Date(100),
                        new Date(100),
                        15,
                        "54321",
                        "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                        new Date(100)

                ), MediaType.APPLICATION_JSON_TYPE));

        RideOut rideout = response.readEntity(RideOut.class);
        assertEquals(200, response.getStatus());
        testRideOut(rideout, null);
    }

    private void testRideOut(RideOut rideOut, ObjectId id) {
        assertNotNull(rideOut);
        if (id != null) assertEquals(id, rideOut.getId());
        assertEquals("Ride around the candovers", rideOut.getName());
        assertEquals(new Date(100),  rideOut.getDateStart());
        assertEquals(new Date(100), rideOut.getDateEnd());
        assertEquals(15, rideOut.getMaxRiders());
        assertEquals("54321", rideOut.getLeadRider());
        assertEquals("https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx", rideOut.getRoute());
        assertEquals(new Date(100), rideOut.getMinCancellationDate());
        assertFalse(rideOut.isPublished());
    }

    private void testStayOut(StayOut stayOut, ObjectId id) {
        assertNotNull(stayOut);
        assertEquals(id, stayOut.getId());
        assertEquals("Stay around the candovers", stayOut.getName());
        assertEquals(new Date(200),  stayOut.getDateStart());
        assertEquals(new Date(200), stayOut.getDateEnd());
        assertEquals(10, stayOut.getMaxRiders());
        assertEquals("1234", stayOut.getLeadRider());
        assertEquals("https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx", stayOut.getRoute());
        assertEquals(new Date(200), stayOut.getMinCancellationDate());
        assertFalse(stayOut.isPublished());
        assertEquals(TestDatabase.BOOKING_1, stayOut.getAccommodationList().get(0).getId());
        assertEquals("Marriot Hotel", stayOut.getAccommodationList().get(0).getName());
        assertEquals("ABCDE", stayOut.getAccommodationList().get(0).getReference());
        assertEquals(TestDatabase.BOOKING_2, stayOut.getRestaurantList().get(0).getId());
        assertEquals("KFC", stayOut.getRestaurantList().get(0).getName());
        assertEquals("", stayOut.getRestaurantList().get(0).getReference());
    }

    private void testTourOut(TourOut tourOut, ObjectId id) {
        assertNotNull(tourOut);
        assertEquals(id, tourOut.getId());
        assertEquals("Tour around the candovers", tourOut.getName());
        assertEquals(new Date(300),  tourOut.getDateStart());
        assertEquals(new Date(300), tourOut.getDateEnd());
        assertEquals(5, tourOut.getMaxRiders());
        assertEquals("2345", tourOut.getLeadRider());
        assertEquals("https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx", tourOut.getRoute());
        assertEquals(new Date(300), tourOut.getMinCancellationDate());
        assertFalse(tourOut.isPublished());
        assertEquals(TestDatabase.BOOKING_1, tourOut.getAccommodationList().get(0).getId());
        assertEquals("Marriot Hotel", tourOut.getAccommodationList().get(0).getName());
        assertEquals("ABCDE", tourOut.getAccommodationList().get(0).getReference());
        assertEquals(TestDatabase.BOOKING_2, tourOut.getRestaurantList().get(0).getId());
        assertEquals("KFC", tourOut.getRestaurantList().get(0).getName());
        assertEquals("", tourOut.getRestaurantList().get(0).getReference());
        assertEquals(TestDatabase.BOOKING_3, tourOut.getTravelBookings().get(0).getId());
        assertEquals("Condor Ferries", tourOut.getTravelBookings().get(0).getName());
        assertEquals("QWERTY", tourOut.getTravelBookings().get(0).getReference());
    }



}
