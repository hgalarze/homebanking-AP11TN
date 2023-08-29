package com.ap.homebanking.services.implement;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.naming.LimitExceededException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    private static final int MAX_ACCOUNT_BY_CLIENT = 3;

    @Override
    public List<AccountDTO> getAccountsDTO() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public List<AccountDTO> getAccountsDTO(Client client) {
        return client.getAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public AccountDTO getAccountDTO(Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @Override
    public void createAccount(Authentication authentication) throws Exception {

        Client currentClient = clientRepository.findByEmail(authentication.getName()).orElse(null);

        if (currentClient == null) {
            throw new EntityNotFoundException("Client not exists");
        }

        if (currentClient.getAccounts().size() >= MAX_ACCOUNT_BY_CLIENT) {
            throw new LimitExceededException("The user has reached the maximum number of accounts");
        }

        Account newAccount = new Account(this.getNewAccountIdentifier(), LocalDate.now(), 0);
        currentClient.addAccount(newAccount);
        this.SaveAccount(newAccount);
    }

    @Override
    public List<AccountDTO> getCurrentUserAccountsDTO(Authentication authentication) {
        Client currentClient = clientRepository.findByEmail(authentication.getName()).orElse(null);

        if (currentClient != null) {
            return this.getAccountsDTO(currentClient);
        }

        return null;
    }

    @Override
    public void SaveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account findByNumber(String accountNumber) { return accountRepository.findByNumber(accountNumber).orElse(null); }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    private String getNewAccountIdentifier() {
        Random random = new Random(System.currentTimeMillis());
        return "VIN-" + (10000000 + random.nextInt(90000000));
    }
}
