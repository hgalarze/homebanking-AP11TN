package com.ap.homebanking.services;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccountsDTO();

    List<AccountDTO> getAccountsDTO(Client client);

    AccountDTO getAccountDTO(Long id);

    void createAccount(Authentication authentication) throws Exception;

    List<AccountDTO> getCurrentUserAccountsDTO(Authentication authentication);

    void SaveAccount(Account account);

    Account findById(Long id);

    Account findByNumber(String accountNumber);

    List<Account> findAll();
}
