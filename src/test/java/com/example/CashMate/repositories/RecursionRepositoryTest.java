package com.example.CashMate.repositories;

import com.example.CashMate.data.Recursion;
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
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
public class RecursionRepositoryTest {

    @Mock
    private RecursionRepository recursionRepository;

    @Test
    public void testFindAll_shouldReturnAllRecursions() {
        List<Recursion> expectedRecursions = Arrays.asList(
                new Recursion(1L, new Date()),
                new Recursion(2L, new Date())
        );

        Mockito.when(recursionRepository.findAll()).thenReturn(expectedRecursions);

        List<Recursion> actualRecursions = recursionRepository.findAll();

        assertThat(actualRecursions).isNotEmpty();
        assertThat(actualRecursions).containsExactlyElementsOf(expectedRecursions);
    }

    @Test
    public void testFindAll_whenNoRecursionsExist_shouldReturnEmptyList() {
        Mockito.when(recursionRepository.findAll()).thenReturn(Collections.emptyList());

        List<Recursion> actualRecursions = recursionRepository.findAll();

        assertThat(actualRecursions).isEmpty();
    }

    @Test
    public void testFindByTransactionId_whenTransactionIdExists_shouldReturnRecursion() {
        long transactionId = 1L;
        Recursion expectedRecursion = new Recursion(transactionId, new Date());

        Mockito.when(recursionRepository.findByTransactionId(transactionId)).thenReturn(expectedRecursion);

        Recursion actualRecursion = recursionRepository.findByTransactionId(transactionId);

        assertThat(actualRecursion).isNotNull();
        assertThat(actualRecursion).isEqualTo(expectedRecursion);
    }

    @Test
    public void testFindByTransactionId_whenTransactionIdDoesNotExist_shouldReturnNull() {
        long transactionId = 1L;

        Mockito.when(recursionRepository.findByTransactionId(transactionId)).thenReturn(null);

        Recursion actualRecursion = recursionRepository.findByTransactionId(transactionId);

        assertThat(actualRecursion).isNull();
    }

}
