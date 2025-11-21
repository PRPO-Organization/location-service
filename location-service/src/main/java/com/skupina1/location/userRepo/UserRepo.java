package com.skupina1.location.userRepo;
import com.skupina1.location.userLocation.UserLocation;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import net.postgis.jdbc.PGgeography;
import org.bson.types.ObjectId;
import org.locationtech.jts.geom.Point;

@ApplicationScoped
public  class UserRepo {
    @Inject
    public EntityManager em;
    public UserRepo() {
    }
    @Transactional
    public void addUserLocation(UserLocation userLocation) {
        em.persist(userLocation);
    }
    //function to update the user location
    @Transactional
    public boolean changeLocation(ObjectId objectId , Point newLocation){
        String oidStr = objectId.toString();
        UserLocation userLocation = em.find(UserLocation.class, oidStr);
        if (userLocation == null){
            return false;
        }
        userLocation.setLocation(newLocation);
        return true;
    }
    public UserLocation findUserLocation(ObjectId objectId){
        String oidStr = objectId.toString();
        return em.find(UserLocation.class,oidStr);
    }

    //function to find the closest neighbour using a native named query
    public UserLocation findNearestUser(UserLocation userLocation){
        return em.createNamedQuery("UserLocation.findNearestLocation",UserLocation.class)
                .setParameter("lng",userLocation.getLocation().getX())
                .setParameter("lat",userLocation.getLocation().getY())
                .getSingleResult();
    }

    //find the distance between two points in space
    public Double findDistance(Point p1 , Point p2){
        return em.createNamedQuery("UserLocation.findDistance", Double.class)
                .setParameter("lng1", p1.getX())
                .setParameter("lat1", p1.getY())
                .setParameter("lng2", p2.getX())
                .setParameter("lat2", p2.getY())
                .getSingleResult();
    }
}