package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Transactional
    @RequestMapping(path = "/api/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        try {
            clientService.register(firstName, lastName, email, password);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClientsDTO();
    }

    @RequestMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.getClientDTO(id);
    }

    @RequestMapping("/api/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return clientService.getCurrentClientDTO(authentication);
    }
}
