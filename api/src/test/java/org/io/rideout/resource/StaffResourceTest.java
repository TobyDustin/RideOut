package org.io.rideout.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.io.rideout.Main;
import org.io.rideout.model.Rider;
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

        assert response.getStatus() == 200;
        assert result.size() == 1;

        ObjectMapper mapper = new ObjectMapper();
        Staff staff = mapper.convertValue(result.get(0), Staff.class);
        testStaff(staff);
    }

    @Test
    public void testGetStaffSuccess() {
        Response response = target.path("/staff/12345").request().get();

        Staff staff = response.readEntity(Staff.class);
        assert response.getStatus() == 200;
        testStaff(staff);
    }

    @Test
    public void testGetStaffNotFound() {
        Response response = target.path("/staff/54321").request().get();

        assert response.getStatus() == 404;
    }

    private void testStaff(Staff staff) {
        assert staff != null;
        assert staff.getId().equals("12345");
        assert staff.getUsername().equals("jsmith");
        assert staff.getFirstName().equals("John");
        assert staff.getLastName().equals("Smith");
        assert staff.getDateOfBirth().equals(new Date(100));
        assert !staff.isAdmin();
    }
}
