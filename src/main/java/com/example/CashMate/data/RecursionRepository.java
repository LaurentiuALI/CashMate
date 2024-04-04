package com.example.CashMate.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecursionRepository extends CrudRepository<Recursion, Long> {
    List<Recursion> findAll();
}
