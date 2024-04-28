package com.example.CashMate.repositories;

import com.example.CashMate.data.Account;
import com.example.CashMate.data.UserAccount;
import com.example.CashMate.data.UserAccountId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Transactional
    @Query(value="DELETE FROM user_account WHERE account_id = ?1 AND user_id = ?2", nativeQuery = true)
    void deleteByAccountIdAndUserId(long accountID, long userID);
}
