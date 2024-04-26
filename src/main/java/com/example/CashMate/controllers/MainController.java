package com.example.CashMate.controllers;

import com.example.CashMate.dtos.CashUserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @RequestMapping({"","/","/home"})
    public String getHome(){
        return "main";
    }

    @RequestMapping("/login")
    public String showLogInForm(){ return "login"; }

    @RequestMapping("/register")
    public String showRegisterForm(Model model){
        CashUserDTO cashUser = new CashUserDTO();
        cashUser.setName("burger");
        model.addAttribute("user", cashUser);

        return "register";
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(){ return "accessDenied"; }

}