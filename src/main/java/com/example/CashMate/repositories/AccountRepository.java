package com.example.CashMate.repositories;

import com.example.CashMate.data.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    Optional<Account> findById(long id);

    void deleteById(long id);

    int count();

    Account save(Account product);

    @Transactional
    @Query(value = "SELECT * FROM account WHERE id IN (SELECT account_id FROM user_account WHERE user_id=?1)", nativeQuery = true)
    List<Account> findAllMemberShipAccounts(long userId);



}
