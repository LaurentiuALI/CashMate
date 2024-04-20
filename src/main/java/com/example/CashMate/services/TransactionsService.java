package com.example.CashMate.services;

import com.example.CashMate.data.Category;
import com.example.CashMate.data.Recursion;
import com.example.CashMate.data.Transaction;

import java.util.List;

public interface TransactionsService {
    String CreateTransaction(Transaction transaction);
    String UpdateTransaction(Transaction transaction);
    String RemoveTransaction(long userID, long accountID, long transactionID);
    String CreateRecursion();
    String UpdateRecursion();
    String RemoveRecursion();
    String CreateCategory();
    String UpdateCategory();
    String RemoveCategory();
    Transaction GetTransactionsByID();
    Transaction GetTransactionsByUserID();
    Transaction GetTransactionsByAccountID();
    List<Transaction> GetAllTransactions();
    Category GetCategoryByID();
    Category GetCategoryByTransactionID();
    List<Category> GetAllCategories();
    List<Category> GetAllCategoriesByAccountID();
    Recursion GetRecursionByID();
    Recursion GetRecursionByTransactionID();
    List<Recursion> GetRecursionsByAccount();
    Recursion GetRecursionsByUserID();
    List<Recursion> GetAllRecursions();
}
