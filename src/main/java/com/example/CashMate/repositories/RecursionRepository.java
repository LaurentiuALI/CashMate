package com.example.CashMate.repositories;

import com.example.CashMate.data.Recursion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecursionRepository extends CrudRepository<Recursion, Long> {
    List<Recursion> findAll();

    @Query(value = "SELECT * FROM recursion WHERE transaction_id = ?1", nativeQuery = true)
    @Transactional
    Recursion findByTransactionId(long id);
}
