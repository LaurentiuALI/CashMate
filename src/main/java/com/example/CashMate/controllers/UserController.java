package com.example.CashMate.controllers;

import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    AccountsService accountsService;
    CashUserService cashUserService;

    ModelMapper modelMapper;

    public UserController(AccountsService accountsService, CashUserService cashUserService, ModelMapper modelMapper) {

        this.accountsService = accountsService;
        this.cashUserService = cashUserService;
        this.modelMapper = modelMapper;

    }

    @PostMapping({"/", ""})
    public String register(@ModelAttribute CashUserDTO user) {

        System.out.println("macar ajunge aici?");
        System.out.println(user.getName());
        System.out.println(user.getPassword());

        return "main";
    }
}
