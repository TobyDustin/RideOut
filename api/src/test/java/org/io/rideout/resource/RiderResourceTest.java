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

        assert response.getStatus() == 200;
        assert result.size() == 1;

        ObjectMapper mapper = new ObjectMapper();
        Rider rider = mapper.convertValue(result.get(0), Rider.class);
        testRider(rider);
    }

    @Test
    public void testGetRiderSuccess() {
        Response response = target.path("/rider/12345").request().get();

        Rider rider = response.readEntity(Rider.class);
        assert response.getStatus() == 200;
        testRider(rider);
    }

    @Test
    public void testGetRiderNotFound() {
        Response response = target.path("rider/54321").request().get();

        assert response.getStatus() == 404;
    }

    private void testRider(Rider rider) {
        assert rider != null;
        assert rider.getId().equals("12345");
        assert rider.getUsername().equals("jsmith");
        assert rider.getFirstName().equals("John");
        assert rider.getLastName().equals("Smith");
        assert rider.getDateOfBirth().equals(new Date(100));
        assert rider.getContactNumber().equals("07491012345");
        assert rider.getEmergencyContactNumber().equals("999");
        assert rider.isInsured();
        assert !rider.isLead();
        assert rider.getLicense().equals("A");
    }
}
