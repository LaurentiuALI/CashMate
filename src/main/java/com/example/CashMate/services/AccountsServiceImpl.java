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
import com.example.CashMate.repositories.UserAccountRepository;
import com.example.CashMate.repositories.security.CashUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountsServiceImpl implements AccountsService{

    private final AccountRepository accountRepository;
    private final UserAccountRepository userAccountRepository;
    private final CashUserRepository cashUserRepository;
    private final ModelMapper modelMapper;

    public AccountsServiceImpl(AccountRepository accountRepository,
                               UserAccountRepository userAccountRepository,
                               CashUserRepository cashUserRepository,
                               ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
        this.cashUserRepository = cashUserRepository;
        this.modelMapper = modelMapper;
        log.info("AccountsServiceImpl instantiated.");
    }

    @Override
    public AccountDTO updateAccount(AccountDTO accountDTO){
        log.info("Updating account with id: {}", accountDTO.getId());
        Optional<Account> accountOpt = accountRepository.findById(accountDTO.getId());

        if(accountOpt.isPresent()){
            Account account = accountOpt.get();
            account.setName(accountDTO.getName());
            Account updatedAccount = accountRepository.save(account);
            return modelMapper.map(updatedAccount, AccountDTO.class);
        } else {
            log.error("Cannot update account. Account with ID {} was not found", accountDTO.getId());
            throw new ResourceNotFoundException("Account with ID " + accountDTO.getId() + " was not found");
        }
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO){
        log.info("Creating account: {}", accountDTO);
        Account account = modelMapper.map(accountDTO, Account.class);
        accountRepository.save(account);
        return accountDTO;
    }

    @Override
    public List<CashUserDTO> getAccountMembers(long accountID) {
        log.info("Fetching members for account ID: {}", accountID);
        List<Long> userAccountIDs = userAccountRepository.findUserIDByAccountId(accountID);
        List<CashUser> users = cashUserRepository.findAllById(userAccountIDs);
        return users.stream()
                .map(user -> modelMapper.map(user, CashUserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addAccountMember(long accountID, long ownerID, String userName) {
        log.info("Adding member to account: accountID={}, ownerID={}, userName={}", accountID, ownerID, userName);

        List<CashUser> users = cashUserRepository.findByName(userName);

        if(users.isEmpty()) {
            throw new CashUserNotFoundException("Couldn't find user " + userName + " to associate!");
        }

        CashUser user = users.get(0);
        UserAccountId userAccountId = new UserAccountId(accountID, user.getId());
        Optional<CashUser> cashUserOpt = cashUserRepository.findById(user.getId());
        Optional<Account> accountOpt = accountRepository.findById(accountID);

        if(cashUserOpt.isPresent() && accountOpt.isPresent()){
            UserAccount userAccount = new UserAccount(userAccountId, cashUserOpt.get(), accountOpt.get());
            userAccountRepository.save(userAccount);
            log.info("User {} added to account successfully", userName);
        } else {
            throw new CashUserNotFoundException("Couldn't find User to associate!");
        }
    }

    @Override
    public String removeAccountMember(long accountID, long ownerID, long userID) {
        log.info("Removing member from account: accountID={}, ownerID={}, userID={}", accountID, ownerID, userID);
        userAccountRepository.deleteByAccountIdAndUserId(accountID, userID);
        return "User removed from account successfully";
    }

    @Override
    public String removeAccount(long accountID) {
        log.info("Removing account with ID: {}", accountID);
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


    @Override
    public Page<AccountDTO> getAllAccountsOwnedAndParticipantByUser(long userID, int page, int size){

        Optional<CashUser> user = cashUserRepository.findById(userID);

        Pageable pageable = PageRequest.of(page, size);

        List<Account> participantAccounts = userAccountRepository.findAccountsByUserId(userID);

        user.ifPresent(cashUser -> participantAccounts.addAll(cashUser.getAccounts()));

        participantAccounts.sort((a1, a2) -> (int) (a1.getId() - a2.getId()));

        List<AccountDTO> accountDTOS = participantAccounts.stream().map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toList());

        List<AccountDTO> accountDTOList = accountDTOS.subList(page * size, Math.min((page + 1) * size, accountDTOS.size()));


        return new PageImpl<>(accountDTOList, pageable, accountDTOS.size());
    }
}
