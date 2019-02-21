package org.io.rideout.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.io.rideout.HttpTestServer;
import org.io.rideout.model.User;
import org.io.rideout.model.Vehicle;
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

public class UserResourceTest {

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
    public void testGetAllUsers() throws IOException {
        Response response = target.path("/user").request().get();

        ArrayList result = response.readEntity(ArrayList.class);

        assertEquals(200, response.getStatus());
        assertEquals(2, result.size());

        ObjectMapper mapper = new ObjectMapper();
        testRider(mapper.convertValue(result.get(0), User.class));
        testStaff(mapper.convertValue(result.get(1), User.class));
    }

    @Test
    public void testGetRiderSuccess() {
        String id = UserResource.UID_12345.toHexString();
        Response response = target.path("/user/" + id).request().get();

        User user = response.readEntity(User.class);
        assertEquals(200, response.getStatus());
        testRider(user);
    }

    @Test
    public void testGetStaffSuccess() {
        String id = UserResource.UID_23456.toHexString();
        Response response = target.path("/user/" + id).request().get();

        User user = response.readEntity(User.class);
        assertEquals(200, response.getStatus());
        testStaff(user);
    }

    @Test
    public void testGetUserNotFound() {
        String id = UserResource.UID_54321.toHexString();
        Response response = target.path("user/" + id).request().get();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testGetUserVehicles() {
        String id = UserResource.UID_12345.toHexString();
        Response response = target.path("user/" + id + "/vehicle").request().get();
        ArrayList<Vehicle> vehicles = response.readEntity(new GenericType<ArrayList<Vehicle>>() {});

        testVehicle(vehicles.get(0));
    }

    @Test
    public void testGetUserVehiclesEmptyCollection() {
        String id = UserResource.UID_23456.toHexString();
        Response response = target.path("user/" + id + "/vehicle").request().get();
        ArrayList<Vehicle> vehicles = response.readEntity(new GenericType<ArrayList<Vehicle>>() {});
        assertEquals(0, vehicles.size());
    }

    @Test
    public void testGetUserVehicleById() {
        String uid = UserResource.UID_12345.toHexString();
        String vid = UserResource.VID_9876.toHexString();
        Response response = target.path("user/" + uid + "/vehicle/" + vid).request().get();
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle);
    }

    @Test
    public void testUpdateVehicle() {
        String uid = UserResource.UID_12345.toHexString();
        String vid = UserResource.VID_9876.toHexString();
        Response response = target.path("user/" + uid + "/vehicle/" + vid).request()
                .put(Entity.entity(new Vehicle(UserResource.VID_9876, "Honda", "Monkey", 125, "REG123"), MediaType.APPLICATION_JSON_TYPE));
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle);
    }

    @Test
    public void testDeleteVehicle() {
        String uid = UserResource.UID_12345.toHexString();
        String vid = UserResource.VID_9876.toHexString();
        Response response = target.path("user/" + uid + "/vehicle/" + vid).request().delete();
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle);
    }

    @Test
    public void testAddVehicle() {
        String id = UserResource.UID_12345.toHexString();
        Response response = target.path("user/" + id + "/vehicle/").request()
                .post(Entity.entity(new Vehicle(UserResource.VID_9876, "Honda", "Monkey", 125, "REG123"), MediaType.APPLICATION_JSON_TYPE));
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle);
    }

    @Test
    public void testPutUser() {
        String uid = UserResource.UID_23456.toHexString();
        String fid = UserResource.UID_54321.toHexString();
        String body = "{\"id\":\"" + uid + "\",\"username\":\"jsmith\",\"password\":\"john123\",\"role\":\"staff\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\"}";
        Response response = target.path("user/" + fid).request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testStaff(response.readEntity(User.class));
    }

    @Test
    public void testPostUserSuccess() {
        String body = "{\"username\":\"jsmith\",\"password\":\"john123\",\"role\":\"staff\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\"}";
        Response response = target.path("user").request().post(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testStaff(response.readEntity(User.class));
    }

    @Test
    public void testPutUserNotFound() {
        String id = UserResource.UID_12345.toHexString();
        String body = "{\"id\":\"" + id + "\",\"username\":\"jsmith\",\"password\":\"john123\",\"role\":\"staff\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\"}";
        Response response = target.path("rider/121212").request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testDeleteRiderSuccess() {
        String id = UserResource.UID_12345.toHexString();
        Response response = target.path("user/" + id).request().delete();

        assertEquals(200, response.getStatus());
        testRider(response.readEntity(User.class));
    }

    static void testRider(User user) {
        assertNotNull(user);
        assertEquals(UserResource.UID_12345, user.getId());
        assertEquals("jsmith", user.getUsername());
        assertEquals("john123", user.getPassword());
        assertEquals(User.RIDER, user.getRole());
        assertEquals("John",  user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals(new Date(100), user.getDateOfBirth());
        assertEquals("07491012345", user.getContactNumber());
        assertNotNull(user.getRiderInformation());
        assertEquals("999", user.getRiderInformation().getEmergencyContactNumber());
        assertTrue(user.getRiderInformation().isInsured());
        assertEquals("A", user.getRiderInformation().getLicense());
    }

    static void testStaff(User user) {
        assertNotNull(user);
        assertEquals(UserResource.UID_23456, user.getId());
        assertEquals("jsmith", user.getUsername());
        assertEquals("john123", user.getPassword());
        assertEquals(User.STAFF, user.getRole());
        assertEquals("John",  user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals(new Date(100), user.getDateOfBirth());
        assertEquals("07491012345", user.getContactNumber());
        assertNull(user.getRiderInformation());
    }

    static void testVehicle(Vehicle vehicle) {
        assertNotNull(vehicle);
        assertEquals(UserResource.VID_9876, vehicle.getId());
        assertEquals("Honda", vehicle.getMake());
        assertEquals("Monkey", vehicle.getModel());
        assertEquals(Integer.valueOf(125), vehicle.getPower());
        assertEquals("REG123", vehicle.getRegistration());
    }
}
