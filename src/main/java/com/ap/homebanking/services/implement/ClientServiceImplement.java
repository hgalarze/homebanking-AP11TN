package com.ap.homebanking.services.implement;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<ClientDTO> getClientsDTO() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @Override
    public ClientDTO getClientDTO(Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @Override
    public ClientDTO getCurrentClientDTO(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).map(ClientDTO::new).orElse(null);
    }

    @Override
    public void register(String firstName, String lastName, String email, String password) throws Exception {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Missing data");
        }

        if (this.findByEmail(email) != null) {
            throw new EntityExistsException("Email already in use");
        }

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account newAccount = new Account(getNewAccountIdentifier(), LocalDate.now(), 0);
        newClient.addAccount(newAccount);

        clientRepository.save(newClient);
        accountService.SaveAccount(newAccount);
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }

    private String getNewAccountIdentifier() {
        Random random = new Random(System.currentTimeMillis());
        return "VIN-" + random.nextInt(99999999);
    }
}
