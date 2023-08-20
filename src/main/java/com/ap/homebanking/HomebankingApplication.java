package com.ap.homebanking;

import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }


    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository,
                                      AccountRepository accountRepository,
                                      TransactionRepository transactionRepository,
                                      LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository,
                                      CardRepository cardRepository) {
        return (args) -> {

            // Admin
            Client adminClient = new Client("admin", "admin", "admin@mindhub.com", passwordEncoder.encode("admin#001"));

            // Clients
            Client melbaClient = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba#001"));
            Client hectorClient = new Client("Hector", "Galarze", "hectorgalarze@gmail.com", passwordEncoder.encode("hector#001"));

            // Accounts
            Account vin001Account = new Account("VIN001", LocalDate.now(), 5000);
            Account vin002Account = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
            Account vin003Account = new Account("VIN003", LocalDate.now().plusDays(5), 10000);
            Account vin004Account = new Account("VIN004", LocalDate.now(), 8000);

            // Transactions
            Transaction transactionDebitVin001 = new Transaction(TransactionType.DEBIT, -500, LocalDate.now(), "Pago de expensas");
            Transaction transactionCreditVin002 = new Transaction(TransactionType.CREDIT, 500, LocalDate.now(), "Recepción de pago de expensas");

            // Loans
            Loan mortgageLoan001 = new Loan("Hipotecario", 500000d, List.of(12,24,36,48,60));
            Loan personalLoan002 = new Loan("Personal", 100000d, List.of(6,12,24));
            Loan autoLoan003 = new Loan("Automotriz", 300000d, List.of(6,12,24,36));

            // ClientLoan
            ClientLoan clientLoan001 = new ClientLoan(400000d, 60, melbaClient, mortgageLoan001);
            ClientLoan clientLoan002 = new ClientLoan(50000d, 12, melbaClient, personalLoan002);
            ClientLoan clientLoan003 = new ClientLoan(100000d, 24, hectorClient, personalLoan002);
            ClientLoan clientLoan004 = new ClientLoan(200000d, 36, hectorClient, autoLoan003);

            // Cards
            Card goldCard001 = new Card(melbaClient.getFirstName() + " " + melbaClient.getLastName(),
                    CardType.DEBIT,
                    CardColor.GOLD,
                    "2860 1764 3015 6983",
                    795,
                    LocalDate.now(),
                    LocalDate.now().plusYears(5));

            Card titaniumCredit001 = new Card(melbaClient.getFirstName() + " " + melbaClient.getLastName(),
                    CardType.CREDIT,
                    CardColor.TITANIUM,
                    "4680 5921 7653 6645",
                    655,
                    LocalDate.now(),
                    LocalDate.now().plusYears(5));

            Card silverCredit001 = new Card(hectorClient.getFirstName() + " " + hectorClient.getLastName(),
                    CardType.CREDIT,
                    CardColor.SILVER,
                    "6504 7832 4462 8511",
                    468,
                    LocalDate.now(),
                    LocalDate.now().plusYears(5));

            // Relationship Account & Transaction
            vin001Account.addTransaction(transactionDebitVin001);
            vin002Account.addTransaction(transactionCreditVin002);

            // Relationship Client & Loan
            melbaClient.addClientLoan(clientLoan001);
            melbaClient.addClientLoan(clientLoan002);
            hectorClient.addClientLoan(clientLoan003);
            hectorClient.addClientLoan(clientLoan004);

            // Relationship Client & Account
            melbaClient.addAccount(vin001Account);
            melbaClient.addAccount(vin002Account);
            hectorClient.addAccount(vin003Account);
            hectorClient.addAccount(vin004Account);

            // Relationship Client & Card
            melbaClient.addCard(goldCard001);
            melbaClient.addCard(titaniumCredit001);
            hectorClient.addCard(silverCredit001);

            // Save a couple of customers
            clientRepository.save(melbaClient);
            clientRepository.save(hectorClient);

            // Save some accounts
            accountRepository.save(vin001Account);
            accountRepository.save(vin002Account);
            accountRepository.save(vin003Account);
            accountRepository.save(vin004Account);

            // Save some transactions
            transactionRepository.save(transactionDebitVin001);
            transactionRepository.save(transactionCreditVin002);

            // Save some loans
            loanRepository.save(mortgageLoan001);
            loanRepository.save(personalLoan002);
            loanRepository.save(autoLoan003);

            // Save some clientloan
            clientLoanRepository.save(clientLoan001);
            clientLoanRepository.save(clientLoan002);
            clientLoanRepository.save(clientLoan003);
            clientLoanRepository.save(clientLoan004);

            // Save some Cards
            cardRepository.save(goldCard001);
            cardRepository.save(titaniumCredit001);
            cardRepository.save(silverCredit001);
        };
    }


}


