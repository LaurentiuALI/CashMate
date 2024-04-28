package com.example.CashMate.services;

import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;

import java.util.List;
import java.util.Set;

public interface AccountsService {
    List<CashUserDTO> GetAccountMembers(long accountID);
    String AddAccountMember(long accountID, long ownerID, long userID);
    String RemoveAccountMember(long accountID, long ownerID, long userID);
    String RemoveAccount(long accountID);
    AccountDTO GetAccount(long accountID, long userID);
    Set<AccountDTO> GetAllAccountsByUser(long userID);
    Set<AccountDTO> GetAll();
    AccountDTO getById(long accountID);
    AccountDTO updateAccount(AccountDTO accountDTO);
    String GetAccountOwnerName(AccountDTO account);
    AccountDTO createAccount(AccountDTO accountDTO);
}
