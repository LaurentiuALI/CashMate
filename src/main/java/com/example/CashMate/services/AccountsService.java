package com.example.CashMate.services;

import com.example.CashMate.data.Account;

import java.util.List;
import java.util.Set;

public interface AccountsService {
    String CreateAccount(Account account);
    String AddAccountMember(long accountID, long ownerID, long userID);
    String RemoveAccountMember(long accountID, long ownerID, long userID);
    String RemoveAccount(long accountID);
    String UpdateAccount(long accountId, String name, long userId);
    Account GetAccount(long accountID, long userID);
    Set<Account> GetAllAccountsByUser(long userID);
}
