package com.ap.homebanking.services;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

public interface TransactionService {

    void create(Authentication authentication, double amount, String description, String fromAccountNumber, String toAccountNumber) throws Exception;
}
