package com.example.CashMate.services;

import com.example.CashMate.data.*;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.TransactionDTO;
import com.example.CashMate.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionsServiceImpl implements TransactionsService{

    TransactionRepository transactionRepository;
    TransactionCategoryRepository transactionCategoryRepository;
    CategoryRepository categoryRepository;
    RecursionRepository recursionRepository;
    AccountRepository accountRepository;

    private ModelMapper modelMapper;

    public TransactionsServiceImpl(
            TransactionRepository transactionRepository,
            TransactionCategoryRepository transactionCategoryRepository,
            CategoryRepository categoryRepository,
            RecursionRepository recursionRepository,
            AccountRepository accountRepository,
            ModelMapper modelMapper) {
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
    public Page<Transaction> findAllTransactionSorted(int page, int size, List<AccountDTO> accounts, String field, String direction){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending() : Sort.by(field).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        List<Long> accountIds = accounts.stream().map(AccountDTO::getId).toList();

        return transactionRepository.findAllForUser(accountIds, pageable);
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


}
