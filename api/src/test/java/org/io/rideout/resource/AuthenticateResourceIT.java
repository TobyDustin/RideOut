package org.io.rideout.resource;

import io.restassured.http.ContentType;
import org.io.rideout.HttpTestServer;
import org.io.rideout.database.TestDatabase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

class AuthenticateResourceIT {

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
    void authenticate() {
        given()
                .with()
                .body("{\"username\": \"jsmith\", \"password\": \"john123\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("api/authenticate")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
