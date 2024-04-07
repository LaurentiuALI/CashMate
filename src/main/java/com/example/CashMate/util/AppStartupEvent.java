package com.example.CashMate.util;

import com.example.CashMate.data.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AppStartupEvent implements ApplicationListener<ApplicationReadyEvent> {

    private final CashUserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserAccountRepository userAccountRepository;
    private final CategoryRepository categoryRepository;
    private final RecursionRepository recursionRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionCategoryRepostiory transactionCategoryRepostiory;



    public AppStartupEvent(CashUserRepository userRepository, AccountRepository accountRepository,
                           UserAccountRepository userAccountRepository, CategoryRepository categoryRepository,
                           RecursionRepository recursionRepository, TransactionRepository transactionRepository, TransactionCategoryRepostiory transactionCategoryRepostiory) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
        this.categoryRepository = categoryRepository;
        this.recursionRepository = recursionRepository;
        this.transactionRepository = transactionRepository;
        this.transactionCategoryRepostiory = transactionCategoryRepostiory;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Iterable<TransactionCategory> userAccounts = transactionCategoryRepostiory.findAll();
        userAccounts.forEach(System.out::println);
    }
}
