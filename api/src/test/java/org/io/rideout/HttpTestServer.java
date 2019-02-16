package org.io.rideout;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * HttpTestServer class.
 * Used for testing API calls in unit tests
 *
 */
public class HttpTestServer {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/api/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static org.glassfish.grizzly.http.server.HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.io.rideout package
        final ResourceConfig rc = new ResourceConfig().packages("org.io.rideout");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * HttpTestServer method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final org.glassfish.grizzly.http.server.HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}
