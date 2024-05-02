package com.example.CashMate.controllers;

import com.example.CashMate.data.Category;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.dtos.CategoryDTO;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import com.example.CashMate.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {

    CashUserService cashUserService;
    AccountsService accountsService;
    CategoryService categoryService;

    @Autowired
    public MainController(CashUserService cashUserService,
                          AccountsService accountsService,
                          CategoryService categoryService) {
        this.cashUserService = cashUserService;
        this.accountsService = accountsService;
        this.categoryService = categoryService;
    }

    @RequestMapping({"","/"})
    public String getMain(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "main";
    }

    @RequestMapping("/home")
    public String getHome(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());

        List<AccountDTO> allAccounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId());
        model.addAttribute("accounts", allAccounts);
        model.addAttribute("user", loggedUser);

        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

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