package com.example.CashMate.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, UserAccountId> {

    @Query("SELECT DISTINCT u FROM UserAccount u JOIN FETCH  u.user users JOIN FETCH u.account accounts")
    List<UserAccount> findAll();
}
