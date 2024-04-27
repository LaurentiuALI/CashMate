package com.example.CashMate.controllers;

import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.exceptions.ResourceAlreadyExistsException;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
@Slf4j
public class CashUserController {

    AccountsService accountsService;
    CashUserService cashUserService;

    ModelMapper modelMapper;

    public CashUserController(AccountsService accountsService, CashUserService cashUserService, ModelMapper modelMapper) {

        this.accountsService = accountsService;
        this.cashUserService = cashUserService;
        this.modelMapper = modelMapper;

    }

    @PostMapping({"/", ""})
    public String register(@ModelAttribute CashUserDTO user) {

        cashUserService.createAccount(user);

        return "main";
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ModelAndView handleCashUserAlreadyExists(ResourceAlreadyExistsException exception) {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("error", exception.getMessage());
        modelAndView.addObject("user", new CashUserDTO()); // Add an empty user object if needed
        return modelAndView;
    }

}
