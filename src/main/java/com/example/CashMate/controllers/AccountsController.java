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

    }

    @RequestMapping({"/", ""})
    public String accountList(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());

        Page<AccountDTO> accounts = accountsService.getAllAccountsOwnedAndParticipantByUser(loggedUser.getId(), page, size);
        for(AccountDTO account: accounts){
            account.setOwnerName(accountsService.getAccountOwner(account).getName());
        }
        model.addAttribute("accounts", accounts);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("account", new AccountDTO());


        return "accountList";
    }

    @RequestMapping("/delete/{id}")
    public String RemoveAccount(@PathVariable long id) {
        accountsService.removeAccount(id);
        return "redirect:/accounts";
    }

    @RequestMapping("/edit/{id}")
    public String UpdateAccount(Model model, @PathVariable long id) {
        AccountDTO account = accountsService.getById(id);
        model.addAttribute("account", account);
        return "accountForm";
    }

    @RequestMapping("/add")
    public String CreateAccount(Model model) {
        AccountDTO account = new AccountDTO();
        model.addAttribute("account", account);
        return "accountAdd";
    }

    @PostMapping({"/", ""})
    public String saveOrUpdate(@ModelAttribute AccountDTO account) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());

        account.setUser_id(loggedUser.getId());

        if(account.getId() != null) {
            accountsService.updateAccount(account);
        }else
            accountsService.createAccount(account);
        return "redirect:/accounts";
    }

    @RequestMapping("/members/{accountID}")
    public String GetAccountMembers(Model model, @PathVariable long accountID) {
        List<CashUserDTO> members = accountsService.getAccountMembers(accountID);
        model.addAttribute("members", members);
        model.addAttribute("accountID", accountID);
        return "members";
    }

    @PostMapping("/members/addMember/{accountID}")
    public String AddAccountMember(Model model, @RequestParam("userName") String userName, @PathVariable long accountID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());
        try{
            accountsService.addAccountMember(accountID, loggedUser.getId(), userName);
        } catch (CashUserNotFoundException ex){
            List<CashUserDTO> members = accountsService.getAccountMembers(accountID);
            model.addAttribute("members", members);
            model.addAttribute("accountID", accountID);
            model.addAttribute("error", ex.getMessage());
            return "members";
        }
        return "redirect:/accounts/members/" + accountID;
    }

    @RequestMapping("/members/deleteMember/{accountID}/{memberID}")
    public String RemoveAccountMember(@PathVariable long accountID, @PathVariable long memberID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());
        accountsService.removeAccountMember(accountID, loggedUser.getId(), memberID);
        return "redirect:/accounts/members/" + accountID;
    }

    @ExceptionHandler({ResourceNotFoundException.class, AccountNotCreatedException.class})
    public ModelAndView handlerNotFoundException(Exception exception){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("exception", exception);
        modelAndView.setViewName("notFoundException");
        return modelAndView;
    }

}
