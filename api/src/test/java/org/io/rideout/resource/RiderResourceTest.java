package org.io.rideout.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.io.rideout.HttpTestServer;
import org.io.rideout.model.Rider;
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

public class RiderResourceTest {

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
        String id = RiderResource.UID_12345.toHexString();

        Response response = target.path("/rider/" + id).request().get();

        Rider rider = response.readEntity(Rider.class);
        assertEquals(200, response.getStatus());
        testRider(rider);
    }

    @Test
    public void testGetRiderNotFound() {
        String id = RiderResource.UID_54321.toHexString();

        Response response = target.path("rider/" + id).request().get();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testGetRiderVehicles() {
        String id = RiderResource.UID_12345.toHexString();

        Response response = target.path("rider/" + id + "/vehicle").request().get();
        ArrayList<Vehicle> vehicles = response.readEntity(new GenericType<ArrayList<Vehicle>>() {});

        testVehicle(vehicles.get(0));
    }

    @Test
    public void testGetRiderVehicleById() {
        String id = RiderResource.UID_12345.toHexString();
        String vid = RiderResource.VID_9876.toHexString();

        Response response = target.path("rider/" + id + "/vehicle/" + vid).request().get();
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle);
    }

    @Test
    public void testUpdateVehicle() {
        String id = RiderResource.UID_12345.toHexString();
        String vid = RiderResource.VID_9876.toHexString();

        Response response = target.path("rider/" + id + "/vehicle/" + vid).request()
                .put(Entity.entity(new Vehicle(RiderResource.VID_9876, "Honda", "Monkey", 125, "REG123"), MediaType.APPLICATION_JSON_TYPE));
        assertEquals(200, response.getStatus());
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle);
    }

    @Test
    public void testDeleteVehicle() {
        String id = RiderResource.UID_12345.toHexString();
        String vid = RiderResource.VID_9876.toHexString();

        Response response = target.path("rider/" + id + "/vehicle/" + vid).request().delete();
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle);
    }

    @Test
    public void testAddVehicle() {
        String id = RiderResource.UID_12345.toHexString();

        Response response = target.path("rider/" + id + "/vehicle/").request()
                .post(Entity.entity(new Vehicle(RiderResource.VID_9876, "Honda", "Monkey", 125, "REG123"), MediaType.APPLICATION_JSON_TYPE));
        Vehicle vehicle = response.readEntity(Vehicle.class);

        testVehicle(vehicle);
    }

    @Test
    public void testPutRider() {
        String id = RiderResource.UID_12345.toHexString();
        String fid = RiderResource.UID_54321.toHexString();

        String body = "{\"modelType\":\"RiderModel\",\"id\":\"" + id + "\",\"username\":\"jsmith\",\"password\":\"john123\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\",\"emergencyContactNumber\":\"999\",\"vehicles\":[{\"id\":\"9876\",\"make\":\"Honda\",\"model\":\"Monkey\",\"power\":125,\"registration\":\"REG123\",\"checked\":false}],\"license\":\"A\",\"payments\":[],\"insured\":true,\"lead\":false}";
        Response response = target.path("rider/" + fid).request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testRider(response.readEntity(Rider.class));
    }

    @Test
    public void testPostRiderSuccess() {
        String body = "{\"modelType\":\"RiderModel\",\"username\":\"jsmith\",\"password\":\"john123\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\",\"emergencyContactNumber\":\"999\",\"vehicles\":[{\"id\":\"9876\",\"make\":\"Honda\",\"model\":\"Monkey\",\"power\":125,\"registration\":\"REG123\",\"checked\":false}],\"license\":\"A\",\"payments\":[],\"insured\":true,\"lead\":false}";
        Response response = target.path("rider").request().post(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        testRider(response.readEntity(Rider.class));
    }

    @Test
    public void testPutRiderNotFound() {
        String id = RiderResource.UID_12345.toHexString();
        String fid = new ObjectId(new Date(121212)).toHexString();

        String body = "{\"modelType\":\"RiderModel\",\"id\":\"" + id + "\",\"username\":\"jsmith\",\"password\":\"john123\",\"firstName\":\"John\",\"lastName\":\"Smith\",\"dateOfBirth\":100,\"contactNumber\":\"07491012345\",\"emergencyContactNumber\":\"999\",\"vehicles\":[{\"id\":\"9876\",\"make\":\"Honda\",\"model\":\"Monkey\",\"power\":125,\"registration\":\"REG123\",\"checked\":false}],\"license\":\"A\",\"payments\":[],\"insured\":true,\"lead\":false}";
        Response response = target.path("rider/" + fid).request().put(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testDeleteRiderSuccess() {
        String id = RiderResource.UID_12345.toHexString();

        Response response = target.path("rider/" + id).request().delete();

        assertEquals(200, response.getStatus());
        testRider(response.readEntity(Rider.class));
    }

    static void testRider(Rider rider) {
        assertNotNull(rider);
        assertEquals(RiderResource.UID_12345, rider.getId());
        assertEquals("jsmith", rider.getUsername());
        assertEquals("john123", rider.getPassword());
        assertEquals("John",  rider.getFirstName());
        assertEquals("Smith", rider.getLastName());
        assertEquals(new Date(100), rider.getDateOfBirth());
        assertEquals("07491012345", rider.getContactNumber());
        assertEquals("999", rider.getEmergencyContactNumber());
        assertTrue(rider.isInsured());
        assertFalse(rider.isLead());
        assertEquals("A", rider.getLicense());
    }

    static void testVehicle(Vehicle vehicle) {
        assertNotNull(vehicle);
        assertEquals(RiderResource.VID_9876, vehicle.getId());
        assertEquals("Honda", vehicle.getMake());
        assertEquals("Monkey", vehicle.getModel());
        assertEquals(Integer.valueOf(125), vehicle.getPower());
        assertEquals("REG123", vehicle.getRegistration());
    }
}
