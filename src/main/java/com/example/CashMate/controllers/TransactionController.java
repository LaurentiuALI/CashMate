package com.example.CashMate.controllers;


import com.example.CashMate.data.Transaction;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import com.example.CashMate.services.TransactionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    CashUserService cashUserService;
    AccountsService accountsService;
    TransactionsService transactionsService;

    @Autowired
    public TransactionController(CashUserService cashUserService, AccountsService accountsService,
                                 TransactionsService transactionsService){
        this.cashUserService = cashUserService;
        this.accountsService = accountsService;
        this.transactionsService = transactionsService;
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



        return "transactionList";
    }




}
