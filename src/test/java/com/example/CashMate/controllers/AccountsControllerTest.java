package com.example.CashMate.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.CashMate.data.Transaction;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;



@ExtendWith(MockitoExtension.class)
public class AccountsControllerTest {

    @Mock
    private AccountsService accountsService;

    @Mock
    private CashUserService cashUserService;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Model mockModel;

    private Authentication authentication;
    private SecurityContext securityContext;

    @InjectMocks
    private AccountsController accountsController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void accountList_Success() throws Exception {
        CashUserDTO mockLoggedUser = new CashUserDTO(1L, "username", "password");
        List<AccountDTO> mockAccounts = new ArrayList<>();
        mockAccounts.add(new AccountDTO(1L, "account1", 1L, new HashSet<Transaction>(), "owner1"));
        Page<AccountDTO> mockAccountsPage = new PageImpl<>(mockAccounts);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(mockLoggedUser.getName());
        when(cashUserService.getByName(mockLoggedUser.getName())).thenReturn(mockLoggedUser);
        when(accountsService.getAllAccountsOwnedAndParticipantByUser(mockLoggedUser.getId(), 0, 10)).thenReturn(mockAccountsPage);
        when(accountsService.getAccountOwner(mockAccounts.get(0))).thenReturn(mockLoggedUser);


        String viewName = accountsController.accountList(mockModel, 0, 10);

        assertEquals("accountList", viewName);
        verify(cashUserService).getByName(mockLoggedUser.getName());
        verify(accountsService).getAllAccountsOwnedAndParticipantByUser(mockLoggedUser.getId(), 0, 10);
        for (AccountDTO mockAccount : mockAccounts) {
            verify(accountsService).getAccountOwner(mockAccount);
        }
        verify(mockModel).addAttribute("accounts", mockAccountsPage);
        verify(mockModel).addAttribute("loggedUser", mockLoggedUser);
    }

    @Test
    public void removeAccount() throws Exception {
        long accountId = 1L;

        String redirectUrl = accountsController.RemoveAccount(accountId);

        verify(accountsService).removeAccount(accountId);
        assertEquals("redirect:/accounts", redirectUrl);
    }

    @Test
    public void updateAccount() throws Exception {
        long accountId = 1L;
        AccountDTO mockAccount = new AccountDTO(1L, "account1", 1L, new HashSet<Transaction>(), "owner1");

        when(accountsService.getById(accountId)).thenReturn(mockAccount);

        String viewName = accountsController.UpdateAccount(mockModel, accountId);

        verify(accountsService).getById(accountId);
        verify(mockModel).addAttribute("account", mockAccount);
        assertEquals("accountForm", viewName);
    }

    @Test
    public void createAccount() throws Exception {
        AccountDTO mockAccount = new AccountDTO(1L, "account1", 1L, new HashSet<Transaction>(), "owner1");

        String viewName = accountsController.CreateAccount(mockModel);

        assertEquals("accountAdd", viewName);
    }

    @Test
    public void saveOrUpdate_Create() throws Exception {
        AccountDTO mockAccount = new AccountDTO(null, "account1", 1L, new HashSet<Transaction>(), "owner1");
        CashUserDTO mockLoggedUser = new CashUserDTO(1L, "username", "password");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(mockLoggedUser.getName());
        when(cashUserService.getByName(mockLoggedUser.getName())).thenReturn(mockLoggedUser);

        String redirectUrl = accountsController.saveOrUpdate(mockAccount);

        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verify(cashUserService).getByName(mockLoggedUser.getName());
        verify(accountsService).createAccount(mockAccount);
        assertEquals("redirect:/accounts", redirectUrl);
    }

    @Test
    public void saveOrUpdate_Update() throws Exception {
        AccountDTO mockAccount = new AccountDTO(1L, "account1", 1L, new HashSet<Transaction>(), "owner1");
        CashUserDTO mockLoggedUser = new CashUserDTO(1L, "username", "password");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(mockLoggedUser.getName());
        when(cashUserService.getByName(mockLoggedUser.getName())).thenReturn(mockLoggedUser);

        String redirectUrl = accountsController.saveOrUpdate(mockAccount);

        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verify(cashUserService).getByName(mockLoggedUser.getName());
        verify(accountsService).updateAccount(mockAccount);
        assertEquals("redirect:/accounts", redirectUrl);
    }

    @Test
    public void getAccountMembers() throws Exception {
        long accountId = 1L;
        List<CashUserDTO> mockMembers = new ArrayList<>();
        mockMembers.add(new CashUserDTO(1L, "username", "password"));

        when(accountsService.getAccountMembers(accountId)).thenReturn(mockMembers);

        String viewName = accountsController.GetAccountMembers(mockModel, accountId);

        verify(accountsService).getAccountMembers(accountId);
        verify(mockModel).addAttribute("members", mockMembers);
        verify(mockModel).addAttribute("accountID", accountId);
        assertEquals("members", viewName);
    }

    @Test
    public void addAccountMember() throws Exception {
        long accountId = 1L;
        String userName = "username";
        CashUserDTO mockLoggedUser = new CashUserDTO(1L, "username", "password");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(mockLoggedUser.getName());
        when(cashUserService.getByName(mockLoggedUser.getName())).thenReturn(mockLoggedUser);

        String redirectUrl = accountsController.AddAccountMember(mockModel, userName, accountId);

        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verify(cashUserService).getByName(mockLoggedUser.getName());
        verify(accountsService).addAccountMember(accountId, mockLoggedUser.getId(), userName);
        assertEquals("redirect:/accounts/members/" + accountId, redirectUrl);
    }

    @Test
    public void removeAccountMember() throws Exception {
        long accountId = 1L;
        long memberID = 1L;
        CashUserDTO mockLoggedUser = new CashUserDTO(1L, "username", "password");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(mockLoggedUser.getName());
        when(cashUserService.getByName(mockLoggedUser.getName())).thenReturn(mockLoggedUser);

        String redirectUrl = accountsController.RemoveAccountMember(accountId, memberID);

        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verify(cashUserService).getByName(mockLoggedUser.getName());
        verify(accountsService).removeAccountMember(accountId, mockLoggedUser.getId(), memberID);
        assertEquals("redirect:/accounts/members/" + accountId, redirectUrl);
    }
}