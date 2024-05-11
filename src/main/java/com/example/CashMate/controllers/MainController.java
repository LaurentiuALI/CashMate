package com.example.CashMate.controllers;

import com.example.CashMate.data.Category;
import com.example.CashMate.data.Transaction;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.dtos.CategoryDTO;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import com.example.CashMate.services.CategoryService;
import com.example.CashMate.services.TransactionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final CashUserService cashUserService;
    private final AccountsService accountsService;
    private final CategoryService categoryService;
    private final TransactionsService transactionsService;

    @Autowired
    public MainController(CashUserService cashUserService,
                          AccountsService accountsService,
                          CategoryService categoryService,
                          TransactionsService transactionsService) {
        this.cashUserService = cashUserService;
        this.accountsService = accountsService;
        this.categoryService = categoryService;
        this.transactionsService = transactionsService;
        log.info("MainController instantiated.");
    }

    @RequestMapping({"","/"})
    public String getMain(Authentication authentication){
        log.info("Entering getMain method");
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("User authenticated, redirecting to home page.");
            return "redirect:/home";
        }
        log.info("User not authenticated, displaying main page.");
        return "main";
    }

    @RequestMapping("/home")
    public String getHome(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size){
        log.info("Entering getHome method");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());

        log.info("Fetching user accounts...");
        List<AccountDTO> allAccounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());
        for(AccountDTO account: allAccounts){
            account.setOwnerName(accountsService.getAccountOwner(account).getName());
        }
        model.addAttribute("accounts", allAccounts);
        model.addAttribute("user", loggedUser);

        log.info("Fetching categories...");
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        log.info("Fetching transactions...");
        Page<Transaction> transactions = transactionsService.findAllTransactionSorted(page, size, allAccounts, "date",  "desc");
        Map<Long, List<Category>> categoriesMap = new HashMap<>();
        Map<Long, String> transactionAccountName = new HashMap<>();
        for (Transaction transaction : transactions) {
            List<Category> mapCategories = transactionsService.getCategoriesByTransactionId(transaction.getId());
            categoriesMap.put(transaction.getId(), mapCategories);

            String accountName = accountsService.getById(transaction.getAccount().getId()).getName();
            transactionAccountName.put(transaction.getId(), accountName);
        }

        model.addAttribute("categoriesMap", categoriesMap);
        model.addAttribute("accountNameMap", transactionAccountName);
        model.addAttribute("transactions", transactions);

        log.info("Exiting getHome method");
        return "home";
    }

    @RequestMapping("/login")
    public String showLogInForm(Authentication authentication){
        log.info("Entering showLogInForm method");
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("User authenticated, redirecting to home page.");
            return "redirect:/home";
        }
        log.info("User not authenticated, displaying login form.");
        return "login";
    }

    @RequestMapping("/register")
    public String showRegisterForm(Model model, Authentication authentication){
        log.info("Entering showRegisterForm method");
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("User authenticated, redirecting to home page.");
            return "redirect:/home";
        }

        log.info("User not authenticated, displaying registration form.");
        CashUserDTO cashUser = new CashUserDTO();
        model.addAttribute("user", cashUser);

        return "register";
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(){
        log.info("Entering accessDeniedPage method");
        return "accessDenied";
    }
}
