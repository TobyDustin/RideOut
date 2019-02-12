package org.io.rideout.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.io.rideout.Main;
import org.io.rideout.model.RideOut;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
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
        assertEquals(1, result.size());
        ObjectMapper mapper = new ObjectMapper();
        RideOut rideOut = mapper.convertValue(result.get(0), RideOut.class);
        testRideOut(rideOut);

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
    public void testPutRideOut(){
        String body = "{\"name\":\"Ride around the candovers\",\"dateStart\":\"100\",\"dateEnd\":\"100\",\"maxRiders\":\"15\",\"leadRider\":\"54321\",\"route\":\"https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx\",\"minCancellationDate\":\"100\"}";
        Response response = target.path("rideout").request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testRideOut(response.readEntity(RideOut.class));
    }


    @Test
    public void testPostRideOutSuccess() {
        String body = "{\"id\":\"12345\",\"name\":\"Ride around the candovers\",\"dateStart\":\"100\",\"dateEnd\":\"100\",\"maxRiders\":\"15\",\"leadRider\":\"54321\",\"route\":\"https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx\",\"minCancellationDate\":\"100\"}";
        Response response = target.path("rideout/12345").request().post(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testRideOut(response.readEntity(RideOut.class));
    }

    @Test
    public void testPostRiderNotFound() {
        String body = "{\"id\":\"54321\",\"name\":\"Ride around the candovers\",\"dateStart\":\"100\",\"dateEnd\":\"100\",\"maxRiders\":\"15\",\"leadRider\":\"54321\",\"route\":\"https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx\",\"minCancellationDate\":\"100\"}";
        Response response = target.path("rideout/5555").request().post(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(404, response.getStatus());
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




}
