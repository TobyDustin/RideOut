package org.io.rideout.resource;

import io.restassured.http.ContentType;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.io.rideout.HttpTestServer;
import org.io.rideout.PasswordManager;
import org.io.rideout.database.TestDatabase;
import org.io.rideout.model.RiderInformation;
import org.io.rideout.model.User;
import org.io.rideout.model.Vehicle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserResourceIT {

    private static org.glassfish.grizzly.http.server.HttpServer server;

    @BeforeAll
    public static void setUp() {
        TestDatabase.setUp();
        server = HttpTestServer.startServer();
    }

    @AfterAll
    public static void tearDown() {
        server.stop();
        TestDatabase.tearDown();
    }

    @Test
    public void testGetAllUsers() {
        given()
                .when()
                .get("api/user")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("User", hasSize(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testGetRiderSuccess() {
        String id = TestDatabase.GET_RIDER.toHexString();

        given()
                .pathParam("id", id)
                .when()
                .get("api/user/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("role", equalTo("rider"));
    }

    @Test
    public void testGetStaffSuccess() {
        String id = TestDatabase.GET_STAFF.toHexString();
        given()
                .pathParam("id", id)
                .when()
                .get("api/user/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("role", equalTo("staff"));
    }

    @Test
    public void testGetUserNotFound() {
        String id = new ObjectId().toHexString();

        given()
                .pathParam("id", id)
                .when()
                .get("api/user/{id}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testGetUserVehicles() {
        String id = TestDatabase.GET_RIDER.toHexString();

        given()
                .pathParam("id", id)
                .when()
                .get("api/user/{id}/vehicle")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testGetUserVehiclesEmptyCollection() {
        String id = TestDatabase.GET_STAFF.toHexString();

        given()
                .pathParam("id", id)
                .when()
                .get("api/user/{id}/vehicle")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("", hasSize(0));
    }

    @Test
    public void testGetUserVehicleById() {
        String uid = TestDatabase.VEHICLE_RIDER.toHexString();
        String vid = TestDatabase.GET_VEHICLE.toHexString();

        given()
                .pathParam("id", uid)
                .pathParam("vid", vid)
                .when()
                .get("api/user/{id}/vehicle/{vid}")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("", not(isEmptyOrNullString()));
    }

    @Test
    public void testUpdateVehicle() {
        String uid = TestDatabase.VEHICLE_RIDER.toHexString();
        String vid = TestDatabase.PUT_VEHICLE.toHexString();
        Vehicle vehicle = new Vehicle(TestDatabase.PUT_VEHICLE, "Honda", "Monkey", 125, "REG123");

        given()
                .pathParam("id", uid)
                .with()
                .contentType(ContentType.JSON)
                .body(vehicle)
                .when()
                .put("api/user/{id}/vehicle")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("", not(isEmptyOrNullString()));
    }

    @Test
    public void testDeleteVehicle() {
        String uid = TestDatabase.VEHICLE_RIDER.toHexString();
        String vid = TestDatabase.DELETE_VEHICLE.toHexString();

        given()
                .pathParam("id", uid)
                .pathParam("vid", vid)
                .with()
                .when()
                .delete("api/user/{id}/vehicle/{vid}")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("", not(isEmptyOrNullString()));
    }

    @Test
    public void testAddVehicle() {
        String id = TestDatabase.VEHICLE_RIDER.toHexString();
        Vehicle vehicle = new Vehicle(null, "Honda", "Monkey", 125, "REG123");

        given()
                .pathParam("id", id)
                .with()
                .contentType(ContentType.JSON)
                .body(vehicle)
                .when()
                .post("api/user/{id}/vehicle")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("", not(isEmptyOrNullString()));
    }

    @Test
    public void testPutUser() {
        String id = TestDatabase.PUT_STAFF.toHexString();
        String password = PasswordManager.hashPassword("john123");
        User user = new User(new ObjectId(id), "jsmith", password, "staff", "John", "Smith", new Date(100), "07491012345", new RiderInformation());

        given()
                .with()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .put("api/user")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("", not(isEmptyOrNullString()));
    }

    @Test
    public void testPostUserSuccess() {
        String id = TestDatabase.PUT_STAFF.toHexString();
        String password = PasswordManager.hashPassword("john123");
        User user = new User(new ObjectId(id), "jsmith", password, "staff", "John", "Smith", new Date(100), "07491012345", new RiderInformation());

        given()
                .with()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("api/user")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("", not(isEmptyOrNullString()));
    }

    @Test
    public void testPutUserNotFound() {
        String id = new ObjectId().toHexString();
        String password = PasswordManager.hashPassword("john123");
        User user = new User(new ObjectId(id), "jsmith", password, "staff", "John", "Smith", new Date(100), "07491012345", new RiderInformation());

        given()
                .with()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .put("api/user")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testDeleteRiderSuccess() {
        String id = TestDatabase.DELETE_RIDER.toHexString();

        given()
                .pathParam("id", id)
                .with()
                .when()
                .delete("api/user/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("", not(isEmptyOrNullString()));
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
