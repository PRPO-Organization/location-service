package com.skupina1.location.generateLocations;

import com.skupina1.location.userLocation.UserLocation;
import jakarta.enterprise.context.ApplicationScoped;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@ApplicationScoped
public class LocationGenerator {
    private static final Random rand = new Random();
    private static final GeometryFactory geometryFactory = new GeometryFactory();
    public static double getRandomLatitude() {
        // Latitude: -90 to +90
        return -90 + 180 * rand.nextDouble();
    }

    public static double getRandomLongitude() {
        // Longitude: -180 to +180
        return -180 + 360 * rand.nextDouble();
    }

    //generate locations for the tests
    //using random latitude and longitude
    public static List<UserLocation> generateLocations(int n) {
        List<UserLocation> userLocations = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double lat = getRandomLatitude();
            double lon = getRandomLongitude();
            Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
            String userId = UUID.randomUUID().toString();
            UserLocation userLocation = new UserLocation(userId,point);
            userLocations.add(userLocation);
        }
        return userLocations;
    }
}