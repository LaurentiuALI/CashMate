package com.example.CashMate.controllers;


import com.example.CashMate.data.Transaction;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import com.example.CashMate.services.TransactionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String accountList(Model model){


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());


        List<AccountDTO> allAccounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());

        model.addAttribute("accounts", allAccounts);

        return "transactionList";
    }

    @PostMapping({"/", ""})
    public String getTransactionsByAccountName(@RequestParam("accountId") Long accountId, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());
        List<AccountDTO> allAccounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());

        model.addAttribute("accounts", allAccounts);

        Set<Transaction> transactions = transactionsService.getTransactionsByAccountID(accountId);
        model.addAttribute("transactions", transactions);

        transactions.forEach(transaction -> {
            System.out.println(transaction.getType());
        });
        return "transactionList";
    }


}
