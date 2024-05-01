package com.example.CashMate.services;

import com.example.CashMate.data.Category;
import com.example.CashMate.data.Recursion;
import com.example.CashMate.data.Transaction;
import com.example.CashMate.dtos.TransactionDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TransactionsService {
    Page<Transaction> findAllTransactions(int page, int size, long accountId);
    Optional<Transaction> getTransactionsByID(long transactionID);
    TransactionDTO createTransaction(TransactionDTO transaction) throws Exception;
    List<Category> getCategoriesByTransactionId(long transactionID);
    void addCategoriesToTransaction(long transactionID, List<Long> categoriesID);
    void removeTransaction(long transactionID);

    String UpdateTransaction(Transaction transaction, long userID);
    String CreateRecursion(Recursion recursion, long userID);
    String UpdateRecursion(Recursion recursion);
    String RemoveRecursion(long userID, long accountID, long recursionID);
    Transaction GetTransactionsByUserID(long userID);
    Set<Transaction> getTransactionsByAccountID(long accountID);
    List<Transaction> GetAllTransactions();
    Category GetCategoryByID(long categoryID);
    List<Category> GetAllCategories();
    List<Recursion> GetRecursionsByAccountID(long accountID);
}
