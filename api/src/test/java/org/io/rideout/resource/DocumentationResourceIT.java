package org.io.rideout.resource;

import io.restassured.http.ContentType;
import org.io.rideout.HttpTestServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class DocumentationResourceIT {

    private static org.glassfish.grizzly.http.server.HttpServer server;

    @BeforeAll
    public static void setUp() {
        server = HttpTestServer.startServer();
    }

    @AfterAll
    public static void tearDown() {
        server.stop();
    }

    @Test
    public void getDocumentation() {
        given()
                .with()
                .queryParam("format", "json")
                .when()
                .get("api/documentation")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON);
    }
}
