package com.example.CashMate.repositories;

import com.example.CashMate.data.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findAll();
}
