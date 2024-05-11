package com.example.CashMate.repositories;

import com.example.CashMate.data.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

    List<Transaction> findAll();

    Page<Transaction> findAllByAccount_Id(Pageable page, long accountId);

    Page<Transaction> findAll(Pageable page);

    @Query("select transaction from Transaction transaction where transaction.account.id in :ids")
    Page<Transaction> findAllForUser(@Param("ids") List<Long> accountIds, Pageable page);

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long id);

    Optional<Transaction> delete(Transaction transaction);

    Optional<Transaction> deleteById(long transactionID);




}
