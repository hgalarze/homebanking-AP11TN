package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.LoanApplicationDTO;
import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class LoanController {

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/api/loans", method = RequestMethod.GET)
    public List<LoanDTO> getLoans() {
        return loanService.getLoans();
    }

    @Transactional
    @RequestMapping(value = "/api/loans", method = RequestMethod.POST)
    public ResponseEntity<String> addLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplication) {

        try {
            loanService.create(authentication, loanApplication);
            return new ResponseEntity<>("The loan has been created", HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

}
