package com.example.CashMate.services;

import com.example.CashMate.data.*;
import com.example.CashMate.dtos.TransactionDTO;
import com.example.CashMate.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionCategoryRepository transactionCategoryRepository;
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TransactionsServiceImpl transactionsService;

    @Test
    public void findAllTransactions() {
        List<Transaction> transactions = new ArrayList<Transaction>() {{
            add(new Transaction());
            add(new Transaction());
        }};
        Page<Transaction> transactionPage = new PageImpl<>(transactions);

        when(transactionRepository.findAllByAccount_Id(any(Pageable.class), any(Long.class))).thenReturn(transactionPage);

        Page<Transaction> transactionsFound = transactionsService.findAllTransactions(0, 2, 1L);

        assertEquals(transactionsFound.getTotalElements(), 2);
        assertEquals(transactionsFound.getContent(), transactions);
    }

    @Test
    public void getTransactionByID() {
        Optional<Transaction> transaction = Optional.of(new Transaction());
        when(transactionRepository.findById(any(Long.class))).thenReturn(transaction);
        Optional<Transaction> transactionFound = transactionsService.getTransactionsByID(1L);
        assertEquals(transactionFound, transaction);
    }

    @Test
    public void createTransaction() throws Exception {
        Set<Transaction> transactions = new HashSet<Transaction>(){{
            add(new Transaction());
        }};
        Optional<Account> account = Optional.of(new Account( 1L, "accountName", 1L, transactions));
        Transaction transaction = new Transaction(1L, account.get(), "transactionName", "",123.0, Type.EXPENSE, new Date());
        TransactionDTO transactionDTO = new TransactionDTO(1L, 1L, "transactionName", "", 123.0, new Date(), Type.EXPENSE, new Recursion(), new ArrayList<Category>());
        when(accountRepository.findById(1L)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(modelMapper.map(any(Transaction.class), eq(TransactionDTO.class))).thenReturn(transactionDTO);
        TransactionDTO transactionCreated = transactionsService.createTransaction(transactionDTO);
        assertEquals(transactionCreated, transactionDTO);
    }

    @Test
    public void GetCategoriesByTransactionID() {
        List<TransactionCategory> trcategories = new ArrayList<TransactionCategory>(){{
            add(new TransactionCategory());
        }};
        List<Category> categories = trcategories.stream().map(TransactionCategory::getCategory).collect(Collectors.toList());
        when(transactionCategoryRepository.findByTransactionId(any(Long.class))).thenReturn(trcategories);
        List<Category> categoriesFound = transactionsService.getCategoriesByTransactionId(1L);
        assertEquals(categoriesFound, categories);
    }

}
