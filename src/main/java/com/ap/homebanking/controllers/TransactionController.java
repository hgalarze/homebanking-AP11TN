package com.ap.homebanking.controllers;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/api/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> create(Authentication authentication,
                                         @RequestParam double amount,
                                         @RequestParam String description,
                                         @RequestParam String fromAccountNumber,
                                         @RequestParam String toAccountNumber
    ) {
        if (amount <= 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Source and target accounts cannot be the same", HttpStatus.FORBIDDEN);
        }

        Account sourceAccount = accountRepository.findByNumber(fromAccountNumber).orElse(null);
        Account targetAccount = accountRepository.findByNumber(toAccountNumber).orElse(null);

        if (sourceAccount == null || !sourceAccount.getOwner().getEmail().equals(authentication.getName())) {
            return new ResponseEntity<>("The source account not exists or you're not the owner", HttpStatus.FORBIDDEN);
        }

        if (sourceAccount.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient credit", HttpStatus.FORBIDDEN);
        }

        if (targetAccount == null) {
            return new ResponseEntity<>("The target account not exists", HttpStatus.FORBIDDEN);
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, amount * -1, LocalDate.now(), description + " - " + sourceAccount.getNumber());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, LocalDate.now(), description + " - " + targetAccount.getNumber());

        sourceAccount.addTransaction(debitTransaction);
        targetAccount.addTransaction(creditTransaction);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
