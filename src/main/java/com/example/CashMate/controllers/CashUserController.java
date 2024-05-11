package com.example.CashMate.controllers;

import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.exceptions.CashUserNotFoundException;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
@Slf4j
public class CashUserController {

    private final CashUserService cashUserService;

    public CashUserController(CashUserService cashUserService) {
        this.cashUserService = cashUserService;
        log.info("CashUserController instantiated.");
    }

    @PostMapping({"/", ""})
    public String register(@Valid @ModelAttribute("user") CashUserDTO user, BindingResult bindingResult, Model model) {
        log.info("Entering register method");
        if (bindingResult.hasErrors()) {
            log.error("Error(s) occurred during user registration: {}", bindingResult.getAllErrors());
            model.addAttribute("user", user);
            return "register";
        }
        cashUserService.createAccount(user);
        log.info("User registered successfully.");
        return "main";
    }

    @ExceptionHandler(CashUserNotFoundException.class)
    public ModelAndView handleCashUserAlreadyExists(CashUserNotFoundException exception) {
        log.error("Cash user not found: {}", exception.getMessage());
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("error", exception.getMessage());
        modelAndView.addObject("user", new CashUserDTO()); // Add an empty user object if needed
        return modelAndView;
    }
}
