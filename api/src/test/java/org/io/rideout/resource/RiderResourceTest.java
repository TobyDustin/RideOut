package org.io.rideout.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.io.rideout.Main;
import org.io.rideout.model.Rider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class RiderResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() {
        server = Main.startServer();
        Client c = ClientBuilder.newClient();

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testGetAllRiders() throws IOException {
        Response response = target.path("/rider").request().get();

        ArrayList result = response.readEntity(ArrayList.class);

        assertEquals(200, response.getStatus());
        assertEquals(1, result.size());

        ObjectMapper mapper = new ObjectMapper();
        Rider rider = mapper.convertValue(result.get(0), Rider.class);
        testRider(rider);
    }

    @Test
    public void testGetRiderSuccess() {
        Response response = target.path("/rider/12345").request().get();

        Rider rider = response.readEntity(Rider.class);
        assertEquals(200, response.getStatus());
        testRider(rider);
    }

    @Test
    public void testGetRiderNotFound() {
        Response response = target.path("rider/54321").request().get();

        assertEquals(404, response.getStatus());
    }

    private void testRider(Rider rider) {
        assertNotNull(rider);
        assertEquals("12345", rider.getId());
        assertEquals("jsmith", rider.getUsername());
        assertEquals("John",  rider.getFirstName());
        assertEquals("Smith", rider.getLastName());
        assertEquals(new Date(100), rider.getDateOfBirth());
        assertEquals("07491012345", rider.getContactNumber());
        assertEquals("999", rider.getEmergencyContactNumber());
        assertTrue(rider.isInsured());
        assertFalse(rider.isLead());
        assertEquals("A", rider.getLicense());
    }
}
