package com.skupina1.location.userRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@RequestScoped
public class EntityManagerProducer {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("postgisPU");

    @Produces
    @RequestScoped
    @MyEM
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    // Add cleanup method if needed
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}