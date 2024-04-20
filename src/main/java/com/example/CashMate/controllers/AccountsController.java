package com.example.CashMate.controllers;

import com.example.CashMate.data.Account;
import com.example.CashMate.services.AccountsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
public class AccountsController {

    AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping("/accounts/create")
    public ResponseEntity<String> CreateAccount(@RequestBody Account account) {
        try {
            return ResponseEntity.ok(accountsService.CreateAccount(account));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accounts/add_member")
    public ResponseEntity<String> AddAccountMember(@RequestBody long accountID, long ownerID, long userID) {
        try {
            return ResponseEntity.ok(accountsService.AddAccountMember(accountID, ownerID, userID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accounts/remove_member")
    public ResponseEntity<String> RemoveAccountMember(@RequestBody long accountID, long ownerID, long userID) {
        try {
            return ResponseEntity.ok(accountsService.RemoveAccountMember(accountID, ownerID, userID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accounts/remove")
    public ResponseEntity<String> RemoveAccount(@RequestBody long accountID) {
        try {
            return ResponseEntity.ok(accountsService.RemoveAccount(accountID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/accounts/update")
    public ResponseEntity<String> UpdateAccount(@RequestBody Account account) {
        try {
            return ResponseEntity.ok(accountsService.UpdateAccount(account.getId(), account.getName(), account.getUser_id()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/accounts/account")
    public ResponseEntity<Account> GetAccount(@RequestHeader long accountID, long userID) {
        try {
            return ResponseEntity.ok(accountsService.GetAccount(accountID, userID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/accounts/user_accounts")
    public ResponseEntity<Set<Account>> GetAllAccountsByUser(@RequestHeader long userID) {
        try {
            return ResponseEntity.ok(accountsService.GetAllAccountsByUser(userID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


}
