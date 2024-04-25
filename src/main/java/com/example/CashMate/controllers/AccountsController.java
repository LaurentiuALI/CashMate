package com.example.CashMate.controllers;

import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.services.AccountsService;
import com.example.CashMate.services.CashUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/accounts")
@Slf4j
public class AccountsController {

    AccountsService accountsService;
    CashUserService cashUserService;

    public AccountsController(AccountsService accountsService, CashUserService cashUserService) {

        this.accountsService = accountsService;
        this.cashUserService = cashUserService;
    }

    @RequestMapping({"/", ""})
    public String accountList(Model model){

        Set<AccountDTO> accounts = accountsService.GetAll();
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
        return "AccountForm";
    }

    @PostMapping({"/", ""})
    public String saveOrUpdate(@ModelAttribute AccountDTO account) {
        System.out.println("account id: " + account.getId());
        System.out.println("account name: " + account.getName());
        System.out.println("account user: " + account.getUser_id());
      accountsService.save(account);
      return "redirect:/accounts";
    }

    @PostMapping("/add_member")
    public ResponseEntity<String> AddAccountMember(@RequestBody long accountID, long ownerID, long userID) {
        try {
            accountsService.AddAccountMember(accountID, ownerID, userID);
            return ResponseEntity.ok("User added to account successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
