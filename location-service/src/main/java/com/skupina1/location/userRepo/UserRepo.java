package com.skupina1.location.userRepo;
import com.skupina1.location.userLocation.UserLocation;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import net.postgis.jdbc.PGgeography;
import org.bson.types.ObjectId;
import org.locationtech.jts.geom.Point;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@ApplicationScoped
public  class UserRepo {
    @Inject
    @MyEM
    @PersistenceContext(unitName = "postgisPU")
    private EntityManager em;
    public UserRepo() {
    }
    public UserLocation addUserLocation(UserLocation userLocation) throws Exception {
        try{
            em.persist(userLocation);
            em.flush();
            em.refresh(userLocation);
            return userLocation;
        }catch(Exception e){
            throw new Exception(e);
        }
    }
    //function to update the user location
    public boolean changeLocation(Long id, Point newLocation) throws Exception {
        if (id == null) {
            return false;
        }
        try{
            List<UserLocation> userLocations = this.findLocationByUserId(id);
            if (userLocations.isEmpty()){
                return false ;
            }
            if(userLocations.size()!=1){
                return false ;
            }
            UserLocation userLocation = userLocations.get(0);
            userLocation.setLocation(newLocation);
            em.flush();
            em.refresh(userLocation);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void refresh(UserLocation location) {
        em.refresh(location);
    }
    public UserLocation findUserLocation(long id){
        return em.find(UserLocation.class,id);
    }
    public EntityTransaction  getTransaction(){
        return em.getTransaction();
    }
    public List<UserLocation> findLocationByUserId(Long id){
        if (id == null){
            return null;
        }
        return  em. createNamedQuery("UserLocation.findByUserId",UserLocation.class)
                .setParameter("id",id)
                .getResultList();

    }
    public UserLocation getUserLocation(Long id){
        if (id == null){
            return null;
        }
        List<UserLocation> userLocations = this.findLocationByUserId(id);
        if(userLocations.isEmpty()){
            return null;
        }
        if (userLocations.size()!=1){
            return null;
        }
        return userLocations.get(0);
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