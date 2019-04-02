package org.io.rideout.resource;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.bson.types.ObjectId;
import org.glassfish.grizzly.http.server.HttpServer;
import org.io.rideout.HttpTestServer;
import org.io.rideout.database.TestDatabase;
import org.io.rideout.model.Booking;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

public class BookingResourceIT {

    private static HttpServer server;
    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1YzZlYzM3OGIxYTA1MjI3OWRiYmY3MTAiLCJyb2xlIjoicmlkZXIiLCJpc3MiOiJyaWRlb3V0IiwidXNlcm5hbWUiOiJqc21pdGgifQ.3T3CyGggttRBsC7iFHcV6gqhdTlzLLoT1cRaVdVivyjOejWT49gaNZd-Gf6MlT0BKq6ptwArb-77tXdVSAOKVw";
    private String staffToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1YzZlYzM3OGIxYTA1MjI3OWRiYmY3MTEiLCJpc3MiOiJyaWRlb3V0IiwidXNlcm5hbWUiOiJqc21pdGgifQ.utEc47HzLcndvRVDo5nFkNSU3N1GhqyqVkICYVx5N4MByQ8khELeO39iX5dSPP4awLH1-XyHbSZoZ3bThksUQQ";

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
    public void testGetAll() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("rid", rideout)
                .when()
                .get("api/rideout/{rid}/booking")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("RideOut", hasSize(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testGetAllNotFound() {
        String rideout = new ObjectId().toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("rid", rideout)
                .when()
                .get("api/rideout/{rid}/booking")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testGetByTypeSuccess() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("rid", rideout)
                .queryParam("type", Booking.RESTAURANT)
                .when()
                .get("api/rideout/{rid}/booking")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("RideOut", hasSize(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testGetByTypeInvalidType() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("rid", rideout)
                .queryParam("type", "randomType")
                .when()
                .get("api/rideout/{rid}/booking")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testGetByIdSuccess() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();
        String id = TestDatabase.GET_BOOKING.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("rid", rideout)
                .pathParam("id", id)
                .when()
                .get("api/rideout/{rid}/booking/{id}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testGetByIdRideoutNotFound() {
        String rideout = new ObjectId().toHexString();
        String id = TestDatabase.GET_BOOKING.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("rid", rideout)
                .pathParam("id", id)
                .when()
                .get("api/rideout/{rid}/booking/{id}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testGetByIdNotFound() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();
        String id = new ObjectId().toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("rid", rideout)
                .pathParam("id", id)
                .when()
                .get("api/rideout/{rid}/booking/{id}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testPostSuccess() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();
        Booking booking = new Booking(new ObjectId(), "Test", "Test booking", Booking.RESTAURANT);

        given()
                .header(new Header("Authorization", "Bearer " + staffToken))
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("rid", rideout)
                .body(booking)
                .when()
                .post("api/rideout/{rid}/booking")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testPostNotAllowed() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();
        Booking booking = new Booking(new ObjectId(), "Test", "Test booking", Booking.RESTAURANT);

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("rid", rideout)
                .body(booking)
                .when()
                .post("api/rideout/{rid}/booking")
                .then()
                .assertThat()
                .statusCode(403);
    }

    @Test
    public void testPostInvalidBody() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();
        Booking booking = new Booking();

        given()
                .header(new Header("Authorization", "Bearer " + staffToken))
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("rid", rideout)
                .body(booking)
                .when()
                .post("api/rideout/{rid}/booking")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testPutSuccess() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();
        Booking booking = new Booking(TestDatabase.PUT_BOOKING, "Test (update)", "Test booking", Booking.RESTAURANT);

        given()
                .header(new Header("Authorization", "Bearer " + staffToken))
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("rid", rideout)
                .body(booking)
                .when()
                .put("api/rideout/{rid}/booking")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testPutNotAllowed() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();
        Booking booking = new Booking(TestDatabase.PUT_BOOKING, "Test (update)", "Test booking", Booking.RESTAURANT);

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("rid", rideout)
                .body(booking)
                .when()
                .put("api/rideout/{rid}/booking")
                .then()
                .assertThat()
                .statusCode(403);
    }

    @Test
    public void testPutInvalidBody() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();
        Booking booking = new Booking();

        given()
                .header(new Header("Authorization", "Bearer " + staffToken))
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("rid", rideout)
                .body(booking)
                .when()
                .put("api/rideout/{rid}/booking")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testDeleteSuccess() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();
        String id = TestDatabase.DELETE_BOOKING.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + staffToken))
                .pathParam("rid", rideout)
                .pathParam("id", id)
                .when()
                .delete("api/rideout/{rid}/booking/{id}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testDeleteNotAllowed() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();
        String id = TestDatabase.DELETE_BOOKING.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("rid", rideout)
                .pathParam("id", id)
                .when()
                .delete("api/rideout/{rid}/booking/{id}")
                .then()
                .assertThat()
                .statusCode(403);
    }

    @Test
    public void testDeleteNotFound() {
        String rideout = TestDatabase.GET_RIDEOUT.toHexString();
        String id = new ObjectId().toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + staffToken))
                .pathParam("rid", rideout)
                .pathParam("id", id)
                .when()
                .delete("api/rideout/{rid}/booking/{id}")
                .then()
                .assertThat()
                .statusCode(404);
    }
}
