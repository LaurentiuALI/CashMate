package com.example.CashMate.repositories;

import com.example.CashMate.data.Account;
import com.example.CashMate.data.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
public class TransactionRepositoryTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void testFindAll_shouldReturnAllTransactions() {
        List<Transaction> expectedTransactions = Arrays.asList(
                new Transaction(1L, new Account()),
                new Transaction(2L, new Account())
        );

        when(transactionRepository.findAll()).thenReturn(expectedTransactions);

        List<Transaction> actualTransactions = transactionRepository.findAll();

        assertThat(actualTransactions).isNotEmpty();
        assertThat(actualTransactions).containsExactlyElementsOf(expectedTransactions);
    }

    @Test
    public void testFindAll_whenNoTransactionsExist_shouldReturnEmptyList() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        List<Transaction> actualTransactions = transactionRepository.findAll();

        assertThat(actualTransactions).isEmpty();
    }

    @Test
    public void testFindAllByAccountId_shouldReturnPaginatedTransactions() {
        long accountId = 1L;
        Pageable mockPageable = PageRequest.of(0, 10); // Pageable with page 0 and size 10
        List<Transaction> expectedTransactions = Arrays.asList(
                new Transaction(1L, new Account()),
                new Transaction(2L, new Account())
        );
        Page<Transaction> expectedPage = new PageImpl<>(expectedTransactions, mockPageable, expectedTransactions.size());

        when(transactionRepository.findAllByAccount_Id(mockPageable, accountId)).thenReturn(expectedPage);

        Page<Transaction> actualPage = transactionRepository.findAllByAccount_Id(mockPageable, accountId);

        assertThat(actualPage).isNotEmpty();
        assertThat(actualPage.getContent()).containsExactlyElementsOf(expectedTransactions);
        assertThat(actualPage.getTotalElements()).isEqualTo(expectedTransactions.size());
    }

    @Test
    public void testSave_shouldSaveTransaction() {
        Transaction transactionToSave = new Transaction(1L, new Account());

        when(transactionRepository.save(transactionToSave)).thenReturn(transactionToSave);

        Transaction savedTransaction = transactionRepository.save(transactionToSave);

        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction).isEqualTo(transactionToSave);
    }

    @Test
    public void testFindById_whenTransactionExists_shouldReturnOptionalTransaction() {
        long transactionId = 1L;
        Transaction expectedTransaction = new Transaction(1L, new Account());

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(expectedTransaction));

        Optional<Transaction> actualOptionalTransaction = transactionRepository.findById(transactionId);

        assertThat(actualOptionalTransaction).isPresent();
        assertThat(actualOptionalTransaction.get()).isEqualTo(expectedTransaction);
    }

    @Test
    public void testDelete_shouldDeleteTransaction() {
        Transaction transactionToDelete = new Transaction(1L, new Account());

        when(transactionRepository.delete(transactionToDelete)).thenReturn(Optional.of(transactionToDelete));

        transactionRepository.delete(transactionToDelete);

        Mockito.verify(transactionRepository).delete(transactionToDelete);
    }


    @Test
    public void testDeleteById_shouldDeleteTransaction() {
        long transactionId = 1L;
        Transaction transactionToDelete = new Transaction(transactionId, new Account());

        when(transactionRepository.deleteById(transactionId)).thenReturn(Optional.of(transactionToDelete));

        transactionRepository.deleteById(transactionId);

        Mockito.verify(transactionRepository).deleteById(transactionId);
    }
}
