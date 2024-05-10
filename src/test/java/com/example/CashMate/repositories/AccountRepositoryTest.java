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
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
public class AccountRepositoryTest {

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void testFindById_whenIdExists_shouldReturnOptionalAccount() {
        long id = 1L;
        Account expectedAccount = new Account(id, "Test Account", 1L, new HashSet<Transaction>());

        when(accountRepository.findById(id)).thenReturn(Optional.of(expectedAccount));

        Optional<Account> actualAccount = accountRepository.findById(id);

        assertThat(actualAccount).isPresent();
        assertThat(actualAccount.get()).isEqualTo(expectedAccount);
    }

    @Test
    public void testFindById_whenIdDoesNotExist_shouldReturnEmptyOptional() {
        long id = 1L;

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Account> actualAccount = accountRepository.findById(id);

        assertThat(actualAccount).isEmpty();
    }

    @Test
    public void testCount_shouldReturnTotalAccountCount() {
        int expectedCount = 10;

        when(accountRepository.count()).thenReturn(expectedCount);

        int actualCount = accountRepository.count();

        assertThat(actualCount).isEqualTo(expectedCount);
    }

    @Test
    public void testDeleteById_shouldDeleteAccount() {
        long id = 1L;

        accountRepository.deleteById(id);

        verify(accountRepository).deleteById(id);
    }

    @Test
    public void testSave_shouldSaveAccount() {
        Account accountToSave = new Account(1L, "Test Account", 1L, new HashSet<Transaction>());

        when(accountRepository.save(accountToSave)).thenReturn(accountToSave);

        Account savedAccount = accountRepository.save(accountToSave);

        assertThat(savedAccount).isEqualTo(accountToSave);
    }


}
