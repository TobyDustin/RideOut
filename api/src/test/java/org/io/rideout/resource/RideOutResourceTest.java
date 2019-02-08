package org.io.rideout.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.io.rideout.Main;
import org.io.rideout.model.RideOut;
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

public class RideOutResourceTest {


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
    public void testGetAllRideOut() throws IOException {
        Response response = target.path("/event/rideout").request().get();
        ArrayList result = response.readEntity(ArrayList.class);
        assertEquals(200, response.getStatus());
        assertEquals(1, result.size());
        ObjectMapper mapper = new ObjectMapper();
        RideOut rideOut = mapper.convertValue(result.get(0), RideOut.class);
        testRideOut(rideOut);

    }

    @Test
    public void testGetRideOutNotFound(){
        Response response = target.path("rider/54321").request().get();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testGetRideOutSuccess() {
        Response response = target.path("/event/rideout/123").request().get();
        RideOut rideOut = response.readEntity(RideOut.class);
        assertEquals(200, response.getStatus());
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

    }


}
