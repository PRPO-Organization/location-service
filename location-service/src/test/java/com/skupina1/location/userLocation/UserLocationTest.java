package com.skupina1.location.userLocation;


import com.skupina1.location.generateLocations.LocationGenerator;
import com.skupina1.location.userLocation.UserLocation;
import com.skupina1.location.userRepo.EntityManagerProducer;
import com.skupina1.location.userRepo.UserRepo;
import com.skupina1.location.userRepo.UserRepoProducer;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.List;

public class UserLocationTest {


    @Test
    public void testUserLocation() {
        EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
        try {
            EntityManager em = entityManagerProducer.createEntityManager();

            UserRepo userRepo = new UserRepo();
            userRepo.em = em;
            GeometryFactory geometryFactory = new GeometryFactory();

            Point location = geometryFactory.createPoint(new Coordinate(14.54, 43.32));
            location.setSRID(4326);
            UserLocation userLocation = new UserLocation("userId", location);
            em.getTransaction().begin();
            userRepo.addUserLocation(userLocation);
            em.getTransaction().commit();
            em.close();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testDistance() {
        EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
        UserRepo userRepo = new UserRepo();
        try{
           EntityManager em = entityManagerProducer.createEntityManager();
           userRepo.em = em;
           GeometryFactory geometryFactory = new GeometryFactory();
           Point location1 = geometryFactory.createPoint(new Coordinate(50.54, 43.32));
           location1.setSRID(4326);
           Point location2 = geometryFactory.createPoint(new Coordinate(14.54, 43.32));
           location2.setSRID(4326);
           double distance = userRepo.findDistance(location1, location2);
            System.out.println("distance = " + distance);
//            assert distance == 0;?
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGenerateLocations(){
        EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
        UserRepo userRepo = new UserRepo();
        LocationGenerator locationGenerator = new LocationGenerator();
        try{
            EntityManager em = entityManagerProducer.createEntityManager();
            userRepo.em = em;
            List<UserLocation> userLocations = locationGenerator.generateLocations(10);
            for (UserLocation userLocation : userLocations) {
                em.getTransaction().begin();
                userRepo.addUserLocation(userLocation);
                em.getTransaction().commit();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNearestLocation(){
        //14.5058, 46.0569
        EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
        UserRepo userRepo = new UserRepo();
        double lat = 14.5058;
        double lng = 46.0569;
        GeometryFactory geometryFactory = new GeometryFactory();
        Point location = geometryFactory.createPoint(new Coordinate(lat, lng));
        UserLocation point = new UserLocation("targetId", location);
        List<UserLocation> userLocations = LocationGenerator.generateLocations(10);

        try{
            EntityManager em = entityManagerProducer.createEntityManager();
            userRepo.em = em;
            for (UserLocation userLocation : userLocations) {
                em.getTransaction().begin();
                userRepo.addUserLocation(userLocation);
                em.getTransaction().commit();
            }
            UserLocation nearestUser = userRepo.findNearestUser(point);
            System.out.printf("%f %f\n",nearestUser.getLocation().getX(),nearestUser.getLocation().getY());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}