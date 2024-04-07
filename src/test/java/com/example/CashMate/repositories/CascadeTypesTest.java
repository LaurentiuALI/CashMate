package com.example.CashMate.repositories;

import com.example.CashMate.data.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
public class CascadeTypesTest {

    @Autowired
    CashUserRepository cashUserRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void insertCashUser(){
        CashUser cashUser = new CashUser();
        cashUser.setName("Clark");
        cashUser.setPassword("test123");
        cashUserRepository.save(cashUser);

        CashUser user = cashUserRepository.findByName("Clark").get(0);

        Account account = new Account();
        account.setName("Test account");
        account.setUser_id(user.getId());
        cashUser.addAccount(account);
        cashUserRepository.save(cashUser);

        user = cashUserRepository.findByName("Clark").get(0);
        Assertions.assertFalse(user.getAccounts().isEmpty());

    }

    @Test
    public void updateTransaction(){
        log.info("Updating transaction name...");
        Optional<Transaction> transactionOpt = transactionRepository.findById(1L);
        Transaction transaction = transactionOpt.get();
        log.info("Transaction name before: " + transaction.getName());

        transaction.setName("testing123");
        log.info("Transaction name after: " + transaction.getName());
        transactionRepository.save(transaction);

        log.info("Asserting transaction change propagated when querying account...");
        Optional<Account> accountOpt = accountRepository.findById(1L);
        Account account = accountOpt.get();
        account.getTransactions().forEach( transaction1 -> {
            if(transaction1.getId() == 1)
                Assertions.assertEquals(transaction1.getName(), "testing123");
        });
    }

    @Test
    public void deleteTransaction(){
        log.info("Deleting transaction...");
        Optional<Transaction> transactionOpt = transactionRepository.findById(1L);
        Transaction transaction = transactionOpt.get();
        String nameToCheck = transaction.getName();
        transactionRepository.delete(transaction);
        entityManager.flush();

        log.info("Asserting transaction delete propagated when querying account...");
        Optional<Account> accountOpt = accountRepository.findById(1L);
        Account account = accountOpt.get();
        account.getTransactions().forEach( transaction1 -> {
            Assertions.assertNotEquals(nameToCheck, transaction1.getName());
        });
    }

}

