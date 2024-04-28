package com.example.CashMate.services;

import com.example.CashMate.data.Category;
import com.example.CashMate.data.Recursion;
import com.example.CashMate.data.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TransactionsService {
    String CreateTransaction(Transaction transaction, long userID);
    String UpdateTransaction(Transaction transaction, long userID);
    String RemoveTransaction(long userID, long accountID, long transactionID);
    String CreateRecursion(Recursion recursion, long userID);
    String UpdateRecursion(Recursion recursion);
    String RemoveRecursion(long userID, long accountID, long recursionID);
    Optional<Transaction> GetTransactionsByID(long transactionID);
    Transaction GetTransactionsByUserID(long userID);
    Set<Transaction> getTransactionsByAccountID(long accountID);
    List<Transaction> GetAllTransactions();
    Category GetCategoryByID(long categoryID);
    Category GetCategoryByTransactionID(long transactionID);
    List<Category> GetAllCategories();
    List<Category> GetAllCategoriesByAccountID(long accountID);
    List<Recursion> GetRecursionsByAccountID(long accountID);
}
