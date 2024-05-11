package com.example.CashMate.controllers;

import com.example.CashMate.data.Category;
import com.example.CashMate.data.Transaction;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.dtos.TransactionDTO;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import com.example.CashMate.services.CategoryService;
import com.example.CashMate.services.TransactionsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    CashUserService cashUserService;
    AccountsService accountsService;
    TransactionsService transactionsService;
    CategoryService categoryService;

    ModelMapper modelMapper;

    @Autowired
    public TransactionController(CashUserService cashUserService, AccountsService accountsService,
                                 TransactionsService transactionsService, CategoryService categoryService,
                                 ModelMapper modelMapper) {
        this.cashUserService = cashUserService;
        this.accountsService = accountsService;
        this.transactionsService = transactionsService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        log.info("TransactionController instantiated.");
    }

    @RequestMapping({"/", ""})
    public String accountList(Model model, @RequestParam(required = false, name = "accountId") Long accountId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "3") int size){
        log.info("Entering accountList method");

        // Get the authenticated user and its accounts for the dropdown
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());
        List<AccountDTO> allAccounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());
        for(AccountDTO account: allAccounts){
            account.setOwnerName(accountsService.getAccountOwner(account).getName());
        }
        model.addAttribute("accounts", allAccounts);

        // Get transactions for the accountId specified when selected from dropdown
        Map<Long, List<Category>> categoriesMap = new HashMap<>();
        if(accountId != null) {
            Page<Transaction> transactions = transactionsService.findAllTransactions(page, size, accountId);
            model.addAttribute("transactions", transactions);
            model.addAttribute("accountId", accountId);

            for (Transaction transaction : transactions) {
                List<Category> categories = transactionsService.getCategoriesByTransactionId(transaction.getId());
                categoriesMap.put(transaction.getId(), categories);
            }
        }

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("categoriesMap", categoriesMap);

        log.info("Exiting accountList method");
        return "transactionList";
    }

    @PostMapping({"/add", "/add/"})
    public String addTransaction(@Valid @ModelAttribute("transaction") TransactionDTO transaction,
                                 BindingResult bindingResult,
                                 @RequestParam("account") Long accountId,
                                 @RequestParam(value = "categories", required = false) List<Long> selectedCategories,
                                 Model model) {
        log.info("Entering addTransaction method");

        if(selectedCategories.size() > 3){
            bindingResult.addError(new ObjectError("Selected Categories", "Transactions must contain maximum 3 categories"));
        }
        if(bindingResult.hasErrors()){
            model.addAttribute("bindingResult", bindingResult);
            log.error("Validation errors occurred: {}", bindingResult.getAllErrors());
            return "transactionError";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());
        List<AccountDTO> allAccounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());
        model.addAttribute("accounts", allAccounts);
        model.addAttribute("accountId", accountId);

        Page<Transaction> transactions = transactionsService.findAllTransactions(0, 3,  accountId);
        model.addAttribute("transactions", transactions);


        TransactionDTO transactionPass = new TransactionDTO();
        AccountDTO usedAccount = accountsService.getById(accountId);
        transactionPass.setAccount_id(usedAccount.getId());
        transactionPass.setName(transaction.getName());
        transactionPass.setDescription(transaction.getDescription());
        transactionPass.setAmount(transaction.getAmount());
        transactionPass.setType(transaction.getType());
        transactionPass.setDate(transaction.getDate());

        try {
            TransactionDTO transactionDTO = transactionsService.createTransaction(transactionPass);
            transactionsService.addCategoriesToTransaction(transactionDTO.getId(), selectedCategories);
        } catch (Exception e) {
            log.error("An error occurred while adding transaction: {}", e.getMessage());
            e.printStackTrace();
        }

        log.info("Exiting addTransaction method");
        return "redirect:/transactions?accountId=" + accountId + "&page=0&size=3";
    }

    @RequestMapping("/delete/{id}")
    public String RemoveAccount(@PathVariable long id) {
        log.info("Entering RemoveAccount method");
        Optional<Transaction> transactionOptional = transactionsService.getTransactionsByID(id);

        transactionsService.removeTransaction(id);
        long accountId = transactionOptional.get().getAccount().getId();

        log.info("Exiting RemoveAccount method");
        return "redirect:/transactions?accountId=" + accountId + "&page=0&size=3";
    }
}
