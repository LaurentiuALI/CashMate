package com.example.CashMate.data;


import com.example.CashMate.services.CashUserService;
import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest( classes=com.example.CashMate.CashMateApplication.class )
@ActiveProfiles("mysql")
public class CashUserEntityTest {

    @Autowired
    CashUserService cashUserService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CashUserRepository cashUserRepository;

    @Test
    public void updateAccountInfo(){
        Set<Account> foundAccounts = cashUserService.getUserAccounts(1L);

        System.out.println(foundAccounts);
        foundAccounts.forEach(account -> { account.setName("Testing account");});
        System.out.println(foundAccounts);


    }

    @Test
    public void findCashUser(){
        Optional<CashUser> foundUser = cashUserRepository.findById(1L);

        Assert.assertEquals("Alice", foundUser.get().getName());

    }

    @Test
    public void updateCashUser(){
        cashUserService.updateName(1L, "Clark");
        CashUser foundUser = cashUserService.find(1L);
        Assert.assertEquals("Clark", foundUser.getName());
    }

    @Test
    public void saveCashUser(){
        CashUser foundUser = new CashUser("Clark", "Kent");
        Account newAccount = new Account();

        newAccount.setUser_id(foundUser.getId());
        newAccount.setName("Test Account");

        Set<Account> set = new HashSet<>();
        set.add(newAccount);
        
        foundUser.setAccounts(set);

        cashUserRepository.save(foundUser);
    }

//    @Test
//    public void deleteCashUser(){
//        CashUser cashUser = new CashUser("Clark", "Kent");
//
//        cashUserRepository.save(cashUser);
//
//        Optional<CashUser> foundUser = cashUserRepository.findByName("Clark");
//        cashUserRepository.delete(foundUser.get());
//        Optional<CashUser> newFoundUser = cashUserRepository.findByName("Clark");
//
//        Assert.assertTrue(newFoundUser.isEmpty());
//    }
}
