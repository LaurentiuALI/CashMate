package com.example.CashMate.repositories;

import com.example.CashMate.data.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

    List<Transaction> findAll();

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long id);

    Optional<Transaction> delete(Transaction transaction);

    void deleteById(long transactionID);
}
