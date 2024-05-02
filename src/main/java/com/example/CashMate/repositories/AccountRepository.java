package com.example.CashMate.repositories;

import com.example.CashMate.data.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    Optional<Account> findById(long id);
    int count();
    void deleteById(long id);
    Account save(Account product);
}
