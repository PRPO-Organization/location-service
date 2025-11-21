package com.skupina1.location.userLocation;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.sql.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import net.postgis.jdbc.PGgeography;
import org.bson.types.ObjectId;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;


@Entity
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "UserLocation.findDistance",
                query = "SELECT ST_Distance(" +
                        "ST_Transform(ST_SetSRID(ST_MakePoint(:lng1, :lat1), 4326), 3857), " +
                        "ST_Transform(ST_SetSRID(ST_MakePoint(:lng2, :lat2), 4326), 3857)" +
                        ") * cosd(:lat1)",
                resultClass = Double.class
        ),
        @NamedNativeQuery(
                name="UserLocation.findNearestLocation",
                query = "select * from public.user_locations "+
                "order BY ST_DISTANCE(location::geography , ST_SetSRID(ST_MakePoint(:lng,:lat),4326)::geography) "+
                "LIMIT 1",
                resultClass = UserLocation.class
        )
})
@Table(name="user_locations")
public class UserLocation{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="user_id")
    private String userId;
    @Column(columnDefinition = "geography(Point,4326)")
    private Point location;
    public UserLocation(){

    }
    public UserLocation(String userId, Point location){
        this.userId = userId;
        this.location = location;
    }
    public void setLocation(Point location) {
        this.location = location;
    }
    public Point getLocation() {
        return location;
    }
    public String getUserId(){
        return this.userId;
    }
    public ObjectId getObjectId() {
        return new ObjectId(userId);
    }

}