package com.example.CashMate.repositories;

import com.example.CashMate.data.Category;
import com.example.CashMate.data.Transaction;
import com.example.CashMate.data.TransactionCategory;
import com.example.CashMate.data.TransactionCategoryId;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
public class TransactionCategoryRepositoryTest {

    @Mock
    private TransactionCategoryRepository transactionCategoryRepository;

    @Test
    public void testFindByTransactionId_whenTransactionIdExists_shouldReturnTransactionCategories() {
        long transactionId = 1L;
        List<TransactionCategory> expectedCategories = Arrays.asList(
                new TransactionCategory(new TransactionCategoryId(1L, 1L), new Transaction(), new Category()),
                new TransactionCategory(new TransactionCategoryId(1L, 2L), new Transaction(), new Category())
        );

        when(transactionCategoryRepository.findByTransactionId(transactionId)).thenReturn(expectedCategories);

        List<TransactionCategory> actualCategories = transactionCategoryRepository.findByTransactionId(transactionId);

        assertThat(actualCategories).isNotEmpty();
        assertThat(actualCategories).containsExactlyElementsOf(expectedCategories);
    }

    @Test
    public void testFindByTransactionId_whenTransactionIdDoesNotExist_shouldReturnEmptyList() {
        long transactionId = 1L;

        when(transactionCategoryRepository.findByTransactionId(transactionId)).thenReturn(Collections.emptyList());

        List<TransactionCategory> actualCategories = transactionCategoryRepository.findByTransactionId(transactionId);

        assertThat(actualCategories).isEmpty();
    }
}
