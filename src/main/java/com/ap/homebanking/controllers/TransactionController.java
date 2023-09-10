package com.ap.homebanking.controllers;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import com.ap.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping(path = "/api/transactions")
    public ResponseEntity<Object> create(Authentication authentication,
                                         @RequestParam double amount,
                                         @RequestParam String description,
                                         @RequestParam String fromAccountNumber,
                                         @RequestParam String toAccountNumber
    ) {
        try {
            transactionService.create(authentication, amount, description, fromAccountNumber, toAccountNumber);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
