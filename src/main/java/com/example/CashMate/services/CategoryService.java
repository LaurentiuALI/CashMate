package com.example.CashMate.services;

import com.example.CashMate.dtos.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();
}
