package com.example.CashMate.util;

import com.example.CashMate.data.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppStartupEvent implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserAccountRepository userAccountRepository;

    public AppStartupEvent(UserRepository userRepository, AccountRepository accountRepository, UserAccountRepository userAccountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Optional<User> user = userRepository.findById(1L);
        Optional<Account> account = accountRepository.findById(1L);

        UserAccountId userAcId = new UserAccountId(1, 1);
        Optional<UserAccount> userAccount = userAccountRepository.findById(userAcId);

        System.out.println(userAccount.get());

    }
}
