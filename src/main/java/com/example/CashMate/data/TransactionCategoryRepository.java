package com.example.CashMate.data;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TransactionCategoryRepository extends CrudRepository <TransactionCategory, TransactionCategoryId> {

    @Query( value = "SELECT * FROM transaction_category WHERE transaction_id = ?1", nativeQuery = true)
    @Transactional
    TransactionCategory findByTransactionId(long transaction_id);
}
