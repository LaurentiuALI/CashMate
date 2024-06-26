package com.example.CashMate.services;

import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface AccountsService {
    AccountDTO createAccount(AccountDTO accountDTO);
    void addAccountMember(long accountID, long ownerID, String userID);

    Set<AccountDTO> getAllAccountsOwnedByUser(long userID);
    List<AccountDTO> getAllAccountsOwnedAndParticipantByUser(long userID);
    Page<AccountDTO> getAllAccountsOwnedAndParticipantByUser(long userID, int page, int size);
    CashUserDTO getAccountOwner(AccountDTO account);
    List<CashUserDTO> getAccountMembers(long accountID);
    AccountDTO getById(long accountID);

    AccountDTO updateAccount(AccountDTO accountDTO);

    String removeAccountMember(long accountID, long ownerID, long userID);
    String removeAccount(long accountID);

}
