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
    TransactionDTO createTransaction(TransactionDTO transaction) throws Exception;
    String UpdateTransaction(Transaction transaction, long userID);
    void removeTransaction(long transactionID);
    String CreateRecursion(Recursion recursion, long userID);
    String UpdateRecursion(Recursion recursion);
    String RemoveRecursion(long userID, long accountID, long recursionID);
    Optional<Transaction> getTransactionsByID(long transactionID);
    Transaction GetTransactionsByUserID(long userID);
    Set<Transaction> getTransactionsByAccountID(long accountID);
    Page<Transaction> findAllTransactions(int page, int size, long accountId);
    List<Transaction> GetAllTransactions();
    Category GetCategoryByID(long categoryID);
    Category GetCategoryByTransactionID(long transactionID);
    List<Category> GetAllCategories();
    List<Category> GetAllCategoriesByAccountID(long accountID);
    List<Recursion> GetRecursionsByAccountID(long accountID);
}
