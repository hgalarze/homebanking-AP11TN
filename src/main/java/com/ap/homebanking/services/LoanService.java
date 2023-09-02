package com.ap.homebanking.services;

import com.ap.homebanking.dtos.LoanApplicationDTO;
import com.ap.homebanking.dtos.LoanDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface LoanService {

    List<LoanDTO> getLoans();
    void create(Authentication authentication, LoanApplicationDTO loanApplication) throws Exception;
}
