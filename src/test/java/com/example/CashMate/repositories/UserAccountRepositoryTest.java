package com.example.CashMate.repositories;

import com.example.CashMate.data.Account;
import com.example.CashMate.data.Transaction;
import com.example.CashMate.data.UserAccount;
import com.example.CashMate.data.UserAccountId;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import jakarta.transaction.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
public class UserAccountRepositoryTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Test
    public void testFindAll_shouldReturnAllUserAccounts() {
        List<UserAccount> expectedUserAccounts = Arrays.asList(
                new UserAccount(new UserAccountId(1L, 1L)),
                new UserAccount(new UserAccountId(2L, 2L))
        );

        when(userAccountRepository.findAll()).thenReturn(expectedUserAccounts);

        List<UserAccount> actualUserAccounts = userAccountRepository.findAll();

        assertThat(actualUserAccounts).isNotEmpty();
        assertThat(actualUserAccounts).containsExactlyElementsOf(expectedUserAccounts);
    }

    @Test
    public void testFindAll_whenNoUserAccountsExist_shouldReturnEmptyList() {
        when(userAccountRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserAccount> actualUserAccounts = userAccountRepository.findAll();

        assertThat(actualUserAccounts).isEmpty();
    }

    @Test
    public void testFindUserIDByAccountId_whenAccountIdExists_shouldReturnListOfUserIds() {
        long accountId = 1L;
        List<Long> expectedUserIds = Arrays.asList(1L, 2L);

        when(userAccountRepository.findUserIDByAccountId(accountId)).thenReturn(expectedUserIds);

        List<Long> actualUserIds = userAccountRepository.findUserIDByAccountId(accountId);

        assertThat(actualUserIds).isNotEmpty();
        assertThat(actualUserIds).containsExactlyElementsOf(expectedUserIds);
    }

    @Test
    public void testFindUserIDByAccountId_whenAccountIdDoesNotExist_shouldReturnEmptyList() {
        long accountId = 1L;

        when(userAccountRepository.findUserIDByAccountId(accountId)).thenReturn(Collections.emptyList());

        List<Long> actualUserIds = userAccountRepository.findUserIDByAccountId(accountId);

        assertThat(actualUserIds).isEmpty();
    }

    @Test
    public void testFindAccountsByUserId_whenUserIdExists_shouldReturnListOfAccounts() {
        long userId = 1L;
        List<Account> expectedAccounts = Arrays.asList(
                new Account(1L, "Account 1", 1L, new HashSet<Transaction>()),
                new Account(2L, "Account 2", 2L, new HashSet<Transaction>())
        );

        when(userAccountRepository.findAccountsByUserId(userId)).thenReturn(expectedAccounts);

        List<Account> actualAccounts = userAccountRepository.findAccountsByUserId(userId);

        assertThat(actualAccounts).isNotEmpty();
        assertThat(actualAccounts).containsExactlyElementsOf(expectedAccounts);
    }

    @Test
    public void testFindAccountsByUserId_whenUserIdDoesNotExist_shouldReturnEmptyList() {
        long userId = 1L;

        when(userAccountRepository.findAccountsByUserId(userId)).thenReturn(Collections.emptyList());

        List<Account> actualAccounts = userAccountRepository.findAccountsByUserId(userId);

        assertThat(actualAccounts).isEmpty();
    }

    @Test
    @Transactional
    public void testDeleteByAccountIdAndUserId_shouldDeleteUserAccount() {
        long accountId = 1L;
        long userId = 2L;
        UserAccount userAccountToDelete = new UserAccount(new UserAccountId(accountId, userId));

        Mockito.doNothing().when(userAccountRepository).deleteByAccountIdAndUserId(accountId, userId);

        userAccountRepository.save(userAccountToDelete);

        userAccountRepository.deleteByAccountIdAndUserId(accountId, userId);

        Mockito.verify(userAccountRepository).deleteByAccountIdAndUserId(accountId, userId);

        List<UserAccount> remainingAccounts = userAccountRepository.findAll();
        assertThat(remainingAccounts).doesNotContain(userAccountToDelete);
    }
}
