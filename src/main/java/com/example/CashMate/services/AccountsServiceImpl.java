package com.example.CashMate.services;

import com.example.CashMate.data.Account;
import com.example.CashMate.data.UserAccount;
import com.example.CashMate.data.UserAccountId;
import com.example.CashMate.data.security.CashUser;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.exceptions.CashUserNotFoundException;
import com.example.CashMate.exceptions.ResourceNotFoundException;
import com.example.CashMate.repositories.AccountRepository;
import com.example.CashMate.repositories.TransactionRepository;
import com.example.CashMate.repositories.UserAccountRepository;
import com.example.CashMate.repositories.security.CashUserRepository;
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

    AccountRepository accountRepository;
    UserAccountRepository userAccountRepository;
    CashUserRepository cashUserRepository;
    TransactionRepository transactionRepository;

    ModelMapper modelMapper;


    public AccountsServiceImpl(AccountRepository accountRepository,
                               UserAccountRepository userAccountRepository,
                               CashUserRepository cashUserRepository,
                               TransactionRepository transactionRepository,
                               ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
        this.cashUserRepository = cashUserRepository;
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AccountDTO updateAccount(AccountDTO accountDTO){
        Optional<Account> accountOpt = accountRepository.findById(accountDTO.getId());
        log.info("Updating info for account with id " + accountDTO.getId());

        if(accountOpt.isPresent()){
            accountOpt.get().setName(accountDTO.getName());
            Account accountSaved = accountRepository.save(accountOpt.get());
            return modelMapper.map(accountSaved, AccountDTO.class);
        }else {
                log.error("Cannot update account. Account with ID " + accountDTO.getId() + " was not found");
                throw new ResourceNotFoundException("Account with ID " + accountDTO.getId() + " was not found");
        }
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO){

        try{
            accountRepository.save(modelMapper.map(accountDTO,Account.class));
        }catch(Exception e){
            throw new CashUserNotFoundException(e.getMessage());
        }

        return accountDTO;
    }

    @Override
    public List<CashUserDTO> getAccountMembers(long accountID) {
        List <Long> userAccountIDs = userAccountRepository.findUserIDByAccountId(accountID);
        List <CashUser> userAccounts = cashUserRepository.findAllById(userAccountIDs);
        return userAccounts.stream().map(cashUser -> modelMapper.map(cashUser, CashUserDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void addAccountMember(long accountID, long ownerID, String userName) {
        log.info("adding member for AccountID" + accountID + "OwnerID" + ownerID + "UserID" + userName);

        List<CashUser> userToAdd = cashUserRepository.findByName(userName);

        if(userToAdd.isEmpty())
            throw new CashUserNotFoundException("Couldn't find user " + userName + " to associate!");

        CashUser user = userToAdd.get(0);

        UserAccountId userAccountId = new UserAccountId(accountID, user.getId());
        Optional<CashUser> cashUserOpt = cashUserRepository.findById(user.getId());
        Optional<Account> accountOpt = accountRepository.findById(accountID);

        if(cashUserOpt.isPresent() && accountOpt.isPresent()){
            UserAccount userAccount = new UserAccount(userAccountId, cashUserOpt.get(), accountOpt.get());
            userAccountRepository.save(userAccount);
            log.info("User " + userName + " added to account successfully");
        }else{
            throw new CashUserNotFoundException("Couldn't find User to associate!");
        }

    }

    @Override
    public String removeAccountMember(long accountID, long ownerID, long userID) {
        userAccountRepository.deleteByAccountIdAndUserId(accountID, userID);
        return "User removed from account successfully";
    }

    @Override
    public String removeAccount(long accountID) {
        accountRepository.deleteById(accountID);
        return "Account removed successfully";
    }

    @Override
    public AccountDTO getById(long accountID){
        Optional<Account> account = accountRepository.findById(accountID);
        if (account.isPresent()){
            return modelMapper.map(account, AccountDTO.class);
        }
        throw new ResourceNotFoundException("Account not found!");
    }

    @Override
    public CashUserDTO getAccountOwner(AccountDTO account){

        if(account == null){
            throw new ResourceNotFoundException("Accounts not found!");
        }

        Optional<CashUser> cashUser = cashUserRepository.findById(account.getUser_id());

        if(cashUser.isEmpty()){
            throw new ResourceNotFoundException("User not found!");
        }

        return modelMapper.map(cashUser.get(), CashUserDTO.class);
    }

    @Override
    public Set<AccountDTO> getAllAccountsOwnedByUser(long userID) {
        Set<Account> accounts = Objects.requireNonNull(cashUserRepository.findById(userID).orElse(null)).getAccounts();
        if (accounts == null){
            throw new ResourceNotFoundException("Accounts not found!");
        }

        return accounts.stream().map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toSet());
    }



    @Override
    public List<AccountDTO> getAllAccountsOwnedAndParticipantByUser(long userID){

        Optional<CashUser> user = cashUserRepository.findById(userID);

        List<Account> participantAccounts = userAccountRepository.findAccountsByUserId(userID);

        user.ifPresent(cashUser -> participantAccounts.addAll(cashUser.getAccounts()));

        participantAccounts.sort((a1, a2) -> (int) (a1.getId() - a2.getId()));

        return participantAccounts.stream().map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toList());
    }


}
