package com.example.CashMate.util;

import com.example.CashMate.data.*;
import jakarta.persistence.EntityManager;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AppStartupEvent implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserAccountRepository userAccountRepository;
    private final CategoryRepository categoryRepository;



    public AppStartupEvent(UserRepository userRepository, AccountRepository accountRepository, UserAccountRepository userAccountRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        List<User> userAccounts = userRepository.findAll();
        userAccounts.forEach(System.out::println);
    }
}
