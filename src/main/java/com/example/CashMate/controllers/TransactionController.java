package com.example.CashMate.controllers;


import com.example.CashMate.data.Account;
import com.example.CashMate.data.Transaction;
import com.example.CashMate.data.Type;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.dtos.TransactionDTO;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import com.example.CashMate.services.CategoryService;
import com.example.CashMate.services.TransactionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    CashUserService cashUserService;
    AccountsService accountsService;
    TransactionsService transactionsService;
    CategoryService categoryService;

    @Autowired
    public TransactionController(CashUserService cashUserService, AccountsService accountsService,
                                 TransactionsService transactionsService, CategoryService categoryService) {
        this.cashUserService = cashUserService;
        this.accountsService = accountsService;
        this.transactionsService = transactionsService;
        this.categoryService = categoryService;
    }

    @RequestMapping({"/", ""})
    public String accountList(Model model, @RequestParam(required = false, name = "accountId") Long accountId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "3") int size){


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());

        List<AccountDTO> allAccounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());

        model.addAttribute("accounts", allAccounts);

        if(accountId != null) {
            Page<Transaction> transactions = transactionsService.findAllTransactions(page, size, accountId);
            model.addAttribute("transactions", transactions);
            model.addAttribute("accountId", accountId);
        }

        model.addAttribute("categories", categoryService.findAll());

        return "transactionList";
    }

    @PostMapping({"/", ""})
    public String getTransactionsByAccountName(@RequestParam("accountId") Long accountId, Model model,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "3") int size) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());
        List<AccountDTO> allAccounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());

        model.addAttribute("accounts", allAccounts);
        Page<Transaction> transactions = transactionsService.findAllTransactions(page, size, accountId);

        model.addAttribute("accountId", accountId);
//        Set<Transaction> transactions = transactionsService.getTransactionsByAccountID(accountId);
        model.addAttribute("transactions", transactions);


        model.addAttribute("categories", categoryService.findAll());

        return "transactionList";
    }

    @PostMapping({"/add", "/add/"})
    public String addTransaction(@RequestParam("name") String name,
                                 @RequestParam("description") String description,
                                 @RequestParam("amount") double amount,
                                 @RequestParam("type") Type type,
                                 @RequestParam("account") Long accountId,
                                 @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                 @RequestParam(value = "categories", required = false) List<Long> selectedCategories,
                                 Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());
        List<AccountDTO> allAccounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());
        model.addAttribute("accounts", allAccounts);
        model.addAttribute("accountId", accountId);

        Page<Transaction> transactions = transactionsService.findAllTransactions(0, 3, accountId);
        model.addAttribute("transactions", transactions);


        TransactionDTO transaction = new TransactionDTO();

        AccountDTO usedAccount = accountsService.getById(accountId);
        transaction.setAccount_id(usedAccount.getId());

        transaction.setName(name);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDate(date);

        selectedCategories.forEach(System.out::println);

        try {
            transactionsService.createTransaction(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/transactions?accountId=" + accountId + "&page=0&size=3";
    }

    @RequestMapping("/delete/{id}")
    public String RemoveAccount(@PathVariable long id) {
        Optional<Transaction> transactionOptional = transactionsService.getTransactionsByID(id);

        transactionsService.removeTransaction(id);
        long accountId = transactionOptional.get().getAccount().getId();

        return "redirect:/transactions?accountId="+accountId+"&page=0&size=3";
    }

}