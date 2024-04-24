package com.example.CashMate.util;

import com.example.CashMate.data.*;
import org.springframework.stereotype.Component;

@Component
public class AccountGeneralChecks {

    AccountRepository accountRepository;
    UserAccountRepository userAccountRepository;

    public AccountGeneralChecks(AccountRepository accountRepository, UserAccountRepository userAccountRepository) {
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public void CheckAccountValidity(long accountID) {
        Account account = accountRepository.findById(accountID).orElse(null);
        if (account == null) {
            throw new RuntimeException("Account not found!");
        }
    }

    public void CheckUserAdministratorAuthorityOnAccount(long accountID, long userID) {
        Account account = accountRepository.findById(accountID).orElse(null);
        if (account == null){
            throw new RuntimeException("Account not found!");
        }
        if (account.getUser_id() != userID) {
            throw new RuntimeException("User not authorized to perform this action!");
        }
    }

    public void CheckUserMemberAuthorityOnAccount(long accountID, long userID) {
        Account account = accountRepository.findById(accountID).orElse(null);
        if (account == null) {
            throw new RuntimeException("Account not found!");
        }
        UserAccountId userAccountId = new UserAccountId(accountID, userID);
        UserAccount userAccount = userAccountRepository.findById(userAccountId).orElse(null);
        if (userAccount == null) {
            throw new RuntimeException("User not authorized to view this account!");
        }
    }

}
