package com.example.CashMate.controllers;

import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public String accountList(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());

        Set<AccountDTO> accounts = accountsService.GetAllAccountsByUser(loggedUser.getId());
        for(AccountDTO account: accounts){
            account.setOwnerName(accountsService.GetAccountOwnerName(account));
        }
        model.addAttribute("accounts", accounts);


        return "accountList";
    }

    @RequestMapping("/delete/{id}")
    public String RemoveAccount(@PathVariable long id) {
        accountsService.RemoveAccount(id);
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

        if(account.getId() != null)
            accountsService.updateAccount(account);
        else
            accountsService.createAccount(account);
        return "redirect:/accounts";
    }

    @RequestMapping("/members/{accountID}")
    public String GetAccountMembers(Model model, @PathVariable long accountID) {
        List<CashUserDTO> members = accountsService.GetAccountMembers(accountID);
        model.addAttribute("members", members);
        model.addAttribute("accountID", accountID);
        return "members";
    }


    @RequestMapping("/members/addMember/{accountID}")
    public String AddAccountMemberSelectionPanel(Model model, @PathVariable long accountID) {
        String userName = "";
        model.addAttribute("accountID", accountID);
        model.addAttribute("userName", userName);
        return "addMember";
    }

    @PostMapping("/members/addMember/{accountID}")
    public String AddAccountMember(@RequestParam("userName") String userName, @PathVariable long accountID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());
        CashUserDTO userToAdd = cashUserService.existsByName(userName);
        accountsService.AddAccountMember(accountID, loggedUser.getId(), userToAdd.getId());
        return "redirect:/accounts/members/" + accountID;
    }

    @RequestMapping("/members/deleteMember/{accountID}/{memberID}")
    public String RemoveAccountMember(@PathVariable long accountID, @PathVariable long memberID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CashUserDTO loggedUser = cashUserService.getByName(auth.getName());
        accountsService.RemoveAccountMember(accountID, loggedUser.getId(), memberID);
        return "redirect:/accounts/members/" + accountID;
    }

    @PostMapping("/remove_member")
    public ResponseEntity<String> RemoveAccountMember(@RequestBody long accountID, long ownerID, long userID) {
        try {
            accountsService.RemoveAccountMember(accountID, ownerID, userID);
            return ResponseEntity.ok("User removed from account successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/account")
    public ResponseEntity<AccountDTO> GetAccount(@RequestHeader long accountID, long userID) {
        try {
            return ResponseEntity.ok(accountsService.GetAccount(accountID, userID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/user_accounts")
    public ResponseEntity<Set<AccountDTO>> GetAllAccountsByUser(@RequestHeader long userID) {
        try {
            return ResponseEntity.ok(accountsService.GetAllAccountsByUser(userID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


}
