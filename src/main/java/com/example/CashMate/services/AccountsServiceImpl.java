package com.example.CashMate.services;

import com.example.CashMate.data.*;
import com.example.CashMate.util.UserGeneralChecks;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class AccountsServiceImpl implements AccountsService{

    @PersistenceContext
    private EntityManager entityManager;
    UserGeneralChecks userGeneralChecks;
    AccountRepository accountRepository;
    UserAccountRepository userAccountRepository;
    CashUserRepository cashUserRepository;


    public AccountsServiceImpl(UserGeneralChecks userGeneralChecks, AccountRepository accountRepository, UserAccountRepository userAccountRepository, CashUserRepository cashUserRepository) {
        this.userGeneralChecks = userGeneralChecks;
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
        this.cashUserRepository = cashUserRepository;
    }

    @Override
    public String CreateAccount(Account account) {
        userGeneralChecks.userValidityCheck(account.getUser_id());
        accountRepository.save(account);
        UserAccountId userAccountId = new UserAccountId(account.getId(), account.getUser_id());
        UserAccount userAccount = new UserAccount(userAccountId);
        userAccountRepository.save(userAccount);
        return "Account created successfully";
    }

    @Override
    public String AddAccountMember(long accountID, long ownerID, long userID) {
        userGeneralChecks.userValidityCheck(userID);
        userGeneralChecks.userValidityCheck(ownerID);
        CheckUserAuthorityOnAccount(accountID, ownerID);
        UserAccountId userAccountId = new UserAccountId(accountID, userID);
        UserAccount userAccount = new UserAccount(userAccountId);
        userAccountRepository.save(userAccount);
        return "User added to account successfully";
    }

    @Override
    public String RemoveAccountMember(long accountID, long ownerID, long userID) {
        userGeneralChecks.userValidityCheck(userID);
        userGeneralChecks.userValidityCheck(ownerID);
        CheckUserAuthorityOnAccount(accountID, ownerID);
        UserAccountId userAccountId = new UserAccountId(accountID, userID);
        userAccountRepository.deleteById(userAccountId);
        return "User removed from account successfully";
    }

    @Override
    public String RemoveAccount(long accountID) {
        CheckUserAuthorityOnAccount(accountID, accountID);
        accountRepository.deleteById(accountID);
        return "Account removed successfully";
    }

    @Override
    @Transactional
    public String UpdateAccount(long accountId, String name, long userId){
        CheckUserAuthorityOnAccount(accountId, userId);
        Account updatedAccount = entityManager.find(Account.class, accountId);
        updatedAccount.setName(name);
        entityManager.persist(updatedAccount);
        return "Account updated successfully";
    }

    @Override
    public Account GetAccount(long accountID, long userID) {
        CheckUserViewPermissionOnAccount(accountID, userID);
        Account account = accountRepository.findById(accountID).orElse(null);
        if (account == null){
            throw new RuntimeException("Account not found!");
        }
        return account;
    }

    @Override
    public Set<Account> GetAllAccountsByUser(long userID) {
        userGeneralChecks.userValidityCheck(userID);
        Set<Account> accounts = Objects.requireNonNull(cashUserRepository.findById(userID).orElse(null)).getAccounts();
        if (accounts == null){
            throw new RuntimeException("Accounts not found!");
        }
        return accounts;
    }

    public void CheckUserAuthorityOnAccount(long accountID, long userID) {
        Account account = accountRepository.findById(accountID).orElse(null);
        if (account == null){
            throw new RuntimeException("Account not found!");
        }
        if (account.getUser_id() != userID) {
            throw new RuntimeException("User not authorized to perform this action!");
        }
    }

    public void CheckUserViewPermissionOnAccount(long accountID, long userID) {
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
