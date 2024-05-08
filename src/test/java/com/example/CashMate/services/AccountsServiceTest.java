package com.example.CashMate.services;


import com.example.CashMate.data.Transaction;
import com.example.CashMate.data.security.CashUser;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.data.Account;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.repositories.AccountRepository;
import com.example.CashMate.repositories.TransactionRepository;
import com.example.CashMate.repositories.UserAccountRepository;
import com.example.CashMate.repositories.security.CashUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;



import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountsServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private CashUserRepository cashUserRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AccountsServiceImpl accountsService;

    @Test
    public void updateAccount() {
        Set<Transaction> transactions = new HashSet<Transaction>(){{
            add(new Transaction());
        }};
        AccountDTO accountDTO = new AccountDTO(1L, "accountName", 1L, transactions, "admin");

        Account account = new Account(1L, "accountName", 1L, transactions);

        when(modelMapper.map(account, AccountDTO.class)).thenReturn(accountDTO);
        when(accountRepository.findById(accountDTO.getId())).thenReturn(java.util.Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        AccountDTO result = accountsService.updateAccount(accountDTO);

        assertEquals(accountDTO, result);

    }

    @Test
    public void createAccount() {
        Set<Transaction> transactions = new HashSet<Transaction>(){{
            add(new Transaction());
        }};
        AccountDTO accountDTO = new AccountDTO(1L, "accountName", 1L, transactions, "admin");

        Account account = new Account(1L, "accountName", 1L, transactions);

        when(modelMapper.map(accountDTO, Account.class)).thenReturn(account);

        when(accountRepository.save(account)).thenReturn(account);

        AccountDTO result = accountsService.createAccount(accountDTO);

        assertEquals(accountDTO, result);

    }

    @Test
    public void getAccountMembers() {
        Set<Account> accounts = new HashSet<Account>(){{
            add(new Account());
        }};
        List<Long> userAccountIDs = new ArrayList<Long>(){{
            add(1L);
        }};
        List<CashUser> cashUsers = new ArrayList<CashUser>(){{
            add(new CashUser(1L, "cashUser", "password", accounts, null, null, null, null, null));
        }};
        List<CashUserDTO> cashUserDTOs = new ArrayList<CashUserDTO>(){{
            add(new CashUserDTO(1L, "cashUser", "password"));
        }};

        when(userAccountRepository.findUserIDByAccountId(1L)).thenReturn(userAccountIDs);
        when(cashUserRepository.findAllById(userAccountIDs)).thenReturn(cashUsers);

        when(modelMapper.map(cashUsers.get(0), CashUserDTO.class)).thenReturn(cashUserDTOs.get(0));

        List<CashUserDTO> result = accountsService.getAccountMembers(1L);

        Assertions.assertEquals(cashUserDTOs, result);

    }

    @Test
    public void removeAccountMember() {
        Assertions.assertEquals("User removed from account successfully", accountsService.removeAccountMember(1L, 1L, 1L));
    }

    @Test
    public void removeAccount() {
        Assertions.assertEquals("Account removed successfully", accountsService.removeAccount(1L));
    }

    @Test
    public void getById() {
        Set<Transaction> transactions = new HashSet<Transaction>(){{
            add(new Transaction());
        }};
        Optional<Account> account = Optional.of(new Account(1L, "accountName", 1L, transactions));
        AccountDTO accountDTO = new AccountDTO(1L, "accountName", 1L, transactions, "admin");


        when(accountRepository.findById(1L)).thenReturn(account);
        when(modelMapper.map(account, AccountDTO.class)).thenReturn(accountDTO);

        AccountDTO result = accountsService.getById(1L);


        assertEquals(accountDTO, result);
    }

    @Test
    public void getAccountOwner() {
        Set<Transaction> transactions = new HashSet<Transaction>(){{
            add(new Transaction());
        }};
        AccountDTO accountDTO = new AccountDTO(1L, "accountName", 1L, transactions, "admin");
        Optional<CashUser> optionalCashUser = Optional.of(new CashUser(1L, "cashUser", "password", null, null, null, null, null, null));
        CashUserDTO cashUserDTO = new CashUserDTO(1L, "cashUser", "password");

        when(cashUserRepository.findById(accountDTO.getUser_id())).thenReturn(optionalCashUser);
        when(modelMapper.map(optionalCashUser.get(), CashUserDTO.class)).thenReturn(cashUserDTO);

        CashUserDTO result = accountsService.getAccountOwner(accountDTO);

        assertEquals(cashUserDTO, result);
    }

    @Test
    public void getAllAccountsOwnedByUser() {
        Set<Account> accounts = new HashSet<Account>(){{
            add(new Account(1L, "accountName", 1L, null));
        }};
        CashUser cashUser = new CashUser(1L, "cashUser", "password", accounts, null, null, null, null, null);
        AccountDTO accountDTO = new AccountDTO(1L, "accountName", 1L, null, "cashUser");
        Set<AccountDTO> accountDTOs = new HashSet<AccountDTO>(){{
            add(accountDTO);
        }};

        when(cashUserRepository.findById(1L)).thenReturn(Optional.of(cashUser));
        when(modelMapper.map(any(Account.class), eq(AccountDTO.class))).thenReturn(accountDTO);

        Set<AccountDTO> result = accountsService.getAllAccountsOwnedByUser(1L);

        assertEquals(accountDTOs, result);

    }

    @Test
    public void getAllAccountsOwnedAndParticipantByUser() {
        Account account1 = new Account(1L, "accountName", 1L, null);
        Account account2 = new Account(2L, "accountName2", 2L, null);
        AccountDTO accountDTO1 = new AccountDTO(1L, "accountName", 1L, null, "cashUser");
        AccountDTO accountDTO2 = new AccountDTO(2L, "accountName2", 2L, null, "cashUser");
        Set<Account> ownedAccounts = new HashSet<Account>(){{
            add(account1);
        }};
        Optional<CashUser> user = Optional.of(new CashUser(1L, "cashUser", "password", ownedAccounts, null, null, null, null, null));
        List<Account> participantAccounts = new ArrayList<Account>(){{
            add(account2);
        }};

        List<AccountDTO> allAccounts = new ArrayList<AccountDTO>(){{
            add(accountDTO1);
            add(accountDTO2);
        }};

        when(cashUserRepository.findById(1L)).thenReturn(user);
        when(userAccountRepository.findAccountsByUserId(1L)).thenReturn(participantAccounts);
        when(modelMapper.map(any(Account.class), eq(AccountDTO.class))).thenReturn(allAccounts.get(0), allAccounts.get(1));

        List<AccountDTO> result = accountsService.getAllAccountsOwnedAndParticipantByUser(1L);

        assertEquals(2, result.size());
        assertEquals(allAccounts, result);
    }

    @Test
    public void getAllAccountsOwnedAndParticipantByUserPage() {
        Account account1 = new Account(1L, "accountName", 1L, null);
        Account account2 = new Account(2L, "accountName2", 2L, null);
        AccountDTO accountDTO1 = new AccountDTO(1L, "accountName", 1L, null, "cashUser");
        AccountDTO accountDTO2 = new AccountDTO(2L, "accountName2", 2L, null, "cashUser");
        Set<Account> ownedAccounts = new HashSet<Account>(){{
            add(account1);
        }};
        Optional<CashUser> user = Optional.of(new CashUser(1L, "cashUser", "password", ownedAccounts, null, null, null, null, null));
        List<Account> participantAccounts = new ArrayList<Account>(){{
            add(account2);
        }};

        List<AccountDTO> allAccounts = new ArrayList<AccountDTO>(){{
            add(accountDTO1);
            add(accountDTO2);
        }};

        Page<AccountDTO> page = new PageImpl<>(allAccounts);

        when(cashUserRepository.findById(1L)).thenReturn(user);
        when(userAccountRepository.findAccountsByUserId(1L)).thenReturn(participantAccounts);
        when(modelMapper.map(any(Account.class), eq(AccountDTO.class))).thenReturn(allAccounts.get(0), allAccounts.get(1));

        Page<AccountDTO> result = accountsService.getAllAccountsOwnedAndParticipantByUser(1L, 0, 2);

        boolean equals = result.getContent().equals(page.getContent());

        assertEquals(2, result.getSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertEquals(true, equals);
    }
}
