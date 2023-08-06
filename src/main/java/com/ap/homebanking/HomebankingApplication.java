package com.ap.homebanking;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }


    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository,
                                      AccountRepository accountRepository,
                                      TransactionRepository transactionRepository) {
        return (args) -> {

            // Clients
            Client melbaClient = new Client("Melba", "Morel", "melba@mindhub.com");
            Client hectorClient = new Client("Hector", "Galarze", "hectorgalarze@gmail.com");

            // Accounts
            Account vin001Account = new Account("VIN001", LocalDate.now(), 5000);
            Account vin002Account = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
            Account vin003Account = new Account("VIN003", LocalDate.now().plusDays(5), 10000);
            Account vin004Account = new Account("VIN004", LocalDate.now(), 8000);

            // Transactions
            Transaction transactionDebitVin001 = new Transaction(TransactionType.DEBIT, -500, LocalDate.now(), "Pago de expensas", vin001Account);
            Transaction transactionCreditVin002 = new Transaction(TransactionType.CREDIT, 500, LocalDate.now(), "Recepción de pago de expensas", vin002Account);

            // Relationship
            vin001Account.setOwner(melbaClient);
            vin002Account.setOwner(melbaClient);
            vin003Account.setOwner(hectorClient);
            vin004Account.setOwner(hectorClient);

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
        };
    }


}


