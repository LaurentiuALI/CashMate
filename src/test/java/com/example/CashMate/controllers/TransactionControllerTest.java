package com.example.CashMate.controllers;

import com.example.CashMate.data.Account;
import com.example.CashMate.data.Category;
import com.example.CashMate.data.Transaction;
import com.example.CashMate.data.Type;
import com.example.CashMate.data.security.CashUser;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.dtos.CategoryDTO;
import com.example.CashMate.dtos.TransactionDTO;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import com.example.CashMate.services.CategoryService;
import com.example.CashMate.services.TransactionsService;
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
import org.springframework.validation.BindingResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private CashUserService cashUserService;
    @Mock
    private AccountsService accountsService;
    @Mock
    private TransactionsService transactionService;
    @Mock
    private CategoryService categoryService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Model mockModel;

    private Authentication authentication;
    private SecurityContext securityContext;

    @InjectMocks
    private TransactionController transactionController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void accountList() throws Exception {
        CashUserDTO loggedUser = new CashUserDTO(1L, "username", "password");
        List<AccountDTO> allAccounts = new ArrayList<>() {{
            add(new AccountDTO());
            add(new AccountDTO());
        }};
        Account account = new Account();
        Transaction transaction = new Transaction(1L, account, "transactionName", "description", 100.0, Type.EXPENSE, new Date());
        Transaction transaction2 = new Transaction(2L, account, "transactionName2", "description", 100.0, Type.EXPENSE, new Date());
        List<Transaction> transactionsList = new ArrayList<>() {{
            add(transaction);
            add(transaction2);
        }};
        Page<Transaction> transactions = new PageImpl<>(transactionsList);
        List<Category> categories = new ArrayList<>() {{
            add(new Category(1L, "categoryName", "description"));
        }};
        List<Category> categories2 = new ArrayList<>() {{
            add(new Category(2L, "categoryName2", "description"));
        }};
        Map<Long, List<Category>> categoriesMap = new HashMap<>(){{
            put(1L, categories);
            put(2L, categories2);
        }};
        List<CategoryDTO> categoriesDTO = new ArrayList<>() {{
            add(new CategoryDTO(1L, "categoryName", "description"));
        }};
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(loggedUser.getName());
        when(cashUserService.getByName(loggedUser.getName())).thenReturn(loggedUser);
        when(accountsService.getAllAccountsOwnedAndParticipantByUser(1L)).thenReturn(allAccounts);
        when(transactionService.findAllTransactions(0, 3, 1)).thenReturn(transactions);
        when(transactionService.getCategoriesByTransactionId(1L)).thenReturn(categories);
        when(transactionService.getCategoriesByTransactionId(2L)).thenReturn(categories2);
        when(categoryService.findAll()).thenReturn(categoriesDTO);


        String viewName = transactionController.accountList(mockModel, 1L, 0,3);

        assertEquals("transactionList", viewName);
        verify(cashUserService).getByName(loggedUser.getName());
        verify(accountsService).getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());
        verify(transactionService).findAllTransactions(0, 3, 1);
        verify(transactionService).getCategoriesByTransactionId(1L);
        verify(transactionService).getCategoriesByTransactionId(2L);
        verify(mockModel).addAttribute("accounts", allAccounts);
        verify(mockModel).addAttribute("transactions", transactions);
        verify(mockModel).addAttribute("accountId", 1L);
        verify(mockModel).addAttribute("categories", categoriesDTO);
        verify(mockModel).addAttribute("categoriesMap", categoriesMap);
    }

    @Test
    public void addTransaction() throws Exception {
        CashUserDTO loggedUser = new CashUserDTO(1L, "username", "password");
        List<AccountDTO> allAccounts = new ArrayList<>() {{
            add(new AccountDTO());
            add(new AccountDTO());
        }};

        List<Transaction> transactionsList = new ArrayList<>() {{
            add(new Transaction(1L, new Account(), "transactionName", "description", 100.0, Type.EXPENSE, new Date()));
            add(new Transaction(2L, new Account(), "transactionName2", "description", 100.0, Type.EXPENSE, new Date()));
        }};

        Page<Transaction> transactions = new PageImpl<>(transactionsList);

        List<Long> categories = new ArrayList<>() {{
            add(1L);
        }};

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccount_id(1L);
        transactionDTO.setName("transactionName");
        transactionDTO.setDescription("description");
        transactionDTO.setAmount(100.0);
        transactionDTO.setType(Type.EXPENSE);
        transactionDTO.setDate(new Date());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(loggedUser.getName());
        when(cashUserService.getByName(loggedUser.getName())).thenReturn(loggedUser);
        when(accountsService.getAllAccountsOwnedAndParticipantByUser(1L)).thenReturn(allAccounts);
        when(transactionService.findAllTransactions(0, 3, 1)).thenReturn(transactions);
        when(accountsService.getById(1L)).thenReturn(new AccountDTO());


        String viewName = transactionController.addTransaction(transactionDTO, bindingResult, transactionDTO.getAccount_id(), categories, mockModel);

        assertEquals("redirect:/transactions?accountId=1&page=0&size=3", viewName);
        verify(cashUserService).getByName(loggedUser.getName());
        verify(accountsService).getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());
        verify(transactionService).findAllTransactions(0, 3, 1);
        verify(accountsService).getById(1L);
        verify(mockModel).addAttribute("accounts", allAccounts);
        verify(mockModel).addAttribute("accountId", 1L);
        verify(mockModel).addAttribute("transactions", transactions);


    }

    @Test
    public void removeAccount() throws Exception {
        long accountId = 1L;
        Account account = new Account(1L, "accountName", 1L , new HashSet<Transaction>());
        Optional<Transaction> transactionOptional = Optional.of(new Transaction(1L, account, "transactionName", "description", 100.0, Type.EXPENSE, new Date()));

        when(transactionService.getTransactionsByID(1L)).thenReturn(transactionOptional);

        String redirectUrl = transactionController.RemoveAccount(accountId);

        assertEquals("redirect:/transactions?accountId=1&page=0&size=3", redirectUrl);
    }
}
