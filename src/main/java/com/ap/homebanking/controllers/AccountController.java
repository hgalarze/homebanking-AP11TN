package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccountsDTO();
    }

    @GetMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccountDTO(id);
    }

    @Transactional
    @PostMapping(value = "/api/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        try {
            accountService.createAccount(authentication);
            return new ResponseEntity<>("The account has been created", HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(value = "/api/clients/current/accounts")
    public List<AccountDTO> getCurrentUserAccounts(Authentication authentication) {
        return accountService.getCurrentUserAccountsDTO(authentication);
    }
}
