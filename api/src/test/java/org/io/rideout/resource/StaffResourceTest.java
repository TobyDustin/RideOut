package org.io.rideout.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.io.rideout.Main;
import org.io.rideout.model.Staff;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class StaffResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testGetAllStaff() {
        Response response = target.path("/staff").request().get();

        ArrayList result = response.readEntity(ArrayList.class);

        assertEquals(200, response.getStatus());
        assertEquals(1, result.size());

        ObjectMapper mapper = new ObjectMapper();
        Staff staff = mapper.convertValue(result.get(0), Staff.class);
        testStaff(staff);
    }

    @Test
    public void testGetStaffSuccess() {
        Response response = target.path("/staff/12345").request().get();

        Staff staff = response.readEntity(Staff.class);
        assertEquals(200, response.getStatus());
        testStaff(staff);
    }

    @Test
    public void testGetStaffNotFound() {
        Response response = target.path("/staff/54321").request().get();

        assertEquals(404, response.getStatus());
    }

    private void testStaff(Staff staff) {
        assertNotNull(staff);
        assertEquals("12345", staff.getId());
        assertEquals("jsmith", staff.getUsername());
        assertEquals("John", staff.getFirstName());
        assertEquals("Smith", staff.getLastName());
        assertEquals(new Date(100), staff.getDateOfBirth());
        assertFalse(staff.isAdmin());
    }
}
