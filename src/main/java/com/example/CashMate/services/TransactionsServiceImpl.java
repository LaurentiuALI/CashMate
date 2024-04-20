package com.example.CashMate.services;

import com.example.CashMate.data.*;
import com.example.CashMate.util.UserGeneralChecks;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionsServiceImpl implements TransactionsService{

    @PersistenceContext
    private EntityManager entityManager;
    UserGeneralChecks userGeneralChecks;
    TransactionRepository transactionRepository;
    TransactionCategoryRepository transactionCategoryRepository;
    CategoryRepository categoryRepository;
    RecursionRepository recursionRepository;

    public TransactionsServiceImpl(UserGeneralChecks userGeneralChecks, TransactionRepository transactionRepository, TransactionCategoryRepository transactionCategoryRepository, CategoryRepository categoryRepository, RecursionRepository recursionRepository) {
        this.userGeneralChecks = userGeneralChecks;
        this.transactionRepository = transactionRepository;
        this.transactionCategoryRepository = transactionCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.recursionRepository = recursionRepository;
    }

    @Override
    public String CreateTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        return "Transaction created successfully";
    }

    @Override
    @Transactional
    public String UpdateTransaction(Transaction transaction){
        entityManager.persist(transaction);
        return "Account updated successfully";
    }

    @Override
    public String RemoveTransaction(long userID, long accountID, long transactionID) {
        transactionRepository.deleteById(transactionID);
        return "Transaction removed successfully";

    }

    @Override
    public String CreateRecursion(Recursion recursion) {
        recursionRepository.save(recursion);
        return "Recursion created successfully";

    }

    @Override
    public String UpdateRecursion() {

    }

    @Override
    public String RemoveRecursion() {

    }

    @Override
    public String CreateCategory() {

    }

    @Override
    public String UpdateCategory() {

    }

    @Override
    public String RemoveCategory() {

    }

    @Override
    public Transaction GetTransactionsByID() {

    }

    @Override
    public Transaction GetTransactionsByUserID() {

    }

    @Override
    public Transaction GetTransactionsByAccountID() {

    }

    @Override
    public List<Transaction> GetAllTransactions() {

    }

    @Override
    public Category GetCategoryByID() {

    }

    @Override
    public Category GetCategoryByTransactionID() {

    }

    @Override
    public List<Category> GetAllCategories() {

    }

    @Override
    public List<Category> GetAllCategoriesByAccountID() {

    }

    @Override
    public Recursion GetRecursionByID() {

    }

    @Override
    public Recursion GetRecursionByTransactionID() {

    }

    @Override
    public List<Recursion> GetRecursionsByAccount() {

    }

    @Override
    public Recursion GetRecursionsByUserID() {

    }

    @Override
    public List<Recursion> GetAllRecursions() {

    }
}
