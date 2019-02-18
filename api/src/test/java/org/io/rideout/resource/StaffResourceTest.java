package org.io.rideout.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.io.rideout.HttpTestServer;
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
        String id = StaffResource.UID_12345.toHexString();

        Response response = target.path("/staff/" + id).request().get();

        Staff staff = response.readEntity(Staff.class);
        assertEquals(200, response.getStatus());
        testStaff(staff);
    }

    @Test
    public void testGetStaffNotFound() {
        String id = StaffResource.UID_54321.toHexString();

        Response response = target.path("/staff/" + id).request().get();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testPostStaff() {
        String body = "{\"modelType\":\"StaffModel\",\"username\":\"jsmith\",\"password\":\"john123\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\",\"admin\":false}";
        Response response = target.path("staff").request().post(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testStaff(response.readEntity(Staff.class));
    }

    @Test
    public void testPutStaffSuccess() {
        String id = StaffResource.UID_12345.toHexString();
        String fid = new ObjectId("5c6a97fe3bd3d419a78de2c5").toHexString();

        String body = "{\"modelType\":\"StaffModel\",\"id\":\"" + id + "\",\"username\":\"jsmith\",\"password\":\"john123\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\",\"admin\":false}";
        Response response = target.path("staff/" + fid).request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testStaff(response.readEntity(Staff.class));
    }

    @Test
    public void testPutStaffNotFound() {
        String id = StaffResource.UID_12345.toHexString();
        String fid = new ObjectId(new Date(121212)).toHexString();

        String body = "{\"modelType\":\"StaffModel\",\"id\":\"" + id + "\",\"username\":\"jsmith\",\"password\":\"john123\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\",\"admin\":false}";
        Response response = target.path("staff/" + fid).request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testDeleteStaffSuccess() {
        String id = StaffResource.UID_12345.toHexString();

        Response response = target.path("staff/" + id).request().delete();

        assertEquals(200, response.getStatus());
        testStaff(response.readEntity(Staff.class));
    }

    @Test
    public void testDeleteStaffNotFound() {
        String id = StaffResource.UID_54321.toHexString();

        Response response = target.path("staff/" + id).request().delete();

        assertEquals(404, response.getStatus());
    }

    private void testStaff(Staff staff) {
        assertNotNull(staff);
        assertEquals(StaffResource.UID_12345, staff.getId());
        assertEquals("jsmith", staff.getUsername());
        assertEquals("john123", staff.getPassword());
        assertEquals("John", staff.getFirstName());
        assertEquals("Smith", staff.getLastName());
        assertEquals(new Date(100), staff.getDateOfBirth());
        assertFalse(staff.isAdmin());
    }
}
