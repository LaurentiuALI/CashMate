package com.example.CashMate.services;

import com.example.CashMate.data.Account;
import com.example.CashMate.data.CashUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CashUserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public CashUser updateName(Long userId, String name){
        CashUser updatedUser = entityManager.find(CashUser.class, userId);
        updatedUser.setName(name);
        entityManager.persist(updatedUser);
        return updatedUser;
    }

    @Transactional
    public Set<Account> getUserAccounts(long userId) {
        CashUser cashUser = entityManager.find(CashUser.class, userId);
        return cashUser.getAccounts();
    }

    public CashUser find(Long userId){
        return entityManager.find(CashUser.class, userId);

    }

}
