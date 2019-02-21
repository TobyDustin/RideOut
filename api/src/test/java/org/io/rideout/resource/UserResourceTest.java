package org.io.rideout.resource;

import org.bson.types.ObjectId;
import org.io.rideout.HttpTestServer;
import org.io.rideout.PasswordManager;
import org.io.rideout.database.TestDatabase;
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
        TestDatabase.setUp();

        server = HttpTestServer.startServer();
        Client c = ClientBuilder.newClient();

        target = c.target(HttpTestServer.BASE_URI);
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
        TestDatabase.tearDown();
    }

    @Test
    public void testGetAllUsers() throws IOException {
        Response response = target.path("/user").request().get();

        ArrayList result = response.readEntity(ArrayList.class);

        assertEquals(200, response.getStatus());
        assertTrue(result.size() >= 2);
    }

    @Test
    public void testGetRiderSuccess() {
        String id = TestDatabase.GET_RIDER.toHexString();
        Response response = target.path("/user/" + id).request().get();

        User user = response.readEntity(User.class);
        assertEquals(200, response.getStatus());
        testRider(user, TestDatabase.GET_RIDER);
    }

    @Test
    public void testGetStaffSuccess() {
        String id = TestDatabase.GET_STAFF.toHexString();
        Response response = target.path("/user/" + id).request().get();

        User user = response.readEntity(User.class);
        assertEquals(200, response.getStatus());
        testStaff(user, TestDatabase.GET_STAFF);
    }

    @Test
    public void testGetUserNotFound() {
        String id = new ObjectId().toHexString();
        Response response = target.path("user/" + id).request().get();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testGetUserVehicles() {
        String id = TestDatabase.GET_RIDER.toHexString();
        Response response = target.path("user/" + id + "/vehicle").request().get();
        ArrayList<Vehicle> vehicles = response.readEntity(new GenericType<ArrayList<Vehicle>>() {});

        testVehicle(vehicles.get(0), TestDatabase.GET_VEHICLE);
    }

    @Test
    public void testGetUserVehiclesEmptyCollection() {
        String id = TestDatabase.GET_STAFF.toHexString();
        Response response = target.path("user/" + id + "/vehicle").request().get();
        ArrayList<Vehicle> vehicles = response.readEntity(new GenericType<ArrayList<Vehicle>>() {});
        assertEquals(0, vehicles.size());
    }

    @Test
    public void testGetUserVehicleById() {
        String uid = TestDatabase.VEHICLE_RIDER.toHexString();
        String vid = TestDatabase.GET_VEHICLE.toHexString();
        Response response = target.path("user/" + uid + "/vehicle/" + vid).request().get();
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle, TestDatabase.GET_VEHICLE);
    }

    @Test
    public void testUpdateVehicle() {
        String uid = TestDatabase.VEHICLE_RIDER.toHexString();
        String vid = TestDatabase.PUT_VEHICLE.toHexString();
        Response response = target.path("user/" + uid + "/vehicle/" + vid).request()
                .put(Entity.entity(new Vehicle(TestDatabase.PUT_VEHICLE, "Honda", "Monkey", 125, "REG123"), MediaType.APPLICATION_JSON_TYPE));
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle, TestDatabase.PUT_VEHICLE);
    }

    @Test
    public void testDeleteVehicle() {
        String uid = TestDatabase.VEHICLE_RIDER.toHexString();
        String vid = TestDatabase.DELETE_VEHICLE.toHexString();
        Response response = target.path("user/" + uid + "/vehicle/" + vid).request().delete();
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle, TestDatabase.DELETE_VEHICLE);
    }

    @Test
    public void testAddVehicle() {
        String id = TestDatabase.VEHICLE_RIDER.toHexString();
        Response response = target.path("user/" + id + "/vehicle/").request()
                .post(Entity.entity(new Vehicle(null, "Honda", "Monkey", 125, "REG123"), MediaType.APPLICATION_JSON_TYPE));
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle, null);
    }

    @Test
    public void testPutUser() {
        String id = TestDatabase.PUT_STAFF.toHexString();
        String body = "{\"id\":\"" + id + "\",\"username\":\"jsmith\",\"password\":\"john123\",\"role\":\"staff\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\"}";
        Response response = target.path("user/" + id).request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        User result = response.readEntity(User.class);
        System.out.println(result.getPassword());
        testStaff(result, TestDatabase.PUT_STAFF);
    }

    @Test
    public void testPostUserSuccess() {
        String body = "{\"username\":\"jsmith\",\"password\":\"john123\",\"role\":\"staff\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\"}";
        Response response = target.path("user").request().post(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testStaff(response.readEntity(User.class), null);
    }

    @Test
    public void testPutUserNotFound() {
        String id = new ObjectId().toHexString();
        String body = "{\"id\":\"" + id + "\",\"username\":\"jsmith\",\"password\":\"john123\",\"role\":\"staff\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\"}";
        Response response = target.path("rider/" + id).request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testDeleteRiderSuccess() {
        String id = TestDatabase.DELETE_RIDER.toHexString();
        Response response = target.path("user/" + id).request().delete();

        assertEquals(200, response.getStatus());
        testRider(response.readEntity(User.class), TestDatabase.DELETE_RIDER);
    }

    static void testRider(User user, ObjectId id) {
        assertNotNull(user);
        if (id != null) assertEquals(id, user.getId());
        assertEquals("jsmith", user.getUsername());
        assertTrue(PasswordManager.verify("john123", user.getPassword()));
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

    static void testStaff(User user, ObjectId id) {
        assertNotNull(user);
        if (id != null) assertEquals(id, user.getId());
        assertEquals("jsmith", user.getUsername());
        assertTrue(PasswordManager.verify("john123", user.getPassword()));
        assertEquals(User.STAFF, user.getRole());
        assertEquals("John",  user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals(new Date(100), user.getDateOfBirth());
        assertEquals("07491012345", user.getContactNumber());
        assertNull(user.getRiderInformation());
    }

    static void testVehicle(Vehicle vehicle, ObjectId id) {
        assertNotNull(vehicle);
        if (id != null) assertEquals(id, vehicle.getId());
        assertEquals("Honda", vehicle.getMake());
        assertEquals("Monkey", vehicle.getModel());
        assertEquals(Integer.valueOf(125), vehicle.getPower());
        assertEquals("REG123", vehicle.getRegistration());
    }
}
