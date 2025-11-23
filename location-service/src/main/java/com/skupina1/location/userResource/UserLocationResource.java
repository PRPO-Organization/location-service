package com.skupina1.location.userResource;


import com.skupina1.location.userLocation.LocationDTO;
import com.skupina1.location.userLocation.UserLocation;
import com.skupina1.location.userLocation.UserLocationDTO;
import com.skupina1.location.userRepo.DistanceDTO;
import com.skupina1.location.userRepo.UserRepo;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.bson.types.ObjectId;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.List;

//rest api for the location api
@Path("location")
public class UserLocationResource {
    //endpoint which fetches the current location of the user
    @Inject
    UserRepo userRepo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getLocation(@PathParam("id") String oidStr) {

        List<UserLocation> currentLocations;
        try {
            currentLocations = userRepo.findLocationByUserId(oidStr);
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("")
                    .build();
        }
        if (currentLocations.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("")
                    .build();
        }
        if (currentLocations.size() != 1) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Only one location can be found")
                    .build();
        }
        UserLocation currentLocation = currentLocations.get(0);
        Point point = currentLocation.getLocation();
        UserLocationDTO currentLocationDTO = new UserLocationDTO(currentLocation.getId(), currentLocation.getUserId(), point.getX(), point.getY());
        return Response.ok(currentLocationDTO).build();
    }

    //endpoint which uploads the location of a new user
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response postLocation(@PathParam("id") String oidStr, LocationDTO userLocation) {
        ObjectId oid;
        //System.out.println("POST req");
        List<UserLocation> locations = userRepo.findLocationByUserId(oidStr);
        if (!locations.isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("location already exists")
                    .build();
        }
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = gf.createPoint(new Coordinate(userLocation.getLng(), userLocation.getLat()));
        UserLocation currentLocation = new UserLocation(oidStr , point);
        try {
            currentLocation = userRepo.addUserLocation(currentLocation);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to save location: " + e.getMessage())
                    .build();
        }
        Long locationId = currentLocation.getId();
        if (locationId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("location id is null")
                    .build();
        }
        UserLocationDTO userLocationDTO = new UserLocationDTO(currentLocation.getId(), currentLocation.getUserId(), userLocation.getLat(), userLocation.getLng());
        return Response.ok(userLocationDTO).build();
    }

    //endpoint which changes the location of an existing user
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response patchLocation(@PathParam("id") String oidStr, LocationDTO userLocation) {
        try {
            GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
            Point point = gf.createPoint(new Coordinate(userLocation.getLng(), userLocation.getLat()));
            boolean exists = userRepo.changeLocation(oidStr, point);
            if (!exists) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("user location does not exist")
                        .build();
            }
            return Response.ok(userLocation).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("internal error")
                    .build();
        }

    }

    //change the location of the user
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response putLocation(@PathParam("id") String oidStr, UserLocationDTO userLocation) {
        try {
            GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
            Point point = gf.createPoint(new Coordinate(userLocation.getLng(), userLocation.getLat()));
            boolean exists = userRepo.changeLocation(oidStr, point);
            if (!exists) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("user location does not exist")
                        .build();
            }
            return Response.ok(userLocation).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid location id " + e.getMessage())
                    .build();
        }

    }
    //to find the location between two points use the userId as a
    // path parameter and the target user object id as a query parameter
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/distance/{id}")
    public Response getDistance(
            @QueryParam("dest") String dest,
            @PathParam("id") String id
    ){
        if(dest==null||id==null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("dest or id is null")
                    .build();
        }
        //find user location
        UserLocation userLocation = userRepo.getUserLocation(id);
        if  (userLocation == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("user location does not exist")
                    .build();
        }
        UserLocation destLocation = userRepo.getUserLocation(dest);
        if (destLocation == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("user location does not exist")
                    .build();
        }
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        Point loc1 = userLocation.getLocation();
        Point loc2 = destLocation.getLocation();
        Double distance = userRepo.findDistance(loc1, loc2);
        if (distance == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("distance is null")
                    .build();
        }
        //find the dest location
        DistanceDTO distanceDTO = new DistanceDTO(distance);
        //convert to points and use the query in userRepo to get the distance
        return Response.ok(distanceDTO).build();
    }
}