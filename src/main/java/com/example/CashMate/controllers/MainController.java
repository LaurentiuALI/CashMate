package com.example.CashMate.controllers;

import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.services.CashUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    CashUserService cashUserService;

    @Autowired
    public MainController(CashUserService cashUserService) {
        this.cashUserService = cashUserService;
    }

    @RequestMapping({"","/"})
    public String getMain(Model model, Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "main";
    }

    @RequestMapping("/home")
    public String getHome(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());

        model.addAttribute("user", loggedUser);

        return "home";
    }

    @RequestMapping("/login")
    public String showLogInForm(Model model, Authentication authentication){
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