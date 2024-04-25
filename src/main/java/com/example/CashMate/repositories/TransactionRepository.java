package com.example.CashMate.repositories;

import com.example.CashMate.data.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findAll();
}
