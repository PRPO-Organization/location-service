package com.skupina1.location.userRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class UserRepoProducer {
    private final static UserRepo userRepo = new UserRepo();
    @Produces
    public UserRepo produceUserRepo() {
        return userRepo;
    }
}