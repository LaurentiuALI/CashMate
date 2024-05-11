package com.example.CashMate.controllers;

import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.exceptions.AccountNotCreatedException;
import com.example.CashMate.exceptions.CashUserNotFoundException;
import com.example.CashMate.exceptions.ResourceNotFoundException;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/accounts")
@Slf4j
public class AccountsController {

    AccountsService accountsService;
    CashUserService cashUserService;
    ModelMapper modelMapper;

    public AccountsController(AccountsService accountsService, CashUserService cashUserService, ModelMapper modelMapper) {
        this.accountsService = accountsService;
        this.cashUserService = cashUserService;
        this.modelMapper = modelMapper;
        log.info("AccountsController instantiated.");
    }

    @RequestMapping({"/", ""})
    public String accountList(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        log.info("Entering accountList method");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("User '{}' accessed the account list page.", auth.getName());

        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());

        Page<AccountDTO> accounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId(), page, size);
        for (AccountDTO account : accounts) {
            account.setOwnerName(accountsService.getAccountOwner(account).getName());
        }
        model.addAttribute("accounts", accounts);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("account", new AccountDTO());

        log.info("Exiting accountList method");
        return "accountList";
    }

    @RequestMapping("/delete/{id}")
    public String RemoveAccount(@PathVariable long id) {
        log.info("Entering RemoveAccount method with id: {}", id);
        accountsService.removeAccount(id);
        log.info("Exiting RemoveAccount method");
        return "redirect:/accounts";
    }

    @RequestMapping("/edit/{id}")
    public String UpdateAccount(Model model, @PathVariable long id) {
        log.info("Entering UpdateAccount method with id: {}", id);
        AccountDTO account = accountsService.getById(id);
        model.addAttribute("account", account);
        log.info("Exiting UpdateAccount method");
        return "accountForm";
    }

    @RequestMapping("/add")
    public String CreateAccount(Model model) {
        log.info("Entering CreateAccount method");
        AccountDTO account = new AccountDTO();
        model.addAttribute("account", account);
        log.info("Exiting CreateAccount method");
        return "accountAdd";
    }

    @PostMapping({"/", ""})
    public String saveOrUpdate(@ModelAttribute AccountDTO account) {
        log.info("Entering saveOrUpdate method");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());

        account.setUser_id(loggedUser.getId());

        if (account.getId() != null) {
            accountsService.updateAccount(account);
        } else {
            accountsService.createAccount(account);
        }
        log.info("Exiting saveOrUpdate method");
        return "redirect:/accounts";
    }

    @RequestMapping("/members/{accountID}")
    public String GetAccountMembers(Model model, @PathVariable long accountID) {
        log.info("Entering GetAccountMembers method with accountID: {}", accountID);
        List<CashUserDTO> members = accountsService.getAccountMembers(accountID);
        model.addAttribute("members", members);
        model.addAttribute("accountID", accountID);
        log.info("Exiting GetAccountMembers method");
        return "members";
    }

    @PostMapping("/members/addMember/{accountID}")
    public String AddAccountMember(Model model, @RequestParam("userName") String userName, @PathVariable long accountID) {
        log.info("Entering AddAccountMember method with userName: {} and accountID: {}", userName, accountID);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());
        try {
            accountsService.addAccountMember(accountID, loggedUser.getId(), userName);
        } catch (CashUserNotFoundException ex) {
            log.error("Error occurred while adding account member: {}", ex.getMessage());
            List<CashUserDTO> members = accountsService.getAccountMembers(accountID);
            model.addAttribute("members", members);
            model.addAttribute("accountID", accountID);
            model.addAttribute("error", ex.getMessage());
            return "members";
        }
        log.info("Exiting AddAccountMember method");
        return "redirect:/accounts/members/" + accountID;
    }

    @RequestMapping("/members/deleteMember/{accountID}/{memberID}")
    public String RemoveAccountMember(@PathVariable long accountID, @PathVariable long memberID) {
        log.info("Entering RemoveAccountMember method with accountID: {} and memberID: {}", accountID, memberID);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());
        accountsService.removeAccountMember(accountID, loggedUser.getId(), memberID);
        log.info("Exiting RemoveAccountMember method");
        return "redirect:/accounts/members/" + accountID;
    }

    @ExceptionHandler({ResourceNotFoundException.class, AccountNotCreatedException.class})
    public ModelAndView handlerNotFoundException(Exception exception) {
        log.error("Error occurred: {}", exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("exception", exception);
        modelAndView.setViewName("notFoundException");
        return modelAndView;
    }
}
