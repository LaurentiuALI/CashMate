package com.example.CashMate.services;

import com.example.CashMate.data.*;
import com.example.CashMate.dtos.TransactionDTO;
import com.example.CashMate.repositories.*;
import com.example.CashMate.util.AccountGeneralChecks;
import com.example.CashMate.util.UserGeneralChecks;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    private ModelMapper modelMapper;

    public TransactionsServiceImpl(
            UserGeneralChecks userGeneralChecks,
            AccountGeneralChecks accountGeneralChecks,
            TransactionRepository transactionRepository,
            TransactionCategoryRepository transactionCategoryRepository,
            CategoryRepository categoryRepository,
            RecursionRepository recursionRepository,
            AccountRepository accountRepository,
            ModelMapper modelMapper) {
        this.userGeneralChecks = userGeneralChecks;
        this.accountGeneralChecks = accountGeneralChecks;
        this.transactionRepository = transactionRepository;
        this.transactionCategoryRepository = transactionCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.recursionRepository = recursionRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<Transaction> findAllTransactions(int page, int size, long accountId) {
        Pageable pageable = PageRequest.of(page, size);

        return transactionRepository.findAllByAccount_Id(pageable, accountId);
    }

    @Override
    public Optional<Transaction> getTransactionsByID(long transactionID) {
        return transactionRepository.findById(transactionID);
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) throws Exception {

        Transaction transaction = new Transaction();

        Optional<Account> account = accountRepository.findById(transactionDTO.getAccount_id());
        if(account.isPresent()){
            transaction.setAccount( account.get() );
        }else{
            throw new Exception("Cannot save transaction. Account not found.");
        }

        transaction.setName(transactionDTO.getName());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setDate(transactionDTO.getDate());

        transactionRepository.save(transaction);
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Override
    public void removeTransaction(long transactionID) {
       transactionRepository.deleteById(transactionID);
    }

    @Override
    public List<Category> getCategoriesByTransactionId(long transactionID){
        List<TransactionCategory> transactionCategories = transactionCategoryRepository.findByTransactionId(transactionID);
        return transactionCategories.stream().map(TransactionCategory::getCategory).collect(Collectors.toList());
    }

    @Override
    public void addCategoriesToTransaction(long transactionID, List<Long> categoriesID){
        Optional<Transaction> transactionDTO = transactionRepository.findById(transactionID);

        if(transactionDTO.isPresent()){

            Transaction transaction = transactionDTO.get();

            for(long categoryId: categoriesID){
                Optional<Category> categoryDTO = categoryRepository.findById(categoryId);
                if(categoryDTO.isPresent()){
                    Category category = categoryDTO.get();

                    TransactionCategoryId transactionCategoryId = new TransactionCategoryId(transaction.getId(), category.getId());

                    TransactionCategory transactionCategory = new TransactionCategory(transactionCategoryId, transaction, category);

                    transactionCategoryRepository.save(transactionCategory);
                }
            }
        }
    }

    @Override
    @Transactional
    public String UpdateTransaction(Transaction transaction, long userID){
        accountGeneralChecks.CheckUserMemberAuthorityOnAccount(transaction.getAccount().getId(), userID);
        entityManager.persist(transaction);
        return "Account updated successfully";
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
    public Transaction GetTransactionsByUserID(long userID) {
        return null;
    }

    @Override
    public Set<Transaction> getTransactionsByAccountID(long accountID) {

        Optional<Account> accountOpt = accountRepository.findById(accountID);
        if(accountOpt.isPresent()){
            Account account = accountOpt.get();

            return account.getTransactions();

        }
        return null;
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
    public List<Category> GetAllCategories() {
        return categoryRepository.findAll();
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
