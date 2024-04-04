package com.example.CashMate.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, UserAccountId> {

//    @Query("SELECT DISTINCT userAccount FROM UserAccount userAccount")
    List<UserAccount> findAll();
}
