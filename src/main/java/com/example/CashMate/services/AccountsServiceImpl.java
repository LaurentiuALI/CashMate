package com.example.CashMate.services;

import com.example.CashMate.data.*;
import com.example.CashMate.util.AccountGeneralChecks;
import com.example.CashMate.util.UserGeneralChecks;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
public class AccountsServiceImpl implements AccountsService{

    @PersistenceContext
    private EntityManager entityManager;
    UserGeneralChecks userGeneralChecks;
    AccountGeneralChecks accountGeneralChecks;
    AccountRepository accountRepository;
    UserAccountRepository userAccountRepository;
    CashUserRepository cashUserRepository;


    public AccountsServiceImpl(
            UserGeneralChecks userGeneralChecks,
            AccountGeneralChecks accountGeneralChecks,
            AccountRepository accountRepository,
            UserAccountRepository userAccountRepository,
            CashUserRepository cashUserRepository) {
        this.userGeneralChecks = userGeneralChecks;
        this.accountGeneralChecks = accountGeneralChecks;
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
        accountGeneralChecks.CheckUserAdministratorAuthorityOnAccount(accountID, ownerID);
        UserAccountId userAccountId = new UserAccountId(accountID, userID);
        UserAccount userAccount = new UserAccount(userAccountId);
        userAccountRepository.save(userAccount);
        return "User added to account successfully";
    }

    @Override
    public String RemoveAccountMember(long accountID, long ownerID, long userID) {
        userGeneralChecks.userValidityCheck(userID);
        userGeneralChecks.userValidityCheck(ownerID);
        accountGeneralChecks.CheckUserAdministratorAuthorityOnAccount(accountID, ownerID);
        UserAccountId userAccountId = new UserAccountId(accountID, userID);
        userAccountRepository.deleteById(userAccountId);
        return "User removed from account successfully";
    }

    @Override
    public String RemoveAccount(long accountID) {
        accountGeneralChecks.CheckUserAdministratorAuthorityOnAccount(accountID, accountID);
        accountRepository.deleteById(accountID);
        return "Account removed successfully";
    }

    @Override
    @Transactional
    public String UpdateAccount(long accountId, String name, long userId){
        accountGeneralChecks.CheckUserAdministratorAuthorityOnAccount(accountId, userId);
        Account updatedAccount = entityManager.find(Account.class, accountId);
        updatedAccount.setName(name);
        entityManager.persist(updatedAccount);
        return "Account updated successfully";
    }

    @Override
    public Account GetAccount(long accountID, long userID) {
        accountGeneralChecks.CheckUserMemberAuthorityOnAccount(accountID, userID);
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


}
