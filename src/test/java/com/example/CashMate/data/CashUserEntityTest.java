package com.example.CashMate.data;

import com.example.CashMate.data.security.CashUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
public class CashUserEntityTest {

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void before(TestInfo testInfo){
        log.info("Running test " + testInfo.getDisplayName());
    }

    @Test
    public void findCashUser() {
       log.info("EntityManager: " + entityManager.getEntityManagerFactory());

       log.info("Searching user with ID 1.");
        CashUser cashUser = entityManager.find(CashUser.class, 1L);
        Assertions.assertEquals("Alice", cashUser.getName());

    }

    @Test
    public void findAccount() {
        log.info("EntityManager: " + entityManager.getEntityManagerFactory());

        log.info("Searching account with ID 1.");
        Account account = entityManager.find(Account.class, 1L);
        Assertions.assertEquals("Savings Account", account.getName());
    }

    @Test
    public void findCategory() {
        log.info("EntityManager: " + entityManager.getEntityManagerFactory());

        log.info("Searching category with ID 1.");
        Category category = entityManager.find(Category.class, 1L);
        Assertions.assertEquals("Food", category.getName());
        Assertions.assertEquals("Expenses related to food purchases", category.getDescription());
    }

    @Test
    public void findRecursion() {
        log.info("EntityManager: " + entityManager.getEntityManagerFactory());

        log.info("Searching recursion with ID 1.");
        Recursion recursion = entityManager.find(Recursion.class, 1L);

        String stringExpectedDate = "2024-03-01 00:00:00";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = formatter.parse(stringExpectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(date, recursion.getDate());
    }

    @Test
    public void findTransaction() {
        log.info("EntityManager: " + entityManager.getEntityManagerFactory());

        log.info("Searching transaction with ID 1.");
        Transaction transaction = entityManager.find(Transaction.class, 3L);

        String stringExpectedDate = "2024-03-01 00:00:00";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = formatter.parse(stringExpectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(transaction);

        Assertions.assertEquals("Groceries", transaction.getName());
        Assertions.assertEquals("Weekly grocery shopping", transaction.getDescription());
        Assertions.assertEquals(150.5, transaction.getAmount());
        Assertions.assertEquals(Type.EXPENSE, transaction.getType());
        Assertions.assertEquals(date, transaction.getDate());
    }
}
