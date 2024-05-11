package com.example.CashMate.services;

import com.example.CashMate.data.Category;
import com.example.CashMate.data.Recursion;
import com.example.CashMate.data.Transaction;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.TransactionDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TransactionsService {
    Page<Transaction> findAllTransactions(int page, int size, long accountId);
    Page<Transaction> findAllTransactionSorted(int page, int size, List<AccountDTO> accounts, String field, String direction);
    Optional<Transaction> getTransactionsByID(long transactionID);
    List<Category> getCategoriesByTransactionId(long transactionID);

    TransactionDTO createTransaction(TransactionDTO transaction) throws Exception;
    void addCategoriesToTransaction(long transactionID, List<Long> categoriesID);

    void removeTransaction(long transactionID);
}
