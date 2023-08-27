package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {

        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);

    }

    @RequestMapping(value = "/api/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        int MAX_ACCOUNT_BY_CLIENT = 3;
        Client currentClient = clientRepository.findByEmail(authentication.getName()).orElse(null);

        if (currentClient != null) {
            if (currentClient.getAccounts().size() < MAX_ACCOUNT_BY_CLIENT) {
                Account newAccount = new Account(getNewAccountIdentifier(), LocalDate.now(), 0);
                currentClient.addAccount(newAccount);
                accountRepository.save(newAccount);
                return new ResponseEntity<>("The account has been created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("The user has reached the maximum number of accounts", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/api/clients/current/accounts")
    public List<AccountDTO> getCurrentUserAccounts(Authentication authentication) {
        Client currentClient = clientRepository.findByEmail(authentication.getName()).orElse(null);

        if (currentClient != null) {
            return currentClient.getAccounts().stream().map(AccountDTO::new).collect(toList());
        }

        return null;
    }

    private String getNewAccountIdentifier() {
        Random random = new Random(System.currentTimeMillis());
        return "VIN-" + (10000000 + random.nextInt(90000000));
    }
}
