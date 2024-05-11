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

    CashUserService cashUserService;
    AccountsService accountsService;
    CategoryService categoryService;
    TransactionsService transactionsService;

    @Autowired
    public MainController(CashUserService cashUserService,
                          AccountsService accountsService,
                          CategoryService categoryService,
                          TransactionsService transactionsService) {
        this.cashUserService = cashUserService;
        this.accountsService = accountsService;
        this.categoryService = categoryService;
        this.transactionsService = transactionsService;
    }

    @RequestMapping({"","/"})
    public String getMain(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "main";
    }

    @RequestMapping("/home")
    public String getHome(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());

        List<AccountDTO> allAccounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());
        model.addAttribute("accounts", allAccounts);
        model.addAttribute("user", loggedUser);

        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        Page<Transaction> transactions = transactionsService.findAllTransactionSorted(page, size, allAccounts, "date", "desc");
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

        return "home";
    }

    @RequestMapping("/login")
    public String showLogInForm(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "login";
    }

    @RequestMapping("/register")
    public String showRegisterForm(Model model, Authentication authentication){

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }

        CashUserDTO cashUser = new CashUserDTO();
        model.addAttribute("user", cashUser);

        return "register";
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(){ return "accessDenied"; }

}