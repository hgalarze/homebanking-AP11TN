package com.ap.homebanking;

import com.ap.homebanking.models.Loan;
import com.ap.homebanking.repositories.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTests {

    @Autowired
    LoanRepository loanRepository;

    @Test
    public void existLoans(){

        // Act
        List<Loan> loans = loanRepository.findAll();

        // Assert
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){

        // Act
        List<Loan> loans = loanRepository.findAll();

        // Assert
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }
}
