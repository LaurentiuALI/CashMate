package com.example.CashMate.repositories;

import com.example.CashMate.data.Account;
import com.example.CashMate.data.UserAccount;
import com.example.CashMate.data.UserAccountId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, UserAccountId> {

//    @Query("SELECT DISTINCT userAccount FROM UserAccount userAccount")
    List<UserAccount> findAll();

    @Query("SELECT userAccount.id.user_id FROM UserAccount userAccount WHERE userAccount.id.account_id = ?1")
    List<Long> findUserIDByAccountId(long accountID);

    @Query("SELECT userAccount.account FROM UserAccount userAccount where userAccount.id.user_id = ?1")
    List<Account> findAccountsByUserId(long UserId );
}
