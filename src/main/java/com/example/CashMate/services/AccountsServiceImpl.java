package com.example.CashMate.services;

import com.example.CashMate.data.*;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.util.UserGeneralChecks;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountsServiceImpl implements AccountsService{

    @PersistenceContext
    private EntityManager entityManager;
    UserGeneralChecks userGeneralChecks;
    AccountRepository accountRepository;
    UserAccountRepository userAccountRepository;
    CashUserRepository cashUserRepository;
    TransactionRepository transactionRepository;

    ModelMapper modelMapper;


    public AccountsServiceImpl(UserGeneralChecks userGeneralChecks,
                               AccountRepository accountRepository,
                               UserAccountRepository userAccountRepository,
                               CashUserRepository cashUserRepository,
                               TransactionRepository transactionRepository,
                               ModelMapper modelMapper) {
        this.userGeneralChecks = userGeneralChecks;
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
        this.cashUserRepository = cashUserRepository;
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AccountDTO save(AccountDTO accountDTO){
        userGeneralChecks.userValidityCheck(accountDTO.getUser_id());

        Optional<Account> findAccount = accountRepository.findById(accountDTO.getId());

        if(findAccount.isPresent()){
            findAccount.get().setName(accountDTO.getName());
            Account accountSaved = accountRepository.save(findAccount.get());
            return modelMapper.map(accountSaved, AccountDTO.class);
        }else {
            Account accountSaved = accountRepository.save(modelMapper.map(accountDTO, Account.class));

            if(cashUserRepository.existsById(accountDTO.getUser_id())){
                UserAccountId userAccountId = new UserAccountId(accountDTO.getId(), accountDTO.getUser_id());
                if(!userAccountRepository.existsById(userAccountId)){
                    UserAccount userAccount = new UserAccount(userAccountId);
                    userAccountRepository.save(userAccount);
                } else {
                    log.error("Cannot create association between user and account. Check if already exists.");
                }
            } else {
                log.error("Cannot save account. The user does not exist");
            }

            return modelMapper.map(accountSaved, AccountDTO.class);
        }
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
    public AccountDTO getById(long accountID){
        Account account = accountRepository.findById(accountID).get();
        if (account == null){
            throw new RuntimeException("Account not found!");
        }
        return modelMapper.map(account, AccountDTO.class);
    }

    @Override
    public AccountDTO GetAccount(long accountID, long userID) {
        CheckUserViewPermissionOnAccount(accountID, userID);
        Account account = accountRepository.findById(accountID).orElse(null);
        if (account == null){
            throw new RuntimeException("Account not found!");
        }
        return modelMapper.map(account, AccountDTO.class);
    }

    @Override
    public String GetAccountOwnerName(AccountDTO account){

        if(account == null){
            throw new RuntimeException("Accounts not found!");
        }

        Optional<CashUser> cashUser = cashUserRepository.findById(account.getUser_id());

        if(cashUser.isEmpty()){
            throw new RuntimeException("User not found!");
        }

        return cashUser.get().getName();
    }

    @Override
    public Set<AccountDTO> GetAllAccountsByUser(long userID) {
        userGeneralChecks.userValidityCheck(userID);
        Set<Account> accounts = Objects.requireNonNull(cashUserRepository.findById(userID).orElse(null)).getAccounts();
        if (accounts == null){
            throw new RuntimeException("Accounts not found!");
        }

        return accounts.stream().map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toSet());
    }

    @Override
    public Set<AccountDTO> GetAll(){
        List<Account> accounts = Objects.requireNonNull(accountRepository.findAll());
        if (accounts == null){
            throw new RuntimeException("Accounts not found!");
        }

        return accounts.stream().map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toSet());

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
