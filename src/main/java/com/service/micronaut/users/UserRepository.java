package com.service.micronaut.users;

import io.micronaut.transaction.annotation.ReadOnly;
import io.micronaut.transaction.annotation.TransactionalAdvice;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;

@Singleton
public class UserRepository  {

    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @ReadOnly
    public List<UserData> findAll() {
        return entityManager.createQuery("select u from UserData u", UserData.class)
                .getResultList();
    }

    @ReadOnly
    public UserData findById(Long userId) {
        return entityManager.find(UserData.class, userId);
    }

    @TransactionalAdvice
    public UserData save(UserData userData) {
        entityManager.persist(userData);
        return userData;
    }

    @TransactionalAdvice
    public void deleteById(Long userId) {
        UserData userData = entityManager.find(UserData.class, userId);
        entityManager.remove(userData);
    }

}
