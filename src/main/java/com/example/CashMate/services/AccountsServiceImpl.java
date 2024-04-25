package com.example.CashMate.services;

import com.example.CashMate.data.*;
import com.example.CashMate.data.security.CashUser;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.repositories.AccountRepository;
import com.example.CashMate.repositories.security.CashUserRepository;
import com.example.CashMate.repositories.TransactionRepository;
import com.example.CashMate.repositories.UserAccountRepository;
import com.example.CashMate.util.UserGeneralChecks;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    public AccountDTO updateAccount(AccountDTO accountDTO){
        userGeneralChecks.userValidityCheck(accountDTO.getUser_id());

        Optional<Account> findAccount = accountRepository.findById(accountDTO.getId());

        log.info("Updating info for account with id " + accountDTO.getId());

        if(findAccount.isPresent()){
            findAccount.get().setName(accountDTO.getName());
            Account accountSaved = accountRepository.save(findAccount.get());
            return modelMapper.map(accountSaved, AccountDTO.class);
        }else {
                log.error("Cannot save account. The user does not exist");
        }
        return null;
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO){
        userGeneralChecks.userValidityCheck(accountDTO.getUser_id());
        accountRepository.save( modelMapper.map(accountDTO,Account.class));

        return accountDTO;
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


    public String CheckUserAuthorityOnAccount(long accountID, long userID) {
        Account account = accountRepository.findById(accountID).orElse(null);
        if (account == null){
            throw new RuntimeException("Account not found!");
        }
        if (account.getUser_id() != userID) {
            return "accesDenied";
        }
        return "";
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
