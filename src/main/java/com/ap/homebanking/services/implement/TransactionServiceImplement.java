package com.ap.homebanking.services.implement;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import com.ap.homebanking.repositories.TransactionRepository;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.naming.LimitExceededException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;


    @Override
    public void create(Authentication authentication, double amount, String description, String fromAccountNumber, String toAccountNumber) throws Exception {

        if (amount <= 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            throw new IllegalArgumentException("Missing data");
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new Exception("Source and target accounts cannot be the same");
        }

        Account sourceAccount = accountService.findByNumber(fromAccountNumber);
        Account targetAccount = accountService.findByNumber(toAccountNumber);

        if (sourceAccount == null || !sourceAccount.getOwner().getEmail().equals(authentication.getName())) {
            throw new Exception("The source account not exists or you're not the owner");
        }

        if (sourceAccount.getBalance() < amount) {
            throw new LimitExceededException("Insufficient credit");
        }

        if (targetAccount == null) {
            throw new EntityNotFoundException("The target account not exists");
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, amount * -1, LocalDate.now(), description + " - " + sourceAccount.getNumber());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, LocalDate.now(), description + " - " + targetAccount.getNumber());

        sourceAccount.addTransaction(debitTransaction);
        targetAccount.addTransaction(creditTransaction);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
    }
}
