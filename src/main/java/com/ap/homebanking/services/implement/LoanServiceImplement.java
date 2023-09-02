package com.ap.homebanking.services.implement;

import com.ap.homebanking.dtos.LoanApplicationDTO;
import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.ClientLoanRepository;
import com.ap.homebanking.repositories.LoanRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
import com.ap.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.naming.LimitExceededException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServiceImplement implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;


    @Override
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }

    @Override
    public void create(Authentication authentication, LoanApplicationDTO loanApplication) throws Exception {

        if (loanApplication == null || loanApplication.getLoanId() == 0
                || loanApplication.getToAccountNumber().isEmpty() || loanApplication.getAmount() <= 0 || loanApplication.getPayments() <= 0) {
            throw new IllegalArgumentException("Missing data");
        }

        Loan loan = loanRepository.findById(loanApplication.getLoanId()).orElse(null);
        if (loan == null) {
            throw new EntityNotFoundException("The requested loan does not exist");
        }

        if (loanApplication.getAmount() > loan.getMaxAmount()) {
            throw new LimitExceededException("The amount cannot be greater than " + loan.getMaxAmount());
        }

        if (!loan.getPayments().contains(loanApplication.getPayments())) {
            throw new LimitExceededException("Invalid number of payments");
        }

        Account toAccount = accountService.findByNumber(loanApplication.getToAccountNumber());
        if (toAccount == null || !toAccount.getOwner().getEmail().equals(authentication.getName())) {
            throw new EntityNotFoundException("The destination account not exists or you're not the owner");
        }

        double amountWithTax = loanApplication.getAmount() * 1.2; // Loan amount + 20%
        String transactionDescription = String.format("'%s' loan approved", loan.getName());
        Client applicantClient = clientService.findByEmail(authentication.getName());

        ClientLoan requestedLoan = new ClientLoan(amountWithTax, loanApplication.getPayments(), applicantClient, loan);
        Transaction loanTransaction = new Transaction(TransactionType.CREDIT, loanApplication.getAmount(), LocalDate.now(), transactionDescription);

        applicantClient.addClientLoan(requestedLoan);
        toAccount.addTransaction(loanTransaction);

        clientLoanRepository.save(requestedLoan);
        transactionRepository.save(loanTransaction);


    }

}
