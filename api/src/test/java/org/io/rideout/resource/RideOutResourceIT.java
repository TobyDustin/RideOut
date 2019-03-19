package org.io.rideout.resource;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.bson.types.ObjectId;
import org.glassfish.grizzly.http.server.HttpServer;
import org.io.rideout.HttpTestServer;
import org.io.rideout.database.TestDatabase;
import org.io.rideout.model.RideOut;
import org.io.rideout.model.StayOut;
import org.io.rideout.model.TourOut;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class RideOutResourceIT {


    private static HttpServer server;
    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1YzcwMmMzODlhYjdkYTBiOTVjMGE5ZjIiLCJpc3MiOiJyaWRlb3V0IiwidXNlcm5hbWUiOiJqc21pdGgifQ.A_OS3PGBki3mTE9S-QzhBz-MgDKKM3fSbTBB0WfOczLuYAMluMG20jrioFV1IbYlFV8J6mgz_RiUtYfTePZyWg";

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
    public void testGetAllRideOut() {
        given()
                .header(new Header("Authorization", "Bearer " + token))
                .when()
                .get("api/rideout")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("RideOut", hasSize(greaterThanOrEqualTo(3)));
    }

    @Test
    public void testGetRideOutNotFound() {
        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("id", "invalid_id")
                .when()
                .get("api/rideout/{id}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testGetRideOutSuccess() {
        String id = TestDatabase.GET_RIDEOUT.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("id", id)
                .when()
                .get("api/rideout/{id}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testAddUserSuccess() {
        String id = TestDatabase.ADD_RIDER_RIDEOUT.toHexString();
        String rid = TestDatabase.GET_RIDER.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("id", id)
                .pathParam("rid", rid)
                .when()
                .put("api/rideout/{id}/rider/{rid}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testAddUserRideOutNotFound() {
        String id = new ObjectId().toHexString();
        String rid = TestDatabase.GET_RIDER.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("id", id)
                .pathParam("rid", rid)
                .when()
                .put("api/rideout/{id}/rider/{rid}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testAddUserNotFound() {
        String id = TestDatabase.ADD_RIDER_RIDEOUT.toHexString();
        String rid = new ObjectId().toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("id", id)
                .pathParam("rid", rid)
                .when()
                .put("api/rideout/{id}/rider/{rid}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testRemoveUserSuccess() {
        String id = TestDatabase.REMOVE_RIDER_RIDEOUT.toHexString();
        String rid = TestDatabase.GET_RIDER.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("id", id)
                .pathParam("rid", rid)
                .when()
                .delete("api/rideout/{id}/rider/{rid}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testGetRideOuts() {
        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("type", "ride")
                .when()
                .get("api/rideout/{type}")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("RideOut", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testGetStayOuts() {
        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("type", "stay")
                .when()
                .get("api/rideout/{type}")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("RideOut", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testGetTourOuts() {
        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("type", "tour")
                .when()
                .get("api/rideout/{type}")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("RideOut", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testRemoveUserRideOutNotFound() {
        String id = new ObjectId().toHexString();
        String rid = TestDatabase.GET_RIDER.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("id", id)
                .pathParam("rid", rid)
                .when()
                .delete("api/rideout/{id}/rider/{rid}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testRemoveUserNotFound() {
        String id = TestDatabase.REMOVE_RIDER_RIDEOUT.toHexString();
        String rid = new ObjectId().toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("id", id)
                .pathParam("rid", rid)
                .when()
                .delete("api/rideout/{id}/rider/{rid}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testPutRideOutSuccess() {
        String id = TestDatabase.PUT_RIDEOUT.toHexString();

        RideOut rideOut = new RideOut(
                new ObjectId(id),
                "Ride around the candovers",
                new Date(100),
                new Date(100),
                15,
                "54321",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date(100)
        );

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .when()
                .with()
                .header(new Header("Content-Type", "application/json"))
                .body(rideOut)
                .put("api/rideout")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testRemoveRideOut() {
        String id = TestDatabase.DELETE_RIDEOUT.toHexString();

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .pathParam("id", id)
                .when()
                .delete("api/rideout/{id}")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testPostRideOut() {
        RideOut rideOut = new RideOut(
                null,
                "Ride around the candovers",
                new Date(100),
                new Date(100),
                15,
                "54321",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date(100)
        );

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .with()
                .header(new Header("Content-Type", "application/json"))
                .body(rideOut)
                .when()
                .post("api/rideout")
                .then()
                .assertThat()
                .statusCode(200);

    }

    @Test
    public void testPostRideOutInvalidBody() {
        RideOut rideOut = new RideOut(
            null,
            "1",
            new Date(100),
            new Date(100),
            15,
            "54321",
            "",
            new Date(100)
        );

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .with()
                .contentType(ContentType.JSON)
                .body(rideOut)
                .post("api/rideout")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testPutRideOutInvalidBody() {
        RideOut rideOut = new RideOut(
                null,
                "1",
                new Date(100),
                new Date(100),
                15,
                "54321",
                "",
                new Date(100)
        );

        given()
                .header(new Header("Authorization", "Bearer " + token))
                .with()
                .contentType(ContentType.JSON)
                .body(rideOut)
                .put("api/rideout")
                .then()
                .assertThat()
                .statusCode(400);
    }

    private void testRideOut(RideOut rideOut, ObjectId id) {
        assertNotNull(rideOut);
        if (id != null) assertEquals(id, rideOut.getId());
        assertEquals("Ride around the candovers", rideOut.getName());
        assertEquals(new Date(100),  rideOut.getDateStart());
        assertEquals(new Date(100), rideOut.getDateEnd());
        assertEquals(15, rideOut.getMaxRiders());
        assertEquals("54321", rideOut.getLeadRider());
        assertEquals("https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx", rideOut.getRoute());
        assertEquals(new Date(100), rideOut.getMinCancellationDate());
        assertFalse(rideOut.isPublished());
    }

    private void testStayOut(StayOut stayOut, ObjectId id) {
        assertNotNull(stayOut);
        assertEquals(id, stayOut.getId());
        assertEquals("Stay around the candovers", stayOut.getName());
        assertEquals(new Date(200),  stayOut.getDateStart());
        assertEquals(new Date(200), stayOut.getDateEnd());
        assertEquals(10, stayOut.getMaxRiders());
        assertEquals("1234", stayOut.getLeadRider());
        assertEquals("https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx", stayOut.getRoute());
        assertEquals(new Date(200), stayOut.getMinCancellationDate());
        assertFalse(stayOut.isPublished());
        assertEquals(TestDatabase.BOOKING_1, stayOut.getAccommodationList().get(0).getId());
        assertEquals("Marriot Hotel", stayOut.getAccommodationList().get(0).getName());
        assertEquals("ABCDE", stayOut.getAccommodationList().get(0).getReference());
        assertEquals(TestDatabase.BOOKING_2, stayOut.getRestaurantList().get(0).getId());
        assertEquals("KFC", stayOut.getRestaurantList().get(0).getName());
        assertEquals("", stayOut.getRestaurantList().get(0).getReference());
    }

    private void testTourOut(TourOut tourOut, ObjectId id) {
        assertNotNull(tourOut);
        assertEquals(id, tourOut.getId());
        assertEquals("Tour around the candovers", tourOut.getName());
        assertEquals(new Date(300),  tourOut.getDateStart());
        assertEquals(new Date(300), tourOut.getDateEnd());
        assertEquals(5, tourOut.getMaxRiders());
        assertEquals("2345", tourOut.getLeadRider());
        assertEquals("https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx", tourOut.getRoute());
        assertEquals(new Date(300), tourOut.getMinCancellationDate());
        assertFalse(tourOut.isPublished());
        assertEquals(TestDatabase.BOOKING_1, tourOut.getAccommodationList().get(0).getId());
        assertEquals("Marriot Hotel", tourOut.getAccommodationList().get(0).getName());
        assertEquals("ABCDE", tourOut.getAccommodationList().get(0).getReference());
        assertEquals(TestDatabase.BOOKING_2, tourOut.getRestaurantList().get(0).getId());
        assertEquals("KFC", tourOut.getRestaurantList().get(0).getName());
        assertEquals("", tourOut.getRestaurantList().get(0).getReference());
        assertEquals(TestDatabase.BOOKING_3, tourOut.getTravelBookings().get(0).getId());
        assertEquals("Condor Ferries", tourOut.getTravelBookings().get(0).getName());
        assertEquals("QWERTY", tourOut.getTravelBookings().get(0).getReference());
    }



}
