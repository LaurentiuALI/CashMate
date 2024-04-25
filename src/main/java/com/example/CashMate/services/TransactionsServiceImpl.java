package com.example.CashMate.services;

import com.example.CashMate.data.*;
import com.example.CashMate.repositories.*;
import com.example.CashMate.util.AccountGeneralChecks;
import com.example.CashMate.util.UserGeneralChecks;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TransactionsServiceImpl implements TransactionsService{

    @PersistenceContext
    private EntityManager entityManager;
    UserGeneralChecks userGeneralChecks;
    AccountGeneralChecks accountGeneralChecks;
    TransactionRepository transactionRepository;
    TransactionCategoryRepository transactionCategoryRepository;
    CategoryRepository categoryRepository;
    RecursionRepository recursionRepository;
    AccountRepository accountRepository;

    public TransactionsServiceImpl(
            UserGeneralChecks userGeneralChecks,
            AccountGeneralChecks accountGeneralChecks,
            TransactionRepository transactionRepository,
            TransactionCategoryRepository transactionCategoryRepository,
            CategoryRepository categoryRepository,
            RecursionRepository recursionRepository,
            AccountRepository accountRepository) {
        this.userGeneralChecks = userGeneralChecks;
        this.accountGeneralChecks = accountGeneralChecks;
        this.transactionRepository = transactionRepository;
        this.transactionCategoryRepository = transactionCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.recursionRepository = recursionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public String CreateTransaction(Transaction transaction, long userID) {
        accountGeneralChecks.CheckUserMemberAuthorityOnAccount(transaction.getAccount().getId(), userID);
        transactionRepository.save(transaction);
        return "Transaction created successfully";
    }

    @Override
    @Transactional
    public String UpdateTransaction(Transaction transaction, long userID){
        accountGeneralChecks.CheckUserMemberAuthorityOnAccount(transaction.getAccount().getId(), userID);
        entityManager.persist(transaction);
        return "Account updated successfully";
    }

    @Override
    public String RemoveTransaction(long userID, long accountID, long transactionID) {
        accountGeneralChecks.CheckUserMemberAuthorityOnAccount(accountID, userID);
        transactionRepository.deleteById(transactionID);
        return "Transaction removed successfully";

    }

    @Override
    public String CreateRecursion(Recursion recursion, long userID) {
        recursionRepository.save(recursion);
        return "Recursion created successfully";
    }

    @Override
    @Transactional
    public String UpdateRecursion(Recursion recursion) {
        entityManager.persist(recursion);
        return "Recursion updated successfully";
    }

    @Override
    public String RemoveRecursion(long userID, long accountID, long recursionID) {
        recursionRepository.deleteById(recursionID);
        return "Recursion removed successfully";
    }

    @Override
    public Optional<Transaction> GetTransactionsByID(long transactionID) {
        return transactionRepository.findById(transactionID);
    }

    @Override
    public Transaction GetTransactionsByUserID(long userID) {
        return null;
    }

    @Override
    public Set<Transaction> GetTransactionsByAccountID(long accountID, long userID) {
        accountGeneralChecks.CheckUserMemberAuthorityOnAccount(accountID, userID);
        return accountRepository.findById(accountID).get().getTransactions();
    }

    @Override
    public List<Transaction> GetAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Category GetCategoryByID(long categoryID) {
        return categoryRepository.findById(categoryID).get();
    }

    @Override
    public Category GetCategoryByTransactionID(long transactionID) {
        TransactionCategory transactionCategory = transactionCategoryRepository.findByTransactionId(transactionID);
        return transactionCategory.getCategory();
    }

    @Override
    public List<Category> GetAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> GetAllCategoriesByAccountID(long accountID) {
        Account account = accountRepository.findById(accountID).get();
        List<Transaction> transactions = (List<Transaction>) account.getTransactions();
        List<Category> categories = null;
        for (Transaction transaction : transactions) {
            TransactionCategory transactionCategory = transactionCategoryRepository.findByTransactionId(transaction.getId());
            categories.add(transactionCategory.getCategory());
        }
        return categories;
    }

    @Override
    public List<Recursion> GetRecursionsByAccountID(long accountID) {
        Account account = accountRepository.findById(accountID).get();
        List<Transaction> transactions = (List<Transaction>) account.getTransactions();
        List<Recursion> recursions = null;
        for (Transaction transaction : transactions) {
            recursions.add(recursionRepository.findByTransactionId(transaction.getId()));
        }
        return recursions;
    }

}
