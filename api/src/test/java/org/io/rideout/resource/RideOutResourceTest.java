package org.io.rideout.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.io.rideout.Main;
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


    private static HttpServer server;
    private static WebTarget target;

    @BeforeClass
    public static void setUp() {
        server = Main.startServer();
        Client c = ClientBuilder.newClient();

        target = c.target(Main.BASE_URI);
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
        Response response = target.path("rideout/54321").request().get();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testGetRideOutSuccess() {
        Response response = target.path("rideout/12345").request().get();
        RideOut rideOut = response.readEntity(RideOut.class);
        assertEquals(200, response.getStatus());
        testRideOut(rideOut);
    }

    @Test
    public void testAddUserSuccess() {
        Response response = target.path("rideout/12345/rider/12345").request().put(Entity.text(""));
        RideOut rideOut = response.readEntity(RideOut.class);
        assertEquals(200, response.getStatus());
        testRideOut(rideOut);
        RiderResourceTest.testRider(rideOut.getRiders().get(0));
    }

    @Test
    public void testAddUserRideOutNotFound() {
        Response response = target.path("rideout/12121/rider/12345").request().put(Entity.text(""));
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testAddUserNotFound() {
        Response response = target.path("rideout/12345/rider/54321").request().put(Entity.text(""));
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testRemoveUserSuccess() {
        Response response = target.path("rideout/12345/rider/12345").request().delete();
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
    public void testPutRideOut(){
        String body = "{\"id\":\"12345\",\"rideoutType\":\"Ride\",\"name\":\"Ride around the candovers\",\"dateStart\":\"100\",\"dateEnd\":\"100\",\"maxRiders\":\"15\",\"leadRider\":\"54321\",\"route\":\"https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx\",\"minCancellationDate\":\"100\"}";
        Response response = target.path("rideout/12345").request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testRideOut(response.readEntity(RideOut.class));
    }

    @Test
    public void testRemoveUserRideOutNotFound() {
        Response response = target.path("rideout/12121/rider/12345").request().delete();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testRemoveUserNotFound() {
        Response response = target.path("rideout/12345/rider/54321").request().delete();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testPutRideOutSuccess() {
        String body = "{\"id\":\"12345\",\"rideoutType\":\"Ride\",\"name\":\"Ride around the candovers\",\"dateStart\":\"100\",\"dateEnd\":\"100\",\"maxRiders\":\"15\",\"leadRider\":\"54321\",\"route\":\"https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx\",\"minCancellationDate\":\"100\"}";
        Response response = target.path("rideout/12345").request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testRideOut(response.readEntity(RideOut.class));
    }

    @Test
    public void testPutRiderNotFound() {
        String body = "{\"id\":\"54321\",\"rideoutType\":\"Ride\",\"name\":\"Ride around the candovers\",\"dateStart\":\"100\",\"dateEnd\":\"100\",\"maxRiders\":\"15\",\"leadRider\":\"54321\",\"route\":\"https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx\",\"minCancellationDate\":\"100\"}";
        Response response = target.path("rideout/5555").request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testRemoveRideOut() {
        Response response = target.path("rideout/12345").request().delete();
        RideOut rideOut = response.readEntity(RideOut.class);

        testRideOut(rideOut);
    }

    private void testRideOut(RideOut rideOut) {
        assertNotNull(rideOut);
        assertEquals("12345", rideOut.getId());
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
        assertEquals("23456", stayOut.getId());
        assertEquals("Stay around the candovers", stayOut.getName());
        assertEquals(new Date(200),  stayOut.getDateStart());
        assertEquals(new Date(200), stayOut.getDateEnd());
        assertEquals(10, stayOut.getMaxRiders());
        assertEquals("1234", stayOut.getLeadRider());
        assertEquals("https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx", stayOut.getRoute());
        assertEquals(new Date(200), stayOut.getMinCancellationDate());
        assertFalse(stayOut.isPublished());
        assertEquals("1234", stayOut.getAccommodationList().get(0).getId());
        assertEquals("Marriot Hotel", stayOut.getAccommodationList().get(0).getName());
        assertEquals("ABCDE", stayOut.getAccommodationList().get(0).getReference());
        assertEquals("4321", stayOut.getRestaurantList().get(0).getId());
        assertEquals("KFC", stayOut.getRestaurantList().get(0).getName());
        assertEquals("", stayOut.getRestaurantList().get(0).getReference());
    }

    private void testTourOut(TourOut tourOut) {
        assertNotNull(tourOut);
        assertEquals("34567", tourOut.getId());
        assertEquals("Tour around the candovers", tourOut.getName());
        assertEquals(new Date(300),  tourOut.getDateStart());
        assertEquals(new Date(300), tourOut.getDateEnd());
        assertEquals(5, tourOut.getMaxRiders());
        assertEquals("2345", tourOut.getLeadRider());
        assertEquals("https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx", tourOut.getRoute());
        assertEquals(new Date(300), tourOut.getMinCancellationDate());
        assertFalse(tourOut.isPublished());
        assertEquals("1234", tourOut.getAccommodationList().get(0).getId());
        assertEquals("Marriot Hotel", tourOut.getAccommodationList().get(0).getName());
        assertEquals("ABCDE", tourOut.getAccommodationList().get(0).getReference());
        assertEquals("4321", tourOut.getRestaurantList().get(0).getId());
        assertEquals("KFC", tourOut.getRestaurantList().get(0).getName());
        assertEquals("", tourOut.getRestaurantList().get(0).getReference());
        assertEquals("9876", tourOut.getTravelBookings().get(0).getId());
        assertEquals("Condor Ferries", tourOut.getTravelBookings().get(0).getName());
        assertEquals("QWERTY", tourOut.getTravelBookings().get(0).getReference());
    }



}
