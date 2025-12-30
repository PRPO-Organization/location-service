
package com.skupina1.location;

import com.skupina1.location.config.CorsFilter;
import com.skupina1.location.config.PreflightRequestFilter;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Hello world!
 *
 */
@ApplicationPath("/api")
public class  LocationApplication extends Application {
    public static void main(String[] args) throws Exception {
        ResourceConfig rc = new ResourceConfig()
                .packages("com.skupina1.accountmanagement.resource")
                .register(CorsFilter.class);

        HttpServer server = org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
                .createHttpServer(java.net.URI.create("http://0.0.0.0:8080/"), rc);

        System.out.println("Account service running on http://localhost:8080/");
        Thread.currentThread().join();
    }
}
