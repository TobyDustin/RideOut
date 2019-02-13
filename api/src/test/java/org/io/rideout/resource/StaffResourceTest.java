package org.io.rideout.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.io.rideout.Main;
import org.io.rideout.model.Staff;
import org.junit.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class StaffResourceTest {

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

    @Test
    public void testPostStaff() {
        String body = "{\"modelType\":\"StaffModel\",\"username\":\"jsmith\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\",\"admin\":false}";
        Response response = target.path("staff").request().post(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testStaff(response.readEntity(Staff.class));
    }

    @Test
    public void testPutStaffSuccess() {
        String body = "{\"modelType\":\"StaffModel\",\"id\":\"12345\",\"username\":\"jsmith\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\",\"admin\":false}";
        Response response = target.path("staff/54321").request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testStaff(response.readEntity(Staff.class));
    }

    @Test
    public void testPutStaffNotFound() {
        String body = "{\"modelType\":\"StaffModel\",\"id\":\"12345\",\"username\":\"jsmith\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\",\"admin\":false}";
        Response response = target.path("staff/121212").request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testDeleteStaffSuccess() {
        Response response = target.path("staff/12345").request().delete();

        assertEquals(200, response.getStatus());
        testStaff(response.readEntity(Staff.class));
    }

    @Test
    public void testDeleteStaffNotFound() {
        Response response = target.path("staff/54321").request().delete();

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
