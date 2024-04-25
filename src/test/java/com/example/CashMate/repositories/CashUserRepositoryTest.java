package com.example.CashMate.repositories;


import com.example.CashMate.data.security.CashUser;
import com.example.CashMate.repositories.security.CashUserRepository;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertFalse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
public class CashUserRepositoryTest {

    CashUserRepository cashUserRepository;

    @Autowired
    public CashUserRepositoryTest(CashUserRepository cashUserRepository) {
        this.cashUserRepository = cashUserRepository;
    }

    @Test
    public void findByName(){
        List<CashUser> users = cashUserRepository.findByName("Fiona");
        assertFalse(users.isEmpty());

        log.info("===== Find User By Name =====");
        users.forEach(user -> log.info(user.getName()));
    }

}
