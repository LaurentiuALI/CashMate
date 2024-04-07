package com.example.CashMate.services;

public interface TransactionsService {
    void CreateTransaction();
    void UpdateTransaction();
    void RemoveTransaction();
    void CreateRecursion();
    void UpdateRecursion();
    void RemoveRecursion();
    void CreateCategory();
    void UpdateCategory();
    void RemoveCategory();
    void GetTransactionsByID();
    void GetTransactionsByUserID();
    void GetTransactionsByAccountID();
    void GetAllTransactions();
    void GetCategoryByID();
    void GetCategoryByTransactionID();
    void GetAllCategories();
    void GetAllCategoriesByAccountID();
    void GetRecursionByID();
    void GetRecursionByTransactionID();
    void GetRecursionsByAccount();
    void GetRecursionsByUserID();
    void GetAllRecursions();
}
